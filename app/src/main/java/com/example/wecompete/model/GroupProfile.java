package com.example.wecompete.model;

public class GroupProfile {

    private String id; //default
    private String ELO;

    public GroupProfile(String ELO, String id) {
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

    public String getELO() {
        return ELO;
    }

    public void setELO(String ELO) {
        this.ELO = ELO;
    }
}
