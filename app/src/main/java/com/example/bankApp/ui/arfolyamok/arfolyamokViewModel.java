package com.example.bankApp.ui.arfolyamok;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class arfolyamokViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public arfolyamokViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}