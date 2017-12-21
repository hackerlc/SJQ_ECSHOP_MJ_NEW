package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-02-21.
 */

public class GOODSVOLUMEPRICE implements Serializable{

    /**
     * count : 10
     * price : 55
     * formated_price : <em>¥</em>55.00
     */

    private int count;
    private String price;
    private String formated_price;

    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFormated_price() {
        return formated_price;
    }

    public void setFormated_price(String formated_price) {
        this.formated_price = formated_price;
    }

    public static GOODSVOLUMEPRICE fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        GOODSVOLUMEPRICE localItem = new GOODSVOLUMEPRICE();
        localItem.count = jsonObject.optInt("count");
        localItem.price = jsonObject.optString("price");
        localItem.formated_price = jsonObject.optString("formated_price");
        localItem.number = jsonObject.optInt("number");
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("count", count);
        localItemObject.put("price", price);
        localItemObject.put("formated_price", formated_price);
        localItemObject.put("number", number);
        return localItemObject;
    }
}
