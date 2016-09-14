package com.delelong.diandian.http;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.delelong.diandian.bean.Str;

/**
 * Created by Administrator on 2016/9/13.
 */
public class MyMessageReceiver extends BroadcastReceiver {
    private static final String TAG = "BAIDUMAPFORTEST";

    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Str.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
            String messge = intent.getStringExtra(KEY_MESSAGE);
            String extras = intent.getStringExtra(KEY_EXTRAS);
            StringBuilder showMsg = new StringBuilder();
            showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
//                if (!ExampleUtil.isEmpty(extras)) {
            showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
//                }
            Log.i(TAG, "onReceive: showMsg:" + showMsg.toString());
        }
    }
}
