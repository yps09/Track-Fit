package com.android.trackfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.android.trackfit.databinding.ActivitySplashBinding;

public class Splash extends AppCompatActivity {

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Handler handler = new Handler();
        // Show text views at intervals
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.workoutSplashScreenImageView.setImageResource(R.drawable.dumble);
                binding.gymTxtBelowDumbleImg.setText("Track Fit");
                binding.descriptionTxt.setText("Work out anytime, anywhere with a digital Digital Phone");
                binding.runnerImg.setImageResource(R.drawable.runningimg);
                binding.getRoot().setVisibility(View.VISIBLE);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.workoutSplashScreenImageView.setImageResource(R.drawable.dumble);
                        binding.gymTxtBelowDumbleImg.setText("Track Fit");
                        binding.runnerImg.setImageResource(R.drawable.workout1);
                        // binding.indiccantImage.setImageResource(R.drawable.indicator_2);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                binding.workoutSplashScreenImageView.setImageResource(R.drawable.dumble);
                                binding.gymTxtBelowDumbleImg.setText("Track Fit");
                                binding.runnerImg.setImageResource(R.drawable.workout3);

                                startActivity(new Intent(Splash.this,MainActivity.class));
                                finish();
                            }
                        }, 2000); // Show textView3 after 2 seconds
                    }
                }, 2000); // Show textView2 after 2 seconds
            }
        }, 1000); // Show textView1 after 2 seconds
    }

}