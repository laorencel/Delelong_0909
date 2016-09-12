package com.delelong.diandian.bean;

/**
 * 侧边菜单栏ListViewItem对象
 * Created by Administrator on 2016/9/5.
 */
public class MenuListItem {
    private int img;
    private String tv;

    public MenuListItem(int img, String tv) {
        this.img = img;
        this.tv = tv;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTv() {
        return tv;
    }

    public void setTv(String tv) {
        this.tv = tv;
    }
}
