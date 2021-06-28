package com.example.expensetrackingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Welcome is a class representing the welcome screen after a user signs into the application.
 */
public class Welcome extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        String username = intent.getStringExtra("username");
        welcome.setText("Welcome " + username + "!");
    }
}