package com.example.app.register;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.app.R;
import com.example.app.utils.HttpUtils;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

/**
 * 注册界面
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";
    private EditText editTextP, editSMS, editTextCT;
    private Button button;
    private TextView textViewTerms;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        init();
    }

    private void init() {
        editTextP = findViewById(R.id.et_phone_num);
        editSMS = findViewById(R.id.et_sms_code);
        editTextCT = findViewById(R.id.et_password);
        textViewTerms = findViewById(R.id.tv_termOfService);   //这个是注册中的《使用条款》
        textViewTerms.setOnClickListener(this);   //《使用条款》设置监听
        button = findViewById(R.id.bn_immediateRegistration);
        button.setOnClickListener(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bn_immediateRegistration:
                register();
                break;
            //点击注册条款，显示dialog提示框，包含《使用条款》的全部内容
            case R.id.tv_termOfService:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                        .setTitle("使用条款")  //这是提示框头部提示文字
                        .setMessage(this.getResources().getString(R.string.Terms))//提示框主体内容，放在String字符集里的terms中
                        .setCancelable(false)//表示点击dialog其它部分不能取消(除了“取消”，“确定”按钮)
                        .setPositiveButton("已阅", new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(RegisterActivity.this, "您点击了已阅", Toast.LENGTH_SHORT).show();

                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//    dialogInterface.dismiss();
                            }
                        });
                alertDialog.show();//别忘了show
        }
    }

    public void register() {
        final String username = editTextP.getText().toString().trim();
        final String password = editSMS.getText().toString().trim();
        String confirm_password = editTextCT.getText().toString().trim();
        if (TextUtils.isEmpty(username) | username.length() < 5) {  //当用户名没有输入时
            Toast.makeText(this, "用户名不能为空,或小于5位字符!", Toast.LENGTH_SHORT).show();
            editTextP.requestFocus();//使输入框失去焦点
            return;
        } else if (TextUtils.isEmpty(password) | password.length() < 6) {//当密码没有输入时
            Toast.makeText(this, "密码不能为空,或小于6位字符", Toast.LENGTH_SHORT).show();
            editSMS.requestFocus();//使输入框失去焦点
            return;
        } else if (TextUtils.isEmpty(confirm_password)) {//当注册密码没有输入时
            Toast.makeText(this, "重复密码不能为空！", Toast.LENGTH_SHORT).show();
            editTextCT.requestFocus();//使输入框失去焦点
            return;
        } else if (!password.equals(confirm_password)) {
            Toast.makeText(this, "两次密码不一致,请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            final ProgressDialog pd = new ProgressDialog(this);


            new Thread(() -> {
                //注册的操作放在此处
                try {
                    String data = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
                    JSONObject send = HttpUtils.send("app/registry", data);
                    TimeUnit.SECONDS.sleep(1);
                    runOnUiThread(() -> {
                        Toast.makeText(RegisterActivity.this, send.optString("msg"), Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } catch (Exception e) {
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Looper.loop();// 进入loop中的循环，查看消息队列
                } finally {

                }

            }).start();

        }
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
