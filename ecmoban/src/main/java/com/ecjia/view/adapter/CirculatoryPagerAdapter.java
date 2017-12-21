package com.ecjia.view.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;

//实现无限翻页功能的PagerAdapter（原理：设置页面最大值为Integer.MAX_VALUE）

public class CirculatoryPagerAdapter extends PagerAdapter {

    public ArrayList<View> viewlist;
    View view;
    public CirculatoryPagerAdapter(ArrayList<View> viewlist) {
        this.viewlist = viewlist;
    }

    @Override
    public int getCount() {    //设置成最大，使用户看不到边界

        if(viewlist.size()==0){
            return 0;
        }
        return viewlist.size() == 1 ? 1 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {

        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //Warning：不要在这里调用removeView
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //对ViewPager页号求模取出View列表中要显示的项

        if(viewlist.size() == 0){
            return null;
        }
        if (viewlist.size() == 1) {
            view = viewlist.get(position);
            container.addView(view);
            return view;
        }
        position %= viewlist.size();
        if (position < 0) {
            position = viewlist.size() + position;
        }

        view = viewlist.get(position);
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp = view.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(view);
        }

        container.addView(view);
        //add listeners here if necessary
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
