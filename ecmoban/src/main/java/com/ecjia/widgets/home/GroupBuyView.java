package com.ecjia.widgets.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.view.activity.GroupbuyGoodsActivity;
import com.ecjia.view.adapter.HomeGroupGoodsAdapter;
import com.ecjia.entity.responsemodel.GROUPGOODS;
import com.ecjia.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Adam on 2016/10/17.
 */
public class GroupBuyView extends HomeView<GROUPGOODS> implements AutoPlayInterface, IgnoredInterface {
    private LinearLayout group_view;
    private LinearLayout group_item;
    private RecyclerView grouplistview;
    private LinearLayout ll_group_more;
    private HomeGroupGoodsAdapter groupGoodAdapter;
    private SimpleDateFormat format;

    public GroupBuyView(Activity activity) {
        super(activity);
    }

    @Override
    protected void init() {
        super.init();
        group_view = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.home_group_goods, null);
        group_item = (LinearLayout) group_view.findViewById(R.id.home_groupgoodlist_item);
        grouplistview = (RecyclerView) group_view.findViewById(R.id.home_groupgoodlist);
        ll_group_more = (LinearLayout) group_view.findViewById(R.id.group_getmore);
        ll_group_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, GroupbuyGoodsActivity.class);
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        LinearLayoutManager  mLayoutManager = new LinearLayoutManager(mActivity);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        grouplistview.setLayoutManager(mLayoutManager);

    }

    @Override
    public void setVisible() {

    }

    @Override
    public void addToListView(ListView listView) {
        listView.addHeaderView(group_view);
    }

    @Override
    public void createView(ArrayList<GROUPGOODS> dataList) {
        if (dataList == null || dataList.size() == 0) {
            group_item.setVisibility(View.GONE);
            return;
        }

        group_item.setVisibility(View.VISIBLE);
        mDataList = dataList;
        if (null == groupGoodAdapter) {
            try {
                initGridAdapterData();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            groupGoodAdapter = new HomeGroupGoodsAdapter(mActivity, dataList);
        } else {
            groupGoodAdapter.notifyDataSetChanged();
        }
        grouplistview.setAdapter(groupGoodAdapter);
        format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        currentDateTime = format.format(new Date());
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onDetach() {

    }

    @Override
    public void startPlay() {

    }

    @Override
    public View getIgnoredView() {
        return grouplistview;
    }


    private void initGridAdapterData() throws InterruptedException {

        for (int i = 0; i < mDataList.size(); i++) {
            if (TextUtils.isEmpty(mDataList.get(i).getPromote_end_date())) {
                mDataList.get(i).setPromote_end_date("2000-01-01 00:00:00");
            }
        }

        isExit = false;
        if (shopThread != null) {
            shopThread.interrupt();
            // shopThread = new ShopThread();
        } else {
            shopThread = new ShopThread();
        }
        if (!shopThread.isAlive()) {
            shopThread.start();
        }
    }

    private ShopThread shopThread;//团购倒计时线程


    private String currentDateTime;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int shopEndCount = 0; // 专场结束的个数
                for (int i = 0; i < mDataList.size(); i++) {
                    String remainDateTime = TimeUtil.getRemainTime(currentDateTime, mDataList.get(i).getPromote_end_date());
                    if (remainDateTime.equals("0")) {
                        shopEndCount++; // 累加结束的专场数
                        remainDateTime = "活动已结束";
                    }
                    mDataList.get(i).setRaminTime(remainDateTime);
                }
                if (shopEndCount == mDataList.size()) {
                    /* 所有活动都结束，则终止线程 */
                    isExit = true;
                }
                currentDateTime = TimeUtil.getDateAddOneSecond(currentDateTime); // 累加服务时间
                groupGoodAdapter.notifyDataSetChanged();
            }

        }
    };

    private boolean isExit = false; // 判断是否断开线程

    //团购倒计时控制线程
    private class ShopThread extends Thread {

        @Override
        public void run() {
            while (!isExit) {
                Message msg = Message.obtain();
                msg.what = 1;
                mHandler.sendMessage(msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("结束");
        }
    }
}
