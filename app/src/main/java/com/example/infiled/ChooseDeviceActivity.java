package com.example.infiled;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChooseDeviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_device);
    }

    public void onChooseDeviceClick(View view) {
        Intent intent = new Intent(this, ChooseEffectActivity.class);
        startActivity(intent);
    }
}