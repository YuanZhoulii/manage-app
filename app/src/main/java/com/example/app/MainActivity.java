package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.example.app.adapters.VpAdapter;
import com.example.app.modules.PersonalCenterActivity;
import com.example.app.page.Page1Controller;
import com.example.app.page.Page2Controller;
import com.example.app.page.Page3Controller;
import com.example.app.page.Page4Controller;
import com.example.app.utils.AppConfig;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private BottomNavigationView navView;
    private ViewPager vp;
    //存储viewPager 需要用的页面
    private View[] views = new View[4];
    int[] menus = {
            R.id.navigation_page1,
            R.id.navigation_page2,
            R.id.navigation_page3,
            R.id.navigation_page4,
    };
    public Page1Controller page1Controller;
    public Page2Controller page2Controller;
    public Page4Controller page4Controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        vp = findViewById(R.id.vp);

        views[0] = getLayoutInflater().inflate(R.layout.activity_page1, null);
        views[1] = getLayoutInflater().inflate(R.layout.activity_page2, null);
        views[2] = getLayoutInflater().inflate(R.layout.activity_page3, null);
        views[3] = getLayoutInflater().inflate(R.layout.activity_page4, null);
        page1Controller = new Page1Controller(this, views[0]);
        page2Controller = new Page2Controller(this, views[1]);
        new Page3Controller(this, views[2]);
        page4Controller = new Page4Controller(this, views[3]);
        vp.setAdapter(new VpAdapter(views));
        //监听ViewPager 滑动
        vp.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "当前在Page : " + position);
                //指定显示滑动到某个底部栏
                navView.setSelectedItemId(menus[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //监听 底部栏点击状态 并 切换 viewPager 视图
        navView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_page1:
                    vp.setCurrentItem(0);
                    break;
                case R.id.navigation_page2:
                    vp.setCurrentItem(1);
                    break;
                case R.id.navigation_page3:
                    vp.setCurrentItem(2);
                    break;
                case R.id.navigation_page4:
                    vp.setCurrentItem(3);
                    break;
            }
            return true;
        });
setSchool();

    }

    private void setSchool(){
        if (AppConfig.spf.getString("school", "未选择").equals("未选择")) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setCancelable(false)
                    .setMessage("请设置学校")
                    .setPositiveButton("设置", (dialog1, which) -> {
                        startActivityForResult(new Intent(this, PersonalCenterActivity.class), 2);
                        dialog1.dismiss();
                    }).setNegativeButton("取消", (dialog12, which) -> finish()).create();
            dialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2) {
            page1Controller.setUserInfo();
            page1Controller.loadRv(AppConfig.spf.getString("school", ""));
            page2Controller.setPage2Info();
            page4Controller.setUserInfo();
            setSchool();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}