package com.delelong.diandian.menuActivity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delelong.diandian.BaseActivity;
import com.delelong.diandian.R;
import com.delelong.diandian.bean.Client;
import com.delelong.diandian.bean.Str;
import com.delelong.diandian.http.HttpUtils;
import com.delelong.diandian.numberPicker.ChooseCityInterface;
import com.delelong.diandian.numberPicker.ChooseCityUtil;
import com.delelong.diandian.pace.MyAMapLocation;
import com.delelong.diandian.view.RoundImageView;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2016/9/7.
 */
public class MenuModifyInfoActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "BAIDUMAPFORTEST";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_menumodifyinfo);
        initActionBar();
        initView();
        initMsg();
    }


    LinearLayout ly_modifyHead;
    RoundImageView img_head;

    EditText edt_nickName, edt_gender, edt_email;
    EditText edt_realName, edt_certificateNno;
    EditText edt_address, edt_postCode;

    LinearLayout ly_chooseFrag;

    TextView tv_city;

    private void initView() {
        //头像
        img_head = (RoundImageView) findViewById(R.id.img_head);
        img_head.setType(RoundImageView.TYPE_CIRCLE);

        img_head.setOnClickListener(this);
        //昵称 性别 email
        edt_nickName = (EditText) findViewById(R.id.edt_nickName);
        edt_gender = (EditText) findViewById(R.id.edt_gender);
        edt_email = (EditText) findViewById(R.id.edt_email);
        //真实姓名 身份证号
        edt_realName = (EditText) findViewById(R.id.edt_realName);
        edt_certificateNno = (EditText) findViewById(R.id.edt_certificateNno);
        //省 市 县
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_city.setOnClickListener(this);

        //地址 邮编
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_postCode = (EditText) findViewById(R.id.edt_postCode);

        ly_chooseFrag = (LinearLayout) findViewById(R.id.ly_chooseFrag);

    }

    ImageButton arrow_back;
    TextView tv_modify;

    private void initActionBar() {
        arrow_back = (ImageButton) findViewById(R.id.arrow_back);
        tv_modify = (TextView) findViewById(R.id.tv_modify);
        arrow_back.setOnClickListener(this);
        tv_modify.setOnClickListener(this);
    }

    FragmentManager fragmentManager;
    MyCameraDialog myCameraDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arrow_back:
                finish();
                break;
            case R.id.tv_modify:
