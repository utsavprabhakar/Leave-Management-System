package com.example.leavemanagement2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.leavemanagement2.helpers.SharedPrefManager;

public class MainActivity extends AppCompatActivity {
    private Handler mHandler;
    private Runnable mRunnable;
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;
    private int progressStatus;
    SharedPrefManager sharedPrefManager;
    private Class nextActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        initVariables();
        initViews();
        setupViews();
        mHandler.post(mRunnable);
    }

    private void initVariables() {
        mHandler = new Handler();
        mRunnable = getRunnable();
        progressStatus = 0;
        sharedPrefManager = SharedPrefManager.getInstance(this);
    }
    private Runnable getRunnable() {
        return () -> {
            if (sharedPrefManager.getCurrentEmployee() == null) {
                nextActivity = LoginActivity.class;
            } else {
                nextActivity = Dashboard.class;
            }
        };
    }


    private void setupViews() {
        initProgressBar();
    }

    private void initProgressBar() {
        new Thread(() -> {
            while (progressStatus < 100) {
                if (progressStatus == 95 && nextActivity == null) {
                    if (sharedPrefManager.getCurrentEmployee()== null) {
                        progressStatus += 5;
                        progressBar.setProgress(progressStatus);
                        nextActivity = LoginActivity.class;
                    }
                    continue;
                }
                progressStatus += 1;
                if (progressStatus == 100) {
                    if (nextActivity != null) {
                        launchNextActivity(nextActivity);
                    }
                }
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressBar.setProgress(progressStatus);
            }
        }).start();
    }
    private void launchNextActivity(Class nextActivityClass) {
        Intent intent = new Intent(this, nextActivityClass);
        startActivity(intent);
        finish();
    }
    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        relativeLayout = findViewById(R.id.rl_container);
    }
}