package com.ecjia.entity.responsemodel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PRICE_RANGE
{


    private int price_min;

    private int price_max;

    public int getPrice_min() {
        return price_min;
    }

    public void setPrice_min(int price_min) {
        this.price_min = price_min;
    }

    public int getPrice_max() {
        return price_max;
    }

    public void setPrice_max(int price_max) {
        this.price_max = price_max;
    }

    public static PRICE_RANGE fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        PRICE_RANGE   localItem = new PRICE_RANGE();

        JSONArray subItemArray;

        localItem.price_min = jsonObject.optInt("price_min");

        localItem.price_max = jsonObject.optInt("price_max");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("price_min", price_min);
        localItemObject.put("price_max", price_max);
        return localItemObject;
    }
}