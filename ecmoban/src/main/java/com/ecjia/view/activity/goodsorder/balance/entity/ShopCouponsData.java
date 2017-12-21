package com.ecjia.view.activity.goodsorder.balance.entity;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-04-06.
 */

public class ShopCouponsData {//店铺优惠券
    /**
     * cou_id : 9
     * cou_name : 全场赠券
     * cou_money : 10
     */

    private String cou_id;
    private String cou_name;
    private String cou_money;

    private boolean isSelect = false;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getCou_id() {
        return cou_id;
    }

    public void setCou_id(String cou_id) {
        this.cou_id = cou_id;
    }

    public String getCou_name() {
        return cou_name;
    }

    public void setCou_name(String cou_name) {
        this.cou_name = cou_name;
    }

    public String getCou_money() {
        return cou_money;
    }

    public void setCou_money(String cou_money) {
        this.cou_money = cou_money;
    }
}
