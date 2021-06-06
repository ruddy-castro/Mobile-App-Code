/*
    Assignment1
    Written by:
        Ly Do
        Ruddy Castro
        Ivy Nguyen
 */

package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


public class Login extends AppCompatActivity {
    private Data data = Data.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}