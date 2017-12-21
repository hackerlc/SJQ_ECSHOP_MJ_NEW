package com.ecjia.widgets.paycenter.uppay;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.PayToastView;
import com.ecjia.consts.AndroidManager;
import com.ecjia.widgets.paycenter.base.BasePayHelper;
import com.ecjia.widgets.paycenter.base.OnPaySucceedListener;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

//银联支付插件类
public class UppayHelper extends BasePayHelper<UppayData> {
    public UppayHelper(Activity activity, UppayData data) {
        super(activity, data);
    }
    @Override
    public void startPay() {
        UPPayAssistEx.startPayByJAR(activity, PayActivity.class, null, null, paymentData.getPay_upmp_tn(), AndroidManager.SERVERMODE);// 运行银联支付
    }
    @Override
    public boolean checkPaymentUtilExist() {
        return false;
    }
    @Override
    public void setOnPaysucceedListener(OnPaySucceedListener listener) {
        this.mPayListener = listener;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String str = data.getExtras().getString("pay_result");
        String msg = null;
        boolean payimage = true;// 定义显示的成功的图片 true为不显示 false为显示
        if (str.equalsIgnoreCase("success")) {
            msg = paysuccess;
            payimage = false;
        } else if (str.equalsIgnoreCase("fail")) {
            msg = payfail;
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = cancel_pay;
        } else {
            msg = system_busy;
        }
 
        /**
         * 弹出提示 true 成功
         * */
        PayToastView paytoast = new PayToastView(activity, msg, payimage);
        paytoast.setGravity(Gravity.CENTER, 0, 0);
        paytoast.show();
 
        if (!payimage) {
            String payment_paysuccess = activity.getResources().getString(R.string.payment_paysuccess);
            paySucceed(OnPaySucceedListener.PaymentType.PAYMENT_UPPAY, payment_paysuccess);
        }
    }
    @Override
    public void paySucceed(PaymentType type, String payment_paysuccess) {
        if (mPayListener != null) {
            mPayListener.paySucceed(type, payment_paysuccess);
        }
    }
 
}