package com.ecjia.network.API;

import com.ecjia.entity.model.AddressData;
import com.ecjia.entity.model.SimpleGoods;
import com.ecjia.entity.responsemodel.GOODSCOUPONS;
import com.ecjia.entity.responsemodel.LOGINDATA;
import com.ecjia.entity.responsemodel.REGISTERCODE;
import com.ecjia.entity.responsemodel.USERBONUS;
import com.ecjia.entity.responsemodel.USERCOUPON;
import com.ecjia.entity.responsemodel.USERMENBERS;
import com.ecjia.network.base.BaseRes;
import com.gear.apifinder.annotation.APIService;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.ecjia.consts.AndroidManager.SERVICE_TAG;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-02-20.
 */
@APIService
public interface APIUser {

//    @FormUrlEncoded
//    @POST(SERVICE_TAG + "goods/list")
//    Flowable<BaseRes<List<SimpleGoods>>> userApplyMenber(@Field("json") String json, @Body RequestBody Body);

    //获取用户红包
    @FormUrlEncoded
    @POST(SERVICE_TAG + "user/bonus")
    Flowable<BaseRes<List<USERBONUS>>> getUserBonus(@Field("json") String json);

    //获取用户红包-根据红包密码获取
    @FormUrlEncoded
    @POST(SERVICE_TAG + "bonus/password")
    Flowable<BaseRes<USERBONUS>> getUserBonusByPwd(@Field("json") String json);

    //获取用户优惠券
    @FormUrlEncoded
    @POST(SERVICE_TAG + "user/coupon")
    Flowable<BaseRes<List<USERCOUPON>>> getUserCoupon(@Field("json") String json);

    //采购商认证
    @FormUrlEncoded
    @POST(SERVICE_TAG + "user/account/authentication")
    Flowable<BaseRes<USERMENBERS>> getApplyMenber(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG + "user/account/authentication")
    Flowable<BaseRes<USERMENBERS>> test(@Field("json") String json);

    //获取商品可以领取优惠券列表
    @FormUrlEncoded
    @POST(SERVICE_TAG + "goods/list_coupons")
    Flowable<BaseRes<List<GOODSCOUPONS>>> getGoodCoupon(@Field("json") String json);

    //领取商品优惠券
    @FormUrlEncoded
    @POST(SERVICE_TAG + "goods/coupons_receive")
    Flowable<BaseRes<GOODSCOUPONS>> getReceiveGoodCoupon(@Field("json") String json);

    //用户手机注册。输入手机号获取验证码
    @FormUrlEncoded
    @POST(SERVICE_TAG + "user/userbind")
    Flowable<BaseRes<REGISTERCODE>> getRegisterPhoneCode(@Field("json") String json);
    //用户手机注册。校验 验证码
    @FormUrlEncoded
    @POST(SERVICE_TAG + "validate/bind")
    Flowable<BaseRes<REGISTERCODE>> getRegisterCheckCode(@Field("json") String json);
    //用户手机注册。输入密码，完成注册
    @FormUrlEncoded
    @POST(SERVICE_TAG + "user/signup")
    Flowable<BaseRes<LOGINDATA>> getRegisterSetPwd(@Field("json") String json);

}
