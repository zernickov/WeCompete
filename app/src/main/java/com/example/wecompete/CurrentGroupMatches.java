package com.example.wecompete;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.wecompete.global.Global;
import com.example.wecompete.model.Group;
import com.example.wecompete.repo.GroupProfileRepo;
import com.example.wecompete.repo.MatchRepo;
import com.example.wecompete.service.GroupMatchesAdapter;
import com.example.wecompete.service.MyLeaderboardAdapter;
import com.example.wecompete.service.Updatable;

public class CurrentGroupMatches extends AppCompatActivity implements Updatable {


    private MatchRepo matchRepo = new MatchRepo();
    private GroupMatchesAdapter groupMatchesAdapter;
    private ListView myMatchesListView;
    private Group currentGroup;
    private Button backBtn;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_group_matches);

        currentGroup = (Group) Global.map.get(Global.GROUP_KEY);

        backBtn = findViewById(R.id.backFromMatchesBtn);
        myMatchesListView = findViewById(R.id.myMatchesListView);
        groupMatchesAdapter = new GroupMatchesAdapter(this, matchRepo.groupMatches());

        myMatchesListView.setAdapter(groupMatchesAdapter);
        matchRepo.setActivity(this, currentGroup.getId());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void update(Object o) {
        System.out.println("update() in 'CurrentGroupMatches' is called");
        // kald på adapters notifyDatasetChange()
        runOnUiThread(()->{
            groupMatchesAdapter.notifyDataSetChanged();
        });
    }
}