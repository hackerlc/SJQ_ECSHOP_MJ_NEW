package com.ecjia.view.adapter;

import java.util.ArrayList;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.entity.responsemodel.GOODS_LIST;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShopgoodsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<GOODS_LIST> list;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    boolean isOrder;

    public ShopgoodsAdapter(Context c, ArrayList<GOODS_LIST> list) {
        this(c, list, false);
        this.context = c;
        this.list = list;
    }


    public ShopgoodsAdapter(Context c, ArrayList<GOODS_LIST> list, boolean isOrder) {
        this.context = c;
        this.list = list;
        this.isOrder = isOrder;
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.shopgoods_item, null);
            holder = new ViewHolder();
            holder.item = (LinearLayout) convertView.findViewById(R.id.shopgoods_item);
            holder.top = convertView.findViewById(R.id.shopgoods_item_top);
            holder.buttom = convertView.findViewById(R.id.shopgoods_item_buttom);
            holder.image = (ImageView) convertView.findViewById(R.id.shopgoods_body_image);
            holder.text = (TextView) convertView.findViewById(R.id.shopgoods_body_text);
            holder.total = (TextView) convertView.findViewById(R.id.shopgoods_body_total);
            holder.num = (TextView) convertView.findViewById(R.id.shopgoods_body_num);
            holder.balanceitem = (LinearLayout) convertView.findViewById(R.id.shopgoods_item);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.top.setVisibility(View.VISIBLE);
        }

        imageLoader.displayImage(list.get(position).getImg().getThumb(), holder.image);

        holder.text.setText(list.get(position).getGoods_name());
        holder.total.setText(list.get(position).getGoods_price());
        holder.num.setText("x " + list.get(position).getGoods_number());

        holder.item.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isOrder) {
                    Intent intent = new Intent(context, GoodsDetailActivity.class);
                    intent.putExtra(IntentKeywords.GOODS_ID, Integer.valueOf(list.get(position).getGoods_id()) + "");
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }

            }
        });

        return convertView;
    }

    class ViewHolder {
        private View top;
        private View buttom;
        private ImageView image;
        private TextView text;
        private TextView total;
        private TextView num;
        private LinearLayout item;
        private LinearLayout balanceitem;
    }
}
