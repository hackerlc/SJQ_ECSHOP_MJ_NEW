package com.ecjia.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.AutoReturnView;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.entity.responsemodel.GROUPGOODS;
import com.ecjia.util.ImageLoaderForLV;

import java.util.ArrayList;

/**
 * 团购列表的adapter
 * Created by Administrator on 2015/8/18.
 */
public class GroupGoodListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<GROUPGOODS> groupgoodsArrayList = new ArrayList<GROUPGOODS>();

    public GroupGoodListAdapter(Context context, ArrayList<GROUPGOODS> groupgoodsArrayList) {
        this.context = context;
        this.groupgoodsArrayList = groupgoodsArrayList;
    }

    @Override
    public int getCount() {
        return groupgoodsArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return groupgoodsArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setData(ArrayList<GROUPGOODS> list) {
        groupgoodsArrayList = list;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        final GROUPGOODS groupgoods = groupgoodsArrayList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.goodlist_good_item, null);

            holder.image = (ImageView) convertView.findViewById(R.id.goodlist_img);
            holder.goodname = (AutoReturnView) convertView.findViewById(R.id.goodlist_goodname);
            holder.shopprice = (TextView) convertView.findViewById(R.id.goodlist_shop_price);
            holder.promoteprice = (TextView) convertView.findViewById(R.id.goodlist_promote_price);
            holder.item = (LinearLayout) convertView.findViewById(R.id.goodlist_item);
            holder.goodlist_buttom_margin_line = convertView.findViewById(R.id.goodlist_middel_line);
            holder.goodlist_buttom_line = convertView.findViewById(R.id.goodlist_bottom_line);
            holder.promoteprice.getPaint().setAntiAlias(true);
            holder.promoteprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            holder.top = convertView.findViewById(R.id.goodlist_top_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.goodname.setContent(groupgoods.getName());
        if (groupgoods.getPromote_price() != null && groupgoods.getPromote_price().length() > 0) {
            String str = groupgoods.getPromote_price().replace("￥", "").replace("元", "").replace("yuan", "").replace("¥", "");
            if ("免费".equals(str) || "0.00".equals(str)) {
                holder.shopprice.setText("免费");
            } else {
                holder.shopprice.setText(groupgoods.getPromote_price());
            }
        } else if (groupgoods.getShop_price() != null && groupgoods.getShop_price().length() > 0) {
            String str = groupgoods.getShop_price().replace("￥", "").replace("元", "").replace("yuan", "").replace("¥", "");
            if ("免费".equals(str) || "0.00".equals(str)) {
                holder.shopprice.setText("免费");
            } else {
                holder.shopprice.setText(groupgoods.getShop_price());
            }
        }
        String str = null;
        if (null != groupgoods.getMarket_price()) {
            str = groupgoods.getMarket_price().replace("￥", "").replace("元", "").replace("yuan", "").replace("¥", "");
        }
        if ("免费".equals(str) || "0.00".equals(str) || null == str || "".equals(str) || "0".equals(str)) {
            holder.promoteprice.setVisibility(View.INVISIBLE);
        } else {
            holder.promoteprice.setVisibility(View.VISIBLE);
            holder.promoteprice.setText(groupgoods.getMarket_price());
        }
        if (position == 0) {
            holder.top.setVisibility(View.VISIBLE);
        } else {
            holder.top.setVisibility(View.GONE);
        }

        if (position == (groupgoodsArrayList.size() - 1)) {
            holder.goodlist_buttom_margin_line.setVisibility(View.GONE);
            holder.goodlist_buttom_line.setVisibility(View.VISIBLE);
        } else {
            holder.goodlist_buttom_margin_line.setVisibility(View.VISIBLE);
            holder.goodlist_buttom_line.setVisibility(View.GONE);
        }

        holder.item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra(IntentKeywords.GOODS_ID, Integer.valueOf(groupgoods.getId()));
                intent.putExtra(IntentKeywords.OBJECT_ID, groupgoods.getObject_id());
                intent.putExtra(IntentKeywords.REC_TYPE, groupgoods.getRec_type());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            }
        });
        ImageLoaderForLV.getInstance(context).setImageRes(holder.image, groupgoods.getImg().getThumb());

        return convertView;
    }

    private class ViewHolder {
        private ImageView image;
        private AutoReturnView goodname;
        private TextView shopprice;
        private TextView promoteprice;
        private View top;
        private LinearLayout item;
        public View goodlist_buttom_margin_line;
        public View goodlist_buttom_line;
    }
}
