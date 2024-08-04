package uk.ac.aston.cs3mdd.weatherroute.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> endPointLiveData = new MutableLiveData<>();

    // Method to set the endpoint data
    public void setEndPoint(String endPoint) {
        endPointLiveData.setValue(endPoint);
    }

    // Method to observe changes in the endpoint data
    public LiveData<String> getEndPoint() {
        return endPointLiveData;
    }
}