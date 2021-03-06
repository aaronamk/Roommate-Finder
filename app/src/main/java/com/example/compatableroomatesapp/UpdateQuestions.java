package com.example.compatableroomatesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateQuestions extends AppCompatActivity implements View.OnClickListener{

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private boolean morningPerson, playsMusic, isSmoker, isVisited, isTidy;

    private SwitchCompat morningSwitch, musicSwitch, smokerSwitch, friendSwitch, cleanSwitch;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_questions);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        morningSwitch = findViewById(R.id.MorningSwitch);
        musicSwitch = findViewById(R.id.MusicSwitch);
        smokerSwitch = findViewById(R.id.SmokerSwitch);
        friendSwitch = findViewById(R.id.FriendsSwitch);
        cleanSwitch = findViewById(R.id.CleanSwitch);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    if (userProfile.morningPerson) morningSwitch.toggle();
                    if (userProfile.playsMusic) musicSwitch.toggle();
                    if (userProfile.isSmoker) smokerSwitch.toggle();
                    if (userProfile.isVisited) friendSwitch.toggle();
                    if (userProfile.isTidy) cleanSwitch.toggle();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateQuestions.this,"Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);


        morningSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                morningPerson = isChecked;
            }
        });

        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                playsMusic = isChecked;
            }
        });

        smokerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSmoker = isChecked;
            }
        });

        friendSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isVisited = isChecked;
            }
        });

        cleanSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isTidy = isChecked;
            }
        });
    }

    @Override
    public void onBackPressed() {
        // pass
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton:
                updateDatabase();
                updatePersonality();
                break;
        }
    }

    private void updateDatabase() {
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    reference.child(userID).child("morningPerson").setValue(morningPerson);
                    reference.child(userID).child("playsMusic").setValue(playsMusic);
                    reference.child(userID).child("isSmoker").setValue(isSmoker);
                    reference.child(userID).child("isVisited").setValue(isVisited);
                    reference.child(userID).child("isTidy").setValue(isTidy);
                    reference.child(userID).child("UID").setValue(userID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateQuestions.this,"Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updatePersonality() {
        Intent intent = new Intent(UpdateQuestions.this, PersonalityTest.class);
        startActivity(intent);
    }
}