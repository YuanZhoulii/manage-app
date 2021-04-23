package com.example.app.modules;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.app.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RelativeLayout rl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        toolbar = findViewById(R.id.toolbar);
        rl = findViewById(R.id.rl);
        toolbar.setTitle("");
        ((TextView) toolbar.getChildAt(0)).setText("关于我们");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        View aboutPage = new AboutPage(this)
//                .isRTL(false)
//                .setImage(R.mipmap.akinator)//图片
//                .setDescription("-")//介绍
//                .addItem(new Element().setTitle("Version 1.0"))
//                .addEmail("xujian@qq.com")//邮箱
//                .addWebsite("http://119.29.189.189/")//网站
//                .addPlayStore("com.example.abouttest")//应用商店
//                .addGitHub("puppet-xiaoyi")//github
//                .create();
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.akinator)//图片
                .setDescription("-")//介绍
                .addItem(new Element().setTitle("Version 1.0"))
                .create();
        rl.addView(aboutPage);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}