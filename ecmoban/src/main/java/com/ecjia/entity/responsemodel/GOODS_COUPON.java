package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Adam on 2016-03-17.
 */
public class GOODS_COUPON implements Serializable {

    public String getBonus_type_id() {
        return bonus_type_id;
    }

    public void setBonus_type_id(String bonus_type_id) {
        this.bonus_type_id = bonus_type_id;
    }

    public String getBonus_name() {
        return bonus_name;
    }

    public void setBonus_name(String bonus_name) {
        this.bonus_name = bonus_name;
    }

    public String getBonus_amount() {
        return bonus_amount;
    }

    public void setBonus_amount(String bonus_amount) {
        this.bonus_amount = bonus_amount;
    }

    public String getFormatted_bonus_amount() {
        return formatted_bonus_amount;
    }

    public void setFormatted_bonus_amount(String formatted_bonus_amount) {
        this.formatted_bonus_amount = formatted_bonus_amount;
    }

    public String getRequest_amount() {
        return request_amount;
    }

    public void setRequest_amount(String request_amount) {
        this.request_amount = request_amount;
    }

    public String getFormatted_request_amount() {
        return formatted_request_amount;
    }

    public void setFormatted_request_amount(String formatted_request_amount) {
        this.formatted_request_amount = formatted_request_amount;
    }

    public String getFormatted_start_date() {
        return formatted_start_date;
    }

    public void setFormatted_start_date(String formatted_start_date) {
        this.formatted_start_date = formatted_start_date;
    }

    public String getFormatted_end_date() {
        return formatted_end_date;
    }

    public void setFormatted_end_date(String formatted_end_date) {
        this.formatted_end_date = formatted_end_date;
    }

    private String bonus_type_id;
    private String bonus_name;
    private String bonus_amount;
    private String formatted_bonus_amount;
    private String request_amount;
    private String formatted_request_amount;
    private String formatted_start_date;
    private String formatted_end_date;

    public String getReceived_coupon() {
        return received_coupon;
    }

    public void setReceived_coupon(String received_coupon) {
        this.received_coupon = received_coupon;
    }

    private String received_coupon;

    public static GOODS_COUPON fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        GOODS_COUPON localItem = new GOODS_COUPON();
        localItem.bonus_type_id = jsonObject.optString("bonus_type_id");
        localItem.bonus_name = jsonObject.optString("bonus_name");
        localItem.bonus_amount = jsonObject.optString("bonus_amount");
        localItem.formatted_bonus_amount = jsonObject.optString("formatted_bonus_amount");
        localItem.request_amount = jsonObject.optString("request_amount");
        localItem.formatted_request_amount = jsonObject.optString("formatted_request_amount");
        localItem.formatted_start_date = jsonObject.optString("formatted_start_date");
        localItem.formatted_end_date = jsonObject.optString("formatted_end_date");
        localItem.received_coupon = jsonObject.optString("received_coupon");

        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("bonus_id", bonus_type_id);
        localItemObject.put("bonus_name", bonus_name);
        localItemObject.put("bonus_amount", bonus_amount);
        localItemObject.put("formatted_bonus_amount", formatted_bonus_amount);
        localItemObject.put("request_amount", request_amount);
        localItemObject.put("formatted_request_amount", formatted_request_amount);
        localItemObject.put("formatted_start_date", formatted_start_date);
        localItemObject.put("formatted_end_date", formatted_end_date);
        localItemObject.put("received_coupon", received_coupon);
        return localItemObject;
    }

}
