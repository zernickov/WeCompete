package com.example.wecompete.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.wecompete.R;
import com.example.wecompete.global.Global;
import com.example.wecompete.model.Group;
import com.example.wecompete.repo.GroupProfileRepo;
import com.example.wecompete.adapters.MyLeaderboardAdapter;
import com.example.wecompete.adapters.Updatable;

public class CurrentGroupLeaderboardActivity extends AppCompatActivity implements Updatable {

    private GroupProfileRepo groupProfileRepo = new GroupProfileRepo();
    private MyLeaderboardAdapter myLeaderboardAdapter;
    private ListView myLeaderboardListView;
    private Group currentGroup;
    private Button backBtn;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_group_leaderboard);

        currentGroup = (Group) Global.map.get(Global.GROUP_KEY);

        backBtn = findViewById(R.id.backFromLeaderboardBtn);
        myLeaderboardListView = findViewById(R.id.myLeaderboardListView);
        myLeaderboardAdapter = new MyLeaderboardAdapter(this, groupProfileRepo.myGroupsProfiles());

        myLeaderboardListView.setAdapter(myLeaderboardAdapter);
        groupProfileRepo.setActivity(this, currentGroup.getId());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    @Override
    public void update(Object o) {
        System.out.println("update() in 'CurrentGroupLeaderboard' is called");
        // kald på adapters notifyDatasetChange()
        runOnUiThread(()->{
            myLeaderboardAdapter.notifyDataSetChanged();
        });
    }
}