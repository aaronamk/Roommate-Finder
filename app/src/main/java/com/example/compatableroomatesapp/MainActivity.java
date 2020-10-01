package com.example.compatableroomatesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register;
    private EditText editTextEmailAddress, editTextPassword;
    private Button login;
    private FirebaseAuth Auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Auth = FirebaseAuth.getInstance();
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextPassword);
        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        login = findViewById(R.id.loginButton);
        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
                //startActivity(new Intent(this, Register.class));
                break;

            case R.id.loginButton:
                userLogin();
        }
    }

    private void userLogin(){
        final String email = editTextEmailAddress.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

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

        Auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()) {
                        Toast.makeText(MainActivity.this,"Login Successful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, Profile.class);
                        startActivity(intent);
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Please check your email and verify your account!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this,"Failed to login user! Try again!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
