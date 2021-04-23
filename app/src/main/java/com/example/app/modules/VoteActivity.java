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
import com.example.app.adapters.VoteRvAdapter;
import com.example.app.pojo.Vote;
import com.example.app.utils.AppConfig;
import com.example.app.utils.HttpUtils;
import com.example.app.utils.Loading;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.json.JSONObject;

/**
 * 社团 - 基础服务 - 决策投票
 */
public class VoteActivity extends AppCompatActivity {
    private static final String TAG = "VoteActivity";
    private Toolbar toolbar;
    private ImageView add;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv;
    private LinearLayout noClub;
    private int clubid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        clubid = getIntent().getIntExtra("data", 0);
        initView();
        setToolbar();
        initData();

    }

    private void initData() {
        new Thread(() -> {
            try {
                JSONObject send1 = HttpUtils.send1("app/getVotes", "{\"clubid\":\"" + clubid + "\"}");
                Vote vote = AppConfig.gson.fromJson(send1.toString(), Vote.class);
                runOnUiThread(() -> {
                    if (vote.getMsg().equals("查询成功") && !vote.getData().isEmpty()) {
                        noClub.setVisibility(View.GONE);
                        rv.setAdapter(new VoteRvAdapter(vote.getData(), (flag, d) -> {
                            Loading.show(this, "投票中...");
                            AppConfig.spfEditor.putInt(AppConfig.spf.getString("userId","")+":"+d.getId() + ":vote", flag ? 1 : 2).apply();
                            new Thread(() -> {
                                try {
                                    JSONObject s = HttpUtils.send("app/updateVote", AppConfig.gson.toJson(d));
                                    runOnUiThread(() -> {
                                        Toast.makeText(this, s.optString("msg"), Toast.LENGTH_SHORT).show();
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    Loading.dismiss();
                                }
                            }).start();
                        }));
                    } else {
                        noClub.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
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
            Intent intent = new Intent(this, AddVoteActivity.class);
            intent.putExtra("data", clubid);
            startActivityForResult(intent, 3);
        });
        refreshLayout.setOnRefreshListener(r -> {
            r.finishRefresh();//传入false表示刷新失败
            initData();
        });
    }

    private void setToolbar() {
        toolbar.setTitle("");
        ((TextView) toolbar.getChildAt(0)).setText("决策投票");
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
        if (requestCode == 3) {
            initData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}