package com.ecjia.network.netmodle;

import android.content.Context;
import android.content.res.Resources;

import com.ecjia.consts.ProtocolConst;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecjia.widgets.ToastView;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.widgets.paycenter.alipay.AlipayData;
import com.ecjia.widgets.paycenter.uppay.UppayData;
import com.ecjia.widgets.paycenter.wxpay.WXpayData;
import com.ecjia.util.LG;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Administrator on 2016/9/5.
 */
public class PayModel extends BaseModel {
    Context c;
    public MyProgressDialog pd;
    public Resources resources;
    public String order_id;
    public String pay_name;
    public UppayData uppayData;
    public AlipayData alipayData;
    public WXpayData wXpayData;
    public String pay_code;
    public String pay_online;
    public String pay_status;
    public String error_message;
    public String user_money;

    public PayModel(Context context) {
        super(context);
        this.c = context;
        resources = AppConst.getResources(context);
        pd = MyProgressDialog.createDialog(context);
        pd.setMessage(resources.getString(R.string.loading));
    }

    public void orderPay(String order_id) {
        this.order_id = order_id;
        final String url = ProtocolConst.ORDER_PAY;
        pd.show();
        SESSION session = SESSION.getInstance();
        final JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("order_id", order_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params = addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===order/pay传入===" + requestJsonObject.toString());

        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                LG.e("支付接口失败");
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                pd.dismiss();
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("===order/pay返回===" + jo.toString());
                    PayModel.this.callback(jo);
                    STATUS responseStatus = STATUS.newFromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject dataJsonObject = jo.optJSONObject("data");
                        JSONObject payment = dataJsonObject.optJSONObject("payment");
                        pay_name = payment.optString("pay_name");
                        pay_code = payment.optString("pay_code");
                        if (AppConst.ALIPAY_MOBILE.equals(pay_code)) {
                            alipayData = new AlipayData();
                            alipayData.setOrder_id(PayModel.this.order_id);
                            alipayData.setSubject(payment.optString("subject"));
                            alipayData.setPartner(payment.optString("partner"));
                            alipayData.setOrder_amount(payment.optString("order_amount"));
                            alipayData.setSeller_id(payment.optString("seller_id"));
                            alipayData.setNotify_url(payment.optString("notify_url"));
                            alipayData.setPay_order_sn(payment.optString("pay_order_sn"));
                            alipayData.setPrivate_key(payment.optString("private_key"));
                        } else if (AppConst.UPPAY.equals(pay_code)) {
                            uppayData = new UppayData();
                            uppayData.setPay_upmp_tn(payment.optString("pay_upmp_tn"));
                        } else if (AppConst.BANK.equals(pay_code)) {
                            pay_online = payment.optString("pay_online");
                        } else if (AppConst.BALANCE.equals(pay_code)) {
                            pay_status = payment.optString("pay_status");
                            error_message = payment.optString("error_message");
                            user_money = payment.optString("user_money");
                        } else if (AppConst.WXPAY.equals(pay_code)) {
                            wXpayData = new WXpayData();
                            wXpayData.setSubject(payment.optString("subject"));
                            wXpayData.setOrder_amount(payment.optString("order_amount"));
                            wXpayData.setNotify_url(payment.optString("notify_url"));
                            wXpayData.setPay_order_sn(payment.optString("pay_order_sn"));
                            wXpayData.setMch_id(payment.optString("mch_id"));
                            wXpayData.setApipwd(payment.optString("private_key"));
                        }
                        pay_online = payment.optString("pay_online");
                    }
                    PayModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===order/pay返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });

    }

    /**
     * 余额充值付款
     */
    public void accountPay(final String payment_id, String account_id) {
        final String url = ProtocolConst.USER_ACCOUNT_PAY;
        SESSION session = SESSION.getInstance();
        pd.show();
        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("payment_id", payment_id);
            requestJsonObject.put("account_id", account_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params = addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===user/account/pay传入===" + requestJsonObject.toString());

        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                pd.dismiss();
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("===user/account/pay返回===" + jo.toString());
                    PayModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject json = jo.optJSONObject("data");
                        JSONObject payment = json.optJSONObject("payment");
                        pay_name = payment.optString("pay_name");
                        pay_code = payment.optString("pay_code");
                        if (AppConst.ALIPAY_MOBILE.equals(pay_code)) {
                            alipayData = new AlipayData();
                            alipayData.setSubject(payment.optString("subject"));
                            alipayData.setPartner(payment.optString("partner"));
                            alipayData.setOrder_amount(payment.optString("order_amount"));
                            alipayData.setSeller_id(payment.optString("seller_id"));
                            alipayData.setNotify_url(payment.optString("notify_url"));
                            alipayData.setPay_order_sn(payment.optString("pay_order_sn"));
                            alipayData.setPrivate_key(payment.optString("private_key"));
                        } else if (AppConst.UPPAY.equals(pay_code)) {
                            uppayData = new UppayData();
                            uppayData.setPay_upmp_tn(payment.optString("pay_upmp_tn"));
                        } else if (AppConst.BANK.equals(pay_code)) {
                            pay_online = payment.optString("pay_online");
                        } else if (AppConst.BALANCE.equals(pay_code)) {
                            pay_status = payment.optString("pay_status");
                            error_message = payment.optString("error_message");
                            user_money = payment.optString("user_money");
                        } else if (AppConst.WXPAY.equals(pay_code)) {
                            wXpayData = new WXpayData();
                            wXpayData.setSubject(payment.optString("subject"));
                            wXpayData.setOrder_amount(payment.optString("order_amount"));
                            wXpayData.setNotify_url(payment.optString("notify_url"));
                            wXpayData.setPay_order_sn(payment.optString("pay_order_sn"));
                            wXpayData.setMch_id(payment.optString("mch_id"));
                            wXpayData.setApipwd(payment.optString("apipwd"));
                        }
                        pay_online = payment.optString("pay_online");

                    } else {
                        ToastView toast = new ToastView(c, responseStatus.getError_desc());
                        toast.show();
                    }
                    PayModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===user/account/pay返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }

}
