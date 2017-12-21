package com.ecjia.network.API;

import com.ecjia.entity.model.City;
import com.ecjia.entity.model.JsonRet;
import com.ecjia.entity.model.MainData;
import com.ecjia.entity.model.News;
import com.ecjia.entity.model.Update;
import com.ecjia.entity.model.WholesaleTop;
import com.ecjia.network.base.BaseRes;
import com.gear.apifinder.annotation.APIService;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.ecjia.consts.AndroidManager.SERVICE_TAG;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-01-24.
 */
@APIService
public interface APIPublic {

    /**
     * @param account     账号
     * @param passwordKey 密码
     * @return
     */
    @FormUrlEncoded
    @POST("appLogin")
    Flowable<BaseRes> login(@Header("drivenCode") String drivenCode, @Field("account") String account, @Field("passwordKey") String passwordKey);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"home/data")
    Flowable<BaseRes<MainData>> getMainData(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"subweb/list")
    Flowable<BaseRes<List<City>>> getSubstations(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"wholesale/top")
    Flowable<BaseRes<WholesaleTop>> getWholesaleTop(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"article/list")
    Flowable<BaseRes<List<News>>> getNews(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"article/detail")
    Flowable<BaseRes<List<News>>> getNewsDetail(@Field("json") String json);

    @POST("http://www.sjq.cn/api/fxb.php?")
    Flowable<JsonRet<Update>> updateFile(@Query("action") String action
            , @Query("version") String version, @Query("appType") String appType);
}
