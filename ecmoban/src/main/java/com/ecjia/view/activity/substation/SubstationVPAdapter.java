package com.ecjia.view.activity.substation;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.entity.model.City;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.base.ECJiaApplication;
import com.ecmoban.android.sijiqing.R;

import java.util.ArrayList;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/2/8 17:00.
 */

public class SubstationVPAdapter extends PagerAdapter {

    ArrayList<City> data;
    Context mContext ;
    onItemClick mOnItemClick;

    public SubstationVPAdapter(Context context,ArrayList<City> cities,onItemClick onItemClick){
        data=cities;
        mContext=context;
        mOnItemClick=onItemClick;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_substation,container,false);
        City city=data.get(position);
        ImageView img=(ImageView)view.findViewById(R.id.iv_img);
        TextView title =(TextView)view.findViewById(R.id.tv_title);
        RelativeLayout btn = (RelativeLayout)view.findViewById(R.id.rl_enter);
        ImageLoaderForLV.getInstance(mContext).setImageRes(img, city.getImgUrl());
        title.setText(city.getName());

        btn.setOnClickListener(v -> {
//            DEVICE.getInstance().setCity(city.getAlias());
//            ECJiaApplication.cityName = city.getName();
            mOnItemClick.onClick(v,position);
        });
        container.addView(view);
        return view;
    }

    public interface onItemClick{
        void onClick(View v,int pos);
    }

    public void setOnItemClick(onItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }
}
