package com.example.app.modules;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.app.R;
import com.example.app.pojo.ClubData;
import com.example.app.utils.AppConfig;
import com.example.app.utils.HttpUtils;
import com.example.app.utils.Loading;
import com.example.app.utils.RSAUtils;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ClubInfoActivity extends AppCompatActivity {
    private static final String TAG = "ClubInfoActivity";
    private Toolbar toolbar;
    private TextView tvSchool;
    private TextView tvClub;
    private TextView tvInfo;
    private TextView tvTime;
    private TextView tvManager;
    private ImageView iv;
    private TextView tvShow;
    private TextView tvRules;
    float y1 = 0;
    float y2 = 0;
    private ClubData.DataDTO clubData;
    private Button btnJoin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_info);
        clubData = (ClubData.DataDTO) getIntent().getSerializableExtra("data");
        initView();
        initData();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initData() {
        tvSchool.setText(AppConfig.spf.getString("school", ""));
        tvClub.setText(AppConfig.spf.getString("school", "") + " " + clubData.getCName());
        tvInfo.setText(clubData.getDeclaration());
        tvTime.setText("成立时间: " + clubData.getCDate());
        tvManager.setText("当前社长: 王燕\n13100000000");
        tvShow.setText(clubData.getCInfo());
        tvShow.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvShow.setOnTouchListener((v, event) -> {
            //按下
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                y1 = event.getY();
                v.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                y2 = event.getY();
                if (y1 - y2 > 50) {
                    v.getParent().requestDisallowInterceptTouchEvent(canVerticalScroll((TextView) v, true));
                    Log.e(TAG, "向上滑: ");
                } else if (y2 - y1 > 200) {
                    v.getParent().requestDisallowInterceptTouchEvent(canVerticalScroll((TextView) v, false));
                    Log.e(TAG, "向下滑: ");
                }
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        });
        tvRules.setText(clubData.getCRules().replace("\\r\\n", "\r\n"));
        tvRules.setMovementMethod(ScrollingMovementMethod.getInstance());
        joinClub();
        btnJoin.setOnClickListener(v -> {
            Loading.show(this, "请求中...");
            new Thread(() -> {
                String data = "{\"userId\":\"" + AppConfig.spf.getString("userId", null) + "\",\"club\":\"" + clubData.getId() + "\"}";
                Log.e(TAG, "组装数据: " + data);
                try {
                    JSONObject send = HttpUtils.send("app/joinClub1", RSAUtils.encryption(data));
                    runOnUiThread(() -> {
                        if (send.optString("msg").equals("申请成功,请等待管理员审核")) {
                            Set<String> apply = AppConfig.spf.getStringSet("apply", new HashSet<>());
                            apply.add(clubData.getId().toString());
                            AppConfig.spfEditor.putStringSet("apply", apply).apply();
                        }
                        Toast.makeText(this, send.optString("msg"), Toast.LENGTH_SHORT).show();
                        joinClub();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Loading.dismiss();
                }
            }).start();
        });
        tvRules.setOnTouchListener((v, event) -> {
            //按下
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                y1 = event.getY();
                v.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                y2 = event.getY();
                if (y1 - y2 > 50) {
                    v.getParent().requestDisallowInterceptTouchEvent(canVerticalScroll((TextView) v, true));
                    Log.e(TAG, "向上滑: ");
                } else if (y2 - y1 > 380) {
                    v.getParent().requestDisallowInterceptTouchEvent(canVerticalScroll((TextView) v, false));
                    Log.e(TAG, "向下滑: ");
                }
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        });
        Glide.with(this).load(HttpUtils.HOST + "app/upload/" + clubData.getCImg()).apply(AppConfig.circle).into(iv);
    }

    private void joinClub() {
        String[] clubId = AppConfig.spf.getString("club", null).split(",");
        Set<String> apply = AppConfig.spf.getStringSet("apply", new HashSet<>());
        Log.e(TAG, "加入的社团: " + Arrays.toString(clubId));
        Log.e(TAG, "申请的社团: " + apply);
        for (String s : clubId) {
            if (s.equals(clubData.getId() + "")) {
                btnJoin.setEnabled(false);
                btnJoin.setText("已加入");
            }
        }

        for (String s : apply) {
            if (s.trim().equals(clubData.getId() + "")) {
                btnJoin.setEnabled(false);
                btnJoin.setText("已申请");
            }
        }
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        tvSchool = findViewById(R.id.tv_school);
        tvClub = findViewById(R.id.tv_club);
        tvInfo = findViewById(R.id.tv_info);
        tvTime = findViewById(R.id.tv_time);
        tvManager = findViewById(R.id.tv_manager);
        iv = findViewById(R.id.iv);
        tvShow = findViewById(R.id.tv_show);
        tvRules = findViewById(R.id.tv_rules);
        btnJoin = findViewById(R.id.btn_join);
        toolbar.setTitle("");
        ((TextView) toolbar.getChildAt(0)).setText("社团名片");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean canVerticalScroll(TextView view, boolean flag) {
        //滚动的距离
        int scrollY = view.getScrollY();
        Log.e(TAG, "滚动距离: " + scrollY);
        //控件内容的总高度
        int scrollRange = view.getLayout().getHeight();
        Log.e(TAG, "控件内容的总高度: " + scrollRange);
        //控件实际显示的高度
        int scrollExtent = view.getHeight() - view.getCompoundPaddingTop() - view.getCompoundPaddingBottom();
        Log.e(TAG, "控件实际显示的高度: " + scrollExtent);
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;
        Log.e(TAG, "显示高度的差值: " + scrollDifference);
        if (flag) {
            return scrollDifference == scrollY ? false : true;
        } else {
            return scrollDifference == 0;
        }
    }
}