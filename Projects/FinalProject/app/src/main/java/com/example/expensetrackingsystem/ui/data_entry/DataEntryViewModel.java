package com.example.expensetrackingsystem.ui.data_entry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DataEntryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DataEntryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is data entry fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}