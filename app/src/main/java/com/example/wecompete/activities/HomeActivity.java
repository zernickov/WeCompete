package com.example.wecompete.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wecompete.R;
import com.example.wecompete.global.Global;
import com.example.wecompete.repo.GroupRepo;
import com.example.wecompete.repo.UserRepo;
import com.example.wecompete.adapters.MyGroupsAdapter;
import com.example.wecompete.adapters.Updatable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity implements Updatable {

    public Button btnLogout;
    public Button btnNewGroup;
    public FirebaseAuth mFirebaseAuth;
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
        myGroupListView.setOnItemClickListener((_listView, linearLayour, adapterPs, arrPos) -> {
            Intent intent = new Intent(this, CurrentGroupActivity.class);
            Global.map.put(Global.GROUP_KEY, groupRepo.myGroups().get((int)arrPos));
            // som kan hentes på den anden side.
            startActivity(intent);
        });
        groupRepo.setActivity(this, mFirebaseAuth.getUid());

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intToSignIn = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intToSignIn);
        });

        btnNewGroup.setOnClickListener(v -> {
            Intent createGrpPage = new Intent(HomeActivity.this, CreateGroupActivity.class);
            startActivity(createGrpPage);
        });
    }

    @Override
    public void update(Object o) { //bliver kaldet fra en background thread
        System.out.println("update() er kaldet");
        // kald på adapters notifyDatasetChange()
        runOnUiThread(()-> myGroupsAdapter.notifyDataSetChanged());
    }

}