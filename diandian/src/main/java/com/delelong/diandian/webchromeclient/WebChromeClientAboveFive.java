package com.delelong.diandian.webchromeclient;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.webkit.ValueCallback;
import android.webkit.WebView;

/**
 * Created by Administrator on 2016/9/20.
 */
public class WebChromeClientAboveFive extends BaseWebChromeClient {

    private ValueCallback<Uri[]> mUploadCallbackAboveFive;
    private Activity mActivity;

    public WebChromeClientAboveFive(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 兼容5.0及以上
     *
     * @param webView
     * @param valueCallback
     * @param fileChooserParams
     * @return
     */
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, android.webkit.WebChromeClient.FileChooserParams fileChooserParams) {
        mUploadCallbackAboveFive = valueCallback;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        mActivity.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int resultCode, Intent data) {
        if (null == mUploadCallbackAboveFive) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    int itemCount = clipData.getItemCount();
                    results = new Uri[itemCount];
                    for (int i = 0; i < itemCount; i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }
        mUploadCallbackAboveFive.onReceiveValue(results);
        mUploadCallbackAboveFive = null;
        return;
    }
}
