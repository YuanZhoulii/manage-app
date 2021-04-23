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
import com.example.app.utils.AppConfig;
import com.example.app.utils.HttpUtils;
import com.example.app.utils.Loading;
import com.example.app.utils.RSAUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddVoteActivity extends AppCompatActivity {
    private static final String TAG = "AddVoteActivity";
    private Toolbar toolbar;
    private EditText etValue;
    private TextView tvTime;
    private Button btn;
    private Calendar calendar = Calendar.getInstance();
    private int clubid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vote);
        clubid = getIntent().getIntExtra("data", 0);
        Log.e(TAG, "onCreate: " + clubid);
        initView();
        setToolbar();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        etValue = findViewById(R.id.et_value);
        tvTime = findViewById(R.id.tv_time);
        tvTime.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(AddVoteActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert,
                    (view, year, month, dayOfMonth) -> {
                        String date = year + "-" + ((month + 1) < 10 ? "0" + (month + 1) : (month + 1)) + "-" + dayOfMonth;
                        int temp = Integer.valueOf(date.replace("-", ""));
                        int old = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(calendar.getTime()));
                        if (temp - old < 0) {
                            Toast.makeText(this, "结束日期 不能 小于今天", Toast.LENGTH_SHORT).show();
//                            tvTime.setHint("请选择日期");
                            return;
                        }
                        tvTime.setText(date);
                        Log.e(TAG, "获取日期: " + date);
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            dialog.show();
        });
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(v -> {
            String value = etValue.getText().toString();
            String date = tvTime.getText().toString();
            if (TextUtils.isEmpty(value) || date.isEmpty()) {
                Toast.makeText(this, "请输入内容或选择时间", Toast.LENGTH_SHORT).show();
                return;
            }
            Loading.show(this, "添加中");
            new Thread(() -> {
                String d = "{\"clubid\":\"" + clubid + "\",\"userid\":\"" + AppConfig.spf.getString("userId", "") + "\",\"value\":\"" + value + "\",\"endDate\":\"" + date + "\"}";
                try {
                    JSONObject send = HttpUtils.send("app/addVote", RSAUtils.encryption(d));
                    runOnUiThread(() -> {
                        Toast.makeText(this, send.optString("msg"), Toast.LENGTH_SHORT).show();
                        etValue.setText("");
                        tvTime.setHint("选择时间");
                        tvTime.setText("");
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
        ((TextView) toolbar.getChildAt(0)).setText("添加决策");
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