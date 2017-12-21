package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/3/12.
 */
public class RECHARGE {

    private String pay_code;
    private String pay_name;

    private String subject;
    private String order_sn;
    private String order_logid;
    private String order_amount;
    private String notify_url;
    private String callback_url;
    private String pay_order_sn;
    private String pay_online;
    private String partner;
    private String seller_id;
    private String account_id;


    private String pay_upmp_tn;

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    private String private_key;

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getPay_code() {
        return pay_code;
    }

    public void setPay_code(String pay_code) {
        this.pay_code = pay_code;
    }

    public String getPay_name() {
        return pay_name;
    }

    public void setPay_name(String pay_name) {
        this.pay_name = pay_name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getOrder_logid() {
        return order_logid;
    }

    public void setOrder_logid(String order_logid) {
        this.order_logid = order_logid;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getCallback_url() {
        return callback_url;
    }

    public void setCallback_url(String callback_url) {
        this.callback_url = callback_url;
    }

    public String getPay_order_sn() {
        return pay_order_sn;
    }

    public void setPay_order_sn(String pay_order_sn) {
        this.pay_order_sn = pay_order_sn;
    }

    public String getPay_upmp_tn() {
        return pay_upmp_tn;
    }

    public void setPay_upmp_tn(String pay_upmp_tn) {
        this.pay_upmp_tn = pay_upmp_tn;
    }

    public String getPay_online() {
        return pay_online;
    }

    public void setPay_online(String pay_online) {
        this.pay_online = pay_online;
    }

    public static RECHARGE fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        RECHARGE localItem = new RECHARGE();

        localItem.subject = jsonObject.optString("subject");
        localItem.partner = jsonObject.optString("partner");
        localItem.order_sn = jsonObject.optString("order_sn");
        localItem.order_logid = jsonObject.optString("order_logid");
        localItem.order_amount = jsonObject.optString("order_amount");
        localItem.seller_id = jsonObject.optString("seller_id");
        localItem.notify_url = jsonObject.optString("notify_url");
        localItem.callback_url = jsonObject.optString("callback_url");
        localItem.pay_order_sn = jsonObject.optString("pay_order_sn");
        localItem.pay_code = jsonObject.optString("pay_code");
        localItem.pay_name = jsonObject.optString("pay_name");
        localItem.private_key = jsonObject.optString("private_key");
        localItem.pay_online = jsonObject.optString("pay_online");


        localItem.pay_upmp_tn = jsonObject.optString("pay_upmp_tn");
        localItem.account_id = jsonObject.optString("rec_id");


        return localItem;
    }


}
