package com.android.trackfit;

import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;
import com.google.android.gms.location.LocationRequest;
import android.os.Looper;

public class RunningActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private long remainingTime;
    TextView runningResultTextView;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private double totalDistance;
    private Location initialLocation;
    private Boolean isInitialLocationSet  = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        long duration = TimeUnit.MINUTES.toMillis(30);
        timerTextView = findViewById(R.id.timerTextView);
        Button startButton = findViewById(R.id.startButton);
        runningResultTextView = findViewById(R.id.runningResultTextView);

        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Check for location permissions at runtime (for devices running Android 6.0 and above)
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
                runningResultTextView.setText("Your Running Result Here");
                startActivity(new Intent(RunningActivity.this, WorkoutActivity.class));
                finish();
            }
        };

        // Set up onClickListener for the startButton
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the timer when the button is clicked
                toggleTimer();
            }
        });
    }


    // Method to update the distance based on location updates
    private void updateDistance(Location location) {
        // Assuming your initial location is stored in a variable called initialLocation
        if (initialLocation != null) {
            float distance = initialLocation.distanceTo(location);
            totalDistance += distance;
            // Update your UI or perform other actions based on the total distance
            //updateDistanceUI(totalDistance);
        }
    }

    private void updateDistanceUI(String distance) {
        TextView distanceTextView = findViewById(R.id.distanceTextView);
        distanceTextView.setText("Distance: " + distance + " meters");
    }

    // Start location updates when the timer starts
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


    // Stop location updates when the timer is paused or finished
    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }



    // Method to toggle the timer (start or pause)
    private void toggleTimer() {
        if (isRunning) {
            // If running, pause the timer and stop location updates
            pauseRunning();
            stopLocationUpdates();
        } else {
            // If not running, check for location permission and start or resume the timer and location updates
            if (checkLocationPermission()) {
                startRunning();
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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return false;
        }
    }

    // Method to start or resume the timer
    private void startRunning() {
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
                    runningResultTextView.setText("Your Running Result Here");
                    startActivity(new Intent(RunningActivity.this, WorkoutActivity.class));
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

    // Method to pause the running timer
    private void pauseRunning() {
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
        timerTextView.setText("Timer: " + min +"min "+sec+"sec");
        Button startButton = findViewById(R.id.startButton);
        // Update the button text based on the timer state
        startButton.setText(isRunning ? "Pause" : "Resume");
    }
}
