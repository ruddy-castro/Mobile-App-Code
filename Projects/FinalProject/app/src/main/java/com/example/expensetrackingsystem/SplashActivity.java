package com.example.expensetrackingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_splash);

        int SPLASH_TIME_OUT = 2750;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, Login.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
