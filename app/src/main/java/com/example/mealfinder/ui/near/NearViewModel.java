package com.example.mealfinder.ui.near;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NearViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NearViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is near me fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}