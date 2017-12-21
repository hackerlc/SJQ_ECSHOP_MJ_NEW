package com.ecjia.entity.requestmodel;

import java.io.Serializable;
import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-04-07.
 */

public class SubOrderBase extends BaseRequest implements Serializable {
    /*"address_id": 9902,//收货人信息id,就是consignee中的id
    "rec_id": "1,3,4", //购物车id集合
            .."bonus_id": 31, //红包id
            .."pay_id":17 支付宝默认的pay_id*/

    private String address_id;
    private String rec_id;
    private String bonus_id;
    private String pay_id;
    private List<SubOrderCondition> shop_use_condition;

    public SubOrderBase(){
        super.setInfo();
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getRec_id() {
        return rec_id;
    }

    public void setRec_id(String rec_id) {
        this.rec_id = rec_id;
    }

    public String getBonus_id() {
        return bonus_id;
    }

    public void setBonus_id(String bonus_id) {
        this.bonus_id = bonus_id;
    }

    public String getPay_id() {
        return pay_id;
    }

    public void setPay_id(String pay_id) {
        this.pay_id = pay_id;
    }

    public List<SubOrderCondition> getShop_use_condition() {
        return shop_use_condition;
    }

    public void setShop_use_condition(List<SubOrderCondition> shop_use_condition) {
        this.shop_use_condition = shop_use_condition;
    }
}
