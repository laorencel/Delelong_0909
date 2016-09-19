package com.delelong.diandian.menuActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.delelong.diandian.R;
import com.delelong.diandian.bean.InvoiceInfo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class MyInvoiceAdapter extends BaseAdapter {

    private static final String TAG = "BAIDUMAPFORTEST";
    private List<InvoiceInfo> list;
    private static HashMap<Integer,Boolean> isSelected;
    // 上下文
    private Context context;
    // 用来导入布局
    private LayoutInflater inflater = null;

    public MyInvoiceAdapter(List<InvoiceInfo> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        isSelected = new HashMap<Integer, Boolean>();
        initDate();
    }
    // 初始化isSelected的数据
    private void initDate(){
        for(int i=0; i<list.size();i++) {
            getIsSelected().put(i,false);
        }
    }

    public static HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer,Boolean> isSelected) {
        MyInvoiceAdapter.isSelected = isSelected;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        InvoiceInfo invoiceInfo = list.get(position);
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_list_invoice,null);
            holder = new ViewHolder();
            holder.chb = (CheckBox) convertView.findViewById(R.id.chb);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.tv_start = (TextView) convertView.findViewById(R.id.tv_start);
            holder.tv_end = (TextView) convertView.findViewById(R.id.tv_end);
            holder.tv_sum = (TextView) convertView.findViewById(R.id.tv_sum);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.chb.setChecked(getIsSelected().get(position));
        holder.time.setText(invoiceInfo.getTime());
        holder.tv_start.setText(invoiceInfo.getStart());
        holder.tv_end.setText(invoiceInfo.getEnd());
        holder.tv_sum.setText(invoiceInfo.getSum());
        return convertView;
    }
    class ViewHolder{
        CheckBox chb;
        TextView time;
        TextView tv_start,tv_end;
        TextView tv_sum;
    }
}
