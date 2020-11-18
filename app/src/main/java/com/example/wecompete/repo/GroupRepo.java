package com.example.wecompete.repo;

import com.example.wecompete.model.Group;
import com.example.wecompete.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GroupRepo {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private Group group = new Group();
    public final String GROUPS = "groups";
    public final String GROUP_NAME = "name";
    public final String GROUP_PROFILES = "groupprofiles";
    public final String ELO = "ELO";


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

    }
}
