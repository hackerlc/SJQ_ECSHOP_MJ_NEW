package com.ecjia.view.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.network.netmodle.PayModel;
import com.ecjia.network.netmodle.RechargeModel;
import com.ecjia.network.netmodle.UserInfoModel;
import com.ecjia.view.ECJiaMainActivity;
import com.ecjia.view.activity.goodsorder.OrderListActivity;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.EventKeywords;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.adapter.PayListAdapter;
import com.ecjia.consts.OrderType;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.widgets.paycenter.alipay.AlipayHelper;
import com.ecjia.widgets.paycenter.base.OnPaySucceedListener;
import com.ecjia.widgets.paycenter.uppay.UppayHelper;
import com.ecjia.widgets.paycenter.wxpay.WXpayHelper;
import com.ecjia.util.EventBus.Event;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.FormatUtil;
import com.ecjia.util.LG;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.OrderModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.PAYMENT;

import com.ecjia.widgets.XListView.IXListViewListener;

import com.ecjia.widgets.PayToastView;
import com.ecjia.widgets.ToastView;
import com.umeng.message.PushAgent;

import de.greenrobot.event.EventBus;

public class ChoosePayActivity extends BaseActivity implements
        IXListViewListener, HttpResponse, OnPaySucceedListener {
    // 所需参数的变量
    Intent intent;

    private EditText mUserId;
    private String order_id;
    private Boolean create;

    private String body;

    // toast的图片
    ImageView img;
    PayToastView paytoast;
    private String showsuccess;

    private Button btnpay;
    private OrderModel orderModel;
    private TextView totalfee, paytype;
    private LinearLayout chooselist;
    private LinearLayout changepaytpye;
    private Resources resource;
    private PAYMENT payment;
    String fee;
    private LinearLayout btn_item;
    private LinearLayout erroritem;
    private TextView errordesc;
    private LinearLayout ordercreateitme;
    private LinearLayout rechangeitem;
    private ListView listview;
    private ImageView choose_showlist;

    private PayListAdapter adapter;

    private LinearLayout surepay_item, yue_more;
    private TextView yue_title, yue_buy, yue_order;
    private View dis_view;

    ArrayList<PAYMENT> paylist;

    private String wappay = "";
    //余额充值相关
    private Boolean ischarger;
    private RechargeModel rechargeModel;
    private String pay_id;

    private String account_id;
    private int flag = 0;
    private String paycode, uppaycode;
    private String payname, uppayname;
    boolean isOnline = true;

    private PayModel payModel;
    private UppayHelper uppayHelper;
    private AlipayHelper alipayHelper;
    private WXpayHelper wXpayHelper;
    private String wxpay;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.choose_pay);
        EventBus.getDefault().register(this);

        initTopView();

        payModel = new PayModel(this);
        payModel.addResponseListener(this);
        orderModel = new OrderModel(this);
        orderModel.addResponseListener(this);
        rechargeModel = new RechargeModel(this);
        rechargeModel.addResponseListener(this);


        init();//初始化

        initData();

    }

    //初始化更改支付方式小布局
    private void rechangeinit() {
        getPayList();
        rechangeitem.setVisibility(View.VISIBLE);
        if (null == adapter) {
            adapter = new PayListAdapter(this, paylist);
            adapter.setOnItemListener(new PayListAdapter.onItemChangeListener() {
                @Override
                public void onItemChange(View v, final int position) {
                    uppaycode = adapter.list.get(position).getPay_code();
                    uppayname = adapter.list.get(position).getPay_name();
                    pay_id = adapter.list.get(position).getPay_id();
                    payname = uppayname;
                    paycode = uppaycode;
                    if (ischarger) {
                        rechargeModel.getRechargeInfo(fee, "", pay_id, account_id);
                    } else {
                        orderModel.UpdateOrder(order_id, pay_id);//更改支付方式
                    }
                    paytype.setText(payname);
                    listview.setVisibility(View.GONE);
                    choose_showlist.setBackgroundResource(R.drawable.search_showchild);
                }
            });
            listview.setAdapter(adapter);
            setListViewHeightBasedOnChildren(listview);
        } else {
            adapter.list = paylist;
            setListViewHeightBasedOnChildren(listview);
            adapter.notifyDataSetChanged();
        }
    }


    private void init() {
        btn_item = (LinearLayout) findViewById(R.id.choose_btnitem);
        errordesc = (TextView) findViewById(R.id.error_desc);
        erroritem = (LinearLayout) findViewById(R.id.error_item);
        ordercreateitme = (LinearLayout) findViewById(R.id.choose_create);
        changepaytpye = (LinearLayout) findViewById(R.id.change_pay_type);// 支付类型
        btnpay = (Button) this.findViewById(R.id.payweb_submit);
        totalfee = (TextView) findViewById(R.id.choose_total_fee);
        paytype = (TextView) findViewById(R.id.choose_paytype);
        chooselist = (LinearLayout) findViewById(R.id.choose_paytype_list);
        rechangeitem = (LinearLayout) findViewById(R.id.type_rechange_item);
        listview = (ListView) findViewById(R.id.choose_listview);
        choose_showlist = (ImageView) findViewById(R.id.choose_showlist);
        //余额支付item
        surepay_item = (LinearLayout) findViewById(R.id.yue_item);
        yue_more = (LinearLayout) findViewById(R.id.yue_more);
        yue_title = (TextView) findViewById(R.id.yue_title);
        yue_buy = (TextView) findViewById(R.id.yue_buy);
        yue_order = (TextView) findViewById(R.id.yue_order);
        dis_view = findViewById(R.id.dis_view);

        yue_buy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ChoosePayActivity.this, ECJiaMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ChoosePayActivity.this.startActivity(intent);
                finish();
                ChoosePayActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        yue_order.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (create) {
                    Intent intent = new Intent();
                    intent.setClass(ChoosePayActivity.this, OrderListActivity.class);
                    intent.putExtra(IntentKeywords.ORDER_TYPE, OrderType.AWAIT_SHIP);
                    ChoosePayActivity.this.startActivity(intent);
                    ChoosePayActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(ChoosePayActivity.this, OrderListActivity.class);
                    intent.putExtra(IntentKeywords.ORDER_TYPE, OrderType.AWAIT_SHIP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ChoosePayActivity.this.startActivity(intent);
                    ChoosePayActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    finish();
                }
            }
        });

        btnpay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (ischarger) {
                    payModel.accountPay(rechargeModel.getpayment_id, rechargeModel.account_id);
                } else {
                    payModel.orderPay(order_id);
                }

            }
        });

        chooselist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listview.getVisibility() == View.VISIBLE) {
                    listview.setVisibility(View.GONE);
                    choose_showlist.setBackgroundResource(R.drawable.search_showchild);
                } else if (listview.getVisibility() == View.GONE) {
                    listview.setVisibility(View.VISIBLE);
                    choose_showlist.setBackgroundResource(R.drawable.search_hidden);
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void initData() {
        resource = getResources();
        String yuan = resource.getString(R.string.yuan);
        String yuan_unit = resource.getString(R.string.yuan_unit);

        intent = getIntent();
        if (!TextUtils.isEmpty(intent.getStringExtra(IntentKeywords.PAY_CODE))) {
            paycode = intent.getStringExtra(IntentKeywords.PAY_CODE);
            for (int i = 0; i < mApp.paymentlist.size(); i++) {
                if (mApp.paymentlist.get(i).getPay_code().equals(paycode)) {
                    payname = mApp.paymentlist.get(i).getPay_name();
                    pay_id = mApp.paymentlist.get(i).getPay_id();
                    break;
                }
            }
        } else if (!TextUtils.isEmpty(intent.getStringExtra(IntentKeywords.PAY_ID))) {
            pay_id = intent.getStringExtra(IntentKeywords.PAY_ID);
            if("9".equals(pay_id)){
                pay_id="17";
            }
            for (int i = 0; i < mApp.paymentlist.size(); i++) {
                if (mApp.paymentlist.get(i).getPay_id().equals(pay_id)) {
                    paycode = mApp.paymentlist.get(i).getPay_code();
                    payname = mApp.paymentlist.get(i).getPay_name();
                    break;
                }
            }
        }
        create = intent.getBooleanExtra(IntentKeywords.PAY_IS_CREATE, false);


        String payType = intent.getStringExtra(IntentKeywords.PAY_TYPE);
        if (payType.equals(IntentKeywords.ACCOUNT_ID)) {
            ischarger = true;
            account_id = intent.getStringExtra(payType);
        } else {
            ischarger = false;
            order_id = intent.getStringExtra(payType);
        }

        body = intent.getStringExtra(IntentKeywords.PAY_BODY);
        fee = intent.getStringExtra(IntentKeywords.PAY_AMOUNT);


        if (ischarger) {
            rechargeModel.getRechargeInfo(fee, "", pay_id, account_id);
            surepay_item.setVisibility(View.GONE);
            btn_item.setVisibility(View.VISIBLE);
            dis_view.setVisibility(View.VISIBLE);
            rechangeinit();//更改支付方式初始换
        } else {
            String[] str = {AppConst.BANK, AppConst.COD};
            for (int i = 0; i < str.length; i++) {
                if (!TextUtils.isEmpty(paycode) && paycode.equals(str[i])) {
                    isOnline = false;
                    break;//线下的
                }
            }
            if (!isOnline || AppConst.BALANCE.equals(paycode)) {
                payModel.orderPay(order_id);
            } else {
                surepay_item.setVisibility(View.GONE);
                btn_item.setVisibility(View.VISIBLE);
                dis_view.setVisibility(View.VISIBLE);
                rechangeinit();//更改支付方式初始换
            }
        }
        paytype.setText(payname);
        if (create) {
            ordercreateitme.setVisibility(View.VISIBLE);
        } else {
            ordercreateitme.setVisibility(View.GONE);
        }
        totalfee.setText(yuan_unit + FormatUtil.formatStrToFloat(fee) + yuan);
    }


    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.ORDER_UPDATE) {
            if (status.getSucceed() == 1) {
                paycode = uppaycode;
                payname = uppayname;
                paytype.setText(payname);
                LG.i("支付方式更新");
                EventBus.getDefault().post(new MyEvent(EventKeywords.UPDATE_ORDER));
                surepay_item.setVisibility(View.GONE);
                dis_view.setVisibility(View.VISIBLE);
                rechangeitem.setVisibility(View.VISIBLE);
                btn_item.setVisibility(View.VISIBLE);
                rechangeinit();
                if (paycode.equals(AppConst.BALANCE)) {
                    payModel.orderPay(order_id);
                }
            }
        } else if (url == ProtocolConst.USER_RECHARGE) {
            flag = status.getSucceed();
            if (status.getSucceed() == 1) {
                pay_id = rechargeModel.getpayment_id;
                account_id = rechargeModel.account_id;
                paytype.setText(payname);
                LG.i("支付方式更新");
                surepay_item.setVisibility(View.GONE);
                dis_view.setVisibility(View.VISIBLE);
                rechangeitem.setVisibility(View.VISIBLE);
                btn_item.setVisibility(View.VISIBLE);
                rechangeinit();
            }

        } else if (url == ProtocolConst.ORDER_PAY) {
            flag = status.getSucceed();
            if (status.getSucceed() == 1) {
                payReturn();
            }
        } else if (url == ProtocolConst.USER_ACCOUNT_PAY) {//余额充值及充值修改支付方式
            flag = status.getSucceed();
            if (status.getSucceed() == 1) {
                payReturn();
            }
        } else if (url == ProtocolConst.USERINFO) {
            if (status.getSucceed() == 1) {
                if (ischarger) {
                    EventBus.getDefault().post(new MyEvent("changed"));//通知金额已改变
                    Intent intent = new Intent(ChoosePayActivity.this, AccountActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        } else {
            btnpay.setVisibility(View.GONE);
            Resources resources = getResources();
            String choosepay_network_problem = resources.getString(R.string.choosepay_network_problem);
            ToastView toast = new ToastView(ChoosePayActivity.this, choosepay_network_problem);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    private void payReturn() {
        String choosepay_can_use = resource.getString(R.string.choosepay_can_use);
        String choosepay_zero_nopay = resource.getString(R.string.choosepay_zero_nopay);
        paycode = payModel.pay_code;
        paytype.setText(payModel.pay_name);
        if (0 == FormatUtil.formatStrToFloat(fee)) {
            paySucceed(PaymentType.PAYMENT_ZERO, choosepay_zero_nopay);
        } else if (AppConst.BALANCE.equals(payModel.pay_code)) {
            if ("error".equals(payModel.pay_status)) {
                rechangeinit();//更改支付方式初始换
                surepay_item.setVisibility(View.VISIBLE);
                yue_more.setVisibility(View.GONE);
                yue_title.setText(payModel.error_message);
                dis_view.setVisibility(View.GONE);
                btn_item.setVisibility(View.GONE);
            } else {
                paySucceed(PaymentType.PAYMENT_CODE_BALANCE, choosepay_can_use + payModel.user_money + "。");
            }

        } else if (AppConst.BANK.equals(payModel.pay_code)) {//银行转账
            String choosepay_need_line_pay = resource.getString(R.string.choosepay_need_line_pay);
            if (create) {
                surepay_item.setVisibility(View.VISIBLE);
            } else {
                surepay_item.setVisibility(View.GONE);
            }
            erroritem.setVisibility(View.VISIBLE);
            errordesc.setText(payModel.pay_online);
            yue_title.setText(choosepay_need_line_pay);

            yue_more.setVisibility(View.VISIBLE);
            btn_item.setVisibility(View.GONE);
            rechangeitem.setVisibility(View.GONE);

        } else if (AppConst.COD.equals(payModel.pay_code)) {//货到付款
            String choosepay_cod_pay = resource.getString(R.string.choosepay_cod);
            if (create) {
                surepay_item.setVisibility(View.VISIBLE);
            } else {
                surepay_item.setVisibility(View.GONE);
            }
            erroritem.setVisibility(View.VISIBLE);
            errordesc.setText(choosepay_cod_pay);

            yue_more.setVisibility(View.VISIBLE);
            btn_item.setVisibility(View.GONE);
            rechangeitem.setVisibility(View.GONE);
            yue_title.setText("");

        } else {
            surepay_item.setVisibility(View.GONE);
            btn_item.setVisibility(View.VISIBLE);
            dis_view.setVisibility(View.VISIBLE);
            rechangeinit();//更改支付方式初始换

            Resources resources = getResources();
            String choosepay_no_unionpay = resources.getString(R.string.choosepay_no_unionpay);
            String choosepay_no_wxpay = resources.getString(R.string.choosepay_no_wxpay);
            String choosepay_unknown_pay = resources.getString(R.string.choosepay_unknown_pay);
            String not_install_wxpay = resources.getString(R.string.not_install_wxpay);

            if (payModel.pay_code.equals(AppConst.ALIPAY_MOBILE)) {
                LG.i(payModel.pay_code + "这是paycod");
                if (AndroidManager.SUPPORT_ALIPAY_MOBILE) {
                    payModel.alipayData.setBody(body);
                    if (alipayHelper == null) {
                        alipayHelper = new AlipayHelper(ChoosePayActivity.this, payModel.alipayData);
                        alipayHelper.setOnPaysucceedListener(ChoosePayActivity.this);
                    }
                    alipayHelper.startPay();
                } else {
                    Intent intent = new Intent(ChoosePayActivity.this, PayWebActivity.class);
                    intent.putExtra("code", payModel.pay_code);
                    intent.putExtra("html", payModel.pay_online);
                    ChoosePayActivity.this.startActivity(intent);
                }
            } else if (payModel.pay_code.equals(AppConst.UPPAY)) {
                if (AndroidManager.SUPPORT_UPMP) {
                    if (uppayHelper == null) {
                        uppayHelper = new UppayHelper(ChoosePayActivity.this, payModel.uppayData);//创建银联支付对象
                        uppayHelper.setOnPaysucceedListener(ChoosePayActivity.this);//设置支付成功回调监听
                    }
                    uppayHelper.startPay();//开始支付
                } else {
                    ToastView toastView = new ToastView(ChoosePayActivity.this, choosepay_no_unionpay);
                    toastView.show();
                }
            } else if (payModel.pay_code.equals(AppConst.WXPAY)) {
                if (AndroidManager.SUPPORT_WXPAY) {
                    if (wXpayHelper == null) {
                        wXpayHelper = new WXpayHelper(ChoosePayActivity.this, payModel.wXpayData);
                    }
                    if (wXpayHelper.checkPaymentUtilExist()) {
                        wXpayHelper.startPay();
                    } else {
                        ToastView toastView = new ToastView(ChoosePayActivity.this, not_install_wxpay);
                        toastView.show();
                    }
                } else {
                    ToastView toastView = new ToastView(ChoosePayActivity.this, choosepay_no_wxpay);
                    toastView.show();
                }

            } else {
                Intent intent = new Intent(ChoosePayActivity.this, PayWebActivity.class);
                intent.putExtra("code", payModel.pay_code);
                intent.putExtra("html", payModel.pay_online);
                ChoosePayActivity.this.startActivity(intent);
            }
        }
    }

    @Override
    public void initTopView() {

        topView = (ECJiaTopView) findViewById(R.id.choosepay_topview);
        topView.setTitleText(R.string.payment_center);
        topView.setLeftBackImage(BACKIMAGE_ID, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onRefresh(int id) {

    }

    @Override
    public void onLoadMore(int id) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        return true;

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    void getPayList() {

        if (null == paylist) {
            paylist = new ArrayList<PAYMENT>();
        } else {
            paylist.clear();
        }

        if (ischarger) {
            for (int i = 0; i < mApp.Rechargepaymentlist.size(); i++) {
                if (!mApp.Rechargepaymentlist.get(i).getPay_code().equals(paycode)) {
                    paylist.add(mApp.Rechargepaymentlist.get(i));
                }
            }
        } else {
            if (flag == 1) {
                for (int i = 0; i < mApp.onlinepaymentlist.size(); i++) {
                    if (!mApp.onlinepaymentlist.get(i).getPay_code().equals(paycode)) {
                        paylist.add(mApp.onlinepaymentlist.get(i));
                    }
                }
            } else {
                for (int i = 0; i < mApp.onlinepaymentlist.size(); i++) {
                    paylist.add(mApp.onlinepaymentlist.get(i));
                }
            }
        }
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if ("wappay".equals(wappay)) {
            String paysuccess = resource.getString(R.string.payment_paysuccess);
            String payment_paysuccess = resource.getString(R.string.payment_payfail);
            paySucceed(PaymentType.PAYMENT_WAPPAY, payment_paysuccess);
            wappay = "";
        }
        if ("wxpay".equals(wxpay)) {
            if (ischarger) {
                new UserInfoModel(this).getUserInfo();
            } else {
                Resources resources = getResources();
                String payment_paysuccess = resources.getString(R.string.payment_paysuccess);
                paySucceed(PaymentType.PAYMENT_WXPAY, payment_paysuccess);
                wxpay = "";
            }
        }
    }

    public void onEvent(Event event) {
        if ("wappay".equals(event.getMsg())) {
            wappay = event.getMsg();
        }
        if ("wxpay".equals(event.getMsg())) {
            wxpay = event.getMsg();
        }
    }

    //支付回调（银联的是走onActivityResult方法）
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null && resultCode == Activity.RESULT_OK && requestCode != 100) {
            uppayHelper.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void paySucceed(PaymentType type, String payment_paysuccess) {
        erroritem.setVisibility(View.GONE);
        btn_item.setVisibility(View.GONE);
        rechangeitem.setVisibility(View.GONE);
        surepay_item.setVisibility(View.VISIBLE);
        yue_more.setVisibility(View.VISIBLE);
        yue_title.setText(payment_paysuccess);

        Resources resources = getResources();
        if (payment_paysuccess.equals(payment_paysuccess)) {
            new UserInfoModel(this).getUserInfo();
        }
        setResult(RESULT_OK);
        if (!create) {
            EventBus.getDefault().post(new MyEvent(true, 2));
        }

        //对银联支付成功的处理
        if (type == PaymentType.PAYMENT_UPPAY) {
            // TODO: 对银联支付成功的处理
        }
    }

}
