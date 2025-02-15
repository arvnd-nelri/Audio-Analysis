//package com.example.audioanalysis.Permissions;

//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.content.pm.PackageManager;
//
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//public class PermissionsManager {
//    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 100;  // Request code for audio permission
//    private boolean permissionToRecordAudio = false;  // Flag to track permission status
//
//
//    private static final int REQUEST_READ_PHONE_STATE_PERMISSION = 200;
//    private boolean permissionToReadPhoneState = false;
//
//    Context context;
//    Activity activity;
//    public PermissionsManager(Activity activity) {
//        this.activity = activity;
//        this.context = activity.getApplicationContext();
//    }
//    public boolean isPermissionGranted(String permission) {
//        /*
//        Checks if a specific permission is granted.
//        @param permission: The permission to check.
//        @return true if granted, false otherwise.
//        */
//        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    public void requestAudioPermissionIfNeeded() {
//        /*
//        Checks if phone state permission is granted. If not, requests permission.
//        */
//        permissionToRecordAudio = isPermissionGranted(Manifest.permission.RECORD_AUDIO);
//        if (!permissionToRecordAudio) {
//            // Permission not granted, so ask for permission
//            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
//        }
//    }
//    public void requestPhoneStatePermissionIfNeeded() {
//        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE_PERMISSION);
//    }
//    public int getAudioPermissionRequestCode() {
//        return REQUEST_RECORD_AUDIO_PERMISSION;
//    }
//
//    public int getPhoneStatePermissionRequestCode() {
//        return REQUEST_READ_PHONE_STATE_PERMISSION;
//    }
//}

package com.example.audioanalysis.Permissions;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionsManager{
    private final Activity activity;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String[] REQUIRED_PERMISSIONS = { // Permissions that I use
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.READ_PHONE_STATE
    };
    public PermissionsManager(Activity activity) {
        this.activity = activity;
    }


    public List<String> getMissingPermissions() { // Check for missing permissions
        List<String> missingPermissions = new ArrayList<>(); // Created a arraylist for storing missing permissions
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission); // add those missing permissions
            }
        }
        return missingPermissions;
    }

    public void requestPermissions() { // Check if there any missing permissions and request them
        List<String> missingPermissions = getMissingPermissions();

        if (!missingPermissions.isEmpty()) {
            // Request the missing permissions
            ActivityCompat.requestPermissions(
                    activity,
                    missingPermissions.toArray(new String[0]),
                    PERMISSION_REQUEST_CODE
            );
        }
    }

    // Handling permissiong result: first check if all granted or not, and if not granted: call showPermissionDeniedMessage() which redirects to settings
    public void handlePermissionResult(int requestCode, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (!allPermissionsGranted) {
                // Permissions were denied; show a prompt for settings later
                showPermissionDeniedMessage();
            }
        }
    }

    private void showPermissionDeniedMessage() {
        new android.app.AlertDialog.Builder(activity)
                .setTitle("Permissions Required")
                .setMessage("Some required permissions were denied. Please enable them in app settings.")
                .setPositiveButton("Go to Settings", (dialog, which) -> openAppSettings())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    private void openAppSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

}