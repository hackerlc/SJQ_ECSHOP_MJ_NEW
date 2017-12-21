package com.ecjia.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2015/4/17.
 */
public class FloatTabScrollView extends ScrollView {

    boolean toTop = false;

    public FloatTabScrollView(Context context) {
        super(context);
    }

    public FloatTabScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatTabScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private float xDistance, yDistance, xLast, yLast;
    public LinearLayout top_tab;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                if (yDistance > xDistance && getScrollY() == 0) {
                    return true;   //y=0并且向上滑动
                }

        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        if (this.getScrollY() > 0) {
            top_tab.setVisibility(VISIBLE);
        } else {
            top_tab.setVisibility(GONE);
        }
        super.onScrollChanged(x, y, oldx, oldy);
    }
}
