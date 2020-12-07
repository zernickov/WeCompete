package com.example.wecompete;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wecompete.model.Group;
import com.example.wecompete.repo.GroupRepo;
import com.example.wecompete.repo.UserRepo;
import com.example.wecompete.service.MyGroupsAdapter;
import com.example.wecompete.service.Updatable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class HomeActivity extends AppCompatActivity implements Updatable {

    public Button btnLogout;
    public Button btnNewGroup;
    public FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private TextView myUsernameTextView;
    private UserRepo userRepo = new UserRepo();
    private GroupRepo groupRepo = new GroupRepo();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MyGroupsAdapter myGroupsAdapter;
    private ListView myGroupListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mFirebaseAuth = FirebaseAuth.getInstance();
        myUsernameTextView = findViewById(R.id.myUsernameTextView);
        btnLogout = findViewById(R.id.logoutButton);
        btnNewGroup = findViewById(R.id.newGroupBtn);
        myGroupListView = findViewById(R.id.myGroupListView);
        myGroupsAdapter = new MyGroupsAdapter(this, groupRepo.myGroups());

        userRepo.showUsername(myUsernameTextView, mFirebaseAuth.getUid());
        myGroupListView.setAdapter(myGroupsAdapter);
        groupRepo.setActivity(this, mFirebaseAuth.getUid());



        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToSignIn = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intToSignIn);
            }
        });

        btnNewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createGrpPage = new Intent(HomeActivity.this, CreateGroupActivity.class);
                startActivity(createGrpPage);
            }
        });
    }

    @Override
    public void update(Object o) { //bliver kaldet fra en background thread
        System.out.println("update() er kaldet");
        // kald på adapters notifyDatasetChange()
        runOnUiThread(()->{
            myGroupsAdapter.notifyDataSetChanged();
        });
    }

}