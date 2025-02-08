package com.example.audioanalysis.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.audioanalysis.R;

public class SignupActivity extends AppCompatActivity {
    private Button signUpButton, buttonLogInFromSignUp;
    private EditText editTextEmailSignup, editTextPasswordSignup;

    private AuthManager auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextEmailSignup = findViewById(R.id.editTextTextEmailAddress);
        editTextPasswordSignup = findViewById(R.id.editTextTextPassword);
        signUpButton = findViewById(R.id.signUpButton);
        buttonLogInFromSignUp = findViewById(R.id.buttonLoginFromSignUp);

        auth = new AuthManager(SignupActivity.this);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmailSignup.getText().toString();
                String password = editTextPasswordSignup.getText().toString();

                // Check if both email and password fields are filled
                if (TextUtils.isEmpty(email)) {
                    editTextEmailSignup.setError("Email is required");
                    return;
                }
                else if (TextUtils.isEmpty(password)) {
                    editTextPasswordSignup.setError("Password is required");
                    return;
                }
                else if (!(auth.isValidEmail(email))) {
                    Toast.makeText(SignupActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!(auth.isValidPassword(password))) {
                    Toast.makeText(SignupActivity.this, "Password must contain at least 8 characters, one uppercase letter, one digit, and one special character", Toast.LENGTH_LONG).show();
                    return;
                }
                auth.signUpUser(email, password);
            }
        });

        buttonLogInFromSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // finish() closes the current activity and prevents the user from returning to it. (Back button will not work)
            }
        });

    }
}