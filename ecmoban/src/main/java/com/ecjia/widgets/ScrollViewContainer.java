package com.ecjia.widgets;

import java.util.Timer;
import java.util.TimerTask;

import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import de.greenrobot.event.EventBus;

/**
 * ������ScrollView������
 * ��������http://dwtedx.com
 *
 * @author chenjing
 */
public class ScrollViewContainer extends RelativeLayout {

    public static final int AUTO_UP = 0;
    public static final int AUTO_DOWN = 1;
    public static final int DONE = 2;
    public static final float SPEED = 6.5f;

    private boolean isMeasured = false;

    private VelocityTracker vt;

    private int mViewHeight;
    private int mViewWidth;

    private View topView;
    private View bottomView;

    private boolean canPullDown;
    private boolean canPullUp;
    public int state = DONE;

    private int mCurrentViewIndex = 0;
    private float mMoveLen;
    private MyTimer mTimer;
    private float mLastY;
    private int mEvents;
    Resources resource;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (mMoveLen != 0) {
                if (state == AUTO_UP) {
                    if (mTextChangeListener != null) {
                        mTextChangeListener.changeText(SC_STATUS.UP);
                    }
                    mMoveLen -= SPEED;
                    if (mMoveLen <= -mViewHeight) {
                        mMoveLen = -mViewHeight;
                        state = DONE;
                        mCurrentViewIndex = 1;
                    }
                } else if (state == AUTO_DOWN) {
                    if (mTextChangeListener != null) {
                        mTextChangeListener.changeText(SC_STATUS.DOWN);
                    }
                    mMoveLen += SPEED;
                    if (mMoveLen >= 0) {
                        mMoveLen = 0;
                        state = DONE;
                        mCurrentViewIndex = 0;
                    }
                } else {
                    mTimer.cancel();
                }
            }
            requestLayout();
        }

    };

    public ScrollViewContainer(Context context) {
        super(context);
        init();
    }

    public ScrollViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollViewContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mTimer = new MyTimer(handler);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (vt == null) {
                    vt = VelocityTracker.obtain();
                } else {
                    vt.clear();
                }
                mLastY = ev.getY();
                vt.addMovement(ev);
                mEvents = 0;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                vt.addMovement(ev);
                if (canPullUp && mCurrentViewIndex == 0 && mEvents == 0) {
                    mMoveLen += (ev.getY() - mLastY);
                    if (mMoveLen > 0) {
                        mMoveLen = 0;
                        mCurrentViewIndex = 0;
                    } else if (mMoveLen < -mViewHeight) {
                        mMoveLen = -mViewHeight;
                        mCurrentViewIndex = 1;
                    }
                    if (mMoveLen < -8) {
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                } else if (canPullDown && mCurrentViewIndex == 1 && mEvents == 0) {
                    mMoveLen += (ev.getY() - mLastY);
                    if (mMoveLen < -mViewHeight) {
                        mMoveLen = -mViewHeight;
                        mCurrentViewIndex = 1;
                    } else if (mMoveLen > 0) {
                        mMoveLen = 0;
                        mCurrentViewIndex = 0;
                    }
                    if (mMoveLen > 8 - mViewHeight) {
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                } else {
                    mEvents++;
                }
                mLastY = ev.getY();
                requestLayout();
                break;
            case MotionEvent.ACTION_UP:
                mLastY = ev.getY();
                vt.addMovement(ev);
                vt.computeCurrentVelocity(700);
                float mYV = vt.getYVelocity();
                if (mMoveLen == 0 || mMoveLen == -mViewHeight) {
                    break;
                }
                if (Math.abs(mYV) < 500) {
                    if (mMoveLen <= -mViewHeight / 3) {
                        state = AUTO_UP;
                    } else if (mMoveLen > -mViewHeight / 3) {
                        state = AUTO_DOWN;
                    }
                } else {
                    if (mYV < 0) {
                        state = AUTO_UP;
                    } else {
                        state = AUTO_DOWN;
                    }
                }
                mTimer.schedule(2);
                try {
                    if (vt != null) {
                        vt.recycle();
                        vt = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
        super.dispatchTouchEvent(ev);
        //始终分发
        return true;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    OnTextChangeListener mTextChangeListener;

    public void setOnTextChangeListener(OnTextChangeListener textChangeListener) {
        mTextChangeListener = textChangeListener;
    }

    public void autoUp() {
        mMoveLen = -mViewHeight / 2;
        mTimer.cancel();
        state = AUTO_UP;
        mCurrentViewIndex = 1;
        mTimer.schedule(2);
        try {
            if (vt != null) {
                vt.recycle();
                vt = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mTextChangeListener != null) {
            mTextChangeListener.changeText(SC_STATUS.UP);
        }
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        topView.layout(0, (int) mMoveLen, mViewWidth, topView.getMeasuredHeight() + (int) mMoveLen);
        bottomView.layout(0, topView.getMeasuredHeight() + (int) mMoveLen, mViewWidth, topView.getMeasuredHeight() +
                (int) mMoveLen + bottomView.getMeasuredHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        if (!isMeasured) {
            isMeasured = true;
            mViewWidth = getMeasuredWidth();
            topView = getChildAt(0);
            bottomView = getChildAt(1);
            bottomView.setOnTouchListener(bottomViewTouchListener);
            topView.setOnTouchListener(topViewTouchListener);
        }
    }

    private OnTouchListener topViewTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ScrollView sv = (ScrollView) v;
            if (sv.getScrollY() == (sv.getChildAt(0).getMeasuredHeight() - sv.getMeasuredHeight()) &&
                    mCurrentViewIndex == 0) {
                canPullUp = true;
            } else {
                canPullUp = false;
            }
            return false;
        }
    };
    private OnTouchListener bottomViewTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ScrollView sv = (ScrollView) v;
            if (sv.getScrollY() == 0 && mCurrentViewIndex == 1) {
                canPullDown = true;
            } else {
                canPullDown = false;
            }
            return false;
        }
    };

    class MyTimer {
        private Handler handler;
        private Timer timer;
        private MyTask mTask;

        public MyTimer(Handler handler) {
            this.handler = handler;
            timer = new Timer();
        }

        public void schedule(long period) {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
            mTask = new MyTask(handler);
            timer.schedule(mTask, 0, period);
        }

        public void cancel() {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
        }

        class MyTask extends TimerTask {
            private Handler handler;

            public MyTask(Handler handler) {
                this.handler = handler;
            }

            @Override
            public void run() {
                handler.obtainMessage().sendToTarget();
            }

        }
    }

    public enum SC_STATUS {
        UP, DOWN
    }

    public interface OnTextChangeListener {
        void changeText(SC_STATUS type);
    }

    public void scrollToPageOne(){

    }
}
