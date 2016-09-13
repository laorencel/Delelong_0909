package com.delelong.diandian.http;

import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/9/13.
 */
public class HttpUtils {

    private static final String TAG = "BAIDUMAPFOTTEST";

    String token, secret;
    SharedPreferences preferences;
    String serialNumber;

    public static final String URL_LOGIN = "http://192.168.4.103:8080/Jfinal/api/login";
    public static final String APPTYPE_CLIENT = "2";
    public static final String DEVICE_TYPE = "1";
    public static String registrationId;

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
//                ToastUtil.show(, "请先连接网络");
            }
        }
        Log.i(TAG, "getHttpResult: " + stringBuilder.toString());
        return stringBuilder.toString();
    }
}
