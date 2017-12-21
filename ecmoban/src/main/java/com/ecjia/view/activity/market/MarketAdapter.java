package com.ecjia.view.activity.market;

import android.content.Context;

import com.ecjia.entity.model.Market;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.view.adapter.adapter.base.ViewHolder;
import com.ecjia.view.adapter.adapter.my.HeaderFooterLoadMoreAdapter;
import com.ecmoban.android.sijiqing.R;

import java.util.List;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/2/20 15:23.
 */

public class MarketAdapter extends HeaderFooterLoadMoreAdapter<Market> {
    Context mContext;
    public MarketAdapter(Context context, List<Market> datas) {
        super(context, R.layout.item_market, datas);
        mContext=context;
    }

    @Override
    public void convert(ViewHolder holder, Market market, int position) {
        ImageLoaderForLV.getInstance(mContext).setImageRes(holder.getView(R.id.iv_market),
                market.getImage());
        holder.setText(R.id.tv_market_name,market.getName())
                .setText(R.id.tv_market_content,market.getDesc())
                .setText(R.id.tv_market_n,market.getShop_num())
                .setText(R.id.tv_goods_n,market.getGoods_num());
    }
}
