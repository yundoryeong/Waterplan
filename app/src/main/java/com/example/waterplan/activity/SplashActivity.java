package com.example.waterplan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.waterplan.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Handler handler; // 선언
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}

/*
1. 클래스
 - 클래스의 구성 요소 : 변수, 메서드(함수) / 얘들을 통틀어서 멤버 혹은 필드라고 함.
 - 객체 생성 및 초기화
        ex) Button
        Button

*/