package com.example.wecompete;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.wecompete.R;
import com.example.wecompete.global.Global;
import com.example.wecompete.model.Group;
import com.example.wecompete.repo.UserRepo;
import com.google.firebase.auth.FirebaseAuth;

public class CurrentGroup extends AppCompatActivity {

    private Group currentGroup;
    private TextView currentGroupNameTextView;
    private TextView myUsernameTextView;
    private TextView myELORatingTextView;
    public FirebaseAuth mFirebaseAuth;
    private UserRepo userRepo = new UserRepo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_group);
        currentGroup = (Group) Global.map.get(Global.GROUP_KEY);
        mFirebaseAuth = FirebaseAuth.getInstance();

        currentGroupNameTextView = findViewById(R.id.currentGroupNameTextView);
        myUsernameTextView = findViewById(R.id.usernameTextView);
        myELORatingTextView = findViewById(R.id.eloTextView);
        currentGroupNameTextView.setText(currentGroup.getGroupName());
        userRepo.showUsername(myUsernameTextView, mFirebaseAuth.getUid());
        userRepo.showELO(myELORatingTextView, currentGroup.getId(), mFirebaseAuth.getUid());

    }
}