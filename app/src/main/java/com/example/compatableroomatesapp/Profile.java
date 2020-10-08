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

public class Profile extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser user;
    private DatabaseReference reference;

    private TextView fullName, personality, bio;
    private Button request, logout, image, match;
    private ImageButton edit;
    private ImageView profile;
    private StorageReference storageReference;

    private String userID, matchID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // add visual elements
        edit = findViewById(R.id.editButton);
        edit.setOnClickListener(this);
        logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(this);
        fullName = findViewById(R.id.fullName);
        personality = findViewById(R.id.personality);
        bio = findViewById(R.id.bio);
        image = findViewById(R.id.imageButton);
        image.setOnClickListener(this);
        profile = findViewById(R.id.profile_pic);

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
                if(userProfile != null) {
                    fullName.setText(userProfile.fullName);
                    personality.setText(userProfile.personality);
                    bio.setText(userProfile.bio);
                }
                if(user.getPhotoUrl() != null ){
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
                matcher();
        }
    }

    private void matcher() {
        reference.child(userID).child("matched").setValue(true);
        Intent intent1 = new Intent(Profile.this, User.class);
        startActivity(intent1);

        //ABOVE THIS WORKS
        FirebaseDatabase.getInstance().getReference().child("Users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            User potential_match = snap.getValue(User.class);
                            Toast.makeText(Profile.this,""+snap.getKey(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Profile.this,"Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                });

        //NEW THING I WANT TO TRY OUT
        /*public void onDataChange(DataSnapshot snapshot) {
            Iterator<DataSnapshot> items = snapshot.getChildren().iterator();
            int counter = 0;
            while (items.hasNext()) {
                DataSnapshot item = items.next();
                Log.i("Result", "email" + item.child("email").getValue().toString());
                Log.i("Result", "User ID" + item.child("userId").getValue().toString());
                Log.i("Result", "User Name" + item.child("username").getValue().toString());
            }
        }*/






       // reference.orderByChild("matched").equalTo(false).limitToFirst(1).addValueEventListener(new ValueEventListener() {
          //  @Override
          //  public void onDataChange(@NonNull DataSnapshot matchedUser) {
                //User userProfile = matchedUser.getValue(User.class);
                //matchID = matchedUser.getKey();

                /*reference.child(matchID).child("matchedUID").setValue(userID);
                reference.child(matchID).child("matched").setValue(true);
                reference.child(userID).child("matchUID").setValue(matchID);
                Intent intent = new Intent(Profile.this, User.class);
                startActivity(intent);*/
            //    Toast.makeText(Profile.this,"Testing HERE IS THE KEY"+matchID, Toast.LENGTH_LONG).show();
           // }

          //  @Override
          //  public void onCancelled(@NonNull DatabaseError error) {
          //      Toast.makeText(Profile.this,"Something went wrong!", Toast.LENGTH_LONG).show();
         //   }

        //});



        /*reference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {
                User userProfile = snap.getValue(User.class);
                if(userProfile.matchUID == null){
                    reference.child(userID).child("matched").setValue(false);
                    Intent intent3 = new Intent(Profile.this, User.class);
                    startActivity(intent3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this,"Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });*/
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
