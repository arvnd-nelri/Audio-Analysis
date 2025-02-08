package com.example.audioanalysis.Permissions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsManager {

    private final Context context;
    private final Activity activity;

    // Constants to identify permission request results
    public static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;
    public static final int REQUEST_READ_PHONE_STATE_PERMISSION = 2;

    // Constructor initializes context and activity references
    public PermissionsManager(Activity activity) {
        this.context = activity.getApplicationContext();
        this.activity = activity;
    }

    /**
     * Check both audio and phone state permissions during app initialization.
     * If permissions are not granted, request them.
     */

    public void checkAllPermissions() {
        boolean audioPermissionGranted = isPermissionGranted(android.Manifest.permission.RECORD_AUDIO);
        boolean phonePermissionGranted = isPermissionGranted(android.Manifest.permission.READ_PHONE_STATE);

        if (!audioPermissionGranted) {
            requestAudioPermission(); // Request audio permission if not granted
        }

        if (!phonePermissionGranted) {
            requestPhoneStatePermission(); // Request phone state permission if not granted
        }
    }

    /**
     * Request permission to record audio.
     */
    private void requestAudioPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
    }

    /**
     * Request permission to read phone state.
     */
    private void requestPhoneStatePermission() {
        ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE_PERMISSION);
    }

    /**
     * Checks if a specific permission is granted.
     *
     * @param permission The permission to check.
     * @return true if the permission is granted, false otherwise.
     */
    private boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Handle the result of permission requests.
     *
     * @param requestCode  The request code identifying the permission request.
     * @param permissions  The requested permissions.
     * @param grantResults The results for the requested permissions.
     */
    public void handlePermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 0) {
            // Handle case where the permission dialog was dismissed without granting permission
            return;
        }

        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            handleAudioPermissionResult(grantResults);
        } else if (requestCode == REQUEST_READ_PHONE_STATE_PERMISSION) {
            handlePhoneStatePermissionResult(grantResults);
        }
    }

    /**
     * Handle the result of the audio permission request.
     *
     * @param grantResults The result of the audio permission request.
     */
    private void handleAudioPermissionResult(int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted
            Toast.makeText(context, "Audio Permission Granted!", Toast.LENGTH_SHORT).show();
        } else {
            // Permission denied, possibly with "Don't ask again"
            handlePermissionDenial(android.Manifest.permission.RECORD_AUDIO, "Microphone access is required to record audio. Please enable it in settings.");
        }
    }

    /**
     * Handle the result of the phone state permission request.
     *
     * @param grantResults The result of the phone state permission request.
     */
    private void handlePhoneStatePermissionResult(int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted
            Toast.makeText(context, "Phone State Permission Granted!", Toast.LENGTH_SHORT).show();
        } else {
            // Permission denied, possibly with "Don't ask again"
            handlePermissionDenial(android.Manifest.permission.READ_PHONE_STATE, "Phone state permission is required to detect calls. Please enable it in settings.");
        }
    }

    /**
     * Handle permission denial, including the case when the user selects "Don't ask again".
     *
     * @param permission The denied permission.
     * @param message    The message to show the user if the permission is permanently denied.
     */
    private void handlePermissionDenial(String permission, String message) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            // User selected "Don't ask again"—show dialog to redirect to app settings
            showPermissionDeniedDialog(message);
        } else {
            // Show a simple toast when permission is denied without "Don't ask again"
            Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Show a dialog directing the user to app settings if they denied the permission with "Don't ask again".
     *
     * @param message The message to show in the dialog.
     */
    private void showPermissionDeniedDialog(String message) {
        new AlertDialog.Builder(activity)
                .setTitle("Permission Required")
                .setMessage(message)
                .setPositiveButton("Open Settings", (dialog, which) -> {
                    // Open the app settings screen to allow the user to manually grant permission
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + context.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
