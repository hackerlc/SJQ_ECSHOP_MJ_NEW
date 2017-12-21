package com.ecjia.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.activity.ShopListActivity;
import com.ecjia.entity.responsemodel.SELLERINFO;
import com.ecjia.util.ImageLoaderForLV;

import java.util.ArrayList;

public class HomeSellerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SELLERINFO> sellerinfos;
    private int width, distance;
    private ImageLoaderForLV imageloader;

    public HomeSellerAdapter(Context context, ArrayList<SELLERINFO> list) {
        this.context = context;
        sellerinfos = list;
        this.width = getDisplayMetricsWidth1();
        distance = (int) context.getResources().getDimension(R.dimen.eight_margin);
        imageloader = ImageLoaderForLV.getInstance(context);
    }


    @Override
    public int getCount() {
        return sellerinfos.size();
    }

    @Override
    public Object getItem(int i) {
        return sellerinfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final SELLERINFO sellerinfo = sellerinfos.get(i);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.home_seller_item, null);
            holder.seller_logo = (ImageView) view.findViewById(R.id.seller_logo);
            holder.seller_item_shopname = (TextView) view.findViewById(R.id.seller_shop_name);
            holder.shop_item = (LinearLayout) view.findViewById(R.id.home_shoplist_item);
            holder.collceted_num = (TextView) view.findViewById(R.id.collected_num);
            holder.img1 = (ImageView) view.findViewById(R.id.goodimg_1);
            holder.img2 = (ImageView) view.findViewById(R.id.goodimg_2);
            holder.img3 = (ImageView) view.findViewById(R.id.goodimg_3);
            initImageView(holder.img1);
            initImageView(holder.img2);
            initImageView(holder.img3);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (!TextUtils.isEmpty(sellerinfo.getSeller_logo())) {
            holder.seller_logo.setVisibility(View.VISIBLE);
            imageloader.setImageRes(holder.seller_logo, sellerinfo.getSeller_logo());
        } else {
            holder.seller_logo.setVisibility(View.INVISIBLE);
        }
        holder.collceted_num.setText(sellerinfo.getFollower() + "人已关注");
        holder.seller_item_shopname.setText(sellerinfo.getSeller_name());
        int size = sellerinfo.getSellergoods().size();
        if (size > 0) {
            holder.img1.setVisibility(View.VISIBLE);
            imageloader.setImageRes(holder.img1, sellerinfo.getSellergoods().get(0).getImg().getThumb());
        } else {
            holder.img1.setVisibility(View.INVISIBLE);
        }
        if (size > 1) {
            holder.img2.setVisibility(View.VISIBLE);
            imageloader.setImageRes(holder.img2, sellerinfo.getSellergoods().get(1).getImg().getThumb());
        } else {
            holder.img2.setVisibility(View.INVISIBLE);
        }
        if (size > 2) {
            holder.img3.setVisibility(View.VISIBLE);
            imageloader.setImageRes(holder.img3, sellerinfo.getSellergoods().get(2).getImg().getThumb());
        } else {
            holder.img3.setVisibility(View.INVISIBLE);
        }
        holder.shop_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShopListActivity.class);
                intent.putExtra(IntentKeywords.MERCHANT_ID, sellerinfo.getId());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);

            }
        });
        return view;
    }

    private class ViewHolder {
        private ImageView img1, img2, img3, seller_logo;
        private TextView seller_item_shopname, collceted_num;
        private LinearLayout shop_item;
    }


    void initImageView(ImageView img) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width / 3 - distance * 2, width / 3 - distance * 2);
        lp.setMargins(distance, distance, distance, distance);
        lp.height = width / 3 - distance * 2;
        img.setLayoutParams(lp);
    }


    public int getDisplayMetricsWidth1() {
        int i = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        int j = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j) - ((int) context.getResources().getDimension(R.dimen.eight_margin)) * 2;
    }
}
