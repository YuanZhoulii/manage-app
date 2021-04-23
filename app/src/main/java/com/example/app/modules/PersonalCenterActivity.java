package com.example.app.modules;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.app.R;
import com.example.app.utils.AppConfig;
import com.example.app.utils.HttpUtils;
import com.example.app.utils.Loading;
import com.example.app.utils.PermissionUtils;
import com.example.app.utils.RSAUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 实现个人信息
 */
public class PersonalCenterActivity extends AppCompatActivity {
    private static final String TAG = "PersonalCenterActivity";
    private Toolbar toolbar;
    private CircleImageView iv;
    private ImageView right;
    private LinearLayout setName;
    private LinearLayout setSex;
    private LinearLayout setSchool;
//    private LinearLayout setClub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        initView();
        setClick();
        setToolbar();
    }


    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        iv = findViewById(R.id.iv);
        right = findViewById(R.id.right);
        setName = findViewById(R.id.setName);
        setSex = findViewById(R.id.setSex);
        setSchool = findViewById(R.id.setSchool);
//        setClub = findViewById(R.id.setClub);
    }

    private void setClick() {
        iv.setOnClickListener(v -> {
            PermissionUtils.isGrantExternalRW(this, 0);
            startActivityForResult(selectPicture(), 1);
        });
        setName.setOnClickListener(v -> {
            View view = getLayoutInflater().inflate(R.layout.set_name_edit, null);
            EditText editText = view.findViewById(R.id.editName);
            editText.requestFocus();
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("设置昵称")
                    .setView(view)
                    .setNegativeButton("确定", (dialog1, which) -> {
                        String name = editText.getText().toString();
                        ((TextView) setName.getChildAt(0)).setText(name);
                        Loading.show(PersonalCenterActivity.this, "修改中...");
                        new Thread(() -> {
                            String data = "{\"v\":\"" + name + "\"}";
                            try {
                                JSONObject userId = HttpUtils.send("app/update/name/"
                                                + AppConfig.spf.getString("userId", ""),
                                        RSAUtils.encryption(data));
                                runOnUiThread(() -> {
                                    if (userId.optString("msg").equals("修改成功")) {
                                        AppConfig.spfEditor.putString("name", name).apply();
                                    }
                                    Toast.makeText(this, userId.optString("msg"), Toast.LENGTH_SHORT).show();
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                Loading.dismiss();
                            }
                        }).start();
                    }).create();
            dialog.show();
        });
        setSex.setOnClickListener(v -> {
            View inflate = getLayoutInflater().inflate(R.layout.set_sex_item, null);
            RadioButton rb1 = inflate.findViewById(R.id.rb1);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("设置性别")
                    .setView(inflate)
                    .setNegativeButton("确定", (dialog1, which) -> {
                        Loading.show(PersonalCenterActivity.this, "修改中...");
                        new Thread(() -> {
                            String data = "{\"v\":\"" + (rb1.isChecked() ? "男" : "女") + "\"}";
                            try {
                                JSONObject userId = HttpUtils.send("app/update/sex/"
                                                + AppConfig.spf.getString("userId", ""),
                                        RSAUtils.encryption(data));
                                runOnUiThread(() -> {
                                    if (userId.optString("msg").equals("修改成功")) {
                                        AppConfig.spfEditor.putString("sex", (rb1.isChecked() ? "男" : "女")).apply();
                                        ((TextView) setSex.getChildAt(0)).setText((rb1.isChecked() ? "男" : "女"));
                                    }
                                    Toast.makeText(this, userId.optString("msg"), Toast.LENGTH_SHORT).show();
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                Loading.dismiss();
                            }
                        }).start();
                    }).create();
            dialog.show();
        });
        setSchool.setOnClickListener(v -> {
            Spinner spinner = new Spinner(PersonalCenterActivity.this);
            spinner.setGravity(0x11);
            new Thread(() -> {
                try {
                    JSONObject send = HttpUtils.send("app/getSchoolName", "");
                    JSONArray data1 = send.optJSONArray("data");
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < data1.length(); i++) {
                        list.add(data1.optString(i));
                    }
                    runOnUiThread(() -> {
                        spinner.setAdapter(new ArrayAdapter<>(PersonalCenterActivity.this, android.R.layout.simple_expandable_list_item_1, list));
                        AlertDialog dialog = new AlertDialog.Builder(this)
                                .setTitle("设置学校")
                                .setView(spinner)
                                .setNegativeButton("确定", (dialog1, which) -> {
                                    Loading.show(PersonalCenterActivity.this, "修改中...");
                                    new Thread(() -> {
                                        String temp = ((TextView) spinner.getSelectedView()).getText().toString();
                                        String data = "{\"v\":\"" + temp + "\"}";
                                        try {
                                            JSONObject userId = HttpUtils.send("app/update/school/"
                                                            + AppConfig.spf.getString("userId", ""),
                                                    RSAUtils.encryption(data));
                                            runOnUiThread(() -> {
                                                if (userId.optString("msg").equals("修改成功")) {
                                                    AppConfig.spfEditor.putString("school", temp).apply();
                                                    ((TextView) setSchool.getChildAt(0)).setText(temp);
                                                }
                                                Toast.makeText(this, userId.optString("msg"), Toast.LENGTH_SHORT).show();
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        } finally {
                                            Loading.dismiss();
                                        }
                                    }).start();
                                }).create();
                        dialog.show();

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    /**
     * 设置标题 和 显示头像
     */
    private void setToolbar() {
        toolbar.setTitle("");
        ((TextView) toolbar.getChildAt(0)).setText("个人信息");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String defaultImage = AppConfig.spf.getString("defaultImage", "");
        String name = AppConfig.spf.getString("name", "");
        String sex = AppConfig.spf.getString("sex", "");
        String school = AppConfig.spf.getString("school", "");
        String club = AppConfig.spf.getString("club", "");
//        String clubName = AppConfig.spf.getString("clubName", "");
        RequestOptions options = new RequestOptions().circleCropTransform();
        Glide.with(this)
                .load(TextUtils.isEmpty(defaultImage) ? R.mipmap.defaultimage : HttpUtils.HOST + "app/upload/" + defaultImage)
                .apply(options)
                .into(iv);
        ((TextView) setName.getChildAt(0)).setText(name);
        ((TextView) setSex.getChildAt(0)).setText(TextUtils.isEmpty(sex) ? "未选择" : sex);
        ((TextView) setSchool.getChildAt(0)).setText(TextUtils.isEmpty(school) ? "未选择" : school);
//        ((TextView) setClub.getChildAt(0)).setText(TextUtils.isEmpty(club) ? "未加入" : clubName);
    }

    public Intent selectPicture() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return intent;
    }

    /**
     * 更改头像
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO 自动生成的方法存根
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            //查询我们需要的数据
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Log.e("TAG", "onActivityResult: " + picturePath);
            //拿到了图片的路径picturePath可以自行使用
            iv.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            File file = new File(picturePath);
            Log.e(TAG, "文件大小: " + file.length() + " byte  " + (file.length() / 1024) + " KB");
            if (file.length() > 2 * 1024 * 1024) {
                Toast.makeText(this, "照片过大,请示选择 2MB 以下的图片", Toast.LENGTH_SHORT).show();
                startActivityForResult(selectPicture(), 1);
            } else {
                Loading.show(PersonalCenterActivity.this, "正在上传...");
                new Thread(() -> {
                    try {
                        String userId = HttpUtils.send("app/upFile/" + AppConfig.spf.getString("userId", ""), file);
                        JSONObject jsonObject = new JSONObject(userId);
                        runOnUiThread(() -> {
                            if (jsonObject.optString("msg").equals("上传成功")) {
                                AppConfig.spfEditor.putString("defaultImage", jsonObject.optString("url")).apply();
                            }
                            Toast.makeText(this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        Loading.dismiss();
                    }
                }).start();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 监听返回按钮
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}