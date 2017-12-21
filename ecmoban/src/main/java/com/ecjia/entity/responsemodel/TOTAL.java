
package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;


public class TOTAL {

    private String goods_price;

    private int virtual_goods_count;

    private String market_price;

    private int real_goods_count;

    private String save_rate;

    private String saving;

    private String goods_amount;

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getGoods_amount() {
        return goods_amount;
    }

    public void setGoods_amount(String goods_amount) {
        this.goods_amount = goods_amount;
    }

    public int getVirtual_goods_count() {
        return virtual_goods_count;
    }

    public void setVirtual_goods_count(int virtual_goods_count) {
        this.virtual_goods_count = virtual_goods_count;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public int getReal_goods_count() {
        return real_goods_count;
    }

    public void setReal_goods_count(int real_goods_count) {
        this.real_goods_count = real_goods_count;
    }

    public String getSave_rate() {
        return save_rate;
    }

    public void setSave_rate(String save_rate) {
        this.save_rate = save_rate;
    }

    public String getSaving() {
        return saving;
    }

    public void setSaving(String saving) {
        this.saving = saving;
    }

    public static TOTAL fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        TOTAL localItem = new TOTAL();
        localItem.goods_price = jsonObject.optString("goods_price");
        localItem.virtual_goods_count = jsonObject.optInt("virtual_goods_count");
        localItem.market_price = jsonObject.optString("market_price");
        localItem.real_goods_count = jsonObject.optInt("real_goods_count");
        localItem.save_rate = jsonObject.optString("save_rate");
        localItem.saving = jsonObject.optString("saving");
        localItem.goods_amount = jsonObject.optString("goods_amount");
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("goods_price", goods_price);
        localItemObject.put("virtual_goods_count", virtual_goods_count);
        localItemObject.put("market_price", market_price);
        localItemObject.put("real_goods_count", real_goods_count);
        localItemObject.put("save_rate", save_rate);
        localItemObject.put("saving", saving);
        localItemObject.put("goods_amount", goods_amount);
        return localItemObject;
    }

}
