package com.ecjia.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.widgets.SelectableRoundedImageView;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.entity.responsemodel.MOBILEGOODS;
import com.ecjia.util.ImageLoaderForLV;
import com.ecmoban.android.sijiqing.R;

import java.util.ArrayList;

public class MobileBuyGridAdapter extends BaseAdapter {

    private final int distance;
    private Context context;

    private ArrayList<MOBILEGOODS> list;
    private ImageLoaderForLV imageLoaderForLV;

    public MobileBuyGridAdapter(Context context) {
        this.context = context;
        resource = context.getResources();
        imageLoaderForLV = ImageLoaderForLV.getInstance(context);
        distance = (int) context.getResources().getDimension(R.dimen.mobilebuy_dp);
    }

    public void setAdapterDate(ArrayList<MOBILEGOODS> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_homefragment_mb, null);
            holder.homefragment_mb_item = (LinearLayout) convertView.findViewById(R.id.homefragment_mb_item);
            holder.fl_mb = (FrameLayout) convertView.findViewById(R.id.fl_mb);
            holder.homefragment_mb_img = (SelectableRoundedImageView) convertView.findViewById(R.id.homefragment_mb_img);
            holder.homefragment_mb_des = (TextView) convertView.findViewById(R.id.homefragment_mb_des);
            holder.homefragment_mb_price = (TextView) convertView.findViewById(R.id.homefragment_mb_price);
            holder.homefragment_mb_marketprice = (TextView) convertView.findViewById(R.id.homefragment_mb_marketprice);
            holder.homefragment_mb_name = (TextView) convertView.findViewById(R.id.homefragment_mb_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ViewGroup.LayoutParams params2 = holder.fl_mb.getLayoutParams();
        params2.width = (int) ((getDisplayMetricsWidth() - distance) * 1.0 / 2);
        params2.height = params2.width;
        holder.fl_mb.setLayoutParams(params2);

        holder.homefragment_mb_marketprice.setVisibility(View.GONE);

        imageLoaderForLV.setImageRes(holder.homefragment_mb_img, list.get(position).getImg().getThumb());

        holder.homefragment_mb_price.setText(list.get(position).getPromote_price());
        holder.homefragment_mb_marketprice.setText(list.get(position).getMarket_price());
        holder.homefragment_mb_marketprice.getPaint().setAntiAlias(true);
        holder.homefragment_mb_marketprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.homefragment_mb_name.setText(list.get(position).getName());
        holder.homefragment_mb_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra(IntentKeywords.GOODS_ID, list.get(position).getId() + "");
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        return convertView;
    }


    private class ViewHolder {
        public LinearLayout homefragment_mb_item;
        public FrameLayout fl_mb;
        public SelectableRoundedImageView homefragment_mb_img;
        public TextView homefragment_mb_des;
        public TextView homefragment_mb_price;
        public TextView homefragment_mb_marketprice;
        public TextView homefragment_mb_name;

    }

    Resources resource;

    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        int j = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j);
    }

}