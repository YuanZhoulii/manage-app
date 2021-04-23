package com.example.app.page;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app.MainActivity;
import com.example.app.R;
import com.example.app.adapters.Page2RvAdapter;
import com.example.app.pojo.ClubData;
import com.example.app.utils.AppConfig;
import com.example.app.utils.HttpUtils;
import com.example.app.utils.RSAUtils;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.superluo.textbannerlibrary.TextBannerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Page2Controller {
    private String TAG = "Page2Controller";
    private MainActivity t;
    private View view;
    private Toolbar toolbar;
    private LinearLayout noClub;
    private RecyclerView rv;
    private ImageView iv;

    private TextBannerView tvBanner;
    private SmartRefreshLayout refreshLayout;


    public Page2Controller(MainActivity t, View view) {
        this.t = t;
        this.view = view;
        fbiView();
        setToolbar();
        setBanner();
        refreshRV();
        setClick();
        setPage2Info();
    }


    private void setBanner() {
        new Thread(() -> {
            try {
                JSONArray data = HttpUtils.send("api/allNotice", "").optJSONArray("data");
                t.runOnUiThread(() -> {
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

    private void fbiView() {
        toolbar = view.findViewById(R.id.toolbar);
        noClub = view.findViewById(R.id.noClub);
        tvBanner = view.findViewById(R.id.tv_banner);
        iv = view.findViewById(R.id.iv);
        Glide.with(t).load(R.mipmap.page2_img_title).apply(AppConfig.options).into(iv);
        rv = view.findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(t);
        rv.setLayoutManager(linearLayoutManager);
        rv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(t));
        refreshLayout.setOnRefreshListener(r -> {
            r.finishRefresh();//传入false表示刷新失败
            setBanner();
            refresh();
            //refreshRV();
        });

    }

    private void refreshRV() {
        new Thread(() -> {
            String data = "{\"userId\":\"" + AppConfig.spf.getString("userId", null) + "\"}";
            try {
                JSONObject send = HttpUtils.send("app/getClub2", RSAUtils.encryption(data));
                ClubData clubData = AppConfig.gson.fromJson(send.toString(), ClubData.class);
                t.runOnUiThread(() -> {
                    if (clubData.getMsg().equals("查询成功")) {
                        rv.setAdapter(new Page2RvAdapter(clubData.getData(), t));
                    }
                    if (clubData.getData() == null || clubData.getData().size() == 0) {
                        noClub.setVisibility(View.VISIBLE);
                    } else {
                        noClub.setVisibility(View.GONE);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void refresh() {
        if (AppConfig.spf.getStringSet("apply", new HashSet<>()).size() != 0) {
            new Thread(() -> {
                String data = "{\"userId\":\"" + AppConfig.spf.getString("userId", null) + "\"}";
                try {
                    JSONObject send = HttpUtils.send("app/joinClub", RSAUtils.encryption(data));
                    ClubData clubData = AppConfig.gson.fromJson(send.toString(), ClubData.class);
                    t.runOnUiThread(() -> {
                        if(clubData.getNo()!=null){
                            String[] split = clubData.getNo().split(",");
                            for (int i = 0; i < split.length; i++) {
                                Set<String> apply = AppConfig.spf.getStringSet("apply", new HashSet<>());
                                apply.remove(split[i]);
                            }
                        }
                        if (clubData.getMsg()!= null && clubData.getMsg().equals("申请通过")) {
                            Set<String> apply = AppConfig.spf.getStringSet("apply", new HashSet<>());
                            String clubs = "";
                            List<ClubData.DataDTO> data1 = clubData.getData();
                            for (ClubData.DataDTO dto : data1) {
                                apply.remove(dto.getId().toString());
                                clubs += dto.getId() + ",";
                            }
                            Log.e(TAG, "refresh: "+clubData.getFlag() );
                            AppConfig.spfEditor.putStringSet("apply", apply)
                                    .putString("flag", clubData.getFlag())
                                    .putString("club", clubs.substring(0, clubs.length() - 1))
                                    .apply();
                            t.page4Controller.setUserInfo();

                        } else if (clubData.getMsg()!=null && clubData.getMsg().equals("管理员已拒绝申请")) {
                            AppConfig.spfEditor.putStringSet("apply", new HashSet<>()).apply();
                        }
                        if (clubData.getData().size()!=0){
                            noClub.setVisibility(View.GONE);
                        }
                        Toast.makeText(t, clubData.getMsg(), Toast.LENGTH_SHORT).show();
                        rv.setAdapter(new Page2RvAdapter(clubData.getData(), t));
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void setClick() {

    }


    private void setToolbar() {
        toolbar.setTitle("");
        ((TextView) toolbar.getChildAt(0)).setText("我的社团");
        t.setSupportActionBar(toolbar);
    }

    public void setPage2Info() {
        String club = AppConfig.spf.getString("club", "");
        if (club != null && !club.isEmpty()) {
            noClub.setVisibility(View.GONE);
        } else {
            noClub.setVisibility(View.VISIBLE);
        }
        refreshRV();
    }
}
