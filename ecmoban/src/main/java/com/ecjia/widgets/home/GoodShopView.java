package com.ecjia.widgets.home;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.MyListView;
import com.ecjia.view.activity.ShopListFragmentActivity;
import com.ecjia.view.adapter.HomeSellerAdapter;
import com.ecjia.entity.responsemodel.SELLERINFO;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Adam on 2016/10/18.
 */
public class GoodShopView extends HomeView<SELLERINFO> {
    //发现好店
    private LinearLayout home_seller_view;
    private LinearLayout home_seller_item;
    private MyListView home_seller_listview;
    private LinearLayout home_more_shop;
    private HomeSellerAdapter homeSellerAdapter;

    private ImageView goodshop_icon;

    public GoodShopView(Activity activity) {
        super(activity);
    }


    @Override
    protected void init() {
        super.init();
        home_seller_view = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.home_sellers_view, null);
        goodshop_icon = (ImageView) home_seller_view.findViewById(R.id.home_seller_icon);
        home_seller_item = (LinearLayout) home_seller_view.findViewById(R.id.home_seller_big_item);
        home_seller_listview = (MyListView) home_seller_view.findViewById(R.id.home_seller_listview);
        home_more_shop = (LinearLayout) home_seller_view.findViewById(R.id.home_more_shop);
        home_more_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, ShopListFragmentActivity.class);
                mActivity.startActivity(intent);
            }
        });

        if (mActivity.getResources().getConfiguration().locale.equals(Locale.CHINA)) {
            setThemeIcon(R.drawable.goodshop_icon_chinese);
        } else {
            setThemeIcon(R.drawable.goodshop_icon_english);
        }
    }


    private void setThemeIcon(int resId) {
        goodshop_icon.setImageResource(resId);
    }

    @Override
    public void setVisible() {
        home_seller_item.setVisibility(View.VISIBLE);
    }

    @Override
    public void addToListView(ListView listView) {
        listView.addHeaderView(home_seller_view);
    }

    @Override
    public void createView(ArrayList<SELLERINFO> dataList) {
        if (dataList == null || dataList.size() == 0) {
            home_seller_item.setVisibility(View.GONE);
            return;
        }
        home_seller_item.setVisibility(View.VISIBLE);

        mDataList = dataList;
    }
}
