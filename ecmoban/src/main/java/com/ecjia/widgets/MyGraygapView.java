package com.ecjia.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ecmoban.android.sijiqing.R;


/**
 * Created by Adam on 2015/12/29.
 */
public class MyGraygapView extends LinearLayout {
    public MyGraygapView(Context context) {
        super(context);
        ViewGroup.inflate(context, R.layout.layout_graygap, this);
    }

    public MyGraygapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ViewGroup.inflate(context, R.layout.layout_graygap, this);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MyGraygapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ViewGroup.inflate(context, R.layout.layout_graygap, this);
//        LayoutInflater.from(context).inflate(R.layout.layout_graygap, this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
}
