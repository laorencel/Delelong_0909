package com.delelong.diandian.menuActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.delelong.diandian.BaseActivity;
import com.delelong.diandian.R;
import com.delelong.diandian.bean.Client;
import com.delelong.diandian.bean.InvoiceInfo;
import com.delelong.diandian.bean.Str;
import com.delelong.diandian.http.HttpUtils;
import com.delelong.diandian.pace.MyAMapLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * 发票详情页
 * Created by Administrator on 2016/9/19.
 */
public class InvoiceActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener,CompoundButton.OnCheckedChangeListener {

    private int checkNum; // 记录选中的条目数量
    private double totalSum;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_invoice);

        initActionBar();
        initView();
        initMsg();
    }

    ListView lv_invoice;
    CheckBox chb_all;
    TextView tv_total;
    Button btn_next;
    private void initView() {
        lv_invoice = (ListView) findViewById(R.id.lv_invoice);
        chb_all = (CheckBox) findViewById(R.id.chb_all);
        tv_total = (TextView) findViewById(R.id.tv_total);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setEnabled(false);//初始化不可点击

        btn_next.setOnClickListener(this);
    }

    HttpUtils httpUtils;
    Client client;
    MyAMapLocation myAMapLocation;
    SharedPreferences preferences;
    List<InvoiceInfo> invoiceInfos;
    MyInvoiceAdapter adapter;
    private void initMsg() {
        httpUtils = new HttpUtils(this);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null){
            myAMapLocation = (MyAMapLocation) bundle.getSerializable("myAMapLocation");
            client = (Client) bundle.getSerializable("client");//从上级activity获取
        }
        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        if (client == null) {
            client = httpUtils.getClientByGET(Str.URL_MEMBER);
        }

        initData();//加载发票信息
    }


    private void initData() {
        invoiceInfos = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            InvoiceInfo invoiceInfo = new InvoiceInfo("2016.09.20 20:30:35","10.50元","上海城市公寓-东门","百乐门悦府");
            invoiceInfos.add(invoiceInfo);
        }
        if (invoiceInfos != null){
            adapter = new MyInvoiceAdapter(invoiceInfos,this);
        }
        lv_invoice.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv_invoice.setOnItemClickListener(this);
        chb_all.setOnCheckedChangeListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arrow_back:
                finish();
                break;
            case R.id.btn_next:
                Bundle bundle = new Bundle();
                bundle.putDouble("totalSum",totalSum);
                intentActivityWithBundle(this,InvoiceInfoActivity.class,bundle);
                break;
        }
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){//全选
            btn_next.setEnabled(true);
            btn_next.setBackgroundResource(R.drawable.bg_corner_btn);
            // 遍历list的长度，将MyAdapter中的map值全部设为true
            for (int i = 0; i < invoiceInfos.size(); i++) {
                if (!MyInvoiceAdapter.getIsSelected().get(i)){
                    MyInvoiceAdapter.getIsSelected().put(i, true);
                    checkNum++;
                    totalSum += Double.parseDouble(invoiceInfos.get(i).getSum().replace("元",""));
                }
            }
            // 刷新listview和TextView的显示
            dataChanged();
        }else {
            btn_next.setEnabled(false);
            btn_next.setBackgroundResource(R.drawable.bg_gray_corner_btn);
            for (int i = 0; i < invoiceInfos.size(); i++) {
                if (MyInvoiceAdapter.getIsSelected().get(i)) {
                    MyInvoiceAdapter.getIsSelected().put(i, false);
                    checkNum--;// 数量减1
                    totalSum -= Double.parseDouble(invoiceInfos.get(i).getSum().replace("元",""));
                }
            }
            dataChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyInvoiceAdapter.ViewHolder holder = (MyInvoiceAdapter.ViewHolder) view.getTag();
        holder.chb.toggle();
        MyInvoiceAdapter.getIsSelected().put(position,holder.chb.isChecked());
        // 调整选定条目
        if (holder.chb.isChecked()) {
            checkNum++;
            totalSum += Double.parseDouble(invoiceInfos.get(position).getSum().replace("元",""));
        } else {
            checkNum--;
            totalSum -= Double.parseDouble(invoiceInfos.get(position).getSum().replace("元",""));
        }
        //button按钮状态
        if (checkNum>0){
            btn_next.setEnabled(true);
            btn_next.setBackgroundResource(R.drawable.bg_corner_btn);
        }else {
            chb_all.setChecked(false);
            btn_next.setEnabled(false);
            btn_next.setBackgroundResource(R.drawable.bg_gray_corner_btn);
        }
        // 用TextView显示
        dataChanged();
    }

    // 刷新listview和TextView的显示
    private void dataChanged() {
        // 通知listView刷新
        adapter.notifyDataSetChanged();
        // TextView显示最新的选中数目
        tv_total.setText(checkNum+"个行程 共"+totalSum+"元");
//        tv_total.setText(Html.fromHtml("<font color='#Fe8a03'>checkNum</font> 个行程 共<font color='#Fe8a03'>checkNum</font>元"));
    }


    ImageButton arrow_back;
    private void initActionBar() {
        arrow_back = (ImageButton) findViewById(R.id.arrow_back);
        arrow_back.setOnClickListener(this);
    }


}
