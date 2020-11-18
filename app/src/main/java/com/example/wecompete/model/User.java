package com.example.wecompete.model;

import java.util.UUID;

public class User {
    private String username;
    private String id; //default

    public User(String username, String id) {
        this.username = username;
        this.id = id;
    }

    public User(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
