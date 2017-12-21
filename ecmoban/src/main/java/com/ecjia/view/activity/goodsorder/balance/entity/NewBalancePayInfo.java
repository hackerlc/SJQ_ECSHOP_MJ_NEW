package com.ecjia.view.activity.goodsorder.balance.entity;

import java.io.Serializable;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-04-07.
 */

public class NewBalancePayInfo implements Serializable {

    private String pay_code;//"pay_alipay", //支付方式code
    private String pay_id;//

    public String getPay_code() {
        return pay_code;
    }

    public void setPay_code(String pay_code) {
        this.pay_code = pay_code;
    }

    public String getPay_id() {
        return pay_id;
    }

    public void setPay_id(String pay_id) {
        this.pay_id = pay_id;
    }
}
