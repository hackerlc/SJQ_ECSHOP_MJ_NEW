package com.ecjia.widgets.paycenter.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;

import com.alipay.sdk.app.PayTask;
import com.ecjia.widgets.PayToastView;
import com.ecjia.widgets.ToastView;
import com.ecjia.consts.AndroidManager;
import com.ecjia.widgets.paycenter.alipay.alipayutil.PayResult;
import com.ecjia.widgets.paycenter.alipay.alipayutil.SignUtils;
import com.ecjia.widgets.paycenter.base.BasePayHelper;
import com.ecjia.widgets.paycenter.base.OnPaySucceedListener;
import com.ecjia.util.LG;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

//银联支付插件类
public class AlipayHelper extends BasePayHelper<AlipayData> {

    private static final int RQF_PAY = 1;
    private static final int RQF_LOGIN = 2;
    private String showsuccess;

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((String) msg.obj);
            boolean payimage = true;// 定义显示的成功的图片 true为不显示 false为显示
            showsuccess = payResult.getResultStatus();

            LG.e("===showsuccess===" + showsuccess);
            // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
            if (TextUtils.equals(showsuccess, AndroidManager.ALIPAY_SUCCESS)) {
                showsuccess = paysuccess;
                payimage = false;
            } else {
                // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                if (TextUtils.equals(showsuccess, "8000")) {
                    showsuccess = paywait;
                    payimage = false;
                } else {
                    showsuccess = payfail;
                }
            }

            PayToastView paytoast = new PayToastView(activity, showsuccess, payimage);
            paytoast.setGravity(Gravity.CENTER, 0, 0);
            paytoast.show();
            if (!payimage) {
                paySucceed(PaymentType.PAYMENT_ALIPAY, paysuccess);
            }
        }
    };

    public AlipayHelper(Activity activity, AlipayData data) {
        super(activity, data);
    }
    @Override
    public void startPay() {
        if (TextUtils.isEmpty(paymentData.getOrder_amount()) || TextUtils.isEmpty(paymentData
                .getSubject()) || TextUtils.isEmpty(paymentData.getBody()) || TextUtils.isEmpty(paymentData.getPrivate_key())) {
            ToastView toast = new ToastView(activity, system_busy);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            try {
                String info = getNewOrderInfo(paymentData.getBody());
                String sign = SignUtils.sign(info, paymentData.getPrivate_key());
                try {
                    /**
                     * 仅需对sign 做URL编码
                     */
                    sign = URLEncoder.encode(sign, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                /**
                 * 完整的符合支付宝参数规范的订单信息
                 */
                final String orderInfo = info + "&sign=\"" + sign + "\"&" + getSignType();
                new Thread() {
                    public void run() {
                        PayTask alipay = new PayTask(activity);
                        String result = alipay.pay(orderInfo,true);
                        Message msg = new Message();
                        msg.what = RQF_PAY;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                }.start();

            } catch (Exception ex) {
                ex.printStackTrace();
                ToastView toast = new ToastView(activity,
                        payfail);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    // 设置支付宝所需传入的参数
    private String getNewOrderInfo(String body) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + paymentData.getPartner() + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + paymentData.getSeller_id() + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + paymentData.getPay_order_sn() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + paymentData.getSubject() + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + paymentData.getOrder_amount() + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + URLEncoder.encode(paymentData.getNotify_url()) + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        return orderInfo;
    }

    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    @Override
    public boolean checkPaymentUtilExist() {
        return false;
    }

    @Override
    public void setOnPaysucceedListener(OnPaySucceedListener listener) {
        this.mPayListener = listener;
    }

    @Override
    public void paySucceed(PaymentType type, String payment_paysuccess) {
        if (mPayListener != null) {
            mPayListener.paySucceed(type, payment_paysuccess);
        }
    }
 
}