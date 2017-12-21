
package com.ecjia.entity.responsemodel;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GOODORDER implements Serializable {

    private String order_time;
    private String total_fee;
    private ArrayList<ORDER_GOODS_LIST> goods_list = new ArrayList<ORDER_GOODS_LIST>();
    private String formated_integral_money;
    private String formated_bonus;
    //新加入折扣
    private String formated_discount;
    //格式化的总价
    private String formated_total_fee;
    private String order_sn;
    private String order_id;
    private String goods_number;
    private String formated_shipping_fee;
    public ORDER_INFO order_info;

    private String order_status;//"2"
    private String pay_status;//"0"
    private String label_order_status;//"已取消"
    private String order_status_code;//"canceled"
    private boolean toComment;

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getLabel_order_status() {
        return label_order_status;
    }

    public void setLabel_order_status(String label_order_status) {
        this.label_order_status = label_order_status;
    }

    public String getOrder_status_code() {
        return order_status_code;
    }

    public void setOrder_status_code(String order_status_code) {
        this.order_status_code = order_status_code;
    }

    public boolean isToComment() {
        return toComment;
    }

    public void setToComment(boolean toComment) {
        this.toComment = toComment;
    }

    public String getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(String goods_number) {
        this.goods_number = goods_number;
    }

    public ORDER_INFO getOrder_info() {
        return order_info;
    }

    public void setOrder_info(ORDER_INFO order_info) {
        this.order_info = order_info;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getFormated_shipping_fee() {
        return formated_shipping_fee;
    }

    public void setFormated_shipping_fee(String formated_shipping_fee) {
        this.formated_shipping_fee = formated_shipping_fee;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public ArrayList<ORDER_GOODS_LIST> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(ArrayList<ORDER_GOODS_LIST> goods_list) {
        this.goods_list = goods_list;
    }

    public String getFormated_integral_money() {
        return formated_integral_money;
    }

    public void setFormated_integral_money(String formated_integral_money) {
        this.formated_integral_money = formated_integral_money;
    }

    public String getFormated_bonus() {
        return formated_bonus;
    }

    public void setFormated_bonus(String formated_bonus) {
        this.formated_bonus = formated_bonus;
    }

    public String getFormated_discount() {
        return formated_discount;
    }

    public void setFormated_discount(String formated_discount) {
        this.formated_discount = formated_discount;
    }

    public String getFormated_total_fee() {
        return formated_total_fee;
    }

    public void setFormated_total_fee(String formated_total_fee) {
        this.formated_total_fee = formated_total_fee;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public static GOODORDER fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        GOODORDER localItem = new GOODORDER();
        JSONArray subItemArray;
        localItem.order_time = jsonObject.optString("order_time");
        localItem.total_fee = jsonObject.optString("total_fee");
        subItemArray = jsonObject.optJSONArray("goods_list");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                ORDER_GOODS_LIST subItem = ORDER_GOODS_LIST.fromJson(subItemObject);
                localItem.goods_list.add(subItem);
            }
        }
        localItem.formated_integral_money = jsonObject.optString("formated_integral_money");
        localItem.formated_bonus = jsonObject.optString("formated_bonus");
        localItem.formated_discount = jsonObject.optString("formated_discount");//折扣
        localItem.formated_total_fee = jsonObject.optString("formated_total_fee");//格式化的总价
        localItem.order_sn = jsonObject.optString("order_sn");
        localItem.order_id = jsonObject.optString("order_id");
        localItem.goods_number = jsonObject.optString("goods_number");
        localItem.formated_shipping_fee = jsonObject.optString("formated_shipping_fee");
        localItem.order_info = ORDER_INFO.fromJson(jsonObject.optJSONObject("order_info"));

        localItem.order_status = jsonObject.optString("order_status");
        localItem.pay_status = jsonObject.optString("pay_status");
        localItem.label_order_status = jsonObject.optString("label_order_status");
        localItem.order_status_code = jsonObject.optString("order_status_code");
        localItem.toComment = jsonObject.optBoolean("toComment");
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("order_time", order_time);
        localItemObject.put("total_fee", total_fee);
        for (int i = 0; i < goods_list.size(); i++) {
            ORDER_GOODS_LIST itemData = goods_list.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("goods_list", itemJSONArray);
        localItemObject.put("formated_integral_money", formated_integral_money);
        localItemObject.put("formated_bonus", formated_bonus);
        localItemObject.put("order_sn", order_sn);
        localItemObject.put("order_id", order_id);
        localItemObject.put("goods_number", goods_number);
        localItemObject.put("formated_shipping_fee", formated_shipping_fee);
        localItemObject.put("formated_discount", formated_discount);
        localItemObject.put("formated_total_fee", formated_total_fee);

        localItemObject.put("order_status", order_status);
        localItemObject.put("pay_status", pay_status);
        localItemObject.put("label_order_status", label_order_status);
        localItemObject.put("order_status_code", order_status_code);
        localItemObject.put("toComment", toComment);

        return localItemObject;
    }

}
