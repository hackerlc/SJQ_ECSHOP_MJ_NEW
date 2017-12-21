package com.ecjia.view.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.AppConst;
import com.ecjia.view.activity.InfoWebActivity;
import com.ecjia.entity.responsemodel.SHOPINFO;
import com.ecjia.util.ImageLoaderForLV;

import java.util.ArrayList;

public class MineHelpListAdapter extends BaseAdapter {

    public ArrayList<SHOPINFO> list = new ArrayList<SHOPINFO>();
    private Context c;

    public MineHelpListAdapter(Context c, ArrayList<SHOPINFO> shopinfos) {
        this.list = shopinfos;
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
            convertView = LayoutInflater.from(c).inflate(R.layout.mine_my_helpcell, null);

            holder.shopinfo_content = (TextView) convertView.findViewById(R.id.shopinfo_content);
            holder.shopinfo_item = (LinearLayout) convertView.findViewById(R.id.shopinfo_item);
            holder.iv_shopinfo = (ImageView) convertView.findViewById(R.id.iv_shopinfo);
            holder.firstline = convertView.findViewById(R.id.info_first_line);
            holder.endline = convertView.findViewById(R.id.info_end_line);
            holder.middlelinetop = convertView.findViewById(R.id.info_middle_line_top);

            holder.firstline.setVisibility(View.GONE);

            if (position == list.size() - 1) {
                holder.middlelinetop.setVisibility(View.GONE);
            } else {
                holder.middlelinetop.setVisibility(View.VISIBLE);
            }
            holder.endline.setVisibility(View.GONE);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.shopinfo_content
                .setText(list.get(position).getTitle());
        ImageLoaderForLV.getInstance(c).setImageRes(holder.iv_shopinfo, list.get(position).getImage(), AppConst.SHOPINFOIMAGE);
        holder.shopinfo_item.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, InfoWebActivity.class);
                intent.putExtra("id", list.get(position).getId());
                intent.putExtra("title", list.get(position).getTitle());
                c.startActivity(intent);
                ((Activity) c).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

            }
        });

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
        TextView shopinfo_content;
        ImageView iv_shopinfo;
        LinearLayout shopinfo_item;
        View firstline, endline, middlelinetop;
    }

}
