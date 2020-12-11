package com.example.wecompete.repo;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.wecompete.model.GroupProfile;
import com.example.wecompete.service.EloSorter;
import com.example.wecompete.adapters.Updatable;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GroupProfileRepo {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<GroupProfile> groupProfilesList = new ArrayList<>(); //gemmer Note objekter. Kan opdateres.
    private Updatable activity;
    public final String GROUPS = "groups";
    public final String GROUP_PROFILES = "groupprofiles";
    public final String ELO = "ELO";
    public final String GROUP_USERNAME = "groupusername";


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setActivity(Updatable a, String groupID) { //kaldes fra aktivitet som skal blive opdateret
        activity = a;
        startListener(groupID);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startListener(String groupID) { // SnapshotListener den lytter hele tiden
        db.collection(GROUPS).document(groupID).collection(GROUP_PROFILES).addSnapshotListener((value, error) -> {
            groupProfilesList.clear();
            for (DocumentSnapshot snap: value.getDocuments()) {
                GroupProfile groupProfile = new GroupProfile(snap.get(ELO).toString(), snap.getId(), snap.get(GROUP_USERNAME).toString());
                groupProfilesList.add(groupProfile);
                activity.update(null);
            }
            groupProfilesList.sort(new EloSorter());
        });
    }


    public List<GroupProfile> myGroupsProfiles() {
        return groupProfilesList;
    }
}
