package com.ecjia.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;

/**
 * Created by Adam on 2016-04-13.
 */
public class ErrorView extends LinearLayout {

    ImageView mImageView;
    TextView mTextView;

    Bitmap mBitmap;
    String errorText;
    int errorTextId;
    int errorImageId;

    public ErrorView(Context context) {
        this(context, null);
    }

    public ErrorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.layout_error_view, this, true);
        mTextView = (TextView) findViewById(R.id.error_text);
        mImageView = (ImageView) findViewById(R.id.error_image);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ErrorView);
        errorImageId = array.getResourceId(R.styleable.ErrorView_errorImage, -1);
        errorTextId = array.getResourceId(R.styleable.ErrorView_errorText, -1);
        errorText = array.getString(R.styleable.ErrorView_errorText);
        array.recycle();
        init();
    }

    void init() {
        if (errorTextId != -1) {
            mTextView.setText(errorTextId);
        }

        if (errorImageId != -1) {
            mImageView.setImageResource(errorImageId);
        }

        if (mBitmap != null) {
            mImageView.setImageBitmap(mBitmap);
        }

        if (errorText != null) {
            mTextView.setText(errorText);
        }
    }

    public void setErrorText(int resId) {
        errorTextId = resId;
        mTextView.setText(errorTextId);
        init();
    }

    public void setErrorText(CharSequence charSequence) {
        errorTextId = -1;
        errorText = (String) charSequence;
        mTextView.setText(errorText);
        init();
    }

    public void setErrorImageResource(int resId) {
        mBitmap = null;
        errorImageId = resId;
        mImageView.setImageResource(errorImageId);
        init();
    }

    public void setErrorImageBitmap(Bitmap bitmap) {
        errorImageId = -1;
        this.mBitmap = bitmap;
        mImageView.setImageBitmap(mBitmap);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
