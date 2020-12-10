package com.example.wecompete;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wecompete.R;
import com.example.wecompete.global.Global;
import com.example.wecompete.model.Group;
import com.example.wecompete.model.Match;
import com.example.wecompete.model.User;
import com.example.wecompete.repo.GroupRepo;
import com.example.wecompete.repo.UserRepo;
import com.example.wecompete.service.MatchService;
import com.example.wecompete.service.Updatable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrentGroup extends AppCompatActivity {

    private Group currentGroup;
    private TextView currentGroupNameTextView;
    private TextView myUsernameTextView;
    private TextView myELORatingTextView;
    public FirebaseAuth mFirebaseAuth;
    private UserRepo userRepo = new UserRepo();
    private GroupRepo groupRepo = new GroupRepo();
    private MatchService matchService = new MatchService();
    private Button inviteUserBtn;
    private String m_Text = "";
    private Button registerMatchBtn;
    private ImageView myRankIcon;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public final String GROUPS = "groups";
    public final String GROUP_NAME = "name";
    public final String GROUP_PROFILES = "groupprofiles";
    public final String USER_PROFILES = "userprofile";
    public final String USERNAME = "username";
    public final String ELO = "ELO";
    public final String WINNER = "winner";
    public final String LOSER = "loser";
    public final String MATCH_TIME = "matchtime";
    public final String MATCHES = "matches";
    private User user = new User();
    public final String USERS = "users";
    private String inviteUserResult;
    private List<Group> groupList = new ArrayList<>(); //gemmer Note objekter. Kan opdateres.
    private Updatable activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_group);
        currentGroup = (Group) Global.map.get(Global.GROUP_KEY);
        mFirebaseAuth = FirebaseAuth.getInstance();

        currentGroupNameTextView = findViewById(R.id.currentGroupNameTextView);
        myUsernameTextView = findViewById(R.id.usernameTextView);
        myELORatingTextView = findViewById(R.id.eloTextView);
        inviteUserBtn = findViewById(R.id.inviteUserBtn);
        registerMatchBtn = findViewById(R.id.createMatchButton);
        myRankIcon = findViewById(R.id.myRankIcon);

        currentGroupNameTextView.setText(currentGroup.getGroupName());
        userRepo.showUsername(myUsernameTextView, mFirebaseAuth.getUid());
        userRepo.showELO(myELORatingTextView, currentGroup.getId(), mFirebaseAuth.getUid());

        //stackoverflow link: https://stackoverflow.com/questions/2597329/how-to-do-a-fadein-of-an-image-on-an-android-activity-screen
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.picturefadein);
        myRankIcon.startAnimation(myFadeInAnimation); //Set animation to your ImageView


        DocumentReference docRef2 = db.collection(GROUPS).document(currentGroup.getId()).collection(GROUP_PROFILES).document(mFirebaseAuth.getUid());
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //TODO (logik) sæt i metode i groupprofileService
                        float myELOFloatForRankIcon = Float.parseFloat(document.get(ELO).toString());
                        if (myELOFloatForRankIcon < 501) {
                            myRankIcon.setImageResource(R.drawable.wecompetebronze);
                        } else if (myELOFloatForRankIcon > 500 && myELOFloatForRankIcon < 1001) {
                            myRankIcon.setImageResource(R.drawable.wecompetesilver);
                        } else if (myELOFloatForRankIcon > 1000 && myELOFloatForRankIcon < 1501) {
                            myRankIcon.setImageResource(R.drawable.wecompetegold);
                        } else if (myELOFloatForRankIcon > 1500) {
                            myRankIcon.setImageResource(R.drawable.wecompeteplatinum);
                        }
                    }
                }
            }
        });





        registerMatchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create AlertDialog boks
                AlertDialog.Builder builder = new AlertDialog.Builder(CurrentGroup.this);
                builder.setTitle("Invite User");
                // Set up the input
                final EditText input = new EditText(CurrentGroup.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                // Set up the buttons
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("I Lost", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        groupRepo.inviteUser(m_Text, currentGroup.getId());
                    }
                });
                builder.setPositiveButton("I Won", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                m_Text = input.getText().toString();
                                //groupRepo.updateNewElo(mFirebaseAuth.getUid(), m_Text, currentGroup.getId(), "1200", "800");
                                db.collection(USERS).whereEqualTo(USERNAME, m_Text).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                DocumentReference ref = db.collection(GROUPS).document(currentGroup.getId()).collection(GROUP_PROFILES).document(document.getId());
                                                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                                        if (task2.isSuccessful()) {
                                                            DocumentSnapshot document2 = task2.getResult();
                                                            if (document2.exists()) {
                                                                float opponentELOFloat = Float.parseFloat(document2.get(ELO).toString());
                                                                DocumentReference docRef2 = db.collection(GROUPS).document(currentGroup.getId()).collection(GROUP_PROFILES).document(mFirebaseAuth.getUid());
                                                                docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task3) {
                                                                        if (task3.isSuccessful()) {
                                                                            DocumentSnapshot document3 = task3.getResult();
                                                                            if (document3.exists()) {
                                                                                float myELOFloat = Float.parseFloat(document3.get(ELO).toString());
                                                                                matchService.EloRating(myELOFloat, opponentELOFloat, 30, true, mFirebaseAuth.getUid(), m_Text, currentGroup.getId());
                                                                                DocumentReference docRef3 = db.collection(USERS).document(mFirebaseAuth.getUid());
                                                                                docRef3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task4) {
                                                                                        if (task4.isSuccessful()) {
                                                                                            DocumentSnapshot document4 = task4.getResult();
                                                                                            if (document4.exists()) {
                                                                                                Match match = new Match(matchService.fetchDateTimeForMatch(), document4.get(USERNAME).toString(), document.get(USERNAME).toString());
                                                                                                groupRepo.registerMatch(currentGroup.getId(), match);
                                                                                                finish();
                                                                                                Intent i = new Intent(CurrentGroup.this, CurrentGroup.class);
                                                                                                startActivity(i);
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });
                            }
                        });
                        builder.show();
                    }
                });


        inviteUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CurrentGroup.this);
                builder.setTitle("Invite User");
                // Set up the input
                final EditText input = new EditText(CurrentGroup.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                // Set up the buttons
                builder.setPositiveButton("Invite", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        groupRepo.inviteUser(m_Text, currentGroup.getId());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

    }
}