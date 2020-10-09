package com.example.compatableroomatesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.*;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth Auth;
    private EditText editTextPersonName, editTextPassword, editTextEmailAddress, editTextGraduation;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Auth = FirebaseAuth.getInstance();

        register = findViewById(R.id.registerButton);
        register.setOnClickListener(this);

        editTextPersonName = findViewById(R.id.editTextPersonName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        editTextGraduation = findViewById(R.id.editTextGraduation);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerButton:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        final String email = editTextEmailAddress.getText().toString().trim();
        final String name = editTextPersonName.getText().toString().trim();
        final String gradYear = editTextGraduation.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (name.isEmpty()) {
            editTextPersonName.setError("Full Name is required!");
            editTextPersonName.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            editTextEmailAddress.setError("Email is required!");
            editTextEmailAddress.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmailAddress.setError("Email is not Valid!");
            editTextEmailAddress.requestFocus();
            return;
        }
        if (email.length() < 7 || !email.substring(email.length() - 6).equals("wm.edu")) {
            editTextEmailAddress.setError("Must use your W&M email address");
            editTextEmailAddress.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 8) {
            editTextPassword.setError("Password should be at least 8 characters");
            editTextPassword.requestFocus();
            return;
        }
        if (!password.matches(".*\\D.*\\D.*")) {
            editTextPassword.setError("Password should contain at least 2 letters");
            editTextPassword.requestFocus();
            return;
        }
        if (!password.matches(".*\\d.*\\d.*")) {
            editTextPassword.setError("Password should contain at least 2 numbers");
            editTextPassword.requestFocus();
            return;
        }
        if (!password.matches(".*[A-Z].*")) {
            editTextPassword.setError("Password should contain at least 1 capital letter");
            editTextPassword.requestFocus();
            return;
        }

        Auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(name,email,false,false, false, false, false);
                    if (!gradYear.isEmpty()) user.setGradYear(gradYear);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (task.isSuccessful()) {
                                user.sendEmailVerification();
                                Toast.makeText(Register.this, "User has been registered, check your email for verification!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Register.this, UpdateQuestions.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Register.this,"Failed to register! Try again!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Register.this,"Failed to register user! Try again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}