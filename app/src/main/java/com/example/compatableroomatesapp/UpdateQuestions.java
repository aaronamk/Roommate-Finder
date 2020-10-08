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

    SwitchCompat morningSwitch;
    SwitchCompat musicSwitch;
    SwitchCompat smokerSwitch;
    SwitchCompat friendSwitch;
    SwitchCompat cleanSwitch;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID, morningPerson, playsMusic, isSmoker, isVisited, isTidy;

    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_questions);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        morningSwitch = (SwitchCompat) findViewById(R.id.MorningSwitch);
        musicSwitch = (SwitchCompat) findViewById(R.id.MusicSwitch);
        smokerSwitch = (SwitchCompat) findViewById(R.id.SmokerSwitch);
        friendSwitch = (SwitchCompat) findViewById(R.id.FriendsSwitch);
        cleanSwitch = (SwitchCompat) findViewById(R.id.CleanSwitch);

        morningPerson ="no";
        playsMusic = "no";
        isSmoker = "no";
        isVisited = "no";
        isTidy = "no";

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null){
                    System.out.println(userProfile.morningPerson);
                    if(userProfile.morningPerson.equals("yes")){
                        morningSwitch.toggle();
                    }
                    if(userProfile.playsMusic.equals("yes")){
                        musicSwitch.toggle();
                    }
                    if(userProfile.isSmoker.equals("yes")){
                        smokerSwitch.toggle();
                    }
                    if(userProfile.isVisited.equals("yes")){
                        friendSwitch.toggle();
                    }
                    if(userProfile.isTidy.equals("yes")){
                        cleanSwitch.toggle();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateQuestions.this,"Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);



        morningSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "You are a morning person", Toast.LENGTH_SHORT).show();
                    morningPerson = "yes";

                } else {
                    Toast.makeText(getApplicationContext(), "You are not a morning person", Toast.LENGTH_SHORT).show();
                    morningPerson = "no";
                }
            }
        });

        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "You like to play music", Toast.LENGTH_SHORT).show();
                    playsMusic = "yes";
                } else {
                    Toast.makeText(getApplicationContext(), "You do not like to play music", Toast.LENGTH_SHORT).show();
                    playsMusic = "no";
                }
            }
        });

        smokerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "You are a smoker", Toast.LENGTH_SHORT).show();
                    isSmoker = "yes";
                } else {
                    Toast.makeText(getApplicationContext(), "You are not a smoker", Toast.LENGTH_SHORT).show();
                    isSmoker = "no";
                }

            }
        });

        friendSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "You like to have friends over", Toast.LENGTH_SHORT).show();
                    isVisited = "yes";
                } else {
                    Toast.makeText(getApplicationContext(), "You do not like to have friends over", Toast.LENGTH_SHORT).show();
                    isVisited = "no";
                }

            }
        });

        cleanSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "You keep your room clean", Toast.LENGTH_SHORT).show();
                    isTidy = "yes";
                } else {
                    Toast.makeText(getApplicationContext(), "You do not keep your room clean", Toast.LENGTH_SHORT).show();
                    isTidy = "no";
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        // pass
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
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
                if(userProfile != null){
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

    private void updatePersonality()
    {
        Intent intent = new Intent(UpdateQuestions.this, PersonalityTest.class);
        startActivity(intent);
    }
}