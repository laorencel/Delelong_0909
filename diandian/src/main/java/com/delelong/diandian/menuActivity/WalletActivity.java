package com.delelong.diandian.menuActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.delelong.diandian.BaseActivity;
import com.delelong.diandian.R;
import com.delelong.diandian.bean.Client;
import com.delelong.diandian.bean.Str;
import com.delelong.diandian.http.HttpUtils;
import com.delelong.diandian.pace.MyAMapLocation;

/**
 * Created by Administrator on 2016/9/18.
 */
public class WalletActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_wallet);
        initActionBar();
        initView();
        initMsg();
    }

    RelativeLayout rl_pay,rl_sum,rl_coupon,rl_invoice;
    TextView tv_pay,tv_sum,tv_coupon;
    private void initView() {
        rl_pay = (RelativeLayout) findViewById(R.id.rl_pay);//支付方式
        rl_sum = (RelativeLayout) findViewById(R.id.rl_sum);//余额
        rl_coupon = (RelativeLayout) findViewById(R.id.rl_coupon);//优惠券
        rl_invoice = (RelativeLayout) findViewById(R.id.rl_invoice);//发票报销

        tv_pay = (TextView) findViewById(R.id.tv_pay);
        tv_sum = (TextView) findViewById(R.id.tv_sum);
        tv_coupon = (TextView) findViewById(R.id.tv_coupon);

        rl_pay.setOnClickListener(this);
        rl_sum.setOnClickListener(this);
        rl_coupon.setOnClickListener(this);
        rl_invoice.setOnClickListener(this);
    }

    HttpUtils httpUtils;
    Client client;
    MyAMapLocation myAMapLocation;
    SharedPreferences preferences;
    Bundle bundle;
    private void initMsg() {
        httpUtils = new HttpUtils(this);
        bundle = getIntent().getBundleExtra("bundle");
        myAMapLocation = (MyAMapLocation) bundle.getSerializable("myAMapLocation");
        client = (Client) bundle.getSerializable("client");//从上级activity获取
        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        if (client == null){
            client = httpUtils.getClientByGET(Str.URL_MEMBER);
        }


    }

    ImageButton arrow_back;
    private void initActionBar() {
        arrow_back = (ImageButton) findViewById(R.id.arrow_back);
        arrow_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arrow_back:
                finish();
                break;
            case R.id.rl_pay:
                startActivityWithBundle(PaymentActivity.class,myAMapLocation,client);
                break;
            case R.id.rl_sum:
                break;
            case R.id.rl_coupon:
                break;
            case R.id.rl_invoice:
                intentActivityWithBundle(this,InvoiceActivity.class,bundle);
                break;
        }
    }
}
