package com.ecjia.view.activity.shop;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.ecjia.consts.IntentKeywords;
import com.ecjia.entity.model.ShopInfo;
import com.ecjia.entity.model.SimpleGoods;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.view.activity.*;
import com.ecjia.view.adapter.adapter.base.ViewHolder;
import com.ecjia.view.adapter.adapter.my.HeaderFooterLoadMoreAdapter;
import com.ecjia.widgets.PriceImageView;
import com.ecmoban.android.sijiqing.R;

import java.util.List;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/2/17 16:09.
 */

public class ShopListAdapter extends HeaderFooterLoadMoreAdapter<ShopInfo> {
    protected Context mContext;
    protected OnClickFollow mOnClickFollow;

    public ShopListAdapter(Context context, List<ShopInfo> datas, OnClickFollow clickFollow) {
        super(context, R.layout.item_good_shop, datas);
        mContext = context;
        mOnClickFollow = clickFollow;
    }

    @Override
    public void convert(ViewHolder holder, ShopInfo shopInfo, int position) {
        ImageView imageView = holder.getView(R.id.iv_shop_img);
        ImageLoaderForLV.getInstance(mContext).setImageRes(imageView, shopInfo.getShop_logo());
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, com.ecjia.view.activity.ShopListActivity.class);
            intent.putExtra(IntentKeywords.MERCHANT_ID, shopInfo.getShop_id());
            mContext.startActivity(intent);
        });
        PriceImageView priceImageView1 = holder.getView(R.id.iv_goods_img_1);
        PriceImageView priceImageView2 = holder.getView(R.id.iv_goods_img_2);
        PriceImageView priceImageView3 = holder.getView(R.id.iv_goods_img_3);
        priceImageView1.setVisibility(View.INVISIBLE);
        priceImageView2.setVisibility(View.INVISIBLE);
        priceImageView3.setVisibility(View.INVISIBLE);
        for (int i = 0; i < shopInfo.getSeller_goods().size(); i++) {
            SimpleGoods goods = shopInfo.getSeller_goods().get(i);
            switch (i) {
                case 0:
                    priceImageView1.setPrice(goods.getShop_price());
                    ImageLoaderForLV.getInstance(mContext).setImageRes(priceImageView1, goods.getImg().getSmall());
                    priceImageView1.setVisibility(View.VISIBLE);
                    priceImageView1.setOnClickListener(v -> {
                        Intent intent = new Intent();
                        intent.setClass(mContext, GoodsDetailActivity.class);
                        intent.putExtra(IntentKeywords.GOODS_ID, goods.getGoods_id());
                        intent.putExtra(IntentKeywords.OBJECT_ID, goods.getObject_id());
                        intent.putExtra(IntentKeywords.REC_TYPE, goods.getRec_type());
                        mContext.startActivity(intent);
                    });
                    break;
                case 1:
                    priceImageView2.setPrice(goods.getShop_price());
                    ImageLoaderForLV.getInstance(mContext).setImageRes(priceImageView2, goods.getImg().getSmall());
                    priceImageView2.setVisibility(View.VISIBLE);
                    priceImageView2.setOnClickListener(v -> {
                        Intent intent = new Intent();
                        intent.setClass(mContext, GoodsDetailActivity.class);
                        intent.putExtra(IntentKeywords.GOODS_ID, goods.getGoods_id());
                        intent.putExtra(IntentKeywords.OBJECT_ID, goods.getObject_id());
                        intent.putExtra(IntentKeywords.REC_TYPE, goods.getRec_type());
                        mContext.startActivity(intent);
                    });
                    break;
                case 2:
                    priceImageView3.setPrice(goods.getShop_price());
                    ImageLoaderForLV.getInstance(mContext).setImageRes(priceImageView3, goods.getImg().getSmall());
                    priceImageView3.setVisibility(View.VISIBLE);
                    priceImageView3.setOnClickListener(v -> {
                        Intent intent = new Intent();
                        intent.setClass(mContext, GoodsDetailActivity.class);
                        intent.putExtra(IntentKeywords.GOODS_ID, goods.getGoods_id());
                        intent.putExtra(IntentKeywords.OBJECT_ID, goods.getObject_id());
                        intent.putExtra(IntentKeywords.REC_TYPE, goods.getRec_type());
                        mContext.startActivity(intent);
                    });
                    break;
            }
        }

        holder.setText(R.id.iv_shop_name, shopInfo.getShop_name())
                .setText(R.id.tv_new_goods_num, shopInfo.getCoupon_str())
                .setText(R.id.tv_other, shopInfo.showOhter());

        holder.getView(R.id.tv_follow).setOnClickListener(v -> {
            if(mOnClickFollow!=null){
                mOnClickFollow.onclick(position);
            }
        });
    }

    public interface OnClickFollow {
        void onclick(int pos);
    }
}
