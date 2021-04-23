package com.example.app.modules;

import android.app.DatePickerDialog;
import android.os.Bundle;
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
import com.example.app.pojo.ClubData;
import com.example.app.utils.HttpUtils;
import com.example.app.utils.Loading;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 社团 -  社团活动 - 添加活动
 */
public class AddClubActivity extends AppCompatActivity {
    private static final String TAG = "AddVoteActivity";
    private Calendar calendar = Calendar.getInstance();
    private Toolbar toolbar;
    private EditText etTitle;
    private EditText etValue;
    private EditText etAddress;
    private TextView tvTime;
    private TextView tvEnd;
    private Button btn;
    private ClubData.DataDTO data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_club);
        data = (ClubData.DataDTO) getIntent().getSerializableExtra("data");
        initView();
        setToolbar();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        etTitle = findViewById(R.id.et_title);
        etValue = findViewById(R.id.et_value);
        etAddress = findViewById(R.id.et_address);
        tvTime = findViewById(R.id.tv_time);
        tvEnd = findViewById(R.id.tv_end);
        btn = findViewById(R.id.btn);
        tvTime.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(AddClubActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert,
                    (view, year, month, dayOfMonth) -> {
                        String date = year + "-" + ((month + 1) < 10 ? "0" + (month + 1) : (month + 1)) + "-" + dayOfMonth;
                        int temp = Integer.valueOf(date.replace("-", ""));
                        int old = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(calendar.getTime()));
                        if (temp - old < 0) {
                            Toast.makeText(this, "结束日期 不能 小于今天", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        tvTime.setText(date);
                        Log.e(TAG, "获取日期: " + date);
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            dialog.show();
        });
        tvEnd.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(AddClubActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert,
                    (view, year, month, dayOfMonth) -> {
                        String date = year + "-" + ((month + 1) < 10 ? "0" + (month + 1) : (month + 1)) + "-" + dayOfMonth;
                        if (tvTime.getText().toString().isEmpty()) {
                            Toast.makeText(this, "请选择开始时间", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int start = Integer.valueOf(tvTime.getText().toString().replace("-", ""));
                        int end = Integer.valueOf(date.replace("-", ""));

                        if (end < start) {
                            Toast.makeText(this, "结束日期 不能 开始日期", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        tvEnd.setText(date);
                        Log.e(TAG, "获取日期: " + date);
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            dialog.show();
        });
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String value = etValue.getText().toString();
            String address = etAddress.getText().toString();
            String date = tvTime.getText().toString();
            String end = tvEnd.getText().toString();
            if (TextUtils.isEmpty(address) || TextUtils.isEmpty(title) || TextUtils.isEmpty(value) || date.isEmpty() || end.isEmpty()) {
                Toast.makeText(this, "标题 内容 地址 开始时间 结束时间 都不能为空!", Toast.LENGTH_SHORT).show();
                return;
            }
            Loading.show(this, "添加中...");
            new Thread(() -> {
                String d = "{\"activity_id\":\"" + data.getCActivityId() + "\"," +
                        "\"title\":\"" + title + "\"," +
                        "\"content\":\"" + value + "\"," +
                        "\"startDate\":\"" + date + "\"," +
                        "\"endDate\":\"" + end + "\"}";
                try {
                    JSONObject send = HttpUtils.send("app/addActivity", d);
                    runOnUiThread(() -> {
                        Toast.makeText(this, send.optString("msg"), Toast.LENGTH_SHORT).show();
                        etTitle.setText("");
                        etValue.setText("");
                        etAddress.setText("");
                        tvTime.setHint("选择时间");
                        tvTime.setText("");
                        tvEnd.setHint("选择时间");
                        tvEnd.setText("");
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Loading.dismiss();
                }
            }).start();
        });

    }

    private void setToolbar() {
        toolbar.setTitle("");
        ((TextView) toolbar.getChildAt(0)).setText("添加活动");
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