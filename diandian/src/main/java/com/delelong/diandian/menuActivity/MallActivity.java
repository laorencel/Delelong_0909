package com.delelong.diandian.menuActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.delelong.diandian.BaseActivity;
import com.delelong.diandian.R;
import com.delelong.diandian.bean.Str;
import com.delelong.diandian.utils.SystemUtils;
import com.delelong.diandian.webchromeclient.DefaultWebChromeClient;
import com.delelong.diandian.webchromeclient.WebChromeClientAboveFive;

/**
 * Created by Administrator on 2016/9/12.
 */
public class MallActivity extends BaseActivity {


    private static final String TAG = "BAIDUMAPFORTEST";
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

        webView.loadUrl(Str.URL_LIANCHENG);
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
        String version = SystemUtils.getSystemVersion();
        if (version.startsWith("5")){
            webView.setWebChromeClient(new WebChromeClientAboveFive(this) {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress == 100) {
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(newProgress);
                    }
                    super.onProgressChanged(view, newProgress);
                }
                @Override
                public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, android.webkit.WebChromeClient.FileChooserParams fileChooserParams) {
                    // TODO Auto-generated method stub
                    return super.onShowFileChooser( webView,  valueCallback,  fileChooserParams);
                }
                @Override
                public void onActivityResult(int resultCode, Intent data) {
                    super.onActivityResult(resultCode,data);
                }

            });
        }else {
            webView.setWebChromeClient(new DefaultWebChromeClient(this) {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress == 100) {
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(newProgress);
                    }
                    super.onProgressChanged(view, newProgress);
                }
                @Override
                public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                    // TODO Auto-generated method stub
                    super.openFileChooser( uploadMsg);
                }
                @Override
                public void onActivityResult(int resultCode, Intent data) {
                    super.onActivityResult(resultCode,data);
                }
            });
        }
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
