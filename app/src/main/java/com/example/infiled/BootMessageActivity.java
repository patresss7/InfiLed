package com.example.infiled;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class BootMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot_message);

        // Create a Handler to delay launching the DelayedActivity
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an Intent to start the DelayedActivity
                Intent intent = new Intent(BootMessageActivity.this, ChooseDeviceActivity.class);
                startActivity(intent);
            }
        }, 2000); // 2-second delay
    }
}