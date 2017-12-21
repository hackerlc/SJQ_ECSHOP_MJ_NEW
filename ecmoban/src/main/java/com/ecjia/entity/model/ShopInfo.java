package com.ecjia.entity.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/2/28 15:05.
 */

public class ShopInfo {

    /**
     * shop_id : 1
     * shop_name : 商创网络科技有限公司
     * shop_desc : 电子商务平台搭建、电商
     * shop_logo : http://test.sjq.cn/seller_imgs/seller_logo/seller_logo1.png
     */
    private String id;
    private String shop_id;
    private String shop_name;
    private String shop_desc;
    private String shop_logo;
    private String cat_name;
    private String market_name;
    private String new_num;
    private String score;
    private String buyer;
    private String sale_num;
    @SerializedName("coupon_str")
    private String coupon_str;
    private int follow;


    private ArrayList<SimpleGoods> seller_goods;

    public String getId() {
        if(id!=null)
            return id;
        else
            return shop_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 因为没有统一返回值只能先这样用
     * @return
     */
    public String getShop_id() {
        if(id!=null)
            return id;
        else
            return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_desc() {
        return shop_desc;
    }

    public void setShop_desc(String shop_desc) {
        this.shop_desc = shop_desc;
    }

    public String getShop_logo() {
        return shop_logo;
    }

    public void setShop_logo(String shop_logo) {
        this.shop_logo = shop_logo;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getMarket_name() {
        return market_name;
    }

    public void setMarket_name(String market_name) {
        this.market_name = market_name;
    }

    public ArrayList<SimpleGoods> getSeller_goods() {
        if(seller_goods == null){
            seller_goods = new ArrayList<>();
        }
        return seller_goods;
    }

    public void setSeller_goods(ArrayList<SimpleGoods> seller_goods) {
        this.seller_goods = seller_goods;
    }

    public String getNew_num() {
        return new_num;
    }

    public void setNew_num(String new_num) {
        this.new_num = new_num;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getSale_num() {
        return sale_num;
    }

    public void setSale_num(String sale_num) {
        this.sale_num = sale_num;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public String getCoupon_str() {
        return coupon_str;
    }

    public void setCoupon_str(String coupon_str) {
        this.coupon_str = coupon_str;
    }

    public String showNewNum(){
        return "本月上新"+new_num+"款";
    }

    public String showOhter(){
        StringBuffer stringBuffer =new StringBuffer();
        return stringBuffer.append("综合分值：").append(score).append("\t\t\t")
                .append(buyer).append("位采购商\t\t\t")
                .append("共卖出").append(sale_num).append("件商品")
                .toString();
    }

    @Override
    public String toString() {
        return "ShopInfo{" +
                "id='" + id + '\'' +
                ", shop_id='" + shop_id + '\'' +
                ", shop_name='" + shop_name + '\'' +
                ", shop_desc='" + shop_desc + '\'' +
                ", shop_logo='" + shop_logo + '\'' +
                ", cat_name='" + cat_name + '\'' +
                ", market_name='" + market_name + '\'' +
                ", new_num='" + new_num + '\'' +
                ", score='" + score + '\'' +
                ", buyer='" + buyer + '\'' +
                ", sale_num='" + sale_num + '\'' +
                ", coupon_str='" + coupon_str + '\'' +
                ", follow=" + follow +
                ", seller_goods=" + seller_goods +
                '}';
    }
}
