package com.android.trackfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.trackfit.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    FirebaseAuth mAuth;
    TextView ForgotActivity,gotoSignUpbtn;

    EditText username,password;
    CardView Loginbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ForgotActivity = findViewById(R.id.textView3);
        gotoSignUpbtn = findViewById(R.id.gotoSignUp);
        Loginbtn = findViewById(R.id.btnlogin);

        mAuth = FirebaseAuth.getInstance();

        // Check if the user is already logged in
        if (mAuth.getCurrentUser() != null) {
            // User is already logged in, redirect to HomeActivity
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();  // Finish the MainActivity to prevent going back to it on pressing back from HomeActivity
        }


        ForgotActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ForgotActivity.class));

            }
        });
        gotoSignUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SingUp.class));
            }
        });

        Loginbtn.setOnClickListener(v -> {
            // Get text from EditText fields
            String usernameText = binding.username.getText().toString().trim();
            String passwordText = binding.password.getText().toString().trim();

            if (usernameText.equals("")) {
                // Show error for empty username
                binding.username.setError("Please Enter your username");
            } else if (passwordText.equals("")) {
                // Show error for empty password
                binding.password.setError("Please enter your password");
            } else {
                // Clear any previous errors
                binding.username.setError(null);
                binding.password.setError(null);

                // Attempt to sign in with Firebase
                mAuth.signInWithEmailAndPassword(usernameText, passwordText)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // Login successful
                                Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Login failed
                                Toast.makeText(MainActivity.this, "Login failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


    }
}