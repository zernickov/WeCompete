package com.example.wecompete.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wecompete.R;
import com.example.wecompete.model.Match;

import java.util.List;

public class GroupMatchesAdapter extends BaseAdapter {
    private List<Match> data;
    private LayoutInflater layoutInflater;

    public GroupMatchesAdapter(Context context, List<Match> data) {
        this.data = data;
        //from er en statisk metode
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
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
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.my_matches_row, null);
        }
        TextView matchTime = convertView.findViewById(R.id.myMatchesTimeTextView);
        TextView matchWinnerName = convertView.findViewById(R.id.myMatchesWinnerNameTextView);
        TextView matchLoserName = convertView.findViewById(R.id.myMatchesLoserNameTextView);
        matchTime.setText(data.get(position).getMatchTime());
        matchWinnerName.setText(data.get(position).getWinner());
        matchLoserName.setText(data.get(position).getLoser());

        return convertView;
    }
}
