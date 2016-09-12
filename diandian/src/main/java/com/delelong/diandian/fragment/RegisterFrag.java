package com.delelong.diandian.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.delelong.diandian.LoginActivity;
import com.delelong.diandian.R;
import com.delelong.diandian.utils.MD5;

import java.util.List;

/**
 * Created by Administrator on 2016/8/19.
 */
public class RegisterFrag extends Fragment implements View.OnClickListener {
    private static final String URL_REGISTER = "http://121.40.142.141:8090/Jfinal/api/register";
    private static final String URL_SMSCODE = "http://121.40.142.141:8090/Jfinal/api/smscode";
    //验证码类型
    private static final String TYPE_REGISTER = "1";
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_register, container, false);

        initView();
        return view;
    }

    EditText edt_phone, edt_verificationCode, edt_newPwd, edt_rePwd;
    Button btn_verificationCode, btn_register;
    ImageButton img_showPwd, btn_back;

    private void initView() {
        edt_phone = (EditText) view.findViewById(R.id.edt_phone);
        edt_verificationCode = (EditText) view.findViewById(R.id.edt_verificationCode);
        edt_newPwd = (EditText) view.findViewById(R.id.edt_newPwd);
        edt_rePwd = (EditText) view.findViewById(R.id.edt_rePwd);

        btn_verificationCode = (Button) view.findViewById(R.id.btn_verificationCode);
        btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_verificationCode.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        img_showPwd = (ImageButton) view.findViewById(R.id.img_showPwd);
        btn_back = (ImageButton) view.findViewById(R.id.btn_back);
        img_showPwd.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    private boolean showPwd;
    private String phone, verificationCode, pwd, rePwd;
    List<String> resultForReg;

    @Override
    public void onClick(View v) {
        phone = edt_phone.getText().toString();
        verificationCode = edt_verificationCode.getText().toString();
        String pwd_edt = edt_newPwd.getText().toString();
        String rePwd_edt = edt_rePwd.getText().toString();
        pwd = MD5.getMD5Str(pwd_edt);
        rePwd = MD5.getMD5Str(rePwd_edt);

        switch (v.getId()) {
            case R.id.img_showPwd:
                showPwd();
                break;
            case R.id.img_showPwd1:
                showPwd();
                break;
            case R.id.btn_back:
                getActivity().getFragmentManager().popBackStack();
                break;
            case R.id.btn_verificationCode:
                postForVerification();
                if (resultForVerific.get(0).equals("FAILURE")) {
                    //失败 重新获取
                    postForVerification();
                    if (resultForVerific.get(0).equals("FAILURE")) {
                        Toast.makeText(activity, "获取失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_register:
                if (!pwd_edt.equals(rePwd_edt)) {
                    Toast.makeText(activity, "请确认密码一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (verificationCode.isEmpty()) {
                    Toast.makeText(activity, "请填写验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                resultForReg = activity.registerApp(URL_REGISTER, phone, verificationCode, pwd, rePwd);
                if (resultForReg.get(0).equals("FAILURE")) {
                    resultForReg = activity.registerApp(URL_REGISTER, phone, verificationCode, pwd, rePwd);
                    if (resultForReg.get(0).equals("FAILURE")) {
                        Toast.makeText(activity, "注册失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (resultForReg.get(0).equals("ERROR")) {
                    resultForReg = activity.registerApp(URL_REGISTER, phone, verificationCode, pwd, rePwd);
                    if (resultForReg.get(0).equals("ERROR")) {
                        Toast.makeText(activity, "注册错误，请稍后重试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //注册成功,返回登陆界面
                Toast.makeText(activity, "注册成功", Toast.LENGTH_SHORT).show();
                //保存手机号并设置为首次登陆（进入登陆页面）
                boolean firstLogin = true;
                preferences.edit()
                        .putString("phone", phone)
                        .putBoolean("firstLogin", firstLogin)
                        .apply();
                //进入登陆页面
                getActivity().getFragmentManager().popBackStack();
                break;
        }
    }

    private void postForVerification() {
        MyHttpRun code = new MyHttpRun();
        Thread thread = new Thread(code);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    List<String> resultForVerific;

    public class MyHttpRun implements Runnable {
        @Override
        public void run() {
            //type:(1:注册;2:忘记密码;3:更换手机号;)
            resultForVerific = activity.getHttpResultForVerification(URL_SMSCODE, phone, TYPE_REGISTER);
        }
    }

    /**
     * 切换显示密码
     */
    private void showPwd() {
        if (showPwd) {
            edt_newPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            img_showPwd.setImageResource(R.drawable.show_open);
        } else {
            edt_newPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            img_showPwd.setImageResource(R.drawable.show_close);
        }
        showPwd = !showPwd;

    }

    LoginActivity activity;
    SharedPreferences preferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (LoginActivity) getActivity();
        preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
    }


}
