package com.android.trackfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.android.trackfit.databinding.ActivityTempProfileBinding;

public class TempProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ActivityTempProfileBinding binding;

    private TextView usernameFromDatabase, emailFromDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        binding = ActivityTempProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize TextViews
        usernameFromDatabase = findViewById(R.id.username_from_database);
        emailFromDatabase = findViewById(R.id.getemail_from_database);

        binding.logOutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(TempProfileActivity.this, MainActivity.class));
                finish(); // Finish the current activity to prevent going back to it on logout
            }
        });

        // Call the method to retrieve username and email
        getUsernameAndEmailFromFirebase();
    }

    private void getUsernameAndEmailFromFirebase() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();

            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(uid);

            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.child("username").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);

                        // Set the username and email in the TextViews
                        usernameFromDatabase.setText(username);
                        emailFromDatabase.setText(email);

                        Log.d("Firebase", "Username from database: " + username);
                        Log.d("Firebase", "Email from database: " + email);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                    Log.e("Firebase", "Error retrieving from database: " + databaseError.getMessage());
                }
            });
        }
    }
}
