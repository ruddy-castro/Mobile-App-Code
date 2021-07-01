package com.example.expensetrackingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

/**
 * The class to take care of the splash screen in the beginning.
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_splash);

        // The time to delay in milliseconds
        int SPLASH_TIME_OUT = 2750;

        // Use handler to execute code after a delayed amount of time
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start login activity
                Intent i = new Intent(SplashActivity.this, Login.class);
                startActivity(i);
                // Close splash activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
