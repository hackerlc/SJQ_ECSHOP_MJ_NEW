package com.ecjia.view.activity.goodsdetail.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.ecmoban.android.sijiqing.R;


public class ScrollViewWrapper extends ScrollView {

    String TAG = "ScrollViewWrapper2";

    private int height;//设定控件的高度
    private View pageOne;
    private View pageTwo;
    private boolean isSetted = false;
    private boolean ispageOne = true;
    private OnScrollViewChangeListener listener = null;
    private float oldY;
    private float oldX;

    public ScrollViewWrapper(Context context) {
        super(context);
        init(context, null, 0);

    }

    public ScrollViewWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);

    }

    public ScrollViewWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollViewWrapper);
        int padding = a.getInteger(R.styleable.ScrollViewWrapper_wrapper_padding, 0);
        height = Utils.getScreenHeight(context) - Utils.getStatusBarHeight(context) - Utils.dp2px(context, padding);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isSetted) {
            LinearLayout wrapper = (LinearLayout) getChildAt(0);

            pageOne = wrapper.getChildAt(0);
            pageTwo = wrapper.getChildAt(1);

            pageOne.getLayoutParams().height = height;
            pageTwo.getLayoutParams().height = height;
            isSetted = true;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo(0, 0);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        //所有事件都分发掉
        return true;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /**
         * 当 第一页，而且，第一页滚到底
         */
        super.onInterceptTouchEvent(ev);
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        LOG.i(TAG + "::onTouchEvent " + ev.getAction());
//        int action = ev.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_UP:
//                Log.i(TAG + "::onTouchEvent ACTION_UP", ispageOne + "getScrollY:::" + getScrollY());
//                int scrollY = getScrollY();
//                //滑动距离,然后才翻页
//                int creteria = height / 5;
//                if (ispageOne) {
//                    if (scrollY <= creteria) {
//                        //返回PageOne
//                        this.smoothScrollTo(0, 0);
//                        if (listener != null) {
//                            listener.change(0);
//                        }
//                    } else {
//                        //显示PageTwo
//                        this.smoothScrollTo(0, height);
//                        this.setFocusable(false);
//                        ispageOne = false;
//                        //返回PageTwo
//                        if (listener != null) {
//                            listener.change(1);
//                        }
//                    }
//                } else {
//                    int pading = height - scrollY;
//                    if (pading >= creteria) {
//                        this.smoothScrollTo(0, 0);
//                        ispageOne = true;
//                        if (listener != null) {
//                            listener.change(0);
//                        }
//                    } else {
//                        this.smoothScrollTo(0, height);
//                        //返回PageOne
//                        if (listener != null) {
//                            listener.change(1);
//                        }
//                    }
//                }
//                return false;
//        }


        if (!isCanHorizentalScroll) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
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
                        return true;
                    }
            }
            super.onTouchEvent(ev);
            return true;
        }
        return super.onTouchEvent(ev);
    }

    public void setOnScrollChangeListener(OnScrollViewChangeListener listener) {
        this.listener = listener;
    }

    public void closeMenu() {
        if (ispageOne) return;
        this.smoothScrollTo(0, 0);
        ispageOne = true;
    }

    public void openMenu() {
        if (!ispageOne) return;
        this.smoothScrollTo(0, height);
        ispageOne = false;
    }


    public void scrollToPageOne() {
        /**
         * 移动到第一页
         */
        Log.i(TAG, "移动到第一页");
        this.post(new Runnable() {
            @Override
            public void run() {
                smoothScrollTo(0, 0);
                ispageOne = true;
                isCanHorizentalScroll = true;
                if (listener != null) {
                    listener.change(0);
                }
                invalidate();
            }
        });
    }


    public void scrollToPageTwo() {
        /**
         * 移动到第一页
         */
        Log.i(TAG, "移动到第二页");
        this.post(new Runnable() {
            @Override
            public void run() {
                smoothScrollTo(0, height);
                ispageOne = false;
                isCanHorizentalScroll = false;
                if (listener != null) {
                    listener.change(1);
                }
                invalidate();
            }
        });
    }

    /**
     * 是否可以水平滑动
     */
    public boolean isCanHorizentalScroll = true;

    public int getScrollHeight() {
        Log.i(TAG, ":::height" + height);
        return height;
    }

    @Override
    public void fling(int velocityY) {
        super.fling((int) (velocityY / 1.5));
    }
}
