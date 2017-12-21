package com.ecjia.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.GroupGoodModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.ErrorView;
import com.ecjia.widgets.ToastView;
import com.ecjia.widgets.XListView;
import com.ecjia.view.adapter.GroupBuyListAdapter;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.PROMOTETIME;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.TimeUtil;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 团购页面
 * Created by Administrator on 2015/8/18.
 */
public class GroupbuyGoodsActivity extends BaseActivity implements HttpResponse, XListView.IXListViewListener {
    private XListView listView;
    private GroupBuyListAdapter adapter;
    private GroupGoodModel model;
    private ErrorView nullpage;

    private String currentDateTime;
    private boolean isExit;
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int shopEndCount = 0; // 专场结束的个数
                for (int i = 0; i < model.groupgoodsArrayList.size(); i++) {
                    int type= TimeUtil.compareTime(currentDateTime, model.groupgoodsArrayList.get(i).getPromote_start_date());
                    int type2=TimeUtil.compareTime(currentDateTime, model.groupgoodsArrayList.get(i).getPromote_end_date());
                    switch (type){
                        case 0:
                            String str0="距离活动结束还有";
                            if(type2==0||type2==1){
                                shopEndCount++; // 累加结束的专场数
                                str0="活动已结束";
                            }
                            PROMOTETIME promotetime0=new PROMOTETIME(
                                    str0+TimeUtil.getRemainTime(currentDateTime, model.groupgoodsArrayList.get(i).getPromote_end_date(),0)+"天",
                                    TimeUtil.getRemainTime(currentDateTime, model.groupgoodsArrayList.get(i).getPromote_end_date(),1),
                                    TimeUtil.getRemainTime(currentDateTime, model.groupgoodsArrayList.get(i).getPromote_end_date(),2),
                                    TimeUtil.getRemainTime(currentDateTime, model.groupgoodsArrayList.get(i).getPromote_end_date(),3)
                            );
                            model.groupgoodsArrayList.get(i).setPromotetime(promotetime0);
                            break;
                        case 1:
                            String str="距离活动结束还有";
                            if(type2==0||type2==1){
                                shopEndCount++; // 累加结束的专场数
                                str="活动已结束";
                            }
                            PROMOTETIME promotetime=new PROMOTETIME(
                                    str+TimeUtil.getRemainTime(currentDateTime, model.groupgoodsArrayList.get(i).getPromote_end_date(),0)+"天",
                                    TimeUtil.getRemainTime(currentDateTime, model.groupgoodsArrayList.get(i).getPromote_end_date(),1),
                                    TimeUtil.getRemainTime(currentDateTime, model.groupgoodsArrayList.get(i).getPromote_end_date(),2),
                                    TimeUtil.getRemainTime(currentDateTime, model.groupgoodsArrayList.get(i).getPromote_end_date(),3)
                            );
                            model.groupgoodsArrayList.get(i).setPromotetime(promotetime);
                            break;
                        case -1:
                            PROMOTETIME promotetime2=new PROMOTETIME(
                                    "距离活动开始还有"+TimeUtil.getRemainTime(model.groupgoodsArrayList.get(i).getPromote_start_date(),currentDateTime,0),
                                    TimeUtil.getRemainTime( model.groupgoodsArrayList.get(i).getPromote_start_date(),currentDateTime,1),
                                    TimeUtil.getRemainTime( model.groupgoodsArrayList.get(i).getPromote_start_date(),currentDateTime,2),
                                    TimeUtil.getRemainTime( model.groupgoodsArrayList.get(i).getPromote_start_date(),currentDateTime,3)
                            );
                            model.groupgoodsArrayList.get(i).setPromotetime(promotetime2);
                            break;
                    }

                }
                if (shopEndCount == model.groupgoodsArrayList.size()) {
                    /* 所有活动都结束，则终止线程 */
                    isExit = true;
                }
                currentDateTime = TimeUtil.getDateAddOneSecond(currentDateTime); // 累加服务时间
                adapter.notifyDataSetChanged();
            }

        }
    };
    private ShopThread shopThread;
    private SimpleDateFormat format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupgoods);
        initTopView();
        initView();
    }

    @Override
    public void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.group_topview);
        topView.setLeftType(ECJiaTopView.LEFT_TYPE_BACKIMAGE);
        topView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (getResources().getConfiguration().locale.equals(Locale.CHINA)) {
            topView.setTitleImage(R.drawable.icon_title_groupbuy);
        } else {
            topView.setTitleImage(R.drawable.icon_title_groupbuy_en);
        }
    }

    //初始化布局
    private void initView() {
        PushAgent.getInstance(this).onAppStart();
        model = new GroupGoodModel(this);
        listView = (XListView) findViewById(R.id.group_listview);
        nullpage = (ErrorView) findViewById(R.id.group_null_pager);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(this, 1);
        model.addResponseListener(this);
        model.getGroupGoods();
        adapter = new GroupBuyListAdapter(this, model.groupgoodsArrayList);
        listView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.GOODS_GROUPBUY) {
            if (status.getSucceed() == 1) {
                listView.setRefreshTime();
                listView.stopRefresh();
                listView.stopLoadMore();
                PAGINATED paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                if (paginated.getMore() == 1) {
                    listView.setPullLoadEnable(true);
                } else {
                    listView.setPullLoadEnable(false);
                }
                setGoodInfo();

            } else {
                listView.setVisibility(View.GONE);
                nullpage.setVisibility(View.VISIBLE);
                ToastView toastView=new ToastView(this,getResources().getString(R.string.payment_network_problem));
                toastView.show();
            }
        }
    }
    //设置列表信息
    private void setGoodInfo() {
        if (model.groupgoodsArrayList.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            nullpage.setVisibility(View.GONE);
            adapter.setData(model.groupgoodsArrayList);
            adapter.notifyDataSetChanged();
            try {
                initData();
            } catch (InterruptedException e) {
            }

            format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            currentDateTime = format.format(new Date());
        } else {
            listView.setVisibility(View.GONE);
            nullpage.setVisibility(View.VISIBLE);
            isExit=true;
            if(shopThread!=null){
                shopThread.interrupt();
                shopThread=null;
            }
        }
    }

    private void initData() throws InterruptedException {
        isExit = false;
        if (shopThread != null) {
            shopThread.interrupt();
        } else {
            shopThread = new ShopThread();
        }
        if (!shopThread.isInterrupted()) {
            shopThread.start();
        }
    }

    private class ShopThread extends Thread {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (!isExit) {
                mHandler.sendEmptyMessage(1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRefresh(int id) {
        isExit=true;
        if(shopThread!=null){
            shopThread.interrupt();
            shopThread=null;
        }
        model.getGroupGoods();
    }

    @Override
    public void onLoadMore(int id) {
        model.getGroupGoodsMore();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
            isExit=true;
            if(shopThread!=null){
                shopThread.interrupt();
                shopThread=null;
            }
    }
}
