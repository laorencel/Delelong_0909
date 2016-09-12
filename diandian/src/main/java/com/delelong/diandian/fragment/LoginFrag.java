package com.delelong.diandian.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.delelong.diandian.LoginActivity;
import com.delelong.diandian.MainActivity;
import com.delelong.diandian.R;
import com.delelong.diandian.utils.MD5;

import java.util.List;


/**
 * Created by Administrator on 2016/8/18.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LoginFrag extends Fragment implements View.OnClickListener {

    private static final String TAG = "BAIDUMAPFORTEST";
    private static final String URL_LOGIN = "http://121.40.142.141:8090/Jfinal/api/login";
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_login, container, false);

        initView();
        return view;
    }

    EditText edt_phone, edt_pwd;
    Button btn_login;
    TextView tv_register, tv_forgotPwd;

    /**
     * 初始化view
     */
    private void initView() {
        edt_phone = (EditText) view.findViewById(R.id.edt_phone);
        edt_pwd = (EditText) view.findViewById(R.id.edt_pwd);
        btn_login = (Button) view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        tv_forgotPwd = (TextView) view.findViewById(R.id.tv_forgotPwd);
        tv_register = (TextView) view.findViewById(R.id.tv_register);

        //如果以前有登陆过，获取登陆的手机号
        preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String preferenceName = preferences.getString("phone", null);
        if (preferenceName != null) {
            edt_phone.setText(preferenceName);
        }
    }

    String phone, pwd_edt, pwd;
    SharedPreferences preferences;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                phone = edt_phone.getText().toString();
                pwd_edt = edt_pwd.getText().toString();
                pwd = MD5.getMD5Str(pwd_edt);
                Log.i(TAG, "onClick: " + pwd);
                login();
                break;
        }
    }

    /**
     * 验证登录
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void login() {
        if (phone.isEmpty() || pwd.isEmpty()) {
            Toast.makeText(activity, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (phone.length() != 11 || pwd.length() < 6) {
                Toast.makeText(activity, "号码或密码长度不够", Toast.LENGTH_SHORT).show();
                return;
            } else {
                List<String> result = activity.loginApp(URL_LOGIN, phone, pwd);
                if (result.get(0).equals("OK")) {
                    LoginActivity finish = (LoginActivity) getActivity();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    //存储用户(token)、密码(sercet)
                    boolean firstLogin = false;

                    Log.i(TAG, "login: "+result);
                    preferences.edit()
                            .putString("token", result.get(2))
                            .putString("sercet", result.get(3))
                            .putString("phone", phone)
                            .putString("pwd", pwd)
                            .putBoolean("firstLogin", firstLogin)
                            .apply();
                    finish.finish();
                } else if (result.get(0).equals("ERROR")) {
                    Toast.makeText(getActivity(), "登陆出错,请重新登陆", Toast.LENGTH_SHORT).show();
                    return;
                }else if (result.get(0).equals("FAILURE")) {
                    Toast.makeText(getActivity(), "登陆失败,请重新登陆", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }


    LoginActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (LoginActivity) getActivity();
    }

}
