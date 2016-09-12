package com.delelong.diandian.menuActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.delelong.diandian.R;

/**
 * Created by Administrator on 2016/9/11.
 */
public class MyCameraDialog extends Dialog {
    public MyCameraDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_choose_camera);
    }
}
