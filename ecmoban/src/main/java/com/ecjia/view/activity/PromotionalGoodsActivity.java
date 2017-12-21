package com.ecjia.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.SuggestListModel;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.ErrorView;
import com.ecjia.widgets.ToastView;
import com.ecjia.widgets.XListView;
import com.ecjia.view.adapter.NewPromoteListAdapter;
import com.ecjia.view.adapter.PromotionalGoodsListAdapter;
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
 * 促销商品页面
 * Created by Administrator on 2015/8/18.
 */
public class PromotionalGoodsActivity extends BaseActivity implements HttpResponse, XListView.IXListViewListener {
    private XListView listView;
    private PromotionalGoodsListAdapter adapter;
    private NewPromoteListAdapter newPromoteListAdapter;
    private SuggestListModel model;
    private ErrorView nullpage;
    final String ACTION_TYPE_HOT = "hot";
    final String ACTION_TYPE_NEW = "new";
    final String ACTION_TYPE_BEST = "best";
    final String ACTION_TYPE_PROMOTION = "promotion";
    private String suggest_type = ACTION_TYPE_NEW;

    private String currentDateTime;
    private boolean isExit;
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int shopEndCount = 0; // 专场结束的个数
                for (int i = 0; i < model.suggests.size(); i++) {
                    int type=TimeUtil.compareTime(currentDateTime, model.suggests.get(i).getPromote_start_date());
                    int type2=TimeUtil.compareTime(currentDateTime, model.suggests.get(i).getPromote_end_date());
                    switch (type){
                        case 0:
                            String str0="距离活动结束还有";
                            if(type2==0||type2==1){
                                shopEndCount++; // 累加结束的专场数
                                str0="活动已结束";
                            }
                            PROMOTETIME promotetime0=new PROMOTETIME(
                                    str0+TimeUtil.getRemainTime(currentDateTime, model.suggests.get(i).getPromote_end_date(),0)+"天",
                                    TimeUtil.getRemainTime(currentDateTime, model.suggests.get(i).getPromote_end_date(),1),
                                    TimeUtil.getRemainTime(currentDateTime, model.suggests.get(i).getPromote_end_date(),2),
                                    TimeUtil.getRemainTime(currentDateTime, model.suggests.get(i).getPromote_end_date(),3)
                            );
                            model.suggests.get(i).setPromotetime(promotetime0);
                            break;
                        case 1:
                            String str="距离活动结束还有";
                            if(type2==0||type2==1){
                                shopEndCount++; // 累加结束的专场数
                                str="活动已结束";
                            }
                            PROMOTETIME promotetime=new PROMOTETIME(
                                    str+TimeUtil.getRemainTime(currentDateTime, model.suggests.get(i).getPromote_end_date(),0)+"天",
                                    TimeUtil.getRemainTime(currentDateTime, model.suggests.get(i).getPromote_end_date(),1),
                                    TimeUtil.getRemainTime(currentDateTime, model.suggests.get(i).getPromote_end_date(),2),
                                    TimeUtil.getRemainTime(currentDateTime, model.suggests.get(i).getPromote_end_date(),3)
                            );
                            model.suggests.get(i).setPromotetime(promotetime);
                            break;
                        case -1:
                            PROMOTETIME promotetime2=new PROMOTETIME(
                                    "距离活动开始还有"+TimeUtil.getRemainTime(model.suggests.get(i).getPromote_start_date(),currentDateTime,0),
                                    TimeUtil.getRemainTime( model.suggests.get(i).getPromote_start_date(),currentDateTime,1),
                                    TimeUtil.getRemainTime( model.suggests.get(i).getPromote_start_date(),currentDateTime,2),
                                    TimeUtil.getRemainTime( model.suggests.get(i).getPromote_start_date(),currentDateTime,3)
                            );
                            model.suggests.get(i).setPromotetime(promotetime2);
                            break;
                    }

                }
                if (shopEndCount == model.suggests.size()) {
                    /* 所有活动都结束，则终止线程 */
                    isExit = true;
                }
                currentDateTime = TimeUtil.getDateAddOneSecond(currentDateTime); // 累加服务时间
                newPromoteListAdapter.notifyDataSetChanged();
            }

        }
    };
    private ShopThread shopThread;
    private SimpleDateFormat format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.activity_promotionalgoods);
        initView();
    }

    //初始化布局
    private void initView() {
        suggest_type = getIntent().getStringExtra("type");
        if (TextUtils.isEmpty(suggest_type)) {
            suggest_type = ACTION_TYPE_NEW;
        }

        nullpage = (ErrorView) findViewById(R.id.mobile_null_pager);
        initTopView();
        listView = (XListView) findViewById(R.id.mobile_listview);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(this, 1);
        model = new SuggestListModel(this);
        model.addResponseListener(this);
        model.getSuggestList(suggest_type);
        adapter = new PromotionalGoodsListAdapter(this, model.suggests);
        newPromoteListAdapter = new NewPromoteListAdapter(this, model.suggests);
        if(suggest_type.equals(ACTION_TYPE_PROMOTION)){
            listView.setAdapter(newPromoteListAdapter);
        }else{
            listView.setAdapter(adapter);
        }
    }


    @Override
    public void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.promotion_topview);
        topView.setLeftType(ECJiaTopView.LEFT_TYPE_BACKIMAGE);
        topView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        switch (suggest_type) {
            case ACTION_TYPE_HOT:
                topView.setTitleText(R.string.suggest_hot);
                break;
            case ACTION_TYPE_NEW:
                topView.setTitleText(R.string.newgoods);
                nullpage.setErrorImageResource(R.drawable.null_newgoods);
                break;
            case ACTION_TYPE_BEST:
                topView.setTitleText(R.string.suggest_best);

                break;
            case ACTION_TYPE_PROMOTION:
                if (getResources().getConfiguration().locale.equals(Locale.CHINA)) {
                    topView.setTitleImage(R.drawable.icon_title_promotion);
                } else {
                    topView.setTitleImage(R.drawable.icon_title_promotion_en);
                }
                nullpage.setErrorImageResource(R.drawable.null_promotion);
                break;
            default:
                topView.setTitleText(R.string.newgoods);
                break;
        }
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
        if (url == ProtocolConst.GOODS_SUGGESTLIST) {
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
                ToastView toastView = new ToastView(this, getResources().getString(R.string.payment_network_problem));
                toastView.show();
            }
        }
    }

    //设置列表信息
    private void setGoodInfo() {
        if (model.suggests.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            nullpage.setVisibility(View.GONE);
            if(suggest_type.equals(ACTION_TYPE_PROMOTION)) {
                newPromoteListAdapter.setData(model.suggests);
                newPromoteListAdapter.notifyDataSetChanged();
                try {
                    initGridAdapterData();
                } catch (InterruptedException e) {
                }

                format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                currentDateTime = format.format(new Date());
            }else{
                adapter.setData(model.suggests);
                adapter.notifyDataSetChanged();
            }
        } else {
            listView.setVisibility(View.GONE);
            nullpage.setVisibility(View.VISIBLE);
            if(suggest_type.equals(ACTION_TYPE_PROMOTION)){
                isExit=true;
                if(shopThread!=null){
                    shopThread.interrupt();
                    shopThread=null;
                }
            }
        }
    }

    private void initGridAdapterData() throws InterruptedException {
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
        if(suggest_type.equals(ACTION_TYPE_PROMOTION)){
            isExit=true;
            if(shopThread!=null){
                shopThread.interrupt();
                shopThread=null;
            }
        }
        model.getSuggestList(suggest_type);
    }

    @Override
    public void onLoadMore(int id) {
        model.getSuggestListMore(suggest_type);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(suggest_type.equals(ACTION_TYPE_PROMOTION)){
            isExit=true;
            if(shopThread!=null){
                shopThread.interrupt();
                shopThread=null;
            }
        }
    }
}
