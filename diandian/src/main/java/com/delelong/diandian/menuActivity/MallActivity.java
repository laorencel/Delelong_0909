package com.delelong.diandian.menuActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.delelong.diandian.BaseActivity;
import com.delelong.diandian.R;

/**
 * Created by Administrator on 2016/9/12.
 */
public class MallActivity extends BaseActivity {

    private static final String URL_LIANCHENG = "http://www.52liancheng.com";
    private static final String TAG = "BAIDUMAPFORTEST";
    private ProgressDialog dialog;
    WebView webView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_menu_mall);
        initView();
        setUpWeb();
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webView = (WebView) findViewById(R.id.web);
    }

    private void setUpWeb() {

        webView.loadUrl(URL_LIANCHENG);
        //设置使用webview打开网页,而不是使用默认浏览器打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


        WebSettings webSettings = webView.getSettings();
        //设置支持JavaScript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//支持网页缓存

        //设置显示进度
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.i(TAG, "onProgressChanged: ");
                if (newProgress == 100) {
//                    closeDialog();
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
//                    openDialog(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

//            private void openDialog(int newProgress) {
//                Log.i(TAG, "openDialog: ");
//                if (dialog == null) {
//                    dialog = new ProgressDialog(getApplicationContext());
////                    dialog.setTitle("加载中...");
//                    dialog.setProgress(newProgress);
////                    Drawable drawable = getResources().getDrawable(R.drawable.car);
////                    dialog.setProgressDrawable(drawable);
//                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                    dialog.show();
//                } else {
//                    dialog.setProgress(newProgress);
//                }
//            }
//
//            private void closeDialog() {
//                Log.i(TAG, "closeDialog: ");
//                if (dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
//                    dialog = null;
//                }
//            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    //设置返回键,使得程序返回上一级网页而不是退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView != null) {
                Log.i(TAG, "onKeyDown: ");
                Toast.makeText(this, webView.getUrl(), Toast.LENGTH_SHORT).show();
                if (webView.canGoBack()) {
                    webView.goBack();
                    return true;
                } else {
                    finish();//退出程序
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
