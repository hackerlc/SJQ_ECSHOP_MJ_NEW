package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 类名介绍：团批商品阶梯价
 * Created by sun
 * Created time 2017-02-21.
 */

public class GOODSPRICELADDER implements Serializable{

    /**
     * amount : 1
     * price : 55
     * formated_price : <em>¥</em>55.00
     */

    private int amount;
    private double price;
    private String formated_price;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getFormated_price() {
        return formated_price;
    }

    public void setFormated_price(String formated_price) {
        this.formated_price = formated_price;
    }


    public static GOODSPRICELADDER fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        GOODSPRICELADDER localItem = new GOODSPRICELADDER();
        localItem.amount = jsonObject.optInt("amount");
        localItem.price = jsonObject.optDouble("price");
        localItem.formated_price = jsonObject.optString("formated_price");
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("amount", amount);
        localItemObject.put("price", price);
        localItemObject.put("formated_price", formated_price);
        return localItemObject;
    }
}
