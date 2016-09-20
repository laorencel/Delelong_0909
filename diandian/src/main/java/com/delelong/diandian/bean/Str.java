package com.delelong.diandian.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/14.
 */
public class Str implements Serializable {


//    private static final String URL_API = "http://121.40.142.141:8090/Jfinal/api";
    private static final String URL_API = "http://192.168.4.110:8080/Jfinal/api";

//    public static final String URL_UPDATEFILE = "http://121.40.142.141:8090/Jfinal/file";//上传文件
    public static final String URL_UPDATEFILE = "http://192.168.4.110:8080/Jfinal/file";//上传文件
//    public static final String URL_HEAD_PORTRAIT = "http://121.40.142.141:8090/Jfinal/";//图片头地址
    public static final String URL_HEAD_PORTRAIT = "http://192.168.4.110:8080/Jfinal/";//图片头地址

    public static final String URL_LOGIN = URL_API+"/login";
    public static final String URL_FORGOT = URL_API+"/member/reset/password";
    public static final String URL_SMSCODE = URL_API+"/smscode";//1:注册;2:忘记密码;3:更换手机号;

    public static final String URL_MEMBER = URL_API+"/member";//获取会员信息
    public static final String URL_LOGINOUT = URL_API+"/logout";//注销登陆

    public static final String URL_MODIFY = URL_API+"/update/password";//修改密码（记得密码）
    public static final String URL_REGISTER = URL_API+"/register";//注册
    public static final String URL_UPDATECLIENT = URL_API+"/member/update";//更新会员信息（GET）
    public static final String URL_GETCARINFO = URL_API+"/member/cars";//获取车辆列表
    public static final String URL_GETDRIVERINFO = URL_API+"/member/drivers";//获取代驾司机列表
    public static final String FILEPATH = "/sdcard/DianDian/Image/";//文件本地地址
    public static final String URL_UPDATELOCATION = URL_API+"/member/update/location";

    /**
     * 验证码类型(1:注册;2:忘记密码;3:更换手机号;)
     */
    public static final String VERIFICATION_TYPE_REGISTER = "1";
    public static final String VERIFICATION_TYPE_RESET = "2";
    public static final String VERIFICATION_TYPE_CHANGEPHONE = "3";
    /**
     * 请求码
     */
    public static final int RESULTNOCHOICECODE = 0;
    public static final int REQUESTCODECAMERA = 1;//相机requestCode
    public static final int REQUESTCODEALBUM = 2;//相册requestCode
    public static final int REQUESTHOMECODE = 3;//设置家庭地址requestCode
    public static final int REQUESTCOMPANYCODE = 4;//设置公司地址相册requestCode
    public static final int REQUESTPOSITIONCODE = 5;//设置起点requestCode
    public static final int REQUESTDESTINATIONCODE = 6;//设置终点requestCode

    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";//receiver服务
    public static final String URL_LIANCHENG = "http://www.52liancheng.com";//链城网
//    public static final String URL_DRIVERENROLL = "http://192.168.4.110:8080/Jfinal/driver/enroll";//司机报名页面
}
