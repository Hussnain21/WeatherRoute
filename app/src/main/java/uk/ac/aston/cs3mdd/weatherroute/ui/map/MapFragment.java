package uk.ac.aston.cs3mdd.weatherroute.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.ac.aston.cs3mdd.weatherroute.R;
import uk.ac.aston.cs3mdd.weatherroute.databinding.FragmentMapBinding;
import uk.ac.aston.cs3mdd.weatherroute.model.WeatherResponse;
import uk.ac.aston.cs3mdd.weatherroute.service.WeatherApi;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private boolean isMapReady = false;
    private GoogleMap map;
    private SearchView searchBarView;
    private FragmentMapBinding binding;
    private MapView mapView;
    private LatLng currentLatLng;
    private String temperature;
    private String humidity;
    private String windspeed;
    private String cloudiness;
    private String region;
    private Polyline polyline;
    private Marker myLocationMarker;
    private Marker searchedLocationMarker;
    private FusedLocationProviderClient fusedClient;
    private Button switchButton;
    private int defaultMapType = GoogleMap.MAP_TYPE_NORMAL;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        switchButton = root.findViewById(R.id.switchButton);
        switchButton.setOnClickListener(v -> switchMapType());

        searchBarView = binding.searchBar;
        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        searchBarView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchBarView.getQuery().toString();
                List<Address> listAddress = null;

                if (location != null && !location.isEmpty()) {
                    Geocoder geo = new Geocoder(requireContext());

                    try {
                        listAddress = geo.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (listAddress != null && !listAddress.isEmpty()) {
                        Address address = listAddress.get(0);
                        LatLng searchedLatLng = new LatLng(address.getLatitude(), address.getLongitude());

                        if (map != null) {
                            removeMarkersAndPolyline();
                            searchedLocationMarker = map.addMarker(new MarkerOptions().position(searchedLatLng).title("Weather Info of " + location));

                            if (isLocationPermissionGranted()) {
                                if (currentLatLng != null) {
                                    myLocationMarker = map.addMarker(new MarkerOptions().position(currentLatLng).title("Your Location"));

                                    PolylineOptions polylineOptions = new PolylineOptions()
                                            .add(currentLatLng, searchedLatLng)
                                            .width(5)
                                            .color(Color.RED);
                                    polyline = map.addPolyline(polylineOptions);
                                }
                            }
                        }
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(searchedLatLng, 12);
                        map.animateCamera(cameraUpdate);
                        fetchWeatherInfoForCity(location);
                        hideKeyboard();
                    }
                }
                return true;
            }
            private boolean isLocationPermissionGranted() {
                return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            }

            //method to remove previous polyline and markers when new ones are placed
            private void removeMarkersAndPolyline() {
                if (myLocationMarker != null) {
                    myLocationMarker.remove();
                }
                if (searchedLocationMarker != null) {
                    searchedLocationMarker.remove();
                }
                if (polyline != null) {
                    polyline.remove();
                }
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        requestLocationPermission();
        getCurrentLocation();
        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        return root;
    }

    //method to switch between different maps and it is linked to one button only
    private void switchMapType() {
        switch (defaultMapType) {
            case GoogleMap.MAP_TYPE_NORMAL:
                defaultMapType = GoogleMap.MAP_TYPE_HYBRID;
                break;
            case GoogleMap.MAP_TYPE_HYBRID:
                defaultMapType = GoogleMap.MAP_TYPE_TERRAIN;
                break;
            case GoogleMap.MAP_TYPE_TERRAIN:
                defaultMapType = GoogleMap.MAP_TYPE_NORMAL;
                break;
            default:
                defaultMapType = GoogleMap.MAP_TYPE_NORMAL;
                break;
        }
        if (map != null) {
            map.setMapType(defaultMapType);
        }
    }

    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
                if (permissions.containsKey(Manifest.permission.ACCESS_FINE_LOCATION) &&
                        permissions.get(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    getCurrentLocation();
                } else {
                    Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    private void requestLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        requestPermissionLauncher.launch(permissions);
    }

    //method to get the current location of the user
    private void getCurrentLocation() {
        if (isMapReady && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            fusedClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), location -> {
                        if (location != null) {
                            LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            if (currentLatLng != null) {
                                Log.d("MapFragment", "Previous currentLatLng: " + currentLatLng.latitude + ", " + currentLatLng.longitude);
                            }
                            currentLatLng = myLatLng;
                            Log.d("MapFragment", "New currentLatLng: " + currentLatLng.latitude + ", " + currentLatLng.longitude);
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 12));
                        }
                    })
                    .addOnFailureListener(requireActivity(), e -> {
                        Log.e("MapFragment", "Failed to get location: " + e.getMessage());
                    });
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        isMapReady = true;
        getCurrentLocation();
    }

    //method to fetch the data from the api to be able to show it on the marker
    private void fetchWeatherInfoForCity(String city) {
        String apiKey = "8d00ff9e48514827b96220459231811";
        String includeAqi = "no";

        String BASE_URL = "https://api.weatherapi.com/v1/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi weatherApi = retrofit.create(WeatherApi.class);

        Call<WeatherResponse> call = weatherApi.getCurrentWeather(apiKey, city, includeAqi);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    if (weatherResponse != null) {

                        temperature = String.valueOf(weatherResponse.getCurrent().getTempC());
                        humidity = String.valueOf(weatherResponse.getCurrent().getHumidity());
                        windspeed = String.valueOf(weatherResponse.getCurrent().getWindSpeed());
                        cloudiness = String.valueOf(weatherResponse.getCurrent().getCloudiness());
                        region = weatherResponse.getLocation().getRegion();
                        updateWeatherInfo(temperature, humidity, windspeed, cloudiness, region);
                    }
                } else {
                    Log.e("WeatherInfo", "Failed to fetch weather data. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                Log.e("WeatherInfo", "Failed to fetch weather data. Error: " + t.getMessage());
            }
        });
    }

    //method to update weather information, this is for when the new search is made
    public void updateWeatherInfo(String temperature, String humidity, String windspeed, String cloudiness, String region) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.windspeed = windspeed;
        this.cloudiness = cloudiness;
        this.region = region;

        if (map != null && currentLatLng != null) {
            if (temperature != null && humidity != null && windspeed != null && cloudiness != null && region != null) {
                String weatherInfo = "Temperature: " + temperature + ", Region: " + region;
                LatLng markerLatLng = new LatLng(currentLatLng.latitude, currentLatLng.longitude);
                if (searchedLocationMarker != null) {
                    searchedLocationMarker.setSnippet(weatherInfo);
                } else {
                    searchedLocationMarker = map.addMarker(new MarkerOptions().position(markerLatLng).title("Your Location").snippet(weatherInfo));
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
    }
}