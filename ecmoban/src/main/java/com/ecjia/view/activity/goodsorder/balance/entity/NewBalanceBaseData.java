package com.ecjia.view.activity.goodsorder.balance.entity;

import com.ecjia.entity.responsemodel.ADDRESS;
import com.ecjia.entity.responsemodel.BONUS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-04-06.
 */

public class NewBalanceBaseData implements Serializable {

    private ADDRESS consignee;

    private ArrayList<NewBlanceShopData> shop_list;//店铺列表

    private String total_goods_money;//总商品金额

    private String total_shipping_fee;//总运费

    private String total_order_money;//总金额

    private String allow_use_bonus;//使用的红包

    private List<BONUS> bonus;//红包列表

    private String total_goods_num;//总的数量

    private String allow_use_bonus_id;//使用的红包

    private NewBalancePayInfo pay_info;

    public NewBalancePayInfo getPay_info() {
        return pay_info;
    }

    public void setPay_info(NewBalancePayInfo pay_info) {
        this.pay_info = pay_info;
    }

    public String getTotal_goods_num() {
        return total_goods_num;
    }

    public void setTotal_goods_num(String total_goods_num) {
        this.total_goods_num = total_goods_num;
    }

    public ADDRESS getConsignee() {
        return consignee;
    }

    public void setConsignee(ADDRESS consignee) {
        this.consignee = consignee;
    }

    public ArrayList<NewBlanceShopData> getShop_list() {
        return shop_list;
    }

    public void setShop_list(ArrayList<NewBlanceShopData> shop_list) {
        this.shop_list = shop_list;
    }

    public String getTotal_goods_money() {
        return total_goods_money;
    }

    public void setTotal_goods_money(String total_goods_money) {
        this.total_goods_money = total_goods_money;
    }

    public String getTotal_shipping_fee() {
        return total_shipping_fee;
    }

    public void setTotal_shipping_fee(String total_shipping_fee) {
        this.total_shipping_fee = total_shipping_fee;
    }

    public String getTotal_order_money() {
        return total_order_money;
    }

    public void setTotal_order_money(String total_order_money) {
        this.total_order_money = total_order_money;
    }

    public String getAllow_use_bonus() {
        return allow_use_bonus;
    }

    public void setAllow_use_bonus(String allow_use_bonus) {
        this.allow_use_bonus = allow_use_bonus;
    }

    public String getAllow_use_bonus_id() {
        return allow_use_bonus_id;
    }

    public void setAllow_use_bonus_id(String allow_use_bonus_id) {
        this.allow_use_bonus_id = allow_use_bonus_id;
    }

    public List<BONUS> getBonus() {
        return bonus;
    }

    public void setBonus(List<BONUS> bonus) {
        this.bonus = bonus;
    }

    public Float getShowAllFee() {
        float fee = 0;
        for (NewBlanceShopData shop : shop_list) {
            fee = fee + shop.getShop_shipping_fee();
        }
        return fee;
    }

    public Float getShowAllGoodsMoney() {
        float goodsMoney = 0;
        for (NewBlanceShopData shop : shop_list) {
            goodsMoney = goodsMoney + shop.getShop_AllGoodsMoney();
        }
        return goodsMoney;
    }

    public  Float getUseBonus(){
        float bonusMoney = 0;
        for (BONUS bo : bonus) {
            if(bo.isIschecked()){
                bonusMoney=Float.parseFloat(bo.getType_money());
                setAllow_use_bonus_id(bo.getBonus_id()+"");
            }
        }
        return bonusMoney;
    }

    public Float getShowAllMoney() {
        return getShowAllFee() + getShowAllGoodsMoney()-getUseBonus();
    }
}
