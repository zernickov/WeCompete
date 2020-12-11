package com.example.wecompete;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wecompete.model.User;
import com.example.wecompete.repo.UserRepo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    public EditText emailId, password, username;
    public Button btnSignUp;
    public TextView tvSignIn;
    public FirebaseAuth mFirebaseAuth;
    private UserRepo userRepo = new UserRepo();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean isNotAvailable = true;
    public final String USERNAME = "username";
    public final String USERS = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.editTextTextUsername);
        emailId = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        btnSignUp = findViewById(R.id.signUpButton);
        tvSignIn = findViewById(R.id.signInTextView);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                String userName = username.getText().toString();
                if (email.isEmpty()) {
                    emailId.setError("Please enter email");
                    emailId.requestFocus();
                }
                else if (pwd.isEmpty()) {
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else if (userName.isEmpty()) {
                    username.setError("Please enter your username");
                    username.requestFocus();
                }
                else if (userName.length() > 15) {
                    username.setError("Username must be less than 15 characters");
                    username.requestFocus();
                }
                else if (email.isEmpty() && pwd.isEmpty() && userName.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                }

                else if (isNotAvailable) {
                    db.collection(USERS).whereEqualTo(USERNAME, userName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                boolean isEmpty = task.getResult().isEmpty();
                                if (!isEmpty) {
                                    Toast.makeText(MainActivity.this, "Username already taken!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    isNotAvailable = false;
                                }
                            }
                        }
                    });
                }
                else if (!(email.isEmpty() && pwd.isEmpty() && userName.isEmpty() && isNotAvailable && userName.length() > 15)) {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Sign Up Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                //todo evt. ret efter video
                                User user = new User(userName, mFirebaseAuth.getUid());
                                userRepo.addUsername(user);
                                Intent successful = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(successful);
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(MainActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginView = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginView);
            }
        });
    }
}