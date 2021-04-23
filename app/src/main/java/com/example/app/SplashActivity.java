package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.example.app.login.LoginActivity;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences spf = getSharedPreferences("appInfo", MODE_PRIVATE);
        String token = spf.getString("userId", null);
        new Handler(Looper.getMainLooper()).postDelayed((Runnable) () -> {
            Intent intent = new Intent(SplashActivity.this, TextUtils.isEmpty(token) ? LoginActivity.class : MainActivity.class);
            startActivity(intent);//载入主窗口
            finish();
        }, 2000);
    }
}