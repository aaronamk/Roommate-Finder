package com.example.compatableroomatesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Update extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID, fullname, gradYear;
    private Button update, logout;
    private EditText editFullName, editGradYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        editFullName =  findViewById(R.id.editTextPersonName);
        editGradYear =  findViewById(R.id.editTextGraduation);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null){
                    fullname = userProfile.fullName;
                    gradYear = userProfile.gradYear;

                    editFullName.setText(fullname);
                    editGradYear.setText(gradYear);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Update.this,"Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
        update = findViewById(R.id.editButton);
        update.setOnClickListener(this);
        logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editButton:
                updateUser();
                finish(); // back to profile
                break;
            case R.id.logoutButton:
                logoutUser();
                break;
        }
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(Update.this,"Logout Successful", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Update.this, MainActivity.class);
        startActivity(intent);
    }

    private void updateUser() {
        Boolean changed = false;
        if(isNameChanged()) {
            changed = true;
        }
        if(isGradYearChanged()){
            changed = true;
        }
        if(changed){
            Toast.makeText(Update.this, "Profile updated!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(Update.this, "Profile is the same and can not be updated!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isGradYearChanged() {
        String editYearString = editGradYear.getText().toString();
        if(!fullname.equals(editYearString)){
            reference.child(userID).child("gradYear").setValue(editGradYear.getText().toString().trim());
            return true;
        }else{
            return false;
        }

    }

    private boolean isNameChanged() {
        String editNameString = editFullName.getText().toString();
        if(!fullname.equals(editNameString)){
            if(editNameString.isEmpty()){
                editFullName.setError("Full Name is required!");
                editFullName.requestFocus();
                return false;
            }
            reference.child(userID).child("fullName").setValue(editFullName.getText().toString().trim());
            return true;
        }else{
            return false;
        }
    }
}