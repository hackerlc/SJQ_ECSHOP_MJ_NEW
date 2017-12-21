package com.ecjia.view.activity.goodsorder.balance.entity;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-04-06.
 */

public class ShopFavourable {
    /**
     * act_id : 2
     * act_name : 打折
     * save_money : 1700.00
     */
    private String act_id;
    private String act_name;
    private String save_money;//店铺活动

    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getAct_id() {
        return act_id;
    }

    public void setAct_id(String act_id) {
        this.act_id = act_id;
    }

    public String getAct_name() {
        return act_name;
    }

    public void setAct_name(String act_name) {
        this.act_name = act_name;
    }

    public String getSave_money() {
        return save_money;
    }

    public void setSave_money(String save_money) {
        this.save_money = save_money;
    }
}
