package uk.ac.aston.cs3mdd.weatherroute.model;

import com.google.gson.annotations.SerializedName;

public class ConditionDetails {
    @SerializedName("text")
    private String conditionText;

    @SerializedName("icon")
    private String conditionIcon;

    @SerializedName("code")
    private int conditionCode;

    public String getConditionText() {
        return conditionText;
    }

    public void setConditionText(String conditionText) {
        this.conditionText = conditionText;
    }

    public String getConditionIcon() {
        return conditionIcon;
    }

    public void setConditionIcon(String conditionIcon) {
        this.conditionIcon = conditionIcon;
    }

    public int getConditionCode() {
        return conditionCode;
    }

    public void setConditionCode(int conditionCode) {
        this.conditionCode = conditionCode;
    }
}

