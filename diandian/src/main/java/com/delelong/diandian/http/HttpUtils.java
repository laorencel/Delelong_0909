package com.delelong.diandian.http;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.delelong.diandian.LoginActivity;
import com.delelong.diandian.bean.CarInfo;
import com.delelong.diandian.bean.Client;
import com.delelong.diandian.bean.DriverInfo;
import com.delelong.diandian.utils.ToastUtil;
import com.google.common.primitives.Ints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/9/13.
 */
public class HttpUtils {

    private final String TAG = "BAIDUMAPFOTTEST";

    private final String APPTYPE_CLIENT = "2";
    private final String DEVICE_TYPE = "1";
    private Context context;

    private SharedPreferences preferences;
    private String token;
    private String secret;
    private String registrationId;
    private String serialNumber = getSerialNumber();
    private String httpResult;

    public HttpUtils(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        token = preferences.getString("token", null);
        secret = preferences.getString("sercet", null);
        registrationId = JPushInterface.getRegistrationID(context);
        Log.i(TAG, "HttpUtils:registrationId: "+registrationId);
    }

//    //
//    private MyMessageReceiver mMessageReceiver;
//    private final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
//    private void registerMessageReceiver() {
//        mMessageReceiver = new MyMessageReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
//        filter.addAction(MESSAGE_RECEIVED_ACTION);
//        context.registerReceiver(mMessageReceiver, filter);
//    }
//    private  void initJPush(){
//        JPushInterface.init(context);
//        registerMessageReceiver();
//    }


