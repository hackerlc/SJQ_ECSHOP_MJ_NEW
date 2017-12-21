package com.ecjia.widgets.paycenter.base;

public interface OnPaySucceedListener {

    int PAYMENT_ZERO = 0;//零元商品
    int PAYMENT_CODE_COD = 1;//货到付款
    int PAYMENT_CODE_BANK = 2;//银行转账
    int PAYMENT_CODE_ONLINE = 3;//线上(余额支付以外)
    int PAYMENT_CODE_BALANCE = 4;//线上余额支付
    int PAYMENT_UPPAY = 5;//银联支付
    int PAYMENT_ALIPAY = 6;//支付宝支付
    int PAYMENT_WXPAY = 7;//微信支付
    int PAYMENT_WAPPAY = 8;//网页支付

    //执行支付操作
    void paySucceed(PaymentType type, String payment_paysuccess);

    enum PaymentType {
        PAYMENT_ZERO, PAYMENT_CODE_COD, PAYMENT_CODE_BANK, PAYMENT_CODE_ONLINE, PAYMENT_CODE_BALANCE,PAYMENT_UPPAY
        ,PAYMENT_ALIPAY, PAYMENT_WXPAY,PAYMENT_WAPPAY
    }
}