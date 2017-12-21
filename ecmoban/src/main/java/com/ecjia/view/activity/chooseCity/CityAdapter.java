package com.ecjia.view.activity.chooseCity;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.base.ECJiaApplication;
import com.ecjia.entity.model.City;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.util.ScreenUtil;
import com.ecjia.view.adapter.adapter.base.ViewHolder;
import com.ecjia.view.adapter.adapter.my.HeaderFooterLoadMoreAdapter;
import com.ecmoban.android.sijiqing.R;

import java.util.List;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/2/20 16:06.
 */

public class CityAdapter extends HeaderFooterLoadMoreAdapter<City> {
    Context mContext;
    public CityAdapter(Context context, List<City> datas) {
        super(context, R.layout.item_city, datas);
        mContext=context;
    }

    @Override
    public void convert(ViewHolder holder, City city, int position) {
        ImageLoaderForLV.getInstance(mContext).setImageRes(holder.getView(R.id.iv_city_img), city.getImgUrl());
        RelativeLayout rlDiv = holder.getView(R.id.rl_div);

        if(ECJiaApplication.initCityName.equals(city.getName())){
            rlDiv.setBackgroundResource(R.drawable.shape_city_back);
        } else {
            rlDiv.setBackgroundColor(0xffffff);
        }

        LinearLayout ll = holder.getView(R.id.ll_label_div);
        //加载标签
        ll.removeAllViews();
        for (String label : city.getLabelArray()) {
            TextView labelTv=new TextView(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, ScreenUtil.dip2px(mContext,12), 0);
            labelTv.setLayoutParams(lp);
            labelTv.setPadding(ScreenUtil.dip2px(mContext,6), 0, ScreenUtil.dip2px(mContext,6), 0);
            labelTv.setBackgroundResource(R.color.main_color_e22664);
            labelTv.setTextColor(mContext.getResources().getColor(R.color.white));
            labelTv.setText(label);
            ll.addView(labelTv);
        }
        holder.setText(R.id.tv_city_name,city.getName())
                .setText(R.id.tv_content,city.getDescription());
    }
}
