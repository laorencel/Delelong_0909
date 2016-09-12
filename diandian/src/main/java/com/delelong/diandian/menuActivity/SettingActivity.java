package com.delelong.diandian.menuActivity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.delelong.diandian.BaseActivity;
import com.delelong.diandian.R;

/**
 * Created by Administrator on 2016/9/6.
 */
public class SettingActivity extends BaseActivity {

    ActionBar actionBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
        setContentView(R.layout.activity_setting);

    }

    private void initActionBar() {
        actionBar = getSupportActionBar();
        if (Build.VERSION.SDK_INT >= 21) {
            //用于去除阴影
            actionBar.setElevation(0);
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
