package com.ecjia.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Adam on 2016-03-22.
 */
public class ScrollView_Main extends ScrollView {
    public ScrollView_Main(Context context) {
        super(context);
    }

    public ScrollView_Main(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollView_Main(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public interface OnScrollListener {
        void onScroll(int l, int t, int oldl, int oldt);
    }

    /**
     * Scroll细致监听器
     */
    private OnScrollListener mScrollListener;

    /**
     * 功能描述: 设置Scroll滚动监听器<br>
     *
     * @param listener
     */
    public void setOnScrollListener(OnScrollListener listener) {
        mScrollListener = listener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (mScrollListener != null) {
            mScrollListener.onScroll(l, t, oldl, oldt);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
