package com.android.trackfit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class PullupActivity extends AppCompatActivity {

    TextView textTimerView,pullUpTxtResult,caloriesBurnedTextView;

    private long remainingTime;
    private CountDownTimer countDownTimer;
    private boolean isRunning = false;

    Button startButton;
    LinearLayout instructionStepsLy;
    CardView instructionCardView;

    private double caloriesBurned;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pullup);

        textTimerView = findViewById(R.id.timerTextView);
        startButton = findViewById(R.id.startButton);
        pullUpTxtResult = findViewById(R.id.pullUpTxtResult);
        instructionStepsLy = findViewById(R.id.InstructionStepLy);
        instructionCardView = findViewById(R.id.instructionCardView);
        caloriesBurnedTextView = findViewById(R.id.caloriesBurnedTextView);
        long duration = TimeUnit.MINUTES.toMillis(5);

        // Initialize the CountDownTimer
        countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                calculateCaloriesBurned();
                // Handle timer finish logic
//                startActivity(new Intent(PullupActivity.this, WorkoutActivity.class));
//                finish();
            }
        };
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the timer when the button is clicked
                toggleTimer();
            }
        });
        instructionCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(instructionStepsLy.getVisibility() == View.VISIBLE){
                    instructionStepsLy.setVisibility(View.INVISIBLE);
                }else if (instructionStepsLy.getVisibility() == View.GONE){
                    instructionStepsLy.setVisibility(View.VISIBLE);
                }
                else {
                    instructionStepsLy.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    // Method to toggle the timer (start or pause)
    private void toggleTimer() {
        if (isRunning) {
            // If running, pause the timer and stop location updates
            pauseRunning();
        } else {
            // If not running, check for location permission and start or resume the timer and location updates
            if (checkLocationPermission()) {
                startRunning();

            } else {
                // Handle the case where the user hasn't granted the location permission
                // You may show a message or request the permission again
            }
        }
    }
    private boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                    calculateCaloriesBurned();
                    // Handle timer finish logic
//                    startActivity(new Intent(PullupActivity.this, WorkoutActivity.class));
//                    finish();
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
    private void pauseRunning() {
        // Cancel the CountDownTimer
        countDownTimer.cancel();
        // Set isRunning to false when pausing the timer
        isRunning = false;
        // Update the button text
        updateTimerText();
    }
    private void updateTimerText() {
        long seconds = remainingTime / 1000;
        long sec = seconds % 60;
        long hour = seconds / 60;
        long min = hour % 60;
        textTimerView.setText("Timer: " + min +"min "+sec+"sec");
        Button startButton = findViewById(R.id.startButton);
        // Update the button text based on the timer state
        startButton.setText(isRunning ? "Pause" : "Resume");
    }

    public void calculateCaloriesBurned() {
        int userWeight = 70; // Replace with the user's weight
        int durationInMinutes = 5;
        String activityType = "Yoga"; // You can dynamically set the activity type

        // Call your updated calorie calculation method
        caloriesBurned = calculateCalories(activityType, durationInMinutes, userWeight);
        caloriesBurnedTextView.setText("Score : " + caloriesBurned +"calories/hour");
    }

    // calorie calculation method
    private double calculateCalories(String activityType, int durationInMinutes, int userWeight) {
        double caloriesPerMinute;
        System.out.println(activityType + "\n" +durationInMinutes + "\n" + userWeight);
        // Set MET values based on activity type
        switch (activityType) {
            case "Yoga":
                caloriesPerMinute = 2.5; // Example MET value for yoga
                break;
            case "Running":
                caloriesPerMinute = 9.8; // Example MET value for running
                break;
            // Add more cases for other activity types if needed
            default:
                caloriesPerMinute = 1.0; // Default MET value, replace with a reasonable default
                break;
        }

        // Calculate total calories burned using the MET formula
        double totalCaloriesBurned = caloriesPerMinute * durationInMinutes * userWeight / 200.0;

        return totalCaloriesBurned;
    }
}