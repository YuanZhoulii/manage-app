package com.example.app.page;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app.MainActivity;
import com.example.app.R;
import com.example.app.adapters.Page3RvAdapter;
import com.example.app.pojo.Page3Data;
import com.example.app.utils.AppConfig;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Page3Controller {
    private View view;
    private MainActivity t;
    private Toolbar toolbar;
    private ImageView iv;
    private TabLayout tabLayout;
    private RecyclerView rv;
    private Page3RvAdapter adapter;
    private List<Page3Data> list = new ArrayList<>();
    private String TAG = "Page3Controller";


    public Page3Controller(MainActivity t, View view) {
        this.view = view;
        this.t = t;
        toolbar = view.findViewById(R.id.toolbar);
        iv = view.findViewById(R.id.iv);
        tabLayout = view.findViewById(R.id.tabLayout);
        rv = view.findViewById(R.id.rv);
        setToolbar();
        setImage();
        setTabLayout();
    }

    private void setTabLayout() {
        String[] titles = {
                "活动竞赛",
                "创意征集",
                "人才招聘",
                "校园公益"
        };


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(t);
        rv.setLayoutManager(linearLayoutManager);

        adapter = new Page3RvAdapter(list);
        //绑定数据
        rv.setAdapter(adapter);
        //tabLayout 点击 item
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e(TAG, "onTabSelected: " + tab);
                list.clear();
                //模拟数据
                switch (tab.getPosition()) {
                    case 0:
                        list.add(new Page3Data(R.mipmap.page3_img_1, "开学疯狂", "3月1日 ~ 3月4日", "2020年3月1日", 3, false));
                        list.add(new Page3Data(R.mipmap.page3_img_2, "消防你我他,安全靠大家", "4月9日 ~ 4月12日", "2020年4月12日", 4, true));
                        list.add(new Page3Data(R.mipmap.page3_img_3, "校园新歌赛", "3月12日 ~ 3月14日", "2020年3月14日", 5, true));
                        list.add(new Page3Data(R.mipmap.page3_img_4, "美团入校", "3月12日 ~ 3月14日", "2020年3月14日", 5, true));
                        break;
                    case 1:
                        list.add(new Page3Data(R.mipmap.page3_img_1, "开学疯狂", "3月1日 ~ 3月4日", "2020年3月1日", 3, false));
                        list.add(new Page3Data(R.mipmap.page3_img_3, "校园新歌赛", "3月12日 ~ 3月14日", "2020年3月14日", 5, true));
                        list.add(new Page3Data(R.mipmap.page3_img_4, "美团入校", "3月12日 ~ 3月14日", "2020年3月14日", 5, true));
                        break;
                    case 2:
                        list.add(new Page3Data(R.mipmap.page3_img_3, "校园新歌赛", "3月12日 ~ 3月14日", "2020年3月14日", 5, true));
                        list.add(new Page3Data(R.mipmap.page3_img_4, "美团入校", "3月12日 ~ 3月14日", "2020年3月14日", 5, true));
                        break;
                    case 3:
                        list.add(new Page3Data(R.mipmap.page3_img_4, "美团入校", "3月12日 ~ 3月14日", "2020年3月14日", 5, true));
                        break;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.e(TAG, "onTabSelected: " + tab);
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

    private void setImage() {
        Glide.with(t)
                .load(R.mipmap.page1_banner)
                .apply(AppConfig.options)
                .into(iv);
    }

    private void setToolbar() {
        toolbar.setTitle("");
        ((TextView) toolbar.getChildAt(0)).setText("实践中心");
        t.setSupportActionBar(toolbar);
       // t.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}
