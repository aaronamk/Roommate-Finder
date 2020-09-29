package com.example.compatableroomatesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth Auth;
    private Button request, logout;
    private ImageButton edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Auth = FirebaseAuth.getInstance();

        edit = findViewById(R.id.editButton);
        edit.setOnClickListener(this);

        logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editButton:
                Toast.makeText(Profile.this,"edit mode", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Profile.this, Update.class);
                startActivity(intent);
                break;

            case R.id.logoutButton:
                logoutUser();
                break;
        }
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(Profile.this,"Logout Successful", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Profile.this, MainActivity.class);
        startActivity(intent);
    }

}
