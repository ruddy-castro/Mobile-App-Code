package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Welcome extends AppCompatActivity {
    TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Wiring with GUI
        welcome = findViewById(R.id.txtWelcome);
        //Output username from login intent
        //Get the intent:
        Intent intent = getIntent();
        String username = intent.getStringExtra("Username");
        //Show username in textview:
        welcome.setText("Welcome " + username + " !");
    }
}