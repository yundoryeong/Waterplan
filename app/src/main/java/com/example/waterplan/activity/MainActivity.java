package com.example.waterplan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.waterplan.R;

public class MainActivity extends AppCompatActivity {

    private Button oneLitter, oneFiveLitter, twoLitter, threeLitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        oneLitter = (Button) findViewById(R.id.oneLitter);
        oneFiveLitter = (Button) findViewById(R.id.oneFiveLitter);
        twoLitter = (Button) findViewById(R.id.twoLitter);
        threeLitter = (Button) findViewById(R.id.threeLitter);

        oneLitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WaterActivity.class);
                intent.putExtra("goalWater", 1000);
                startActivity(intent);
                finish();
            }
        });

        oneFiveLitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WaterActivity.class);
                intent.putExtra("goalWater", 1500);
                startActivity(intent);
                finish();
            }
        });

        twoLitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WaterActivity.class);
                intent.putExtra("goalWater", 2000);
                startActivity(intent);
                finish();
            }
        });

        threeLitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WaterActivity.class);
                intent.putExtra("goalWater", 3000);
                startActivity(intent);
                finish();
            }
        });
    }
}