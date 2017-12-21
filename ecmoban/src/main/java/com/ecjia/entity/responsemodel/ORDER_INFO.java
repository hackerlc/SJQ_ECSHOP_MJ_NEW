package com.ecjia.entity.responsemodel;


import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;


public class ORDER_INFO implements Serializable {

    private String pay_code;
    private String order_amount;
    private int order_id;
    private String order_sn;
    private String subject;
    private String desc;

    private ORDER_INFO order_info;

    public ORDER_INFO getOrder_info() {
        return order_info;
    }

    public void setOrder_info(ORDER_INFO order_info) {
        this.order_info = order_info;
    }

    public String getPay_code() {
        return pay_code;
    }

    public void setPay_code(String pay_code) {
        this.pay_code = pay_code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public static ORDER_INFO fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        ORDER_INFO localItem = new ORDER_INFO();
        localItem.pay_code = jsonObject.optString("pay_code");
        localItem.order_amount = jsonObject.optString("order_amount");
        localItem.order_id = jsonObject.optInt("order_id");
        localItem.subject = jsonObject.optString("subject");
        localItem.desc = jsonObject.optString("desc");
        localItem.order_sn = jsonObject.optString("order_sn");
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("pay_code", pay_code);
        localItemObject.put("order_amount", order_amount);
        localItemObject.put("order_id", order_id);
        localItemObject.put("subject", subject);
        localItemObject.put("desc", desc);
        localItemObject.put("order_sn", order_sn);
        return localItemObject;
    }

}
