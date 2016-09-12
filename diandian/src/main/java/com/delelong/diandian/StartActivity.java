package com.delelong.diandian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2016/8/26.
 */
public class StartActivity extends BaseActivity {

    private static final String TAG = "BAIDUMAPFORTEST";
    public static final String URL_LOGIN = "http://121.40.142.141:8090/Jfinal/api/login";
    Handler handler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_start);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        init();
                        break;
                }
            }
        };

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        },3000);
    }

    String phone, pwd;
    boolean firstLogin;

    private void init() {
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        firstLogin = preferences.getBoolean("firstLogin", true);
        if (firstLogin) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            phone = preferences.getString("phone", null);
            pwd = preferences.getString("pwd", null);
            loginAppByPreferences(URL_LOGIN, phone, pwd);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
