package com.example.expensetrackingsystem.ui.reports;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DailySavingsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DailySavingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is daily savings fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}