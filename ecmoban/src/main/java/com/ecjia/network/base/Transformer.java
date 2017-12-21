package com.ecjia.network.base;


import com.ecjia.consts.AppConst;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Retrofit2  线程切换，flatMap检测Response model是否为null
 * Created by yubaokang on 2016/9/12.
 */
public class Transformer {

    public static <T> FlowableTransformer<T, T> ioMain() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> flowable) {
                return flowable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * @param <T> BaseRes类中的泛型 -即有result
     * @return
     */
    public static <T> FlowableTransformer<BaseRes<T>, T> retrofit() {
        return new FlowableTransformer<BaseRes<T>, T>() {
            @Override
            public Publisher<T> apply(Flowable<BaseRes<T>> flowable) {
                return flowable.compose(Transformer.<T>retrofitIo()).observeOn(AndroidSchedulers.mainThread());//线程切换
            }
        };
    }

    /**
     * @param <T> BaseRes类中的泛型 -即有result
     * @return
     */
    public static <T> FlowableTransformer<BaseRes<T>, T> retrofitIo() {
        return new FlowableTransformer<BaseRes<T>, T>() {
            @Override
            public Publisher<T> apply(Flowable<BaseRes<T>> flowable) {
                return flowable.flatMap(new Function<BaseRes<T>, Publisher<T>>() {
                    @Override
                    public Publisher<T> apply(BaseRes<T> baseRes) {
                        if (baseRes == null) {//当作没有网络
                            return Flowable.error(new Throwable("没有网络"));
                        } else {//有网络
                            if (AppConst.ResponseSucceed==baseRes.getStatus().getSucceed()) {
                                T t = baseRes.getData();
                                if (t == null || (t instanceof List && ((List) t).size() == 0)) {
                                    return Flowable.error(new Throwable("response's model is null"));
                                } else {
                                    return Flowable.just(baseRes.getData());
                                }
                            } else {
                                //如果网络未连接不会调用flatMap,所以网络未连接不会出现ServerException错误
                                return Flowable.error(new Throwable(baseRes.getStatus().getError_desc()));//return the response's returnCode and msg
                            }
                        }
                    }
                }).subscribeOn(Schedulers.io());//线程切换
            }
        };
    }

    /**
     * 接口响应只有BaseRes,内部的泛型为null
     *
     * @return
     */
    public static FlowableTransformer<BaseRes, BaseRes> retrofitBaseRes() {
        return new FlowableTransformer<BaseRes, BaseRes>() {
            @Override
            public Publisher<BaseRes> apply(Flowable<BaseRes> flowable) {
                return flowable.compose(retrofitBaseResIo()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 接口响应只有BaseRes,内部的泛型为null
     *
     * @return
     */
    public static FlowableTransformer<BaseRes, BaseRes> retrofitBaseResIo() {
        return new FlowableTransformer<BaseRes, BaseRes>() {
            @Override
            public Publisher<BaseRes> apply(Flowable<BaseRes> flowable) {
                return flowable.flatMap(new Function<BaseRes, Publisher<BaseRes>>() {
                    @Override
                    public Publisher<BaseRes> apply(BaseRes t) {
                        if (t == null) {//当作没有网络
                            return Flowable.error(new Throwable("网络错误"));
                            //return Flowable.error(new NetWorkException());
                        } else {//有网络
                            if (AppConst.ResponseSucceed==t.getStatus().getSucceed()) {//如果返回时"1"表示数据请求正常
                                return Flowable.just(t);
                            } else {
                                //如果网络未连接不会调用flatMap,所以网络未连接不会出现ServerException错误
                                return Flowable.error(new Throwable(t.getStatus().getError_desc()));//return the response's returnCode and msg
                            }
                        }
                    }
                }).subscribeOn(Schedulers.io());
            }
        };
    }
}
