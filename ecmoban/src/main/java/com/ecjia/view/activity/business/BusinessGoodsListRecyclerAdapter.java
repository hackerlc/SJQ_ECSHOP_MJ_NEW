package com.ecjia.view.activity.business;

import android.content.Context;

import com.ecjia.entity.model.SimpleGoods;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.view.adapter.adapter.base.ViewHolder;
import com.ecjia.view.adapter.adapter.my.HeaderFooterLoadMoreAdapter;
import com.ecmoban.android.sijiqing.R;

import java.util.List;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/2/10 13:31.
 */

public class BusinessGoodsListRecyclerAdapter extends HeaderFooterLoadMoreAdapter<SimpleGoods> {
    private Context mContext;

    public BusinessGoodsListRecyclerAdapter(Context context, List<SimpleGoods> datas) {
        super(context, R.layout.item_business_goods, datas);
        mContext=context;
    }

    @Override
    public void convert(ViewHolder holder, SimpleGoods simpleGoods, int position) {
        ImageLoaderForLV.getInstance(mContext).setImageRes(holder.getView(R.id.iv_goods_img), simpleGoods.getImg().getThumb());
        holder.setText(R.id.tv_name,simpleGoods.getName())
                .setText(R.id.tv_show_price,simpleGoods.showPrice())
                .setText(R.id.tv_market_price,simpleGoods.getMarket_price());
    }
}
