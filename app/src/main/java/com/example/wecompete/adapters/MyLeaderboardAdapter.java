package com.example.wecompete.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wecompete.R;
import com.example.wecompete.model.GroupProfile;

import java.util.List;

public class MyLeaderboardAdapter extends BaseAdapter {
    private List<GroupProfile> groupProfileData;
    private LayoutInflater layoutInflater;

    public MyLeaderboardAdapter(Context context, List<GroupProfile> groupProfileData) {
        this.groupProfileData = groupProfileData;
        //from er en statisk metode
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return groupProfileData.size();
    }

    @Override
    public Object getItem(int position) { // bliver ikke kaldt
        return null;
    }

    @Override
    public long getItemId(int position) { //bliver kaldt 2 gange i starten
        return position;
    }

    //I denne metode kan det lave dynamisk.. set image på baggrund af textview i stedet for position
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.my_leaderboard_row, null);
        }
        TextView textView = convertView.findViewById(R.id.myGroupRowTextView);
        textView.setText(groupProfileData.get(position).getGroupUsername());
        TextView textView2 = convertView.findViewById(R.id.myLeaderboardRowTextView5);
        textView2.setText(groupProfileData.get(position).getELO());

        ImageView rankIcon = convertView.findViewById(R.id.myRankIconForLeaderboard);

        float myELOFloatForRankIcon = Float.parseFloat(groupProfileData.get(position).getELO());
        if (myELOFloatForRankIcon < 500) {
            rankIcon.setImageResource(R.drawable.wecompetebronzewithouttext);
        } else if (myELOFloatForRankIcon >= 500 && myELOFloatForRankIcon <= 1000) {
            rankIcon.setImageResource(R.drawable.wecompetesilverwithouttext);
        } else if (myELOFloatForRankIcon > 1000 && myELOFloatForRankIcon < 1500) {
            rankIcon.setImageResource(R.drawable.wecompetegoldwithouttext);
        } else if (myELOFloatForRankIcon >= 1500) {
            rankIcon.setImageResource(R.drawable.wecompeteplatinumwithouttext);
        }
        return convertView;
    }
}
