package com.delelong.diandian.menuActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delelong.diandian.BaseActivity;
import com.delelong.diandian.R;
import com.delelong.diandian.bean.Client;
import com.delelong.diandian.bean.Str;
import com.delelong.diandian.http.HttpUtils;
import com.delelong.diandian.numberPicker.ChooseCityInterface;
import com.delelong.diandian.numberPicker.ChooseCityUtil;
import com.delelong.diandian.pace.MyAMapLocation;
import com.delelong.diandian.utils.ToastUtil;

/**
 * Created by Administrator on 2016/9/19.
 */
public class InvoiceInfoActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "BAIDUMAPFORTEST";

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
    HttpUtils httpUtils;
    Bundle bundle;
    Client client;
    MyAMapLocation myAMapLocation;
    private void initMsg() {
        httpUtils = new HttpUtils(this);
        bundle = getIntent().getBundleExtra("bundle");
        myAMapLocation = (MyAMapLocation) bundle.getSerializable("myAMapLocation");
        client = (Client) bundle.getSerializable("client");//从上级activity获取
        totalSum = bundle.getDouble("totalSum");
        if (client == null){
            client = httpUtils.getClientByGET(Str.URL_MEMBER);
        }

        String province_bundle, city_bundle, district_bundle, address_bundle, postCode_bundle;
        province_bundle = myAMapLocation.getProvince();
        city_bundle = myAMapLocation.getCity();
        district_bundle = myAMapLocation.getDistrict();
        address_bundle = myAMapLocation.getAddress();

        String phone = client.getPhone();
        String county = client.getCounty();
        String province = client.getProvince();
        String city = client.getCity();
        String address = client.getAddress();
        String email = client.getEmail();
        String real_name = client.getReal_name();


        edt_name.setText(real_name);
        edt_phone.setText(phone);
        tv_totalSum.setText(totalSum+"元");
        tv_totalSum.setText(Html.fromHtml("<font color='#Fe8a03'>"+totalSum+"</font> 元"));
        tv_city.setText(province + " " + city + " " + county);
        if (!tv_city.getText().toString().contains("省")) {
            tv_city.setText(province_bundle + " " + city_bundle + " " + district_bundle);
        }
        if (!address.equals("")) {
            edt_address.setText(address);
        } else {
            edt_address.setText(address_bundle);
        }
        if (edt_address.getText().toString().contains("省")){
            //删掉前缀
            String addressStr = edt_address.getText().toString();
            String[] addresslist = addressStr.split("[省市区县]");

            edt_address.setText(addresslist[addresslist.length-1]);
        }
        if (!email.equals("")) {
            edt_email.setText(email);
        }
    }

    EditText edt_company;
    TextView tv_totalSum;
    EditText edt_name,edt_phone;
    LinearLayout ly_city;
    TextView tv_city;
    EditText edt_address,edt_email;
    Button btn_confirm;
    private void initView() {
        edt_company = (EditText) findViewById(R.id.edt_company);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_phone = (EditText) findViewById(R.id.edt_phone);
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_email = (EditText) findViewById(R.id.edt_email);

        ly_city = (LinearLayout) findViewById(R.id.ly_city);
        tv_totalSum = (TextView) findViewById(R.id.tv_totalSum);
        tv_city = (TextView) findViewById(R.id.tv_city);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);

        ly_city.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arrow_back:
                finish();
                break;
            case R.id.ly_city:
                chooseCityDialog();
                break;
            case R.id.btn_confirm:
                if (edt_company.getText().toString().equals("")){
                    ToastUtil.show(this,"请填写公司抬头");
                    return;
                }
                if (edt_name.getText().toString().equals("")){
                    ToastUtil.show(this,"请填写收件人姓名");
                    return;
                }
                if (edt_phone.getText().toString().equals("")){
                    ToastUtil.show(this,"请填写联系电话");
                    return;
                }
                if (edt_address.getText().toString().equals("")){
                    ToastUtil.show(this,"请填写详细地址");
                    return;
                }

                break;
        }
    }

    //Choose Date 选择省市县
    public void chooseCityDialog() {
        final ChooseCityUtil cityUtil = new ChooseCityUtil();
        String[] oldCityArray = tv_city.getText().toString().split(" ");
        oldCityArray[0] = oldCityArray[0].replace("省", "");
        oldCityArray[1] = oldCityArray[1].replace("市", "");
        oldCityArray[2] = oldCityArray[2].replace("区", "");
        cityUtil.createDialog(this, oldCityArray, new ChooseCityInterface() {
            @Override
            public void sure(String[] newCityArray) {
                tv_city.setText(newCityArray[0] + "省 " + newCityArray[1] + "市 " + newCityArray[2] + "区");
            }
        });
    }
    ImageButton arrow_back;
    private void initActionBar() {
        arrow_back = (ImageButton) findViewById(R.id.arrow_back);
        arrow_back.setOnClickListener(this);
    }
}
