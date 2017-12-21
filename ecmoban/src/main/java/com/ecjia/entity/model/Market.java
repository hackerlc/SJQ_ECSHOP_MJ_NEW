package com.ecjia.entity.model;

import java.util.List;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/2/28 14:58.
 */

public class Market {

    /**
     * id : 28
     * name : 四季青老市场
     * image : 1488132801508635706.png
     * shop_num : 21
     */

    private String id;
    private String name;
    private String image;
    private String shop_num;
    private String goods_num;
    private String desc;
    private List<ShopInfo> shop;

    public List<ShopInfo> getShop() {
        return shop;
    }

    public void setShop(List<ShopInfo> shop) {
        this.shop = shop;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShop_num() {
        return shop_num;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getShowShopNum(){
        return "入驻商家："+shop_num;
    }

    public void setShop_num(String shop_num) {
        this.shop_num = shop_num;
    }
}
