package com.ecjia.view.activity.goodsdetail.adapter;

import android.content.Context;
import android.view.View;

import com.ecjia.consts.OrderType;
import com.ecjia.entity.responsemodel.GOODSCOUPONS;
import com.ecjia.entity.responsemodel.USERBONUS;
import com.ecjia.view.adapter.adapter.CommonAdapter;
import com.ecjia.view.adapter.adapter.base.ViewHolder;
import com.ecmoban.android.sijiqing.R;

import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-31.
 */

public class ReceiveCouponsAdapter extends CommonAdapter<GOODSCOUPONS> {



    public ReceiveCouponsAdapter(Context context, List<GOODSCOUPONS> datas) {
        super(context, R.layout.item_userbonus, datas);
    }

    @Override
    protected void convert(ViewHolder holder, GOODSCOUPONS goodscoupons, int position) {
        holder.setVisibility(R.id.coupon_lv_item_checked, View.INVISIBLE);
        holder.setText(R.id.coupon_lv_item_value,goodscoupons.getCou_money()+"");
        holder.setText(R.id.coupon_lv_item_condition,"满"+goodscoupons.getCou_man()+"减"+goodscoupons.getCou_money());
        holder.setText(R.id.coupon_lv_item_type,"立即领取");
        holder.setText(R.id.coupon_lv_item_time,goodscoupons.getCou_start_time()+"-"+goodscoupons.getCou_end_time());
        //"can_receive": 1//可领取状态 0为可领取，1为已领取，2为已领完
        if("0".equals(goodscoupons.getCan_receive())){//可使用
            holder.setText(R.id.coupon_lv_item_type,"立即领取");
            holder.setTextColor(R.id.coupon_lv_item_type,R.color._333333);
            holder.setBackgroundRes(R.id.coupon_lv_item_flag,R.drawable.bg_bouns_item);
        }else if("1".equals(goodscoupons.getCan_receive())){//1为已领取
            holder.setText(R.id.coupon_lv_item_type,"已领取");
            holder.setTextColor(R.id.coupon_lv_item_type,R.color._999999);
            holder.setBackgroundRes(R.id.coupon_lv_item_flag,R.drawable.bg_bouns_item_gray);
        }else if("2".equals(goodscoupons.getCan_receive())){//已领完
            holder.setText(R.id.coupon_lv_item_type,"已领完");
            holder.setTextColor(R.id.coupon_lv_item_type,R.color._999999);
            holder.setBackgroundRes(R.id.coupon_lv_item_flag,R.drawable.bg_bouns_item_gray);
        }else{//已领完
            holder.setText(R.id.coupon_lv_item_type,"已领完");
            holder.setTextColor(R.id.coupon_lv_item_type,R.color._999999);
            holder.setBackgroundRes(R.id.coupon_lv_item_flag,R.drawable.bg_bouns_item_gray);
        }
    }
}
