package com.delelong.diandian.pace;

import android.location.Location;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/9.
 */
public class MyAMapLocation  implements Serializable {

    private String county;
    private String province;
    private String city;
    private String district;
    private String adCode;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @param county 国家
     * @param province 省
     * @param city 市
     * @param district 区
     * @param postCode 区域编码
     */
    public MyAMapLocation(String county, String province, String city, String district,String address, String postCode) {
        this.county = county;
        this.province = province;
        this.city = city;
        this.district = district;
        this.adCode = postCode;
        this.address = address;
    }
    public MyAMapLocation() {

    }

    public MyAMapLocation(Location location) {

    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    @Override
    public String toString() {
        return "MyAMapLocation{" +
                "county='" + county + '\'' +
                ", province='" + province + '\'' +
                ", com.delelong.diandian.city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", adCode='" + adCode + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
