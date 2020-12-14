package com.example.wecompete.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wecompete.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    public EditText emailId, password;
    public Button btnSignIn;
    public TextView tvSignUp;
    public FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        btnSignIn = findViewById(R.id.signInButton);
        tvSignUp = findViewById(R.id.signUpTextView);

        mAuthStateListener = firebaseAuth -> {
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
            if (mFirebaseUser != null) {
                Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                Intent loginSuccessful = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(loginSuccessful);
            }
            else {
                Toast.makeText(LoginActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
            }
        };

        btnSignIn.setOnClickListener(v -> {
            String email = emailId.getText().toString();
            String pwd = password.getText().toString();
            if (email.isEmpty()) {
                emailId.setError("Please enter email");
                emailId.requestFocus();
            }
            else if (pwd.isEmpty()) {
                password.setError("Please enter your password");
                password.requestFocus();
            }
            else if (email.isEmpty() && pwd.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Fields are empty!", Toast.LENGTH_SHORT).show();

            }
            else if (!(email.isEmpty() && pwd.isEmpty())) {
                mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login error, please try again", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intToHome = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intToHome);
                    }
                });
            }
            else {
                Toast.makeText(LoginActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
            }
        });
        tvSignUp.setOnClickListener(v -> {
            Intent intSignUp = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intSignUp);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}