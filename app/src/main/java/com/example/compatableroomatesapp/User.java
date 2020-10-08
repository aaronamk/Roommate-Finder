package com.example.compatableroomatesapp;


public class User {


    public String fullName, email, bio, gradYear, personality, matchUID;
    public boolean matched, acceptedMatch, morningPerson, playsMusic, isSmoker, isVisited, isTidy;

    public User() {}

    public User(String fullName, String email, boolean morningPerson, boolean playsMusic, boolean isSmoker, boolean isVisited, boolean isTidy) {
        this.fullName = fullName;
        this.email = email;
        this.matched = false;
        this.morningPerson = morningPerson;
        this.playsMusic = playsMusic;
        this.isVisited = isVisited;
        this.isSmoker = isSmoker;
        this.isTidy = isTidy;
    }

    public void setBio(String bio) { this.bio = bio; }

    public void setGradYear(String gradYear) { this.gradYear = gradYear; }

    public void setAcceptedMatch(Boolean acceptedMatch) { this.acceptedMatch = acceptedMatch; }

    public void setPersonality(String personality) {this.personality = personality; }

}
