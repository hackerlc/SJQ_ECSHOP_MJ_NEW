package com.ecjia.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.ecmoban.android.sijiqing.R;

import java.util.List;


/**
 * SJQ_ECSHOP_MJ_NEW
 * <p>
 * xml中使用方式
 * <com.ecjia.widgets.IndicatorProgressBar
 * style="@style/TogetherWholesaleProgressBar"
 * android:layout_width="match_parent"
 * android:layout_height="6dp"
 * android:layout_alignParentLeft="true"
 * android:layout_marginLeft="12dp"
 * android:layout_marginRight="12dp"
 * android:layout_below="@id/v_line"
 * android:layout_toLeftOf="@id/btn_enter"
 * android:layout_marginTop="12dp"
 * android:progress="50"
 * app:topStartText="$100"
 * app:topContentText="$50"
 * app:topEndText="$30"
 * app:topLightTextSize="20sp"
 * app:topLightTextColor="@color/main_color_f82d7c"
 * app:topTextSize="16sp"
 * app:topTextColor="@color/main_font_color_6"
 * app:btmTextColor="@color/main_font_color_9"
 * app:btmTextSize="16sp"
 * app:btmStartText="起批"
 * app:btmUnitText="件"
 * app:btmStartNum="20"
 * app:btmContentNum="40"
 * app:btmEndNum="120"
 * app:buyNum="100"
 * app:pointColor="@color/main_font_color_e7e7e7"
 * app:pointLightColor="@color/main_color_db0f61"/>
 * <p>
 * Created by YichenZ on 2017/2/16 09:27.
 */

public class IndicatorProgressBar extends ProgressBar {
    /**
     * 默认属性
     */
    //indicator default font size
    protected static final int DEF_FONT_SIZE = 16;
    //indicator default text
    protected static final String DEF_TEXT = "";
    //indicator default font color
    protected static final int DEF_FONT_COLOR = 0xff666666;

    //top indicator height
    protected int topHeight;
    //btm indicator height
    protected int btmHeight;
    protected int defHeight;

    /**
     * 变量
     */

    protected int progress_width;
    /**
     * 顶部开始提示器变量
     */
    protected String topStartText;
    /**
     * 中间
     */
    protected String topContentText;
    /**
     * 结尾
     */
    protected String topEndText;

    /**
     * 默认文字尺寸，颜色
     */
    protected int topTextColor;
    protected int topTextSize;
    /**
     * 高亮文字颜色
     */
    protected int topLightTextColor;
    protected int topLightTextSize;

    /**
     * 底部提示器文字颜色，尺寸
     */
    protected int btmTextColor;
    protected int btmTextSize;
    /**
     * 底部提示器开始文字
     */
    protected String btmStartText;
    protected String btmContentText;
    protected String btmEndText;
    /**
     * 底部提示器单位 例如：个 件 元 ect
     */
    protected String btmUnitText;
    /**
     * 底部提示器开始数量
     */
    protected int btmStartNum;
    /**
     * 底部提示器中间数量
     */
    protected int btmContentNum;
    /**
     * 底部提示器结束数量
     */
    protected int btmEndNum;
    /**
     * 当前购买数量
     */
    protected int buyNum;
    /**
     * 圆点默认颜色和高亮颜色
     */
    protected int pointColor;
    protected int pointLightColor;

    /**
     * 绘制
     */

    protected Paint topStartPaint;
    protected Paint topContentPaint;
    protected Paint topEndPaint;
    protected Paint btmPaint;
    //三个圆点的笔触
    protected Paint contentStartPaint;
    protected Paint contentPaint;
    protected Paint contentEndPaint;


    public IndicatorProgressBar(Context context) {
        this(context, null);
    }

    public IndicatorProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        obtainAttributes(attrs);

        setProgress();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        topStartPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        topStartPaint.setColor(DEF_FONT_COLOR);
        topStartPaint.setTextAlign(Paint.Align.LEFT);
        topStartPaint.setTextSize(DEF_FONT_SIZE);
        topStartPaint.setFakeBoldText(true);

