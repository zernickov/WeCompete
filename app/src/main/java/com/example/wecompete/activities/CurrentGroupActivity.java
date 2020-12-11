package com.example.wecompete.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wecompete.R;
import com.example.wecompete.global.Global;
import com.example.wecompete.model.Group;
import com.example.wecompete.repo.GroupRepo;
import com.example.wecompete.repo.MatchRepo;
import com.example.wecompete.repo.UserRepo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CurrentGroupActivity extends AppCompatActivity {

    private Group currentGroup;
    private TextView currentGroupNameTextView;
    private TextView myUsernameTextView;
    private TextView myELORatingTextView;
    public FirebaseAuth mFirebaseAuth;
    private UserRepo userRepo = new UserRepo();
    private GroupRepo groupRepo = new GroupRepo();
    private MatchRepo matchRepo = new MatchRepo();
    private Button inviteUserBtn;
    private String m_Text = "";
    private Button registerMatchBtn;
    private ImageView myRankIcon;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public final String GROUPS = "groups";
    public final String GROUP_PROFILES = "groupprofiles";
    public final String ELO = "ELO";
    public final String GROUP_USERNAME = "groupusername";
    private Button myLeaderBoardBtn;
    private Button myMatchesBtn;
    private Button backBtn;

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
        myRankIcon = findViewById(R.id.myRankIconForLeaderboard);
        myLeaderBoardBtn = findViewById(R.id.myLeaderboardBtn);
        myMatchesBtn = findViewById(R.id.myMatchesBtn);
        backBtn = findViewById(R.id.backFromCurrentGroupBtn);

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
                        if (myELOFloatForRankIcon < 500) {
                            myRankIcon.setImageResource(R.drawable.wecompetebronze);
                        } else if (myELOFloatForRankIcon >= 500 && myELOFloatForRankIcon <= 1000) {
                            myRankIcon.setImageResource(R.drawable.wecompetesilver);
                        } else if (myELOFloatForRankIcon > 1000 && myELOFloatForRankIcon < 1500) {
                            myRankIcon.setImageResource(R.drawable.wecompetegold);
                        } else if (myELOFloatForRankIcon >= 1500) {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(CurrentGroupActivity.this);
                builder.setTitle("Choose Opponent");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CurrentGroupActivity.this, android.R.layout.select_dialog_singlechoice);
                db.collection(GROUPS).document(currentGroup.getId()).collection(GROUP_PROFILES).addSnapshotListener((value, error) -> {
                    arrayAdapter.clear();
                    for (DocumentSnapshot snap: value.getDocuments()) {
                        if (!snap.getId().equals(mFirebaseAuth.getUid())) {
                            arrayAdapter.add(snap.get(GROUP_USERNAME).toString());
                        }
                    }
                });
                // Set up the buttons
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(CurrentGroupActivity.this);
                        builderInner.setMessage(m_Text);
                        builderInner.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builderInner.setNegativeButton("I Lost", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                matchRepo.declareMatchResult(m_Text, currentGroup, mFirebaseAuth, CurrentGroupActivity.this, false);
                            }
                        });
                        builderInner.setPositiveButton("I Won", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        matchRepo.declareMatchResult(m_Text, currentGroup, mFirebaseAuth, CurrentGroupActivity.this, true);
                                    }
                                });
                        builderInner.show();



                    }

                });
                        builder.show();
                    }
                });


        inviteUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CurrentGroupActivity.this);
                builder.setTitle("Invite User");
                // Set up the input
                final EditText input = new EditText(CurrentGroupActivity.this);
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

        myLeaderBoardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent leaderBoardPage = new Intent(CurrentGroupActivity.this, CurrentGroupLeaderboardActivity.class);
                startActivity(leaderBoardPage);
            }
        });

        myMatchesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent matchesPage = new Intent(CurrentGroupActivity.this, CurrentGroupMatchesActivity.class);
                startActivity(matchesPage);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}