package com.example.compatableroomatesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

public class UpdateQuestions extends AppCompatActivity {

    SwitchCompat morningSwitch;
    SwitchCompat musicSwitch;
    SwitchCompat smokerSwitch;
    SwitchCompat friendSwitch;
    SwitchCompat cleanSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_questions);

        morningSwitch = (SwitchCompat) findViewById(R.id.MorningSwitch);
        musicSwitch = (SwitchCompat) findViewById(R.id.MusicSwitch);
        smokerSwitch = (SwitchCompat) findViewById(R.id.SmokerSwitch);
        friendSwitch = (SwitchCompat) findViewById(R.id.FriendsSwitch);
        cleanSwitch = (SwitchCompat) findViewById(R.id.CleanSwitch);

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
}