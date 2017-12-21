
package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;


public class PAYMENT {

    private String is_cod;


    private String pay_code;


    private String pay_fee;


    private String pay_id;


    private String formated_pay_fee;


    private String pay_name;

    private String is_online;

    public String getIs_online() {
        return is_online;
    }

    public void setIs_online(String is_online) {
        this.is_online = is_online;
    }

    public String getIs_cod() {
        return is_cod;
    }

    public void setIs_cod(String is_cod) {
        this.is_cod = is_cod;
    }

    public String getPay_name() {
        return pay_name;
    }

    public void setPay_name(String pay_name) {
        this.pay_name = pay_name;
    }

    public String getPay_code() {
        return pay_code;
    }

    public void setPay_code(String pay_code) {
        this.pay_code = pay_code;
    }

    public String getPay_fee() {
        return pay_fee;
    }

    public void setPay_fee(String pay_fee) {
        this.pay_fee = pay_fee;
    }

    public String getPay_id() {
        return pay_id;
    }

    public void setPay_id(String pay_id) {
        this.pay_id = pay_id;
    }

    public String getFormated_pay_fee() {
        return formated_pay_fee;
    }

    public void setFormated_pay_fee(String formated_pay_fee) {
        this.formated_pay_fee = formated_pay_fee;
    }

    public static PAYMENT fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        PAYMENT localItem = new PAYMENT();
        localItem.is_cod = jsonObject.optString("is_cod");
        localItem.pay_code = jsonObject.optString("pay_code");
        localItem.pay_fee = jsonObject.optString("pay_fee");
        localItem.pay_id = jsonObject.optString("pay_id");
        localItem.formated_pay_fee = jsonObject.optString("formated_pay_fee");
        localItem.pay_name = jsonObject.optString("pay_name");
        localItem.is_online = jsonObject.optString("is_online");
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("is_cod", is_cod);
        localItemObject.put("pay_code", pay_code);
        localItemObject.put("pay_fee", pay_fee);
        localItemObject.put("pay_id", pay_id);
        localItemObject.put("formated_pay_fee", formated_pay_fee);
        localItemObject.put("pay_name", pay_name);
        localItemObject.put("is_online", is_online);
        return localItemObject;
    }

}
