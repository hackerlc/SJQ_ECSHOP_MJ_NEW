package com.ecjia.view.activity.shopdetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.ecjia.consts.IntentKeywords;
import com.ecjia.entity.responsemodel.SIMPLEGOODS;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.view.adapter.adapter.base.ViewHolder;
import com.ecjia.view.adapter.adapter.my.HeaderFooterLoadMoreAdapter;
import com.ecjia.widgets.AutoReturnView;
import com.ecmoban.android.sijiqing.R;

import java.util.List;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/2/10 13:31.
 */

public class GoodsListAdapter extends HeaderFooterLoadMoreAdapter<SIMPLEGOODS> {
    private Context mContext;

    public GoodsListAdapter(Context context, List<SIMPLEGOODS> datas) {
        super(context, R.layout.item_substation_goods, datas);
        mContext=context;
    }

    @Override
    public void convert(ViewHolder holder, SIMPLEGOODS simpleGoods, int position) {
        ImageLoaderForLV.getInstance(mContext).setImageRes(holder.getView(R.id.goods_img_left), simpleGoods.getImg().getThumb());
        AutoReturnView text=holder.getView(R.id.goodlist_goodname_left);
        text.setContent(simpleGoods.getName());
        holder.setText(R.id.goodlist_shop_price_left,simpleGoods.getPromote_price());
        holder.getView(R.id.goodlist_origin_price_left).setVisibility(View.GONE);

        holder.getView(R.id.ll_goods_item_left).setOnClickListener(v -> {
            Intent intent = new Intent(mContext, GoodsDetailActivity.class);
            intent.putExtra(IntentKeywords.GOODS_ID, simpleGoods.getGoods_id());
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        });
    }
}
