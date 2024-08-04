package uk.ac.aston.cs3mdd.weatherroute.model;

import com.google.gson.annotations.SerializedName;


public class WeatherResponse {

    @SerializedName("location")
    private Location location;

    @SerializedName("current")
    private WeatherCondition current;

    public Location getLocation() {
        return location;
    }

    public WeatherCondition getCurrent() {
        return current;
    }
}