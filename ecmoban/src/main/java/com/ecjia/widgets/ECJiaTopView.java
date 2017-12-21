package com.ecjia.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.view.activity.goodsdetail.view.Utils;
import com.ecjia.util.LG;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by Adam on 2016-03-15.
 */
public class ECJiaTopView extends FrameLayout {

    public static final int LEFT_TYPE_BACKIMAGE = 1;//只显示返回按钮
    public static final int LEFT_TYPE_PHOTOIMAGE = 2;//只显示左侧头像
    public static final int LEFT_TYPE_TEXT = 3;//只显示左侧文字
    public static final int LEFT_TYPE_GONE = 4;//全部隐藏
    public static final int LEFT_TYPE_DEFAULT = 5;//默认显示（只显示返回按钮）

    public static final int RIGHT_TYPE_TEXT = 11;//只显示右侧文字
    public static final int RIGHT_TYPE_IMAGE = 12;//只显示右侧图片
    public static final int RIGHT_TYPE_GONE = 13;//全部隐藏
    public static final int RIGHT_TYPE_DEFAULT = 14;//默认显示（全部隐藏）

    private Context mContext;
    private FrameLayout leftLayout, rightLayout;
    private ImageView leftBackImage, leftPhotoImage, rightImage;
    private TextView titleTextView, leftTextView, rightTextView;
    private ImageView titleImageView;

    public ECJiaTopView(Context context) {
        this(context, null);
    }

