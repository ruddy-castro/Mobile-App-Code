package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Welcome is a class representing the welcome screen after a user signs into the application.
 */
public class Welcome extends AppCompatActivity {
    TextView welcome;

    /**
     * A function that initializes the activity with the proper welcome screen.
     * @param savedInstanceState String keys representing the saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Calling onCreate of parent class and then inflating the view with activity_welcome layout
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