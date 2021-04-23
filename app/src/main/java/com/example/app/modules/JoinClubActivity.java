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
import com.example.app.adapters.Page4RvAdapter;
import com.example.app.pojo.JoinData;
import com.example.app.utils.AppConfig;
import com.example.app.utils.HttpUtils;
import com.example.app.utils.Loading;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.json.JSONObject;

import java.util.List;

public class JoinClubActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv;
    private LinearLayout noClub;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_club);
        flag = AppConfig.spf.getString("flag","");
        initView();
        setToolbar();
        if (flag.endsWith("社团管理员"))
        initData();

    }

    private void initData() {
        new Thread(() -> {
            try {
                JSONObject send = HttpUtils.send("app/getClubAll", "");
                JoinData joinData = AppConfig.gson.fromJson(send.toString(), JoinData.class);
                runOnUiThread(() -> {
                    if (joinData.getData().size() != 0) {
                        rv.setAdapter(new Page4RvAdapter(joinData.getData(), new Page4RvAdapter.onClick() {
                            @Override
                            public void yes(Page4RvAdapter p, JoinData.DataDTO d, int pos) {
                                Loading.show(JoinClubActivity.this,"请求中...");
                                new Thread(() -> {
                                    try {
                                        JSONObject send1 = HttpUtils.send1("app/agreeClub",
                                                "{\"id\":\"" + d.getId() + "\",\"club\":\"" + d.getClubid() + "\",\"userid\":\"" + d.getUserid() + "\"}");
                                        runOnUiThread(() -> {
                                            Toast.makeText(JoinClubActivity.this, send1.optString("msg"), Toast.LENGTH_SHORT).show();
                                            if (send1.optString("msg").equals("同意申请")) {
                                                joinData.getData().remove(pos);
                                                p.notifyDataSetChanged();
                                            }
                                            hide(joinData.getData());
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }finally {
                                        Loading.dismiss();
                                    }
                                }).start();
                            }

                            @Override
                            public void no(int id) {
                                Loading.show(JoinClubActivity.this,"请求中...");
                                new Thread(() -> {
                                    try {
                                        JSONObject send1 = HttpUtils.send("app/refuseClub", "{\"id\":\"" + id + "\"}");
                                        runOnUiThread(() -> {
                                            Toast.makeText(JoinClubActivity.this, send1.optString("msg"), Toast.LENGTH_SHORT).show();
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }finally {
                                        Loading.dismiss();
                                    }
                                }).start();
                            }
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
            if (flag.equals("社团管理员"))
            initData();
        });
    }

    private void hide(List<JoinData.DataDTO> dataDTOS) {
        noClub.setVisibility(dataDTOS.size() == 0 ? View.VISIBLE : View.GONE);
    }

    private void setToolbar() {
        toolbar.setTitle("");
        ((TextView) toolbar.getChildAt(0)).setText("通知");
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