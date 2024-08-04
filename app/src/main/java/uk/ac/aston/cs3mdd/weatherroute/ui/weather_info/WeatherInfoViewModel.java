package uk.ac.aston.cs3mdd.weatherroute.ui.weather_info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WeatherInfoViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public WeatherInfoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is weather_info fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}