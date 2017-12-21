package com.ecjia.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.CITY;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/14.
 */
public class HotCityAdapter extends BaseAdapter{
    private Context c;
    private ArrayList<CITY> list;
    public HotCityAdapter(Context c,ArrayList<CITY> list){
        this.c=c;
        this.list=list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if(view==null){
            holder = new ViewHolder();
            view= LayoutInflater.from(c).inflate(R.layout.hotcity_item,null);
            holder.citytext= (TextView) view.findViewById(R.id.city_text);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.citytext.setText(list.get(i).getName());
        return view;
    }

    private class ViewHolder{
        TextView citytext;
    }
}
