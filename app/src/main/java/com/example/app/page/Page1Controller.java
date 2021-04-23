package com.example.app.page;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.app.MainActivity;
import com.example.app.R;
import com.example.app.adapters.CustomViewHolder;
import com.example.app.adapters.Page1RvAdapter;
import com.example.app.modules.PersonalCenterActivity;
import com.example.app.pojo.ClubData;
import com.example.app.utils.AppConfig;
import com.example.app.utils.HttpUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.ms.banner.Banner;
import com.ms.banner.BannerConfig;
import com.ms.banner.Transformer;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class Page1Controller {
    private static final String TAG = "Page1Controller";
    private MainActivity t;
    private View view;
    private Banner banner;
    private TabLayout tabLayout;
    private RecyclerView rv;
    private ImageView civHead;
    private TextView tvName;
    private TextView tvSchool;
    private EditText editQuery;
    private LinearLayout noData;
    private static Gson gson = new Gson();
    private TextView tvInfo;
    private SmartRefreshLayout refreshLayout;


    //适配器
    private Page1RvAdapter adapter;
    //RecyclerView社团需要的数据
    private ClubData data = new ClubData();
    private int position = 0;


    public Page1Controller(MainActivity t, View view) {
        this.t = t;
        this.view = view;
        initView();
        setView();
        setUserInfo();
        setOnClick();
    }

    private void setOnClick() {
        civHead.setOnClickListener(v -> {
            t.startActivityForResult(new Intent(t, PersonalCenterActivity.class), 2);
        });
    }

    public void setUserInfo() {
        String defaultImage = AppConfig.spf.getString("defaultImage", "");
        String name = AppConfig.spf.getString("name", "昵称");
        String school = AppConfig.spf.getString("school", "昵称");
        RequestOptions options = new RequestOptions().circleCropTransform();
        Glide.with(t)
                .load(TextUtils.isEmpty(defaultImage) ? R.mipmap.defaultimage : (HttpUtils.HOST + "app/upload/" + defaultImage).trim())
                .apply(options)
                .into(civHead);
        tvName.setText(name);
        tvSchool.setText(school);
    }

    private void initView() {
        if (data.getData() == null) data.setData(new ArrayList<ClubData.DataDTO>());
        banner = view.findViewById(R.id.banner);
        rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(t, 2));
        rv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter = new Page1RvAdapter(data, t);
        rv.setAdapter(adapter);
        civHead = view.findViewById(R.id.civ_head);
        tabLayout = view.findViewById(R.id.tabLayout);
        tvName = view.findViewById(R.id.tv_name);
        tvSchool = view.findViewById(R.id.tv_school);
        editQuery = view.findViewById(R.id.edit_query);
        noData = view.findViewById(R.id.noData);
        tvInfo = view.findViewById(R.id.tv_info);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(t));
        setBanner();
        refreshLayout.setOnRefreshListener(r -> {
            r.finishRefresh();//传入false表示刷新失败
            loadRv(AppConfig.spf.getString("school", ""));
            // setBanner();
            new Thread(() -> {
                int[] banners = new int[3];
                try {
                    JSONObject send = HttpUtils.send("app/getCarousel", "");
                    banners[0] = send.optInt("page1_banner_1.jpg");
                    banners[1] = send.optInt("page1_banner_2.jpg");
                    banners[2] = send.optInt("page1_banner_3.jpg");
                    Log.e(TAG, "initView: " + Arrays.toString(banners));
                    t.runOnUiThread(() -> {
                        banner.update(Arrays.asList(
                                HttpUtils.HOST + "app/upload/page1_banner_1.jpg#" + banners[0],
                                HttpUtils.HOST + "app/upload/page1_banner_2.jpg#" + banners[1],
                                HttpUtils.HOST + "app/upload/page1_banner_3.jpg#" + banners[2]));
                        AppConfig.spfEditor.putString("b1", banners[0] + "").putString("b2", banners[1] + "").putString("b3", banners[2] + "").apply();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    public void setBanner() {
        //首页轮播图实现
        banner.setAutoPlay(true)
                .setDelayTime(3000)
                .setPages(Arrays.asList(
                        HttpUtils.HOST + "app/upload/page1_banner_1.jpg#" + AppConfig.spf.getString("b1", "0"),
                        HttpUtils.HOST + "app/upload/page1_banner_2.jpg#" + AppConfig.spf.getString("b2", "0"),
                        HttpUtils.HOST + "app/upload/page1_banner_3.jpg#" + AppConfig.spf.getString("b3", "0")),
                        new CustomViewHolder())
                .setBannerStyle(BannerConfig.NOT_INDICATOR)
                .setBannerAnimation(Transformer.Scale)
                .start();
    }

    private void setView() {
        //校园社团 和 活动赞赏 切换

        String[] titles = {
                "校园社团",
                "活动赞助",
        };
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();
                switch (tab.getPosition()) {
                    case 0:
                        tvInfo.setText("更多社团");
                        loadRv(AppConfig.spf.getString("school", ""));
                        break;
                    case 1:
                        tvInfo.setText("活动赞助");
                        loadRv("FFF");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        for (String title : titles) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }
        tabLayout.getTabAt(0).select();
    }

    public void loadRv(String school) {
        data.getData().clear();
        new Thread(() -> {
            try {
                JSONObject send = HttpUtils.send("app/getClub/" + school, "");
                ClubData temp = gson.fromJson(send.toString(), ClubData.class);
                t.runOnUiThread(() -> {
                    if (temp.getData() != null)
                        for (ClubData.DataDTO datum : temp.getData()) {
                            data.getData().add(datum);
                        }
                    noData.setVisibility(data.getData().size() == 0 ? View.VISIBLE : View.GONE);
                    adapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                e.printStackTrace();
                t.runOnUiThread(() -> {
                    ((TextView) noData.getChildAt(1)).setText("网络超时");
                    noData.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                });
            }
        }).start();
    }
}
