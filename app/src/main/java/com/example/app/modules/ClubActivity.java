package com.example.app.modules;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.adapters.ActivityRvAdapter;
import com.example.app.pojo.Activity;
import com.example.app.pojo.ClubData;
import com.example.app.pojo.JoinActivityData;
import com.example.app.utils.AppConfig;
import com.example.app.utils.HttpUtils;
import com.example.app.utils.Loading;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.json.JSONObject;

/**
 * 社团 - 基础服务 - 社团活动
 */
public class ClubActivity extends AppCompatActivity {
    private static final String TAG = "ClubActivity ";
    private Toolbar toolbar;
    private ImageView add;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv;
    private LinearLayout noClub;
    private ClubData.DataDTO data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);
        data = (ClubData.DataDTO) getIntent().getSerializableExtra("data");
        initView();
        setToolbar();
        initData();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        add = findViewById(R.id.add);
        refreshLayout = findViewById(R.id.refreshLayout);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        noClub = findViewById(R.id.noClub);
        add.setVisibility(AppConfig.spf.getString("flag", "").equals("社团管理员") ? View.VISIBLE : View.GONE);
        add.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddClubActivity.class);
            intent.putExtra("data", data);
            startActivityForResult(intent, 4);
        });
        refreshLayout.setOnRefreshListener(r -> {
            r.finishRefresh();//传入false表示刷新失败
            initData();
        });
    }

    private void initData() {
        //Loading.show(this, "请求中...");
        new Thread(() -> {
            String data = "{\"activityId\":\"" + this.data.getCActivityId() + "\"}";
            String data2 = "{\"userid\":\"" + AppConfig.spf.getString("userId", "") + "\"}";
            try {
                JSONObject jsonObject = HttpUtils.send1("app/getActivity", data);
                JSONObject jsonObject2 = HttpUtils.send1("app/getJoinActivity", data2);
                Activity activity = AppConfig.gson.fromJson(jsonObject.toString(), Activity.class);
                JoinActivityData joinActivityData = AppConfig.gson.fromJson(jsonObject2.toString(), JoinActivityData.class);
                joinActivityData.getData().stream().forEach(dataDTO -> {
                    activity.getData().stream().forEach(dataDTO1 -> {
                        if (dataDTO.getActivityId() == dataDTO1.getId()) {
                            dataDTO1.setFlag(dataDTO.getFlag());
                        }
                    });
                });

                runOnUiThread(() -> {
                    noClub.setVisibility((activity == null || activity.getData().size() == 0) ? View.VISIBLE : View.GONE);
                    rv.setAdapter(new ActivityRvAdapter(activity, (button, data1) -> {
                        Loading.show(this, "请求中...");
                        new Thread(() -> {
                            try {
                                String json = "{\"userid\":\""+AppConfig.spf.getString("userId", "")+"\"," +
                                        "\"activityId\":\""+data1.getId()+"\"," +
                                        "\"clubid\":\""+this.data.getId()+"\"}";
                                JSONObject send = HttpUtils.send("app/joinActivity", json);
                                runOnUiThread(() -> {
                                    Toast.makeText(this, send.optString("msg"), Toast.LENGTH_SHORT).show();
                                    if (send.optString("msg").equals("申请成功")) {
                                        button.setText("已申请");
                                        button.setEnabled(false);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                Loading.dismiss();
                            }
                        }).start();
                    }));
                });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //Loading.dismiss();
            }
        }).start();
    }

    private void setToolbar() {
        toolbar.setTitle("");
        ((TextView) toolbar.getChildAt(0)).setText("社团活动");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 4) {
            initData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}