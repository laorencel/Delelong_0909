package com.delelong.diandian.listener;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Administrator on 2016/8/11.
 */
public class MyOrientationListener implements SensorEventListener {

    private SensorManager mSensorManager = null;
    private Sensor mSensor;
    private Context mContext;

    private float lastX;

    public MyOrientationListener(Context context){
        mContext = context;
    }

    public void start(){
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager != null){
            //获得方向传感器
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
        if (mSensor != null){
            mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_UI);
        }
    }
    public void stop(){
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION){
            float x = event.values[SensorManager.DATA_X];
            if (Math.abs(x - lastX) > 1.0){
                if (mOnOritationListener != null){
                    mOnOritationListener.onOritationChanged(x);
                }
            }
            lastX = x;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private OnOritationListener mOnOritationListener;

    public void setmOnOritationListener(OnOritationListener mOnOritationListener) {
        this.mOnOritationListener = mOnOritationListener;
    }

    public interface OnOritationListener{
        void onOritationChanged(float x);
    }
}
