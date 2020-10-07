package com.example.compatableroomatesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

public class UpdateQuestions extends AppCompatActivity implements View.OnClickListener{

    SwitchCompat morningSwitch;
    SwitchCompat musicSwitch;
    SwitchCompat smokerSwitch;
    SwitchCompat friendSwitch;
    SwitchCompat cleanSwitch;

    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_questions);

        morningSwitch = (SwitchCompat) findViewById(R.id.MorningSwitch);
        musicSwitch = (SwitchCompat) findViewById(R.id.MusicSwitch);
        smokerSwitch = (SwitchCompat) findViewById(R.id.SmokerSwitch);
        friendSwitch = (SwitchCompat) findViewById(R.id.FriendsSwitch);
        cleanSwitch = (SwitchCompat) findViewById(R.id.CleanSwitch);

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);



        morningSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "You are a morning person", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You are not a morning person", Toast.LENGTH_SHORT).show();
                }
            }
        });

        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "You like to play music", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You do not like to play music", Toast.LENGTH_SHORT).show();
                }
            }
        });

        smokerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "You are not a smoker", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You are not a smoker", Toast.LENGTH_SHORT).show();
                }

            }
        });

        friendSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "You like to have friends over", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You do not like to have friends over", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cleanSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "You keep your room clean", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You do not keep your room clean", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.nextButton:
                updatePersonality();
                break;
        }
    }

    private void updatePersonality()
    {
        Intent intent = new Intent(UpdateQuestions.this, PersonalityTest.class);
        startActivity(intent);
    }
}