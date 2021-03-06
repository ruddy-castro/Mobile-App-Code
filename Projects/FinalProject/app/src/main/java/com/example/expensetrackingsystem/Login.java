/*
    Assignment1
    Written by:
        Ly Do 018504783
        Ruddy Castro 026392117
        Ivy Nguyen 016618483
 */

package com.example.expensetrackingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import org.jetbrains.annotations.NotNull;

/**
 * This is a class for Login activity.
 */
public class Login extends AppCompatActivity implements View.OnClickListener {

    // Firestore
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // GUI elements
    private Button btnSignup, btnLogin;
    private EditText edtUsername, edtPassword;

    // AwesomeValidation for login check
    private AwesomeValidation awesomeValidation;

    /**
     * Hook method called when the activity is spawned
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        // Wiring with GUI
        btnSignup = findViewById(R.id.btnSignup);
        btnLogin = findViewById(R.id.btnLogin);
        edtUsername = findViewById(R.id.txtUsername);
        edtPassword = findViewById(R.id.txtPassword);

        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);

        // Add validations
        // Required field validations
        awesomeValidation.addValidation(this, R.id.txtUsername, "^(?!\\s*$).+", R.string.email_required_err_msg);
        awesomeValidation.addValidation(this, R.id.txtPassword, "^(?!\\s*$).+", R.string.password_required_err_msg);
    }

    /**
     * Verifies the user when they login.
     */
    private void userLogin() {
        String email = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    onSignedIn(email);
                } else {
                    // Add toast message
                    Toast.makeText(getApplicationContext(), "Failed to Login!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * The activity visible to the user.
     */
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            onSignedIn(currentUser.getEmail());
        }
    }

    /**
     * Check if the user signed in, if yes, they got sent to the welcome page.
     * @param email
     */
    private void onSignedIn(String email) {
        Intent welcomeIntent = new Intent(getApplicationContext(), WelcomeScreen.class);

        // Query to db to find the username of the user logging in
        db.collection("users").document(email).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            final DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                final String username = document.getString("username");
                                welcomeIntent.putExtra("username", username);
                                startActivity(welcomeIntent);
                            }
                        }
                    }
                });
    }

    /**
     * Set on click for all buttons.
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignup:
                // Transition to Signup page
                Intent signup = new Intent(getApplicationContext(), Signup.class);
                startActivity(signup);
                break;

            case R.id.btnLogin:
                // Validate using awesome validation util
                if (!awesomeValidation.validate()) {
                    System.out.println("Validation failed");
                    return;
                } else {
                    System.out.println("Validation successful");
                }
                userLogin();

                // Add toast message:
                Toast.makeText(getApplicationContext(), "Trying to login",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }
}