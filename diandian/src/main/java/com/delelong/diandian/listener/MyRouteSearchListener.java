package com.delelong.diandian.listener;

import android.content.Context;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.delelong.diandian.R;
import com.delelong.diandian.route.DriveRouteColorfulOverLay;
import com.delelong.diandian.utils.AMapUtil;
import com.delelong.diandian.utils.ToastUtil;

/**
 * Created by Administrator on 2016/9/4.
 */
public class MyRouteSearchListener implements RouteSearch.OnRouteSearchListener {

    private static final String TAG = "BAIDUMAPFORTEST";
    AMap aMap;
    Context context;
    public MyRouteSearchListener(AMap aMap,Context context){
        this.aMap = aMap;

    }
    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int errorCode) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == 1000) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                if (driveRouteResult.getPaths().size() > 0) {
                    driveRouteResult = driveRouteResult;
                    final DrivePath drivePath = driveRouteResult.getPaths()
                            .get(0);
                    myDrivePathListener.getDrivePath(drivePath);
                    DriveRouteColorfulOverLay drivingRouteOverlay = new DriveRouteColorfulOverLay(
                            aMap, drivePath,
                            driveRouteResult.getStartPos(),
                            driveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    int dis = (int) drivePath.getDistance();//行驶里程
                    int dur = (int) drivePath.getDuration();//行驶时间
                    String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
                    int taxiCost = (int) driveRouteResult.getTaxiCost();
                    Log.i(TAG, "onDriveRouteSearched: " + des + "//" + taxiCost);

                } else if (driveRouteResult != null && driveRouteResult.getPaths() == null) {
                    ToastUtil.show(context, context.getString(R.string.no_result));
                }
            } else {
                ToastUtil.show(context, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(context, errorCode);
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int errorCode) {

    }

    private MyDrivePathListener myDrivePathListener;
    public void getDrivePathListener(MyDrivePathListener myDrivePathListener){
        this.myDrivePathListener = myDrivePathListener;
    }
    public interface MyDrivePathListener{
        void getDrivePath(DrivePath drivePath);
    }
}
