package com.example.wecompete.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.wecompete.R;
import com.example.wecompete.model.Group;
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

        backBtn.setOnClickListener(v -> finish());

        confirmBtn.setOnClickListener(v -> {
            Group group = new Group(groupNameInput.getText().toString());
            groupRepo.addGroup(group, mFirebaseAuth.getUid());
            finish();
        });

    }


}