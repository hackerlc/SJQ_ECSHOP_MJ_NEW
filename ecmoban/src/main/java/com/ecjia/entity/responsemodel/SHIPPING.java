
package com.ecjia.entity.responsemodel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class SHIPPING implements Serializable
{


    private String support_cod;


    private String shipping_desc;


    private String shipping_id;


    private String format_shipping_fee;


    private String insure;


    private String insure_formated;


    private String shipping_code;


    private String shipping_name;


    private String free_money;


    private String shipping_fee;

    private String is_use;

    private String cac_name;

    public String getCac_name() {
        if(cac_name==null){
            cac_name="";
        }
        return cac_name;
    }

    public void setCac_name(String cac_name) {
        this.cac_name = cac_name;
    }

    public String getIs_use() {
        return is_use;
    }

    public void setIs_use(String is_use) {
        this.is_use = is_use;
    }

    public String getSupport_cod() {
        return support_cod;
    }

    public void setSupport_cod(String support_cod) {
        this.support_cod = support_cod;
    }

    public String getShipping_fee() {
        return shipping_fee;
    }

    public String getShipping_feeString() {
        if("0".equals(shipping_fee)||"免运费".equals(shipping_fee)||shipping_fee==null){
            return "免运费";
        }else {
            return shipping_fee;
        }
    }

    public void setShipping_fee(String shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public String getShipping_desc() {
        return shipping_desc;
    }

    public void setShipping_desc(String shipping_desc) {
        this.shipping_desc = shipping_desc;
    }

    public String getShipping_id() {
        return shipping_id;
    }

    public void setShipping_id(String shipping_id) {
        this.shipping_id = shipping_id;
    }

    public String getFormat_shipping_fee() {
        return format_shipping_fee;
    }

    public void setFormat_shipping_fee(String format_shipping_fee) {
        this.format_shipping_fee = format_shipping_fee;
    }

    public String getInsure() {
        return insure;
    }

    public void setInsure(String insure) {
        this.insure = insure;
    }

    public String getInsure_formated() {
        return insure_formated;
    }

    public void setInsure_formated(String insure_formated) {
        this.insure_formated = insure_formated;
    }

    public String getShipping_code() {
        return shipping_code;
    }

    public void setShipping_code(String shipping_code) {
        this.shipping_code = shipping_code;
    }

    public String getShipping_name() {
        return shipping_name;
    }

    public void setShipping_name(String shipping_name) {
        this.shipping_name = shipping_name;
    }

    public String getFree_money() {
        return free_money;
    }

    public void setFree_money(String free_money) {
        this.free_money = free_money;
    }

    public static SHIPPING fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        SHIPPING   localItem = new SHIPPING();

        JSONArray subItemArray;

        localItem.support_cod = jsonObject.optString("support_cod");

        localItem.shipping_desc = jsonObject.optString("shipping_desc");

        localItem.shipping_id = jsonObject.optString("shipping_id");

        localItem.format_shipping_fee = jsonObject.optString("format_shipping_fee");

        localItem.insure = jsonObject.optString("insure");

        localItem.insure_formated = jsonObject.optString("insure_formated");

        localItem.shipping_code = jsonObject.optString("shipping_code");

        localItem.shipping_name = jsonObject.optString("shipping_name");

        localItem.free_money = jsonObject.optString("free_money");

        localItem.shipping_fee = jsonObject.optString("shipping_fee");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("support_cod", support_cod);
        localItemObject.put("shipping_desc", shipping_desc);
        localItemObject.put("shipping_id", shipping_id);
        localItemObject.put("format_shipping_fee", format_shipping_fee);
        localItemObject.put("insure", insure);
        localItemObject.put("insure_formated", insure_formated);
        localItemObject.put("shipping_code", shipping_code);
        localItemObject.put("shipping_name", shipping_name);
        localItemObject.put("free_money", free_money);
        localItemObject.put("shipping_fee", shipping_fee);
        return localItemObject;
    }

}
