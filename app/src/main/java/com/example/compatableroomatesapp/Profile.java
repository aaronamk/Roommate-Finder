package com.example.compatableroomatesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser user;
    private DatabaseReference reference;

    private TextView fullName, personality, bio, quickFacts;
    private Button logout, image, match;
    private ImageButton edit;
    private ImageView profile;
    private StorageReference storageReference;
    private List<User> userList;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        findViewById(R.id.rejectButton).setVisibility(View.GONE);
        findViewById(R.id.acceptButton).setVisibility(View.GONE);
        findViewById(R.id.emailTextView).setVisibility(View.GONE);
        findViewById(R.id.backMatchButton).setVisibility(View.GONE);

        // add visual elements
        edit = findViewById(R.id.editButton);
        edit.setOnClickListener(this);
        logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(this);
        fullName = findViewById(R.id.fullName);
        personality = findViewById(R.id.personality);
        bio = findViewById(R.id.bio);
        quickFacts = findViewById(R.id.quick_facts);
        image = findViewById(R.id.imageButton);
        image.setOnClickListener(this);
        profile = findViewById(R.id.profile_pic);

        userList = new ArrayList<>();

        match = findViewById(R.id.matchButton);
        match.setOnClickListener(this);

        // update profile based on firebase user data
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

        userID = user.getUid();

        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    fullName.setText(userProfile.fullName);
                    personality.setText(userProfile.personality);
                    bio.setText(userProfile.bio);

                    // quick facts
                    String facts = "";
                    facts = facts.concat(userProfile.morningPerson ? "Early bird\n" : "");
                    facts = facts.concat(userProfile.playsMusic ? "Plays Music out loud\n" : "");
                    facts = facts.concat(userProfile.isVisited ? "Has visitors\n" : "Does not have visitors\n");
                    facts = facts.concat(userProfile.isSmoker ? "Smoker\n" : "Non-smoker\n");
                    facts = facts.concat(userProfile.isTidy? "tidy\n" : "messy\n");
                    quickFacts.setText(facts);
                }
                if (user.getPhotoUrl() != null) {
                    Glide.with(Profile.this).load(user.getPhotoUrl()).into(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this,"Something went wrong!", Toast.LENGTH_LONG).show();
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
            case R.id.editButton:
                // switch to editing screen
                Intent intent = new Intent(Profile.this, Update.class);
                startActivity(intent);
                break;
            case R.id.logoutButton:
                logoutUser();
                break;
            case R.id.imageButton:
                Intent select_image = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                select_image.setType("image/*");
                startActivityForResult(select_image, 246);
                break;
            case R.id.matchButton:
                reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userProfile = snapshot.getValue(User.class);
                        if (userProfile != null) {
                            if (userProfile.matched) {
                                Intent otherProf = new Intent(Profile.this, Match.class);
                                otherProf.putExtra("profileUserID", userProfile.matchUID);
                                startActivity(otherProf);
                            }
                            else {
                                matcher();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Profile.this,"Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                });
        }
    }

    private void matcher() {
          FirebaseDatabase.getInstance().getReference().child("Users")
                .orderByChild("matched").equalTo(false)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            userList.clear();
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                User potential_match = snap.getValue(User.class);
                                userList.add(potential_match);
                            }
                            matching_output_and_result();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Profile.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void matching_output_and_result() {
        String partnerUID = "";
        if (userList.size() > 1){
            do {
                Random i = new Random();
                int index = i.nextInt(userList.size());
                partnerUID = userList.get(index).UID;
            }
            while (partnerUID.equals(userID));
            reference.child(userID).child("matchUID").setValue(partnerUID);
            reference.child(userID).child("matched").setValue(true);
            set_matched_person_values(partnerUID);
        }
        else{
            reference.child(userID).child("matched").setValue(false);
        }
        userList.clear();
    }

    private void set_matched_person_values(String partnerUID) {
        reference.child(partnerUID).child("matched").setValue(true);
        reference.child(partnerUID).child("matchUID").setValue(userID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 246 && resultCode == RESULT_OK && data != null){
            Uri imageUri = data.getData();
            profile.setImageURI(imageUri);
            uploadImage(imageUri);
        }
    }

    private void uploadImage(Uri imageUri) {
        final StorageReference file = storageReference.child("profileImage").child(user.getUid()+".jpeg");
        file.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getDownloadUri(file);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile.this, "Failed image upload.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getDownloadUri(StorageReference file) {
        file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Toast.makeText(Profile.this, "Uploading image.", Toast.LENGTH_LONG).show();
                setUserProfileImage(uri);
            }
        });
    }

    private void setUserProfileImage(Uri uri) {
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
        user.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Profile.this, "Uploaded successful.", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile.this, "Uploaded failed.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(Profile.this,"Logged out", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Profile.this, MainActivity.class);
        startActivity(intent);
    }

}
