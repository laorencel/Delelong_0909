package com.delelong.diandian.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/18.
 */
public class Amount implements Serializable {

    private double totalAmount;//总计费用
    private double baseAmount;//起步费用
    private double distanceAmount;//里程费用
    private double nightAmount;//夜间服务费
    private double peakAmount;//低速费
    private double superDistanceAmount;//空驶费
    private double cancelAmount;//取消费
}
