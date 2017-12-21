package com.ecjia.view.activity.goodsorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.ecjia.base.BaseActivity;
import com.ecjia.view.activity.SearchOrderActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.OrderModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.ErrorView;
import com.ecjia.widgets.XListView;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.adapter.TradeAdapter;
import com.ecjia.entity.responsemodel.ORDER_GOODS_LIST;
import com.ecjia.entity.responsemodel.ORDER_INFO;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.EventBus.MyEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
/**
 * 所有订单
 */
public class OrderListAllActivity extends BaseActivity implements XListView.IXListViewListener {

    private XListView xlistView;
    private TradeAdapter tradeAdapter;
    private ErrorView null_paView;

    private OrderModel orderModel;
    ORDER_INFO order_info;

    private Intent intent;
    public boolean orderupdate = false;
    private boolean isreset = false;
    private int buyAgain;
    private ArrayList<ORDER_GOODS_LIST> buylist;

    private String orderType;
    private View searchlayout_in;
    private View searchlayout_bg;
    private View ll_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_order_list_all);
        EventBus.getDefault().register(this);
        initData();
        initView();
        initModel();
    }

    void initView() {
        initTopView();
        null_paView = (ErrorView) findViewById(R.id.null_pager);
        xlistView = (XListView) findViewById(R.id.trade_list);
        xlistView.setPullLoadEnable(true);
        xlistView.setRefreshTime();
        xlistView.setXListViewListener(this, 1);

        ll_search = findViewById(R.id.ll_search);
        searchlayout_in = findViewById(R.id.order_list_searchlayout_in);
        searchlayout_bg = findViewById(R.id.order_list_searchlayout_bg);
        findViewById(R.id.order_list_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -res.getDimension(R.dimen.dp_48));
                ScaleAnimation animation1 = new ScaleAnimation(1f, 0.82f, 1f, 1f);
                int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
                int leftMagin = (int) getResources().getDimension(R.dimen.ten_margin);
                TranslateAnimation animation2 = new TranslateAnimation(0, -screenWidth / 2 + 2 * leftMagin + searchlayout_in.getWidth() / 2, 0, 0);
                animation.setDuration(300);
                animation1.setDuration(300);
                animation2.setDuration(300);
                animation.setFillAfter(true);
                animation1.setFillAfter(true);
                animation2.setFillAfter(true);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent(OrderListAllActivity.this, SearchOrderActivity.class);
                        intent.putExtra(IntentKeywords.ORDER_TYPE, orderType);
                        startActivityForResult(intent, 100);
                        overridePendingTransition(R.anim.animation_2, 0);
                    }
                });
                ll_search.startAnimation(animation);
                searchlayout_bg.startAnimation(animation1);
                searchlayout_in.startAnimation(animation2);
            }
        });
    }

    void initData() {
        orderType = getIntent().getStringExtra(IntentKeywords.ORDER_TYPE);
        if (TextUtils.isEmpty(orderType)) {
            orderType = "";
        }
    }

    void initModel() {
        orderModel = new OrderModel(this);
        orderModel.addResponseListener(this);
        orderModel.getOrder("", "", true);
    }

    @Override
    public void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.orderlist_topview);
        topView.setLeftBackImage(BACKIMAGE_ID, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topView.setTitleText(R.string.order_all_orders);
    }

    public void setOrder() {
        if (null == tradeAdapter) {
            tradeAdapter = new TradeAdapter(this, orderModel.order_list, "");
            xlistView.setAdapter(tradeAdapter);
        } else {
            tradeAdapter.notifyDataSetChanged();
        }

        if (orderModel.order_list.size() == 0) {
            null_paView.setVisibility(View.VISIBLE);
            xlistView.setVisibility(View.GONE);
        } else {
            null_paView.setVisibility(View.GONE);
            xlistView.setVisibility(View.VISIBLE);
        }

        if (isreset) {
            xlistView.setSelection(0);
            isreset = false;
        }
    }

    @Override
    public void onRefresh(int id) {
        orderModel.getOrder("", "", false);
    }

    @Override
    public void onLoadMore(int id) {
        orderModel.getOrderMore("", "");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void onEvent(MyEvent event) {
        orderupdate = event.getFlag();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                orderModel.getOrder("", "", true);
            }
        } else if (requestCode == 100) {
            int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
            int leftMagin = (int) getResources().getDimension(R.dimen.ten_margin);
            //动画
            TranslateAnimation animation = new TranslateAnimation(0, 0, -res.getDimension(R.dimen.dp_48), 0);
            ScaleAnimation animation1 = new ScaleAnimation(0.85f, 1f, 1f, 1f);
            TranslateAnimation animation2 = new TranslateAnimation(-screenWidth / 2 + 2 * leftMagin + searchlayout_in.getWidth() / 2, 0, 0, 0);
            animation.setDuration(300);
            animation1.setDuration(300);
            animation2.setDuration(300);
            animation.setFillAfter(true);
            animation1.setFillAfter(true);
            animation2.setFillAfter(true);
            ll_search.startAnimation(animation);
            searchlayout_bg.startAnimation(animation1);
            searchlayout_in.startAnimation(animation2);
        }
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        super.onMessageResponse(url, jo, status);
        xlistView.stopRefresh();
        xlistView.stopLoadMore();
        if (url.equals(ProtocolConst.ORDER_LIST)) {
            if (status.getSucceed() == 1) {
                xlistView.setRefreshTime();
                if (orderModel.paginated.getMore() == 0) {
                    xlistView.setPullLoadEnable(false);
                } else {
                    xlistView.setPullLoadEnable(true);
                }
                setOrder();
            }
        }

    }
}
