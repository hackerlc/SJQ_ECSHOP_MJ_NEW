package com.ecjia.widgets.paycenter.uppay;

import com.ecjia.widgets.paycenter.base.BasePaymentData;

//银联支付的数据实体类
public class UppayData implements BasePaymentData {
    private String pay_upmp_tn;//银联支付参数
    public String getPay_upmp_tn() {
        return pay_upmp_tn;
    }
    public void setPay_upmp_tn(String pay_upmp_tn) {
        this.pay_upmp_tn = pay_upmp_tn;
    }
}