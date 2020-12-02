package com.example.wecompete.repo;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.wecompete.model.Group;
import com.example.wecompete.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class UserRepo {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public final String USERS = "users";
    public final String USERNAME = "username";
    public final String USER_PROFILES = "userprofile";

    public void addUsername(User user) {
        DocumentReference ref = db.collection(USERS).document(user.getId()); //opret nyt dokument i Firebase hvor vi selv angiver document id
        System.out.println("addUsername kaldet " + ref);
        Map<String, String> map = new HashMap<>();
        map.put(USERNAME, user.getUsername()); //tilføj selv flere key-value par efter behov
        ref.set(map).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                System.out.println("error i gem: " +task.getException());
            }
        }); //gemmer hele map i aktuelt dokument
        /*
        DocumentReference documentReference = ref.collection(USER_PROFILES).document("DEFAULT");
        Map<String, String> colMap = new HashMap<>();
        colMap.put("VALUE", "default document");
        documentReference.set(colMap).addOnCompleteListener(task -> {
            if (!task.isSuccessful()){
                System.out.println("error i opret collection groupprofiles: " + task.getException());
            }
        });
         */
    }

    /*
    public void addNote(Note note) {
        DocumentReference ref = db.collection(NOTES).document(note.getId()); //opret nyt dokument i Firebase hvor vi selv angiver document id
        System.out.println("addNote kaldet " + ref);
        Map<String, String> map = new HashMap<>();
        map.put(TITLE, note.getTitle()); //tilføj selv flere key-value par efter behov
        ref.set(map).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                System.out.println("error i gem: " +task.getException());
            }
        }); //gemmer hele map i aktuelt dokument
    }
     */
    public void showUsername(TextView textView, String userID) {
        DocumentReference docRef = db.collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        System.out.println("DocumentSnapshot data: " + document.getData());
                        System.out.println(document.get("username").toString());
                        textView.setText(document.get("username").toString());

                    } else {
                        System.out.println("No such document");
                    }
                } else {
                    System.out.println("get failed with "+ task.getException());
                }
            }
        });
    }

    public void showELO() {
        DocumentReference docRef2 = db.collection("users").document("4mgBmE36ElUyCX7eVdACJwzZVuC3").collection("profil").document("4HG4Cg9RdTXzXXi9KACe");
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        System.out.println("DocumentSnapshot data: " + document.getData());
                        System.out.println(document.get("ELO").toString());
                        //myUsernameTextView.setText(document.get("ELO").toString());

                    } else {
                        System.out.println("No such document");
                    }
                } else {
                    System.out.println("get failed with "+ task.getException());
                }
            }
        });
    }

}
