package com.example.app.page;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.app.MainActivity;
import com.example.app.R;
import com.example.app.login.LoginActivity;
import com.example.app.modules.AboutActivity;
import com.example.app.modules.CommunityJoinActivity;
import com.example.app.modules.JoinClubActivity;
import com.example.app.utils.AppConfig;
import com.example.app.utils.HttpUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class Page4Controller {
    private static final String TAG = "Page4Controller";
    private MainActivity t;
    private View view;
    private Toolbar toolbar;
    private CircleImageView ivAvatar;
    private TextView tvName;
    private TextView tvSchool;
    private ImageView ivEdit;
    private ImageView ivInfo;
    private LinearLayout llBtn1;
    private LinearLayout llBtn2;
    private LinearLayout llBtnList1;
    private LinearLayout llBtnList2;
    private LinearLayout llBtnList3;
    private LinearLayout llBtnList4;
    private Button btnLogout;


    public Page4Controller(MainActivity t, View view) {
        this.t = t;
        this.view = view;
        fbiView();
        setToolbar();
        setListener();
        setUserInfo();
    }

    private void setListener() {
        ivEdit.setOnClickListener(v -> {
            Toast.makeText(t, "点击 编辑", Toast.LENGTH_SHORT).show();
        });
        ivInfo.setOnClickListener(v -> {
            Intent intent = new Intent(t, JoinClubActivity.class);
            t.startActivity(intent);
        });
        llBtn1.setOnClickListener(v -> {
            Toast.makeText(t, "点击 我的活动", Toast.LENGTH_SHORT).show();
        });
        llBtn2.setOnClickListener(v -> {
            Toast.makeText(t, "点击 我的收藏", Toast.LENGTH_SHORT).show();
        });
        llBtnList1.setOnClickListener(v -> {
            Toast.makeText(t, "活动日程 功能开发中", Toast.LENGTH_SHORT).show();
        });
        llBtnList2.setOnClickListener(v -> {
            Toast.makeText(t, "校园快递 功能开发中", Toast.LENGTH_SHORT).show();
        });
        llBtnList3.setOnClickListener(v -> {
            t.startActivityForResult(new Intent(t, CommunityJoinActivity.class), 2);
        });
        llBtnList4.setOnClickListener(v -> {
            t.startActivity(new Intent(t, AboutActivity.class));
        });
    }

    private void fbiView() {
        toolbar = view.findViewById(R.id.toolbar);
        btnLogout = view.findViewById(R.id.btn_logout);
        toolbar = view.findViewById(R.id.toolbar);
        ivAvatar = view.findViewById(R.id.iv_avatar);
        tvName = view.findViewById(R.id.tv_name);
        tvSchool = view.findViewById(R.id.tv_school);
        ivEdit = view.findViewById(R.id.iv_edit);
        ivInfo = view.findViewById(R.id.iv_info);
        llBtn1 = view.findViewById(R.id.ll_btn_1);
        llBtn2 = view.findViewById(R.id.ll_btn_2);
        llBtnList1 = view.findViewById(R.id.ll_btn_list_1);
        llBtnList2 = view.findViewById(R.id.ll_btn_list_2);
        llBtnList3 = view.findViewById(R.id.ll_btn_list_3);
        llBtnList4 = view.findViewById(R.id.ll_btn_list_4);
        btnLogout = view.findViewById(R.id.btn_logout);
    }

    private void setToolbar() {
        toolbar.setTitle("");
        ((TextView) toolbar.getChildAt(0)).setText("MY CLUB");
        t.setSupportActionBar(toolbar);
        //   t.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnLogout.setOnClickListener(v -> {
            AppConfig.spfEditor.putString("userId", null).apply();
            Toast.makeText(t, "退出登录成功", Toast.LENGTH_SHORT).show();
            t.startActivity(new Intent(t, LoginActivity.class));
            t.finish();
        });
    }

    public void setUserInfo() {
        String defaultImage = AppConfig.spf.getString("defaultImage", "");
        String name = AppConfig.spf.getString("name", "昵称");
        String school = AppConfig.spf.getString("school", "");
        String flag = AppConfig.spf.getString("flag", "");
        tvSchool.setText(school + " | " + flag);
        Glide.with(t)
                .load(TextUtils.isEmpty(defaultImage) ? R.mipmap.defaultimage : HttpUtils.HOST + "app/upload/" + defaultImage)
                .apply(AppConfig.circle)
                .into(ivAvatar);
        tvName.setText(name);
    }
}
