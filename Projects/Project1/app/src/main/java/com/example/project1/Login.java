/*
    Assignment1
    Written by:
        Arjang Fahim 098765432
        Arjang Fahim 123455678


 */

package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


public class Login extends AppCompatActivity {
    Data users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        users = new Data();

    }
}