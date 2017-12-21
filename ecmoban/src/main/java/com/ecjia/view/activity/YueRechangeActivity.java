package com.ecjia.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.RechargeModel;
import com.ecjia.widgets.ToastView;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.adapter.YuepaytypeAdapter;
import com.ecjia.entity.responsemodel.STATUS;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


public class YueRechangeActivity extends BaseActivity {
    private ImageView back, paytype, yue_showlist, paytype_selected;
    private TextView title, tv_name, choosedtype;
    private LinearLayout choosetype, payitem;
    private SharedPreferences shared;
    private Button rechange_ok;
    private EditText et_money;
    private ListView listview;
    YuepaytypeAdapter adapter;
    RechargeModel model;
    public Handler messageHandler;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yue_rechange);
        PushAgent.getInstance(this).onAppStart();
        shared = this.getSharedPreferences("userInfo", 0);
        setInfo();
    }


    void setInfo() {
        messageHandler = new Handler() {
            public void handleMessage(Message msg) {
                position = msg.arg1;
                if (mApp.Rechargepaymentlist.size() != 0) {
                    choosedtype.setText(mApp.Rechargepaymentlist.get(position).getPay_name());
                    listview.setVisibility(View.GONE);
                    yue_showlist.setBackgroundResource(R.drawable.search_showchild);
                }
            }
        };
        model = new RechargeModel(this);
        model.addResponseListener(this);
        final Resources res = AppConst.getResources(this);
        title = (TextView) findViewById(R.id.top_view_text);
        back = (ImageView) findViewById(R.id.top_view_back);
        choosetype = (LinearLayout) findViewById(R.id.yue_choosetype);
        choosedtype = (TextView) findViewById(R.id.yue_choosedtype);
        if (mApp.Rechargepaymentlist.size() != 0 && mApp.Rechargepaymentlist != null) {
            choosedtype.setText(mApp.Rechargepaymentlist.get(position).getPay_name());
        } else {
            choosedtype.setText(R.string.yue_nopayment);
        }
        tv_name = (TextView) findViewById(R.id.yue_username);
        yue_showlist = (ImageView) findViewById(R.id.yue_showlist);
        rechange_ok = (Button) findViewById(R.id.rechange_ok);
        et_money = (EditText) findViewById(R.id.yue_money);

        et_money.setFocusable(true);
        et_money.setFocusableInTouchMode(true);
        et_money.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) et_money.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_money, 0);
            }
        }, 300);

        listview = (ListView) findViewById(R.id.yue_listview);
        adapter = new YuepaytypeAdapter(this, mApp.Rechargepaymentlist, messageHandler);
        adapter.parentHandler = messageHandler;
        listview.setAdapter(adapter);
        listview.setVisibility(View.GONE);
        tv_name.setText(shared.getString("uname", ""));
        choosetype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listview.getVisibility() == View.VISIBLE) {
                    listview.setVisibility(View.GONE);
                    yue_showlist.setBackgroundResource(R.drawable.search_showchild);
                } else if (listview.getVisibility() == View.GONE) {
                    listview.setVisibility(View.VISIBLE);
                    yue_showlist.setBackgroundResource(R.drawable.search_hidden);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title.setText(res.getString(R.string.yue_rechange));

        rechange_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(et_money.getText().toString())) {
                    ToastView toast = new ToastView(YueRechangeActivity.this, res.getString(R.string.yue_inputnotnull));
                    toast.setGravity(Gravity.CENTER);
                    toast.show();
                } else if (mApp.Rechargepaymentlist.size() > 0) {
                    model.getRechargeInfo(et_money.getText().toString(), "", mApp.Rechargepaymentlist.get(position).getPay_id(), "");
                } else {
                    ToastView toast = new ToastView(YueRechangeActivity.this, res.getString(R.string.yue_nopayment));
                    toast.setGravity(Gravity.CENTER);
                    toast.show();
                }
            }
        });
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        super.onMessageResponse(url, jo, status);
        if (url.equals(ProtocolConst.USER_RECHARGE)) {
            Intent intent = new Intent(YueRechangeActivity.this, ChoosePayActivity.class);
            intent.putExtra(IntentKeywords.PAY_TYPE, IntentKeywords.ACCOUNT_ID);
            intent.putExtra(IntentKeywords.ACCOUNT_ID, model.account_id);
            intent.putExtra(IntentKeywords.PAY_IS_CREATE, true);
            intent.putExtra(IntentKeywords.PAY_BODY, "余额充值");
            intent.putExtra(IntentKeywords.PAY_AMOUNT, et_money.getText().toString());
            intent.putExtra(IntentKeywords.PAY_ID, model.getpayment_id);
            startActivity(intent);
        }
    }
}
