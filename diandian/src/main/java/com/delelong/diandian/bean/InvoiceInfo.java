package com.delelong.diandian.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/19.
 */
public class InvoiceInfo implements Serializable {

    private String time;
    private String sum;
    private String start;
    private String end;

    public InvoiceInfo(String time, String sum, String start, String end) {
        this.time = time;
        this.sum = sum;
        this.start = start;
        this.end = end;
    }

    public InvoiceInfo() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
