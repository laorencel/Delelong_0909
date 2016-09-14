package com.delelong.diandian.menuActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.delelong.diandian.BaseActivity;
import com.delelong.diandian.ChoosePosition;
import com.delelong.diandian.R;
import com.delelong.diandian.bean.Client;
import com.delelong.diandian.bean.Str;
import com.delelong.diandian.pace.MyAMapLocation;

/**
 * Created by Administrator on 2016/9/13.
 */
public class CommonAddressActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "BAIDUMAPFORTEST";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_common_address);
        initActionBar();
        initView();
        initMsg();
    }

    LinearLayout ly_home,ly_company;
    TextView tv_home,tv_home_address;
    TextView tv_company,tv_company_address;
    private void initView() {
        ly_home = (LinearLayout) findViewById(R.id.ly_home);
        ly_company = (LinearLayout) findViewById(R.id.ly_company);
        ly_home.setOnClickListener(this);
        ly_company.setOnClickListener(this);

        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_home_address = (TextView) findViewById(R.id.tv_home_address);
        tv_company = (TextView) findViewById(R.id.tv_company);
        tv_company_address = (TextView) findViewById(R.id.tv_company_address);
    }

    ImageButton arrow_back;
    private void initActionBar() {
        arrow_back = (ImageButton) findViewById(R.id.arrow_back);
        arrow_back.setOnClickListener(this);
    }

    Client client;
    MyAMapLocation myAMapLocation;
    SharedPreferences preferences;
    private void initMsg() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        myAMapLocation = (MyAMapLocation) bundle.getSerializable("myAMapLocation");
        client = (Client) bundle.getSerializable("client");//从上级activity获取

        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String home = preferences.getString("home",null);
        String homeAddress = preferences.getString("homeAddress",null);
        String company = preferences.getString("company",null);
        String companyAddress = preferences.getString("companyAddress",null);
        if (home == null){
            tv_home.setHint("输入家庭地址");
            tv_home_address.setVisibility(View.GONE);
        }else {
            tv_home.setText(home);
            tv_home_address.setText(homeAddress);
        }
        if (company == null){
            tv_company.setHint("输入公司地址");
            tv_home_address.setVisibility(View.GONE);
        }else {
            tv_company.setText(company);
            tv_home_address.setText(companyAddress);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.arrow_back:
                finish();
                break;
            case R.id.ly_home:
                intentActivityForResult(this, ChoosePosition.class, "choose", "home", myAMapLocation.getCity(), Str.REQUESTHOMECODE);
                break;
            case R.id.ly_company:
                intentActivityForResult(this, ChoosePosition.class, "choose", "company", myAMapLocation.getCity(), Str.REQUESTCOMPANYCODE);
                break;
        }
    }


    PoiItem mHomePoiItem;//家庭poi
    PoiItem mCompanyPoiItem;//公司poi
    //获取选取的位置信息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String value = data.getStringExtra("key");
        if (value.equals("noChoice")) {
            return;
        }
        Bundle bundle = data.getBundleExtra("bundle");
        switch (resultCode) {
            case Str.REQUESTHOMECODE:
                if (value.equals("home")) {
                    mHomePoiItem = bundle.getParcelable("PoiInfo");
                    Log.i(TAG, "onActivityResult: " + mHomePoiItem.getTitle());
                    tv_home_address.setVisibility(View.VISIBLE);
                    tv_home.setText(mHomePoiItem.getTitle());
                    tv_home_address.setText(mHomePoiItem.getSnippet());
                    preferences.edit().putString("home",mHomePoiItem.getTitle())
                            .putString("homeAddress",mHomePoiItem.getSnippet())
                            .commit();
                }
                break;
            case Str.REQUESTCOMPANYCODE:
                if (value.equals("company")) {
                    mCompanyPoiItem = bundle.getParcelable("PoiInfo");
                    tv_company_address.setVisibility(View.VISIBLE);
                    tv_company.setText(mCompanyPoiItem.getTitle());
                    tv_company_address.setText(mCompanyPoiItem.getSnippet());
                    preferences.edit()
                            .putString("company",mCompanyPoiItem.getTitle())
                            .putString("companyAddress",mCompanyPoiItem.getSnippet())
                            .commit();
                }
                break;
        }
    }
}
