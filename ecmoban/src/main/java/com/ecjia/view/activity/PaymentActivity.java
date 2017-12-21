package com.ecjia.view.activity;


import java.util.ArrayList;
import java.util.HashMap;

import android.content.res.Resources;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.AppConst;
import com.ecjia.view.adapter.PaymentAdapter1;
import com.ecjia.view.adapter.PaymentAdapter;
import com.ecjia.entity.responsemodel.PAYMENT;
import com.ecjia.entity.responsemodel.SHIPPING;
import com.umeng.message.PushAgent;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PaymentActivity extends BaseActivity {

    private TextView title;
    private ImageView back;

    private ListView listView, listView1;
    private PaymentAdapter paymentAdapter;
    private PaymentAdapter1 paymentAdapter1;
    private Handler handler, handler1;
    public static int position;
    private ArrayList<PAYMENT> list = new ArrayList<PAYMENT>();
    private ArrayList<PAYMENT> list1 = new ArrayList<PAYMENT>();
    private String s;
    SHIPPING shipping;
    private LinearLayout onlineitem, uplineitem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        PushAgent.getInstance(this).onAppStart();
        Intent intent = getIntent();

        s = intent.getStringExtra("payment");

        if (null != mApp.onlinepaymentlist && mApp.onlinepaymentlist.size() > 0) {
            list.clear();
            for (int i = 0; i < mApp.onlinepaymentlist.size(); i++) {
                list.add(mApp.onlinepaymentlist.get(i));
            }

        }

        if (null != mApp.uplinepaymentlist && mApp.uplinepaymentlist.size() > 0) {
            list1.clear();
            for (int i = 0; i < mApp.uplinepaymentlist.size(); i++) {
                list1.add(mApp.uplinepaymentlist.get(i));
            }

        }


        title = (TextView) findViewById(R.id.top_view_text);
        Resources resource = (Resources) getBaseContext().getResources();
        String pay = resource.getString(R.string.balance_pay);
        title.setText(pay);
        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    position = msg.arg1;
                    Intent intent = new Intent();
                    PAYMENT payment = list.get(position);

                    try {
                        intent.putExtra("payment", payment.toJson().toString());
                        if (oneshipping()) {
                            intent.putExtra("shipping", shipping.toJson()
                                    .toString());
                        }
                    } catch (JSONException e) {

                    }
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                if (paymentAdapter1 != null) {
                    paymentAdapter1.notifyDataSetChanged();
                }
                paymentAdapter.notifyDataSetChanged();

            }
        };
        handler1 = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    position = msg.arg1;
                    Intent intent = new Intent();
                    PAYMENT payment = list1.get(position);

                    try {
                        intent.putExtra("payment", payment.toJson().toString());
                        if (oneshipping()) {
                            intent.putExtra("shipping", shipping.toJson()
                                    .toString());
                        }
                    } catch (JSONException e) {

                    }
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                if (paymentAdapter != null) {
                    paymentAdapter.notifyDataSetChanged();
                }
                paymentAdapter1.notifyDataSetChanged();
            }
        };

        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mApp.onlinepaymentlist.size() > 0) {
                    if (PaymentAdapter.isFirst) {
                        Message msg = new Message();
                        msg.what = 1;
                        msg.arg1 = 0;
                        handler.handleMessage(msg);
                    }
                } else {
                    if (list1.size() > 0) {
                        if (PaymentAdapter1.isFirst) {
                            Message msg = new Message();
                            msg.what = 1;
                            msg.arg1 = 0;
                            handler1.handleMessage(msg);
                        }
                    }
                }
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.payment_list);
        listView1 = (ListView) findViewById(R.id.payment_list1);
        onlineitem = (LinearLayout) findViewById(R.id.payment_onlineitem);
        uplineitem = (LinearLayout) findViewById(R.id.payment_uplineitem);
        if (AppConst.paymentlist == null) {
            AppConst.paymentlist = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < mApp.paymentlist.size(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                if (i == 0) {
                    map.put("select", "true");
                } else {
                    map.put("select", "false");
                }
                AppConst.paymentlist.add(map);
            }
        }
        if (list.size() > 0) {
            paymentAdapter = new PaymentAdapter(this, list);
            listView.setAdapter(paymentAdapter);
            paymentAdapter.handler = this.handler;
        } else {
            onlineitem.setVisibility(View.GONE);
        }
        if (list1.size() > 0) {
            paymentAdapter1 = new PaymentAdapter1(this, list1);
            listView1.setAdapter(paymentAdapter1);
            paymentAdapter1.handler = this.handler1;
        } else {
            uplineitem.setVisibility(View.GONE);
        }


    }

    // 当只有一种配送方式时默认选中
    private boolean oneshipping() {
        try {
            if (StringUtils.isNotEmpty(s)) {
                JSONObject jo = new JSONObject(s);
                JSONArray shipArray = jo.optJSONArray("shipping_list");
                if (null != shipArray && shipArray.length() == 1) {
                    if (AppConst.shippinglist == null) {
                        AppConst.shippinglist = new ArrayList<HashMap<String, String>>();
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("select", "true");
                        AppConst.shippinglist.add(map);
                    } else {
                        if (AppConst.shippinglist.size() == 0) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("select", "true");
                            AppConst.shippinglist.add(map);
                        }
                    }
                    JSONObject shippingJsonObject = shipArray.getJSONObject(0);
                    shipping = SHIPPING.fromJson(shippingJsonObject);
                    return true;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mApp.onlinepaymentlist.size() > 0) {
                if (PaymentAdapter.isFirst) {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.arg1 = 0;
                    handler.handleMessage(msg);
                }
            } else {
                if (list1.size() > 0) {
                    if (PaymentAdapter1.isFirst) {
                        Message msg = new Message();
                        msg.what = 1;
                        msg.arg1 = 0;
                        handler1.handleMessage(msg);
                    }
                }
            }
            finish();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
