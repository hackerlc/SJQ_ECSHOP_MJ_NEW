package com.ecjia.view.fragment.snatch;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ecjia.entity.model.SnatchGoods;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.view.adapter.adapter.base.ViewHolder;
import com.ecjia.view.adapter.adapter.my.HeaderFooterLoadMoreAdapter;
import com.ecmoban.android.sijiqing.R;

import java.util.List;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/2/15 10:28.
 */

public class SnatchWholesaleAdapter extends HeaderFooterLoadMoreAdapter<SnatchGoods>{
    private Context mContext;
        private boolean istoday;
    public SnatchWholesaleAdapter(Context context, List<SnatchGoods> datas,boolean istoday) {
        super(context, R.layout.item_snatch_wholesale, datas);
        mContext=context;
        this.istoday=istoday;
    }

    @Override
    public void convert(ViewHolder holder, SnatchGoods simpleGoods, int position) {
        ImageLoaderForLV.getInstance(mContext).setImageRes(holder.getView(R.id.iv_goods_img), simpleGoods.getImg().getThumb());
        holder.setText(R.id.tv_goods_name,simpleGoods.getName())
        .setText(R.id.tv_show_price,simpleGoods.getShop_price())
        .setText(R.id.tv_remaining_num,simpleGoods.getRemaining());

        ProgressBar num = holder.getView(R.id.pb_num);
        num.setMax(simpleGoods.getAll_num());
        num.setProgress(simpleGoods.getBuy_num());

        TextView view = holder.getView(R.id.tv_tag_price);
        view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        view.setText(simpleGoods.getMarket_price());

        Button btn =holder.getView(R.id.btn_enter);
        Button btn2=holder.getView(R.id.btn_enter2);
        if(istoday){
            btn.setVisibility(View.GONE);
            btn2.setVisibility(View.VISIBLE);
        }else{
            btn.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.GONE);
        }
    }
}