        topContentPaint = new TextPaint(topStartPaint);

        topEndPaint = new TextPaint(topStartPaint);
        topEndPaint.setTextAlign(Paint.Align.CENTER);

        btmPaint = new TextPaint(topStartPaint);

        contentStartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        contentStartPaint.setAntiAlias(true);
        contentStartPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        contentPaint = new Paint(contentStartPaint);
        contentEndPaint = new Paint(contentStartPaint);
    }

    /**
     * 获取自定义属性
     *
     * @param attrs
     */
    private void obtainAttributes(AttributeSet attrs) {
        TypedArray typedArray = getContext()
                .obtainStyledAttributes(attrs, R.styleable.IndicatorProgressBar);
        //顶部控件
        //获取文字
        topStartText = typedArray.getString(R.styleable.IndicatorProgressBar_topStartText);
        topContentText = typedArray.getString(R.styleable.IndicatorProgressBar_topContentText);
        topEndText = typedArray.getString(R.styleable.IndicatorProgressBar_topEndText);
        //获取默认文字尺寸、颜色
        topTextSize = (int) typedArray.getDimension(R.styleable.IndicatorProgressBar_topTextSize, DEF_FONT_SIZE);
        topTextColor = typedArray.getColor(R.styleable.IndicatorProgressBar_topTextColor, DEF_FONT_COLOR);
        //获取高亮文字尺寸、颜色
        topLightTextSize = (int) typedArray.getDimension(R.styleable.IndicatorProgressBar_topLightTextSize, DEF_FONT_SIZE);
        topLightTextColor = typedArray.getColor(R.styleable.IndicatorProgressBar_topLightTextColor, DEF_FONT_COLOR);

        //圆点默认颜色和高亮颜色
        pointColor = typedArray.getColor(R.styleable.IndicatorProgressBar_pointColor, DEF_FONT_COLOR);
        pointLightColor = typedArray.getColor(R.styleable.IndicatorProgressBar_pointLightColor, DEF_FONT_COLOR);

        //底部控件
        //文字颜色，尺寸
        btmTextColor = typedArray.getColor(R.styleable.IndicatorProgressBar_btmTextColor, DEF_FONT_COLOR);
        btmTextSize = (int) typedArray.getDimension(R.styleable.IndicatorProgressBar_btmTextSize, DEF_FONT_SIZE);
        //底部提示器开始文字
        btmStartText = typedArray.getString(R.styleable.IndicatorProgressBar_btmStartText);
        btmContentText = typedArray.getString(R.styleable.IndicatorProgressBar_btmContentText);
        btmEndText = typedArray.getString(R.styleable.IndicatorProgressBar_btmEndText);
        //3数量
        btmStartNum = typedArray.getInteger(R.styleable.IndicatorProgressBar_btmStartNum, 0);
        btmContentNum = typedArray.getInteger(R.styleable.IndicatorProgressBar_btmContentNum, 0);
        btmEndNum = typedArray.getInteger(R.styleable.IndicatorProgressBar_btmEndNum, 0);
        //当前数量
        buyNum = typedArray.getInteger(R.styleable.IndicatorProgressBar_buyNum, 0);
        //设置笔触
        setPaintSizeAndColor();
        btmPaint.setTextSize(btmTextSize);
        btmPaint.setColor(btmTextColor);

        //根据高亮尺寸设置顶部高度
        Paint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(topLightTextColor);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(topLightTextSize);
        paint.setFakeBoldText(true);
        topHeight = (int) (paint.getTextSize() + paint.getFontMetrics().bottom);
        //设置底部高度
        paint.setAntiAlias(true);
        paint.setColor(btmTextColor);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(btmTextSize);
        paint.setFakeBoldText(true);
        btmHeight = (int) (paint.getTextSize() + paint.getFontMetrics().bottom * 2);
        paint = null;
        typedArray.recycle();
    }

    /**
     * 根据不同的数量设置笔触颜色尺寸
     */
    private void setPaintSizeAndColor() {
        if (buyNum >= btmEndNum) {
            topStartPaint.setTextSize(topTextSize);
            topStartPaint.setColor(topTextColor);
            topStartPaint.setStrikeThruText(true);

            topContentPaint.setTextSize(topTextSize);
            topContentPaint.setColor(topTextColor);
            topContentPaint.setStrikeThruText(true);

            topEndPaint.setTextSize(topLightTextSize);
            topEndPaint.setColor(topLightTextColor);

            contentStartPaint.setColor(pointLightColor);
            contentPaint.setColor(pointLightColor);
            contentEndPaint.setColor(pointLightColor);
        } else if (buyNum >= btmContentNum) {//中间开始高亮
            topStartPaint.setTextSize(topTextSize);
            topStartPaint.setColor(topTextColor);
            topStartPaint.setStrikeThruText(true);

            topContentPaint.setTextSize(topLightTextSize);
            topContentPaint.setColor(topLightTextColor);
            topContentPaint.setStrikeThruText(false);

            topEndPaint.setTextSize(topTextSize);
            topEndPaint.setColor(topTextColor);

            contentStartPaint.setColor(pointLightColor);
            contentPaint.setColor(pointLightColor);
            contentEndPaint.setColor(pointColor);
        } else if (buyNum >= btmStartNum) {//开始高亮
            topStartPaint.setTextSize(topLightTextSize);
            topStartPaint.setColor(topLightTextColor);
            topStartPaint.setStrikeThruText(false);


            topContentPaint.setTextSize(topTextSize);
            topContentPaint.setColor(topTextColor);
            topContentPaint.setStrikeThruText(false);


            topEndPaint.setTextSize(topTextSize);
            topEndPaint.setColor(topTextColor);

            contentStartPaint.setColor(pointLightColor);
            contentPaint.setColor(pointColor);
            contentEndPaint.setColor(pointColor);
        } else {
            topStartPaint.setTextSize(topTextSize);
            topStartPaint.setColor(topTextColor);
            topStartPaint.setStrikeThruText(false);


            topContentPaint.setTextSize(topTextSize);
            topContentPaint.setColor(topTextColor);
            topContentPaint.setStrikeThruText(false);


            topEndPaint.setTextSize(topTextSize);
            topEndPaint.setColor(topTextColor);

            contentStartPaint.setColor(pointColor);
            contentPaint.setColor(pointColor);
            contentEndPaint.setColor(pointColor);
        }
    }

    /**
     * 测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        defHeight = MeasureSpec.getSize(heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        setMeasuredDimension(width, height);
        progress_width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    /**
     * 绘制
     *
     * @param canvas
     */
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        Drawable progressDrawable = getProgressDrawable();
        if (progressDrawable != null
                && progressDrawable instanceof LayerDrawable) {
            LayerDrawable d = (LayerDrawable) progressDrawable;

            for (int i = 0; i < d.getNumberOfLayers(); i++) {
                d.getDrawable(i).getBounds().left = defHeight / 2;
                d.getDrawable(i).getBounds().top = topHeight;
                d.getDrawable(i).getBounds().right = progress_width - defHeight / 2;
                d.getDrawable(i).getBounds().bottom = topHeight + defHeight;
            }
        }

        super.onDraw(canvas);
        canvas.save();
        if (section > 0) {
            setTopStartIndicator(canvas, progressDrawable);
        }
        canvas.restore();
    }

    private void updateProgressBar() {
        Drawable progressDrawable = getProgressDrawable();

        if (progressDrawable != null
                && progressDrawable instanceof LayerDrawable) {
            LayerDrawable d = (LayerDrawable) progressDrawable;

            final float scale = getScale(getProgress());

            // get the progress bar and update it's size
            Drawable progressBar = d.findDrawableByLayerId(R.id.progress);

            final int width = d.getBounds().right - d.getBounds().left;

            if (progressBar != null) {
                Rect progressBarBounds = progressBar.getBounds();
                progressBarBounds.right = progressBarBounds.left
                        + (int) (width * scale + 0.5f);
                progressBar.setBounds(progressBarBounds);
            }

        }
    }

    private float getScale(int progress) {
        float scale = getMax() > 0 ? (float) progress / (float) getMax() : 0;

        return scale;
    }

    /**
     * 绘制指示器文字
     *
     * @param canvas
     * @param progressDrawable
     */
    private void setTopStartIndicator(Canvas canvas, Drawable progressDrawable) {
        setPaintSizeAndColor();//设置一下笔触，不知道是否会影响到16ms

        float width = 0.0f;
        float baseline = 0.0f;

        //绘制圆点
        int x = 0;
        int y = topHeight + defHeight / 2;
        int r = defHeight - defHeight / 4;
        canvas.translate(0, 0);
        baseline = topHeight / 2 + topEndPaint.getTextSize() / 2 - topEndPaint.getFontMetrics().descent;
        // indicator start
        if (section >= 2) {
            canvas.drawText(topStartText, 0, baseline, topStartPaint);
            //1
            x = x + defHeight;
            canvas.drawCircle(x, y, r, contentStartPaint);
        }
        // indicator content
        if (section >= 3) {
            width = progress_width / 2 - topContentPaint.measureText(topContentText, 0, topContentText.length() / 2);
            canvas.drawText(topContentText, width, baseline, topContentPaint);
            //2
            x = progress_width / 2;
            canvas.drawCircle(x, y, r, contentPaint);
        }
        // indicator end
        width = (progress_width - topEndPaint.measureText(topEndText, 0, topEndText.length()) / 2);
        canvas.drawText(topEndText, width, baseline, topEndPaint);
        //3
        x = progress_width - defHeight;
        canvas.drawCircle(x, y, r, contentEndPaint);


        //绘制下方提示器
        canvas.translate(0, topHeight + defHeight);
        //1
        if (section >= 2) {
            canvas.drawText(btmStartText, 0, baseline, btmPaint);
        }
        //2
        if (section >= 3) {
            width = progress_width / 2 - btmPaint.measureText(btmContentText, 0, btmContentText.length() / 2);
            canvas.drawText(btmContentText, width, baseline, btmPaint);
        }
        //3
        TextPaint paint = new TextPaint(btmPaint);
        paint.setTextAlign(Paint.Align.CENTER);
        width = (progress_width - paint.measureText(btmEndText, 0, btmEndText.length()) / 2);
        canvas.drawText(btmEndText, width, baseline, paint);
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        result = specSize + topHeight + btmHeight + getPaddingTop() + getPaddingBottom();
        return result;
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        result = specSize + getPaddingLeft() + getPaddingRight();
        return result;
    }

    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    /**
     */

    public synchronized void setProgress() {
        float progress = 0;
        setMax(max);
        if (section >= 3) {

            if (buyNum <= btmContentNum) {
                if(buyNum<=btmStartNum){
                    progress=0;
                }else {
                    progress = (buyNum- btmStartNum)/ ((float) btmContentNum-btmStartNum) * 50;
                }
            } else {
                progress = (buyNum - btmContentNum) / (float) (btmEndNum - btmContentNum) * 50 + 50;
            }
        } else {
            progress = buyNum / (float) btmEndNum * 100;
        }
        if (progress < 0) {
            progress *= -1;
        }

        setProgress((int) progress);
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
    }

    /**
     * 锁定最大值
     *
     * @param max
     */
    int max = 100;

    @Override
    public synchronized void setMax(int max) {
        max = this.max;
        super.setMax(max);
    }

    //片段，默认为3，如果是0则不显示提示器
    int section = 3;

    /**
     * 设置分段
     *
     * @param sections
     */
    public void setSection(List<Section> sections) {
        section = sections.size();
        if (section == 1) {
            topEndText = sections.get(0).getSectionStr();
            btmEndText = sections.get(0).getNumStr();
            btmEndNum = sections.get(0).getNum();
            btmContentNum = btmEndNum;
            btmStartNum = btmEndNum;
        }
        if (section > 1) {
            topStartText = sections.get(0).getSectionStr();
            btmStartText = sections.get(0).getNumStr();
            btmStartNum = sections.get(0).getNum();
        }

        if (section == 2) {
            topEndText = sections.get(1).getSectionStr();
            btmEndText = sections.get(1).getNumStr();
            btmEndNum = sections.get(1).getNum();
            btmContentNum = btmEndNum;
        }
        if (section > 2) {
            topContentText = sections.get(1).getSectionStr();
            btmContentText = sections.get(1).getNumStr();
            btmContentNum = sections.get(1).getNum();
        }

        if (section >= 3) {
            topEndText = sections.get(2).getSectionStr();
            btmEndText = sections.get(2).getNumStr();
            btmEndNum = sections.get(2).getNum();
        }
    }

    public static class Section {
        private int num;
        private String sectionStr;
        private String numStr;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getSectionStr() {
            return sectionStr;
        }

        public void setSectionStr(String sectionStr) {
            this.sectionStr = sectionStr;
        }

        public String getNumStr() {
            return numStr;
        }

        public void setNumStr(String numStr) {
            this.numStr = numStr;
        }
    }

    public String getTopStartText() {
        return topStartText;
    }

    public void setTopStartText(String topStartText) {
        this.topStartText = topStartText;
    }

    public String getTopContentText() {
        return topContentText;
    }

    public void setTopContentText(String topContentText) {
        this.topContentText = topContentText;
    }

    public String getTopEndText() {
        return topEndText;
    }

    public void setTopEndText(String topEndText) {
        this.topEndText = topEndText;
    }

    public int getTopTextColor() {
        return topTextColor;
    }

    public void setTopTextColor(int topTextColor) {
        this.topTextColor = topTextColor;
    }

    public int getTopTextSize() {
        return topTextSize;
    }

    public void setTopTextSize(int topTextSize) {
        this.topTextSize = topTextSize;
    }

    public int getTopLightTextColor() {
        return topLightTextColor;
    }

    public void setTopLightTextColor(int topLightTextColor) {
        this.topLightTextColor = topLightTextColor;
    }

    public int getTopLightTextSize() {
        return topLightTextSize;
    }

    public void setTopLightTextSize(int topLightTextSize) {
        this.topLightTextSize = topLightTextSize;
    }

    public int getBtmTextColor() {
        return btmTextColor;
    }

    public void setBtmTextColor(int btmTextColor) {
        this.btmTextColor = btmTextColor;
    }

    public int getBtmTextSize() {
        return btmTextSize;
    }

    public void setBtmTextSize(int btmTextSize) {
        this.btmTextSize = btmTextSize;
    }

    public String getBtmStartText() {
        return btmStartText;
    }

    public void setBtmStartText(String btmStartText) {
        this.btmStartText = btmStartText;
    }

    public String getBtmUnitText() {
        return btmUnitText;
    }

    public void setBtmUnitText(String btmUnitText) {
        this.btmUnitText = btmUnitText;
    }

    public int getBtmStartNum() {
        return btmStartNum;
    }

    public void setBtmStartNum(int btmStartNum) {
        this.btmStartNum = btmStartNum;
    }

    public int getBtmContentNum() {
        return btmContentNum;
    }

    public void setBtmContentNum(int btmContentNum) {
        this.btmContentNum = btmContentNum;
    }

    public int getBtmEndNum() {
        return btmEndNum;
    }

    public void setBtmEndNum(int btmEndNum) {
        this.btmEndNum = btmEndNum;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
//        this.buyNum = buyNum < btmStartNum ? 0 : buyNum - btmStartNum;
        this.buyNum = buyNum;
    }

    public int getPointColor() {
        return pointColor;
    }

    public void setPointColor(int pointColor) {
        this.pointColor = pointColor;
    }

    public int getPointLightColor() {
        return pointLightColor;
    }

    public void setPointLightColor(int pointLightColor) {
        this.pointLightColor = pointLightColor;
    }

    public String getBtmContentText() {
        return btmContentText;
    }

    public void setBtmContentText(String btmContentText) {
        this.btmContentText = btmContentText;
    }

    public String getBtmEndText() {
        return btmEndText;
    }

    public void setBtmEndText(String btmEndText) {
        this.btmEndText = btmEndText;
    }
}
