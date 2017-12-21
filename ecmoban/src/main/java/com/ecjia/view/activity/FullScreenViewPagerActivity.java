package com.ecjia.view.activity;


import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;

import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.umeng.message.PushAgent;

import ru.truba.touchgallery.GalleryWidget.UrlPagerAdapter;

public class FullScreenViewPagerActivity extends BaseActivity {

    private ImageView share;
    private GalleryViewPager photoViewPager;

    private TextView titleTextView;

    UrlPagerAdapter pagerAdapter;
    List<String> items = new ArrayList<String>();
    ViewGroup group;
    TextView textView;
    TextView[] textViews;
    int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.full_screen_view_pager);
        PushAgent.getInstance(this).onAppStart();
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);

        items = intent.getStringArrayListExtra("pictures");

        size = intent.getIntExtra("size", 0);


        Window lp = getWindow();
        lp.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        photoViewPager = (GalleryViewPager) findViewById(R.id.fullscreen_viewpager);

        group = (ViewGroup) findViewById(R.id.full_viewGroup);
        LinearLayout full = (LinearLayout) findViewById(R.id.full_layout);
        full.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                FullScreenViewPagerActivity.this.finish();
            }
        });
        photoViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int arg0) {
                for (int i = 0; i < textViews.length; i++) {
                    textViews[arg0].setBackgroundResource(R.drawable.view_selectde);
                    if (arg0 != i) {
                        textViews[i].setBackgroundResource(R.drawable.view_unselected);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        addBannerView();
        photoViewPager.setCurrentItem(position % size);

        share = (ImageView) findViewById(R.id.item_grid_share);
        share.setVisibility(View.GONE);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.my_scale_action, R.anim.my_scale_finish);

    }

    public void addBannerView() {
        pagerAdapter = new UrlPagerAdapter(this, items);
        photoViewPager.setAdapter(pagerAdapter);
        AddPoint();

    }

    // 添加轮播图小点
    public void AddPoint() {
        final Resources res = getResources();
        group.removeAllViews();
        textViews = new TextView[size];
        for (int i = 0; i < size; i++) {
            textView = new TextView(this);
            // 使用res.getDimension(R.dimen.default_pointwidth)则可在不同分辨率下都得到相同的大小
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    (int) res.getDimension(R.dimen.default_pointwidth),
                    (int) res.getDimension(R.dimen.default_pointwidth));
            lp.setMargins(
                    (int) res.getDimension(R.dimen.default_pointdistance), 0,
                    (int) res.getDimension(R.dimen.default_pointdistance), 0);

            textView.setLayoutParams(lp);
            textViews[i] = textView;
            if (i == 0) {
                textViews[i].setBackgroundResource(R.drawable.view_selectde);
            } else {
                textViews[i].setBackgroundResource(R.drawable.view_unselected);
            }
            group.addView(textViews[i]);
        }
        group.invalidate();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.my_scale_action,
                    R.anim.my_scale_finish);
        }
        return true;

    }

}