//              提交信息到服务器
                getClientForUpDate(httpUtils);
                if (client == null) client = httpUtils.getClientByGET(Str.URL_MEMBER);
                if (httpUtils == null) httpUtils = new HttpUtils(this);

                Log.i(TAG, "onClick: "+client);
                List<String> result = httpUtils.upDateClient(Str.URL_UPDATECLIENT, client);
                if (result.get(0).equalsIgnoreCase("OK")) {
                    Toast.makeText(MenuModifyInfoActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(MenuModifyInfoActivity.this, "更新失败，请重新再试", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.img_head:
                if (myCameraDialog == null) {
                    myCameraDialog = new MyCameraDialog(this);
                }
                myCameraDialog.show();
                myCameraDialog.setCancelable(true);
                initCameraDialog();
                break;
            case R.id.dialog_camera:
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentCamera, Str.REQUESTCODECAMERA);
                myCameraDialog.hide();
                break;
            case R.id.dialog_album:
                //调用相册
                Intent intentAlbum = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentAlbum, Str.REQUESTCODEALBUM);
                myCameraDialog.hide();
                break;
            case R.id.dialog_cancel:
                myCameraDialog.hide();
                break;
            case R.id.tv_city:
                chooseCityDialog();
                break;
        }
    }

    String headPath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            String fileName = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
            Bitmap bitmap = null;
            if (requestCode == Str.REQUESTCODECAMERA) {
                //拍照
                bitmap = getCamera(data, bitmap);
            } else if (requestCode == Str.REQUESTCODEALBUM) {
                //相册（自带拍照）
                bitmap = getAlbum(data, bitmap);
            }
            img_head.setImageBitmap(bitmap);// 将图片显示在ImageView里
            //保存图片到手机
            File file = new File(Str.FILEPATH);
            file.mkdirs();// 创建文件夹
            headPath = createImage(Str.FILEPATH, fileName, bitmap);
        }
    }

    TextView dialog_camera, dialog_album;
    TextView dialog_cancel;

    private void initCameraDialog() {
        dialog_camera = (TextView) myCameraDialog.findViewById(R.id.dialog_camera);
        dialog_album = (TextView) myCameraDialog.findViewById(R.id.dialog_album);
        dialog_cancel = (TextView) myCameraDialog.findViewById(R.id.dialog_cancel);
        dialog_camera.setOnClickListener(this);
        dialog_album.setOnClickListener(this);
        dialog_cancel.setOnClickListener(this);
    }

    private void getClientForUpDate(HttpUtils httpUtils) {
        if (client == null) {
            client = httpUtils.getClientByGET(Str.URL_MEMBER);
        }
        if (headPath !=null){
            //上传头像文件
            List<String> headImage = httpUtils.upDateFile(Str.URL_UPDATEFILE, headPath);
            client.setHead_portrait(headImage.get(2));
        }

        String nickName = edt_nickName.getText().toString();
        int gender = edt_gender.getText().toString().equals("男") ? 1 : 2;
        String email = edt_email.getText().toString();
        String realName = edt_realName.getText().toString();
        String certificateNno = edt_certificateNno.getText().toString();

        String[] cityArray = tv_city.getText().toString().split(" ");
        String province = cityArray[0];
        String city = cityArray[1];
        String county = cityArray[2];
        String address = edt_address.getText().toString();
        String postCode = edt_postCode.getText().toString();

        client.setNick_name(nickName);
        client.setGender(gender);
        client.setEmail(email);
        client.setReal_name(realName);
        client.setCertificate_no(certificateNno);
        client.setProvince(province);
        client.setCity(city);
        client.setCounty(county);
        client.setAddress(address);
        client.setPost_code(postCode);
        Log.i(TAG, "getClientForUpDate: "+client);
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

    Client client;
    MyAMapLocation myAMapLocation;
    HttpUtils httpUtils;
    private void initMsg() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        myAMapLocation = (MyAMapLocation) bundle.getSerializable("myAMapLocation");
        String province_bundle, city_bundle, district_bundle, address_bundle, postCode_bundle;
        province_bundle = myAMapLocation.getProvince();
        city_bundle = myAMapLocation.getCity();
        district_bundle = myAMapLocation.getDistrict();
        address_bundle = myAMapLocation.getAddress();

        httpUtils = new HttpUtils(this);
        client = (Client) bundle.getSerializable("client");//从上级activity获取
        if (client == null){
            client = httpUtils.getClientByGET(Str.URL_MEMBER);
        }
        int level = client.getLevel();
        String phone = client.getPhone();
        String post_code = client.getPost_code();
        String urgent_name = client.getUrgent_name();
        String urgent_phone = client.getUrgent_phone();
        String nick_name = client.getNick_name();
        String certificate_type = client.getCertificate_type();
        String head_portrait = client.getHead_portrait();
        String county = client.getCounty();
        String province = client.getProvince();
        String city = client.getCity();
        String address = client.getAddress();
        String email = client.getEmail();
        int gender = client.getGender();
        String certificate_no = client.getCertificate_no();
        String real_name = client.getReal_name();

        MyHeadTask myHeadTask = new MyHeadTask(img_head);
        myHeadTask.execute(Str.URL_HEAD_PORTRAIT, head_portrait);
        edt_nickName.setText(nick_name);
        edt_gender.setText(gender == 1 ? "男" : "女");
        edt_email.setText(email);
        edt_realName.setText(real_name);
        edt_certificateNno.setText(certificate_no);
        tv_city.setText(province + " " + city + " " + county);
        if (!tv_city.getText().toString().contains("省")) {
            tv_city.setText(province_bundle + " " + city_bundle + " " + district_bundle);
        }
        if (!address.equals("")) {
            edt_address.setText(address);
        } else {
            edt_address.setText(address_bundle);
        }
        if (!post_code.equals("")) {
            edt_postCode.setText(post_code);
        }
    }

}
