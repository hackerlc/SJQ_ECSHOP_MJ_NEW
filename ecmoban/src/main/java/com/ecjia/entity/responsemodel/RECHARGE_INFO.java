package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/3/16.
 */
public class RECHARGE_INFO {
    private String account_id;//记录id
    private String user_id;//用户id
    private String admin_user;//管理员名称
    private String amount;//金额
    private String format_amount;
    private String add_time;//申请时间
    private String user_note;//用户备注

    private String type;//申请类型
    private String type_lable;
    private String payment_name;//付款方式
    private String payment_id;
    private String is_paid;//是否已付款
    private String pay_status;//申请状态

    public String getAccount_id() {
        return account_id;
    }

    public void setAccountid(String account_id) {
        this.account_id = account_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAdmin_user() {
        return admin_user;
    }

    public void setAdmin_user(String admin_user) {
        this.admin_user = admin_user;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFormat_amount() {
        return format_amount;
    }

    public void setFormat_amount(String format_amount) {
        this.format_amount = format_amount;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getUser_note() {
        return user_note;
    }

    public void setUser_note(String user_note) {
        this.user_note = user_note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_lable() {
        return type_lable;
    }

    public void setType_lable(String type_lable) {
        this.type_lable = type_lable;
    }

    public String getPayment_name() {
        return payment_name;
    }

    public void setPayment_name(String payment_name) {
        this.payment_name = payment_name;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getIs_paid() {
        return is_paid;
    }

    public void setIs_paid(String is_paid) {
        this.is_paid = is_paid;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public static RECHARGE_INFO fromJson(JSONObject jsonObject)  throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        RECHARGE_INFO info=new RECHARGE_INFO();

        info.account_id=jsonObject.optString("account_id");
        info.user_id=jsonObject.optString("user_id");
        info.admin_user=jsonObject.optString("admin_user");
        info.amount=jsonObject.optString("amount");
        info.format_amount=jsonObject.optString("format_amount");
        info.user_note=jsonObject.optString("user_note");
        info.type=jsonObject.optString("type");
        info.type_lable=jsonObject.optString("type_lable");
        info.payment_name=jsonObject.optString("payment_name");
        info.payment_id=jsonObject.optString("payment_id");
        info.is_paid=jsonObject.optString("is_paid");
        info.pay_status=jsonObject.optString("pay_status");
        info.add_time=jsonObject.optString("add_time");

        return info;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject json = new JSONObject();

        json.put("account_id",account_id);
        json.put("user_id",user_id);
        json.put("admin_user",admin_user);
        json.put("amount",amount);
        json.put("format_amount",format_amount);
        json.put("user_note",user_note);
        json.put("type",type);
        json.put("type_lable",type_lable);
        json.put("payment_name",payment_name);
        json.put("payment_id",payment_id);
        json.put("is_paid",is_paid);
        json.put("pay_status",pay_status);
        json.put("add_time",add_time);

        return json;
    }
}
