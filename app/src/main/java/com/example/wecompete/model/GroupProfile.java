package com.example.wecompete.model;

public class GroupProfile {

    private String id; //default
    private String ELO;
    private String groupUsername;

    public GroupProfile(String ELO, String id, String groupUsername) {
        this.groupUsername = groupUsername;
        this.ELO = ELO;
        this.id = id;
    }


    public String getGroupUsername() {
        return groupUsername;
    }

    public void setGroupUsername(String groupUsername) {
        this.groupUsername = groupUsername;
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
