package com.android.trackfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.trackfit.databinding.ActivitySingUpBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// Fully qualify the User class to avoid conflicts
import com.android.trackfit.User;

public class SingUp extends AppCompatActivity {

    ActivitySingUpBinding binding;

    TextView GotoLoginbtn;
    EditText username, email, password;
    CardView SignUp;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();
        GotoLoginbtn = findViewById(R.id.gotologin);
        SignUp = findViewById(R.id.SignUp);
        username = findViewById(R.id.username);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.password);

        GotoLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SingUp.this, MainActivity.class));
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = email.getText().toString().trim();
                String userpass = password.getText().toString().trim();
                String usernameText = username.getText().toString().trim();

                if (usernameText.isEmpty()) {
                    username.setError("Enter User Name");
                } else if (userpass.isEmpty()) {
                    password.setError("Please Enter the Password");
                } else if (useremail.isEmpty()) {
                    email.setError("Please Enter your Email");
                } else if (isValidEmail(useremail)) {
                    mAuth.createUserWithEmailAndPassword(useremail, userpass)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // Store additional user information in Firebase Realtime Database
                                    String userId = mAuth.getCurrentUser().getUid();
                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

                                    // Assuming you have a User class with properties like email and username
                                    @SuppressLint("RestrictedApi") User user = new User(useremail, usernameText);
                                    userRef.setValue(user);

                                    Toast.makeText(SingUp.this, "Account created Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SingUp.this, MainActivity.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SingUp.this, "Account creation failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(SingUp.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
