package com.example.app.utils;

import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {
    private static final String TAG = "HttpUtils";
    private static OkHttpClient client = new OkHttpClient.Builder()
            .proxy(Proxy.NO_PROXY)
            .connectTimeout(5,TimeUnit.SECONDS)
            .build();
    public static String HOST = "http://10.189.66.144/";

    public static JSONObject send(String url, String data) throws Exception {
        FormBody.Builder body = new FormBody.Builder();
        body.add("data", data);
        Request request = new Request.Builder()
                .url(HOST + url)
                .post(body.build())
                .build();
        String string = client.newCall(request).execute().body().string();
        Log.e(TAG, "取得数据: " + string);
        return new JSONObject(string);
    }

    public static JSONObject send1(String url, String data) throws Exception {
        FormBody.Builder body = new FormBody.Builder();
        if (!data.equals("")) {
            JSONObject o = new JSONObject(data);
            Iterator<String> keys = o.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                body.add(key, o.get(key) + "");
            }
        }
        Request request = new Request.Builder()
                .url(HOST + url)
                .post(body.build())
                .build();
        String string = client.newCall(request).execute().body().string();
        Log.e(TAG, "取得数据: " + string);
        return new JSONObject(string);
    }


    public static String send(String url, File file) throws IOException {
        //2.创建RequestBody
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
        //3.构建MultipartBody
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        //4.构建请求
        Request request = new Request.Builder()
                .url(HOST + url)
                .post(requestBody)
                .build();

        //5.发送请求
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        Log.e(TAG, "返回数据: " + string);
        return string;
    }

    public static String send(String url, File file, String data) throws IOException {
        //2.创建RequestBody
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
        //3.构建MultipartBody
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .addFormDataPart("data", data)
                .build();
        //4.构建请求
        Request request = new Request.Builder()
                .url(HOST + url)
                .post(requestBody)
                .build();

        //5.发送请求
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        Log.e(TAG, "返回数据: " + string);
        return string;
    }
}
