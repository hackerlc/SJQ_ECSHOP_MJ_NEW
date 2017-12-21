package com.ecjia.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.ecjia.util.LG;

/**
 * Created by Administrator on 2015/12/30.
 */
public class MyHorizontalScrollView extends HorizontalScrollView {
    private View actionview;

    public MyHorizontalScrollView(Context context) {
        super(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    public View getAction() {
        return actionview;
    }

    public void setAction(View action) {
        this.actionview = action;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LG.e("event.getAction()=="+event.getAction());
        if (mListener != null&&mListener.getShowView()) {
            mListener.setShowView(false);
            if(getScrollX()>0){
                smoothScrollTo(0, 0);
            }else {
                mListener.onStatusChange();
            }
            return true;//触摸的是item是当前已显示的item
        }
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

            case MotionEvent.ACTION_CANCEL:
                smoothScrollTo(0,0);

            case MotionEvent.ACTION_UP:
                // 获得ViewHolder
                // 获得HorizontalScrollView滑动的水平方向值.
                int scrollX = getScrollX();

                // 获得操作区域的长度
                int actionW = actionview.getWidth();

                // 注意使用smoothScrollTo,这样效果看起来比较圆滑,不生硬
                // 如果水平方向的移动值<操作区域的长度的一半,就复原
                if (scrollX < actionW / 2) {
                    smoothScrollTo(0, 0);
                    mListener.setShowView(false);
                } else// 否则的话显示操作区域
                {
                    smoothScrollTo(actionW, 0);
                    if (mListener != null) {
                        mListener.setShowView(true);
                    }
                }
                return true;
        }

        return super.onTouchEvent(event);
    }

    private onStatusListener mListener = null;

    public void onStatusChangeListener(onStatusListener listener) {
        mListener = listener;
    }

    public interface onStatusListener {
        void onStatusChange();

        void setShowView(boolean isShow);

        boolean getShowView();
    }
}
