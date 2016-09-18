package com.delelong.diandian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.delelong.diandian.http.HttpUtils;

import java.util.List;

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
//            List<String> loginResult = loginApp(URL_LOGIN, phone, pwd);
            HttpUtils httpUtils = new HttpUtils(this);
            List<String> loginResult = httpUtils.login(URL_LOGIN, phone, pwd);

            if (loginResult.get(0).equalsIgnoreCase("OK")){
                startActivity(new Intent(this, MainActivity.class));
            }
            else if (loginResult.get(0).equals("ERROR")) {
                Toast.makeText(this, "登陆出错,请重新登陆", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
            }else if (loginResult.get(0).equals("FAILURE")) {
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
        }
    }
}
