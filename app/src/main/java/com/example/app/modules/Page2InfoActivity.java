package com.example.app.modules;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.app.R;
import com.example.app.pojo.ClubData;
import com.example.app.utils.AppConfig;
import com.example.app.utils.HttpUtils;
import com.example.app.utils.Loading;
import com.example.app.utils.RSAUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.superluo.textbannerlibrary.TextBannerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Page2InfoActivity extends AppCompatActivity {
    private static final String TAG = "Page2InfoActivity";
    private Toolbar toolbar;
    private SmartRefreshLayout refreshLayout;
    private TextView tvHeader;
    private ImageView iv;
    private TextBannerView tvBanner;
    private LinearLayout llClub;
    private LinearLayout llText1;
    private LinearLayout llText2;
    private LinearLayout llText3;
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private List<LinearLayout> lls = new ArrayList<>();
    private int[] llbtn = {R.id.ll_1, R.id.ll_2, R.id.ll_3, R.id.ll_4, R.id.ll_5, R.id.ll_6, R.id.ll_7, R.id.ll_8, R.id.ll_9, R.id.ll_10};
    private LinearLayout noClub;
    ClubData.DataDTO data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2_info);
        data = (ClubData.DataDTO) getIntent().getSerializableExtra("data");
        initView();
        setToolbar();
        setClick();
        initData();
        setBanner();
    }

    private void initData() {

        refreshLayout.setOnRefreshListener(r -> {
            r.finishRefresh();//传入false表示刷新失败
            setBanner();
        });
    }
    private void setBanner() {
        new Thread(() -> {
            try {
                JSONArray data = HttpUtils.send("api/allNotice", "").optJSONArray("data");
                runOnUiThread(() -> {
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObject = data.optJSONObject(i);
                        list.add(jsonObject.optString("value"));
                    }
                    tvBanner.setDatas(list);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void setClick() {
        lls.get(1).setOnClickListener(v -> {
            Intent intent = new Intent(this, ClubActivity.class);
            intent.putExtra("data", data);
            startActivity(intent);
        });
        lls.get(2).setOnClickListener(v -> {
            if (!AppConfig.spf.getString("flag","").equals("社团管理员")){
                Toast.makeText(this, "您不是社团管理员,此功能不能使用", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(this, JoinActivity.class);
            intent.putExtra("data", data);
            startActivity(intent);
        });
        lls.get(3).setOnClickListener(v -> {
            Intent intent = new Intent(this, VoteActivity.class);
            intent.putExtra("data", this.data.getId());
            startActivity(intent);
        });
        lls.get(9).setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("是否退出社团?")
                    .setNegativeButton("确定", (dialog1, which) -> {
                        Loading.show(this, "请求中...");
                        new Thread(() -> {
                            String data = "{\"userId\":\"" + AppConfig.spf.getString("userId", null) +
                                    "\",\"club\":\"" + this.data.getId() + "\"}";
                            Log.e(TAG, "组装数据: " + data);
                            try {
                                JSONObject send = HttpUtils.send("app/quitClub", RSAUtils.encryption(data));
                                runOnUiThread(() -> {
                                    if (send.optString("msg").equals("退出社团成功")) {
                                        AppConfig.spfEditor
                                                .putString("club", send.optString("club"))
                                                .putString("flag", send.optString("flag"))
//                                                .putString("apply", "")
//                                                .putString("clubName", "")
//                                                .putString("clubData", "")
                                                .apply();
                                    }
                                    Toast.makeText(this, send.optString("msg"), Toast.LENGTH_SHORT).show();
                                    dialog1.dismiss();
                                    finish();
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                Loading.dismiss();
                            }
                        }).start();
                    })
                    .setPositiveButton("取消", (dialog12, which) -> {
                        dialog12.dismiss();
                    }).create();
            dialog.show();
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        refreshLayout = findViewById(R.id.refreshLayout);
        tvHeader = findViewById(R.id.tv_header);
        iv = findViewById(R.id.iv);
        tvBanner = findViewById(R.id.tv_banner);
        llClub = findViewById(R.id.ll_club);
        llText1 = findViewById(R.id.ll_text_1);
        llText2 = findViewById(R.id.ll_text_2);
        llText3 = findViewById(R.id.ll_text_3);
        iv1 = findViewById(R.id.iv1);
        iv2 = findViewById(R.id.iv2);
        iv3 = findViewById(R.id.iv3);
        for (int i : llbtn) {
            lls.add(findViewById(i));
        }
        noClub = findViewById(R.id.noClub);
        Glide.with(this).load(R.mipmap.page2_img_title).apply(AppConfig.options).into(iv);
        Glide.with(this).load(R.mipmap.page2_icon_1).apply(AppConfig.circle).into(iv1);
        Glide.with(this).load(R.mipmap.page2_icon_3).apply(AppConfig.circle).into(iv2);
        Glide.with(this).load(R.mipmap.page2_icon_4).apply(AppConfig.circle).into(iv3);
    }

    private void setToolbar() {
        toolbar.setTitle("");
        ((TextView) toolbar.getChildAt(0)).setText(data.getCName());
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