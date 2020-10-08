package com.example.compatableroomatesapp;

import android.net.Uri;

public class User {


    public String fullName, email, bio, gradYear, personality, morningPerson, playsMusic, isSmoker, isVisited, isTidy, matchUID;
    public Boolean matched, acceptedMatch;
    public Uri pImage;

    public User() {}

    public User(String fullName, String email, String morningPerson, String playsMusic, String isSmoker, String isVisited, String isTidy) {
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

    public void setProfileImage(Uri file) {this.pImage = file; }
}
