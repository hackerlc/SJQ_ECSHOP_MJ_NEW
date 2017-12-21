package com.ecjia.entity.requestmodel;

import java.io.Serializable;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-04-07.
 */

public class SubOrderCondition implements Serializable {
    /*{//店铺用优惠活动，用优惠券情况
        ...."shop_id": "310", //店铺id
        ...."cou_id": "11",..//优惠券id
        ...."act_id": 2,......//优惠活动id
        ...."shipping_id": 37 //配送方式id
            ..}*/

    private String shop_id;
    private String cou_id;
    private String act_id;
    private String shipping_id;

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getCou_id() {
        return cou_id;
    }

    public void setCou_id(String cou_id) {
        this.cou_id = cou_id;
    }

    public String getAct_id() {
        return act_id;
    }

    public void setAct_id(String act_id) {
        this.act_id = act_id;
    }

    public String getShipping_id() {
        return shipping_id;
    }

    public void setShipping_id(String shipping_id) {
        this.shipping_id = shipping_id;
    }
}
