package com.ecjia.widgets.home;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.MyListView;
import com.ecjia.view.adapter.EventAdapter;
import com.ecjia.entity.responsemodel.ADSENSE_GROUP;

import java.util.ArrayList;

/**
 * Created by Adam on 2016/10/18.
 */
public class EventView extends HomeView<ADSENSE_GROUP> {

    //event
    private LinearLayout event_item;
    private LinearLayout event;
    private MyListView lv_event;
    private EventAdapter eventAdapter;

    public EventView(Activity activity) {
        super(activity);
    }

    @Override
    protected void init() {
        super.init();
        event = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.home_event, null);
        event_item = (LinearLayout) event.findViewById(R.id.event_item);
        lv_event = (MyListView) event.findViewById(R.id.lv_event);
    }

    @Override
    public void setVisible() {

    }

    @Override
    public void addToListView(ListView listView) {
        listView.addHeaderView(event);
    }

    @Override
    public void createView(ArrayList<ADSENSE_GROUP> dataList) {
        if (null != dataList && dataList.size() > 0) {
            event_item.setVisibility(View.VISIBLE);
        } else {
            event_item.setVisibility(View.GONE);
            return;
        }
        mDataList = dataList;
        if (eventAdapter == null) {
            eventAdapter = new EventAdapter(mActivity, mDataList);
            lv_event.setAdapter(eventAdapter);
        }
        eventAdapter.notifyDataSetChanged();
    }
}
