package com.hyphenate.easeui.model;

import java.io.Serializable;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/4/1 11:00.
 */

public class Goods implements Serializable{
    String id;
    String name;
    String img;
    String price;
    String hxString;
    String hxId;
    String hxName;

    public Goods() {
    }

    public Goods(String id, String name, String img, String price, String hxString, String hxId, String hxName) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.price = price;
        this.hxString = hxString;
        this.hxId = hxId;
        this.hxName = hxName;
    }

    public String getHxId() {
        return hxId;
    }

    public void setHxId(String hxId) {
        this.hxId = hxId;
    }

    public String getHxName() {
        return hxName;
    }

    public void setHxName(String hxName) {
        this.hxName = hxName;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getHxString() {
        return hxString;
    }

    public void setHxString(String hxString) {
        this.hxString = hxString;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", price='" + price + '\'' +
                ", hxString='" + hxString + '\'' +
                ", hxId='" + hxId + '\'' +
                ", hxName='" + hxName + '\'' +
                '}';
    }
}
