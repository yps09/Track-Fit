package com.android.trackfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class CyclingActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private TextView textTimerView;

    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private long remainingTime;
    private TextView CyclingResultTextView;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private double totalDistance;
    private Location initialLocation;
    private boolean isInitialLocationSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycling);

        textTimerView = findViewById(R.id.timerTextView);
        long duration = TimeUnit.MINUTES.toMillis(30);
        Button startButton = findViewById(R.id.startButton);
        CyclingResultTextView = findViewById(R.id.cyclingActivityResult);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.d("onLocationUpdateResults", "onLocationResult: ");
                if (locationResult != null) {
                    if (!isInitialLocationSet){
                        initialLocation = locationResult.getLastLocation();
                        isInitialLocationSet = true;
                    }
                    DecimalFormat df = new DecimalFormat("#.##");
                    updateDistanceUI(df.format(locationResult.getLastLocation().distanceTo(initialLocation)));
                }
            }
        };

        // Initialize the CountDownTimer
        countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                // Handle timer finish logic
                CyclingResultTextView.setText("Your Running Result Here");
                startActivity(new Intent(CyclingActivity.this, WorkoutActivity.class));
                finish();
            }
        };

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTimer();
            }
        });
    }

    private void updateDistance(Location location) {
        // Assuming your initial location is stored in a variable called initialLocation
        if (initialLocation != null) {
            float distance = initialLocation.distanceTo(location);
            totalDistance += distance;
            // Update your UI or perform other actions based on the total distance
            //updateDistanceUI(totalDistance);
        }
    }

    private void updateDistanceUI(String format) {
        TextView disttancetextview = findViewById(R.id.cyclingDistanceTextView);
        disttancetextview.setText("Distance : " + format + "meter");
    }

    private void startLocationUpdates() {
        // Check for location permissions
        if (checkLocationPermissions()) {
            // Permissions are granted, proceed with location updates
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000); // Update every 1 second

            try {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            } catch (SecurityException e) {
                // Handle the exception (e.g., show a message, request permissions again, etc.)
                e.printStackTrace();
            }
        } else {
            // Location permissions not granted, handle accordingly (show a message, request permissions again, etc.)
            requestLocationPermissions();
        }
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private boolean checkLocationPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void toggleTimer() {
        if (isRunning) {
            // If running, pause the timer and stop location updates
            pauseCycling();
            stopLocationUpdates();
        } else {
            // If not running, check for location permission and start or resume the timer and location updates
            if (checkLocationPermission()) {
                startCycling();
                startLocationUpdates();
            } else {
                // Handle the case where the user hasn't granted the location permission
                // You may show a message or request the permission again
            }
        }
    }

    private boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            // You may request the location permission here or handle it in your UI
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return false;
        }
    }

    private void startCycling() {
        if (remainingTime > 0) {
            // If there is remaining time, create a new CountDownTimer with the remaining time
            countDownTimer = new CountDownTimer(remainingTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    remainingTime = millisUntilFinished;
                    updateTimerText();
                }

                @Override
                public void onFinish() {
                    // Handle timer finish logic
                    CyclingResultTextView.setText("Your Running Result Here");
                    startActivity(new Intent(CyclingActivity.this, WorkoutActivity.class));
                    finish();
                }
            }.start();
        } else {
            // If no remaining time, start from the initial duration
            countDownTimer.start();
        }
        // Set isRunning to true when starting or resuming the timer
        isRunning = true;
        // Update the button text
        updateTimerText();
    }

    private void pauseCycling() {
        // Cancel the CountDownTimer
        countDownTimer.cancel();
        // Set isRunning to false when pausing the timer
        isRunning = false;
        // Update the button text
        updateTimerText();
    }

    // Method to update the timer text and button text
    private void updateTimerText() {
        long seconds = remainingTime / 1000;
        long sec = seconds % 60;
        long hour = seconds / 60;
        long min = hour % 60;
        textTimerView.setText("Timer: " + min + "min " + sec + "sec");
        Button startButton = findViewById(R.id.startButton);
        // Update the button text based on the timer state
        startButton.setText(isRunning ? "Pause" : "Resume");
    }
}
