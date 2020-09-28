package com.example.compatableroomatesapp;

public class User {

    public String fullName, email, bio, gradYear;

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    public void setBio(String bio) { this.bio = bio; }

    public void setGradYear(String gradYear) { this.gradYear = gradYear; }
}
