package com.ecjia.widgets.paycenter.wxpay;

import com.ecjia.widgets.paycenter.base.BasePaymentData;

//微信支付的数据实体类
public class WXpayData implements BasePaymentData {
    private String order_amount;//金额
    private String subject;
    private String notify_url;
    private String apipwd;
    private String mch_id;
    private String pay_order_sn;

    public String getApipwd() {
        return apipwd;
    }

    public void setApipwd(String apipwd) {
        this.apipwd = apipwd;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getPay_order_sn() {
        return pay_order_sn;
    }

    public void setPay_order_sn(String pay_order_sn) {
        this.pay_order_sn = pay_order_sn;
    }
}