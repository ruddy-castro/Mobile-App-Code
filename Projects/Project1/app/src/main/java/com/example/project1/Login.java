/*
    Assignment1
    Written by:
        Ly Do
        Ruddy Castro
        Ivy Nguyen
 */

package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends AppCompatActivity {
    private Data data = Data.getInstance();
    private Button btnSignup, btnLogin;
    private EditText edtUsername, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Wiring with GUI
        btnSignup = findViewById(R.id.btnSignup);
        btnLogin = findViewById(R.id.btnLogin);
        edtUsername = findViewById(R.id.txtUsername);
        edtPassword = findViewById(R.id.txtPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check login credentials

                // TODO: login successful: if()
                String username = edtUsername.getText().toString();
                Intent login = new Intent(getApplicationContext(), Welcome.class);
                login.putExtra("Username", username);
                startActivity(login);

                // TODO: login unsuccessful: else()
                Toast.makeText(getApplicationContext(),
                        "Username/Password Combination Incorrect!", Toast.LENGTH_SHORT).show();
                edtPassword.getText().clear();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: validate and store data

                // Transition to login activity after all data has been validated and stored
                Intent signup = new Intent(getApplicationContext(), Login.class);
                startActivity(signup);
            }
        });
    }
}