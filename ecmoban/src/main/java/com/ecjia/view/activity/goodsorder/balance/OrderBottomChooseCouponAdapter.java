package com.ecjia.view.activity.goodsorder.balance;

import android.content.Context;
import android.view.View;

import com.ecjia.entity.responsemodel.GOODSCOUPONS;
import com.ecjia.view.activity.goodsorder.balance.entity.NewBlanceShopData;
import com.ecjia.view.activity.goodsorder.balance.entity.ShopCouponsData;
import com.ecjia.view.adapter.adapter.CommonAdapter;
import com.ecjia.view.adapter.adapter.base.ViewHolder;
import com.ecmoban.android.sijiqing.R;

import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-04-01.
 */

public class OrderBottomChooseCouponAdapter extends CommonAdapter<ShopCouponsData> {

    public OrderBottomChooseCouponAdapter(Context context, List<ShopCouponsData> datas) {
        super(context, R.layout.item_choosecoupon_text, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ShopCouponsData shopCouponsData, int position) {
        holder.setText(R.id.tv_name, "省" + shopCouponsData.getCou_money() + ":" + shopCouponsData.getCou_name());
        if (shopCouponsData.isSelect()) {
            holder.setVisibility(R.id.img_select, View.VISIBLE);
        } else {
            holder.setVisibility(R.id.img_select, View.GONE);
        }
    }
}
