package com.delelong.diandian.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/14.
 */
public class CarInfo implements Serializable {

    private String phone;//司机手机号码
    private String nick_name;//司机称呼
    private String plate_no;//车牌号
    private double orientation;//方向
    private double id;//司机主键
    private double latitude;//纬度
    private double longitude;//经度
    private double speed;//速度

    /**
     *
     * @param phone 司机手机号码
     * @param nick_name 司机称呼
     * @param plate_no 车牌号
     * @param orientation 方向
     * @param id 司机主键
     * @param latitude 纬度
     * @param longitude 经度
     * @param speed 速度
     */
    public CarInfo(String phone, String nick_name, String plate_no, double orientation, double id, double latitude, double longitude, double speed) {
        this.phone = phone;
        this.nick_name = nick_name;
        this.plate_no = plate_no;
        this.orientation = orientation;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
    }

    public CarInfo() {
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

    public String getPlate_no() {
        return plate_no;
    }

    public void setPlate_no(String plate_no) {
        this.plate_no = plate_no;
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

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
