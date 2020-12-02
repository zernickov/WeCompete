package com.example.wecompete.model;

public class UserProfile {

    private String id; //default

    public UserProfile(String id) {
        this.id = id;
    }

    public UserProfile() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
