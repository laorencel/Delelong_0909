package com.delelong.diandian.utils;

import android.app.Application;

/**
 * Created by Administrator on 2016/8/11.
 */
public class MyApp extends Application {

    private static MyApp myApp;
    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
    }
    public static MyApp getInstance(){
        return myApp;
    }
}
