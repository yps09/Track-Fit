package com.android.trackfit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

public class WorkoutActivity extends AppCompatActivity {
 private boolean isSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        // Find the TabLayout and ViewPager in your layout
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        // Create an adapter to manage the fragments for each tab
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());

        // Add fragments for each tab
        tabAdapter.addFragment(new OutdoorFragment(), "Outdoor");
        tabAdapter.addFragment(new IndoorFragment(), "Indoor");

        // Set the adapter to the ViewPager
        viewPager.setAdapter(tabAdapter);

        // Connect the TabLayout to the ViewPager
        tabLayout.setupWithViewPager(viewPager);

        // For day selector

        // Initialize TextViews for each day
        TextView mondayTextView = findViewById(R.id.mondayTextView);
        TextView tuesdayTextView = findViewById(R.id.tuesdayTextView);
        TextView wednesdayTextView = findViewById(R.id.wednesdayTextView);
        TextView thursdayTextView = findViewById(R.id.thursdayTextView);
        TextView fridayTextView = findViewById(R.id.fridayTextView);
        TextView saturdayTextView = findViewById(R.id.saturdayTextView);
        TextView sundayTextView = findViewById(R.id.sundayTextView);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) CardView userprofile  = findViewById(R.id.userprofile);

        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkoutActivity.this,TempProfileActivity.class));
            }
        });

        // Set OnClickListener for each day
        mondayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDaySelection("Monday");
            }
        });

        tuesdayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDaySelection("Tuesday");
            }
        });

        wednesdayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDaySelection("Wednesday");
            }
        });

        thursdayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDaySelection("Thursday");
            }
        });

        fridayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDaySelection("Friday");
            }
        });

        saturdayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDaySelection("Saturday");
            }
        });

        sundayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDaySelection("Sunday");
            }
        });
    }

    // Method to change the background color of the selected day TextView
    private void changeDayBackgroundColor(String selectedDay) {
        int selectedColor = getResources().getColor(R.color.selectedDayColor); // Replace with your color resource
        int normalColor = getResources().getColor(android.R.color.system_accent2_200); // Replace with your normal color
        @SuppressLint("ResourceType") int dayContainer=getResources().getColor(R.id.dayContainer);

        // Reset background color for all days
        resetDayBackgroundColors();

        // Set background color for the selected day
        switch (selectedDay) {
            case "Monday":
                findViewById(R.id.mondayTextView).setBackgroundColor(selectedColor);
                break;
            case "Tuesday":
                findViewById(R.id.tuesdayTextView).setBackgroundColor(selectedColor);
                break;
            case "Wednesday":
                findViewById(R.id.wednesdayTextView).setBackgroundColor(selectedColor);
                break;
            case "Thursday":
                findViewById(R.id.thursdayTextView).setBackgroundColor(selectedColor);
                break;
            case "Friday":
                findViewById(R.id.fridayTextView).setBackgroundColor(selectedColor);
                break;
            case "Saturday":
                findViewById(R.id.saturdayTextView).setBackgroundColor(selectedColor);
                break;
            case "Sunday":
                findViewById(R.id.sundayTextView).setBackgroundColor(selectedColor);
                break;
            default:
                findViewById(R.id.dayContainer).setBackgroundColor(normalColor);
                break;
        }
    }

    // Method to reset the background color for all days
    private void resetDayBackgroundColors() {
        TextView[] dayTextViews = {
                findViewById(R.id.mondayTextView),
                findViewById(R.id.tuesdayTextView),
                findViewById(R.id.wednesdayTextView),
                findViewById(R.id.thursdayTextView),
                findViewById(R.id.fridayTextView),
                findViewById(R.id.saturdayTextView),
                findViewById(R.id.sundayTextView),
        };

        int normalColor = getResources().getColor(android.R.color.background_light); // Replace with your normal color

        for (TextView dayTextView : dayTextViews) {

            dayTextView.setBackgroundColor(normalColor);
        }
    }

    // Method to handle the selection logic for each day
    private void handleDaySelection(String selectedDay) {
        // Example logic: Show a toast message with the selected day
        Toast.makeText(WorkoutActivity.this, "Selected day: " + selectedDay, Toast.LENGTH_SHORT).show();

        // Example: Start a specific workout routine
        startWorkoutRoutine(selectedDay);

        // Example: Update UI components based on the selected day
        changeDayBackgroundColor(selectedDay); // Add this line to change the background color
    }

    // Example: Method to start a specific workout routine based on the selected day
    private void startWorkoutRoutine(String selectedDay) {
        // Add your code to start the workout routine for the selected day
        // This could involve launching a new activity, fragment, or any workout-related logic
        Toast.makeText(WorkoutActivity.this, "Starting workout routine for " + selectedDay, Toast.LENGTH_SHORT).show();
    }

    // Example: Method to update UI components based on the selected day

}
