package com.delelong.diandian.menuActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.delelong.diandian.BaseActivity;
import com.delelong.diandian.R;

/**
 * Created by Administrator on 2016/9/13.
 */
public class FeedBackActivity extends BaseActivity implements View.OnClickListener{

    private static final String URL_QQCONTACT="mqqwpa://im/chat?chat_type=wpa&uin=850705356";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_menu_feedback);
        initActionBar();
        initView();
    }

    EditText edt_feedBack;
    Button btn_feedBack;
    ImageView img_qq_contact;
    private void initView() {
        edt_feedBack = (EditText) findViewById(R.id.edt_feedBack);
        btn_feedBack = (Button) findViewById(R.id.btn_feedBack);
        img_qq_contact = (ImageView) findViewById(R.id.img_qq_contact);

        btn_feedBack.setOnClickListener(this);
        img_qq_contact.setOnClickListener(this);
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
            case R.id.btn_feedBack:
                finish();
                break;
            case R.id.img_qq_contact:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL_QQCONTACT)));
                break;
        }
    }
}
