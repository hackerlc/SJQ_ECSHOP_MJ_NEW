package com.ecjia.view.adapter;

import java.util.ArrayList;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.GOODS_LIST;
import com.ecjia.widgets.webimageview.WebImageView;
import com.ecjia.util.FormatUtil;
import com.ecjia.util.ImageLoaderForLV;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BalanceAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<GOODS_LIST> list;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    protected ImageLoaderForLV imageLoader;
    View v;

    public BalanceAdapter(Context context, ArrayList<GOODS_LIST> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        imageLoader = ImageLoaderForLV.getInstance(context);
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return new Object();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.shop_goods_group, null);
            holder = new ViewHolder();
            holder.goodsgroup = (LinearLayout) convertView.findViewById(R.id.goods_group);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        for (int i = 0; i < list.size(); i++) {// for (int i = 0; i <
            v = LayoutInflater.from(context).inflate(R.layout.order_create_item, null);
            WebImageView image = (WebImageView) v
                    .findViewById(R.id.order_body_image);
            TextView text = (TextView) v
                    .findViewById(R.id.trade_body_text);
            TextView total = (TextView) v
                    .findViewById(R.id.trade_body_total);
            TextView num = (TextView) v
                    .findViewById(R.id.trade_body_num);
            View trade_item_mid = (View) v
                    .findViewById(R.id.trade_item_mid);
            View trade_item_buttom = (View) v
                    .findViewById(R.id.trade_item_buttom);

            if (i == list.size() - 1) {
                trade_item_buttom.setVisibility(View.VISIBLE);
                trade_item_mid.setVisibility(View.GONE);
            } else {
                trade_item_buttom.setVisibility(View.GONE);
                trade_item_mid.setVisibility(View.VISIBLE);
            }

            if (null != list.get(i).getImg() && list.get(i).getImg().getThumb().length() > 0) {
                imageLoader.setImageRes(image, list.get(i).getImg().getThumb());
            } else {
                imageLoader.setImageRes(image, R.drawable.default_image);
            }
            text.setText(list.get(i).getGoods_name());
            if (FormatUtil.formatStrToFloat(list.get(i).getFormated_goods_price()) == 0) {

            } else {

            }

            total.setText(list.get(i).getFormated_goods_price());
            num.setText("X " + list.get(i).getGoods_number());

            holder.goodsgroup.addView(v);
        }
        holder.goodsgroup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });
        v = null;
        return convertView;
    }

    class ViewHolder {
        private LinearLayout goodsgroup;
    }
}