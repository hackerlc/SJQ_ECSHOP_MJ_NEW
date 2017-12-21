package com.ecjia.view.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.view.activity.HelpActivity;
import com.ecjia.network.netmodle.HelpModel;

public class HelpListAdapter extends BaseAdapter {

    private HelpModel dataModel;
    private Context c;

    public HelpListAdapter(Context c, HelpModel dataModel) {
        this.dataModel = dataModel;
        this.c = c;
    }

    @Override
    public int getCount() {
        return dataModel.shophelpsList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(c).inflate(R.layout.my_helpcell, null);
            holder.shophelp_content = (TextView) convertView.findViewById(R.id.shophelp_content);
            holder.shophelp_item = (LinearLayout) convertView.findViewById(R.id.shophelp_item);
            holder.firstline = convertView.findViewById(R.id.help_middle_line_top);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (dataModel.shophelpsList.size() == 1 || position == dataModel.shophelpsList.size() - 1) {
            holder.firstline.setVisibility(View.GONE);
        } else {
            holder.firstline.setVisibility(View.VISIBLE);
        }
        holder.shophelp_content.setText(dataModel.shophelpsList.get(position).getName());
        holder.shophelp_item.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, HelpActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("data", dataModel.data);
                c.startActivity(intent);
                ((Activity) c).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return dataModel.shophelpsList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    class ViewHolder {
        TextView shophelp_content;
        LinearLayout shophelp_item;
        View firstline;
    }

}
