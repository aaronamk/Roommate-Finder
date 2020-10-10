package com.example.compatableroomatesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class Match extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser user;
    private DatabaseReference reference;

    private TextView fullName, personality, bio, quickFacts, emailView, back;
    private Button logout, accept, reject;
    private ImageView profile;
    private StorageReference storageReference;
    private String userID, profileUserID;
    private Boolean userAcceptedMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // hide unneeded buttons
        findViewById(R.id.editButton).setVisibility(View.GONE);
        findViewById(R.id.matchButton).setVisibility(View.GONE);
        findViewById(R.id.imageButton).setVisibility(View.GONE);
        // show needed buttons/fields
        findViewById(R.id.acceptButton).setVisibility(View.VISIBLE);
        findViewById(R.id.rejectButton).setVisibility(View.VISIBLE);
        findViewById(R.id.emailTextView).setVisibility(View.VISIBLE);
        findViewById(R.id.backMatchButton).setVisibility(View.VISIBLE);

        // add visual elements
        logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(this);
        fullName = findViewById(R.id.fullName);
        personality = findViewById(R.id.personality);
        bio = findViewById(R.id.bio);
        quickFacts = findViewById(R.id.quick_facts);
        profile = findViewById(R.id.profile_pic);
        accept = findViewById(R.id.acceptButton);
        accept.setOnClickListener(this);
        reject = findViewById(R.id.rejectButton);
        reject.setOnClickListener(this);
        emailView = findViewById(R.id.emailTextView);
        back = findViewById(R.id.backMatchButton);
        back.setOnClickListener(this);

        // update profile based on UID from intent
        profileUserID = this.getIntent().getStringExtra("profileUserID");
        reference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

        // get logged in user
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    userAcceptedMatch = userProfile.acceptedMatch;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Match.this,"Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        reference.child(profileUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    if (userProfile.acceptedMatch && userAcceptedMatch){
                        fullName.setText(userProfile.fullName);
                        emailView.setText(userProfile.email);
                        storageReference.child("profileImage").child(profileUserID + ".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(Match.this).load(uri).into(profile);
                                }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Match.this, "Match does not have a profile image", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
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
            case R.id.acceptButton:
                reference.child(userID).child("acceptedMatch").setValue(true);
                userAcceptedMatch = true;
                reference.child(profileUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userProfile = snapshot.getValue(User.class);
                        if (userProfile != null) {
                            if (userProfile.acceptedMatch && userAcceptedMatch) {
                                fullName.setText(userProfile.fullName);
                                emailView.setText(userProfile.email);
                                storageReference.child("profileImage").child(profileUserID + ".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(Match.this).load(uri).into(profile);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Match.this, "Match does not have a profile image", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Match.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                });
                Toast.makeText(Match.this, "Success! When match accepts you back, you will see more info.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rejectButton:
                reference.child(userID).child("matched").setValue(false);
                reference.child(userID).child("matchUID").setValue("");
                reference.child(userID).child("acceptedMatch").setValue(false);

                reference.child(profileUserID).child("matched").setValue(false);
                reference.child(profileUserID).child("matchUID").setValue("");
                reference.child(profileUserID).child("acceptedMatch").setValue(false);

                findViewById(R.id.editButton).setVisibility(View.VISIBLE);
                findViewById(R.id.matchButton).setVisibility(View.VISIBLE);
                findViewById(R.id.imageButton).setVisibility(View.VISIBLE);
                findViewById(R.id.rejectButton).setVisibility(View.GONE);
                findViewById(R.id.acceptButton).setVisibility(View.GONE);
                findViewById(R.id.emailTextView).setVisibility(View.GONE);
                findViewById(R.id.backMatchButton).setVisibility(View.GONE);

                Intent otherProf = new Intent(Match.this, Profile.class);
                startActivity(otherProf);
                break;
            case R.id.backMatchButton:
                findViewById(R.id.editButton).setVisibility(View.VISIBLE);
                findViewById(R.id.matchButton).setVisibility(View.VISIBLE);
                findViewById(R.id.imageButton).setVisibility(View.VISIBLE);
                findViewById(R.id.rejectButton).setVisibility(View.GONE);
                findViewById(R.id.acceptButton).setVisibility(View.GONE);
                findViewById(R.id.emailTextView).setVisibility(View.GONE);
                findViewById(R.id.backMatchButton).setVisibility(View.GONE);

                Intent backProf = new Intent(Match.this, Profile.class);
                startActivity(backProf);
                break;
        }
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(Match.this,"Logged out", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Match.this, MainActivity.class);
        startActivity(intent);
    }

}
