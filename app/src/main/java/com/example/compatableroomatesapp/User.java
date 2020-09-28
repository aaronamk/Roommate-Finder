package com.example.compatableroomatesapp;

public class User {

    public String fullName, email, bio, gradYear;

    public User() {

    }

    public User(String fullName, String email, String gradyear){
        this.fullName = fullName;
        this.email = email;
        this.gradYear = gradyear;
    }

    public void setBio(String bio) { this.bio = bio; }
}
