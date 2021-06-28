package com.example.expensetrackingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

/**
 * This is a class for Sign-up activity.
 */
public class Signup extends AppCompatActivity {

    // GUI elements
    private EditText m_txtUsername;
    private EditText m_txtPassword;
    private EditText m_txtRetypePassword;
    private EditText m_txtEmail;
    private EditText m_txtPhone;
    private Button m_btnSignMeUp;

    // Util for validating inputs from users
    private AwesomeValidation awesomeValidation;

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
        awesomeValidation.addValidation(this, R.id.txtUsername, "^[A-Za-z\\s]*$", R.string.username_format_err_msg);
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
                
                // Extract username and password
                String username = m_txtUsername.getText().toString();
                String password = m_txtPassword.getText().toString();


                // Transition to login activity once validation is done
                Intent signup = new Intent(getApplicationContext(), Login.class);
                startActivity(signup);
            }
        });
    }
}