package com.example.wecompete.repo;

import androidx.annotation.NonNull;

import com.example.wecompete.model.Group;
import com.example.wecompete.model.GroupProfile;
import com.example.wecompete.model.User;
import com.example.wecompete.model.UserProfile;
import com.example.wecompete.service.Updatable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GroupRepo {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private Group group = new Group();
    public final String GROUPS = "groups";
    public final String GROUP_NAME = "name";
    public final String GROUP_PROFILES = "groupprofiles";
    public final String USER_PROFILES = "userprofile";
    public final String ELO = "ELO";
    private User user = new User();
    public final String USERS = "users";
    private String inviteUserResult;
    private List<Group> groupList = new ArrayList<>(); //gemmer Note objekter. Kan opdateres.
    private Updatable activity;


    public void setActivity(Updatable a, String userID) { //kaldes fra aktivitet som skal blive opdateret
        activity = a;
        startListener(userID);
    }

    public void addGroup(Group group, String groupProfileID) {
        DocumentReference ref = db.collection(GROUPS).document(group.getId()); //opret nyt dokument i Firebase hvor vi selv angiver document id
        System.out.println("addGroup kaldet " + ref);
        Map<String, String> map = new HashMap<>();
        map.put(GROUP_NAME, group.getGroupName()); //tilføj selv flere key-value par efter behov
        ref.set(map).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                System.out.println("error i gem: " +task.getException());
            }
        });//gemmer hele map i aktuelt dokument
        DocumentReference documentReference = ref.collection(GROUP_PROFILES).document(groupProfileID);
        Map<String, String> colMap = new HashMap<>();
        colMap.put(ELO, "1000");
        documentReference.set(colMap).addOnCompleteListener(task -> {
            if (!task.isSuccessful()){
                System.out.println("error i opret collection groupprofiles: " + task.getException());
            }
        });
        DocumentReference ref2 = db.collection(USERS).document(groupProfileID);
        DocumentReference documentReference2 = ref2.collection(USER_PROFILES).document(group.getId());
        Map<String, String> colMap2 = new HashMap<>();
        colMap2.put("VALUE", "null");
        documentReference2.set(colMap2).addOnCompleteListener(task -> {
            if (!task.isSuccessful()){
                System.out.println("error i opret collection groupprofiles: " + task.getException());
            }
        });
    }

    public void inviteUser() {
        db.collection(USERS).whereEqualTo("username", "robi0297").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        inviteUserResult = document.getId();
                        System.out.println("WHYWHYWHY"+ inviteUserResult);
                        System.out.println(document.getId() + " => " + document.getData());
                    }
                } else {
                    System.out.println("ERROR: getting documents: "+ task.getException());
                }
            }
        });
    }

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

    public List<Group> myGroups() {

        return groupList;
    }
}
