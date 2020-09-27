package com.example.compatableroomatesapp;

public class User {

    public String fullName, email, bio;

    public User() {

    }

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    public void setBio(String bio) { this.bio = bio; }
}
