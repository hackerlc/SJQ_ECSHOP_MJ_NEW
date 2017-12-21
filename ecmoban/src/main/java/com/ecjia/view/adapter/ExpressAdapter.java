package com.ecjia.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.widgets.DashedLineView;
import com.ecjia.entity.responsemodel.EXPRESS;
import com.ecmoban.android.sijiqing.R;

import java.util.ArrayList;

public class ExpressAdapter extends BaseAdapter {

    public ArrayList<EXPRESS> list;

    public Context c;

    public String[] dates;

    public ExpressAdapter(Context c, ArrayList<EXPRESS> list) {
        this.c = c;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list.size() == 0 || list == null) {
            return 0;
        }

        return list.size();
    }

    @Override
    public Object getItem(int i) {
        if (list.size() == 0 || list == null) {
            return null;
        }
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (list.size() == 0 || list == null) {
            return null;
        }
        ViewHolder holder;
        if (null == view) {
            holder = new ViewHolder();
            view = View.inflate(c, R.layout.kuaidi_item, null);
            holder.date = (TextView) view.findViewById(R.id.log_date);
            holder.time = (TextView) view.findViewById(R.id.log_time);
            holder.info = (TextView) view.findViewById(R.id.log_text);
            holder.top = (DashedLineView) view.findViewById(R.id.dashedlin_top);
            holder.bottom = (DashedLineView) view.findViewById(R.id.dashedlin_bottom);
            holder.img_status2 = (ImageView) view.findViewById(R.id.img_status2);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        dates = list.get(i).getTime().split(" ");
        holder.info.setText(list.get(i).getContext());
        holder.date.setText(dates[0]);
        holder.time.setText(dates[1]);
        if (i == 0) {
            holder.top.setVisibility(View.INVISIBLE);
            holder.bottom.setVisibility(View.VISIBLE);
            holder.info.setTextColor(Color.parseColor("#000000"));
            holder.date.setTextColor(Color.parseColor("#aaaaaa"));
            holder.time.setTextColor(Color.parseColor("#aaaaaa"));
        } else {
            holder.top.setVisibility(View.VISIBLE);
            if (i == list.size() - 1) {
                holder.bottom.setVisibility(View.INVISIBLE);
            } else {
                holder.bottom.setVisibility(View.VISIBLE);
            }
            holder.info.setTextColor(Color.parseColor("#999999"));
            holder.date.setTextColor(Color.parseColor("#aaaaaa"));
            holder.time.setTextColor(Color.parseColor("#aaaaaa"));
        }
        return view;
    }

    class ViewHolder {
        TextView time, date;
        TextView info;
        ImageView img_status2;
        DashedLineView top, bottom;

    }

}