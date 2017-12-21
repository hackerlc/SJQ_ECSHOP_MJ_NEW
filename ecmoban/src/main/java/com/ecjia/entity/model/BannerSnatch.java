package com.ecjia.entity.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/3/2 15:14.
 */

public class BannerSnatch {
    protected String banner;

    //market
    protected String market_num;
    protected String shop_num;
    protected String desc;
    protected long left_scond;

    @SerializedName("goods")
    protected List<SnatchGoods> snatchGoods;

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getMarket_num() {
        return market_num;
    }

    public void setMarket_num(String market_num) {
        this.market_num = market_num;
    }

    public String getShop_num() {
        return shop_num;
    }

    public void setShop_num(String shop_num) {
        this.shop_num = shop_num;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getLeft_scond() {
        return left_scond;
    }

    public void setLeft_scond(long left_scond) {
        this.left_scond = left_scond;
    }

    public List<SnatchGoods> getSnatchGoods() {
        return snatchGoods;
    }

    public void setSnatchGoods(List<SnatchGoods> snatchGoods) {
        this.snatchGoods = snatchGoods;
    }

    public void lessLeftSecond(){
        this.left_scond--;
    }
}
