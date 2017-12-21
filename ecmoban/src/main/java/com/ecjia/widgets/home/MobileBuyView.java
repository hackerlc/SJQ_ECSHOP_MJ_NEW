package com.ecjia.widgets.home;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.MyGridView;
import com.ecjia.view.activity.MobilebuyGoodsActivity;
import com.ecjia.view.adapter.MobileBuyGridAdapter;
import com.ecjia.entity.responsemodel.MOBILEGOODS;

import java.util.ArrayList;

/**
 * 手机专享
 * Created by Adam on 2016/7/13.
 */
public class MobileBuyView extends HomeView<MOBILEGOODS> {
    private LinearLayout mobileView;
    private LinearLayout mobileView_in;
    private LinearLayout ll_mobile_more;
    private MyGridView mgridView;
    private MobileBuyGridAdapter mmGridViewAdapter;

    public MobileBuyView(Activity activity) {
        super(activity);
    }

    @Override
    protected void init() {
        super.init();
        mobileView = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.home_mobile_buy, null);
        mobileView_in = (LinearLayout) mobileView.findViewById(R.id.homefragment_mb);
        ll_mobile_more = (LinearLayout) mobileView.findViewById(R.id.mobile_getmore);
        ll_mobile_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, MobilebuyGoodsActivity.class);
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        mgridView = (MyGridView) mobileView.findViewById(R.id.mymb_item);
    }

    @Override
    public void setVisible() {

    }

    @Override
    public void addToListView(ListView listView) {
        listView.addHeaderView(mobileView);
    }

    @Override
    public void createView(ArrayList<MOBILEGOODS> dataList) {
        if (dataList == null || dataList.size() == 0) {
            mobileView_in.setVisibility(View.GONE);
            return;
        }
        mobileView_in.setVisibility(View.VISIBLE);
        if (null == mmGridViewAdapter) {
            mmGridViewAdapter = new MobileBuyGridAdapter(mActivity);
            mmGridViewAdapter.setAdapterDate(dataList);
            mgridView.setAdapter(mmGridViewAdapter);
        } else {
            mmGridViewAdapter.notifyDataSetChanged();
        }
    }
}
