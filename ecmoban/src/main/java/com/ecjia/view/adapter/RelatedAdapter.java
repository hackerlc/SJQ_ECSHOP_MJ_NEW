package com.ecjia.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.widgets.AutoReturnView;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.entity.responsemodel.RELATED_GOOD;
import com.ecjia.util.ImageLoaderForLV;
import com.ecmoban.android.sijiqing.R;

import java.util.List;

/**
 * Created by Administrator on 2015/3/25.
 */
public class RelatedAdapter extends BaseAdapter {

    private Context context;
    public List<RELATED_GOOD> list;

    public RelatedAdapter(Context c, List<RELATED_GOOD> list) {
        context = c;
        this.list = list;
    }

    public void setDate(List<RELATED_GOOD> list) {
        this.list = list;
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.goodlist_good_item, null);

            holder.image = (ImageView) convertView
                    .findViewById(R.id.goodlist_img);
            holder.goodname = (AutoReturnView) convertView
                    .findViewById(R.id.goodlist_goodname);
            holder.shopprice = (TextView) convertView
                    .findViewById(R.id.goodlist_shop_price);
            holder.promoteprice = (TextView) convertView
                    .findViewById(R.id.goodlist_promote_price);
            holder.tv_saving = (TextView) convertView
                    .findViewById(R.id.tv_saving);
            holder.ll_goodlist_mb = (LinearLayout) convertView.findViewById(R.id.ll_goodlist_mb);
            holder.item = (LinearLayout) convertView.findViewById(R.id.goodlist_item);
            holder.promoteprice.getPaint().setAntiAlias(true);
            //textview的中划线
            holder.promoteprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            holder.top = convertView.findViewById(R.id.goodlist_top_line);
            holder.goodlist_buttom_margin_line = convertView.findViewById(R.id.goodlist_middel_line);
            holder.goodlist_buttom_line = convertView.findViewById(R.id.goodlist_bottom_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.goodname.setContent(list.get(position).getGood_name());
        if (!TextUtils.isEmpty(list.get(position).getPromote_price()) && !"0".equals(list.get(position).getPromote_price())) {
            holder.shopprice.setText(list.get(position).getPromote_price());
        } else {
            holder.shopprice.setText(list.get(position).getShop_price());
        }
        if("免费".equals(list.get(position).getShop_price())){
            holder.promoteprice.setVisibility(View.INVISIBLE);
        }else {
            holder.promoteprice.setVisibility(View.VISIBLE);
            holder.promoteprice.setText(list.get(position).getMarket_price());
        }
        holder.promoteprice.setText(list.get(position).getMarket_price());


        if (position == 0) {
            holder.top.setVisibility(View.VISIBLE);
        } else {
            holder.top.setVisibility(View.GONE);
        }

        if (position == (list.size()-1)) {
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
                intent.putExtra("goods_id", list.get(position).getId());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            }
        });
        ImageLoaderForLV.getInstance(context).setImageRes(holder.image, list.get(position).getImg().getThumb());


        return convertView;
    }

    class ViewHolder {

        private ImageView image;
        private AutoReturnView goodname;
        private TextView shopprice;
        private TextView tv_saving;
        private TextView promoteprice;
        private View top,goodlist_buttom_line,goodlist_buttom_margin_line;
        private LinearLayout ll_goodlist_mb;
        private LinearLayout item;
    }
}
