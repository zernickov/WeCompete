package com.example.wecompete.service;

import com.example.wecompete.model.GroupProfile;

import java.util.Comparator;

/**
 * FORMÅL: at sortere de objekter, som indsættes i en arrayliste
 * BRUG: i startListener() metoden i GroupProfileRepo klassen.
 */
public class EloSorter implements Comparator<GroupProfile> {

    @Override
    public int compare(GroupProfile o1, GroupProfile o2) {
        float f1 = Float.parseFloat(o1.getELO());
        float f2 = Float.parseFloat(o2.getELO());
        return Float.compare(f2,f1);
    }
}
