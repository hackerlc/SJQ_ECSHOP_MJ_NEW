package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lm on 2015/12/01.
 * 手机专享商品
 */
public class MOBILEGOODS {
    private String id;
    private String name;
    private String market_price;
    private String shop_price;
    private String promote_price;
    private String brief;
    private PHOTO img;
    private String groupbuy_id;
    private String object_id;
    private String rec_type;
    private String promote_start_date;
    private String promote_end_date;
    private String raminTime;
    private String activity_type;
    private String formatted_saving_price; //已省xx元
    private int saving_price;

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public int getSaving_price() {
        return saving_price;
    }

    public void setSaving_price(int saving_price) {
        this.saving_price = saving_price;
    }

    public String getFormatted_saving_price() {
        return formatted_saving_price;
    }

    public void setFormatted_saving_price(String formatted_saving_price) {
        this.formatted_saving_price = formatted_saving_price;
    }

    public String getRaminTime() {
        return raminTime;
    }

    public void setRaminTime(String raminTime) {
        this.raminTime = raminTime;
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

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getRec_type() {
        return rec_type;
    }

    public void setRec_type(String rec_type) {
        this.rec_type = rec_type;
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

    public String getPromote_price() {
        return promote_price;
    }

    public void setPromote_price(String promote_price) {
        this.promote_price = promote_price;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public PHOTO getImg() {
        return img;
    }

    public void setImg(PHOTO img) {
        this.img = img;
    }

    public String getGroupbuy_id() {
        return groupbuy_id;
    }

    public void setGroupbuy_id(String groupbuy_id) {
        this.groupbuy_id = groupbuy_id;
    }

    public static MOBILEGOODS fromJson(JSONObject jsonObject)  throws JSONException {
        MOBILEGOODS mobilegoods=new MOBILEGOODS();
        mobilegoods.id=jsonObject.optString("id");
        mobilegoods.name=jsonObject.optString("name");
        mobilegoods.market_price=jsonObject.optString("market_price");
        mobilegoods.shop_price=jsonObject.optString("shop_price");
        mobilegoods.promote_price=jsonObject.optString("promote_price");
        mobilegoods.brief=jsonObject.optString("brief");
        mobilegoods.img=PHOTO.fromJson(jsonObject.optJSONObject("img"));
        mobilegoods.groupbuy_id=jsonObject.optString("groupbuy_id");
        mobilegoods.object_id=jsonObject.optString("object_id");
        mobilegoods.rec_type=jsonObject.optString("rec_type");
        mobilegoods.promote_start_date = jsonObject.optString("promote_start_date");
        mobilegoods.promote_end_date = jsonObject.optString("promote_end_date");
        mobilegoods.raminTime = jsonObject.optString("raminTime");
        mobilegoods.activity_type = jsonObject.optString("activity_type");
        mobilegoods.saving_price = jsonObject.optInt("saving_price");
        mobilegoods.formatted_saving_price = jsonObject.optString("formatted_saving_price");

        return mobilegoods;
    }

}
