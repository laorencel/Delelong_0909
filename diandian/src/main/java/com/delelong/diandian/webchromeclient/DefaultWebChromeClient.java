package com.delelong.diandian.webchromeclient;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.ValueCallback;

/**
 * Created by Administrator on 2016/9/20.
 */
public class DefaultWebChromeClient extends BaseWebChromeClient {

    private ValueCallback<Uri> mUploadMessage;
    private Activity mActivity;

    public DefaultWebChromeClient(Activity activity) {
        this.mActivity = activity;
    }

    // For Android < 3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        openFileChooser(mUploadMessage, "", "");
    }

    // For Android 3.0+
    public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
        openFileChooser(mUploadMessage, acceptType, "");
    }

    //For Android 4.1
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        mActivity.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
    }

    @Override
    public void onActivityResult(int resultCode, Intent data) {
        if (null == mUploadMessage) {
            return;
        }
        Uri result = data == null || resultCode != Activity.RESULT_OK ? null
                : data.getData();
        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }
}