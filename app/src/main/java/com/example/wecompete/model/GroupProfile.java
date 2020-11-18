package com.example.wecompete.model;

import java.util.UUID;

public class GroupProfile {

    private String id; //default
    private int ELO;

    public GroupProfile(int ELO, String id) {
        this.ELO = ELO;
        this.id = id;
    }

    public GroupProfile(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getELO() {
        return ELO;
    }

    public void setELO(int ELO) {
        this.ELO = ELO;
    }
}
