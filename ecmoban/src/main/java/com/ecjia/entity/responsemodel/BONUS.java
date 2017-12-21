package com.ecjia.entity.responsemodel;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class BONUS implements Serializable {

    private String bonus_id;

    private int type_id;

    private String type_name;

    private String type_money;

    private String bonus_money_formated;

    private String min_goods_amount;

    private String use_start_date;

    private String use_end_date;

    private String status;

    private String formated_use_start_date;

    private String formated_use_end_date;

    private String formated_min_goods_amount;

    private int bonus_status;

    private String formatted_bonus_status;

    private String formatted_use_start_date;

    private String formatted_use_end_date;

    private String bonus_name;

    private String bonus_amount;

    private String formatted_bonus_amount;

    private String start_date;

    private String end_date;

    private String formatted_start_date;

    private String formatted_end_date;

    private boolean ischecked=false;

    private String request_amount;

    private String formatted_request_amount;

    private String label_status;

    public int getBonus_status() {
        return bonus_status;
    }

    public void setBonus_status(int bonus_status) {
        this.bonus_status = bonus_status;
    }

    public String getFormatted_bonus_status() {
        return formatted_bonus_status;
    }

    public void setFormatted_bonus_status(String formatted_bonus_status) {
        this.formatted_bonus_status = formatted_bonus_status;
    }

    public String getFormatted_use_start_date() {
        return formatted_use_start_date;
    }

    public void setFormatted_use_start_date(String formatted_use_start_date) {
        this.formatted_use_start_date = formatted_use_start_date;
    }

    public String getFormatted_use_end_date() {
        return formatted_use_end_date;
    }

    public void setFormatted_use_end_date(String formatted_use_end_date) {
        this.formatted_use_end_date = formatted_use_end_date;
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

    public String getFormated_min_goods_amount() {
        return formated_min_goods_amount;
    }

    public void setFormated_min_goods_amount(String formated_min_goods_amount) {
        this.formated_min_goods_amount = formated_min_goods_amount;
    }

    public String getMin_goods_amount() {
        return min_goods_amount;
    }

    public void setMin_goods_amount(String min_goods_amount) {
        this.min_goods_amount = min_goods_amount;
    }

    public String getUse_start_date() {
        return use_start_date;
    }

    public void setUse_start_date(String use_start_date) {
        this.use_start_date = use_start_date;
    }

    public String getUse_end_date() {
        return use_end_date;
    }

    public void setUse_end_date(String use_end_date) {
        this.use_end_date = use_end_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFormated_use_start_date() {
        return formated_use_start_date;
    }

    public void setFormated_use_start_date(String formated_use_start_date) {
        this.formated_use_start_date = formated_use_start_date;
    }

    public String getFormated_use_end_date() {
        return formated_use_end_date;
    }

    public void setFormated_use_end_date(String formated_use_end_date) {
        this.formated_use_end_date = formated_use_end_date;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getType_money() {
        return type_money;
    }

    public void setType_money(String type_money) {
        this.type_money = type_money;
    }

    public String getBonus_id() {
        return bonus_id;
    }

    public void setBonus_id(String bonus_id) {
        this.bonus_id = bonus_id;
    }

    public String getBonus_money_formated() {
        return bonus_money_formated;
    }

    public void setBonus_money_formated(String bonus_money_formated) {
        this.bonus_money_formated = bonus_money_formated;
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

    public String getLabel_status() {
        return label_status;
    }

    public void setLabel_status(String label_status) {
        this.label_status = label_status;
    }

    public boolean isIschecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public static BONUS fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        BONUS localItem = new BONUS();

//        private int bonus_status;
//        private String formatted_bonus_status;
//        private String formatted_use_start_date;
//        private String formatted_use_end_date;
//        private String bonus_name;
//        private String bonus_amount;
//        private String formatted_bonus_amount;
//        private String start_date;
//        private String end_date;
//        private String formatted_start_date;
//        private String formatted_end_date;
        localItem.type_id = jsonObject.optInt("type_id");
        localItem.type_name = jsonObject.optString("type_name");
        localItem.type_money = jsonObject.optString("type_money");
        localItem.bonus_id = jsonObject.optString("bonus_id");
        localItem.use_start_date = jsonObject.optString("use_start_date");
        localItem.use_end_date = jsonObject.optString("use_end_date");
        localItem.min_goods_amount = jsonObject.optString("min_goods_amount");
        localItem.bonus_money_formated = jsonObject.optString("bonus_money_formated");
        localItem.bonus_name = jsonObject.optString("bonus_name");
        localItem.bonus_amount = jsonObject.optString("bonus_amount");
        localItem.formatted_bonus_amount = jsonObject.optString("formatted_bonus_amount");
        localItem.request_amount=jsonObject.optString("request_amount");
        localItem.formatted_request_amount=jsonObject.optString("formatted_request_amount");
        localItem.bonus_status=jsonObject.optInt("bonus_status");
        localItem.formatted_bonus_status = jsonObject.optString("formatted_bonus_status");
        localItem.start_date = jsonObject.optString("start_date");
        localItem.end_date = jsonObject.optString("end_date");
        localItem.formated_use_start_date = jsonObject.optString("formated_use_start_date");
        localItem.formated_use_end_date = jsonObject.optString("formated_use_end_date");


        localItem.status = jsonObject.optString("status");
        localItem.formated_min_goods_amount = jsonObject.optString("formated_min_goods_amount");
        localItem.formatted_use_start_date = jsonObject.optString("formatted_use_start_date");
        localItem.formatted_use_end_date = jsonObject.optString("formatted_use_end_date");
        localItem.formatted_start_date = jsonObject.optString("formatted_start_date");
        localItem.formatted_end_date = jsonObject.optString("formatted_end_date");
        localItem.label_status = jsonObject.optString("label_status");
        localItem.ischecked=false;
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("type_id", type_id);
        localItemObject.put("type_name", type_name);
        localItemObject.put("type_money", type_money);
        localItemObject.put("bonus_id", bonus_id);
        localItemObject.put("bonus_money_formated", bonus_money_formated);

        localItemObject.put("min_goods_amount", min_goods_amount);
        localItemObject.put("use_start_date", use_start_date);
        localItemObject.put("use_end_date", use_end_date);
        localItemObject.put("status", status);
        localItemObject.put("formated_use_start_date", formated_use_start_date);
        localItemObject.put("formated_use_end_date", formated_use_end_date);
        localItemObject.put("formated_min_goods_amount", formated_min_goods_amount);


        localItemObject.put("bonus_status",bonus_status);
        localItemObject.put("formatted_bonus_status",formatted_bonus_status);
        localItemObject.put("formatted_use_start_date",formatted_use_start_date);
        localItemObject.put("formatted_use_end_date",formatted_use_end_date);
        localItemObject.put("bonus_name",bonus_name);
        localItemObject.put("bonus_amount",bonus_amount);
        localItemObject.put("formatted_bonus_amount",formatted_bonus_amount);
        localItemObject.put("start_date",start_date);
        localItemObject.put("end_date",end_date);
        localItemObject.put("formatted_start_date",formatted_start_date);
        localItemObject.put("formatted_end_date",formatted_end_date);
        localItemObject.put("ischecked",ischecked);
        localItemObject.put("formatted_request_amount",formatted_request_amount);
        localItemObject.put("request_amount",request_amount);
        localItemObject.put("label_status",label_status);
        return localItemObject;
    }


}
