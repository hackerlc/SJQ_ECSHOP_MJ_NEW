package com.ecjia.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.view.activity.ShopListActivity;
import com.ecjia.entity.responsemodel.SELLERINFO;
import com.ecjia.util.ImageLoaderForLV;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/22.
 */
public class ShopSearchListAdapter extends BaseAdapter {
    private Resources res;
    private Context context;
    private ArrayList<SELLERINFO> sellerlist;
    private ImageLoaderForLV imageloader;
    private int width, distance;

    public Handler messageHandler;

    public ShopSearchListAdapter(Context context, ArrayList<SELLERINFO> sellerlist) {
        this.context = context;
        this.sellerlist = sellerlist;
        this.width = getDisplayMetricsWidth();
        res=context.getResources();
        distance = (int) res.getDimension(R.dimen.dp_5);
        imageloader = ImageLoaderForLV.getInstance(context);

    }

    public void setList(ArrayList<SELLERINFO> sellerlist) {
        this.sellerlist = sellerlist;
    }

    public ArrayList<SELLERINFO> getList() {
        return this.sellerlist;
    }

    @Override
    public int getCount() {
        return sellerlist.size();
    }

    @Override
    public Object getItem(int i) {
        return sellerlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        final SELLERINFO sellerinfo = sellerlist.get(i);
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.shop_search_item, null);
            holder.shop_item = (LinearLayout) convertView.findViewById(R.id.shoplist_item);
            holder.seller_logo = (ImageView) convertView.findViewById(R.id.seller_logo);
            holder.seller_item_shopname = (TextView) convertView.findViewById(R.id.seller_item_shopname);
            holder.seller_item_shopinfo = (TextView) convertView.findViewById(R.id.seller_item_shopinfo);
            holder.tv_enter = (TextView) convertView.findViewById(R.id.tv_enter);
            holder.tv_goods_price1 = (TextView) convertView.findViewById(R.id.tv_goods_price1);
            holder.tv_goods_price2 = (TextView) convertView.findViewById(R.id.tv_goods_price2);
            holder.tv_goods_price3 = (TextView) convertView.findViewById(R.id.tv_goods_price3);
            holder.img1 = (ImageView) convertView.findViewById(R.id.goodimg_1);
            holder.img2 = (ImageView) convertView.findViewById(R.id.goodimg_2);
            holder.img3 = (ImageView) convertView.findViewById(R.id.goodimg_3);
            initImageView(holder.img1);
            initImageView(holder.img2);
            initImageView(holder.img3);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (!TextUtils.isEmpty(sellerinfo.getSeller_logo())) {
            holder.seller_logo.setVisibility(View.VISIBLE);
            imageloader.setImageRes(holder.seller_logo, sellerinfo.getSeller_logo());
        } else {
            holder.seller_logo.setVisibility(View.INVISIBLE);
        }

        holder.seller_item_shopname.setText(sellerinfo.getSeller_name());
        holder.seller_item_shopinfo.setText(sellerinfo.getFollower() + res.getString(R.string.follower_num)
                +"  ");


        holder.tv_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShopListActivity.class);
                intent.putExtra(IntentKeywords.MERCHANT_ID, sellerinfo.getId());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);

            }
        });
        int size = sellerinfo.getSellergoods().size();
        if (size > 0) {
            holder.img1.setVisibility(View.VISIBLE);
            imageloader.setImageRes(holder.img1, sellerinfo.getSellergoods().get(0).getImg().getThumb());
            holder.tv_goods_price1.setVisibility(View.VISIBLE);
            if(TextUtils.isEmpty(sellerinfo.getSellergoods().get(0).getPromote_price())){
                holder.tv_goods_price1.setText(sellerinfo.getSellergoods().get(0).getShop_price());
            }else{
                holder.tv_goods_price1.setText(sellerinfo.getSellergoods().get(0).getPromote_price());
            }
            holder.img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, GoodsDetailActivity.class);
                    intent.putExtra(IntentKeywords.GOODS_ID, sellerinfo.getSellergoods().get(0).getId());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }
            });
        } else {
            holder.img1.setVisibility(View.INVISIBLE);
            holder.tv_goods_price1.setVisibility(View.INVISIBLE);
        }
        if (size > 1) {
            holder.img2.setVisibility(View.VISIBLE);
            imageloader.setImageRes(holder.img2, sellerinfo.getSellergoods().get(1).getImg().getThumb());
            holder.tv_goods_price2.setVisibility(View.VISIBLE);
            if(TextUtils.isEmpty(sellerinfo.getSellergoods().get(1).getPromote_price())){
                holder.tv_goods_price2.setText(sellerinfo.getSellergoods().get(1).getShop_price());
            }else{
                holder.tv_goods_price2.setText(sellerinfo.getSellergoods().get(1).getPromote_price());
            }
            holder.img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, GoodsDetailActivity.class);
                    intent.putExtra(IntentKeywords.GOODS_ID, sellerinfo.getSellergoods().get(1).getId());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }
            });
        } else {
            holder.img2.setVisibility(View.INVISIBLE);
            holder.tv_goods_price2.setVisibility(View.INVISIBLE);
        }
        if (size > 2) {
            holder.img3.setVisibility(View.VISIBLE);
            imageloader.setImageRes(holder.img3, sellerinfo.getSellergoods().get(2).getImg().getThumb());
            holder.tv_goods_price3.setVisibility(View.VISIBLE);
            if(TextUtils.isEmpty(sellerinfo.getSellergoods().get(2).getPromote_price())){
                holder.tv_goods_price3.setText(sellerinfo.getSellergoods().get(2).getShop_price());
            }else{
                holder.tv_goods_price3.setText(sellerinfo.getSellergoods().get(2).getPromote_price());
            }
            holder.img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, GoodsDetailActivity.class);
                    intent.putExtra(IntentKeywords.GOODS_ID, sellerinfo.getSellergoods().get(2).getId());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }
            });
        } else {
            holder.img3.setVisibility(View.INVISIBLE);
            holder.tv_goods_price3.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private class ViewHolder {
        private ImageView img1, img2, img3;
        private TextView tv_goods_price1,tv_goods_price2,tv_goods_price3;
        private ImageView seller_logo;
        private TextView tv_enter;
        private LinearLayout shop_item;
        private TextView seller_item_shopname,seller_item_shopinfo;
    }

    void initImageView(ImageView img) {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                width / 3 - distance * 2, width / 3 - distance * 2);
        lp.height = width / 3 - distance * 2;
        img.setLayoutParams(lp);
    }

    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        int j = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j);
    }

}
