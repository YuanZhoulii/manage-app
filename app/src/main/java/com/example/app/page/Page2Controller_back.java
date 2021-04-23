package com.example.app.page;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.example.app.MainActivity;
import com.example.app.R;
import com.example.app.modules.ClubInfoActivity;
import com.example.app.pojo.ClubData;
import com.example.app.utils.AppConfig;
import com.example.app.utils.HttpUtils;
import com.example.app.utils.Loading;
import com.example.app.utils.RSAUtils;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.superluo.textbannerlibrary.TextBannerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Page2Controller_back {
    private String TAG = "Page2Controller";
    private MainActivity t;
    private View view;
    private Toolbar toolbar;
    private TextView tvHeader;
    private ImageView iv;
    private TextBannerView tvBanner;
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private LinearLayout noClub;
    private LinearLayout llClub;
    private List<LinearLayout> lls = new ArrayList<>();
    private int[] llbtn = {R.id.ll_1, R.id.ll_2, R.id.ll_3, R.id.ll_4, R.id.ll_5, R.id.ll_6, R.id.ll_7, R.id.ll_8, R.id.ll_9, R.id.ll_10};
    private SmartRefreshLayout refreshLayout;


    public Page2Controller_back(MainActivity t, View view) {
        this.t = t;
        this.view = view;
        fbiView();
        setToolbar();
        setBanner();
        setClick();
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
        tvHeader = view.findViewById(R.id.tv_header);
        iv = view.findViewById(R.id.iv);
        tvBanner = view.findViewById(R.id.tv_banner);
        iv1 = view.findViewById(R.id.iv1);
        iv2 = view.findViewById(R.id.iv2);
        iv3 = view.findViewById(R.id.iv3);
        noClub = view.findViewById(R.id.noClub);
        llClub = view.findViewById(R.id.ll_club);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(t));
        for (int i : llbtn) {
            lls.add(view.findViewById(i));
        }

        setPage2Info();
        refreshLayout.setOnRefreshListener(r -> {
            r.finishRefresh();//传入false表示刷新失败
            setBanner();
            refresh();
        });
    }

    private void refresh() {
        if (AppConfig.spf.getString("club", "").isEmpty() && !AppConfig.spf.getString("apply", "").isEmpty()) {
            new Thread(() -> {
                String data = "{\"userId\":\"" + AppConfig.spf.getString("userId", null) + "\"}";
                try {
                    JSONObject send = HttpUtils.send("app/joinClub", RSAUtils.encryption(data));
                    ClubData clubData = AppConfig.gson.fromJson(send.toString(), ClubData.class);
                    t.runOnUiThread(() -> {
                        if (clubData.getMsg().equals("申请通过")) {
                            String[] applies = AppConfig.spf.getString("apply", "").split(",");
                            String clubs = "";
                            List<ClubData.DataDTO> data1 = clubData.getData();
                            for (ClubData.DataDTO dto : data1) {
                                applies[dto.getId()] = "";
                                clubs += dto.getId() + ",";
                            }
                            String s = Arrays.toString(applies);
                            Log.e(TAG, "refresh: " + s);
                            AppConfig.spfEditor.putString("apply", s.substring(1,s.length()-1))
                                    .putString("clubName", data1.get(0).getCName())
                                    .putString("flag", clubData.getFlag())
                                    .putString("club", clubs.substring(0,clubs.length()-1))
//                                    .putString("clubData", AppConfig.gson.toJson(dataDTO))
                                    .apply();
                            t.page4Controller.setUserInfo();
                        } else if (clubData.getMsg().equals("管理员已拒绝申请")) {
                            AppConfig.spfEditor.putString("apply", "").apply();
                        }
                        Toast.makeText(t, clubData.getMsg(), Toast.LENGTH_SHORT).show();
                        setPage2Info();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void setClick() {
        lls.get(0).setOnClickListener(v -> {
            Intent intent = new Intent(t, ClubInfoActivity.class);
            intent.putExtra("data",
                    AppConfig.gson.fromJson(AppConfig.spf.getString("clubData", "{}"), ClubData.DataDTO.class));
            t.startActivityForResult(intent, 2);
        });
        lls.get(9).setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(t)
                    .setTitle("提示")
                    .setMessage("是否退出社团?")
                    .setNegativeButton("确定", (dialog1, which) -> {
                        String club = AppConfig.spf.getString("club", "");
                        Loading.show(t, "请求中...");
                        new Thread(() -> {
                            String data = "{\"userId\":\"" + AppConfig.spf.getString("userId", null) + "\",\"club\":\"" + club + "\"}";
                            Log.e(TAG, "组装数据: " + data);
                            try {
                                JSONObject send = HttpUtils.send("app/quitClub", RSAUtils.encryption(data));
                                t.runOnUiThread(() -> {
                                    if (send.optString("msg").equals("退出社团成功")) {
                                        AppConfig.spfEditor.putString("club", "")
                                                .putString("flag", send.optString("flag"))
                                                .putString("apply", "")
                                                .putString("clubName", "")
                                                .putString("clubData", "")
                                                .apply();
                                    }
                                    Toast.makeText(t, send.optString("msg"), Toast.LENGTH_SHORT).show();
                                    setPage2Info();
                                    t.page1Controller.setUserInfo();
                                    t.page4Controller.setUserInfo();
                                    dialog1.dismiss();
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


    private void setToolbar() {
        toolbar.setTitle("");
        ((TextView) toolbar.getChildAt(0)).setText("我的社团");
        t.setSupportActionBar(toolbar);
        //t.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setPage2Info() {
        String club = AppConfig.spf.getString("club", "");
        if (club != null && !club.isEmpty()) {
            noClub.setVisibility(View.GONE);
            llClub.setVisibility(View.VISIBLE);
        } else {
            noClub.setVisibility(View.VISIBLE);
            llClub.setVisibility(View.INVISIBLE);
        }
    }
}
