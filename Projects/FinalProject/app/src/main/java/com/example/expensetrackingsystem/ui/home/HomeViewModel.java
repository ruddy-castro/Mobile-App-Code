package com.example.expensetrackingsystem.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mText.setValue(getUsername());
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setText(String s) { mText.setValue(s); }

    private String getUsername() {
        return currentUser.getEmail().toString();
    }
}