package com.ecjia.view.activity.goodsorder.balance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.res.Resources;

import com.ecjia.base.BaseActivity;
import com.ecjia.network.HttpResponse;
import com.ecjia.view.activity.AddressManageActivity;
import com.ecjia.view.activity.ChoosePayActivity;
import com.ecjia.view.activity.IntegralActivity;
import com.ecjia.view.activity.InvoiceActivity;
import com.ecjia.view.activity.PaymentActivity;
import com.ecjia.view.activity.RedPacketsActivity;
import com.ecjia.view.activity.RemarkActivity;
import com.ecjia.view.activity.ShippingActivity;
import com.ecjia.view.activity.ShopGoodsActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.HorizontalListView;
import com.ecjia.consts.AppConst;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.adapter.GoodsHorizLVAdapter;
import com.ecjia.util.FormatUtil;
import com.ecjia.util.LG;

import com.ecjia.view.adapter.BalanceAdapter;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.ShoppingCartModel;
import com.ecjia.view.adapter.ShippingAdapter;
import com.ecjia.entity.responsemodel.*;
import com.ecjia.widgets.ToastView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

/**
 * 商品结算页面
 */
public class BalanceActivity extends BaseActivity implements OnClickListener, HttpResponse {

    private TextView title;
    private ImageView back;
    private LinearLayout user;
    private TextView name;
    private TextView phoneNum;
    private TextView address;


    private LinearLayout pay;
    private TextView pay_type;
    private LinearLayout dis;
    private TextView shipping_name_text;
    private LinearLayout invoice;
    private TextView invoice_message;
    // 添加积分红包折扣功能
    private int min_inteagral;
    private LinearLayout goods;
    private TextView goods_num;
    private LinearLayout redPaper;
    private TextView redPaper_name;
    private LinearLayout integral;
    private TextView integral_num;
    //private String allow_can_invoice;// 发票是否可用
    //public String bonusinfo;// 红包验证
    //private String discount_formated;// 格式化后的折扣价格
    private LinearLayout remark;// 订单备注
    private TextView tvremark;//备注简要信息
    private LinearLayout body;
    private TextView shipping_fee_text;
    private TextView totalPriceTextView;
    private LinearLayout submit;

    private ShoppingCartModel shoppingCartModel;
    private float totalGoods_fee; // 总价格
    private float final_fee; //最终结算价格

    private ArrayList<GOODS_LIST> goodslist;
    private BalanceAdapter adapter;
    private String paymentJSONString;

    private PAYMENT payment;
    private SHIPPING shipping;
    BONUS selectedBONUS;

    private String integralNum = ""; // 兑换的积分数
    private String integralChangedMoney = null; // 积分兑换的钱
    private String integralChangedMoneyFormated = null; // 积分兑换的钱

    private String inv_type = null; // 发票类型
    private String inv_content = null; // 发票内容
    private String inv_payee = null; // 发票抬头
    private String remark_content;//备注信息

    private TextView tvredpager;
    private TextView tvintegral;
    private TextView tvinvoice;
    private ImageView ivredpager;
    private ImageView ivintegral;
    private ImageView ivinvoice;
    //private String discount_fee;// 折扣优惠价格

