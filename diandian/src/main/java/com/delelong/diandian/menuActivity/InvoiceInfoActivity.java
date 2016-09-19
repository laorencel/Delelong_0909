package com.delelong.diandian.menuActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;

import com.delelong.diandian.BaseActivity;
import com.delelong.diandian.R;
import com.delelong.diandian.bean.Client;
import com.delelong.diandian.bean.Str;
import com.delelong.diandian.http.HttpUtils;

/**
 * Created by Administrator on 2016/9/19.
 */
public class InvoiceInfoActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_invoice_info);
        initActionBar();
        initView();
        initMsg();
    }

    Double totalSum;
    HttpUtils httpUtils ;
    Client client;
    private void initMsg() {
        httpUtils = new HttpUtils(this);
        client = httpUtils.getClientByGET(Str.URL_MEMBER);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        totalSum = bundle.getDouble("totalSum");
    }

    private void initView() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arrow_back:
                finish();
                break;

        }
    }

    ImageButton arrow_back;
    private void initActionBar() {
        arrow_back = (ImageButton) findViewById(R.id.arrow_back);
        arrow_back.setOnClickListener(this);
    }
}
