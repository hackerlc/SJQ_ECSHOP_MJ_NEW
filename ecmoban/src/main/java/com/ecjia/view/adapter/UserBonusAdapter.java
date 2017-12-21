package com.ecjia.view.adapter;

import android.content.Context;
import android.view.View;

import com.ecjia.consts.OrderType;
import com.ecjia.entity.responsemodel.CATEGORY;
import com.ecjia.entity.responsemodel.USERBONUS;
import com.ecjia.view.adapter.adapter.CommonAdapter;
import com.ecjia.view.adapter.adapter.base.ViewHolder;
import com.ecmoban.android.sijiqing.R;

import java.util.Date;
import java.util.List;

/**
 * 类名介绍：红包页面Adapter
 * Created by sun
 * Created time 2017-03-02.
 */

public class UserBonusAdapter extends CommonAdapter<USERBONUS> {
    private int[] bgs = {R.drawable.coupon_item_bg_1, R.drawable.coupon_item_bg_2, R.drawable.coupon_item_bg_3, R.drawable.coupon_item_bg_4};

    public UserBonusAdapter(Context context, List<USERBONUS> datas) {
        super(context, R.layout.item_userbonus, datas);
    }

    @Override
    protected void convert(ViewHolder holder, USERBONUS userbonus, int position) {
        holder.setVisibility(R.id.coupon_lv_item_checked, View.INVISIBLE);
        holder.setBackgroundRes(R.id.coupon_lv_item_rl,bgs[position % 4]);

        holder.setText(R.id.coupon_lv_item_value,userbonus.getBonus_amount());
        holder.setText(R.id.coupon_lv_item_condition,"需消费总额超过"+userbonus.getRequest_amount()+"使用");
        holder.setText(R.id.coupon_lv_item_type,"全场通用");

        holder.setText(R.id.coupon_lv_item_starttime,userbonus.getFormatted_start_date());
        holder.setText(R.id.coupon_lv_item_endtime,userbonus.getFormatted_end_date());

        if(OrderType.ALLOW_USE.equals(userbonus.getStatus())){//可使用
            holder.setVisibility(R.id.coupon_lv_item_flag, View.GONE);
        }else if(OrderType.EXPIRED.equals(userbonus.getStatus())){
            holder.setVisibility(R.id.coupon_lv_item_flag, View.VISIBLE);
            holder.setBackgroundRes(R.id.coupon_lv_item_flag,R.drawable.coupon_item_expired);
        }else if(OrderType.IS_USED.equals(userbonus.getStatus())){
            holder.setVisibility(R.id.coupon_lv_item_flag, View.VISIBLE);
            holder.setBackgroundRes(R.id.coupon_lv_item_flag,R.drawable.coupon_item_used);
        }else{
            holder.setVisibility(R.id.coupon_lv_item_flag, View.GONE);
        }

    }
}
