package com.delelong.diandian.http;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/14.
 */
public class ClientLocationInfo implements Serializable {

    private String longitude;//经度
    private String latitude;//纬度
    private String speed;//速度
    private String orientation;//方向

    public ClientLocationInfo(String longitude, String latitude, String speed, String orientation) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.speed = speed;
        this.orientation = orientation;
    }

    public ClientLocationInfo() {
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
}
