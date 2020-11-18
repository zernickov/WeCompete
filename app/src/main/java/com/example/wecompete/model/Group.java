package com.example.wecompete.model;

import java.util.UUID;

public class Group {
    private String groupName;
    private String id = UUID.randomUUID().toString();; //default

    public Group(String groupName, String _id) {
        this.groupName = groupName;
        if (_id != null) {
            id = _id;
        }
    }

    public Group(String groupName){
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
