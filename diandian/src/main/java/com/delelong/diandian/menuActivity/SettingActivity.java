package com.delelong.diandian.menuActivity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.delelong.diandian.BaseActivity;
import com.delelong.diandian.R;
import com.delelong.diandian.fragment.ForgotFrag;

/**
 * Created by Administrator on 2016/9/6.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_setting);
        initActionBar();
        initView();
        initFragment();
    }
    FragmentManager fragmentManager;
    ForgotFrag forgotFrag;
    private void initFragment() {
        fragmentManager = getFragmentManager();
        forgotFrag = new ForgotFrag();
    }

    Button btn_modifyPwd;
    Button btn_commonAddress,btn_remindMode;
    Button btn_clause,btn_versionUpdate,btn_aboutUs;
    private void initView() {
        btn_modifyPwd = (Button) findViewById(R.id.btn_modifyPwd);

        btn_commonAddress = (Button) findViewById(R.id.btn_commonAddress);
        btn_remindMode = (Button) findViewById(R.id.btn_remindMode);

        btn_clause = (Button) findViewById(R.id.btn_clause);
        btn_versionUpdate = (Button) findViewById(R.id.btn_versionUpdate);
        btn_aboutUs = (Button) findViewById(R.id.btn_aboutUs);

        btn_modifyPwd.setOnClickListener(this);
        btn_commonAddress.setOnClickListener(this);
        btn_remindMode.setOnClickListener(this);
        btn_clause.setOnClickListener(this);
        btn_versionUpdate.setOnClickListener(this);
        btn_aboutUs.setOnClickListener(this);
    }

    ImageButton arrow_back;
    private void initActionBar() {
        arrow_back = (ImageButton) findViewById(R.id.arrow_back);
        arrow_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.arrow_back:
                finish();
                break;
            case R.id.btn_modifyPwd:
                finish();
                break;
            case R.id.btn_commonAddress:
                finish();
                break;
            case R.id.btn_remindMode:
                finish();
                break;
            case R.id.btn_clause:
                finish();
                break;
            case R.id.btn_versionUpdate:
                finish();
                break;
            case R.id.btn_aboutUs:
                finish();
                break;
        }

    }
}
