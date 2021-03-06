package com.example.wecompete.repo;


import com.example.wecompete.model.Group;

import com.example.wecompete.adapters.Updatable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupRepo {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public final String GROUPS = "groups";
    public final String GROUP_NAME = "name";
    public final String GROUP_PROFILES = "groupprofiles";
    public final String USER_PROFILES = "userprofile";
    public final String USERNAME = "username";
    public final String ELO = "ELO";
    public final String GROUP_USERNAME = "groupusername";
    public final String USERS = "users";
    private List<Group> groupList = new ArrayList<>(); //gemmer Note objekter. Kan opdateres.
    private Updatable activity;

    /**
     * FORMÅL: at sætte Updatable objektet og kalde startListener metoden, da startListener metoden gør brug af Updatable objektet
     * BRUG: i onCreate() metoden i HomeActivity.
     */
    public void setActivity(Updatable a, String userID) { //kaldes fra aktivitet som skal blive opdateret
        activity = a;
        startListener(userID);
    }

    /**
     * FORMÅL: at tilføje en gruppe til databasen, hvilket indebære at give samme bruger et userprofile dokument og et groupprofile dokument.
     * BRUG: i onCreate() metoden i CreateGroupActivity klassen
     */
    public void addGroup(Group group, String groupProfileID) {
        DocumentReference ref = db.collection(GROUPS).document(group.getId()); //opret nyt dokument i Firebase hvor vi selv angiver document id
        System.out.println("addGroup is called " + ref);
        Map<String, String> map = new HashMap<>();
        map.put(GROUP_NAME, group.getGroupName()); //tilføj selv flere key-value par efter behov
        ref.set(map).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                System.out.println("error updating groups collection: " +task.getException());
            }
        });//gemmer hele map i aktuelt dokument
        db.collection(USERS).document(groupProfileID).addSnapshotListener((value, error) -> {
            DocumentReference documentReference = db.collection(GROUPS).document(group.getId()).collection(GROUP_PROFILES).document(groupProfileID);
            Map<String, String> colMap = new HashMap<>();
            colMap.put(ELO, "1000");
            colMap.put(GROUP_USERNAME, value.get(USERNAME).toString());
            documentReference.set(colMap).addOnCompleteListener(task -> {
                if (!task.isSuccessful()){
                    System.out.println("error updating groupprofiles collection: " + task.getException());
                }
            });
        });
        DocumentReference ref2 = db.collection(USERS).document(groupProfileID);
        DocumentReference documentReference2 = ref2.collection(USER_PROFILES).document(group.getId());
        Map<String, String> colMap2 = new HashMap<>();
        colMap2.put("VALUE", "null");
        documentReference2.set(colMap2).addOnCompleteListener(task -> {
            if (!task.isSuccessful()){
                System.out.println("error updating userprofiles collection: " + task.getException());
            }
        });
    }

    /**
     * FORMÅL: at tilføje en bruger til en gruppe i databasen.
     * BRUG:i onCreate() metoden i CurrentGroupActivity klassen.
     */
    public void inviteUser(String userInput, String groupID) {
        db.collection(USERS).whereEqualTo(USERNAME, userInput).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    document.getId();
                    DocumentReference ref = db.collection(USERS).document(document.getId()).collection(USER_PROFILES).document(groupID);
                    Map<String, String> colMap2 = new HashMap<>();
                    colMap2.put("VALUE", "null");
                    ref.set(colMap2).addOnCompleteListener(task2 -> {
                        if (!task2.isSuccessful()){
                            System.out.println("error i opret collection groupprofiles: " + task2.getException());
                        }
                    });
                    DocumentReference ref2 = db.collection(GROUPS).document(groupID).collection(GROUP_PROFILES).document(document.getId());
                    Map<String, String> colMap = new HashMap<>();
                    colMap.put(ELO, "1000");
                    colMap.put(GROUP_USERNAME, userInput);
                    ref2.set(colMap).addOnCompleteListener(task3 -> {
                        if (!task3.isSuccessful()){
                            System.out.println("error i opret collection groupprofiles: " + task3.getException());
                        }
                    });
                }
            }
        });
    }

    /**
     * FORMÅL: at populere arraylisten med group objekter.
     * BRUG: i setActivity() metoden i GroupRepo klassen
     */
    public void startListener(String userID) { // SnapshotListener den lytter hele tiden
        db.collection(USERS).document(userID).collection(USER_PROFILES).addSnapshotListener((value, error) -> {
            groupList.clear();
            for (DocumentSnapshot snap: value.getDocuments()) {
                db.collection(GROUPS).document(snap.getId()).addSnapshotListener((value1, error1) -> {
                    Group group = new Group(value1.get(GROUP_NAME).toString(), value1.getId());
                    groupList.add(group);
                    activity.update(null); // kaldes efter vi har hentet data fra Firebase
                });
            }
        });
    }

    /**
     * FORMÅL: at opdatere en brugers ELO-rating i databasen.
     * BRUG: i EloRating() metoden i MatchService klassen.
     */
    public void updateNewElo(String userID, String userInput, String groupID, String myNewElo, String newEloOpponent) {
        db.collection(USERS).whereEqualTo(USERNAME, userInput).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    DocumentReference ref = db.collection(GROUPS).document(groupID).collection(GROUP_PROFILES).document(document.getId());
                    ref.update(ELO, newEloOpponent);
                }
            }
        });
        DocumentReference ref2 = db.collection(GROUPS).document(groupID).collection(GROUP_PROFILES).document(userID);
        ref2.update(ELO, myNewElo);
    }

    /**
     * FORMÅL: at returnerer arraylisten med gruppe objekter, som bliver populeret i startListener() metoden.
     * BRUG: i onCreate() metoden i HomeActivity klassen.
     */
    public List<Group> myGroups() {
        return groupList;
    }
}
