package com.example.app.modules;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.app.R;
import com.example.app.pojo.Club;
import com.example.app.utils.AppConfig;
import com.example.app.utils.HttpUtils;
import com.example.app.utils.Loading;
import com.example.app.utils.PermissionUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunityJoinActivity extends AppCompatActivity {
    private static String TAG = "社团入驻(CommunityJoinActivity): ";
    private Toolbar toolbar;
    private EditText etName;
    private CircleImageView iv;
    private TextView tvDate;
    private EditText etDeclaration;
    private EditText etInfo;
    private EditText etRules;
    private Button btn;
    private String imgFile;
    private Calendar calendar = Calendar.getInstance();
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_join);
        initView();
        setonClick();
    }

    private void setonClick() {
        iv.setOnClickListener(v -> {
            PermissionUtils.isGrantExternalRW(this, 0);
            startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1);
        });
        tvDate.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(CommunityJoinActivity.this,
                    android.R.style.Theme_DeviceDefault_Light_Dialog_Alert,
                    (view, year, month, dayOfMonth) -> {
                        String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                        tvDate.setText(date);
                        Log.e(TAG, "获取日期: " + date);
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_MONTH), calendar.get(Calendar.DATE));
            dialog.show();
        });
        btn.setOnClickListener(v -> {
            //判断输入信息是否为空
            String[] msg = {"社团名称不能为空!", "请选择社团头像", "请选择社团创建日期", "请输入社团宣言", "请输入社团简介", "请输入社团规则"};
            List<String> data = Arrays.asList(etName.getText().toString(), imgFile, tvDate.getText().toString(),
                    etDeclaration.getText().toString(), etInfo.getText().toString(), etRules.getText().toString());
            for (int i = 0; i < data.size(); i++) {
                if (TextUtils.isEmpty(data.get(i))) {
                    Toast.makeText(this, msg[i], Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Club club = new Club();
            club.setC_name(data.get(0));
            club.setC_date(data.get(2));
            club.setDeclaration(data.get(3));
            club.setC_info(data.get(4));
            club.setC_rules(data.get(5));
            club.setC_school_id(AppConfig.spf.getString("school", ""));
            club.setFlag(0);
            Log.e(TAG, "加载数据: " + gson.toJson(club));
            Loading.show(this, "请求中...");
            new Thread(() -> {
                try {
                    String send = HttpUtils.send("/app/addClub", new File(imgFile), gson.toJson(club));
                    JSONObject jsonObject = new JSONObject(send);
                    runOnUiThread(() -> {
                        if (jsonObject.optString("msg").equals("申请成功")) {
                            finish();
                        }
                        Toast.makeText(this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Loading.dismiss();
                }
            }).start();
        });
    }

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
            Log.e("TAG", "得到图片路径: " + picturePath);

            File file = new File(picturePath);
            Log.e(TAG, "文件大小: " + file.length() + " byte  " + (file.length() / 1024) + " KB");
            if (file.length() > 2 * 1024 * 1024) {
                Toast.makeText(this, "照片过大,请示选择 2MB 以下的图片", Toast.LENGTH_SHORT).show();
            } else {
                //拿到了图片的路径picturePath可以自行使用
                iv.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                imgFile = picturePath;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void initView() {
        etName = findViewById(R.id.et_name);
        iv = findViewById(R.id.iv);
        tvDate = findViewById(R.id.tv_date);
        etDeclaration = findViewById(R.id.et_declaration);
        etInfo = findViewById(R.id.et_info);
        etRules = findViewById(R.id.et_rules);
        toolbar = findViewById(R.id.toolbar);
        btn = findViewById(R.id.btn);
        toolbar.setTitle("");
        ((TextView) toolbar.getChildAt(0)).setText("社团入驻");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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