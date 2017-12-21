package com.ecjia.widgets.paycenter.wxpay;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.ecjia.consts.AndroidManager;
import com.ecjia.widgets.paycenter.base.BasePayHelper;
import com.ecjia.widgets.paycenter.base.OnPaySucceedListener;
import com.ecjia.util.MD5;
import com.ecjia.util.WXPayUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

//银联支付插件类
public class WXpayHelper extends BasePayHelper<WXpayData> {

    //集成微信支付
    Map<String, String> resultunifiedorder;
    StringBuffer sb;
    PayReq req;
    private String prePayXml;
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(activity, null);

    public WXpayHelper(Activity activity, WXpayData data) {
        super(activity, data);
        req = new PayReq();
        sb = new StringBuffer();
        msgApi.registerApp(AndroidManager.WXAPPID);
    }

    @Override
    public void startPay() {
        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
        getPrepayId.execute();
    }

    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
            resultunifiedorder = result;
            genPayReq();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {

            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = genProductArgs();

            Log.e("orion", entity);

            byte[] buf = WXPayUtil.httpPost(url, entity);

            String content = new String(buf);
            if (!content.contains("SUCCESS")) {
            }
            Log.e("orionf", content);
            Map<String, String> xml = decodeXml(content);

            return xml;
        }
    }

    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            //实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion", e.toString());
        }
        return null;

    }

    private String genProductArgs() {
        StringBuffer xml = new StringBuffer();

        try {
            String nonceStr = genNonceStr();

            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", AndroidManager.WXAPPID));
            packageParams.add(new BasicNameValuePair("body", paymentData.getSubject()));
            packageParams.add(new BasicNameValuePair("mch_id", paymentData.getMch_id()));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", paymentData.getNotify_url()));
            packageParams.add(new BasicNameValuePair("out_trade_no", genOutTradNo()));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));

            int total_fee = 0;
            BigDecimal bd = new BigDecimal(paymentData.getOrder_amount());
            BigDecimal bd2 = new BigDecimal(100);
            total_fee = (int) (bd.multiply(bd2).doubleValue());

            packageParams.add(new BasicNameValuePair("total_fee", total_fee + ""));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));

            String sign = genPackageSign(packageParams);

            packageParams.add(new BasicNameValuePair("sign", sign));

            String xmlstring = toXml(packageParams);
            return xmlstring;

        } catch (Exception e) {
            Log.e("WXPAYTAG", "genProductArgs fail, ex = " + e.getMessage());
            return null;
        }

    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }


    private String genOutTradNo() {
        return paymentData.getPay_order_sn();
    }

    /**
     * 生成签名
     */

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(paymentData.getApipwd());


        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", packageSign);
        return packageSign;
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(paymentData.getApipwd());

        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", appSign);
        return appSign;
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");

        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");


            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        try {
            prePayXml = new String((sb.toString()).getBytes("UTF-8"), "ISO-8859-1");
        } catch (Exception e) {
        }

        Log.e("orion", sb.toString());
        return prePayXml;
    }

    private void genPayReq() {

        req.appId = AndroidManager.WXAPPID;
        req.partnerId = paymentData.getMch_id();
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());


        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        Log.e("orions", signParams.toString());
        req.sign = genAppSign(signParams);

        msgApi.registerApp(AndroidManager.WXAPPID);
        msgApi.sendReq(req);

    }


    @Override
    public boolean checkPaymentUtilExist() {
        return getWXApps(activity);
    }

    public boolean getWXApps(Context context) {
        PackageManager pManager = context.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            //判断是否为非系统预装的应用程序
            if ("com.tencent.mm".equals(pak.packageName)) {
                // customs applications
                return true;
            }
        }
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