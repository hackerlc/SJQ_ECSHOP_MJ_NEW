package com.ecjia.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 2016/9/29.
 */
public class GoodsViewPager extends ViewPager {

    private GestureDetector gestureDetector;

    public GoodsViewPager(Context context) {
        super(context);
        gestureDetector = new GestureDetector(new YScrollDetector());

    }

    public GoodsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(new YScrollDetector());

    }


    private boolean isInIgnoredView(MotionEvent ev) {
        Rect rect = new Rect();
        for (View v : mIgnoredViews) {
            v.getGlobalVisibleRect(rect);
            if (rect.contains((int) ev.getX(), (int) ev.getY())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isInIgnoredView(ev)) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void removeIgnoredView(View v) {
        mIgnoredViews.remove(v);
    }

    public void clearIgnoredViews() {
        mIgnoredViews.clear();
    }

    public void addIgnoredView(View v) {
        if (!mIgnoredViews.contains(v)) {
            mIgnoredViews.add(v);
        }
    }

    private List<View> mIgnoredViews = new ArrayList<View>();


    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceY) <= Math.abs(distanceX);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (!isCanScroll) {
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    boolean isCanScroll = true;

    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        if (isCanScroll) {
            super.scrollTo(x, y);
        }
    }

    @Override
    public void scrollBy(int x, int y) {
        if (isCanScroll) {
            super.scrollBy(x, y);
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        super.addOnPageChangeListener(listener);
    }
}
