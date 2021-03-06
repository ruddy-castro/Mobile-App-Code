package com.example.expensetrackingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.expensetrackingsystem.model.Settings;
import com.example.expensetrackingsystem.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

/**
 * This is a class for Sign-up activity.
 */
public class Signup extends AppCompatActivity {

    private FirebaseAuth mAuth;

    // GUI elements
    private EditText m_txtUsername;
    private EditText m_txtPassword;
    private EditText m_txtRetypePassword;
    private EditText m_txtEmail;
    private EditText m_txtPhone;
    private Button m_btnSignMeUp;

    // Util for validating inputs from users
    private AwesomeValidation awesomeValidation;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = Signup.class.getSimpleName();

    /**
     * Hook method called when the activity is spawned.
     *
     * @param savedInstanceState the saved instance statte
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call super methods
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        // Instantiate awesome validation util object
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        // Link with GUI elements
        m_txtUsername = findViewById(R.id.txtUsername);
        m_txtPassword = findViewById(R.id.txtPassword);
        m_txtRetypePassword = findViewById(R.id.txtRetypePassword);
        m_txtEmail = findViewById(R.id.txtEmail);
        m_txtPhone = findViewById(R.id.txtPhone);
        m_btnSignMeUp = findViewById(R.id.btnSignMeUp);

        // Add validations
        // Required field validations
        awesomeValidation.addValidation(this, R.id.txtUsername, "^(?!\\s*$).+", R.string.username_required_err_msg);
        awesomeValidation.addValidation(this, R.id.txtPassword, "^(?!\\s*$).+", R.string.password_required_err_msg);
        awesomeValidation.addValidation(this, R.id.txtRetypePassword, "^(?!\\s*$).+", R.string.retypepassword_required_err_msg);
        awesomeValidation.addValidation(this, R.id.txtEmail, "^(?!\\s*$).+", R.string.email_required_err_msg);
        awesomeValidation.addValidation(this, R.id.txtPhone, "^(?!\\s*$).+", R.string.phone_required_err_msg);

        // Other validations
        awesomeValidation.addValidation(this, R.id.txtUsername, "^[A-Za-z\\\\s]*$", R.string.username_format_err_msg);
        awesomeValidation.addValidation(this, R.id.txtEmail, Patterns.EMAIL_ADDRESS, R.string.email_format_err_msg);
        awesomeValidation.addValidation(this, R.id.txtPhone, Patterns.PHONE, R.string.phone_format_err_msg);

        // Add listener to the sign-me-up button
        m_btnSignMeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate using awesome validation util
                if (!awesomeValidation.validate()) {
                    System.out.println("Validation failed");
                    return;
                } else {
                    System.out.println("Validation successful");
                }

                // Extract email and password
                String username = m_txtUsername.getText().toString().trim();
                String email = m_txtEmail.getText().toString().trim();
                String password = m_txtPassword.getText().toString().trim();
                String phone = m_txtPhone.getText().toString().trim();

                // Add user to the auth database and to the users collection
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final User user = User.builder()
                                    .email(email)
                                    .username(username)
                                    .build();

                            // Add new user to DB
                            db.collection("users").document(email).set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "DocumentSnapshot added with ID: " + email);
                                            Toast.makeText(Signup.this, "User has been registered successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                            Toast.makeText(Signup.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            // Add new settings for the user to DB
                            db.collection("settings").document(email).set(Settings.builder().build())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "DocumentSnapshot added with ID: " + email);
                                            Toast.makeText(Signup.this, "User has been registered successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                            Toast.makeText(Signup.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            // Transition to login activity once validation is done
                            Intent signup = new Intent(getApplicationContext(), Login.class);
                            startActivity(signup);
                        } else {
                            Toast.makeText(Signup.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Failed to register", task.getException());
                        }
                    }
                });

            }
        });
    }
}

// TODO: make sure users are unique
// TODO: Check if password and retyped password are matched