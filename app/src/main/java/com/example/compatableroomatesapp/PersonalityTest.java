package com.example.compatableroomatesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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


public class PersonalityTest extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference reference;
    private WebView webView;
    private EditText personality;
    private Button next;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personality_test);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        webView = (WebView) findViewById(R.id.personalityTestLink);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.personalityperfect.com/test/free-personality-test/");

        personality = findViewById(R.id.editPersonalityType);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null){
                    personality.setText(userProfile.personality);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PersonalityTest.this,"Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });


        next = findViewById(R.id.submitButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String result = personality.getText().toString().trim();
                if(!result.isEmpty()){
                    if(result.length() != 4){
                        Toast.makeText(PersonalityTest.this,"Please insert a valid personality test result!", Toast.LENGTH_LONG).show();
                    }
                    reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User userProfile = snapshot.getValue(User.class);
                            if(userProfile != null){
                                reference.child(userID).child("personality").setValue(result);
                                Intent intent = new Intent(PersonalityTest.this, Profile.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(PersonalityTest.this,"Something went wrong!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        // pass
    }
}