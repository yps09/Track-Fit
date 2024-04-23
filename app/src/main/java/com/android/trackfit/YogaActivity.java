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


public class YogaActivity extends AppCompatActivity {

    private TextView timerTextView, caloriesBurnedTextView, yogaActivityResult;
    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private long remainingTime;

    LinearLayout instructionStepsLy;
    CardView instructionCardView;

    private double caloriesBurned;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga);

        long duration = TimeUnit.MINUTES.toMillis(5);
        timerTextView = findViewById(R.id.timerTextView);
        Button startButton = findViewById(R.id.startButton);
        yogaActivityResult = findViewById(R.id.yogaActivityResult);
        caloriesBurnedTextView = findViewById(R.id.caloriesBurnedTextView);
        instructionStepsLy = findViewById(R.id.InstructionStepLy);
        instructionCardView = findViewById(R.id.instructionCardView);

        countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                calculateCaloriesBurned();
                yogaActivityResult.setText("Your Running Result Here");
                //startActivity(new Intent(YogaActivity.this, WorkoutActivity.class)
                       // .putExtra("caloriesBurned", caloriesBurned));
                //finish();
            }
        };

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTimer();
            }
        });

        //for Instruction Button
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

    private void toggleTimer() {
        if (isRunning) {
            pauseRunning();
        } else {
            if (checkLocationPermission()) {
                startRunning();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void startRunning() {
        if (remainingTime > 0) {
            countDownTimer = new CountDownTimer(remainingTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    remainingTime = millisUntilFinished;
                    updateTimerText();
                }

                @Override
                public void onFinish() {
                    calculateCaloriesBurned();
                    yogaActivityResult.setText("Your Running Result Here");
                    //startActivity(new Intent(YogaActivity.this, WorkoutActivity.class)
                          //  .putExtra("caloriesBurned", caloriesBurned));
                    //finish();
                }
            }.start();
        } else {
            countDownTimer.start();
        }
        isRunning = true;
        updateTimerText();
    }

    private void pauseRunning() {
        countDownTimer.cancel();
        isRunning = false;
        updateTimerText();
    }

    private void updateTimerText() {
        long seconds = remainingTime / 1000;
        long sec = seconds % 60;
        long hour = seconds / 60;
        long min = hour % 60;
        timerTextView.setText("Timer: " + min + "min " + sec + "sec");

        Button startButton = findViewById(R.id.startButton);
        startButton.setText(isRunning ? "Pause" : "Resume");
    }

    public void calculateCaloriesBurned() {
        int userWeight = 70; // Replace with the user's weight
        int durationInMinutes = 5;
        String activityType = "Yoga"; // You can dynamically set the activity type

        // Call your updated calorie calculation method
        caloriesBurned = calculateCalories(activityType, durationInMinutes, userWeight);
        caloriesBurnedTextView.setText("Score: " + caloriesBurned +"calories/hour");
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