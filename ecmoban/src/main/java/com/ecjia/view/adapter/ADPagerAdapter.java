package com.ecjia.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

//实现无限翻页功能的PagerAdapter（原理：设置页面最大值为Integer.MAX_VALUE）

public class ADPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<View> views;

    public ADPagerAdapter(Context context, ArrayList<View> views) {
        this.context = context;
        this.views = views;


    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }
}
