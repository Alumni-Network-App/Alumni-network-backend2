package com.example.alumniserver.model;

import javax.persistence.Id;

public class FirebaseUser {

    @Id
    private String uid;
    private String name;

    public FirebaseUser(String uid, String name) {
        super();
        this.uid = uid;
        this.name = name;
    }

    public FirebaseUser() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
