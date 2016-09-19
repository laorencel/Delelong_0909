package com.delelong.diandian.menuActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.delelong.diandian.BaseActivity;
import com.delelong.diandian.R;

/**
 * Created by Administrator on 2016/9/19.
 */
public class PaymentActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_payment);
        initActionBar();
        initView();
    }

    RelativeLayout rl_ali;
    TextView tv_default_ali;
    ImageView img_add_ali;
    private void initView() {
        rl_ali = (RelativeLayout) findViewById(R.id.rl_ali);
        tv_default_ali = (TextView) findViewById(R.id.tv_default_ali);
        img_add_ali = (ImageView) findViewById(R.id.img_add_ali);

        rl_ali.setOnClickListener(this);
    }

    ImageButton arrow_back;
    private void initActionBar() {
        arrow_back = (ImageButton) findViewById(R.id.arrow_back);
        arrow_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arrow_back:
                finish();
                break;
            case R.id.rl_ali:

                break;
        }
    }
}
