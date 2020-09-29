package com.example.compatableroomatesapp;

public class User {


    public String fullName, email, bio, gradYear, personality, timeOfDay, playingMusic, isSmoker, friendsVisit, tidy, matchUID;
    public Boolean matched, acceptedMatch;

    public User() {}

    public User(String fullName, String email, Boolean matched){
        this.fullName = fullName;
        this.email = email;
        this.matched = matched;
    }

    public void setBio(String bio) { this.bio = bio; }

    public void setGradYear(String gradYear) { this.gradYear = gradYear; }

    public void setAcceptedMatch(Boolean acceptedMatch) { this.acceptedMatch = acceptedMatch; }
}
