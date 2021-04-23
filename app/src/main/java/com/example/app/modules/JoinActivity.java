package com.example.app.modules;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.adapters.JoinActivityRvAdapter;
import com.example.app.pojo.ClubData;
import com.example.app.pojo.JoinActivityData;
import com.example.app.utils.AppConfig;
import com.example.app.utils.HttpUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.json.JSONObject;

import java.util.List;

public class JoinActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv;
    private LinearLayout noClub;
    private ClubData.DataDTO data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_club);
        data = (ClubData.DataDTO) getIntent().getSerializableExtra("data");
        initView();
        setToolbar();
        initData();
    }

    private void initData() {
        new Thread(() -> {
            try {
                JSONObject send = HttpUtils.send1("app/tempActivity", "{\"clubid\":\"" + data.getId() + "\"}");
                JoinActivityData joinData = AppConfig.gson.fromJson(send.toString(), JoinActivityData.class);
                runOnUiThread(() -> {
                    if (joinData.getData().size() != 0) {
                        rv.setAdapter(new JoinActivityRvAdapter(joinData.getData(), (b, j,d, pos) -> {
                            new Thread(() -> {
                                try {
                                    JSONObject send1 = HttpUtils.send1("app/ifActivity", "{\"id\":\"" + d.getActivityId() + "\",\"flag\":\"" + (b ? "Y" : "") + "\"}");
                                    runOnUiThread(() -> {
                                        Toast.makeText(this, send1.optString("msg"), Toast.LENGTH_SHORT).show();
                                        joinData.getData().remove(pos);
                                        j.notifyDataSetChanged();
                                        hide(joinData.getData());
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        }));
                    }
                    hide(joinData.getData());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        refreshLayout = findViewById(R.id.refreshLayout);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        noClub = findViewById(R.id.noClub);
        refreshLayout.setOnRefreshListener(r -> {
            r.finishRefresh();//传入false表示刷新失败
            initData();
        });
    }

    private void hide(List<JoinActivityData.DataDTO> dataDTOS) {
        noClub.setVisibility(dataDTOS.size() == 0 ? View.VISIBLE : View.GONE);
    }

    private void setToolbar() {
        toolbar.setTitle("");
        ((TextView) toolbar.getChildAt(0)).setText("活动申请记录");
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
}