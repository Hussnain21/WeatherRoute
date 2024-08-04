package uk.ac.aston.cs3mdd.weatherroute.ui.weather_info;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.ac.aston.cs3mdd.weatherroute.databinding.FragmentWeatherInfoBinding;
import uk.ac.aston.cs3mdd.weatherroute.model.Location;
import uk.ac.aston.cs3mdd.weatherroute.model.WeatherCondition;
import uk.ac.aston.cs3mdd.weatherroute.model.WeatherResponse;
import uk.ac.aston.cs3mdd.weatherroute.service.WeatherApi;

public class WeatherInfoFragment extends Fragment {
    private String temperature;
    private String humidity;
    private String windspeed;
    private String cloudiness;
    private String region;
    private TextView temperatureTextView;
    private TextView humidityTextView;
    private TextView windSpeedTextView;
    private TextView cloudinessTextView;
    private TextView regionTextView;
    private TextView rainTextView;
    private FragmentWeatherInfoBinding binding;
    private WeatherCondition currentCondition;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        WeatherInfoViewModel weatherInfoViewModel = new ViewModelProvider(this).get(WeatherInfoViewModel.class);

        binding = FragmentWeatherInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        EditText locationEditText = binding.editTextLocation;
        Button fetchWeatherButton = binding.buttonFetchWeather;
        Button checkRainingButton = binding.seeRecomendations;
        rainTextView = binding.rainTextView;

        locationEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                fetchWeatherData(locationEditText.getText().toString());
                hideKeyboard();
            }
            return true;
        });

        fetchWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchWeatherData(locationEditText.getText().toString());
                hideKeyboard();
            }
        });

        checkRainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (currentCondition != null) {
                boolean isRaining = currentCondition.isRaining();
                String raining = "• Movie at Cinema " + "\n" +
                        "• Visit a Museum" + "\n" +
                        "• Visit an Arcade or" + "\n" +
                        "• Read a Book or " + "\n" +
                        "• Do some Cooking " + "\n";

                String notRaining = "• Go for Photography or " + "\n" +
                        "• Go for Cycling/Biking or " + "\n" +
                        "• Go play Outdoor Sports" + "\n" +
                        "• Go for Picnic in The park " + "\n" +
                        "• Go for Hiking  " + "\n";

                if (isRaining) {
                    raining += "\nBecause it is currently raining at this location.";
                } else {
                    notRaining += "\nBecause it is not raining at this location.";
                }

                displayWeatherInfoDialog(raining, notRaining);
            } else {

                Log.e("WeatherInfo", "No weather location found!");
                showWeatherInfoExceptionDialog();
            }
        }
        });
        return root;
    }

    //method to show the popup containing rain information and activities information
    private void displayWeatherInfoDialog(String raining, String notRaining) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Activity Recommendations");

        boolean isRaining = currentCondition.isRaining();

        if (isRaining) {
            builder.setMessage(raining);
        } else {
            builder.setMessage(notRaining);
        }

        builder.setPositiveButton("OK", (dialog, which) -> {

            dialog.dismiss();
        });

        builder.show();
    }
    
    //method to fetch weather data from the api
    private void fetchWeatherData(String city) {
        String apiKey = "8d00ff9e48514827b96220459231811";
        String includeAqi = "no";

        final String BASE_URL = "https://api.weatherapi.com/v1/";
        WeatherApi weatherApi;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherApi = retrofit.create(WeatherApi.class);

        Call<WeatherResponse> call = weatherApi.getCurrentWeather(apiKey, city, includeAqi);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    if (weatherResponse != null) {
                        currentCondition = weatherResponse.getCurrent();
                        updateUIWithData(weatherResponse);
                        Log.d("Weatherinfo", "Successfully fetched weather data");
                    }
                } else {
                    Log.e("WeatherInfo", "Failed to fetch weather data. " + response.code());
                }
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("WeatherInfo", "Failed to fetch weather data. Error: " + t.getMessage());
            }
        });
    }
    
    //method to show an exception when the user clicks on recommendation button with getting the weather information
    private void showWeatherInfoExceptionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Recommendations Unavailable");
        builder.setMessage("Please search for weather information first.");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
    
    //method to update data when another city is searched for
    private void updateUIWithData(WeatherResponse weatherResponse) {
        WeatherCondition condition = weatherResponse.getCurrent();
        Location location = weatherResponse.getLocation();

        if (condition != null && location != null) {
            temperature = String.valueOf(condition.getTempC());
            humidity = String.valueOf(condition.getHumidity());
            windspeed = String.valueOf(condition.getWindSpeed());
            cloudiness = String.valueOf(condition.getCloudiness());
            region = String.valueOf(location.getRegion());

            temperatureTextView = binding.temperatureTextView;
            humidityTextView = binding.humidityTextView;
            windSpeedTextView = binding.windSpeedTextView;
            cloudinessTextView = binding.cloudinessTextView;
            regionTextView = binding.regionTextView;

            if (temperature != null && humidity != null) {
                temperatureTextView.setText("Temperature: " + temperature + " C°");
                humidityTextView.setText("Humidity: " + humidity + " %");
                windSpeedTextView.setText("Wind Speed: " + windspeed + " km/h");
                cloudinessTextView.setText("Cloudiness: " + cloudiness + " %");
                regionTextView.setText("Region:  " + region + "");

                ImageView weatherIconImageView = binding.weatherIconImageView;
                String weatherIconUrl = "https:" + condition.getCondition().getConditionIcon();
                Picasso.get().load(weatherIconUrl).into(weatherIconImageView);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EditText locationEditText = binding.editTextLocation;
        locationEditText.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
    }
}
