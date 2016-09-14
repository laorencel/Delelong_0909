package com.delelong.diandian.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
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
import com.delelong.diandian.bean.Str;
import com.delelong.diandian.http.HttpUtils;
import com.delelong.diandian.utils.MD5;

import java.util.List;

/**
 * Created by Administrator on 2016/8/20.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ModifyFrag extends Fragment implements View.OnClickListener {
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_modify, container, false);

        initView();
        return view;
    }

    EditText edt_pwd, edt_newPwd, edt_rePwd;
    Button btn_modify;
    ImageButton img_showPwd, img_showPwd1, btn_back;

    private void initView() {
        //隐藏不需要的验证码模块

        edt_pwd = (EditText) view.findViewById(R.id.edt_pwd);
        edt_newPwd = (EditText) view.findViewById(R.id.edt_newPwd);
        edt_rePwd = (EditText) view.findViewById(R.id.edt_rePwd);
        img_showPwd = (ImageButton) view.findViewById(R.id.img_showPwd);
        img_showPwd1 = (ImageButton) view.findViewById(R.id.img_showPwd1);

        btn_back = (ImageButton) view.findViewById(R.id.btn_back);

        btn_modify = (Button) view.findViewById(R.id.btn_modify);

        img_showPwd.setOnClickListener(this);
        img_showPwd1.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_modify.setOnClickListener(this);
    }

    private String pwd, newPwd, rePwd;
    List<String> resultForMod;

    @Override
    public void onClick(View v) {
        String pwd_edt = edt_newPwd.getText().toString();
        String newPwd_edt = edt_newPwd.getText().toString();
        String rePwd_edt = edt_rePwd.getText().toString();

        pwd = MD5.getMD5Str(pwd_edt);
        newPwd = MD5.getMD5Str(newPwd_edt);
        rePwd = MD5.getMD5Str(rePwd_edt);

        switch (v.getId()) {
            case R.id.img_showPwd:
                showPwd();
                break;
            case R.id.img_showPwd1:
                showPwd();
                break;
            case R.id.btn_back:
                activity.getFragmentManager().popBackStack();
                break;
            case R.id.btn_modify:
                if (!pwd_edt.equals(rePwd_edt)) {
                    Toast.makeText(activity, "请确认密码一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                HttpUtils httpUtils = new HttpUtils(activity);
//                resultForMod = activity.modifyPwd(URL_MODIFY, pwd, newPwd, rePwd);
                resultForMod = httpUtils.modifyPwd(Str.URL_MODIFY, pwd, newPwd, rePwd);
                if (resultForMod.get(0).equals("FAILURE")) {
                    resultForMod = httpUtils.modifyPwd(Str.URL_MODIFY, pwd, newPwd, rePwd);
                    if (resultForMod.get(0).equals("FAILURE")) {
                        Toast.makeText(activity, "修改失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (resultForMod.get(0).equals("ERROR")) {
                    resultForMod = httpUtils.modifyPwd(Str.URL_MODIFY, pwd, newPwd, rePwd);
                    if (resultForMod.get(0).equals("ERROR")) {
                        Toast.makeText(activity, "修改错误，请稍后重试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //注册成功,返回登陆界面
                Toast.makeText(activity, "修改成功", Toast.LENGTH_SHORT).show();
                //进入登陆页面
                getActivity().getFragmentManager().popBackStack();
                break;
        }
    }

    private boolean showPwd;

    /**
     * 切换显示密码
     */
    private void showPwd() {
        if (showPwd) {
            edt_newPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            edt_rePwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            img_showPwd.setImageResource(R.drawable.show_open);
            img_showPwd1.setImageResource(R.drawable.show_open);
        } else {
            edt_newPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            edt_rePwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            img_showPwd.setImageResource(R.drawable.show_close);
            img_showPwd1.setImageResource(R.drawable.show_close);
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
