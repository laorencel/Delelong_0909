package com.delelong.diandian;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.delelong.diandian.bean.Client;
import com.delelong.diandian.listener.MyOrientationListener;
import com.delelong.diandian.utils.ToastUtil;
import com.google.common.primitives.Ints;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BAIDUMAPFORTEST";
    public static final String URL_LOGIN = "http://192.168.4.103:8080/Jfinal/api/login";
    public static final String APPTYPE_CLIENT = "2";
    public static final String DEVICE_TYPE = "1";
    public static String registrationId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initJPush();
    }

    /**
     * 注册
     *
     * @param url_upDate url
     * @param name       用户名
     * @param pwd        密码
     * @return 注册结果
     */
    public List<String> registerApp(String url_upDate, String name, String verification, String pwd, String rePwd) {
        String upDateStr = "phone=" + name + "&code=" + verification + "&rePassword=" + rePwd + "&password=" + pwd;//注册

        getHttpMsg(url_upDate, upDateStr);
        return loginResultByJson(httpResult);
    }

    /**
     * 登录
     *
     * @param url_upDate url
     * @param name       用户名
     * @param pwd        密码
     * @return 登录结果
     */
    public List<String> loginApp(String url_upDate, String name, String pwd) {
        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        token = preferences.getString("token", null);
        secret = preferences.getString("sercet", null);
        if (registrationId == null) {
            registrationId = JPushInterface.getRegistrationID(getApplicationContext());
            if (registrationId.isEmpty()) {
                preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                registrationId = preferences.getString("registrationId", "");
            }
        }
        String upDateStr = "username=" + name + "&registrationId=" + registrationId + "&password=" + pwd;//注册

        //利用线程获取数据
        getHttpMsg(url_upDate, upDateStr);
        //json数据解析登录结果
        return loginResultByJson(httpResult);
    }

    /**
     * 注册
     *
     * @param url_upDate url
     * @param phone       用户名
     * @param pwd        密码
     * @return 注册结果
     */
    public List<String> resetPwd(String url_upDate, String phone, String verification, String pwd, String rePwd) {
        String upDateStr = "phone=" + phone + "&code=" + verification + "&rePassword=" + rePwd + "&password=" + pwd;//注册
        getHttpMsg(url_upDate, upDateStr);
        return loginResultByJson(httpResult);
    }

    String token, secret;
    SharedPreferences preferences;

    /**
     * 修改密码
     *
     * @param url_upDate url
     * @param password   旧密码
     * @param newPwd     新密码
     * @param rePwd      确认密码
     * @return 修改密码结果
     */
    public List<String> modifyPwd(String url_upDate, String password, String newPwd, String rePwd) {
        String upDateStr = "password=" + password + "&newPassword=" + newPwd + "&rePassword=" + rePwd;//修改密码

        //利用线程获取数据
        getHttpMsg(url_upDate, upDateStr);
        return loginResultByJson(httpResult);
    }

    public List<String> upDateClient(String url_upDate, Client client) {
        String upDateStr = "postCode=" + client.getPost_code()
                + "&nickName=" + client.getNick_name()
                + "&headPortrait=" + client.getHead_portrait()
                + "&county=" + client.getCounty()//县
                + "&province=" + client.getProvince()
                + "&city=" + client.getCity()
                + "&address=" + client.getAddress()
                + "&email=" + client.getEmail()
                + "&gender=" + client.getGender()
                + "&certificateNno=" + client.getCertificate_no()
                + "&realName=" + client.getReal_name();
        //利用线程获取数据
        getHttpMsg(url_upDate, upDateStr);
        return loginResultByJson(httpResult);
    }

    /**
     * @param result 从服务器获取的数据
     * @return 登录结果，是否成功登录
     */
    public List<String> loginResultByJson(String result) {
        return analysisJsonObject(result);
    }

    /**
     * @param result result
     * @return json数据解析返回String集合
     */
    public List<String> analysisJsonObject(String result) {
        JSONObject object;
        List<String> list = new ArrayList<>();
        try {
            object = new JSONObject(result);
            String status = object.getString("status");
            String msg = object.has("msg") ? object.getString("msg") : object.getString("message");
            list.add(status);
            list.add(msg);
            JSONObject data = object.getJSONObject("data");
            // data为空(注册/修改密码/获取验证码/更新会员信息)
            if (data != null) {
                if (data.has("token")){
                    //返回了token和secret
                    String token = (String) data.get("token");
                    String secret = (String) data.get("secret");
                    list.add(token);
                    list.add(secret);
                }
                else if (data.has("path")){
                    //返回了文件路径
                    String path = (String) data.get("path");
                    String name = (String) data.get("name");
                    String type = (String) data.get("type");
                    list.add(path);
                    list.add(name);
                    list.add(type);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    String httpResult = null;
    String httpResult1 = null;

    /**
     * 封装线程，简化代码
     *
     * @param url_upDate
     * @param upDateStr
     */
    public void getHttpMsg(String url_upDate, String upDateStr) {
        MyHttpResultThread myHttpResultThread = new MyHttpResultThread(url_upDate, upDateStr);
        myHttpResultThread.start();
        try {
            myHttpResultThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启线程获得服务器数据POST
     */
    class MyHttpResultThread extends Thread {
        String url_upDate, detailStr;

        MyHttpResultThread(String url_upDate, String detailStr) {
            this.url_upDate = url_upDate;
            this.detailStr = detailStr;
        }

        @Override
        public void run() {
            super.run();
            httpResult = getHttpResult(url_upDate, detailStr);
        }
    }

    String serialNumber = getSerialNumber();

    /**
     * @param url_upDate url_upDate
     * @param detailStr  detailStr
     * @return 返回服务器数据
     */
    public String getHttpResult(String url_upDate, String detailStr) {
        StringBuilder stringBuilder = null;
        try {
            URL url = new URL(url_upDate);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            //appType：请求的类型，1:表示司机端;2:表示普通会员
            connection.addRequestProperty("appType", APPTYPE_CLIENT);
            //deviceno：设备序列号
            connection.addRequestProperty("deviceno", serialNumber);
            //devicetype：设备序类型，1:android;2:ios
            connection.addRequestProperty("devicetype", DEVICE_TYPE);
            //登陆接口
            if (url_upDate.substring(url_upDate.length() - 5, url_upDate.length()).equals("login")) {

            }
            //注册接口
            else if (url_upDate.substring(url_upDate.length() - 8, url_upDate.length()).equals("register")) {

            }
            //修改密码接口
            else if (url_upDate.contains("update/password")) {
//            else if (url_upDate.substring(url_upDate.length() - 8, url_upDate.length()).equals("password")) {
                connection.addRequestProperty("token", token);
                connection.addRequestProperty("secret", secret);
            }
            //更新会员信息接口
            else if (url_upDate.contains("member/update")) {
//            else if (url_upDate.substring(url_upDate.length() - 6, url_upDate.length()).equals("update")) {
                connection.addRequestProperty("token", token);
                connection.addRequestProperty("secret", secret);
            }
            //忘记密码接口
            else if (url_upDate.contains("reset/password")) {

            }

            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(detailStr.getBytes());
            outputStream.flush();

            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), "utf-8");
            char[] chars = new char[1024];
            int len = 0;
            stringBuilder = new StringBuilder();
            while ((len = inputStreamReader.read(chars)) != -1) {
                stringBuilder.append(chars, 0, len);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            if (e.toString().contains("Network is unreachable")) {
                ToastUtil.show(this, "请先连接网络");
            }
        }
        Log.i(TAG, "getHttpResult: " + stringBuilder.toString());
        return stringBuilder.toString();
    }


    /**
     * 获取验证码
     *
     * @param url_upDate
     * @param phone
     * @return
     */
    public List<String> getHttpResultForVerification(String url_upDate, String phone, String type) {
        StringBuilder stringBuilder = null;
        String upDateStr = "phone=" + phone + "&type=" + type;
        try {
            URL url = new URL(url_upDate);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            //请求的类型，1:表示司机端;2:表示普通会员
            connection.addRequestProperty("appType", APPTYPE_CLIENT);
            connection.addRequestProperty("deviceno", serialNumber);
            connection.addRequestProperty("devicetype", DEVICE_TYPE);

            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(upDateStr.getBytes());

            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), "utf-8");
            char[] chars = new char[1024];
            int len = 0;
            stringBuilder = new StringBuilder();
            while ((len = inputStreamReader.read(chars)) != -1) {
                stringBuilder.append(chars, 0, len);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loginResultByJson(stringBuilder.toString());
    }

    ////////////////////////////////////////////////////////////获取客户信息

    /**
     * GET方法请求数据
     *
     * @param url_upDate
     * @return result
     */
    public String getHttpResultGET(String url_upDate) {
        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        token = preferences.getString("token", null);
        secret = preferences.getString("sercet", null);
        StringBuilder stringBuilder = null;
        try {
            URL url = new URL(url_upDate);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //appType：请求的类型，1:表示司机端;2:表示普通会员
            connection.addRequestProperty("appType", APPTYPE_CLIENT);
            //deviceno：设备序列号
            connection.addRequestProperty("deviceno", serialNumber);
            connection.addRequestProperty("devicetype", DEVICE_TYPE);
            connection.addRequestProperty("token", token);
            connection.addRequestProperty("secret", secret);

            InputStreamReader inputStream = new InputStreamReader(connection.getInputStream(), "utf-8");
            char[] chars = new char[1024];
            int len = 0;
            stringBuilder = new StringBuilder();
            while ((len = inputStream.read(chars)) != -1) {
                stringBuilder.append(chars, 0, len);
            }
            Log.i(TAG, "getHttpResultGET: " + stringBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i(TAG, "getHttpResultGET: MalformedURLException//" + e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "getHttpResultGET: IOException//" + e);
        }
        return stringBuilder.toString();
    }

    /**
     * 开启线程获得服务器数据GET
     */
    class MyHttpResultGETThread extends Thread {
        String url_upDate;

        MyHttpResultGETThread(String url_upDate) {
            this.url_upDate = url_upDate;
        }

        @Override
        public void run() {
            super.run();
            httpResult = getHttpResultGET(url_upDate);
        }
    }

    public void getHttpMsgByGET(String url_upDate) {
        MyHttpResultGETThread myHttpResultThread = new MyHttpResultGETThread(url_upDate);
        myHttpResultThread.start();
        try {
            myHttpResultThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户信息
     * @param url_upDate
     * @return  client
     */
    public Client getClientByGET(String url_upDate) {
        //利用线程获取数据
        getHttpMsgByGET(url_upDate);
        return getClientInfoByJson(httpResult);
    }

    /**
     * @param result httpResult
     * @return json数据解析返回Client
     */
    public Client getClientInfoByJson(String result) {
        Client client = null;
        JSONObject object;
        List<String> list = new ArrayList<>();
        try {
            object = new JSONObject(result);
            String status = object.getString("status");
            String msg = object.has("msg") ? object.getString("msg") : object.getString("message");
            list.add(status);
            list.add(msg);
            JSONObject data = object.getJSONObject("data");
            //注册、修改密码和获取验证码时 data为空
            if (data != null) {
                if (data.has("email")) {
                    //获得的是用户信息
                    client = getClientByJson(client, data, list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "getClientInfoByJson: "+e);
        }
        return client;
    }

    private Client getClientByJson(Client client, JSONObject data, List<String> list) {
        try {
//            int login_id = data.has("login_id") ? data.get("login_id") .equals("null") ? (int) data.get("login_id") : 0 : 0;
//            Log.i(TAG, "setClientByJson: login_id"+login_id);
//            int id = data.has("id") ? data.get("id") .equals("null") ? (int) data.get("id") : 0 : 0;
//            Map<String,String> a = Maps.newHashMap();
            int certificate_type = data.has("certificate_type") ? !data.getString("certificate_type") .equals("null") ? Ints.tryParse(data.getString("certificate_type")) : 1 : 1;
            int gender = data.has("gender") ? !data.getString("gender") .equals("null") ? Ints.tryParse(data.getString("gender"))  : 1 : 1;//性别(Number:1:男;2:女;)
//            String last_update_time = data.has("last_update_time") ? !data.get("last_update_time") .equals("null") ? (String) data.get("last_update_time") : "" : "";
            String phone = data.has("phone") ? !data.getString("phone") .equals("null") ? (String) data.get("phone") : "" : "";
            String post_code = data.has("post_code") ? !data.getString("post_code") .equals("null") ? (String) data.get("post_code") : "" : "";
            String city = data.has("city") ? !data.getString("city") .equals("null") ? (String) data.get("city") : "" : "";//所属城市
            String nick_name = data.has("nick_name") ? !data.getString("nick_name") .equals("null") ? (String) data.get("nick_name") : "" : "";
            String head_portrait = data.has("head_portrait") ? !data.getString("head_portrait") .equals("null") ? (String) data.get("head_portrait") : "" : "";//头像图片地址
            String urgent_phone = data.has("urgent_phone") ? !data.getString("urgent_phone") .equals("null") ? (String) data.get("urgent_phone") : "" : "";//紧急号码
            String urgent_name = data.has("urgent_name") ? !data.getString("urgent_name") .equals("null") ? (String) data.get("urgent_name") : "" : "";//紧急联系人名称
            String certificate_no = data.has("certificate_no") ? !data.getString("certificate_no") .equals("null") ? (String) data.get("certificate_no") : "" : "";//证件号
            String county = data.has("county") ? !data.getString("county") .equals("null") ? (String) data.get("county") : "" : "";//所属县
            String email = data.has("email") ? !data.getString("email") .equals("null") ? (String) data.get("email") : "" : "";//email
            String address = data.has("address") ? !data.getString("address") .equals("null") ? (String) data.get("address") : "" : "";//地址
            String real_name = data.has("real_name") ? !data.getString("real_name") .equals("null") ? (String) data.get("real_name") : "" : "";//实姓名
            String province = data.has("province") ? !data.getString("province") .equals("null") ? (String) data.get("province") : "" : "";//实姓名
            int level = data.has("level") ? !data.getString("level") .equals("null") ? Ints.tryParse( data.getString("level")) : 0 : 0;//会员等级

            client = new Client(level, phone, post_code, urgent_name, urgent_phone, nick_name,
                    certificate_type + "", head_portrait, county,
                    province, city, address, email, gender, certificate_no, real_name);
            client.setStatusList(list);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "setClientByJson: e"+e);
        }
        return client;
    }

    /**
     * 注销登陆
     * @param url_upDate
     * @return  client
     */
    public List<String> getLoginOutResultByGET(String url_upDate) {
        //利用线程获取数据
        getHttpMsgByGET(url_upDate);
        return loginResultByJson(httpResult);
    }

    //////////////////////////////////////////////////////////////////////上传文件

    /**
     * 上传文件
     * @param url_upDate
     * @param filePath 文件本地地址
     * @return
     */
    public String httpUpDateFile(String url_upDate, String filePath) {
        StringBuilder stringBuilder = null;
        String end ="\r\n";
        String twoHyphens ="--";
        String boundary ="*****";
        try {
            URL url = new URL(url_upDate);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary="+boundary);
            /* 设置DataOutputStream */
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(twoHyphens + boundary + end);
            dataOutputStream.writeBytes("Content-Disposition: form-data; "+
                    "name=\"file1\";filename=\""+
                    filePath.substring(filePath.length()-19,filePath.length()) +"\""+ end);

            dataOutputStream.writeBytes(end);

            FileInputStream fileInputStream = new FileInputStream(filePath);
            int fileLen = 0;
            byte[] buffer = new byte[1024];
            while ((fileLen = fileInputStream.read(buffer, 0, buffer.length)) != -1) {
                dataOutputStream.write(buffer, 0, buffer.length);
            }
            dataOutputStream.writeBytes(end);
            dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + end);
          /* close streams */
            fileInputStream.close();
            dataOutputStream.flush();

            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), "utf-8");
            char[] chars = new char[1024];
            int len = 0;
            stringBuilder = new StringBuilder();
            while ((len = inputStreamReader.read(chars)) != -1) {
                stringBuilder.append(chars, 0, len);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i(TAG, "httpUpDateFile: "+e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "httpUpDateFile: "+e);
        }
        return stringBuilder.toString();
    }

    public void getUpDateMsg(String url_upDate, String filePath) {
        MyHttpResultUpDateThread myHttpResultThread = new MyHttpResultUpDateThread(url_upDate, filePath);
        myHttpResultThread.start();
        try {
            myHttpResultThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启线程获得服务器数据POST
     */
    class MyHttpResultUpDateThread extends Thread {
        String url_upDate, filePath;

        MyHttpResultUpDateThread(String url_upDate, String filePath) {
            this.url_upDate = url_upDate;
            this.filePath = filePath;
        }

        @Override
        public void run() {
            super.run();
            httpResult1 = httpUpDateFile(url_upDate, filePath);
        }
    }

    /**
     * 上传文件
     * @param url_upDate
     * @param filePath 文件本地地址
     * @return
     */
    public List<String> upDateFile(String url_upDate, String filePath) {
        //利用线程获取数据
        getUpDateMsg(url_upDate, filePath);
        return loginResultByJson(httpResult1);
    }
    //////////////////////////////////////////////////////////////////////////定位
    /**
     * 定位到我的位置
     */
    /**
     * 定位到我的位置
     */
    public void centerToMyLocation(AMap aMap, AMapLocationClient mLocationClient, MyOrientationListener myOrientationListener,double myLatitude, double myLongitude) {
        if (myOrientationListener != null){
            if(!myOrientationListener.isStarted()){
                myOrientationListener.start();
            }
        }
        if (mLocationClient != null){
            if (!mLocationClient.isStarted()){
                mLocationClient.startLocation();
            }
        }
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(
                myLatitude, myLongitude)));
    }

    public <T> void intentActivityForResult(Context context, Class<T> tClass, String key, String value, String city, int requestCode) {
        Intent intent = new Intent(context, tClass);
        intent.putExtra(key, value);
        intent.putExtra("city", city);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 带bundle的界面跳转
     *
     * @param context
     * @param tClass
     * @param bundle
     * @param <T>
     */
    public <T> void intentActivityWithBundle(Context context, Class<T> tClass, Bundle bundle) {
        Intent intent = new Intent(context, tClass);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    /**
     * @return 获取手机序列号
     */
    public String getSerialNumber() {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }
    ///////////////////////////////////////////图片获取与存储
    /**
     * 把数据写入文件
     * @param filePath
     * @param fileName
     * @param bitmap
     * @return 文件路径
     */
    public String createImage(String filePath, String fileName, Bitmap bitmap) {
        String path = filePath+fileName;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);// 把数据写入文件
            Toast.makeText(this, "保存图片"+fileName, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }
    /**
     * 获取相机返回的图片
     * @param data
     * @param bitmap
     * @return Bitmap
     */
    public Bitmap getCamera(Intent data, Bitmap bitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.i(TAG, "SD card is not avaiable/writeable right now.");
            return null;
        }
        Bundle bundle = data.getExtras();
        bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
        return bitmap;
    }

    /**
     * 获取相册返回的图片
     * @param data Intent
     * @param bitmap
     * @return Bitmap
     */
    public Bitmap getAlbum(Intent data, Bitmap bitmap) {
        Uri selectedImage = data.getData();
        String[] filePathColumns = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
        String imagePath = cursor.getString(columnIndex);
        bitmap = BitmapFactory.decodeFile(imagePath);
        cursor.close();
        return bitmap;
    }
    public static class MyHeadTask extends AsyncTask<String, Void, Bitmap> {
        ImageView img_head;
        public MyHeadTask(ImageView img_head){
            this.img_head = img_head;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            URL url_head_portrait = null;
            Bitmap head = null;
            try {
                url_head_portrait = new URL(params[0] + params[1]);
                HttpURLConnection conn = (HttpURLConnection) url_head_portrait.openConnection();
                conn.setConnectTimeout(8000);
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                head = BitmapFactory.decodeStream(is);
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return head;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                img_head.setImageBitmap(bitmap);
            }
        }
    }
    /////////////////////////////////////////////////////////////////极光推送
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);
    }

    private void initJPush() {
        JPushInterface.init(this);
        registerMessageReceiver();
    }


    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
//                if (!ExampleUtil.isEmpty(extras)) {
                showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
//                }
                Log.i(TAG, "onReceive: showMsg:" + showMsg.toString());
            }
        }
    }
}
