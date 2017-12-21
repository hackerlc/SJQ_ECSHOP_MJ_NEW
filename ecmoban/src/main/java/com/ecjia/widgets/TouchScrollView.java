package com.ecjia.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.ecjia.util.LG;

/**
 * Created by Administrator on 2015/4/17.
 */
public class TouchScrollView extends ScrollView {

    boolean toTop = false;

    public TouchScrollView(Context context) {
        super(context);
    }

    public TouchScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private float xDistance, yDistance, xLast, yLast;

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

                if (this.getScrollY() == 0) {
                    if (curY - xLast < 0 && Math.abs(yDistance) > 0) {//初始位置向上滑
//                        return true;
                    }
                }
            case MotionEvent.ACTION_UP:

                break;

        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {

        if (this.getScrollY() > 0) {
            mListener.setVisible(VISIBLE);
        } else {
            mListener.setVisible(GONE);
        }
        super.onScrollChanged(x, y, oldx, oldy);
    }

    OnTabVisibleChangeListener mListener;

    public void setOnTabVisibleChangeListener(OnTabVisibleChangeListener listener) {
        this.mListener = listener;
    }

    public interface OnTabVisibleChangeListener {
        void setVisible(int visibility);
    }

}
