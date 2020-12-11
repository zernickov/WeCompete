package com.example.wecompete.repo;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.wecompete.model.GroupProfile;
import com.example.wecompete.model.Match;
import com.example.wecompete.service.EloSorter;
import com.example.wecompete.service.Updatable;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatchRepo {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Match> matchesList = new ArrayList<>(); //gemmer Note objekter. Kan opdateres.
    private Updatable activity;
    public final String GROUPS = "groups";
    public final String MATCH_TIME = "matchtime";
    public final String MATCHES = "matches";
    public final String WINNER = "winner";
    public final String LOSER = "loser";



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setActivity(Updatable a, String groupID) { //kaldes fra aktivitet som skal blive opdateret
        activity = a;
        startListener(groupID);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startListener(String groupID) { // SnapshotListener den lytter hele tiden
        db.collection(GROUPS).document(groupID).collection(MATCHES).addSnapshotListener((value, error) -> {
            matchesList.clear();
            for (DocumentSnapshot snap: value.getDocuments()) {
                Match match = new Match(snap.getId(), snap.get(MATCH_TIME).toString(), snap.get(WINNER).toString(), snap.get(LOSER).toString());
                matchesList.add(match);
                activity.update(null);
            }
            Collections.reverse(matchesList);
        });
    }


    public List<Match> groupMatches() {
        return matchesList;
    }
}
