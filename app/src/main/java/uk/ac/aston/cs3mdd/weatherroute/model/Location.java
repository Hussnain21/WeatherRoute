package uk.ac.aston.cs3mdd.weatherroute.model;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("name")
    private String name;

    @SerializedName("region")
    private String region;

    @SerializedName("country")
    private String country;

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

}
