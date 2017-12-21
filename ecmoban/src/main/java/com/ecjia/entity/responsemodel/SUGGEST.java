package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/9/18.
 */
public class SUGGEST {
    private String goods_id;
    private String name;
    private String goods_name;
    private String market_price;
    private String format_market_price;
    private String shop_price;
    private String format_shop_price;
    private String promote_price;
    private String brief;
    private String promote_start_date;
    private String promote_end_date;
    private PHOTO img;
    private PROMOTETIME promotetime;

    public PROMOTETIME getPromotetime() {
        return promotetime;
    }

    public void setPromotetime(PROMOTETIME promotetime) {
        this.promotetime = promotetime;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getFormat_market_price() {
        return format_market_price;
    }

    public void setFormat_market_price(String format_market_price) {
        this.format_market_price = format_market_price;
    }

    public String getShop_price() {
        return shop_price;
    }

    public void setShop_price(String shop_price) {
        this.shop_price = shop_price;
    }

    public String getFormat_shop_price() {
        return format_shop_price;
    }

    public void setFormat_shop_price(String format_shop_price) {
        this.format_shop_price = format_shop_price;
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

    public PHOTO getPhoto() {
        return img;
    }

    public void setPhoto(PHOTO photo) {
        this.img = photo;
    }

    public static SUGGEST fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        SUGGEST localItem = new SUGGEST();
        localItem.goods_id = jsonObject.optString("goods_id");
        localItem.name = jsonObject.optString("name");
        localItem.goods_name = jsonObject.optString("goods_name");
        localItem.market_price = jsonObject.optString("market_price");
        localItem.format_market_price = jsonObject.optString("format_market_price");
        localItem.shop_price = jsonObject.optString("shop_price");
        localItem.format_shop_price = jsonObject.optString("format_shop_price");
        localItem.promote_price = jsonObject.optString("promote_price");
        localItem.brief = jsonObject.optString("brief");
        localItem.promote_start_date = jsonObject.optString("promote_start_date");
        localItem.promote_end_date = jsonObject.optString("promote_end_date");
        localItem.img = PHOTO.fromJson(jsonObject.optJSONObject("img"));
        return localItem;
    }
}
