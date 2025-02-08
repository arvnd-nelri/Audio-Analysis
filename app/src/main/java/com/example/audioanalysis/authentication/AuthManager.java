package com.example.audioanalysis.authentication;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.audioanalysis.recording.RecordingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;


public class AuthManager {
    public static final String TAG = "AuthManager"; // Used for logs
    private static AuthManager instance; // Singleton instance of AuthManager class, ensures that only one instance of AuthManager exists throughout the application.
    private FirebaseAuth mAuth; // Firebase Authentication instance for handling user authentication: sing in, sing out etc..
    private Context context; // Context for toast

    private Boolean isSignUpUser; // for sending Email toast message

    public AuthManager (Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
    }

    public boolean checkUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Return true if the user is logged in and email is verified
        return currentUser != null && currentUser.isEmailVerified();
    }

    public void loginUser(String email, String password) {
        isSignUpUser = false;
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            // redirect to home page
                            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, RecordingActivity.class);
                            context.startActivity(intent);
                        } else {
                            // resend email link
                            sendEmailVerification(user, isSignUpUser);
                            // internally sign out
                            mAuth.signOut(); // Couldn't use logOutUser Function here, as it also print toast message
                        }
                    } else {
                        // Send error message: unable to login currently..
                        Toast.makeText(context,"Unable to login right now" , Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void signUpUser (String email, String password) {

        isSignUpUser = true;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Send email verification link
                            sendEmailVerification(user, isSignUpUser);
                            // Navigate back to login activity
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "User creation failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Sign-up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void logOutUser () {
        mAuth.signOut(); // signing out
        Toast.makeText(context, "Logout Successful", Toast.LENGTH_SHORT).show();
        // redirect to login page
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public void sendEmailVerification (FirebaseUser user, Boolean isSignUp) {
        if (user != null) {
            // Send email verification link
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (isSignUp) {
                                Toast.makeText(context, "Verification email sent. Please check your inbox.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Verification email resent. Please check your inbox.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Un able to verify the email", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(context, "User is not logged in, Failed to send email.", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();    //matches() returns true if email pattern matches with regular expression, else false.
    }

    // Method to validate password format using regular expression
    /*
     * This pattern ensures the following:
     * (?=.*[A-Z]): At least one uppercase letter.
     * (?=.*[a-z]): At least one lowercase letter.
     * (?=.*\d): At least one digit.
     * (?=.*[@$!%*?&]): At least one special character (e.g., @, $, !, etc.).
     * {8,}: The password must be at least 8 characters long.
     * */
    // Method to validate password format using regular expression
    public boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordPattern);
    }
}