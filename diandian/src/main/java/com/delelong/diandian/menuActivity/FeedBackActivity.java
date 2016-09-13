package com.delelong.diandian.menuActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.delelong.diandian.BaseActivity;
import com.delelong.diandian.R;
import com.delelong.diandian.bean.Client;
import com.delelong.diandian.pace.MyAMapLocation;

/**
 * Created by Administrator on 2016/9/13.
 */
public class FeedBackActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "BAIDUMAPFORTEST";

//    private static final String URL_QQCONTACT="mqqwpa://im/chat?chat_type=wpa&uin=850705356";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_menu_feedback);
        initActionBar();
        initView();
        getServicePhone();
    }

    EditText edt_feedBack;
    Button btn_feedBack;
    TextView tv_contact_phone;
    private void initView() {
        edt_feedBack = (EditText) findViewById(R.id.edt_feedBack);
        btn_feedBack = (Button) findViewById(R.id.btn_feedBack);
        tv_contact_phone = (TextView) findViewById(R.id.tv_contact_phone);

        btn_feedBack.setOnClickListener(this);
        tv_contact_phone.setOnClickListener(this);
        phone = "18355407487";//需要从服务器获取（根据adcode）
    }

    ImageButton arrow_back;
    private void initActionBar() {
        arrow_back = (ImageButton) findViewById(R.id.arrow_back);
        arrow_back.setOnClickListener(this);
    }

    String phone;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.arrow_back:
                finish();
                break;
            case R.id.btn_feedBack:
                finish();
                break;
            case R.id.tv_contact_phone:
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL_QQCONTACT)));
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone));
                    startActivity(callIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    Client client;
    MyAMapLocation myAMapLocation;
    SharedPreferences preferences;
    /**
     * 获取当地客服电话
     * @return
     */
    public String  getServicePhone() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        myAMapLocation = (MyAMapLocation) bundle.getSerializable("myAMapLocation");
        client = (Client) bundle.getSerializable("client");//从上级activity获取
        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);

        String adCode = myAMapLocation.getAdCode();

        String phone = null;
        return phone;
    }
}
