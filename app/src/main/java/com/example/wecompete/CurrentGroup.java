package com.example.wecompete;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wecompete.R;
import com.example.wecompete.global.Global;
import com.example.wecompete.model.Group;
import com.example.wecompete.repo.GroupRepo;
import com.example.wecompete.repo.UserRepo;
import com.google.firebase.auth.FirebaseAuth;

public class CurrentGroup extends AppCompatActivity {

    private Group currentGroup;
    private TextView currentGroupNameTextView;
    private TextView myUsernameTextView;
    private TextView myELORatingTextView;
    public FirebaseAuth mFirebaseAuth;
    private UserRepo userRepo = new UserRepo();
    private GroupRepo groupRepo = new GroupRepo();
    private Button inviteUserBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_group);
        currentGroup = (Group) Global.map.get(Global.GROUP_KEY);
        mFirebaseAuth = FirebaseAuth.getInstance();

        currentGroupNameTextView = findViewById(R.id.currentGroupNameTextView);
        myUsernameTextView = findViewById(R.id.usernameTextView);
        myELORatingTextView = findViewById(R.id.eloTextView);
        inviteUserBtn = findViewById(R.id.inviteUserBtn);

        currentGroupNameTextView.setText(currentGroup.getGroupName());
        userRepo.showUsername(myUsernameTextView, mFirebaseAuth.getUid());
        userRepo.showELO(myELORatingTextView, currentGroup.getId(), mFirebaseAuth.getUid());

        inviteUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupRepo.inviteUser("patr7756", currentGroup.getId());
                finish();
            }
        });

    }
}