package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/11/26.
 * 添加红包信息
 */
public class BONUSINFO {
    private String bonus_id;
    private String bonus_name;
    private String bonus_amount;
    private String formatted_bonus_amout;
    private String request_amount;
    private String formatted_request_amount;
    private String start_date;
    private String end_date;
    private String formatted_start_date;
    private String formatted_end_date;

    public String getBonus_id() {
        return bonus_id;
    }

    public void setBonus_id(String bonus_id) {
        this.bonus_id = bonus_id;
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

    public String getFormatted_bonus_amout() {
        return formatted_bonus_amout;
    }

    public void setFormatted_bonus_amout(String formatted_bonus_amout) {
        this.formatted_bonus_amout = formatted_bonus_amout;
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

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
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

    public static BONUSINFO fromJson(JSONObject jsonObject) throws JSONException {

        if (null == jsonObject) {

            return null;

        }

        BONUSINFO localItem = new BONUSINFO();


        localItem.bonus_id = jsonObject.optString("bonus_id");
        localItem.bonus_name = jsonObject.optString("bonus_name");
        localItem.bonus_amount = jsonObject.optString("bonus_amount");
        localItem.formatted_bonus_amout = jsonObject.optString("formatted_bonus_amount");
        localItem.request_amount = jsonObject.optString("request_amount");
        localItem.formatted_request_amount = jsonObject.optString("formatted_request_amount");
        localItem.start_date = jsonObject.optString("start_date");
        localItem.end_date = jsonObject.optString("end_date");
        localItem.formatted_start_date = jsonObject.optString("formatted_start_date");
        localItem.formatted_end_date = jsonObject.optString("formatted_end_date");

        return localItem;
    }
}
