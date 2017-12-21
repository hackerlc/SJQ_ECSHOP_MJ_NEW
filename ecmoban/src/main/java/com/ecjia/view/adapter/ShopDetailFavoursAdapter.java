package com.ecjia.view.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.FAVOUR;

import java.util.ArrayList;

public class ShopDetailFavoursAdapter extends BaseAdapter {

    public ArrayList<FAVOUR> list = new ArrayList<>();
    private Context c;

    public ShopDetailFavoursAdapter(Context c, ArrayList<FAVOUR> data) {
        this.list = data;
        this.c = c;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(c).inflate(R.layout.item_shop_detail_favour, null);

            holder.name = (TextView) convertView.findViewById(R.id.tv_favour_name);
            holder.content = (TextView) convertView.findViewById(R.id.tv_favour_content);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position).getType_label());
        holder.content.setText(list.get(position).getName());

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    class ViewHolder {
        TextView name,content;
    }

}
