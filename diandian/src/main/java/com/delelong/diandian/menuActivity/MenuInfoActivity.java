package com.delelong.diandian.menuActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.delelong.diandian.BaseActivity;
import com.delelong.diandian.R;
import com.delelong.diandian.bean.Client;
import com.delelong.diandian.view.RoundImageView;

/**
 * Created by Administrator on 2016/9/7.
 */
public class MenuInfoActivity extends BaseActivity implements View.OnClickListener{

    private static final String URL_MEMBER = "http://121.40.142.141:8090/Jfinal/api/member";//获取会员信息
    private static final String URL_UPDATECLIENT = "http://121.40.142.141:8090/Jfinal/api/member/update";//更新会员信息（GET）
    private static final String URL_HEAD_PORTRAIT = "http://121.40.142.141:8090/Jfinal/";//图片头地址
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_menuinfo);
        initActionBar();
        initView();
        initMsg();
    }

    RoundImageView img_head;
    TextView tv_nick_name,tv_signature;
    TextView tv_certification_detail,tv_owner_detail;

    private void initView() {
        img_head = (RoundImageView) findViewById(R.id.img_head);
        img_head.setType(RoundImageView.TYPE_CIRCLE);

        tv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
        tv_signature = (TextView) findViewById(R.id.tv_signature);
        tv_certification_detail = (TextView) findViewById(R.id.tv_certification_detail);
        tv_owner_detail = (TextView) findViewById(R.id.tv_owner_detail);
    }

    Client client;
    private void initMsg() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        client = (Client) bundle.getSerializable("client");//从上级activity获取
        if (client == null){
            client = getClientByGET(URL_MEMBER);
        }

        int level = client.getLevel();
        String phone = client.getPhone();
//        String post_code = client.getPost_code();
//        String urgent_name = client.getUrgent_name();
//        String urgent_phone = client.getUrgent_phone();
        String nick_name = client.getNick_name();
//        String certificate_type = client.getCertificate_type();
        String head_portrait = client.getHead_portrait();
//        String county = client.getCounty();
//        String province = client.getProvince();
//        String city = client.getCity();
//        String address = client.getAddress();
//        String email = client.getEmail();
//        int gender = client.getGender();
        String certificate_no = client.getCertificate_no();
        String real_name = client.getReal_name();
        //设置头像
        MyHeadTask myHeadTask = new MyHeadTask(img_head);
        myHeadTask.execute(URL_HEAD_PORTRAIT, head_portrait);
        if (nick_name.equals("")){
            tv_nick_name.setText(phone);
            tv_signature.setVisibility(View.VISIBLE);
        }
        else {
            tv_nick_name.setText(nick_name);
            tv_signature.setVisibility(View.GONE);
        }
        if (!certificate_no.equals("")){
            tv_certification_detail.setText("已认证");
            tv_certification_detail.setTextColor(R.color.colorPinChe);
        }
    }

    ImageButton arrow_back;
    TextView tv_modifyInfo;
    private void initActionBar() {
        arrow_back = (ImageButton) findViewById(R.id.arrow_back);
        tv_modifyInfo = (TextView) findViewById(R.id.tv_modifyInfo);
        arrow_back.setOnClickListener(this);
        tv_modifyInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.arrow_back:
                finish();
                break;
            case R.id.tv_modifyInfo:
                Bundle bundle = getIntent().getBundleExtra("bundle");
                bundle.putSerializable("client",client);
                intentActivityWithBundle(this,MenuModifyInfoActivity.class,bundle);
                break;
        }
    }


}
