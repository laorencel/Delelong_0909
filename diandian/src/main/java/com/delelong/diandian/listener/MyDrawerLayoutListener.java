package com.delelong.diandian.listener;

import android.support.v4.widget.DrawerLayout;
import android.view.View;

/**
 * Created by Administrator on 2016/9/21.
 */
public class MyDrawerLayoutListener implements DrawerLayout.DrawerListener {

    DrawerLayout mDrawerLayout;

    public MyDrawerLayoutListener(DrawerLayout mDrawerLayout) {
        this.mDrawerLayout = mDrawerLayout;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
