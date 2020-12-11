package com.example.wecompete.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wecompete.R;
import com.example.wecompete.model.Group;
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
        TextView textView = convertView.findViewById(R.id.myMatchesRowTextView);
        textView.setText(data.get(position).getMatchTime());
        TextView textView2 = convertView.findViewById(R.id.myMatchesRowTextView5);
        textView2.setText(data.get(position).getLoser());
        //ImageView imageView = convertView.findViewById(R.id.myImageView);
        //imageView.setImageResource(images[position]);
        return convertView;
    }
}
