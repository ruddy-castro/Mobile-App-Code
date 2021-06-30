package com.example.expensetrackingsystem.ui.reports;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DailyExpensesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DailyExpensesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is daily expenses fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}