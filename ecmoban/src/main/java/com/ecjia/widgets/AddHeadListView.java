package com.ecjia.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.ecjia.util.LG;

/**
 * Created by Administrator on 2015/7/28.
 */
public class AddHeadListView extends XListView{


    private View view=null;
    private int distance=0;

    public AddHeadListView(Context context) {
        super(context);
    }

    public AddHeadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AddHeadListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LG.i("this.getScrollY()===" + this.getScrollY());
        if(distance>0&&view!=null) {
            if (this.getScrollY() > distance) {
                view.setVisibility(VISIBLE);
            } else {
                view.setVisibility(GONE);
            }
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void addHeadView(View view){
        this.view=view;
    }

    public void removeHeadView(){
        this.view=null;
    }

    public void setDistance(int distance){
        this.distance=distance;
    }

}
