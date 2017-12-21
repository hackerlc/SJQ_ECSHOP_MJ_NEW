package com.ecjia.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ecmoban.android.sijiqing.R;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/2/17 16:31.
 */

public class PriceImageView extends ImageView {
    protected static final int DEF_COLOR = 0xff666666;

    protected String price;
    protected int divColor;
    protected int divWidth;
    protected int priceStrColor;
    protected int priceStrSize;

    protected Paint strPaint;
    protected Paint divPaint;

    private int width;
    private int height;
    private float textWidth;
    private float textHeight;


    public PriceImageView(Context context) {
        this(context,null);
    }

    public PriceImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PriceImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        obtainAttributes(attrs);
        initPaint();
    }

    private void obtainAttributes(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.PriceImageView);

        price = typedArray.getString(R.styleable.PriceImageView_priceStr);
        divColor = typedArray.getColor(R.styleable.PriceImageView_divColor,DEF_COLOR);
        divWidth = (int) typedArray.getDimension(R.styleable.PriceImageView_divWidth,DEF_COLOR);
        priceStrColor = typedArray.getColor(R.styleable.PriceImageView_priceStrColor,DEF_COLOR);
        priceStrSize = (int) typedArray.getDimension(R.styleable.PriceImageView_priceStrSize,16);

        typedArray.recycle();
    }

    private void initPaint() {
        divPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        divPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        divPaint.setColor(divColor);

        strPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        strPaint.setTextAlign(Paint.Align.LEFT);
        strPaint.setColor(priceStrColor);
        strPaint.setTextSize(priceStrSize);


        textWidth=strPaint.measureText(price,0,price.length());
        textHeight=strPaint.getTextSize() + strPaint.getFontMetrics().bottom;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);

        width=getMeasuredWidth();
        height=getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(null != price && !price.equals("")) {
            canvas.save();
            float left = width - divWidth + (divWidth - textWidth) / 2;
            canvas.drawRect(width - divWidth, height - textHeight, width, height, divPaint);
            canvas.drawText(price, left, height - textHeight + strPaint.getTextSize(), strPaint);

            canvas.restore();
        }
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
