package com.ecjia.view.activity.goodsorder.balance.entity;

import com.ecjia.entity.responsemodel.SHIPPING;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-04-06.
 */

public class NewBlanceShopData implements Serializable {

    private String shop_id;//店铺ID
    private String shop_name;//店铺名称
    private String goods_total;//店铺下所有商品的总数
    private String shop_money;//店铺总金额
    private String feedback;//意见
    private ShopFavourable shop_favourable;//店铺优 惠活动

    private List<ShopCouponsData> coupons;// 优惠券

    private List<SHIPPING> shipping_list;

    private ArrayList<NewBlanceShopGoodsData> goods_list;

    private float shop_shipping_fee;//运费

    private float coupons_money = 0;//商品优惠金额

    private String use_shipping_id;

    private String use_coupons_id;


    public float getShop_shipping_fee() {
        setUse_shipping_id("");
        for (int i = 0; i < shipping_list.size(); i++) {
            if ("1".equals(shipping_list.get(i).getIs_use())) {//选中快递方式
                shop_shipping_fee = Float.parseFloat(shipping_list.get(i).getShipping_fee());
                setUse_shipping_id(shipping_list.get(i).getShipping_id());
            }
        }
        return shop_shipping_fee;
    }

    public void setShop_shipping_fee(float shop_shipping_fee) {
        this.shop_shipping_fee = shop_shipping_fee;
    }

    public float getCoupons_money() {
        setUse_coupons_id("");
        coupons_money=0;
        for (int i = 0; i < coupons.size(); i++) {
            if (coupons.get(i).isSelect()) {
                coupons_money = Float.parseFloat(coupons.get(i).getCou_money());
                setUse_coupons_id(coupons.get(i).getCou_id());
            }
        }
        return coupons_money;
    }

    public void setCoupons_money(float coupons_money) {
        this.coupons_money = coupons_money;
    }

    public String getShop_id() {
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

    public String getGoods_total() {
        return goods_total;
    }

    public void setGoods_total(String goods_total) {
        this.goods_total = goods_total;
    }

    public float getShop_AllGoodsMoney() {
        return Float.parseFloat(shop_money) - getCoupons_money();
    }

    public String getShop_money() {
        return shop_money;
    }

    public void setShop_money(String shop_money) {
        this.shop_money = shop_money;
    }


    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public ShopFavourable getShop_favourable() {
        return shop_favourable;
    }

    public void setShop_favourable(ShopFavourable shop_favourable) {
        this.shop_favourable = shop_favourable;
    }

    public List<ShopCouponsData> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<ShopCouponsData> coupons) {
        this.coupons = coupons;
    }

    public List<SHIPPING> getShipping_list() {
        return shipping_list;
    }

    public void setShipping_list(List<SHIPPING> shipping_list) {
        this.shipping_list = shipping_list;
    }

    public ArrayList<NewBlanceShopGoodsData> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(ArrayList<NewBlanceShopGoodsData> goods_list) {
        this.goods_list = goods_list;
    }

    public String getUse_shipping_id() {
        return use_shipping_id;
    }

    public void setUse_shipping_id(String use_shipping_id) {
        this.use_shipping_id = use_shipping_id;
    }

    public String getUse_coupons_id() {
        return use_coupons_id;
    }

    public void setUse_coupons_id(String use_coupons_id) {
        this.use_coupons_id = use_coupons_id;
    }
}
