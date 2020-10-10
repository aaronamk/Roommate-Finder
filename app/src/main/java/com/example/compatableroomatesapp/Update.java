package com.example.compatableroomatesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

public class Update extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID, fullName, gradYear, Bio;
    private Button next, logout, image;
    private EditText editFullName, editGradYear, editBio;
    private ImageView profile;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        image = findViewById(R.id.image2Button);
        image.setOnClickListener(this);
        profile = findViewById(R.id.imageView);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
        userID = user.getUid();

        editFullName =  findViewById(R.id.editTextPersonName);
        editGradYear =  findViewById(R.id.editTextGraduation);
        editBio = findViewById(R.id.editTextBio);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null){
                    fullName = userProfile.fullName;
                    gradYear = userProfile.gradYear;
                    Bio = userProfile.bio;

                    editFullName.setText(fullName);
                    editGradYear.setText(gradYear);
                    editBio.setText(Bio);
                }
                if(user.getPhotoUrl() != null){
                    Glide.with(Update.this).load(user.getPhotoUrl()).into(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Update.this,"Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
        next = findViewById(R.id.nextButton);
        next.setOnClickListener(this);
        logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton:
                updateUser();
                Intent intent = new Intent(Update.this, UpdateQuestions.class);
                startActivity(intent);
                break;
            case R.id.logoutButton:
                logoutUser();
                break;
            case R.id.image2Button:
                Intent select_image = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                select_image.setType("image/*");
                startActivityForResult(select_image, 246);
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
        if(isBioChanged()){
            changed = true;
        }
        if(changed){
            Toast.makeText(Update.this, "Profile updated!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(Update.this, "Profile is the same and can not be updated!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isGradYearChanged() {
        String editYearString = editGradYear.getText().toString().trim();
        if(!fullName.equals(editYearString)){
            reference.child(userID).child("gradYear").setValue(editGradYear.getText().toString().trim());
            return true;
        }else{
            return false;
        }

    }

    private boolean isNameChanged() {
        String editNameString = editFullName.getText().toString().trim();
        if(!fullName.equals(editNameString)){
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

    private boolean isBioChanged() {
        String editBioString = editBio.getText().toString().trim();
        if(Bio == null || !Bio.equals(editBioString)){
            reference.child(userID).child("bio").setValue(editBio.getText().toString().trim());
            return true;
        }else{
            return false;
        }
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
                Toast.makeText(Update.this, "Failed image upload.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getDownloadUri(StorageReference file) {
        file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Toast.makeText(Update.this, "Uploading image.", Toast.LENGTH_LONG).show();
                setUserProfileImage(uri);
            }
        });
    }

    private void setUserProfileImage(Uri uri) {
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
        user.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Update.this, "Uploaded successful.", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Update.this, "Uploaded failed.", Toast.LENGTH_LONG).show();
            }
        });
    }

}