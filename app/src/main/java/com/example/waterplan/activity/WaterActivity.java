package com.example.waterplan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.waterplan.R;


public class WaterActivity extends AppCompatActivity {
    ImageView imageView; //imageView 변수 선언
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    EditText editTextNumber;

    public int goalWater;
    public int totalWater = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        imageView = findViewById(R.id.imageView);// imageView 변수를 찾기
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        editTextNumber = findViewById(R.id.editTextNumber);
        // t1 = findViewById(R.id.changImage1);

        Intent intent = getIntent();
        int param = intent.getIntExtra("goalWater", 0);
        goalWater = param;
        Button button = findViewById(R.id.inputButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editText = editTextNumber.getText().toString();

                int inputData = Integer.parseInt(editText);
                setOneleter(inputData);

            }

        });
        Button newButton= findViewById(R.id.newButton);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("goalWater","totalWater");
                startActivity(intent);
                finish();
            }
        });

        Button alarmButton = findViewById(R.id.alarmButton);
        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LitterActivity.class);
                intent.putExtra("goalWater","totalWater");
                startActivity(intent);
                finish();
            }
        });
    }




    public void changeImage1() {
        imageView.setVisibility(View.VISIBLE); //보이게 하기
        imageView2.setVisibility(View.GONE); //안보이게 하기
        imageView3.setVisibility(View.GONE); //안보이게 하기
        imageView4.setVisibility(View.GONE); //안보이게 하기
        imageView5.setVisibility(View.GONE); //안보이게 하기
    }

    public void Button2onClick(View v) {
        totalWater = 0;
    }

    public void changeImage2() {
        imageView.setVisibility(View.GONE);
        imageView2.setVisibility(View.VISIBLE);
        imageView3.setVisibility(View.GONE);
        imageView4.setVisibility(View.GONE);
        imageView5.setVisibility(View.GONE);
    }

    public void changeImage3() {
        imageView.setVisibility(View.GONE);
        imageView2.setVisibility(View.GONE);
        imageView3.setVisibility(View.VISIBLE);
        imageView4.setVisibility(View.GONE);
        imageView5.setVisibility(View.GONE);
    }

    public void changeImage4() {
        imageView.setVisibility(View.GONE);
        imageView2.setVisibility(View.GONE);
        imageView3.setVisibility(View.GONE);
        imageView4.setVisibility(View.VISIBLE);
        imageView5.setVisibility(View.GONE);
    }




    public void changeImage5() {
        imageView.setVisibility(View.GONE);
        imageView2.setVisibility(View.GONE);
        imageView3.setVisibility(View.GONE);
        imageView4.setVisibility(View.GONE);
        imageView5.setVisibility(View.VISIBLE);
    }

    public void setOneleter(int water) {
        totalWater += water;

        double per = (double) totalWater / (double) goalWater;


        int current = (int) (per * 100);
        // 오늘 마실물의 양을 1L라고 설정했을때의 조건식
        if (25 > current && 0 <= current) {
            changeImage1();
        } else if (25 <= current && 50 > current) {
            changeImage2();
        } else if (50 <= current && 75 > current) {
            changeImage3();
        } else if (75 <= current && 100 > current) {
            changeImage4();
        } else if (100 <= current) {
            changeImage5();
        }

    }

}