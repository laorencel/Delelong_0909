package com.delelong.diandian.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lawrence on 2016/8/23.
 */
public class Client implements Serializable {

    private int level;//会员等级
    private String phone;//手机号码
    private String post_code;//邮编
    private String urgent_name;//紧急联系人名称
    private String urgent_phone;//紧急号码
    private String nick_name;//昵称
    private String certificate_type;//
    private String head_portrait;//
    private String county;//
    private String province;//
    private String city;//
    private String address;//
    private String email;//
    private int gender;//
    private String certificate_no;//
    private String real_name;//


    public Client() {

    }

    /**
     * @param level//会员等级
     * @param phone//手机号码
     * @param post_code//邮编
     * @param urgent_name//紧急联系人名称
     * @param urgent_phone//紧急号码
     * @param nick_name//昵称
     * @param certificate_type//证件类型(Number:1:身份证;)
     * @param head_portrait//头像图片地址
     * @param county//所属县
     * @param province//所属省
     * @param city//所属城市
     * @param address//地址
     * @param email//邮箱
     * @param gender//性别(Number:1:男;2:女;)
     * @param certificate_no//证件号
     * @param real_name 真实姓名
     */
    public Client(int level,String phone, String post_code, String urgent_name, String urgent_phone,
                  String nick_name, String certificate_type, String head_portrait, String county,
                  String province, String city, String address, String email, int gender, String certificate_no,String real_name) {
        this.level = level;
        this.real_name = real_name;
        this.phone = phone;
        this.post_code = post_code;
        this.urgent_name = urgent_name;
        this.urgent_phone = urgent_phone;
        this.nick_name = nick_name;
        this.certificate_type = certificate_type;
        this.head_portrait = head_portrait;
        this.county = county;
        this.province = province;
        this.city = city;
        this.address = address;
        this.email = email;
        this.gender = gender;
        this.certificate_no = certificate_no;
    }

    List<String> statusList;
    public void setStatusList(List<String> statusList){
        this.statusList = statusList;
    }
    public List<String> getStatusList(){return statusList;}

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPost_code() {
        return post_code;
    }

    public void setPost_code(String post_code) {
        this.post_code = post_code;
    }

    public String getUrgent_name() {
        return urgent_name;
    }

    public void setUrgent_name(String urgent_name) {
        this.urgent_name = urgent_name;
    }

    public String getUrgent_phone() {
        return urgent_phone;
    }

    public void setUrgent_phone(String urgent_phone) {
        this.urgent_phone = urgent_phone;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getCertificate_type() {
        return certificate_type;
    }

    public void setCertificate_type(String certificate_type) {
        this.certificate_type = certificate_type;
    }

    public String getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        this.head_portrait = head_portrait;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCertificate_no() {
        return certificate_no;
    }

    public void setCertificate_no(String certificate_no) {
        this.certificate_no = certificate_no;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    @Override
    public String toString() {
        return "Client{" +
                "level='" + level + '\'' +
                ", phone='" + phone + '\'' +
                ", post_code='" + post_code + '\'' +
                ", urgent_name='" + urgent_name + '\'' +
                ", urgent_phone='" + urgent_phone + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", certificate_type='" + certificate_type + '\'' +
                ", head_portrait='" + head_portrait + '\'' +
                ", county='" + county + '\'' +
                ", province='" + province + '\'' +
                ", com.delelong.diandian.city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", certificate_no='" + certificate_no + '\'' +
                ", real_name='" + real_name + '\'' +
                ", statusList=" + statusList +
                '}';
    }
}
