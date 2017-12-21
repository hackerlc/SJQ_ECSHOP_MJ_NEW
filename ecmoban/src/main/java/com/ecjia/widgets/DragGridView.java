package com.ecjia.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.util.LG;

import java.util.ArrayList;
import java.util.Collections;

public class DragGridView extends ViewGroup implements View.OnTouchListener,
        View.OnClickListener, View.OnLongClickListener {
    // layout vars
    protected int colCount;
    int childWidth;
    int chilHeight;
    int padding;
    int dpi;
    int scroll = 0;
    protected float lastDelta = 0;
    protected Handler handler = new Handler();
    // dragging vars
    protected int dragged = -1, lastX = -1, lastY = -1, lastTarget = -1;
    protected boolean enabled = true;//是否可以点击
    protected boolean touching = false;//手在控件上
    // anim vars
    public static final int animT = 100;//动画执行时间
    protected ArrayList<Integer> newPositions = new ArrayList<Integer>();
    // listeners
    protected OnRearrangeListener onRearrangeListener;
    protected OnClickListener secondaryOnClickListener;
    private AdapterView.OnItemClickListener onItemClickListener;
    private String TAG = "DragGridView";


    private int screenWidth;

    public DragGridView(Context context) {
        this(context, null);
    }

    // CONSTRUCTOR AND HELPERS
    public DragGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setListeners();
        handler.removeCallbacks(updateTask);
        handler.postAtTime(updateTask, SystemClock.uptimeMillis() + 500);
        setChildrenDrawingOrderEnabled(true);

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        dpi = metrics.densityDpi;
        screenWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        chilHeight = (int) context.getResources().getDimension(R.dimen.dp_48);
        padding = 0;
        colCount = 1;
        childWidth = screenWidth - 2 * padding;
    }

    /**
     * 拖动item的接口
     */
    public interface OnRearrangeListener {

        void onRearrange(int oldIndex, int newIndex);
    }


    protected void setListeners() {
        setOnTouchListener(this);
        super.setOnClickListener(this);
        setOnLongClickListener(this);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        secondaryOnClickListener = l;
    }

    protected Runnable updateTask = new Runnable() {
        public void run() {
            if (dragged != -1) {
                if (lastY < padding * 3 && scroll > 0) {
                    scroll -= 20;
                } else if (lastY > getBottom() - getTop() - (padding * 3) && scroll < getMaxScroll()) {
                    scroll += 20;
                }
            } else if (lastDelta != 0 && !touching) {
                scroll += lastDelta;
                lastDelta *= .9;
                if (Math.abs(lastDelta) < .25) {
                    lastDelta = 0;
                }
            }
            clampScroll();
            mlayout(getLeft(), getTop(), getRight(), getBottom());
            handler.postDelayed(this, 25);
        }
    };

    @Override
    public void addView(View child) {
        super.addView(child);
        newPositions.add(-1);
    }


    @Override
    public void removeViewAt(int index) {
        super.removeViewAt(index);
        newPositions.remove(index);
    }

    ;

    // LAYOUT
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.getTag().equals("tag")) {
                /**
                 * 获取tag数据位置
                 */
                if (mTagListener != null) {
                    mTagListener.getTagPosition(i);
                    LG.i("mTagListener:" + i);
                }
            }
            if (i != dragged) {
                Point xy = getCoorFromIndex(i);
                view.layout(xy.x, xy.y, xy.x + childWidth, xy.y + chilHeight);
            }
        }
    }


    // LAYOUT
    protected void mlayout(int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.getTag().equals("tag")) {
                /**
                 * 获取tag数据位置
                 */
                if (mTagListener != null) {
                    mTagListener.getTagPosition(i);
                    LG.i("mTagListener:" + i);
                }
            }
            if (i != dragged) {
                Point xy = getCoorFromIndex(i);
                view.layout(xy.x, xy.y, xy.x + childWidth, xy.y + chilHeight);
            }
        }
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (dragged == -1)
            return i;
        else if (i == childCount - 1)
            return dragged;
        else if (i >= dragged)
            return i + 1;
        return i;
    }


    /**
     * 根据 点坐标 确定是第几个view
     *
     * @param x
     * @param y
     * @return
     */
    public int getIndexFromCoor(int x, int y) {
        int col = getColOrColFromCoor(x);
        int row = getColOrRowFromCoor(y + scroll);
        if (col == -1 || row == -1) // touch is between columns or rows
            return -1;
        int index = row * colCount + col;
        if (index >= getChildCount())
            return -1;
        return index;
    }


    /**
     * 获取列数
     *
     * @param coor
     * @return
     */
    protected int getColOrColFromCoor(int coor) {
        coor -= padding;
        if (coor >= padding && coor <= padding + childWidth) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * 获取行数
     *
     * @param coor
     * @return
     */
    protected int getColOrRowFromCoor(int coor) {
        coor -= padding;
        for (int i = 0; coor > 0; i++) {
            if (coor < chilHeight) {
                return i;
            }
            coor -= (chilHeight + padding);
        }
        return -1;
    }

    /**
     * 获取需要交换位置的 index
     *
     * @param x
     * @param y
     * @return
     */
    protected int getTargetFromCoor(int x, int y) {
        if (getColOrRowFromCoor(y + scroll) == -1) // touch is between rows
            return -1;
        // if (getIndexFromCoor(x, y) != -1) //touch on top of another visual
        // return -1;
        int leftPos = getIndexFromCoor(x, y - (chilHeight / 4));
        int rightPos = getIndexFromCoor(x, y + (chilHeight / 4));
        if (leftPos == -1 && rightPos == -1) // touch is in the middle of
            // nowhere
            return -1;
        if (leftPos == rightPos) {
            // touch is in the middle of a visual
            return -1;
        }
        int target = -1;
        if (rightPos > -1) {
            target = rightPos;
        } else if (leftPos > -1) {
            target = leftPos + 1;
        }
        if (dragged < target) {
            return target - 1;
        }
        // Toast.makeText(getContext(), "Target: " + target + ".",
        // Toast.LENGTH_SHORT).show();
        return target;
    }

    /**
     * 获取当前的index视图的位置对象
     * 获取到的是 右上角定点位置
     *
     * @param index
     * @return
     */
    protected Point getCoorFromIndex(int index) {
        int row = index;
        return new Point(padding, padding + (chilHeight + padding) * row - scroll);
    }

    public int getIndexOf(View child) {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) == child)
                return i;
        }
        return -1;
    }

    // EVENT HANDLERS
    public void onClick(View view) {
        if (enabled) {
            if (secondaryOnClickListener != null) {
                secondaryOnClickListener.onClick(view);
            }
            if (onItemClickListener != null && getLastIndex() != -1) {
                onItemClickListener.onItemClick(null, getChildAt(getLastIndex()), getLastIndex(), getLastIndex() / colCount);
            }
        }
    }

    public boolean onLongClick(View view) {
        if (!enabled) {
            return false;
        }
        int index = getLastIndex();
        if (getChildAt(index).getTag().equals("tag")) {
            return false;
        }
        if (index != -1) {
            dragged = index;
            Log.i(TAG, "dragged:" + dragged);
            animateDragged();
            return true;
        }
        return false;
    }

    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                enabled = true;
                lastX = (int) event.getX();
                lastY = (int) event.getY();
                touching = true;
                break;
            case MotionEvent.ACTION_MOVE:
                int delta = lastY - (int) event.getY();
                if (dragged != -1) {
                    // change draw location of dragged visual

                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    Log.i(TAG, "y:" + y);
                    Log.i(TAG, "x:" + x);
                    int t = y - (chilHeight / 2);
                    getChildAt(dragged).layout(getCoorFromIndex(dragged).x, t, getCoorFromIndex(dragged).x + childWidth, t + chilHeight);

                    // check for new target hover
                    int target = getTargetFromCoor(x, y);
                    if (lastTarget != target) {
                        if (target != -1) {
                            Log.i(TAG + "交换", "交换");
                            animateGap(target);
                            lastTarget = target;
                        }
                    }
                } else {
                    scroll += delta;
                    clampScroll();
                    if (Math.abs(delta) > 2) {
                        enabled = false;
                    }
                    mlayout(getLeft(), getTop(), getRight(), getBottom());
                }
                lastX = (int) event.getX();
                lastY = (int) event.getY();
                lastDelta = delta;
                break;
            case MotionEvent.ACTION_UP:
                if (dragged != -1) {
                    View v = getChildAt(dragged);
                    if (lastTarget != -1) {
                        reorderChildren();
                    } else {
                        Point xy = getCoorFromIndex(dragged);
                        v.layout(xy.x, xy.y, xy.x + childWidth, xy.y + childWidth);
                    }
                    v.clearAnimation();
                    if (v instanceof ImageView) {
                        ((ImageView) v).setAlpha(255);
                    }
                    lastTarget = -1;
                    dragged = -1;
                }
                touching = false;
                break;
        }
        if (dragged != -1) {
            return true;
        }
        return false;
    }

    // EVENT HELPERS

    /**
     * 触发长安事件
     */
    protected void animateDragged() {
        View v = getChildAt(dragged);
        int x = getCoorFromIndex(dragged).x;
        int y = getCoorFromIndex(dragged).y;
        v.layout(x, y, x + childWidth, y + chilHeight);
        AnimationSet animSet = new AnimationSet(true);
        AlphaAnimation alpha = new AlphaAnimation(1, 0.7f);
        alpha.setDuration(animT);
        animSet.addAnimation(alpha);
        animSet.setFillEnabled(true);
        animSet.setFillAfter(true);

        v.clearAnimation();
        v.startAnimation(animSet);
    }

    /**
     * 执行动画
     *
     * @param target
     */
    protected void animateGap(final int target) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (i == dragged) {
                continue;
            }
            int newPos = i;
            //向下移动
            if (dragged < target && i >= dragged + 1 && i <= target) {
                newPos--;
            } else if (target < dragged && i >= target && i < dragged) {
                newPos++;
            }

            // animate
            int oldPos = i;
            if (newPositions.get(i) != -1) {
                oldPos = newPositions.get(i);
            }
            if (oldPos == newPos) {
                continue;
            }

            Point oldXY = getCoorFromIndex(oldPos);
            Point newXY = getCoorFromIndex(newPos);
            Point oldOffset = new Point(oldXY.x - v.getLeft(), oldXY.y - v.getTop());
            Point newOffset = new Point(newXY.x - v.getLeft(), newXY.y - v.getTop());

            TranslateAnimation translate = new TranslateAnimation(
                    Animation.ABSOLUTE, oldOffset.x, Animation.ABSOLUTE,
                    newOffset.x, Animation.ABSOLUTE, oldOffset.y,
                    Animation.ABSOLUTE, newOffset.y);
            translate.setDuration(animT);
            translate.setFillEnabled(true);
            translate.setFillAfter(true);
            v.clearAnimation();
            v.startAnimation(translate);
            newPositions.set(i, newPos);
        }
    }

    protected void reorderChildren() {
        // FIGURE OUT HOW TO REORDER CHILDREN WITHOUT REMOVING THEM ALL AND
        // RECONSTRUCTING THE LIST!!!
        if (onRearrangeListener != null) {
            onRearrangeListener.onRearrange(dragged, lastTarget);
        }
        ArrayList<View> children = new ArrayList<View>();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).clearAnimation();
            children.add(getChildAt(i));
        }
        removeAllViews();
        while (dragged != lastTarget)
            if (lastTarget == children.size()) // dragged and dropped to the
            // right of the last element
            {
                children.add(children.remove(dragged));
                dragged = lastTarget;
            } else if (dragged < lastTarget) // shift to the right
            {
                Collections.swap(children, dragged, dragged + 1);
                dragged++;
            } else if (dragged > lastTarget) // shift to the left
            {
                Collections.swap(children, dragged, dragged - 1);
                dragged--;
            }
        for (int i = 0; i < children.size(); i++) {
            newPositions.set(i, -1);
            addView(children.get(i));
        }
        mlayout(getLeft(), getTop(), getRight(), getBottom());
        LG.i("dragged:" + dragged + "   lastTarget:" + lastTarget);
        if (mDataExchangelistener != null) {
            mDataExchangelistener.exchange(dragged, lastTarget);
        }
    }

    public void scrollToTop() {
        scroll = 0;
    }

    public void scrollToBottom() {
        scroll = Integer.MAX_VALUE;
        clampScroll();
    }

    protected void clampScroll() {
        int stretch = 3;
        int overreach = 200;
        int max = getMaxScroll();
        max = Math.max(max, 0);
        if (scroll < -overreach) {
            scroll = -overreach;
            lastDelta = 0;
        } else if (scroll > max + overreach) {
            scroll = max + overreach;
            lastDelta = 0;
        } else if (scroll < 0) {
            if (scroll >= -stretch) {
                scroll = 0;
            } else if (!touching) {
                scroll -= scroll / stretch;
            }
        } else if (scroll > max) {
            if (scroll <= max + stretch) {
                scroll = max;
            } else if (!touching) {
                scroll += (max - scroll) / stretch;
            }
        }
    }

    /**
     * 滑动距离
     *
     * @return
     */
    protected int getMaxScroll() {
        int rowCount = getChildCount();
        int max = rowCount * chilHeight + (rowCount + 1) * padding - getHeight();
        return max;
    }

    public int getLastIndex() {
        return getIndexFromCoor(lastX, lastY);
    }

    // OTHER METHODS
    public void setOnRearrangeListener(OnRearrangeListener l) {
        this.onRearrangeListener = l;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener l) {
        this.onItemClickListener = l;
    }

    public interface OnDataExchangeListener {
        void exchange(int from, int to);
    }


    OnDataExchangeListener mDataExchangelistener;

    public void setOnDataExchangeListener(OnDataExchangeListener listener) {
        this.mDataExchangelistener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    TagPositionListener mTagListener;

    public void setOnTagPositionListener(TagPositionListener listener) {
        this.mTagListener = listener;
    }

    public interface TagPositionListener {
        void getTagPosition(int position);
    }
}