package com.example.compatableroomatesapp;

public class User {


    public String fullName, email, bio, gradYear, personality, timeOfDay, playsMusic, isSmoker, isVisited, isTidy, matchUID;
    public Boolean matched, acceptedMatch;

    public User() {}

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
        this.matched = false;
    }

    public void setBio(String bio) { this.bio = bio; }

    public void setGradYear(String gradYear) { this.gradYear = gradYear; }

    public void setAcceptedMatch(Boolean acceptedMatch) { this.acceptedMatch = acceptedMatch; }

    public void setPersonality(String personality) {this.personality = personality; }
}
