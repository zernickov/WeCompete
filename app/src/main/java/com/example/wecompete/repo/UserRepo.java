package com.example.wecompete.repo;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.wecompete.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;

public class UserRepo {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private User user = new User();
    public final String USERS = "users";

    public void showUsername(TextView textView) {
        DocumentReference docRef = db.collection("users").document("4mgBmE36ElUyCX7eVdACJwzZVuC3");
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