    /**
     * @return 获取手机序列号
     */
    private String getSerialNumber() {
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

    ////////////////////////////////////////////////////////////////POST

    /**
     * POST访问网络
     *
     * @param url_upDate
     * @param detailStr
     * @return
     */
    private String connectPOSTHttp(String url_upDate, String detailStr) {
        StringBuilder stringBuilder = null;
        OutputStream outputStream = null;
        HttpURLConnection connection = null;
        InputStreamReader inputStreamReader = null;
        try {
            URL url = new URL(url_upDate);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            //appType：请求的类型，1:表示司机端;2:表示普通会员
            connection.addRequestProperty("appType", APPTYPE_CLIENT);
            //deviceno：设备序列号
            connection.addRequestProperty("deviceno", serialNumber);
            //devicetype：设备序类型，1:android;2:ios
            connection.addRequestProperty("devicetype", DEVICE_TYPE);
            //登陆接口
            if (url_upDate.contains("/api/login")) {

            }
            //注册接口
            else if (url_upDate.contains("/api/register")) {

            }
            //修改密码接口
            else if (url_upDate.contains("/api/member/update/password")) {
                connection.addRequestProperty("token", token);
                connection.addRequestProperty("secret", secret);
            }
            //更新会员信息接口
            else if (url_upDate.contains("/api/member/update")) {
                connection.addRequestProperty("token", token);
                connection.addRequestProperty("secret", secret);
            }
            //忘记密码接口
            else if (url_upDate.contains("/api/member/reset/password")) {

            }
            //更新会员位置接口
            else if (url_upDate.contains("/api/member/update/location")) {
                connection.addRequestProperty("token", token);
                connection.addRequestProperty("secret", secret);
            }
            //获取车辆列表
            else if (url_upDate.contains("/api/member/cars")) {
                connection.addRequestProperty("token", token);
                connection.addRequestProperty("secret", secret);
            }
            //获取代驾司机列表
            else if (url_upDate.contains("/api/member/drivers")) {
                connection.addRequestProperty("token", token);
                connection.addRequestProperty("secret", secret);
            }

            connection.setDoOutput(true);
            outputStream = connection.getOutputStream();
            outputStream.write(detailStr.getBytes());
            outputStream.flush();

            inputStreamReader = new InputStreamReader(connection.getInputStream(), "utf-8");
            char[] chars = new char[1024];
            int len = 0;
            stringBuilder = new StringBuilder();
            while ((len = inputStreamReader.read(chars)) != -1) {
                stringBuilder.append(chars, 0, len);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i(TAG, "connectPOSTHttp: MalformedURLException e"+e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "connectPOSTHttp: IOException e"+e);
            ToastUtil.show(context,"连接错误，请稍后重试");
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG, "IOException: e"+e);
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG, "IOException: e"+e);
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        Log.i(TAG, "connectPOSTHttp: " + stringBuilder.toString());
        return stringBuilder.toString();
    }

    /**
     * 开启线程获得服务器数据POST
     */
    private class MyHttpResultThread extends Thread {
        String url_upDate, detailStr;

        MyHttpResultThread(String url_upDate, String detailStr) {
            this.url_upDate = url_upDate;
            this.detailStr = detailStr;
        }

        @Override
        public void run() {
            super.run();
            httpResult = connectPOSTHttp(url_upDate, detailStr);
        }
    }

    /**
     * 封装线程，简化代码
     *
     * @param url_upDate
     * @param upDateStr
     */
    private void getHttpMsgByPOST(String url_upDate, String upDateStr) {
        MyHttpResultThread myHttpResultThread = new MyHttpResultThread(url_upDate, upDateStr);
        myHttpResultThread.start();
        try {
            myHttpResultThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param result result
     * @return json数据解析返回String集合
     */
    private List<String> resultByJson(String result) {
        JSONObject object;
        List<String> list = new ArrayList<>();
        try {
            object = new JSONObject(result);
            String status = object.getString("status");
            String msg = object.has("msg") ? object.getString("msg") : object.getString("message");
            if (msg.equals("未登陆")){
                toLogin();
            }
            list.add(status);
            list.add(msg);
            JSONObject data = object.getJSONObject("data");
            // data为空(注册/修改密码/获取验证码/更新会员信息)
            if (data != null) {
                if (data.has("token")) {
                    //返回了token和secret
                    String token = (String) data.get("token");
                    String secret = (String) data.get("secret");
                    list.add(token);
                    list.add(secret);
                } else if (data.has("path")) {
                    //返回了文件路径
                    String path = (String) data.get("path");
                    String name = (String) data.get("name");
                    String type = (String) data.get("type");
                    list.add(path);
                    list.add(name);
                    list.add(type);
                } else if (data.has("plate_no")){
                    //车辆列表

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void toLogin() {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    private class MyUpDateLocationTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... infos) {
            String httpResult = connectPOSTHttp(infos[0], infos[1]);
            resultList = resultByJson(httpResult);
            return null;
        }
    }


    private List<CarInfo> carInfoByJson(String result) {
        JSONObject object;
        List<CarInfo> carInfos = new ArrayList<>();
        List<String> list = new ArrayList<>();
        try {
            object = new JSONObject(result);
            String status = object.getString("status");
            String msg = object.has("msg") ? object.getString("msg") : object.getString("message");
            if (msg.equals("未登陆")){
                toLogin();
            }
            list.add(status);
            list.add(msg);
            JSONArray data = object.getJSONArray("data");
            // data为空
            if (data != null) {
                for (int i = 0; i < data.length(); i++) {
                    JSONObject object1 = data.getJSONObject(i);
                    CarInfo info = new CarInfo(object1.getString("phone"),
                            object1.getString("nick_name"),
                            object1.getString("plate_no"),
                            object1.getDouble("orientation"),
                            object1.getDouble("id"),
                            object1.getDouble("latitude"),
                            object1.getDouble("longitude"),
                            object1.getDouble("speed"));
                    carInfos.add(info);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return carInfos;
    }

    /**
     * 获取car列表线程
     */
    private class CarInfoThread extends Thread {
        String url_upDate, detailStr;
        CarInfoThread(String url_upDate, String detailStr) {
            this.url_upDate = url_upDate;
            this.detailStr = detailStr;
        }
        @Override
        public void run() {
            super.run();
            String httpResult = connectPOSTHttp(url_upDate, detailStr);
            carInfos = carInfoByJson(httpResult);
        }
    }

    private List<DriverInfo> driverInfoByJson(String result) {
        JSONObject object;
        List<DriverInfo> driverInfos = new ArrayList<>();
        List<String> list = new ArrayList<>();
        try {
            object = new JSONObject(result);
            String status = object.getString("status");
            String msg = object.has("msg") ? object.getString("msg") : object.getString("message");
            if (msg.equals("未登陆")){
                toLogin();
            }
            list.add(status);
            list.add(msg);
            JSONArray data = object.getJSONArray("data");
            // data为空
            if (data != null) {
                for (int i = 0; i < data.length(); i++) {
                    JSONObject object1 = data.getJSONObject(i);
                    DriverInfo info = new DriverInfo(
                            object1.getString("phone"),
                            object1.getString("nick_name"),
                            object1.getDouble("orientation"),
                            object1.getDouble("id"),
                            object1.getDouble("latitude"),
                            object1.getDouble("longitude"));
                    driverInfos.add(info);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return driverInfos;
    }

    private class DriverInfoThread extends Thread {
        String url_upDate, detailStr;
        DriverInfoThread(String url_upDate, String detailStr) {
            this.url_upDate = url_upDate;
            this.detailStr = detailStr;
        }
        @Override
        public void run() {
            super.run();
            String httpResult = connectPOSTHttp(url_upDate, detailStr);
            driverInfos = driverInfoByJson(httpResult);
        }
    }
    ////////////////////////////////////////////////////////使用POST方法
    private List<DriverInfo> driverInfos;
    /**
     * 获取代驾司机列表
     * @param url_upDate
     * @param leftTop
     * @param rightBottom
     * @return
     */
    public List<DriverInfo> getDriverInfos(String url_upDate, LatLng leftTop,LatLng rightBottom){
        String upDateStr = "a=" + leftTop.latitude + "&b=" + leftTop.longitude
                + "&c=" + rightBottom.latitude + "&d=" + rightBottom.longitude;//位置信息

        DriverInfoThread infoThread = new DriverInfoThread(url_upDate,upDateStr);
        infoThread.start();
        try {
            infoThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return driverInfos;
    }

    private List<CarInfo> carInfos;
    /**
     * 获取车辆列表
     * @param url_upDate
     * @param leftTop
     * @param rightBottom
     * @return List<CarInfo>
     */
    public List<CarInfo> getCarInfos(String url_upDate, LatLng leftTop,LatLng rightBottom){
        String upDateStr = "a=" + leftTop.latitude + "&b=" + leftTop.longitude
                + "&c=" + rightBottom.latitude + "&d=" + rightBottom.longitude;//位置信息

        CarInfoThread infoThread = new CarInfoThread(url_upDate,upDateStr);
        infoThread.start();
        try {
            infoThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return carInfos;
    }

    private List<String> resultList;
    /**
     * 更新会员位置
     * @param url_upDate
     * @param locationInfo 位置信息
     * @return
     */
    public List<String> upDateLocation(String url_upDate, ClientLocationInfo locationInfo) {
        String upDateStr = "longitude=" + locationInfo.getLongitude() + "&latitude=" + locationInfo.getLatitude()
                + "&speed=" + locationInfo.getSpeed() + "&orientation=" + locationInfo.getOrientation();//位置信息

        MyUpDateLocationTask locationTask = new MyUpDateLocationTask();
        locationTask.execute(url_upDate,upDateStr);
        return resultList;
    }

    /**
     * 登录
     *
     * @param url_upDate url
     * @param name       用户名
     * @param pwd        密码
     * @return 登录结果
     */
    public List<String> login(String url_upDate, String name, String pwd) {
        if (registrationId == null) {
//            initJPush();
        }
        if (registrationId == null) {
            registrationId = JPushInterface.getRegistrationID(context);
            if (registrationId != null) {
                if (registrationId.isEmpty()) {
                    registrationId = preferences.getString("registrationId", "");
                }
            }
        }
        String upDateStr = "username=" + name + "&registrationId=" + registrationId + "&password=" + pwd;//注册
        //利用线程获取数据
        getHttpMsgByPOST(url_upDate, upDateStr);
        //json数据解析登录结果
        return resultByJson(httpResult);
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

        getHttpMsgByPOST(url_upDate, upDateStr);
        return resultByJson(httpResult);
    }

    /**
     * 修改密码(忘记密码)
     *
     * @param url_upDate url
     * @param phone      用户名
     * @param pwd        密码
     * @return 注册结果
     */
    public List<String> resetPwd(String url_upDate, String phone, String verification, String pwd, String rePwd) {
        String upDateStr = "phone=" + phone + "&code=" + verification + "&rePassword=" + rePwd + "&password=" + pwd;//注册
        getHttpMsgByPOST(url_upDate, upDateStr);
        return resultByJson(httpResult);
    }

    /**
     * 修改密码（记得密码）
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
        getHttpMsgByPOST(url_upDate, upDateStr);
        return resultByJson(httpResult);
    }

    /**
     * 获取验证码
     *
     * @param url_upDate
     * @param phone
     * @param type
     * @return
     */
    public List<String> getVerification(String url_upDate, String phone, String type) {
        String upDateStr = "?phone=" + phone + "&type=" + type;
        //利用线程获取数据
//        getHttpMsgByPOST(url_upDate, upDateStr);
        getHttpMsgByGET(url_upDate+upDateStr);
        return resultByJson(httpResult);
    }


    /**
     * 更新客户信息
     *
     * @param url_upDate
     * @param client
     * @return
     */
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
                + "&certificateNo=" + client.getCertificate_no()
                + "&realName=" + client.getReal_name();
        Log.i(TAG, "upDateClient: "+client.getPost_code());
        //利用线程获取数据
        getHttpMsgByPOST(url_upDate, upDateStr);
        return resultByJson(httpResult);
    }

    //////////////////////////////////////////////////////////////////////GET

    /**
     * GET方法请求数据
     *
     * @param url_upDate
     * @return result
     */
    private String connectGETHttp(String url_upDate) {
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
    private class MyHttpResultGETThread extends Thread {
        String url_upDate;

        MyHttpResultGETThread(String url_upDate) {
            this.url_upDate = url_upDate;
        }

        @Override
        public void run() {
            super.run();
            httpResult = connectGETHttp(url_upDate);
        }
    }

    private void getHttpMsgByGET(String url_upDate) {
        MyHttpResultGETThread myHttpResultThread = new MyHttpResultGETThread(url_upDate);
        myHttpResultThread.start();
        try {
            myHttpResultThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param result httpResult
     * @return json数据解析返回Client
     */
    private Client getClientInfoByJson(String result) {
        Client client = null;
        JSONObject object;
        List<String> list = new ArrayList<>();
        try {
            object = new JSONObject(result);
            String status = object.getString("status");
            String msg = object.has("msg") ? object.getString("msg") : object.getString("message");
            if (msg.equals("未登陆")){
                toLogin();
            }
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
            Log.i(TAG, "getClientInfoByJson: " + e);
        }
        return client;
    }
    //////////////////////////////////使用GET方法

    /**
     * 获取用户信息   GET
     *
     * @param url_upDate
     * @return client
     */
    public Client getClientByGET(String url_upDate) {
        //利用线程获取数据
        getHttpMsgByGET(url_upDate);
        return getClientInfoByJson(httpResult);
    }

    /**
     * 注销登陆 GET
     *
     * @param url_upDate
     * @return client
     */
    public List<String> getLoginOutResultByGET(String url_upDate) {
        //利用线程获取数据
        getHttpMsgByGET(url_upDate);
        return resultByJson(httpResult);
    }

    //////////////////////////////////////////////////////////////////////POST上传文件

    /**
     * 上传文件 POST
     *
     * @param url_upDate
     * @param filePath   文件本地地址
     * @return
     */
    public String httpUpDateFile(String url_upDate, String filePath) {
        StringBuilder stringBuilder = null;
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(url_upDate);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            /* 设置DataOutputStream */
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(twoHyphens + boundary + end);
            dataOutputStream.writeBytes("Content-Disposition: form-data; " +
                    "name=\"file1\";filename=\"" +
                    filePath.substring(filePath.length() - 19, filePath.length()) + "\"" + end);

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
            Log.i(TAG, "httpUpDateFile: "+stringBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i(TAG, "httpUpDateFile: " + e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "httpUpDateFile: " + e);
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
    private class MyHttpResultUpDateThread extends Thread {
        String url_upDate, filePath;

        MyHttpResultUpDateThread(String url_upDate, String filePath) {
            this.url_upDate = url_upDate;
            this.filePath = filePath;
        }

        @Override
        public void run() {
            super.run();
            httpResult = httpUpDateFile(url_upDate, filePath);
        }
    }

    ////////////////////////////////////////////////使用POST上传方法

    /**
     * 上传文件
     *
     * @param url_upDate
     * @param filePath   文件本地地址
     * @return
     */
    public List<String> upDateFile(String url_upDate, String filePath) {
        //利用线程获取数据
        getUpDateMsg(url_upDate, filePath);
        return resultByJson(httpResult);
    }

    /////////////////////////////////////////////////////////解析获得Client
    private Client getClientByJson(Client client, JSONObject data, List<String> list) {
        try {
//            int login_id = data.has("login_id") ? data.get("login_id") .equals("null") ? (int) data.get("login_id") : 0 : 0;
//            Log.i(TAG, "setClientByJson: login_id"+login_id);
//            int id = data.has("id") ? data.get("id") .equals("null") ? (int) data.get("id") : 0 : 0;
//            Map<String,String> a = Maps.newHashMap();
            int certificate_type = data.has("certificate_type") ? !data.getString("certificate_type").equals("null") ? Ints.tryParse(data.getString("certificate_type")) : 1 : 1;
            int gender = data.has("gender") ? !data.getString("gender").equals("null") ? Ints.tryParse(data.getString("gender")) : 1 : 1;//性别(Number:1:男;2:女;)
//            String last_update_time = data.has("last_update_time") ? !data.get("last_update_time") .equals("null") ? (String) data.get("last_update_time") : "" : "";
            String phone = data.has("phone") ? !data.getString("phone").equals("null") ? (String) data.get("phone") : "" : "";
            String post_code = data.has("post_code") ? !data.getString("post_code").equals("null") ? (String) data.get("post_code") : "" : "";
            String city = data.has("city") ? !data.getString("city").equals("null") ? (String) data.get("city") : "" : "";//所属城市
            String nick_name = data.has("nick_name") ? !data.getString("nick_name").equals("null") ? (String) data.get("nick_name") : "" : "";
            String head_portrait = data.has("head_portrait") ? !data.getString("head_portrait").equals("null") ? (String) data.get("head_portrait") : "" : "";//头像图片地址
            String urgent_phone = data.has("urgent_phone") ? !data.getString("urgent_phone").equals("null") ? (String) data.get("urgent_phone") : "" : "";//紧急号码
            String urgent_name = data.has("urgent_name") ? !data.getString("urgent_name").equals("null") ? (String) data.get("urgent_name") : "" : "";//紧急联系人名称
            String certificate_no = data.has("certificate_no") ? !data.getString("certificate_no").equals("null") ? (String) data.get("certificate_no") : "" : "";//证件号
            String county = data.has("county") ? !data.getString("county").equals("null") ? (String) data.get("county") : "" : "";//所属县
            String email = data.has("email") ? !data.getString("email").equals("null") ? (String) data.get("email") : "" : "";//email
            String address = data.has("address") ? !data.getString("address").equals("null") ? (String) data.get("address") : "" : "";//地址
            String real_name = data.has("real_name") ? !data.getString("real_name").equals("null") ? (String) data.get("real_name") : "" : "";//实姓名
            String province = data.has("province") ? !data.getString("province").equals("null") ? (String) data.get("province") : "" : "";//实姓名
            int level = data.has("level") ? !data.getString("level").equals("null") ? Ints.tryParse(data.getString("level")) : 0 : 0;//会员等级

            client = new Client(level, phone, post_code, urgent_name, urgent_phone, nick_name,
                    certificate_type + "", head_portrait, county,
                    province, city, address, email, gender, certificate_no, real_name);
            client.setStatusList(list);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "setClientByJson: e" + e);
        }
        return client;
    }

}
