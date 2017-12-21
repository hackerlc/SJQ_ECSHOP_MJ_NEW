
package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SIMPLEGOODS {

    private int id;
    private String shop_price;
    private String market_price;
    private String name;
    private int goods_id;
    private PHOTO img;
    private String brief;
    private String promote_price;
    private String formatted_saving_price; //已省xx元
    private double saving_price;
    private String activity_type;

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public String getFormatted_saving_price() {
        return formatted_saving_price;
    }

    public void setFormatted_saving_price(String formatted_saving_price) {
        this.formatted_saving_price = formatted_saving_price;
    }

    public double getSaving_price() {
        return saving_price;
    }

    public void setSaving_price(double saving_price) {
        this.saving_price = saving_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPromote_price() {
        return promote_price;
    }

    public void setPromote_price(String promote_price) {
        this.promote_price = promote_price;
    }

    public String getShop_price() {
        return shop_price;
    }

    public void setShop_price(String shop_price) {
        this.shop_price = shop_price;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public PHOTO getImg() {
        return img;
    }

    public void setImg(PHOTO img) {
        this.img = img;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public static SIMPLEGOODS fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        SIMPLEGOODS localItem = new SIMPLEGOODS();

        JSONArray subItemArray;

        localItem.id = jsonObject.optInt("id");

        localItem.shop_price = jsonObject.optString("shop_price");

        localItem.market_price = jsonObject.optString("market_price");

        localItem.name = jsonObject.optString("name");

        localItem.goods_id = jsonObject.optInt("goods_id");
        localItem.img = PHOTO.fromJson(jsonObject.optJSONObject("img"));

        localItem.brief = jsonObject.optString("brief");

        localItem.promote_price = jsonObject.optString("promote_price");
        localItem.activity_type = jsonObject.optString("activity_type");
        localItem.saving_price = jsonObject.optDouble("saving_price");
        localItem.formatted_saving_price = jsonObject.optString("formatted_saving_price");
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("id", id);
        localItemObject.put("shop_price", shop_price);
        localItemObject.put("market_price", market_price);
        localItemObject.put("name", name);
        localItemObject.put("goods_id", goods_id);
        if (null != img) {
            localItemObject.put("img", img.toJson());
        }
        localItemObject.put("brief", brief);
        localItemObject.put("promote_price", promote_price);
        localItemObject.put("activity_type", activity_type);
        localItemObject.put("saving_price", saving_price);
        localItemObject.put("formatted_saving_price", formatted_saving_price);
        return localItemObject;
    }

}
