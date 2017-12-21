package com.ecjia.widgets.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.SelectableRoundedImageView;
import com.ecjia.view.activity.TopicListActivity;
import com.ecjia.entity.responsemodel.QUICK;
import com.ecjia.util.ecjiaopentype.ECJiaOpenType;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Adam on 2016-06-13.
 */
public class ThemeStreets extends HomeView<QUICK> {

    private int radius;
    private LinearLayout home_theme_view;
    private LinearLayout home_theme_item;
    private LinearLayout home_more_theme;

    private List<SelectableRoundedImageView> imageViews = new ArrayList<>();
    int sideDistance;
    int layoutHeight;
    private LinearLayout home_theme_above;
    private LinearLayout home_theme_below;
    private SelectableRoundedImageView imageView0, imageView1, imageView2, imageView3, imageView4, imageView5,
            imageView6;

    private ImageView theme_icon;


    public ThemeStreets(Activity activity) {
        super(activity);
    }

    @Override
    protected void init() {
        sideDistance = mActivity.getResources().getDimensionPixelOffset(R.dimen.dp_10);
        layoutHeight = (width - 2 * sideDistance) / 2;
        radius = (int) mActivity.getResources().getDimension(R.dimen.dp_5);
        home_theme_view = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.home_theme_view, null);

        //主题类
        home_theme_item = (LinearLayout) home_theme_view.findViewById(R.id.home_theme_big_item);
        home_theme_item.setVisibility(View.GONE);
        theme_icon = (ImageView) home_theme_view.findViewById(R.id.home_theme_icon);

