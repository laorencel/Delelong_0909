package com.delelong.diandian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.delelong.diandian.bean.Str;

import java.util.List;

/**
 * Created by Administrator on 2016/8/23.
 */
public class ChoosePosition extends BaseActivity implements PoiSearch.OnPoiSearchListener, TextWatcher,AdapterView.OnItemClickListener,View.OnClickListener {

    private static final String TAG = "BAIDUMAPFORTESTChoosePosition";
    TextView tv_city;
    EditText edt_choose;
    TextView tv_home, tv_company;
    ListView lv_address;
    LinearLayout ly_home_company;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.ly_choose_location);
        initView();
        searchPosition();
    }


    String city;
    String intentValue;
    SharedPreferences preferences;
    private void initView() {
        preferences = getSharedPreferences("user",Context.MODE_PRIVATE);

        tv_city = (TextView) findViewById(R.id.tv_city);
        edt_choose = (EditText) findViewById(R.id.edt_choose);

        ly_home_company = (LinearLayout) findViewById(R.id.ly_home_company);
        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_company = (TextView) findViewById(R.id.tv_company);
        tv_home.setOnClickListener(this);
        tv_company.setOnClickListener(this);

        lv_address = (ListView) findViewById(R.id.lv_address);
        lv_address.setDivider(getResources().getDrawable(R.color.listViewDivider));
        //初始化家庭公司地址
        String home = preferences.getString("home",null);
        String company = preferences.getString("company",null);
        if (home != null){
            tv_home.setText(home);
        }
        if (company != null){
            tv_company.setText(company);
        }
        //设置不同的提示语
        intentValue = getIntent().getStringExtra("choose");
        city = getIntent().getStringExtra("city");
        if (city != null){
            tv_city.setText(city);
        }
        if (intentValue!=null){
            if (intentValue.equals("myPosition")) {
                edt_choose.setHint("从哪里出发");
            } else if (intentValue.equals("myDestination")){
                edt_choose.setHint("到哪里去");
            }else if (intentValue.equals("home")){
                edt_choose.setHint("设置家庭住址");
                ly_home_company.setVisibility(View.GONE);
            }else if (intentValue.equals("company")){
                edt_choose.setHint("设置公司地址");
                ly_home_company.setVisibility(View.GONE);
            }
        }
        edt_choose.addTextChangedListener(this);
    }

    MyAddressAdapter adapter;

    PoiSearch mPoiSearch;
    PoiSearch.Query query;
    List<PoiItem> poiItems;

    private void searchPosition() {
        query = new PoiSearch.Query("美食", null, city);
        mPoiSearch = new PoiSearch(this, query);
        mPoiSearch.setOnPoiSearchListener(this);
        mPoiSearch.searchPOIAsyn();

        if (poiItems != null) {
            adapter = new MyAddressAdapter();
            lv_address.setAdapter(adapter);
        }
    }

    //获取POI检索结果
    @Override
    public void onPoiSearched(PoiResult poiResult, int rCode) {
        if (rCode == 1000) {
            if (poiResult != null && poiResult.getQuery() != null) {// 搜索poi的结果
                if (poiResult.getQuery().equals(query)) {// 是否是同一条
                    // 取得搜索到的poiitems有多少页
                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    if (poiItems != null && poiItems.size() > 0) {
                        if (adapter == null) {
                            adapter = new MyAddressAdapter();
                            lv_address.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        lv_address.setOnItemClickListener(this);
                    }
                }
            } else {
                Toast.makeText(ChoosePosition.this, "暂无搜索结果", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(ChoosePosition.this, "错误码："+rCode, Toast.LENGTH_SHORT).show();
            return;
        }

    }


    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    //监听输入框
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        query = new PoiSearch.Query(s.toString(), null, city);
        mPoiSearch = new PoiSearch(this, query);
        mPoiSearch.setOnPoiSearchListener(this);
        mPoiSearch.searchPOIAsyn();
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    /**
     * 点击选择地址，返回地图界面并传值
     *
     *  @param parent   parent
     *  @param view     view
     *  @param position position
     *  @param id       id
     *
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PoiItem item = poiItems.get(position);
        Bundle bundle = new Bundle();
        Intent intent = new Intent();
        if (intentValue.equals("myPosition")) {
            bundle.putParcelable("PoiInfo", item);
            intent.putExtra("bundle", bundle)
                    .putExtra("key", "myPosition");
            //返回结果
            setResult(Str.REQUESTPOSITIONCODE, intent);
            finish();
        } else if (intentValue.equals("myDestination")){
            bundle.putParcelable("PoiInfo", item);
            intent.putExtra("bundle", bundle)
                    .putExtra("key", "myDestination");
            setResult(Str.REQUESTDESTINATIONCODE, intent);
            finish();
        }
        else if (intentValue.equals("home")){
            bundle.putParcelable("PoiInfo", item);
            intent.putExtra("bundle", bundle)
                    .putExtra("key", "home");
            setResult(Str.REQUESTHOMECODE, intent);
            finish();
        }
        else if (intentValue.equals("company")){
            bundle.putParcelable("PoiInfo", item);
            intent.putExtra("bundle", bundle)
                    .putExtra("key", "company");
            setResult(Str.REQUESTCOMPANYCODE, intent);
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("key", "noChoice");
        setResult(Str.RESULTNOCHOICECODE, intent);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_home:
                intentActivityForResult(this, ChoosePosition.class, "choose", "home", city, Str.REQUESTHOMECODE);
                break;
            case R.id.tv_company:
                intentActivityForResult(this, ChoosePosition.class, "choose", "company", city, Str.REQUESTCOMPANYCODE);
                break;
        }
    }

    PoiItem mHomePoiItem;//家庭poi
    PoiItem mCompanyPoiItem;//公司poi
    //获取选取的位置信息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String value = data.getStringExtra("key");
        if (value.equals("noChoice")) {
            return;
        }
        Bundle bundle = data.getBundleExtra("bundle");
        switch (resultCode) {
            case Str.REQUESTHOMECODE:
                if (value.equals("home")) {
                    mHomePoiItem = bundle.getParcelable("PoiInfo");
                    tv_home.setText(mHomePoiItem.getTitle());
                    preferences.edit()
                            .putString("home",mHomePoiItem.getTitle())
                            .putString("homeAddress",mHomePoiItem.getSnippet())
                            .commit();
                }
                break;
            case Str.REQUESTCOMPANYCODE:
                if (value.equals("company")) {
                    mCompanyPoiItem = bundle.getParcelable("PoiInfo");
                    tv_company.setText(mCompanyPoiItem.getTitle());
                    preferences.edit()
                            .putString("company",mCompanyPoiItem.getTitle())
                            .putString("companyAddress",mCompanyPoiItem.getSnippet())
                            .commit();
                }
                break;
        }
    }


    /**
 * 适配器
 */
class MyAddressAdapter extends BaseAdapter {
    LayoutInflater inflater;

    public MyAddressAdapter() {
        inflater = LayoutInflater.from(ChoosePosition.this);
    }

    @Override
    public int getCount() {
        return poiItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PoiItem item = poiItems.get(position);

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_addr, null);
            holder = new ViewHolder();

            holder.addressName = (TextView) convertView.findViewById(R.id.addressName);
            holder.addressDetail = (TextView) convertView.findViewById(R.id.addressDetail);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.addressName.setText(item.getTitle());
        holder.addressDetail.setText(item.getSnippet()+"\t距离"+item.getDistance()+"米");

        return convertView;
    }

    class ViewHolder {
        TextView addressName, addressDetail;
    }

}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPoiSearch != null) {
            mPoiSearch = null;
        }
    }
}
