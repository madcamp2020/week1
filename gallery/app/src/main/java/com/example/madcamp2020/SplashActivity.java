package com.example.madcamp2020;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Handler handler = new Handler();
        handler.postDelayed(new splashhandler(), 2000);  // 3000Millis = 3초, 로딩 시간


    }

    private class splashhandler implements Runnable{
        @Override
        public void run() {
            startActivity(new Intent(getApplication(), MainActivity.class)); // 로딩 끝나고, 메인으로
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        // 뒤로 가기 못하게
    }
}