    public ECJiaTopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ECJiaTopView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        LG.i("baseresult =" + result);
        return result;
    }

    void init() {
        LG.i("Build.VERSION.SDK_INT" + Build.VERSION.SDK_INT);
        LayoutInflater.from(mContext).inflate(R.layout.layout_ecjiatopview, this, true);
        setBackgroundResource(R.color.public_theme_color_normal);
        titleTextView = (TextView) findViewById(R.id.top_view_title);
        leftLayout = (FrameLayout) findViewById(R.id.top_view_left);
        rightLayout = (FrameLayout) findViewById(R.id.top_view_right);

        leftBackImage = (ImageView) findViewById(R.id.top_view_left_back);
        leftPhotoImage = (ImageView) findViewById(R.id.top_view_left_photo);
        rightImage = (ImageView) findViewById(R.id.top_view_right_image);

        leftTextView = (TextView) findViewById(R.id.top_view_left_text);
        rightTextView = (TextView) findViewById(R.id.top_view_right_text);

        titleImageView = (ImageView) findViewById(R.id.top_view_title_image);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setLeftBackImageSize(int dimensId) {
        leftBackImage.getLayoutParams().height = mContext.getResources().getDimensionPixelOffset(dimensId);
        leftBackImage.getLayoutParams().width = mContext.getResources().getDimensionPixelOffset(dimensId);
    }

    public void setRightImageSize(int dimensId) {
        rightImage.getLayoutParams().height = mContext.getResources().getDimensionPixelOffset(dimensId);
        rightImage.getLayoutParams().width = mContext.getResources().getDimensionPixelOffset(dimensId);
    }


    TabLayout tabLayout;

    public enum TitleType {
        TEXT, IMAGE, TABLAYOUT

    }

    /**
     * 设置title的样式
     *
     * @param type
     */
    public void setTitleType(TitleType type) {
        switch (type) {
            case TEXT:
                titleTextView.setVisibility(VISIBLE);
                titleImageView.setVisibility(GONE);
                tabLayout.setVisibility(GONE);
                break;

            case IMAGE:
                titleTextView.setVisibility(GONE);
                titleImageView.setVisibility(VISIBLE);
                tabLayout.setVisibility(GONE);
                break;

            case TABLAYOUT:
                titleTextView.setVisibility(GONE);
                titleImageView.setVisibility(GONE);
                tabLayout.setVisibility(VISIBLE);
                break;
        }
    }


    /**
     * 设置左边的返回按钮的背景图
     *
     * @param resId
     */
    public void setLeftBackImageBackground(int resId) {
        leftBackImage.setBackgroundResource(resId);
    }

    /**
     * 设置左边的返回按钮的背景图
     *
     * @param colorId
     */
    public void setTopTextColor(int colorId) {
        titleTextView.setTextColor(colorId);
        leftTextView.setTextColor(colorId);
        rightTextView.setTextColor(colorId);
    }


    public TextView getTitleTextView() {

        return titleTextView;
    }

    /**
     * 设置右边的按钮的背景图
     *
     * @param resId
     */
    public void setRightImageBackground(int resId) {
        rightImage.setBackgroundResource(resId);
    }

    /**
     * @param type 左边的视图可视类型
     *             <p/>
     *             用于设置视图的可视化
     */
    public void setLeftType(int type) {

        switch (type) {
            case LEFT_TYPE_BACKIMAGE:
                leftLayout.setVisibility(VISIBLE);
                leftBackImage.setVisibility(VISIBLE);
                leftBackImage.setImageResource(R.drawable.header_back_arrow);
                leftPhotoImage.setVisibility(GONE);
                leftTextView.setVisibility(GONE);
                break;
            case LEFT_TYPE_PHOTOIMAGE:
                leftLayout.setVisibility(VISIBLE);
                leftBackImage.setVisibility(GONE);
                leftPhotoImage.setVisibility(VISIBLE);
                leftTextView.setVisibility(GONE);
                break;
            case LEFT_TYPE_TEXT:
                leftLayout.setVisibility(VISIBLE);
                leftBackImage.setVisibility(GONE);
                leftPhotoImage.setVisibility(GONE);
                leftTextView.setVisibility(VISIBLE);
                break;
            case LEFT_TYPE_GONE:
                leftLayout.setVisibility(GONE);
                leftBackImage.setVisibility(GONE);
                leftPhotoImage.setVisibility(GONE);
                leftTextView.setVisibility(GONE);
                break;
            case LEFT_TYPE_DEFAULT:
                leftLayout.setVisibility(VISIBLE);
                leftBackImage.setVisibility(VISIBLE);
                leftBackImage.setImageResource(R.drawable.header_back_arrow);
                leftPhotoImage.setVisibility(GONE);
                leftTextView.setVisibility(GONE);
                break;
            default:
                break;
        }
    }

    public void setTopViewBackground(int recId) {
        setBackgroundResource(recId);
    }

    /**
     * @param type 右边的视图可视类型
     *             <p/>
     *             用于设置视图的可视化
     */
    public void setRightType(int type) {

        switch (type) {
            case RIGHT_TYPE_TEXT:
                rightLayout.setVisibility(VISIBLE);
                rightTextView.setVisibility(VISIBLE);
                rightImage.setVisibility(GONE);
                break;
            case RIGHT_TYPE_IMAGE:
                rightLayout.setVisibility(VISIBLE);
                rightTextView.setVisibility(GONE);
                rightImage.setVisibility(VISIBLE);
                break;
            case RIGHT_TYPE_GONE:
                rightLayout.setVisibility(GONE);
                rightTextView.setVisibility(GONE);
                rightImage.setVisibility(GONE);
                break;
            case RIGHT_TYPE_DEFAULT:
                rightLayout.setVisibility(GONE);
                rightTextView.setVisibility(GONE);
                rightImage.setVisibility(GONE);
                break;
            default:
                break;
        }
    }

    /**
     * @param str 标题文字
     *            <p/>
     *            用于设置标题文字
     */
    public void setTitleText(String str) {
        titleTextView.setText(str);
    }

    /**
     * @param resid 标题文字对应的资源id
     *              <p/>
     *              用于设置标题文字
     */
    public void setTitleText(int resid) {
        titleTextView.setText(resid);
    }

    /**
     * @param onClickListener 左边按钮的动作
     *                        <p/>
     *                        设置点击事件
     */
    public void setLeftOnClickListener(OnClickListener onClickListener) {
        if (null != onClickListener) leftLayout.setOnClickListener(onClickListener);
    }

    /**
     * @param onClickListener 右边按钮的动作
     *                        <p/>
     *                        设置点击事件
     */
    public void setRightOnClickListener(OnClickListener onClickListener) {
        if (null != onClickListener) rightLayout.setOnClickListener(onClickListener);
    }

    /**
     * @param str             左边文本框文字内容
     * @param onClickListener 左边文本框的动作
     *                        设置文本框的文字和点击事件
     */
    public void setLeftText(String str, OnClickListener onClickListener) {
        leftTextView.setText(str);
        if (null != onClickListener) leftTextView.setOnClickListener(onClickListener);
    }

    /**
     * @param resid           左边文本框文字内容在资源中的id
     * @param onClickListener 左边文本框的动作
     *                        <p/>
     *                        设置文本框的文字和点击事件
     */
    public void setLeftText(int resid, OnClickListener onClickListener) {
        leftTextView.setText(resid);
        if (null != onClickListener) leftTextView.setOnClickListener(onClickListener);
    }

    /**
     * @param str             右边文本框文字内容
     * @param onClickListener 右边文本框的动作
     *                        设置文本框的文字和点击事件
     */
    public void setRightText(String str, OnClickListener onClickListener) {
        rightTextView.setText(str);
        if (null != onClickListener) rightTextView.setOnClickListener(onClickListener);
    }

    /**
     * @param resid           右边文本框文字内容在资源中的id
     * @param onClickListener 右边文本框的动作
     *                        设置文本框的文字和点击事件
     */
    public void setRightText(int resid, OnClickListener onClickListener) {
        rightTextView.setText(resid);
        if (null != onClickListener) rightTextView.setOnClickListener(onClickListener);
    }

    public void setRightText(int resid) {
        rightTextView.setText(resid);
    }

    public void setRightText(String str) {
        rightTextView.setText(str);
    }

    public void setRightTextEnable(boolean enable) {
        rightTextView.setEnabled(enable);
    }

    /**
     * @param resid           左边返回按钮在资源中的id
     * @param onClickListener 左边返回按钮的动作
     *                        设置返回按钮和图片和点击事件
     */
    public void setLeftBackImage(int resid, OnClickListener onClickListener) {
        leftBackImage.setImageResource(resid);
        if (null != onClickListener) leftBackImage.setOnClickListener(onClickListener);
    }

    /**
     * @param bitmap          左边返回按钮的bitmap对象
     * @param onClickListener 左边返回按钮的动作
     *                        <p/>
     *                        设置返回按钮和图片和点击事件
     */
    public void setLeftBackImage(Bitmap bitmap, OnClickListener onClickListener) {
        leftBackImage.setImageBitmap(bitmap);
        if (null != onClickListener) leftBackImage.setOnClickListener(onClickListener);
    }

    /**
     * @param resid           左边头像在资源中的id
     * @param onClickListener 左边头像的动作
     *                        <p/>
     *                        设置头像的图片和点击事件
     */
    public void setLeftPhotoImage(int resid, OnClickListener onClickListener) {
        leftPhotoImage.setImageResource(resid);
        if (null != onClickListener) leftPhotoImage.setOnClickListener(onClickListener);
    }

    /**
     * @param bitmap          左边头像的bitmap对象
     * @param onClickListener 左边头像的动作
     *                        <p/>
     *                        设置头像的图片和点击事件
     */
    public void setLeftPhotoImage(Bitmap bitmap, OnClickListener onClickListener) {
        leftPhotoImage.setImageBitmap(bitmap);
        if (null != onClickListener) leftPhotoImage.setOnClickListener(onClickListener);
    }

    /**
     * @param resid           右边分享等按钮在资源中的id
     * @param onClickListener 右边分享等按钮的动作
     *                        <p/>
     *                        设置分享等按钮的图片和点击事件
     */
    public void setRightImage(int resid, OnClickListener onClickListener) {
        rightImage.setImageResource(resid);
        if (null != onClickListener) rightImage.setOnClickListener(onClickListener);
    }


    /**
     * @param bitmap          右边分享等按钮bitmap对象
     * @param onClickListener 右边分享等按钮的动作
     *                        <p/>
     *                        设置分享等按钮的图片和点击事件
     */
    public void setRightImage(Bitmap bitmap, OnClickListener onClickListener) {
        rightImage.setImageBitmap(bitmap);
        if (null != onClickListener) rightImage.setOnClickListener(onClickListener);
    }

    /**
     * 固定高度，为了适应API21
     *
     * @param params
     */
    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
    }

    /**
     * 设置中间标题的图片
     *
     * @param resId
     */
    public void setTitleImage(int resId) {
        titleImageView.setImageResource(resId);
    }


    /**
     * 设置中间标题的图片
     *
     * @param bitmap
     */
    public void setTitleImage(Bitmap bitmap) {
        titleImageView.setImageBitmap(bitmap);
    }

    public TextView getRightTextView() {
        return rightTextView;
    }


    public void setupWithViewPager(ViewPager viewPager) {
        tabLayout.setupWithViewPager(viewPager);

    }

    OnTitleAnimationEndListener mAnimationListener;

    boolean mAnimationEnable = false;

    public void setTitleAnimationEnable(boolean listener) {
        this.mAnimationEnable = listener;
    }


    public void setOnTitleAnimationEndListener(OnTitleAnimationEndListener listener) {
        this.mAnimationListener = listener;

        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                titleTextView.setVisibility(INVISIBLE);
                ObjectAnimator animator0 = ObjectAnimator.ofFloat(titleTextView, "translationY", 0, Utils.dp2px(mContext, 40));
                animator0.start();
                titleTextView.setVisibility(INVISIBLE);
            }
        }, 500);
    }

    public interface OnTitleAnimationEndListener {

        void upAnimationStart();

        void downAnimationStart();

        void upAnimationEnd();

        void downAnimationEnd();
    }

    public void startUpAnimation() {
        if (mAnimationEnable) {
            titleTextView.setVisibility(VISIBLE);
            ObjectAnimator animator = ObjectAnimator.ofFloat(tabLayout, "translationY", 0, -tabLayout.getMeasuredHeight());
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(titleTextView, "translationY", titleTextView.getMeasuredHeight(), 0);

            AnimatorSet set = new AnimatorSet();
            set.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (mAnimationListener != null) {
                        mAnimationListener.upAnimationEnd();
                    }
                }
            });
            set.setDuration(800).playTogether(animator, animator2);
            set.start();
        }
    }

    public void startDownAnimation() {
        if (mAnimationEnable) {
            if (mAnimationListener != null) {
                mAnimationListener.downAnimationStart();
            }
            titleTextView.setVisibility(VISIBLE);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(tabLayout, "translationY", -tabLayout.getMeasuredHeight(), 0);
            ObjectAnimator animator = ObjectAnimator.ofFloat(titleTextView, "translationY", 0, titleTextView.getMeasuredHeight());
            AnimatorSet set = new AnimatorSet();
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (mAnimationListener != null) {
                        mAnimationListener.downAnimationEnd();
                    }
                }
            });
            set.setDuration(800).playTogether(animator, animator2);
            set.start();

        }
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }
}

