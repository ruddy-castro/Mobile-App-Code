/*
    Assignment1
    Written by:
        Ly Do 018504783
        Ruddy Castro 026392117
        Ivy Nguyen 016618483
 */

package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This is a class for Login activity.
 */
public class Login extends AppCompatActivity {

    // Util for validating inputs from users
    private Data data = Data.getInstance();

    // GUI elements
    private Button btnSignup, btnLogin;
    private EditText edtUsername, edtPassword;

    /**
     * Hook method called when the activity is spawned
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Wiring with GUI
        btnSignup = findViewById(R.id.btnSignup);
        btnLogin = findViewById(R.id.btnLogin);
        edtUsername = findViewById(R.id.txtUsername);
        edtPassword = findViewById(R.id.txtPassword);

        /**
         * Events that will happen when the user clicks btnLogin button
         */
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user inputs:
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                // Add toast message:
                Toast.makeText(getApplicationContext(), "Trying to login",
                        Toast.LENGTH_SHORT).show();

                // Check login credentials by using checkCredentials method
                if (data.CheckCredentials(username, password)) {
                    // Successfully logged in: User will be sent to Welcome page with username
                    Intent login = new Intent(getApplicationContext(), Welcome.class);
                    login.putExtra("Username", username);
                    startActivity(login);

                    // Add toast message:
                    Toast.makeText(getApplicationContext(), "Successfully logged in",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Login failed:
                    Toast.makeText(getApplicationContext(),
                            "Username/Password Combination Incorrect!", Toast.LENGTH_SHORT).show();

                    // blank the password field:
                    edtPassword.getText().clear();
                }
            }
        });

        /**
         * Events that will happen when the user clicks btnSignup button
         */
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Transition to Signup page
                Intent signup = new Intent(getApplicationContext(), Signup.class);
                startActivity(signup);
            }
        });
    }
}