        home_more_theme = (LinearLayout) home_theme_view.findViewById(R.id.home_more_theme);
        home_more_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, TopicListActivity.class);
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        home_theme_above = (LinearLayout) home_theme_view.findViewById(R.id.home_theme_above);
        home_theme_below = (LinearLayout) home_theme_view.findViewById(R.id.home_theme_below);
        home_theme_above.setVisibility(View.GONE);
        home_theme_below.setVisibility(View.GONE);

        if (mActivity.getResources().getConfiguration().locale.equals(Locale.CHINA)) {
            setThemeIcon(R.drawable.theme_icon_chinese);
        } else {
            setThemeIcon(R.drawable.theme_icon_english);
        }
    }

    @Override
    public void setVisible() {

    }

    @Override
    public void addToListView(ListView listView) {
        listView.addHeaderView(home_theme_view);
    }

    @Override
    public void createView(final ArrayList<QUICK> dataList) {
        if (dataList == null || dataList.size() == 0) {
            home_theme_item.setVisibility(View.GONE);
            home_theme_above.setVisibility(View.GONE);
            home_theme_below.setVisibility(View.GONE);
            return;
        }
        mDataList = dataList;
        home_theme_item.setVisibility(View.VISIBLE);
        home_theme_above.removeAllViews();
        home_theme_below.removeAllViews();
        imageViews.clear();
        if (dataList.size() > 0 && dataList.size() <= 2) {
            createAboveView();
        } else if (dataList.size() > 2 && dataList.size() <= 5) {
            createAboveView();
            createBelowView();
        } else if (dataList.size() > 5) {
            createAboveViewFour();
            createBelowViewFour();
        }
        setImageViewOnClick(dataList);
    }


    private void setImageViewOnClick(final List<QUICK> dataList) {
        int i = 0;
        while (i < Math.min(imageViews.size(), dataList.size())) {
            ImageLoader.getInstance().displayImage(dataList.get(i).getImg(), imageViews.get(i));
            final int j = i;
            imageViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ECJiaOpenType.getDefault().startAction(mActivity, dataList.get(j).getUrl());
                }
            });
            i++;
        }

    }

    private void setThemeIcon(int resId) {
        theme_icon.setImageResource(resId);
    }

    /**
     * 最多只有2个
     */
    private void createAboveView() {
        home_theme_above.setVisibility(View.VISIBLE);
        LinearLayout linearLayout = new LinearLayout(mActivity);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(2 * layoutHeight, layoutHeight);
        linearLayout.setLayoutParams(params);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                layoutHeight);
        params2.weight = 1;
        params2.gravity = Gravity.CENTER;
        imageView0 = new SelectableRoundedImageView(mActivity);
        imageView1 = new SelectableRoundedImageView(mActivity);
        imageView0.setLayoutParams(params2);
        imageView1.setLayoutParams(params2);
        imageView0.setCornerRadius(radius, 0, radius, 0);
        imageView1.setCornerRadius(0, radius, 0, radius);

        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(1, layoutHeight);
        View view = new View(mActivity);
        view.setBackgroundColor(Color.parseColor("#DDDDDD"));
        view.setLayoutParams(params3);

        linearLayout.addView(imageView0);
        linearLayout.addView(view);
        linearLayout.addView(imageView1);

        home_theme_above.addView(linearLayout);
        imageViews.add(imageView0);
        imageViews.add(imageView1);
    }

    /**
     * 最多只有2个
     */
    private void createAboveViewFour() {
        home_theme_above.setVisibility(View.VISIBLE);
        LinearLayout linearLayout = new LinearLayout(mActivity);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(2 * layoutHeight, layoutHeight);
        linearLayout.setLayoutParams(params);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                layoutHeight);
        params2.weight = 1;
        params2.gravity = Gravity.CENTER;
        imageView0 = new SelectableRoundedImageView(mActivity);
        imageView1 = new SelectableRoundedImageView(mActivity);
        imageView0.setLayoutParams(params2);
        imageView1.setLayoutParams(params2);
        imageView0.setCornerRadius(radius, 0, 0, 0);
        imageView1.setCornerRadius(0, radius, 0, 0);

        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(1, layoutHeight);
        View viewAbove = new View(mActivity);
        viewAbove.setBackgroundColor(Color.parseColor("#DDDDDD"));
        viewAbove.setLayoutParams(params3);

        linearLayout.addView(imageView0);
        linearLayout.addView(viewAbove);
        linearLayout.addView(imageView1);

        home_theme_above.addView(linearLayout);

        LinearLayout linearLayout2 = new LinearLayout(mActivity);
        linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout2.setLayoutParams(params);

        imageView2 = new SelectableRoundedImageView(mActivity);
        imageView3 = new SelectableRoundedImageView(mActivity);
        imageView2.setLayoutParams(params2);
        imageView3.setLayoutParams(params2);

        imageView2.setCornerRadius(0, 0, radius, 0);
        imageView3.setCornerRadius(0, 0, 0, radius);

        View viewBelow = new View(mActivity);
        viewBelow.setBackgroundColor(Color.parseColor("#DDDDDD"));
        viewBelow.setLayoutParams(params3);

        linearLayout2.addView(imageView2);
        linearLayout2.addView(viewBelow);
        linearLayout2.addView(imageView3);

        View view1 = new View(mActivity);
        view1.setBackgroundColor(Color.parseColor("#DDDDDD"));
        LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(width, 1);
        view1.setLayoutParams(params4);
        home_theme_above.addView(view1);
        home_theme_above.addView(linearLayout2);


        imageViews.add(imageView0);
        imageViews.add(imageView1);
        imageViews.add(imageView2);
        imageViews.add(imageView3);

    }

    private void createBelowView() {
        home_theme_below.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((width - 4 * sideDistance) / 3, (width -
                4 * sideDistance) / 3);
        params.setMargins(radius, 0, radius, 0);

        LinearLayout layout2 = new LinearLayout(mActivity);
        layout2.setLayoutParams(params);

        LinearLayout layout3 = new LinearLayout(mActivity);
        layout3.setLayoutParams(params);

        LinearLayout layout4 = new LinearLayout(mActivity);
        layout4.setLayoutParams(params);

        layout2.setPadding(1, 1, 1, 1);
        layout3.setPadding(1, 1, 1, 1);
        layout4.setPadding(1, 1, 1, 1);

        layout2.setBackgroundResource(R.drawable.selecter_raditem_press);
        layout3.setBackgroundResource(R.drawable.selecter_raditem_press);
        layout4.setBackgroundResource(R.drawable.selecter_raditem_press);

        imageView2 = new SelectableRoundedImageView(mActivity);
        imageView3 = new SelectableRoundedImageView(mActivity);
        imageView4 = new SelectableRoundedImageView(mActivity);

        imageView2.setCornerRadius(radius, radius, radius, radius);
        imageView3.setCornerRadius(radius, radius, radius, radius);
        imageView4.setCornerRadius(radius, radius, radius, radius);

        LinearLayout.LayoutParams paramsImage = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        imageView2.setLayoutParams(paramsImage);
        imageView3.setLayoutParams(paramsImage);
        imageView4.setLayoutParams(paramsImage);

        layout2.addView(imageView2);
        layout3.addView(imageView3);
        layout4.addView(imageView4);

        home_theme_below.addView(layout2);
        home_theme_below.addView(layout3);
        home_theme_below.addView(layout4);

        imageViews.add(imageView2);
        imageViews.add(imageView3);
        imageViews.add(imageView4);
    }

    private void createBelowViewFour() {
        home_theme_below.setVisibility(View.VISIBLE);

        LinearLayout.LayoutParams params5 = new LinearLayout.LayoutParams((width - 4 * sideDistance) / 3, (width -
                4 * sideDistance) / 3);

        params5.setMargins(radius, 0, radius, 0);

        LinearLayout layout4 = new LinearLayout(mActivity);
        layout4.setLayoutParams(params5);

        LinearLayout layout5 = new LinearLayout(mActivity);
        layout5.setLayoutParams(params5);

        LinearLayout layout6 = new LinearLayout(mActivity);
        layout6.setLayoutParams(params5);

        layout4.setPadding(1, 1, 1, 1);
        layout5.setPadding(1, 1, 1, 1);
        layout6.setPadding(1, 1, 1, 1);

        layout4.setBackgroundResource(R.drawable.selecter_raditem_press);
        layout5.setBackgroundResource(R.drawable.selecter_raditem_press);
        layout6.setBackgroundResource(R.drawable.selecter_raditem_press);

        imageView4 = new SelectableRoundedImageView(mActivity);
        imageView5 = new SelectableRoundedImageView(mActivity);
        imageView6 = new SelectableRoundedImageView(mActivity);

        imageView4.setCornerRadius(radius, radius, radius, radius);
        imageView5.setCornerRadius(radius, radius, radius, radius);
        imageView6.setCornerRadius(radius, radius, radius, radius);

        LinearLayout.LayoutParams paramsImage = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        imageView4.setLayoutParams(paramsImage);
        imageView5.setLayoutParams(paramsImage);
        imageView6.setLayoutParams(paramsImage);

        layout4.addView(imageView4);
        layout5.addView(imageView5);
        layout6.addView(imageView6);

        home_theme_below.addView(layout4);
        home_theme_below.addView(layout5);
        home_theme_below.addView(layout6);

        imageViews.add(imageView4);
        imageViews.add(imageView5);
        imageViews.add(imageView6);

        if (mDataList.size() < 7) {
            layout6.setVisibility(View.INVISIBLE);
        } else {
            layout6.setVisibility(View.VISIBLE);
        }
    }

}
