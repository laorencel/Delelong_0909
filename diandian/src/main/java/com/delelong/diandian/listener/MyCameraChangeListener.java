package com.delelong.diandian.listener;

import android.content.Context;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

/**
 * Created by Administrator on 2016/9/1.
 */
public class MyCameraChangeListener implements AMap.OnCameraChangeListener {
    private static final String TAG = "BAIDUMAPFORTEST";
    TextView textView;
    Context context;
    public MyCameraChangeListener(TextView textView, Context context) {
        this.textView = textView;
        this.context = context;
    }
    private boolean isFirstIn;
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (!isFirstIn){
            isFirstIn = !isFirstIn;
            return;
        }
        textView.setText("正在定位起点...");
    }

    LatLng centerOfMap;

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        centerOfMap = cameraPosition.target;
        geoCodeResulutListener.getLatlng(centerOfMap);
        onCameraChanged(cameraPosition);

    }

    GeocodeSearch geocodeSearch;

    private void onCameraChanged(CameraPosition cameraPosition) {
        geocodeSearch = new GeocodeSearch(context);

        geocodeSearch.getFromLocationAsyn(new RegeocodeQuery(new LatLonPoint(centerOfMap.latitude, centerOfMap.longitude), 2000, GeocodeSearch.AMAP));

        /**
         * 逆地理编码查询
         */
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                geoCodeResulutListener.getReverseGeoCodeResult(regeocodeResult);
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });

    }

    public LatLng getLatLng() {
        return centerOfMap;
    }

    //回调获取POI
    public GeoCodeResulutListener geoCodeResulutListener;

    public void getGeoCodeResultListener(GeoCodeResulutListener geoCodeResulutListener) {
        this.geoCodeResulutListener = geoCodeResulutListener;
    }

    public interface GeoCodeResulutListener {
        void getReverseGeoCodeResult(RegeocodeResult regeocodeResult);
        void getLatlng(LatLng centerOfMap);
    }
}
