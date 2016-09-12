package com.delelong.diandian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.delelong.diandian.R;
import com.delelong.diandian.bean.MenuListItem;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public class MyMenuGridAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private List<MenuListItem> itemList;
    public MyMenuGridAdapter(Context context,
                             List<MenuListItem> itemList) {
        this.context = context;
        this.itemList = itemList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MenuListItem item = itemList.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_grid_menu, null);
            holder = new ViewHolder();

            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.tv = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.img.setImageResource(item.getImg());
        holder.tv.setText(item.getTv());

        return convertView;
    }

    class ViewHolder {
        ImageView img;
        TextView tv;
    }
    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}