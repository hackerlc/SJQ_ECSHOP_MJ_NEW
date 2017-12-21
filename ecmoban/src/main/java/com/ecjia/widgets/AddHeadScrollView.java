package com.ecjia.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.ecjia.util.LG;

/**
 * Created by Administrator on 2015/7/28.
 */
public class AddHeadScrollView extends ScrollView {

    public AddHeadScrollView(Context context) {
        super(context);
    }

    public AddHeadScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AddHeadScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private View view;
    private int distance;
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                xDistance = yDistance = 0f;
//                xLast = ev.getX();
//                yLast = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                final float curX = ev.getX();
//                final float curY = ev.getY();
//
//                xDistance += Math.abs(curX - xLast);
//                yDistance += Math.abs(curY - yLast);
//                xLast = curX;
//                yLast = curY;
//
//                if (this.getScrollY() ==0) {
//                    if (curY - xLast < 0 && Math.abs(yDistance) > 0) {//初始位置向上滑
//                        return true;
//                    }
//                }
//            case MotionEvent.ACTION_UP:
//
//                break;
//
//        }
//
//        return super.onInterceptTouchEvent(ev);
//    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        LG.i("this.getScrollY()==="+this.getScrollY());
        if(this.getScrollY()>distance) {
            view.setVisibility(VISIBLE);
        }else{
            view.setVisibility(GONE);
        }
        super.onScrollChanged(x, y, oldx, oldy);
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
