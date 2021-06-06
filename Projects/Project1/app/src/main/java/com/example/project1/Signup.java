package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

public class Signup extends AppCompatActivity {

    private EditText m_txtUsername;
    private EditText m_txtPassword;
    private EditText m_txtRetypePassword;
    private EditText m_txtEmail;
    private EditText m_txtPhone;
    private Button m_btnSignMeUp;

    private AwesomeValidation awesomeValidation;

    private Data data = Data.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        // Link with GUI
        m_txtUsername = findViewById(R.id.txtUsername);
        m_txtPassword = findViewById(R.id.txtPassword);
        m_txtRetypePassword = findViewById(R.id.txtRetypePassword);
        m_txtEmail = findViewById(R.id.txtEmail);
        m_txtPhone = findViewById(R.id.txtPhone);
        m_btnSignMeUp = findViewById(R.id.btnSignMeUp);

        // Validations
        // Required fields
        awesomeValidation.addValidation(this, R.id.txtUsername, "^(?!\\s*$).+", R.string.username_required_err_msg);
        awesomeValidation.addValidation(this, R.id.txtPassword, "^(?!\\s*$).+", R.string.password_required_err_msg);
        awesomeValidation.addValidation(this, R.id.txtRetypePassword, "^(?!\\s*$).+", R.string.retypepassword_required_err_msg);
        awesomeValidation.addValidation(this, R.id.txtEmail, "^(?!\\s*$).+", R.string.email_required_err_msg);
        awesomeValidation.addValidation(this, R.id.txtPhone, "^(?!\\s*$).+", R.string.phone_required_err_msg);

        // Other validations
        awesomeValidation.addValidation(this, R.id.txtUsername, "^[A-Za-z\\s]*$", R.string.username_format_err_msg);
        awesomeValidation.addValidation(this, R.id.txtEmail, Patterns.EMAIL_ADDRESS, R.string.email_format_err_msg);
        awesomeValidation.addValidation(this, R.id.txtPhone, Patterns.PHONE, R.string.phone_format_err_msg);

        // Add listener to sign up button
        m_btnSignMeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!awesomeValidation.validate()) {
                    System.out.println("Validation failed");
                    return;
                } else {
                    System.out.println("Validation successful");
                }

                // Validation
                if (data.CheckUsername(m_txtUsername.getText().toString())) {
                    System.out.printf("Username '%s' already exists\n", m_txtUsername.getText().toString());
                    Toast.makeText(getApplicationContext(), R.string.already_exist_username_err_msg, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!m_txtPassword.getText().toString().equals(m_txtRetypePassword.getText().toString())) {
                    System.out.println("Password and retype password are not matched");
                    Toast.makeText(getApplicationContext(), R.string.different_passwords_err_msg, Toast.LENGTH_SHORT).show();
                    return;
                }



            }
        });
    }
}