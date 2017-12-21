package com.ecjia.widgets.paycenter.alipay;

import com.ecjia.widgets.paycenter.base.BasePaymentData;

//支付宝支付的数据实体类
public class AlipayData implements BasePaymentData {
    private String order_amount;//金额
    private String order_id;
    private String subject;
    private String partner;
    private String seller_id;
    private String notify_url;
    private String pay_order_sn;
    private String body;
    private String private_key;

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}