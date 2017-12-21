package com.ecjia.widgets.home;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.MyGridView;
import com.ecjia.view.adapter.QuickEnterAdapter;
import com.ecjia.entity.responsemodel.QUICK;

import java.util.ArrayList;

/**
 * Created by Adam on 2016/7/18.
 */
public class QuickEnter extends HomeView<QUICK> {
    private LinearLayout quick_new_item;
    private LinearLayout quickshowitem;
    private MyGridView quickGridView;
    private QuickEnterAdapter quickEnterAdapter;

    public QuickEnter(Activity activity) {
        super(activity);
    }


    @Override
    protected void init() {
        super.init();
        quick_new_item = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.quick_new_item, null);
        quickshowitem = (LinearLayout) quick_new_item.findViewById(R.id.quick_showview);
        quickGridView = (MyGridView) quick_new_item.findViewById(R.id.quick_gradview);
    }

    @Override
    public void setVisible() {

    }

    @Override
    public void addToListView(ListView listView) {
        listView.addHeaderView(quick_new_item);
    }

    @Override
    public void createView(ArrayList<QUICK> dataList) {
        if (dataList == null || dataList.size() == 0) {
            quickshowitem.setVisibility(View.GONE);
            return;
        }
        quickshowitem.setVisibility(View.VISIBLE);
        if (null == quickEnterAdapter) {
            quickEnterAdapter = new QuickEnterAdapter(mActivity, dataList);
            quickGridView.setAdapter(quickEnterAdapter);
        } else {
            quickEnterAdapter.notifyDataSetChanged();
        }
    }

}
