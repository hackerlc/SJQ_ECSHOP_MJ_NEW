package com.ecjia.network.API;

import com.ecjia.entity.model.ShopInfo;
import com.ecjia.entity.responsemodel.CATEGORY;
import com.ecjia.entity.responsemodel.SIMPLEGOODS;
import com.ecjia.network.base.BaseRes;
import com.gear.apifinder.annotation.APIService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.ecjia.consts.AndroidManager.SERVICE_TAG;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/3/1 13:29.
 */
@APIService
public interface APIShop {
    @FormUrlEncoded
    @POST(SERVICE_TAG+"seller/best")
    Flowable<BaseRes<List<ShopInfo>>> getSellers(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"seller/list")
    Flowable<BaseRes<List<ShopInfo>>> getList(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"merchant/goods/category")
    Flowable<BaseRes<ArrayList<CATEGORY>>> getCategory(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"merchant/goods/list")
    Flowable<BaseRes<ArrayList<SIMPLEGOODS>>> getGoodsList(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"seller/collect/create")
    Flowable<BaseRes<List<String>>> putSellerCollectCreate(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"seller/collect/delete")
    Flowable<BaseRes<List<String>>> putSellerCollectDelete(@Field("json") String json);
}
