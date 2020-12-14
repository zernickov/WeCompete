package com.example.wecompete.repo;

import android.widget.TextView;


import com.example.wecompete.model.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserRepo {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public final String USERS = "users";
    public final String GROUPS = "groups";
    public final String USERNAME = "username";
    public final String GROUP_PROFILES = "groupprofiles";

    public void addUsername(User user) {
        DocumentReference ref = db.collection(USERS).document(user.getId()); //opret nyt dokument i Firebase hvor vi selv angiver document id
        System.out.println("addUsername kaldet " + ref);
        Map<String, String> map = new HashMap<>();
        map.put(USERNAME, user.getUsername()); //tilføj selv flere key-value par efter behov
        ref.set(map).addOnCompleteListener(task2 -> {
            if (!task2.isSuccessful()) {
                System.out.println("error i gem: " +task2.getException());
            }
        }); //gemmer hele map i aktuelt dokument
    }

    public void showUsername(TextView textView, String userID) {
        DocumentReference docRef = db.collection("users").document(userID);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    textView.setText(document.get(USERNAME).toString());

                } else {
                    System.out.println("No such document");
                }
            } else {
                System.out.println("get failed with "+ task.getException());
            }
        });
    }

    public void showELO(TextView textView, String groupID, String userID) {
        DocumentReference docRef2 = db.collection(GROUPS).document(groupID).collection(GROUP_PROFILES).document(userID);
        docRef2.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    textView.setText(document.get("ELO").toString());
                } else {
                    System.out.println("No such document");
                }
            } else {
                System.out.println("get failed with "+ task.getException());
            }
        });
    }

}
