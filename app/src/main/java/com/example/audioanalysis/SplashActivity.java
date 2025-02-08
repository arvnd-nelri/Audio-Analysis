package com.example.audioanalysis;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.audioanalysis.authentication.AuthManager;
import com.example.audioanalysis.authentication.LoginActivity;
import com.example.audioanalysis.recording.RecordingActivity;

public class SplashActivity extends AppCompatActivity {
    private AuthManager auth;
    private Boolean UserLoggedIn = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = new AuthManager(SplashActivity.this);

        UserLoggedIn = auth.checkUser();
        if (UserLoggedIn) {
            // redirect to recordings page
            Intent intent = new Intent(SplashActivity.this, RecordingActivity.class);
            startActivity(intent);
        } else {
            // redirect to login page
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
