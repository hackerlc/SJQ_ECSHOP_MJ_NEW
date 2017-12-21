package com.ecjia.network.query;

import android.text.TextUtils;

import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.util.common.BitmapUtils;
import com.ecjia.widgets.paycenter.alipay.alipayutil.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import gear.yc.com.gearlibrary.network.api.GearHttpServiceManager;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-02-21.
 */

public class UserQuery extends AbstractQuery {

    protected static UserQuery instance;

    public static UserQuery getInstance() {
        if (instance == null) {
            synchronized (UserQuery.class) {
                if (instance == null) {
                    instance = new UserQuery();
                }
            }
        }
        return instance;
    }

    public UserQuery() {
        requestJsonObject = new JSONObject();
    }


    public String getUserBonus(String coupon_type) {
        requestJsonObject=new JSONObject();
        try {
            requestJsonObject.put("session", SESSION.getInstance().toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("bonus_type", coupon_type);
        } catch (JSONException e) {
            e.getMessage();
        }

        return requestJsonObject.toString();
    }

    public String getUserBonusByPwd(String bonus_password) {
        requestJsonObject=new JSONObject();
        try {
            requestJsonObject.put("session", SESSION.getInstance().toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("bonus_password", bonus_password);
        } catch (JSONException e) {
            e.getMessage();
        }

        return requestJsonObject.toString();
    }



    public String getUserCoupon(String coupon_type) {
        requestJsonObject=new JSONObject();
        try {
            requestJsonObject.put("session", SESSION.getInstance().toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("coupon_type", coupon_type);
        } catch (JSONException e) {
            e.getMessage();
        }
        return requestJsonObject.toString();
    }

    ////获取商品可以领取优惠券列表
    public String getGoodCoupon(String goods_id) {
        requestJsonObject=new JSONObject();
        try {
            requestJsonObject.put("session", SESSION.getInstance().toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("goods_id", goods_id);
        } catch (JSONException e) {
            e.getMessage();
        }
        return requestJsonObject.toString();
    }

    //领取商品优惠券
    public String getReceiveGoodCoupon(String cou_id) {
        requestJsonObject=new JSONObject();
        try {
            requestJsonObject.put("session", SESSION.getInstance().toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("cou_id", cou_id);
        } catch (JSONException e) {
            e.getMessage();
        }
        return requestJsonObject.toString();
    }

    public String uploudApplyMenberInfo(String shopName,String address, String shopType,String imagCardOnPath,String imagCardDpwnPath,String businessLicensePath) {
        requestJsonObject=new JSONObject();
        try {
            requestJsonObject.put("session", SESSION.getInstance().toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("shop_name", shopName);
            requestJsonObject.put("shop_address", address);
            requestJsonObject.put("shop_category", shopType);
            requestJsonObject.put("identification_card_front", imagCardOnPath);
            requestJsonObject.put("identification_card_begind", imagCardDpwnPath);
            requestJsonObject.put("business_licence", businessLicensePath);
        } catch (JSONException e) {
            e.getMessage();
        }
        return requestJsonObject.toString();
    }

    public String getChekOrder(String array, String address_id) {
        requestJsonObject=new JSONObject();
        try {
            requestJsonObject.put("session", SESSION.getInstance().toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            if (!TextUtils.isEmpty(array) && null != array) {
                requestJsonObject.put("rec_id", array);
            }
            if (!TextUtils.isEmpty(address_id)) {
                requestJsonObject.put("address_id", address_id);
            }
        } catch (JSONException e) {
            e.getMessage();
        }
        return requestJsonObject.toString();
    }

    //用户手机注册。输入手机号获取验证码
    public String getRegisterPhoneCode(String mobileNo) {
        requestJsonObject=new JSONObject();
        try {
//            requestJsonObject.put("session", SESSION.getInstance().toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("value", mobileNo);
            requestJsonObject.put("type", "mobile");
        } catch (JSONException e) {
            e.getMessage();
        }
        return requestJsonObject.toString();
    }
    //用户手机注册。校验 验证码
    public String getRegisterCheckCode(String mobileNo,String code) {
        requestJsonObject=new JSONObject();
        try {
//            requestJsonObject.put("session", SESSION.getInstance().toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("value", mobileNo);
            requestJsonObject.put("type", "mobile");
            requestJsonObject.put("code", code);
        } catch (JSONException e) {
            e.getMessage();
        }
        return requestJsonObject.toString();
    }
    //用户手机注册。输入密码，完成注册
    public String getRegisterSetPwd(String mobile,String password,String invite_code) {
        requestJsonObject=new JSONObject();
        try {
//            requestJsonObject.put("session", SESSION.getInstance().toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("name", mobile);
            requestJsonObject.put("password", password);
            requestJsonObject.put("email", "");
            requestJsonObject.put("mobile", mobile);
            requestJsonObject.put("invite_code", invite_code);
        } catch (JSONException e) {
            e.getMessage();
        }
        return requestJsonObject.toString();
    }


}
