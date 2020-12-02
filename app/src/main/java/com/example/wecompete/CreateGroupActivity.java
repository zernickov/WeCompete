package com.example.wecompete;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wecompete.model.Group;
import com.example.wecompete.model.User;
import com.example.wecompete.repo.GroupRepo;
import com.google.firebase.auth.FirebaseAuth;

public class CreateGroupActivity extends AppCompatActivity {

    public Button backBtn;
    public TextView groupNameInput;
    public Button confirmBtn;
    private GroupRepo groupRepo = new GroupRepo();
    public FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        backBtn = findViewById(R.id.backBtn);
        groupNameInput = findViewById(R.id.groupNameTextField);
        confirmBtn = findViewById(R.id.confirmBtn);
        mFirebaseAuth = FirebaseAuth.getInstance();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Group group = new Group(groupNameInput.getText().toString());
                groupRepo.addGroup(group, mFirebaseAuth.getUid());
                finish();
            }
        });

    }


}