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
import com.google.firebase.database.FirebaseDatabase;

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

    private void registerUser(){
        final String email = editTextEmailAddress.getText().toString().trim();
        final String name = editTextPersonName.getText().toString().trim();
        final String gradYear = editTextGraduation.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(name.isEmpty()){
            editTextPersonName.setError("Full Name is required!");
            editTextPersonName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextEmailAddress.setError("Email is required!");
            editTextEmailAddress.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmailAddress.setError("Email is not Valid!");
            editTextEmailAddress.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            editTextPassword.setError("Password should be greater than 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        Auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(name,email,gradYear);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Register.this, "User has been registered!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Register.this, MainActivity.class); //TODO: Make this go to the activity that starts up the questionnaire.
                                startActivity(intent);
                            }else {
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