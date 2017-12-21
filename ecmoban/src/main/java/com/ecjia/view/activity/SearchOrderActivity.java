package com.ecjia.view.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.OrderModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.ShoppingCartModel;
import com.ecjia.widgets.ToastView;
import com.ecjia.widgets.XListView;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.adapter.TradeAdapter;
import com.ecjia.entity.responsemodel.ORDER_GOODS_LIST;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.FormatUtil;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 独立的搜索界面
 */

public class SearchOrderActivity extends BaseActivity implements OnClickListener {

    private LinearLayout fl_search_top;
    private EditText search_input;
    private XListView searchListView;
    private OrderModel orderModel;
    private String orderType;
    private TradeAdapter tradeAdapter;
    private ArrayList<ORDER_GOODS_LIST> buylist;
    private ShoppingCartModel cartModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search_order);
        PushAgent.getInstance(this).onAppStart();
        initData();
        initView();
    }


    void initData() {
        orderType = getIntent().getStringExtra(IntentKeywords.ORDER_TYPE);
        if (TextUtils.isEmpty(orderType)) {
            orderType = "";
        }

        orderModel = new OrderModel(this);
        orderModel.addResponseListener(this);
        cartModel = new ShoppingCartModel(this);
        cartModel.addResponseListener(this);
    }

    private void initView() {

        findViewById(R.id.null_pager).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return false;
            }
        });


        findViewById(R.id.order_search_empty).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return false;
            }
        });

        findViewById(R.id.tv_search_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fl_search_top = (LinearLayout) findViewById(R.id.fl_search_top);
        fl_search_top.setBackgroundColor(0xffdddddd);

        search_input = (EditText) findViewById(R.id.et_search_input);
        search_input.setFocusable(true);
        search_input.setFocusableInTouchMode(true);
        search_input.requestFocus();


        search_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (TextUtils.isEmpty(v.getText())) {
                    Resources resources = getBaseContext().getResources();
                    String please_input = resources.getString(R.string.search_please_input);
                    ToastView toastView = new ToastView(SearchOrderActivity.this, please_input);
                    toastView.show();
                } else {
                    closeKeyBoard(search_input);
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                        orderModel.getOrder("", v.getText().toString(), true);
                    }
                }


                return false;
            }
        });

        InputMethodManager inputManager = (InputMethodManager) search_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(search_input, 0);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) search_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(search_input, 0);
                mHandler.sendEmptyMessage(0);
            }

        }, 300);


        searchListView = (XListView) findViewById(R.id.order_search_list);
        searchListView.setPullLoadEnable(true);
        searchListView.setRefreshTime();
        searchListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh(int id) {
                orderModel.getOrder("", search_input.getText().toString(), false);
            }

            @Override
            public void onLoadMore(int id) {
                orderModel.getOrderMore("", search_input.getText().toString());
            }
        }, 1);
    }

    private Intent intent;
    private int buyAgain;


    public void setOrder() {

        if (null == tradeAdapter) {
            searchListView.setBackgroundColor(Color.WHITE);
            tradeAdapter = new TradeAdapter(this, orderModel.order_list, orderType);
            searchListView.setAdapter(tradeAdapter);
        } else {
            tradeAdapter.notifyDataSetChanged();
        }

        if (orderModel.order_list.size() == 0) {
            searchListView.setVisibility(View.GONE);
            findViewById(R.id.order_search_empty).setBackgroundColor(Color.parseColor("#a0000000"));
            findViewById(R.id.order_search_empty).setEnabled(true);
            findViewById(R.id.null_pager).setVisibility(View.VISIBLE);
        } else {
            searchListView.setVisibility(View.VISIBLE);
            findViewById(R.id.order_search_empty).setBackgroundColor(Color.parseColor("#FFEEEEEE"));
            findViewById(R.id.order_search_empty).setEnabled(false);
            findViewById(R.id.null_pager).setVisibility(View.GONE);
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onClick(View v) {

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
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.animation_4, 0);
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        super.onMessageResponse(url, jo, status);
        if (url.equals(ProtocolConst.ORDER_LIST)) {
            if (status.getSucceed() == 1) {
                searchListView.setRefreshTime();
                if (orderModel.paginated.getMore() == 0) {
                    searchListView.setPullLoadEnable(false);
                } else {
                    searchListView.setPullLoadEnable(true);
                }
                setOrder();
            }
        } else if (url.equals(ProtocolConst.CARTCREATE)) {
//            buyAgain -= 1;
            buyAgain = 0;
            if (buyAgain > 0) {
//                cartModel.cartCreate(FormatUtil.formatStrToInt(buylist.get(buylist.size() - buyAgain).getGoods_id()) + "",
//                        new ArrayList<Integer>(), FormatUtil.formatStrToInt(buylist.get(buylist.size() - buyAgain).getGoods_number()), null, null);
            } else {
                Intent intent = new Intent(SearchOrderActivity.this, ShoppingCartActivity.class);
                startActivity(intent);
            }
        } else if (url.equals(ProtocolConst.AFFIRMRECEIVED)) {
            if (status.getSucceed() == 1) {
                ToastView toast = new ToastView(SearchOrderActivity.this, R.string.tradeitem_receive);
                toast.setDuration(3000);
                toast.show();
                orderModel.getOrder("", orderType, true);
            } else {
                ToastView toast = new ToastView(SearchOrderActivity.this, status.getError_desc());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        } else if (url.equals(ProtocolConst.ORDER_REMINDER)) {
            if (status.getSucceed() == 1) {
                ToastView toast = new ToastView(SearchOrderActivity.this, res.getString(R.string
                        .orderdetail_remind_success));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                ToastView toast = new ToastView(SearchOrderActivity.this, res.getString(R.string
                        .orderdetail_remind_failed));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }
}
