package com.ecjia.view.activity.together;

import android.content.Context;

import com.ecjia.entity.model.GroupGoods;
import com.ecjia.util.DateManager;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.view.adapter.adapter.base.ViewHolder;
import com.ecjia.view.adapter.adapter.my.HeaderFooterLoadMoreAdapter;
import com.ecjia.widgets.IndicatorProgressBar;
import com.ecmoban.android.sijiqing.R;

import java.util.List;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/2/15 17:00.
 */

public class TogetherWholesaleAdapter extends HeaderFooterLoadMoreAdapter<GroupGoods> {
    private Context mContext;
    public TogetherWholesaleAdapter(Context context, List<GroupGoods> datas) {
        super(context, R.layout.item_together_wholesale, datas);
        mContext = context;
    }

    @Override
    public void convert(ViewHolder holder, GroupGoods simpleGoods, int position) {
        ImageLoaderForLV.getInstance(mContext).setImageRes(holder.getView(R.id.iv_goods_img_1), simpleGoods.getImg1());
        ImageLoaderForLV.getInstance(mContext).setImageRes(holder.getView(R.id.iv_goods_img_2), simpleGoods.getImg2());
        ImageLoaderForLV.getInstance(mContext).setImageRes(holder.getView(R.id.iv_goods_img_3), simpleGoods.getImg3());

        holder.setText(R.id.tv_goods_name,simpleGoods.getName());
        holder.setText(R.id.tv_over_time, DateManager.formatDateTime(simpleGoods.getLeft_second()));
        //设置进度条
        IndicatorProgressBar ipPrice = holder.getView(R.id.ip_price);
        ipPrice.setSection(simpleGoods.getSections());
        ipPrice.setBuyNum(simpleGoods.getBuy_num());
        ipPrice.setProgress();
        holder.setText(R.id.tv_goods_name, simpleGoods.getName());
    }
}
