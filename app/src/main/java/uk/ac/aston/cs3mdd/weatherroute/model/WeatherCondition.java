package uk.ac.aston.cs3mdd.weatherroute.model;
import com.google.gson.annotations.SerializedName;

public class WeatherCondition {
    @SerializedName("temp_c")
    private float tempC;

    @SerializedName("humidity")
    private int humidity;

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(int cloudiness) {
        this.cloudiness = cloudiness;
    }

    @SerializedName("wind_kph")
    private float windSpeed;

    @SerializedName("cloud")
    private int cloudiness;

    public float getTempC() {
        return tempC;
    }

    public int getHumidity() {
        return humidity;
    }

    public ConditionDetails getCondition() {
        return condition;
    }

    public void setCondition1(ConditionDetails condition) {
        this.condition = condition;
    }

    @SerializedName("condition")
    private ConditionDetails condition;

    public boolean isRaining(){
        return condition != null && condition.getConditionText().toLowerCase().contains("rain");
    }
}


