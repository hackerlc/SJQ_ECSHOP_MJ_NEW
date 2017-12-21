package com.ecjia.view.adapter;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.widgets.AutoReturnView;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.AppConst;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.entity.responsemodel.GOODS;
import com.ecjia.util.FormatUtil;
import com.ecjia.util.ImageLoaderForLV;

public class GoodbrowseAdapter extends BaseAdapter {

    private Context context;
    public List<GOODS> list;
    private LayoutInflater inflater;
    private Bitmap b = null;

    public GoodbrowseAdapter(Context context, List<GOODS> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.lastbrowse_item, null);

            holder.image = (ImageView) convertView.findViewById(R.id.goodlist_img);
            holder.goodname = (AutoReturnView) convertView.findViewById(R.id.goodlist_goodname);
            holder.shopprice = (TextView) convertView.findViewById(R.id.goodlist_shop_price);
            holder.originprice = (TextView) convertView.findViewById(R.id.goodlist_promote_price);
            holder.item = (LinearLayout) convertView.findViewById(R.id.goodlist_item);
            holder.tv_saving = (TextView) convertView.findViewById(R.id.tv_saving);
            holder.ll_goodlist_mb = (LinearLayout) convertView.findViewById(R.id.ll_goodlist_mb);
            holder.originprice.getPaint().setAntiAlias(true);
            //textview的中划线
            holder.originprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            holder.top = convertView.findViewById(R.id.goodlist_top_line);
            holder.goodlist_buttom_margin_line = convertView.findViewById(R.id.goodlist_buttom_margin_line);
            holder.goodlist_buttom_line = convertView.findViewById(R.id.goodlist_buttom_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.goodname.setContent(list.get(position).getGoods_name());

        float promote = FormatUtil.formatStrToFloat(list.get(position).getFormated_promote_price());
        if (promote!=0) {
            holder.shopprice.setText(list.get(position).getFormated_promote_price());
            holder.originprice.setText(list.get(position).getShop_price());
        } else {
            float str = FormatUtil.formatStrToFloat(list.get(position).getShop_price());
            if (str==0) {
                holder.shopprice.setText("免费");
                holder.originprice.setText("");
            } else {
                holder.shopprice.setText(list.get(position).getShop_price());
                holder.originprice.setText(list.get(position).getMarket_price());
            }
        }

        if (AppConst.MOBILEBUY_GOODS.equals(list.get(position).getActivity_type())) {
            holder.ll_goodlist_mb.setVisibility(View.VISIBLE);
            holder.tv_saving.setText(list.get(position).getFormatted_saving_price());
        } else {
            holder.ll_goodlist_mb.setVisibility(View.GONE);
        }

        if (position == 0) {
            holder.top.setVisibility(View.VISIBLE);
        } else {
            holder.top.setVisibility(View.GONE);
        }

        if (position == (list.size() - 1)) {
            holder.goodlist_buttom_margin_line.setVisibility(View.GONE);
            holder.goodlist_buttom_line.setVisibility(View.VISIBLE);
        } else {
            holder.goodlist_buttom_margin_line.setVisibility(View.VISIBLE);
            holder.goodlist_buttom_line.setVisibility(View.GONE);
        }

        holder.item.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra("goods_id", list.get(position).getId() + "");
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        ImageLoaderForLV.getInstance(context).setImageRes(holder.image,list.get(position).getImg().getThumb());

        return convertView;
    }

    class ViewHolder {

        private ImageView image;
        private AutoReturnView goodname;
        private TextView shopprice;
        private TextView originprice, tv_saving;
        private View top;
        private LinearLayout item, ll_goodlist_mb;
        public View goodlist_buttom_margin_line;
        public View goodlist_buttom_line;
    }

}
