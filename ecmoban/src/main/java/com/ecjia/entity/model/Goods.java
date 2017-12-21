
package com.ecjia.entity.model;

import com.ecjia.entity.responsemodel.PHOTO;


public class Goods{

    protected String id;
    protected String shop_price;
    protected String market_price;
    protected String name;
    protected String goods_id;
    protected PHOTO img;
    protected String brief;
    protected String promote_price;
    protected String formatted_saving_price; //已省xx元
    protected float saving_price;
    protected String activity_type;
    protected String object_id;
    protected String rec_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public void setSaving_price(float saving_price) {
        this.saving_price = saving_price;
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

    public String getPromote_price() {
        return promote_price;
    }

    public void setPromote_price(String promote_price) {
        this.promote_price = promote_price;
    }

    public String getFormatted_saving_price() {
        return formatted_saving_price;
    }

    public void setFormatted_saving_price(String formatted_saving_price) {
        this.formatted_saving_price = formatted_saving_price;
    }

    public float getSaving_price() {
        return saving_price;
    }

    public void setSaving_price(int saving_price) {
        this.saving_price = saving_price;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
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

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", shop_price='" + shop_price + '\'' +
                ", market_price='" + market_price + '\'' +
                ", name='" + name + '\'' +
                ", goods_id=" + goods_id +
                ", img=" + img +
                ", brief='" + brief + '\'' +
                ", promote_price='" + promote_price + '\'' +
                ", formatted_saving_price='" + formatted_saving_price + '\'' +
                ", saving_price=" + saving_price +
                ", activity_type='" + activity_type + '\'' +
                '}';
    }
}
