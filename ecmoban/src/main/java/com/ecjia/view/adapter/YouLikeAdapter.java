package com.ecjia.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

/**
 * Created by Adam on 2015/1/20.
 */
public class YouLikeAdapter extends PagerAdapter {
    private Context context;
    List<View> pagerList;

 public YouLikeAdapter(Context context,List<View> pagerList){
    this.context=context;
    this.pagerList=pagerList;
}
    //销毁position位置的界面
    @Override
    public void destroyItem(ViewGroup container, int position, Object arg2) {

    }
    //获取当前窗体界面数
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Integer.MAX_VALUE;
    }

    //初始化position位置的界面
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        //对ViewPager页号求模取出View列表中要显示的项
        position %= pagerList.size();
        if (position<0){
            position = pagerList.size()+position;
        }
        View view = pagerList.get(position);
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp =view.getParent();
        if (vp!=null){
            ViewGroup parent = (ViewGroup)vp;
            parent.removeView(view);
        }
        container.addView(view);
        return pagerList.get(position % pagerList.size());
    }
    // 判断是否由对象生成界面
    @Override
    public boolean isViewFromObject(View v, Object arg1) {
        // TODO Auto-generated method stub
        return v == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return super.getItemPosition(object);
    }

}
