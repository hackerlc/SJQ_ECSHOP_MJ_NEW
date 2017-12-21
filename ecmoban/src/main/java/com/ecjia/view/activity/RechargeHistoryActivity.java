package com.ecjia.view.activity;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.RechargeModel;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.XListView;
import com.ecjia.view.adapter.RechargeListAdapter;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.EventBus.Event;
import com.ecmoban.android.sijiqing.R;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class RechargeHistoryActivity extends BaseActivity implements XListView
        .IXListViewListener, HttpResponse {
    private TextView head_all, head_recharge, head_withdrawal;
    private LinearLayout history_head_bg;
    private XListView listview;
    private View nothingbg;
    Resources resource;
    RechargeModel model;
    RechargeListAdapter adapter;
    String gettype;//数据分类
    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_history);
        EventBus.getDefault().register(this);
        PushAgent.getInstance(this).onAppStart();
        initView();
    }


    private void initView() {

        initTopView();

        res = this.getResources();
        model = new RechargeModel(this);
        model.addResponseListener(this);
        resource = (Resources) getBaseContext().getResources();

        history_head_bg = (LinearLayout) findViewById(R.id.history_head_bg);
        head_all = (TextView) findViewById(R.id.head_all);
        head_withdrawal = (TextView) findViewById(R.id.head_withdrawal);
        head_recharge = (TextView) findViewById(R.id.head_recharge);
        listview = (XListView) findViewById(R.id.recharge_list);
        nothingbg = findViewById(R.id.null_pager);

        listview.setPullLoadEnable(true);
        listview.setRefreshTime();
        listview.setXListViewListener(this, 1);
        //"raply"为提现，"deposit"为充值
        head_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                history_selected("");
                model.getAccountRecordList(gettype);
            }
        });

        head_withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                history_selected("raply");
                model.getAccountRecordList(gettype);
            }
        });

        head_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                history_selected("deposit");
                model.getAccountRecordList(gettype);
            }
        });
        history_selected("");
        model.getAccountRecordList(gettype);
    }

    @Override
    public void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.recharge_history_topview);
        topView.setTitleText(R.string.accoubt_record);
        topView.setLeftBackImage(BACKIMAGE_ID, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //点击事件
    private void history_selected(String number) {
        if ("".equals(number)) {
            history_head_bg.setBackgroundResource(R.drawable.history_all);

            head_all.setTextColor(Color.WHITE);
            head_withdrawal.setTextColor(resource
                    .getColor(R.color.trade_head_selectbg));
            head_recharge.setTextColor(resource
                    .getColor(R.color.trade_head_selectbg));

            head_all.setEnabled(false);
            head_withdrawal.setEnabled(true);
            head_recharge.setEnabled(true);
            gettype = "";
        } else if ("raply".equals(number)) {
            history_head_bg.setBackgroundResource(R.drawable.history_withdrawal);

            head_withdrawal.setTextColor(Color.WHITE);
            head_all.setTextColor(resource
                    .getColor(R.color.trade_head_selectbg));
            head_recharge.setTextColor(resource
                    .getColor(R.color.trade_head_selectbg));
            head_all.setEnabled(true);
            head_withdrawal.setEnabled(false);
            head_recharge.setEnabled(true);
            gettype = "raply";
        } else if ("deposit".equals(number)) {
            history_head_bg.setBackgroundResource(R.drawable.history_recharge);

            head_recharge.setTextColor(Color.WHITE);
            head_all.setTextColor(resource
                    .getColor(R.color.trade_head_selectbg));
            head_withdrawal.setTextColor(resource
                    .getColor(R.color.trade_head_selectbg));
            head_all.setEnabled(true);
            head_withdrawal.setEnabled(true);
            head_recharge.setEnabled(false);
            gettype = "deposit";
        } else {

        }
    }

    private void setContent() {
        if (adapter == null) {
            if (model.rechargelist.size() == 0) {
                nothingbg.setVisibility(View.VISIBLE);
                listview.setVisibility(View.GONE);
            } else {
                nothingbg.setVisibility(View.GONE);
                listview.setVisibility(View.VISIBLE);
                adapter = new RechargeListAdapter(this, model.rechargelist);
                listview.setAdapter(adapter);
            }
        } else {
            if (model.rechargelist.size() == 0) {
                nothingbg.setVisibility(View.VISIBLE);
                listview.setVisibility(View.GONE);
            } else {
                nothingbg.setVisibility(View.GONE);
                listview.setVisibility(View.VISIBLE);
                adapter.list = model.rechargelist;
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh(int id) {
        model.getAccountRecordList(gettype);
    }

    @Override
    public void onLoadMore(int id) {
        model.getAccountRecordMore(gettype);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    public void onEvent(Event event) {
        if ("recharge_cancel".equals(event.getMsg())) {
            model.getAccountRecordList(gettype);
        }
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.USER_ACCOUNT_RECORD) {
            if (status.getSucceed() == 1) {
                listview.stopRefresh();
                listview.stopLoadMore();
                listview.setRefreshTime();
                setContent();//数据展示
                if (0 == model.paginated.getMore()) {
                    listview.setPullLoadEnable(false);
                } else {
                    listview.setPullLoadEnable(true);
                }

            }
        }
    }
}
