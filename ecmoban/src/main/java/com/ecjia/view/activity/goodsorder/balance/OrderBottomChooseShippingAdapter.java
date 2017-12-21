package com.ecjia.view.activity.goodsorder.balance;

import android.content.Context;
import android.view.View;

import com.ecjia.entity.responsemodel.BONUS;
import com.ecjia.entity.responsemodel.SHIPPING;
import com.ecjia.view.adapter.adapter.CommonAdapter;
import com.ecjia.view.adapter.adapter.base.ViewHolder;
import com.ecmoban.android.sijiqing.R;

import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-04-01.
 */

public class OrderBottomChooseShippingAdapter extends CommonAdapter<SHIPPING> {

    public OrderBottomChooseShippingAdapter(Context context, List<SHIPPING> datas) {
        super(context, R.layout.item_choosecoupon_text, datas);
    }

    @Override
    protected void convert(ViewHolder holder, SHIPPING shipping, int position) {
        holder.setText(R.id.tv_name, shipping.getShipping_name());
        if ("1".equals(shipping.getIs_use())) {
            holder.setVisibility(R.id.img_select, View.VISIBLE);
        } else {
            holder.setVisibility(R.id.img_select, View.GONE);
        }
    }
}
