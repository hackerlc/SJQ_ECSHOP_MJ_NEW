package com.ecjia.view.activity.goodsdetail.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class PageTwoView extends LinearLayout {
    String TAG = "PageTwoScrollView";
    public float oldY;
    private int t;
    private float oldX;

    public PageTwoView(Context context) {
        super(context);
    }

    public PageTwoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PageTwoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                oldY = ev.getY();
                oldX = ev.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                float Y = ev.getY();
                float Ys = Y - oldY;
                float X = ev.getX();
                float gapHorizontal = X - oldX;

                /** 说明:
                 *如果是横向移动,就让父控件重新获得触摸事件
                 */
                if (Math.abs(gapHorizontal) > 120) {
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                }
                Log.i(TAG, "Ys::" + Ys + "      t::" + t + "   oldt::" + (getChildAt(0).getMeasuredHeight() - getMeasuredHeight()));
                if (Ys > 0 && t == 0) {
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                }

                if (Ys < 0 && t == getChildAt(0).getMeasuredHeight() - getMeasuredHeight()) {
                    getParent().getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

            case MotionEvent.ACTION_UP:
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }
        super.onTouchEvent(ev);
        return super.onTouchEvent(ev);
    }

    private int oldt;

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        this.t = t;
        this.oldt = oldt;

        super.onScrollChanged(l, t, oldl, oldt);
    }


}
