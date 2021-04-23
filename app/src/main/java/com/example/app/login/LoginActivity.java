package com.example.app.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.app.MainActivity;
import com.example.app.R;
import com.example.app.register.RegisterActivity;
import com.example.app.utils.AppConfig;
import com.example.app.utils.HttpUtils;
import com.example.app.utils.RSAUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "LoginActivity";
    private EditText editPerson, editCode;
    private TextView textViewR, textViewTerms;
    private Button btn;
    private boolean autoLogin = false;
    public static String currentUsername;
    private String currentPassword;
    private boolean progressShow;
    private ImageView qq, weixin, weibo;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        btn = findViewById(R.id.bn_common_login);
        btn.setOnClickListener(this);
        editCode = findViewById(R.id.et_password);
        editPerson = findViewById(R.id.et_username);
        textViewR = findViewById(R.id.tv_register);
        textViewTerms = findViewById(R.id.tv_termOfService);   //这个是《使用条款》
        qq = findViewById(R.id.iv_qq_login);
        weixin = findViewById(R.id.iv_weixin_login);
        weibo = findViewById(R.id.iv_sina_login);
        textViewTerms.setOnClickListener(this);   //《使用条款》设置监听
        qq.setOnClickListener(this);
        weixin.setOnClickListener(this);
        weibo.setOnClickListener(this);
        textViewR.setOnClickListener(this);
        toolbar.setTitle("");
        toolbar.getChildAt(1).setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
        setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.bn_common_login:  //登录按钮,显示Intent实现LoginActivity跳转至Team_MainActivity
                login(v);
                break;
            case R.id.iv_qq_login:  //QQ登录
                Toast.makeText(this, "QQ登录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_weixin_login:  //微信登录
                Toast.makeText(this, "微信登录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_sina_login:    //微博登录
                Toast.makeText(this, "微博登录", Toast.LENGTH_SHORT).show();
                break;
            //下面提示窗口只能显示"已阅"，不能显示"取消"
           /* case R.id.tv_termOfService:    //《使用条款按钮》，设置dialog提示窗口
                AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                dialog.setTitle("使用条款");  //这是提示框头部提示文字
                //下面一行是提示框主体内容，放在String字符集里的terms中
                dialog.setMessage(this.getResources().getString(R.string.Terms ));
                dialog.setCancelable(false);
                dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.setPositiveButton("已阅", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                break;
            */
            case R.id.tv_termOfService:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                        .setTitle("使用条款")  //这是提示框头部提示文字
                        .setMessage(this.getResources().getString(R.string.Terms))//提示框主体内容，放在String字符集里的terms中
                        .setCancelable(false)//表示点击dialog其它部分不能取消(除了“取消”，“确定”按钮)
                        .setPositiveButton("已阅", new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(LoginActivity.this, "您点击了已阅", Toast.LENGTH_SHORT).show();

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

    @Override
    protected void onResume() {
        super.onResume();
        if (autoLogin) {
            return;
        }
    }


    /**
     * 登录
     *
     * @param view
     */
    public void login(View view) {

        currentUsername = editPerson.getText().toString().trim(); //去除空格，获取手机号
        currentPassword = editCode.getText().toString().trim();  //去除空格，获取密码
        if (TextUtils.isEmpty(currentUsername)) { //判断手机号是不是为空
            Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {  //判断密码是不是空
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        progressShow = true;
        final ProgressDialog pd = new ProgressDialog(this);  //初始化等待动画
        /**
         * 设置监听
         * */
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(dialog -> {
            progressShow = false;   //设置Boolean值为false
        });
        new Thread(() -> {
            try {
                String data = "{\"username\":\"" + currentUsername + "\",\"password\":\"" + currentPassword + "\"}";
                JSONObject send = HttpUtils.send("app/login", RSAUtils.encryption(data));
                runOnUiThread(() -> {
                    Log.e(TAG, "login: "+send.opt("userid") );
                    Toast.makeText(this, send.optString("msg"), Toast.LENGTH_SHORT).show();
                    if (!TextUtils.isEmpty(send.optString("userid"))) {

                        String temp = new String(Base64.decode(send.optString("data").getBytes(), Base64.DEFAULT));
                        JSONObject json = null;
                        try {
                            json = new JSONObject(temp);
                            Log.e(TAG, "用户信息: " + json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        AppConfig.spfEditor
                                .putString("userId", json.optString("userid"))
                                .putString("name", json.optString("name"))
                                .putString("defaultImage", json.optString("headImage"))
                                .putString("sex", json.optString("sex"))
                                .putString("school", json.optString("school"))
                                .putString("club", json.optString("club"))
                                .putString("clubName", json.optString("clubName"))
                                .putString("flag", json.optString("flag"))
                                .apply();
                        startActivity(new Intent(this, MainActivity.class));
                        hideInput();
                        finish();
                    }
                });

            } catch (Exception e) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            } finally {
            }
        }).start();  //开始线程

    }
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

}
