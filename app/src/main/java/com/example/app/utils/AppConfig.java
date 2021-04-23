package com.example.app.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

public class AppConfig extends Application {
    public static Context context;
    public static Activity activity;
    public static Gson gson =new Gson();
    //图片圆角为30
    public static RequestOptions options = new RequestOptions().bitmapTransform(new RoundedCorners(30));
    public static RequestOptions circle = new RequestOptions().circleCropTransform();
    public static SharedPreferences spf;
    public static SharedPreferences.Editor spfEditor;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        spf = getSharedPreferences("appInfo", MODE_PRIVATE);
        spfEditor = getSharedPreferences("appInfo", MODE_PRIVATE).edit();
    }
}
