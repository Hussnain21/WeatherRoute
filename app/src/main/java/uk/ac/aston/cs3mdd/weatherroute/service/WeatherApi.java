package uk.ac.aston.cs3mdd.weatherroute.service;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import uk.ac.aston.cs3mdd.weatherroute.model.WeatherResponse;

public interface WeatherApi {
    @GET("current.json")
    Call<WeatherResponse> getCurrentWeather(
            @Query("key") String apiKey,
            @Query("q") String city,
            @Query("aqi") String includeAqi
    );
}