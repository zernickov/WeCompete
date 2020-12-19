package com.example.wecompete.repo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.wecompete.activities.CurrentGroupActivity;
import com.example.wecompete.model.Group;
import com.example.wecompete.model.Match;
import com.example.wecompete.service.MatchService;
import com.example.wecompete.adapters.Updatable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchRepo {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Match> matchesList = new ArrayList<>(); //gemmer Note objekter. Kan opdateres.
    private Updatable activity;
    public final String GROUPS = "groups";
    public final String MATCH_TIME = "matchtime";
    public final String MATCHES = "matches";
    public final String WINNER = "winner";
    public final String LOSER = "loser";
    public final String GROUP_PROFILES = "groupprofiles";
    public final String USERNAME = "username";
    public final String ELO = "ELO";
    public final String USERS = "users";
    private GroupRepo groupRepo = new GroupRepo();
    private MatchService matchService = new MatchService();

    /**
     * FORMÅL: at sætte Updatable objektet og kalde startListener metoden, da startListener metoden gør brug af Updatable objektet
     * BRUG: i onCreate() metoden i CurrentGroupMatchesActivity.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setActivity(Updatable a, String groupID) { //kaldes fra aktivitet som skal blive opdateret
        activity = a;
        startListener(groupID);
    }

    /**
     * FORMÅL: at populere arraylisten med match objekter og sortere til den rigtige rækkefølge.
     * BRUG: i setActivity() metoden i MatchRepo
     */
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

    /**
     * FORMÅL: at oprette en kamp i databasen.
     * BRUG: i declareMatchResult() metoden i MatchRepo klassen.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void registerMatch(String groupID, Match match) {
        //opret nyt dokument i Firebase hvor vi selv angiver document id
        DocumentReference ref = db.collection(GROUPS).document(groupID).collection(MATCHES).document(match.getId());
        Map<String, String> colMap = new HashMap<>();
        colMap.put(WINNER, match.getWinner());
        colMap.put(LOSER, match.getLoser());
        colMap.put(MATCH_TIME, match.getMatchTime());
        ref.set(colMap).addOnCompleteListener(task -> {
            if (!task.isSuccessful()){
                System.out.println("error i opret collection groupprofiles: " + task.getException());
            }
        });
    }

    /**
     * FORMÅL: at deklarere en kamps udfald og bruge deklerationen når ny elo skal udregnes og kampen skal registreres i databasen.
     * BRUG: i onCreate() metoden i CurrentGroupActivity
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void declareMatchResult(String m_Text, Group currentGroup, FirebaseAuth mFirebaseAuth, Context currentGroupActivity, boolean IWon) {
                db.collection(USERS).whereEqualTo(USERNAME, m_Text).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DocumentReference ref = db.collection(GROUPS).document(currentGroup.getId()).collection(GROUP_PROFILES).document(document.getId());
                            ref.get().addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    DocumentSnapshot document2 = task2.getResult();
                                    if (document2.exists()) {
                                        float opponentELOFloat = Float.parseFloat(document2.get(ELO).toString());
                                        DocumentReference docRef2 = db.collection(GROUPS).document(currentGroup.getId()).collection(GROUP_PROFILES).document(mFirebaseAuth.getUid());
                                        docRef2.get().addOnCompleteListener(task3 -> {
                                            if (task3.isSuccessful()) {
                                                DocumentSnapshot document3 = task3.getResult();
                                                if (document3.exists()) {
                                                    float myELOFloat = Float.parseFloat(document3.get(ELO).toString());
                                                    matchService.EloRating(myELOFloat, opponentELOFloat, 30, IWon, mFirebaseAuth.getUid(), m_Text, currentGroup.getId());
                                                    DocumentReference docRef3 = db.collection(USERS).document(mFirebaseAuth.getUid());
                                                    docRef3.get().addOnCompleteListener(task4 -> {
                                                        if (task4.isSuccessful()) {
                                                            DocumentSnapshot document4 = task4.getResult();
                                                            if (document4.exists()) {
                                                                LocalDateTime currentDate = LocalDateTime.now();
                                                                DateTimeFormatter formatForDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                                                                if (IWon) {
                                                                    Match match = new Match(currentDate.format(formatForDate), matchService.fetchDateTimeForMatch(), document4.get(USERNAME).toString(), document.get(USERNAME).toString());
                                                                    registerMatch(currentGroup.getId(), match);
                                                                } else if (!IWon) {
                                                                    Match match = new Match(currentDate.format(formatForDate), matchService.fetchDateTimeForMatch(), document.get(USERNAME).toString(), document4.get(USERNAME).toString());
                                                                    registerMatch(currentGroup.getId(), match);
                                                                }
                                                                ((Activity) currentGroupActivity).finish();
                                                                Intent i = new Intent(currentGroupActivity, CurrentGroupActivity.class);
                                                                currentGroupActivity.startActivity(i);
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
    }

    /**
     * FORMÅL: at returnere den arrayliste, som bliver populeret i startListener() metoden.
     * BRUG: i onCreate() metoden i CurrentGroupMatchesActivity klassen.
     */
    public List<Match> groupMatches() {
        return matchesList;
    }
}
