package com.ecjia.network.API;

import com.ecjia.entity.model.AddressData;
import com.ecjia.entity.model.Banner;
import com.ecjia.entity.model.BannerSnatch;
import com.ecjia.entity.model.SimpleGoods;
import com.ecjia.entity.responsemodel.ODERRETURNGOODSDETAIL;
import com.ecjia.entity.responsemodel.ORDER_INFO;
import com.ecjia.entity.responsemodel.REJECTEDGOODSTATU;
import com.ecjia.entity.responsemodel.RETURNCAUSE;
import com.ecjia.entity.responsemodel.RETURNSTATUS;
import com.ecjia.entity.responsemodel.SHIPPING;
import com.ecjia.network.base.BaseRes;
import com.ecjia.view.activity.goodsorder.balance.entity.NewBalanceBaseData;
import com.gear.apifinder.annotation.APIService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static com.ecjia.consts.AndroidManager.SERVICE_TAG;

/**
 * SJQ_ECSHOP_MJ_NEW
 * 商品API
 * Created by YichenZ on 2017/2/10 14:26.
 */
@APIService
public interface APIGoods {
    @FormUrlEncoded
    @POST(SERVICE_TAG + "goods/list")
    Flowable<BaseRes<List<SimpleGoods>>> getBusinessGoodsList(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG + "goods/list")
    Flowable<BaseRes<List<SimpleGoods>>> userApplyMenber(@Field("json") String json, @Part("identification_card_front\"; filename=\"img.jpg") RequestBody pic);

    //获取商品运费
    @FormUrlEncoded
    @POST(SERVICE_TAG + "goods/shipping_fee")
    Flowable<BaseRes<AddressData>> getShippingFee(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"goods/timegroup")
    Flowable<BaseRes<Banner>> getTimeGroup(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"goods/timepanic")
    Flowable<BaseRes<BannerSnatch>> getTimePanic(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"market/list")
    Flowable<BaseRes<Banner>> getMarket(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"goods/subweb")
    Flowable<BaseRes<ArrayList<SimpleGoods>>> getSubWeb(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"goods/list")
    Flowable<BaseRes<ArrayList<SimpleGoods>>> getGoodsList(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"order/list_return_cause")//获取退换货原因
    Flowable<BaseRes<ArrayList<RETURNCAUSE>>> getReturnause(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"order/submit_return")//订单退款、退货退款
    Flowable<BaseRes<REJECTEDGOODSTATU>> getRejected(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"order/return_detail")//订单退款、退货退款进度详情
    Flowable<BaseRes<ODERRETURNGOODSDETAIL>> getReturnDetail(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"order/shipping")//订单退款、退货退款，获取物流公司列表信息
    Flowable<BaseRes<ArrayList<SHIPPING>>> getShipping(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"order/send_courier")//订单退款、退货退款，提交退货物流信息
    Flowable<BaseRes<RETURNSTATUS>> getSendCourier(@Field("json") String json);

    /**
     *  admin/orders/return_received
     */
    @FormUrlEncoded
    @POST(SERVICE_TAG+"order/return_received")//确认 退换货 收到退回货品
    Flowable<BaseRes<RETURNSTATUS>> getReturnGoodsReceivedShipping(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"flow/checkOrder")//确认订单，订单生成预览界面
    Flowable<BaseRes<NewBalanceBaseData>> getChekOrder(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"flow/done")//确认提交生产订单
    Flowable<BaseRes<ORDER_INFO>> getSubmitOrder(@Field("json") String json);

}
