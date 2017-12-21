package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/22.
 */
public class SELLERGOODS {
    private String id;
    private String name;
    private String market_price;
    private String shop_price;
    private String promote_price;
    private String promote_start_date;
    private String promote_end_date;
    private String brief;

    private PHOTO img;

    private String activity_type;
    private String formatted_saving_price; //已省xx元
    private int saving_price;
    private int object_id;

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

    public int getSaving_price() {
        return saving_price;
    }

    public void setSaving_price(int saving_price) {
        this.saving_price = saving_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getShop_price() {
        return shop_price;
    }

    public void setShop_price(String shop_price) {
        this.shop_price = shop_price;
    }

    public PHOTO getImg() {
        return img;
    }

    public void setImg(PHOTO img) {
        this.img = img;
    }

    public int getObject_id() {
        return object_id;
    }

    public void setObject_id(int object_id) {
        this.object_id = object_id;
    }

    public String getPromote_price() {
        return promote_price;
    }

    public void setPromote_price(String promote_price) {
        this.promote_price = promote_price;
    }

    public String getPromote_start_date() {
        return promote_start_date;
    }

    public void setPromote_start_date(String promote_start_date) {
        this.promote_start_date = promote_start_date;
    }

    public String getPromote_end_date() {
        return promote_end_date;
    }

    public void setPromote_end_date(String promote_end_date) {
        this.promote_end_date = promote_end_date;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public static SELLERGOODS fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        SELLERGOODS localItem = new SELLERGOODS();

        localItem.id = jsonObject.optString("id");
        localItem.name = jsonObject.optString("name");
        localItem.market_price=jsonObject.optString("market_price");
        localItem.shop_price=jsonObject.optString("shop_price");
        localItem.promote_price=jsonObject.optString("promote_price");
        localItem.promote_start_date=jsonObject.optString("promote_start_date");
        localItem.promote_end_date=jsonObject.optString("promote_end_date");
        localItem.brief=jsonObject.optString("brief");

        localItem.img=PHOTO.fromJson(jsonObject.optJSONObject("img"));
        localItem.activity_type = jsonObject.optString("activity_type");
        localItem.saving_price = jsonObject.optInt("saving_price");
        localItem.formatted_saving_price = jsonObject.optString("formatted_saving_price");
//        localItem.object_id=jsonObject.getInt("object_id");
        return localItem;
    }
}
