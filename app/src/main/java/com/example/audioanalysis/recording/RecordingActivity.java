package com.example.audioanalysis.recording;



import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.audioanalysis.Permissions.PermissionsManager;
import com.example.audioanalysis.R;


public class RecordingActivity extends AppCompatActivity {
    private final String TAG = "Recording Activity"; // used for logging

    AudioRecorderManager audioRecorderManager;
    TextView statusTextView;
    Button startRecordingButton, stopRecordingButton, pauseRecordingButton, resumeRecordingButton, playRecordingButton, uploadRecordingButton, logOutButton;

    PermissionsManager permissionsManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        permissionsManager = new PermissionsManager(this);
        permissionsManager.requestPermissions();
    }

    @Override
    protected void onStart() {
        audioRecorderManager = new AudioRecorderManager(this);
        super.onStart();

        initializeUI();
        audioRecorderManager.createRequiredDirectories(); // Create recordings and current_recordings directories if not already created.

    }

    public void initializeUI() {
        // Initialize UI components
        statusTextView = findViewById(R.id.statusTextView);
        startRecordingButton = findViewById(R.id.startRecordingButton);
        stopRecordingButton = findViewById(R.id.stopRecordingButton);
        pauseRecordingButton = findViewById(R.id.pauseRecordingButton);
        resumeRecordingButton = findViewById(R.id.resumeRecordingButton);
        playRecordingButton = findViewById(R.id.playRecordingButton);
        uploadRecordingButton = findViewById(R.id.uploadRecordingButton);
        logOutButton = findViewById(R.id.logoutButton);

        startRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "startRecordingButton clicked");
                // Create a file
                // Start the media recorder
                audioRecorderManager.startRecording();
            }
        });

        stopRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "stopRecordingButton clicked");
                // Save the file and destroy the instance/set it to null
                // Stop the media recorder and destroy it
                audioRecorderManager.stopRecording();
            }
        });

        pauseRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "pauseRecordingButton clicked");
                //
                audioRecorderManager.pauseRecording();
            }
        });

        resumeRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "resumeRecordingButton clicked");
                //
                audioRecorderManager.resumeRecording();
            }
        });

        playRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "playRecordingButton clicked");
                //
                audioRecorderManager.playRecording();
            }
        });

        uploadRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "uploadRecordingButton clicked");
                //
                audioRecorderManager.uploadRecording();
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "logOutButton clicked");
                //
                audioRecorderManager.releaseRecorder(); // Call releaseRecorder() function (or implement logout logic)
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.handlePermissionResult(requestCode, grantResults);
    }
}