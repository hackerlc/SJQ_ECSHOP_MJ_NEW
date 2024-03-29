package com.ecjia.widgets;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;

import java.util.Date;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class XListViewHeader extends LinearLayout {
    private LinearLayout mContainer;
    private ImageView mArrowImageView;
    private ProgressBar mProgressBar;
    private TextView mHintTextView;
    private int mState = STATE_NORMAL;

    public void setTopPullLoadMore(boolean topPullLoadMore) {
        this.topPullLoadMore = topPullLoadMore;
    }

    private boolean topPullLoadMore = false;

//	private Animation mRotateUpAnim;
//	private Animation mRotateDownAnim;

    private final int ROTATE_ANIM_DURATION = 180;

    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;
    public long startRefreshTime;

    public XListViewHeader(Context context) {
        super(context);
        initView(context);
    }

    public AnimationDrawable animationDrawable;

    /**
     * @param context
     * @param attrs
     */
    public XListViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        // 初始情况，设置下拉刷新view高度为0
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 0);
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.xlistview_header, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);
        mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
        mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
        mProgressBar = (ProgressBar) findViewById(R.id.xlistview_header_progressbar);

//		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
//				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//				0.5f);
//		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
//		mRotateUpAnim.setFillAfter(true);
//		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
//				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//				0.5f);
//		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
//		mRotateDownAnim.setFillAfter(true);
    }

    public void setState(int state) {
        mArrowImageView.setImageResource(R.drawable.animation_xlistview);
        animationDrawable = (AnimationDrawable) mArrowImageView.getDrawable();
        if (state == mState) return;

        if (state == STATE_REFRESHING) {    // 显示进度
            Date date = new Date();
            startRefreshTime = date.getTime();
            if (animationDrawable.isOneShot()) {
                animationDrawable.setOneShot(false);
            }
            animationDrawable.start();
//			mArrowImageView.clearAnimation();
//			mArrowImageView.setVisibility(View.INVISIBLE);
//			mProgressBar.setVisibility(View.VISIBLE);
        } else {    // 显示箭头图片
            if (animationDrawable.isRunning()) {
                animationDrawable.stop();
            }
            mArrowImageView.setVisibility(View.VISIBLE);
//			mProgressBar.setVisibility(View.INVISIBLE);
        }

        switch (state) {
            case STATE_NORMAL:
                if (mState == STATE_READY) {
//				mArrowImageView.startAnimation(mRotateDownAnim);
                }
                if (mState == STATE_REFRESHING) {
//				mArrowImageView.clearAnimation();
                }
                if (topPullLoadMore) {
                    mHintTextView.setText(R.string.xlistview_header_hint_normal_load);
                } else {
                    mHintTextView.setText(R.string.xlistview_header_hint_normal);
                }
                break;
            case STATE_READY:
                if (mState != STATE_READY) {
                    mArrowImageView.clearAnimation();
//				mArrowImageView.startAnimation(mRotateUpAnim);
                    if (topPullLoadMore) {
                        mHintTextView.setText(R.string.xlistview_header_hint_ready_load);
                    } else {
                        mHintTextView.setText(R.string.xlistview_header_hint_ready);
                    }
                }
                break;
            case STATE_REFRESHING:
                mHintTextView.setText(R.string.xlistview_header_hint_loading);
                break;
            default:
        }
        mState = state;
    }

    public void setVisiableHeight(int height) {
        if (height < 0) height = 0;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisiableHeight() {
        return mContainer.getHeight();
    }

}
