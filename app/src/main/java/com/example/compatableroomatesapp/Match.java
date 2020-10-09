package com.example.compatableroomatesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Match extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser user;
    private DatabaseReference reference;

    private TextView fullName, personality, bio, quickFacts;
    private Button logout;
    private ImageView profile;
    private StorageReference storageReference;
    private String userID, profileUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // hide unneeded buttons
        findViewById(R.id.editButton).setVisibility(View.GONE);
        findViewById(R.id.matchButton).setVisibility(View.GONE);
        findViewById(R.id.imageButton).setVisibility(View.GONE);

        // add visual elements
        logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(this);
        fullName = findViewById(R.id.fullName);
        personality = findViewById(R.id.personality);
        bio = findViewById(R.id.bio);
        quickFacts = findViewById(R.id.quick_facts);
        profile = findViewById(R.id.profile_pic);

        // update profile based on UID from intent
        profileUserID = this.getIntent().getStringExtra("profileUserID");
        reference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

        // get logged in user
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        reference.child(profileUserID);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    fullName.setText(userProfile.fullName);
                    personality.setText(userProfile.personality);
                    bio.setText(userProfile.bio);

                    // quick facts
                    String facts = "";
                    facts = facts.concat(userProfile.morningPerson ? "Early bird\n" : "");
                    facts = facts.concat(userProfile.playsMusic ? "Plays Music out loud\n" : "");
                    facts = facts.concat(userProfile.isVisited ? "Has visitors\n" : "Does not have visitors\n");
                    facts = facts.concat(userProfile.isSmoker ? "Smoker\n" : "Non-smoker\n");
                    facts = facts.concat(userProfile.isTidy ? "tidy\n" : "messy\n");
                    quickFacts.setText(facts);
                }
                if (user.getPhotoUrl() != null) {
                    Glide.with(Match.this).load(user.getPhotoUrl()).into(profile);
                    //TODO: make this draw the other user's profile pic... somehow. maybe store the url in the user profile class?
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Match.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logoutButton:
                logoutUser();
                break;
        }
    }

    private void getDownloadUri(StorageReference file) {
        file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Toast.makeText(Match.this, "Uploading image.", Toast.LENGTH_LONG).show();
                setUserProfileImage(uri);
            }
        });
    }

    private void setUserProfileImage(Uri uri) {
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
        user.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Match.this, "Uploaded successful.", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Match.this, "Uploaded failed.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(Match.this,"Logged out", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Match.this, MainActivity.class);
        startActivity(intent);
    }

}
