package com.example.audioanalysis.authentication;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.audioanalysis.R;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmailLogin;
    private EditText editTextPasswordLogin;
    private Button buttonLogin;
    private Button buttonSignUpFromLogin;

    AuthManager auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmailLogin = findViewById(R.id.editTextEmailLogin);
        editTextPasswordLogin = findViewById(R.id.editTextPasswordLogin);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignUpFromLogin = findViewById(R.id.buttonSignUpFromLogin);

        auth = new AuthManager(LoginActivity.this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmailLogin.getText().toString();
                String password = editTextPasswordLogin.getText().toString();

                // Check if both email and password fields are filled
                if (TextUtils.isEmpty(email)) {
                    editTextEmailLogin.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    editTextPasswordLogin.setError("Password is required");
                    return;
                }

                auth.loginUser(email, password);
            }
        });

        buttonSignUpFromLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}