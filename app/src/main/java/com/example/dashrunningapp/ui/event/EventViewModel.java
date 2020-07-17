package com.example.dashrunningapp.ui.event;

import android.Manifest;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EventViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EventViewModel() {
        mText = new MutableLiveData<>();







        mText.setValue("This is event fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}