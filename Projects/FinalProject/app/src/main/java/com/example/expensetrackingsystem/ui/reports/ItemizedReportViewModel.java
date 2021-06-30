package com.example.expensetrackingsystem.ui.reports;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemizedReportViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ItemizedReportViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is itemized report fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}