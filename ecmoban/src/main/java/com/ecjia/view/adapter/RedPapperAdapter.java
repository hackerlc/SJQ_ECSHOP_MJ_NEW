package com.ecjia.view.adapter;

import java.util.ArrayList;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.BONUS;
import com.ecjia.util.FormatUtil;


import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RedPapperAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<BONUS> data;
    private Context context;

    public RedPapperAdapter(ArrayList<BONUS> data, Context context) {
        this.data = data;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BONUS bonus = data.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.redpapper_list_item, null);
            holder.type_name = (TextView) convertView.findViewById(R.id.red_type);
            holder.money = (TextView) convertView.findViewById(R.id.red_money);
            holder.goodfee = (TextView) convertView.findViewById(R.id.red_goodsfee);
            holder.starttime = (TextView) convertView.findViewById(R.id.red_starttime);
            holder.endtime = (TextView) convertView.findViewById(R.id.red_endtime);
            holder.status = (TextView) convertView.findViewById(R.id.red_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.type_name.setText(data.get(position).getBonus_name());
        holder.money.setText(data.get(position).getFormatted_bonus_amount());
        if (0 == FormatUtil.formatStrToFloat(data.get(position).getRequest_amount())) {
            holder.goodfee.setText("");
        } else {
            Resources resources=context.getResources();
            String need_tobuy=resources.getString(R.string.redpapper_youneed);
            String goods=resources.getString(R.string.redpapper_goods);
            holder.goodfee.setText(need_tobuy + data.get(position).getFormatted_request_amount() + goods);
        }
        holder.starttime.setText(data.get(position).getFormatted_start_date());
        holder.endtime.setText(data.get(position).getFormatted_end_date());
        holder.status.setText(data.get(position).getLabel_status());
        return convertView;

    }

    class ViewHolder {
        TextView type_name;
        TextView money;
        TextView goodfee;
        TextView starttime;
        TextView endtime;
        TextView status;
    }

}
