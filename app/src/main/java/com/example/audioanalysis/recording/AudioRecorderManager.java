package com.example.audioanalysis.recording;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class AudioRecorderManager {

    private final Activity activity;
    private final Context context;

    private String fileName;    // Name of the file that is getting created
    private File recordingFile; // File Object for audio file
    private File currentRecordingsDir;  // Using File class to create directories, currentRecordingDir: stores the recording files that are currently got recorded
    private File recordingsDir; // Stores all the recordings temporarily till they get uploaded to cloud
    private MediaRecorder mediaRecorder;    // Dealing with audio
    private MediaPlayer mediaPlayer;

    public boolean isRecorderSetup = false; // This will be true once the MediaRecorder is initialized for recording and false when it is released. It prevents reinitialization while an active MediaRecorder exists.

    public AudioRecorderManager(Activity activity){
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }
    private static final String TAG = "AudioRecordingManager";
    // Handling MediaRecorder logic

    public void createRequiredDirectories() {
        Log.i(TAG, "createRequiredDirectories: This function is running");
        // Create recording directories to store recordings temporarily
        recordingsDir = new File(context.getFilesDir(), "recordings");
        if (!recordingsDir.exists()) {
            Log.i(TAG, "createRequiredDirectories: Created recordings directory");
            recordingsDir.mkdirs(); // Create the directory if it doesn't exist
        }

        // Create Current Recording directory, to store all the recordings of current call and if needed later do merge operations on the audio files in this directory
        currentRecordingsDir = new File(context.getFilesDir(), "current_recordings");
        if (!currentRecordingsDir.exists()) {
            Log.i(TAG, "createRequiredDirectories: Created current recordings directory");
            currentRecordingsDir.mkdirs(); // // Create the directory if it doesn't exist
        }
    }


    public void startRecording() {
        Log.i(TAG, "startRecording: StartRecording() running");

        try {
            if (!isRecorderSetup) {
                // Generate a unique file name with a timestamp
                fileName = "Recording_" + System.currentTimeMillis() + ".3gp";

                // Full path of the recording file
                recordingFile = new File(recordingsDir, fileName);

                // Initialize and configure MediaRecorder
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mediaRecorder.setOutputFile(recordingFile.getAbsolutePath());

                // Prepare and start recording
                mediaRecorder.prepare();
                mediaRecorder.start();
                Toast.makeText(context, "Recording started!", Toast.LENGTH_SHORT).show();

                isRecorderSetup = true; // Prevent duplicate recordings
            } else {
                Toast.makeText(context, "Recording is already in progress!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Failed to start recording: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    public void stopRecording() {
        Log.i(TAG, "stopRecording: StopRecording() running");
        // make isRecording variable to false
        try {
            if (isRecorderSetup && mediaRecorder != null) {
                mediaRecorder.stop();   // Stop recording
                mediaRecorder.release(); // release resources (microphone)
                mediaRecorder = null;   // Release the MediaRecorder object

                isRecorderSetup = false;    // Set the flag to false to prevent duplicate recordings

                Toast.makeText(context, "Recording stopped!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "No active recording to stop!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Failed to stop recording: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            isRecorderSetup = false; // Reset flag regardless of success or failure
        }
    }

    public void pauseRecording() {
        Log.i(TAG, "pauseRecording: PauseRecording() running");
    }

    public void resumeRecording() {
        Log.i(TAG, "resumeRecording: ResumeRecording() running");
    }

    public void playRecording() {
        Log.i(TAG, "playRecording: PlayRecording() running");

    }

    public void uploadRecording() {
        Log.i(TAG, "uploadRecording: UploadRecording() running");
    }

    public void releaseRecorder() {
        Log.i(TAG, "releaseRecorder: ReleaseRecorder() running");
    }

}
