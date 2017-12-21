package com.ecjia.widgets.paycenter.base;

import android.app.Activity;

import com.ecmoban.android.sijiqing.R;

/**
 * Created by Administrator on 2016/9/5.
 */
public abstract class BasePayHelper<T extends BasePaymentData> implements OnPaySucceedListener {

    public String paysuccess;
    public String paywait;
    public String payfail;
    public String cancel_pay;
    public String system_busy;

    public OnPaySucceedListener mPayListener;
    protected Activity activity;
    public T paymentData;
    public BasePayHelper(Activity activity, T data) {
        this.activity = activity;
        this.paymentData = data;

        paysuccess = activity.getResources().getString(R.string.payment_paysuccess);
        paywait = activity.getResources().getString(R.string.payment_paywait);
        payfail = activity.getResources().getString(R.string.payment_payfail);
        cancel_pay = activity.getResources().getString(R.string.payment_cancel_pay);
        system_busy = activity.getResources().getString(R.string.payment_system_busy);
    }

    public abstract void startPay();
    public abstract boolean checkPaymentUtilExist();
    public abstract void setOnPaysucceedListener(OnPaySucceedListener listener);
}