    ORDER_INFO order_info;
    private LinearLayout moreitem;
    private ListView goodslistview;
    private View invoiceline;
    private LinearLayout balance_distance;
    private Resources resource;
    private TextView balance_cast_text, balance_discount_text;//商品总价和放心
    private Intent getintent;
    private String rec_ids = "";//需要购买的商品id列表
    private String address_id = "";
    String yuan_unit;
    String yuan;
    private SharedPreferences shared;
    private HorizontalListView goodshorilistview;
    private LinearLayout ll_goodslist;
    private GoodsHorizLVAdapter horiadapter;
    private TextView invoice_fee_text;
    float invoice_fee = 0.0f;//发票价格
    private int need_inv = 0;//是否需要发票
    private String inv_rate = null;
    private float discount_fee;//返现
    private float shipping_fee;//运费
    private float bonus_fee;//红包
    private float integral_fee;//积分金额
    private TextView bonus_fee_text;//红包textView
    private TextView integral_fee_text;//积分textview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance);
        PushAgent.getInstance(this).onAppStart();
        shared = getSharedPreferences("userInfo", 0);
        title = (TextView) findViewById(R.id.top_view_text);
        resource = (Resources) getBaseContext().getResources();
        String set = resource.getString(R.string.shopcarfooter_settleaccounts);
        yuan_unit = resource.getString(R.string.yuan_unit);
        yuan = resource.getString(R.string.yuan);
        title.setText(set);

        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        getintent = getIntent();
        rec_ids = getintent.getStringExtra("rec_ids");
        address_id = getintent.getStringExtra(IntentKeywords.ADDRESS_ID);
        goodslist = new ArrayList<GOODS_LIST>();
        // 商品列表
        goodslistview = (ListView) findViewById(R.id.balance_goodslistview);
        goodshorilistview = (HorizontalListView) findViewById(R.id.balance_horilistview);
        ll_goodslist = (LinearLayout) findViewById(R.id.ll_goodslist);
        goodshorilistview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BalanceActivity.this, ShopGoodsActivity.class);
                intent.putExtra("datalist", (Serializable) goodslist);
                startActivity(intent);
            }
        });

        balance_distance = (LinearLayout) findViewById(R.id.balance_distance);
        user = (LinearLayout) findViewById(R.id.balance_user);
        name = (TextView) findViewById(R.id.balance_name);
        phoneNum = (TextView) findViewById(R.id.balance_phoneNum);
        address = (TextView) findViewById(R.id.balance_address);

        pay = (LinearLayout) findViewById(R.id.balance_pay);
        pay_type = (TextView) findViewById(R.id.balance_pay_type);
        dis = (LinearLayout) findViewById(R.id.balance_dis);
        shipping_name_text = (TextView) findViewById(R.id.balance_dis_type);
        invoice = (LinearLayout) findViewById(R.id.balance_invoice);
        invoice_message = (TextView) findViewById(R.id.balance_invoice_message);

        goods = (LinearLayout) findViewById(R.id.balance_goods);
        goods_num = (TextView) findViewById(R.id.balance_goods_num);
        redPaper = (LinearLayout) findViewById(R.id.balance_redPaper);
        redPaper_name = (TextView) findViewById(R.id.balance_redPaper_name);
        integral = (LinearLayout) findViewById(R.id.balance_integral);
        integral_num = (TextView) findViewById(R.id.balance_integral_num);
        moreitem = (LinearLayout) findViewById(R.id.balance_moreitem_body);

        // 新增红包,积分和修改发票的属性
        tvredpager = (TextView) findViewById(R.id.tv_redPaper);
        tvintegral = (TextView) findViewById(R.id.tv_integral);
        tvinvoice = (TextView) findViewById(R.id.tv_invoice);
        ivredpager = (ImageView) findViewById(R.id.iv_redpager);
        ivintegral = (ImageView) findViewById(R.id.iv_integral);
        ivinvoice = (ImageView) findViewById(R.id.iv_invoice);
        // discount = (TextView) findViewById(R.id.balance_discount);
        invoiceline = findViewById(R.id.invoice_line);

        totalPriceTextView = (TextView) findViewById(R.id.balance_total);
        submit = (LinearLayout) findViewById(R.id.balance_submit);

        remark = (LinearLayout) findViewById(R.id.balance_remark);
        tvremark = (TextView) findViewById(R.id.tv_remark);

        balance_cast_text = (TextView) findViewById(R.id.balance_cast_text);//商品金额
        balance_discount_text = (TextView) findViewById(R.id.balance_goback_text);//返现
        invoice_fee_text = (TextView) findViewById(R.id.invoice);//发票
        shipping_fee_text = (TextView) findViewById(R.id.balance_fees);//运费
        bonus_fee_text = (TextView) findViewById(R.id.balance_bonus_fee);//红包
        integral_fee_text = (TextView) findViewById(R.id.balance_integral_fee);//积分


        user.setOnClickListener(this);
        pay.setOnClickListener(this);
        dis.setOnClickListener(this);
        invoice.setOnClickListener(this);
        goods.setOnClickListener(this);
        redPaper.setOnClickListener(this);
        integral.setOnClickListener(this);
        submit.setOnClickListener(this);
        remark.setOnClickListener(this);
        if (null == shoppingCartModel) {
            shoppingCartModel = new ShoppingCartModel(this);
            shoppingCartModel.addResponseListener(this);
            shoppingCartModel.checkOrder(rec_ids, address_id);
        } else {
            setInfo();
        }
    }

    @Override
    public void onClick(View v) {
        resource = getBaseContext().getResources();
        String choose_payment_firse = resource.getString(R.string.balance_choose_payment_first);
        String invoice_close = resource.getString(R.string.balance_invoice_close);
        String notsupport_redpaper = resource.getString(R.string.balance_notsupport_redpaper);
        String notsupport_integral = resource.getString(R.string.balance_notsupport_integral);
        String choose_payment = resource.getString(R.string.balance_choose_payment);
        String choose_shipping = resource.getString(R.string.balance_choose_shipping);
        String notsupport_COD = resource.getString(R.string.balance_notsupport_COD);
        Intent intent;
        switch (v.getId()) {
            case R.id.balance_user:
                intent = new Intent(this, AddressManageActivity.class);
                intent.putExtra("flag", 1);
                startActivityForResult(intent, 1);
                this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.balance_pay:
                if (shoppingCartModel.payment_list.size() == 0) {
                    ToastView toast1 = new ToastView(BalanceActivity.this, R.string.balance_nopayment);
                    toast1.setGravity(Gravity.CENTER, 0, 0);
                    toast1.show();
                } else {
                    intent = new Intent(this, PaymentActivity.class);
                    intent.putExtra("payment", paymentJSONString);
                    startActivityForResult(intent, 2);
                    this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    LG.i("+++++跳转至PaymentActivity页面+++++");
                }
                break;
            case R.id.balance_dis:
                if (StringUtils.isEmpty(pay_type.getText().toString())) {
                    ToastView toast1 = new ToastView(BalanceActivity.this, choose_payment_firse);
                    toast1.setGravity(Gravity.CENTER, 0, 0);
                    toast1.show();
                } else {
                    intent = new Intent(this, ShippingActivity.class);
                    intent.putExtra("payment", paymentJSONString);
                    startActivityForResult(intent, 3);
                    this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    LG.i("+++++跳转至ShippingActivity页面+++++");
                }
                break;
            case R.id.balance_invoice:
                if ("0".equals(shoppingCartModel.allow_can_invoice)) {
                    ToastView toast = new ToastView(BalanceActivity.this, invoice_close);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    intent = new Intent(this, InvoiceActivity.class);
                    intent.putExtra("payment", paymentJSONString);
                    intent.putExtra("inv_type", inv_type);
                    intent.putExtra("inv_content", inv_content);
                    intent.putExtra("inv_payee", inv_payee);
                    startActivityForResult(intent, 5);
                    this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    LG.i("+++++跳转至InvoiceActivity页面+++++");
                }
                break;
            case R.id.balance_goods:

                Resources resource = (Resources) getBaseContext().getResources();
                String list = resource.getString(R.string.balance_list);
                ToastView toast = new ToastView(this, list);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
            case R.id.balance_redPaper://红包
                if (shoppingCartModel.bonusinfo == "null") {
                    toast = new ToastView(BalanceActivity.this, notsupport_redpaper);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    try {
                        JSONObject jo = new JSONObject(shoppingCartModel.orderInfoJsonString);
                        String bonus = jo.optString("allow_use_bonus");
                        if (bonus.equals("1")) {
                            intent = new Intent(this, RedPacketsActivity.class);
                            intent.putExtra("payment", paymentJSONString);
                            startActivityForResult(intent, 6);
                            this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                        } else {
                            Resources resourc = (Resources) getBaseContext().getResources();
                            String not = resourc.getString(R.string.not_support_a_red_envelope);
                            ToastView toast2 = new ToastView(BalanceActivity.this, not);
                            toast2.setGravity(Gravity.CENTER, 0, 0);
                            toast2.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.balance_integral://积分
                if (shoppingCartModel.all_use_integral == 0 || min_inteagral == 0) {
                    toast = new ToastView(BalanceActivity.this, notsupport_integral);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    intent = new Intent(this, IntegralActivity.class);
                    intent.putExtra("integral", paymentJSONString);
                    intent.putExtra("used_integral", integralNum);
                    startActivityForResult(intent, 4);
                    this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
                break;
            case R.id.balance_remark:

                intent = new Intent(this, RemarkActivity.class);
                intent.putExtra("remark", remark_content);
                startActivityForResult(intent, 7);
                this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.balance_submit:

                if (null == payment) {
                    ToastView toast1 = new ToastView(BalanceActivity.this, choose_payment);
                    toast1.setGravity(Gravity.CENTER, 0, 0);
                    toast1.show();
                    return;
                }

                if (null == shipping) {
                    ToastView toast1 = new ToastView(BalanceActivity.this, choose_shipping);
                    toast1.setGravity(Gravity.CENTER, 0, 0);
                    toast1.show();
                    return;
                }

                if (checkCashOnDeliverOk(payment, shipping)) {
                    if (null != selectedBONUS) {
                        shoppingCartModel.flowDone(rec_ids,payment.getPay_id(), address_id, shipping.getShipping_id(), selectedBONUS.getBonus_id(), integralNum, inv_type, inv_payee, inv_content,
                                remark_content);
                    } else {
                        shoppingCartModel.flowDone(rec_ids,payment.getPay_id(), address_id, shipping.getShipping_id(), null, integralNum, inv_type, inv_payee, inv_content, remark_content);
                    }

                } else {
                    ToastView toast1 = new ToastView(BalanceActivity.this, notsupport_COD);
                    toast1.setGravity(Gravity.CENTER, 0, 0);
                    toast1.show();
                }
                break;
        }

    }

    public boolean checkCashOnDeliverOk(PAYMENT payment, SHIPPING shipping) {
        if (null != payment && null != shipping) {
            if (payment.getIs_cod().equals("1") && shipping.getSupport_cod().equals("0")) {
                return false;
            }
        }
        return true;
    }

    public void setInfo() {

        resource = getBaseContext().getResources();
        String yuan = resource.getString(R.string.yuan);
        String yuan_unit = resource.getString(R.string.yuan_unit);

        totalGoods_fee = 0;

        paymentJSONString = shoppingCartModel.orderInfoJsonString;

        name.setText(shoppingCartModel.address.getConsignee());
        phoneNum.setText(shoppingCartModel.address.getMobile());

        StringBuffer sbf = new StringBuffer();
        sbf.append(shoppingCartModel.address.getProvince_name() + " ");
        sbf.append(shoppingCartModel.address.getCity_name() + " ");
        sbf.append(shoppingCartModel.address.getDistrict_name() + " ");
        sbf.append(shoppingCartModel.address.getAddress());
//        address.setText(sbf.toString());
        String c = ToDBC(sbf.toString());
        address.setText(c);


        //商品总价
        for (int i = 0; i < shoppingCartModel.balance_goods_list.size(); i++) {
            goods_num.setText("X " + shoppingCartModel.balance_goods_list.get(i).getGoods_number());
            totalGoods_fee += Float.parseFloat(shoppingCartModel.balance_goods_list.get(i).getSubtotal().replace("$", "").replace("￥", "").replace("元", ""));
        }
        balance_cast_text.setText(yuan_unit + FormatUtil.formatFloatTwoDecimal(totalGoods_fee) + yuan);

        //折扣价
        discount_fee = Float.parseFloat(shoppingCartModel.discount_fee);

        balance_discount_text.setText("-" + yuan_unit + shoppingCartModel.discount_fee + yuan);
        if (discount_fee > 0) {
            findViewById(R.id.discount_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.discount_layout).setVisibility(View.GONE);
        }
        //运费
        if (null != shipping && null != shipping.getShipping_fee()) {
            shipping_fee = Float.parseFloat(shipping.getShipping_fee());
        } else {
            shipping_fee = 0.00f;
        }
        // 显示折扣价格
        if ("0".equals(shoppingCartModel.allow_can_invoice)) {// 发票信息
            invoice.setVisibility(View.GONE);
            invoiceline.setVisibility(View.GONE);
        } else {
            invoice.setBackgroundResource(R.drawable.selecter_newitem_press);
        }


        // 判断红包是否可用
        if (shoppingCartModel.bonusinfo == "null") {
            tvredpager.setTextColor(this.getResources().getColor(R.color.useless));
            ivredpager.setVisibility(View.GONE);
        } else {
            redPaper.setBackgroundResource(R.drawable.selecter_newitem_press);
            redPaper.setEnabled(true);
        }

        // 判断积分是否可用
        min_inteagral = Math.min(shoppingCartModel.your_integral, shoppingCartModel.order_max_integral);
        if (min_inteagral == 0 || shoppingCartModel.all_use_integral == 0) {
            tvintegral.setTextColor(this.getResources().getColor(R.color.useless));
            ivintegral.setVisibility(View.GONE);
        } else {
            integral.setBackgroundResource(R.drawable.selecter_newitem_press);
            integral.setEnabled(true);
        }

        refreshTotalPrice();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (data != null) {
                    address_id = data.getStringExtra("address_id");
                    shoppingCartModel.checkOrder(rec_ids, address_id);
            }
        } else if (requestCode == 2) {
            if (data != null) {
                String paymentString = data.getStringExtra("payment");
                try {
                    JSONObject paymentJSONObject = new JSONObject(paymentString);
                    payment = PAYMENT.fromJson(paymentJSONObject);
                    pay_type.setText(payment.getPay_name());
                    if (payment.getPay_code().equals(AppConst.COD)) {
                        AppConst.iscod = true;
                    } else {
                        AppConst.iscod = false;
                    }
                    LG.i("paycode===" + payment.getPay_code());
                } catch (JSONException e) {

                }
                String shippingString = data.getStringExtra("shipping");
                if (StringUtils.isNotEmpty(shippingString)) {
                    try {
                        JSONObject shippingJSONObject = new JSONObject(shippingString);
                        shipping = SHIPPING.fromJson(shippingJSONObject);
                        try {
                            shipping_fee = Float.parseFloat(shipping.getShipping_fee());
                        } catch (Exception e) {
                            shipping_fee = 0.00f;
                        }
                        shipping_name_text.setText(shipping.getShipping_name());
                        shipping_fee_text.setText("+" + shipping.getFormat_shipping_fee() + yuan);
                        refreshTotalPrice();
                    } catch (JSONException e) {

                    }
                }

            }
        } else if (requestCode == 3) {//配送方式
            if (data != null) {
                String shippingString = data.getStringExtra("shipping");
                try {
                    JSONObject shippingJSONObject = new JSONObject(shippingString);
                    shipping = SHIPPING.fromJson(shippingJSONObject);
                    try {
                        shipping_fee = Float.parseFloat(shipping.getShipping_fee());
                    } catch (Exception e) {
                        shipping_fee = 0.00f;
                    }

                    shipping_name_text.setText(shipping.getShipping_name());
                    shipping_fee_text.setText("+" + shipping.getFormat_shipping_fee() + yuan);
                    refreshTotalPrice();
                } catch (JSONException e) {

                }
            }
        } else if (requestCode == 4) {//积分
            if (data != null) {
                integralNum = data.getStringExtra("input");
                if (TextUtils.isEmpty(integralNum)) {
                    integral_num.setText("");
                    integralChangedMoney = "0";
                    integral_fee = 0.00f;
                    integralChangedMoneyFormated = "";
                } else {
                    Resources resource = (Resources) getBaseContext().getResources();
                    String use = resource.getString(R.string.use);
                    String inte = resource.getString(R.string.integral_integral);
                    integral_num.setText(use + integralNum + inte);
                    integralChangedMoney = data.getStringExtra("bonus");
                    if (TextUtils.isEmpty(integralChangedMoney)) {
                        try {
                            integral_fee = Float.parseFloat(integralChangedMoney);
                        } catch (Exception e) {
                            integral_fee = 0.00f;
                        }
                    }
                    integralChangedMoneyFormated = data.getStringExtra("bonus_formated");
                }

                if (integral_fee > 0) {
                    findViewById(R.id.integral_layout).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.integral_layout).setVisibility(View.GONE);
                }

                integral_fee_text.setText("-" + yuan_unit + FormatUtil.formatFloatTwoDecimal(bonus_fee) + yuan);
                refreshTotalPrice();
            }
        } else if (requestCode == 5) {//发票
            if (data != null) {
                final_fee = final_fee - invoice_fee;
                inv_type = data.getStringExtra("inv_type");
                inv_content = data.getStringExtra("inv_content");
                inv_payee = data.getStringExtra("inv_payee");
                invoice_message.setText(inv_payee);
                inv_rate = data.getStringExtra("inv_type_rate");
                if (!TextUtils.isEmpty(inv_rate)) {
                    invoice_fee = totalGoods_fee * Float.parseFloat(inv_rate) / 100;
                } else {
                    invoice_fee = 0.00f;
                }

                if (invoice_fee > 0) {
                    findViewById(R.id.invoice_layout).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.invoice_layout).setVisibility(View.GONE);
                }

                invoice_fee_text.setText("+" + yuan_unit + FormatUtil.formatFloatTwoDecimal(invoice_fee) + yuan);
                //刷新价格
                refreshTotalPrice();
            }
        } else if (requestCode == 6) {//红包回调
            if (data != null) {
                String bonusJSONString = data.getStringExtra("bonus");
                if (null != bonusJSONString) {
                    try {
                        JSONObject jsonObject = new JSONObject(bonusJSONString);
                        selectedBONUS = BONUS.fromJson(jsonObject);

                        if (selectedBONUS != null) {
                            try {
                                bonus_fee = Float.parseFloat(selectedBONUS.getType_money());
                            } catch (Exception e) {
                                bonus_fee = 0.0f;
                            }
                        }
                        redPaper_name.setText(selectedBONUS.getType_name() + "[" + selectedBONUS.getBonus_money_formated() + "]");
                        refreshTotalPrice();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    selectedBONUS = null;
                    bonus_fee = 0.00f;
                    redPaper_name.setText("");
                }

                if (bonus_fee > 0) {
                    findViewById(R.id.bonus_layout).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.bonus_layout).setVisibility(View.GONE);
                }

                bonus_fee_text.setText("-" + yuan_unit + FormatUtil.formatFloatTwoDecimal(bonus_fee) + yuan);
                refreshTotalPrice();
            }
        } else if (requestCode == 7) {
            if (data != null) {
                remark_content = data.getStringExtra("remark");
                tvremark.setText(remark_content);
            }
        }

    }

    /**
     * 税费 = （商品总金额 - 优惠）* 税率
     * 总价 = 2
     * + 税费 - 红包 - 积分 + 运费
     */
    void refreshTotalPrice() {


        final_fee = totalGoods_fee - discount_fee;

        //处理发票
        if (final_fee < 0) {
            final_fee = 0.00f;
        }
        final_fee = final_fee + invoice_fee;

        //处理红包
        if (final_fee < 0) {
            final_fee = 0.00f;
        }
        final_fee = final_fee - bonus_fee;


        //处理积分
        if (final_fee < 0) {
            final_fee = 0.00f;
        }
        final_fee = final_fee - integral_fee;


        //处理运费
        if (final_fee < 0) {
            final_fee = 0.00f;
        }
        final_fee = final_fee + shipping_fee;

        //总价格显示
        totalPriceTextView.setText(yuan_unit + FormatUtil.formatFloatTwoDecimal(final_fee) + yuan);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        return true;

    }

    // 设置listview让其完全显示
    private void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
//        for (int i = 0; i < listAdapter.getCount(); i++) {
        for (int i = 0; i < 1; i++) {

            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        params.height = totalHeight + (listView.getDividerHeight());
        listView.setLayoutParams(params);
    }

    public String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }


    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {

        if (url.equals(ProtocolConst.CHECKORDER)) {
            if (status.getSucceed() == 1) {
                //默认支付方式
                if (AppConst.paymentlist != null && AppConst.paymentlist.size() > 0
                        && shoppingCartModel.payment_list != null && shoppingCartModel.payment_list.size() > 0) {
                    if (shoppingCartModel.onlinepaymentlist.size() > 0) {

                        if (shoppingCartModel.uplinepaymentlist.size() > 0) {
                            for (int i = 0; i < AppConst.paymentlist.size(); i++) {
                                if ("true".equals(AppConst.paymentlist.get(i).get("select"))) {
                                    if (i < shoppingCartModel.onlinepaymentlist.size()) {
                                        pay_type.setText(shoppingCartModel.onlinepaymentlist.get(i).getPay_name());
                                        payment = shoppingCartModel.onlinepaymentlist.get(i);
                                    } else {
                                        pay_type.setText(shoppingCartModel.uplinepaymentlist.get(i - shoppingCartModel.onlinepaymentlist.size()).getPay_name());
                                        payment = shoppingCartModel.uplinepaymentlist.get(i - shoppingCartModel.onlinepaymentlist.size());
                                    }
                                    break;
                                }
                            }
                        } else {
                            for (int i = 0; i < AppConst.paymentlist.size(); i++) {
                                if ("true".equals(AppConst.paymentlist.get(i).get("select"))) {
                                    pay_type.setText(shoppingCartModel.onlinepaymentlist.get(i).getPay_name());
                                    payment = shoppingCartModel.onlinepaymentlist.get(i);
                                    break;
                                }
                            }
                        }
                    } else {
                        for (int i = 0; i < AppConst.paymentlist.size(); i++) {
                            if ("true".equals(AppConst.paymentlist.get(i).get("select"))) {
                                pay_type.setText(shoppingCartModel.uplinepaymentlist.get(i).getPay_name());
                                payment = shoppingCartModel.uplinepaymentlist.get(i);
                                break;
                            }
                        }
                    }
                } else {
                    if (shoppingCartModel.onlinepaymentlist != null && shoppingCartModel.onlinepaymentlist.size() > 0) {
                        pay_type.setText(shoppingCartModel.onlinepaymentlist.get(0).getPay_name());
                        payment = shoppingCartModel.onlinepaymentlist.get(0);
                    } else if (shoppingCartModel.uplinepaymentlist != null && shoppingCartModel.uplinepaymentlist.size() > 0) {
                        pay_type.setText(shoppingCartModel.uplinepaymentlist.get(PaymentActivity.position).getPay_name());
                        payment = shoppingCartModel.uplinepaymentlist.get(PaymentActivity.position);
                    } else {
                        pay_type.setText("");
                    }
                }

                //默认配送方式
                if (AppConst.shippinglist == null) {
                    if (shoppingCartModel.shipping_list.size() > 0) {
                        AppConst.shippinglist = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < shoppingCartModel.shipping_list.size(); i++) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("select", "false");
                            AppConst.shippinglist.add(map);
                        }
                        AppConst.shippinglist.get(0).put("select", "true");
                        shipping_name_text.setText(shoppingCartModel.shipping_list.get(0).getShipping_name());
                        shipping = shoppingCartModel.shipping_list.get(0);
                        try {
                            shipping_fee = Float.parseFloat(shipping.getShipping_fee());
                        } catch (Exception e) {
                            shipping_fee = 0.00f;
                        }
                        shipping_fee_text.setText("+" + shipping.getFormat_shipping_fee() + yuan);
                    } else {
                        shipping_name_text.setText("");
                    }
                } else {

                    if (shoppingCartModel.shipping_list.size() > 0) {
                        AppConst.shippinglist.get(ShippingAdapter.positionIndex).put("select", "true");
                        shipping_name_text.setText(shoppingCartModel.shipping_list.get(ShippingAdapter.positionIndex).getShipping_name());
                        shipping = shoppingCartModel.shipping_list.get(ShippingAdapter.positionIndex);
                        try {
                            shipping_fee = Float.parseFloat(shipping.getShipping_fee());
                        } catch (Exception e) {
                            shipping_fee = 0.00f;
                        }
                        shipping_fee_text.setText("+" + shipping.getFormat_shipping_fee() + yuan);
                    } else {
                        shipping_name_text.setText("");
                        ShippingAdapter.positionIndex = 0;
                    }
                }
                setInfo();
                goodslist = shoppingCartModel.balance_goods_list;

//                if (goodslist.size() == 1) {
                    adapter = new BalanceAdapter(BalanceActivity.this, goodslist);
                    goodslistview.setAdapter(adapter);
                    balance_distance.setVisibility(View.VISIBLE);
                    setListViewHeightBasedOnChildren(goodslistview);
                    ll_goodslist.setVisibility(View.GONE);
//                } else if (goodslist.size() > 1) {
//                    horiadapter = new GoodsHorizLVAdapter(BalanceActivity.this, goodslist);
//                    goodshorilistview.setAdapter(horiadapter);
//                    goodslistview.setAdapter(null);
//                    balance_distance.setVisibility(View.VISIBLE);
//                    ll_goodslist.setVisibility(View.VISIBLE);
//                }
            }else{

            }
        } else {
            pay.setClickable(false);
            dis.setClickable(false);
        }


        if (url.equals(ProtocolConst.FLOW_DOWN)) {
            if (status.getSucceed() == 1) {
                JSONObject json = null;
                try {
                    json = shoppingCartModel.flowdownjson.getJSONObject("data");
                    JSONObject orderObject = json.optJSONObject("order_info");
                    order_info = ORDER_INFO.fromJson(orderObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(BalanceActivity.this, ChoosePayActivity.class);
                intent.putExtra(IntentKeywords.PAY_TYPE, IntentKeywords.ORDER_ID);
                intent.putExtra(IntentKeywords.ORDER_ID, order_info.getOrder_id()+"");
                intent.putExtra(IntentKeywords.PAY_IS_CREATE, true);
                intent.putExtra(IntentKeywords.PAY_BODY, getBody());
                intent.putExtra(IntentKeywords.PAY_AMOUNT, order_info.getOrder_amount() + "");
                intent.putExtra(IntentKeywords.PAY_CODE, order_info.getPay_code());
                startActivity(intent);
                finish();
                BalanceActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }else{

            }
        }

    }

    private String getBody() {
        final String order_incloud = resource.getString(R.string.balance_order_incloud);
        final String deng = resource.getString(R.string.balance_deng);
        final String zhong_goods = resource.getString(R.string.balance_zhong_goods);
        String body = order_incloud + shoppingCartModel.balance_goods_list.get(0).getGoods_name() + deng + shoppingCartModel.balance_goods_list.size() + zhong_goods;
        return body;
    }
}
