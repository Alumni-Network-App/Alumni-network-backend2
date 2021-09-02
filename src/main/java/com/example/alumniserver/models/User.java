package com.example.alumniserver.models;

public class User {

    private int user_Id;
    private String name;
    private String picture;
    private String status;
    private String bio;
    private String fun_fact;

    public User(int user_Id, String name, String picture, String status, String bio, String fun_fact) {
        this.user_Id = user_Id;
        this.name = name;
        this.picture = picture;
        this.status = status;
        this.bio = bio;
        this.fun_fact = fun_fact;
    }

    public int getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(int user_Id) {
        this.user_Id = user_Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFun_fact() {
        return fun_fact;
    }

    public void setFun_fact(String fun_fact) {
        this.fun_fact = fun_fact;
    }
}
