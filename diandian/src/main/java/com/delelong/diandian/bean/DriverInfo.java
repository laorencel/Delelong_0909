package com.delelong.diandian.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/23.
 */
public class DriverInfo implements Serializable {
    private String phone;//司机手机号码
    private String nick_name;//司机称呼
    private double orientation;//方向
    private double id;//司机主键
    private double latitude;//纬度
    private double longitude;//经度

    public DriverInfo(String phone, String nick_name, double orientation, double id, double latitude, double longitude) {
        this.phone = phone;
        this.nick_name = nick_name;
        this.orientation = orientation;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public DriverInfo() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public double getOrientation() {
        return orientation;
    }

    public void setOrientation(double orientation) {
        this.orientation = orientation;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
