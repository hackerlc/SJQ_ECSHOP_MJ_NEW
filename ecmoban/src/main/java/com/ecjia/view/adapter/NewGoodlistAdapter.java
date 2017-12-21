package com.ecjia.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.entity.responsemodel.SIMPLEGOODS;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.widgets.AutoReturnView;
import com.ecmoban.android.sijiqing.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class NewGoodlistAdapter extends BaseAdapter {
    private Context context;
    public List<SIMPLEGOODS> list;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    public NewGoodlistAdapter(Context c, List<SIMPLEGOODS> list) {
        context = c;
        this.list = list;
    }

    public void setDate(List<SIMPLEGOODS> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.goodlist_good_item, null);

            holder.image = (ImageView) convertView.findViewById(R.id.goodlist_img);
            holder.goodname = (AutoReturnView) convertView
                    .findViewById(R.id.goodlist_goodname);
            holder.shopprice = (TextView) convertView
                    .findViewById(R.id.goodlist_shop_price);
            holder.promoteprice = (TextView) convertView
                    .findViewById(R.id.goodlist_promote_price);
            holder.item = (LinearLayout) convertView.findViewById(R.id.goodlist_item);
            holder.tv_saving = (TextView) convertView
                    .findViewById(R.id.tv_saving);
            holder.ll_goodlist_mb = (LinearLayout) convertView.findViewById(R.id.ll_goodlist_mb);
            holder.promoteprice.getPaint().setAntiAlias(true);
            //holder.promoteprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            //textview的中划线
            holder.promoteprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            holder.top = convertView.findViewById(R.id.goodlist_top_line);
            holder.goodlist_buttom_margin_line = convertView.findViewById(R.id.goodlist_middel_line);
            holder.goodlist_buttom_line = convertView.findViewById(R.id.goodlist_bottom_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.goodname.setContent(list.get(position).getName());
        if (list.get(position).getPromote_price() != null && list.get(position).getPromote_price().length() > 0) {
            String str = list.get(position).getPromote_price().replace("￥", "").replace("元", "").replace("yuan", "").replace("¥", "");
            if ("免费".equals(str) || "0.00".equals(str)) {
                holder.shopprice.setText("免费");
            } else {
                holder.shopprice.setText(list.get(position).getPromote_price());
            }
        } else if (list.get(position).getShop_price() != null && list.get(position).getShop_price().length() > 0) {
            String str = list.get(position).getShop_price().replace("￥", "").replace("元", "").replace("yuan", "").replace("¥", "");
            if ("免费".equals(str) || "0.00".equals(str)) {
                holder.shopprice.setText("免费");
            } else {
                holder.shopprice.setText(list.get(position).getShop_price());
            }
        }

        String str = null;
        if (null != list.get(position).getMarket_price()) {
            str = list.get(position).getMarket_price().replace("￥", "").replace("元", "").replace("yuan", "").replace("¥", "");
        }
        if ("免费".equals(str) || "0.00".equals(str) || null == str || "".equals(str) || "0".equals(str)) {
            holder.promoteprice.setVisibility(View.INVISIBLE);
        } else {
            holder.promoteprice.setVisibility(View.VISIBLE);
            holder.promoteprice.setText(list.get(position).getMarket_price());
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
                int id = list.get(position).getGoods_id();
                if (id == 0) {
                    id = list.get(position).getId();
                }
                intent.putExtra(IntentKeywords.GOODS_ID, id+"");
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            }
        });
        Glide.with(context).load(list.get(position).getImg().getThumb()).into(holder.image);
//        ImageLoaderForLV.getInstance(context).setImageRes(holder.image, list.get(position).getImg().getThumb());

        return convertView;
    }

    class ViewHolder {

        private ImageView image;
        private AutoReturnView goodname;
        private TextView shopprice;
        private TextView promoteprice, tv_saving;
        private View top;
        private LinearLayout item, ll_goodlist_mb;
        public View goodlist_buttom_margin_line;
        public View goodlist_buttom_line;
    }
}
