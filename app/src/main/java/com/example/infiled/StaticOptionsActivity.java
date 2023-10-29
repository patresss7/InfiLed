package com.example.infiled;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StaticOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_options);

        Button buttonLaunchNewActivity = findViewById(R.id.buttonCustom);
        buttonLaunchNewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StaticOptionsActivity.this, CustomStaticActivity.class);
                startActivity(intent);
            }
        });
    }
}