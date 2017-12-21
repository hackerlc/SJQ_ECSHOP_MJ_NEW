package com.ecjia.view.activity.webwholesale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import com.ecjia.consts.IntentKeywords;
import com.ecjia.entity.model.SimpleGoods;
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

public class WebWholesaleAdapter extends HeaderFooterLoadMoreAdapter<SimpleGoods> {
    private boolean isShowMarketPrice = false;
    private Context mContext;

    public WebWholesaleAdapter(Context context, List<SimpleGoods> datas) {
        this(context,datas,false);
    }
    public WebWholesaleAdapter(Context context, List<SimpleGoods> datas, boolean isShow) {
        super(context, R.layout.item_substation_goods, datas);
        mContext=context;
        isShowMarketPrice = isShow;
    }

    @Override
    public void convert(ViewHolder holder, SimpleGoods simpleGoods, int position) {
        ImageLoaderForLV.getInstance(mContext).setImageRes(holder.getView(R.id.goods_img_left), simpleGoods.getImg().getThumb());
        AutoReturnView text=holder.getView(R.id.goodlist_goodname_left);
        text.setContent(simpleGoods.getName());
        holder.setText(R.id.goodlist_shop_price_left,simpleGoods.showPrice());
        TextView origin = holder.getView(R.id.goodlist_origin_price_left);
        if(isShowMarketPrice) {
            origin.setText(simpleGoods.getMarket_price());
            origin.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            origin.setVisibility(View.VISIBLE);
        } else {
            origin.setVisibility(View.GONE);
        }

        holder.getView(R.id.ll_goods_item_left).setOnClickListener(v -> {
            Intent intent = new Intent(mContext, GoodsDetailActivity.class);
            intent.putExtra(IntentKeywords.GOODS_ID, simpleGoods.getGoods_id());
            intent.putExtra(IntentKeywords.OBJECT_ID, simpleGoods.getObject_id());
            intent.putExtra(IntentKeywords.REC_TYPE, simpleGoods.getRec_type());
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        });
    }
}
