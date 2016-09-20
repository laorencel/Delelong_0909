package com.delelong.diandian.webchromeclient;

import android.content.Intent;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by Administrator on 2016/9/20.
 */
public class BaseWebChromeClient extends WebChromeClient {

    public static final int FILECHOOSER_RESULTCODE = 10000;

    // For Android < 3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {

    }

    // For Android 3.0+
    public void openFileChooser(ValueCallback uploadMsg, String acceptType) {

    }

    //For Android 4.1
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {

    }
    //For Android 5.0+
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
        return true;
    }

    public void onActivityResult(int resultCode, Intent data) {

    }
}
