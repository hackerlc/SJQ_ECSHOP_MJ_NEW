package com.ecjia.view.activity.goodsdetail.fragment.interfaces;

import android.content.Context;
import android.view.View;

import com.ecjia.entity.responsemodel.FAVOUR;
import com.ecjia.view.adapter.adapter.CommonAdapter;
import com.ecjia.view.adapter.adapter.base.ViewHolder;
import com.ecmoban.android.sijiqing.R;

import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-05-04.
 */

public class BottomShowShopDiscountAdapter extends CommonAdapter<FAVOUR> {

    public BottomShowShopDiscountAdapter(Context context, List<FAVOUR> datas) {
        super(context, R.layout.item_choosecoupon_text, datas);
    }

    @Override
    protected void convert(ViewHolder holder, FAVOUR favour, int position) {
        holder.setText(R.id.tv_name,(position+1)+favour.getType_label()+ favour.getName());
        holder.setVisibility(R.id.img_select, View.GONE);
    }
}
