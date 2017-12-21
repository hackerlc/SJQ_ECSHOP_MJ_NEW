package com.ecjia.network.netmodle;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.ecjia.consts.AndroidManager;

import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.BRAND;
import com.ecjia.entity.responsemodel.CATEGORY;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.PRICE_RANGE;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.LG;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdvanceSearchModel extends BaseModel {

    public ArrayList<BRAND> brandList = new ArrayList<BRAND>();
    public ArrayList<PRICE_RANGE> priceRangeArrayList = new ArrayList<PRICE_RANGE>();
    public ArrayList<CATEGORY> categoryArrayList = new ArrayList<CATEGORY>();
    SESSION session = SESSION.getInstance();;
    public AdvanceSearchModel(Context context) {
        super(context);
    }

    public void getAllBrand(String category_id, final Handler handler) {
        String url = ProtocolConst.BRAND;
        SESSION session = SESSION.getInstance();

        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("session", session.toJson());
            if (null != category_id) {
                requestJsonObject.put("category_id", category_id);
                requestJsonObject.put("device", DEVICE.getInstance().toJson());
            }

        } catch (JSONException e) {
            // TODO: handle exception
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===goods/brand传入===" + requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {

                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            AdvanceSearchModel.this.callback(jo);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray dataJsonArray = jo.optJSONArray("data");
                                LG.i("===goods/brand返回===" + jo.toString());
                                if (null != dataJsonArray && dataJsonArray.length() > 0) {
                                    brandList.clear();
                                    for (int i = 0; i < dataJsonArray.length(); i++) {
                                        JSONObject brandJsonObject = dataJsonArray.getJSONObject(i);
                                        BRAND brandItem = BRAND.fromJson(brandJsonObject);
                                        brandList.add(brandItem);
                                    }
                                }
                                Message msg = new Message();
                                msg.obj = ProtocolConst.BRAND;
                                msg.what = responseStatus.getSucceed();
                                handler.sendMessage(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                });

    }

    public void getPriceRange(int categoryId, final Handler handler) {
        String url = ProtocolConst.PRICE_RANGE;
      //  pd.show();
        SESSION session = SESSION.getInstance();

        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("category_id", categoryId);
            requestJsonObject.put("device",DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            // TODO: handle exception
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===goods/price_range传入===" + requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        //pd.dismiss();
                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                LG.i("===goods/price_range返回===" + jo.toString());
                                JSONArray dataJsonArray = jo.optJSONArray("data");
                                if (null != dataJsonArray && dataJsonArray.length() > 0) {
                                    priceRangeArrayList.clear();
                                    for (int i = 0; i < dataJsonArray.length(); i++) {
                                        JSONObject priceJsonObject = dataJsonArray.getJSONObject(i);
                                        PRICE_RANGE brandItem = PRICE_RANGE.fromJson(priceJsonObject);
                                        priceRangeArrayList.add(brandItem);
                                    }
                                }
                                Message msg = new Message();
                                msg.obj = ProtocolConst.PRICE_RANGE;
                                msg.what = responseStatus.getSucceed();
                                handler.sendMessage(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                });
    }

    public void getCategory(final Handler handler) {
        String url = ProtocolConst.CATEGORY;
        HttpUtils http = new HttpUtils();
        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("device",DEVICE.getInstance().toJson());
            requestJsonObject.put("session", session.toJson());
        } catch (JSONException e) {
            // TODO: handle exception
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===goods/category传入===" + requestJsonObject.toString());
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url,params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                JSONObject jo;
                try {
                    jo = new JSONObject(objectResponseInfo.result);
                    categoryArrayList.clear();
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray categoryJSONArray = jo.optJSONArray("data");
                        LG.i("===goods/category返回===" + jo.toString());
                        if (null != categoryJSONArray && categoryJSONArray.length() > 0) {
                            for (int i = 0; i < categoryJSONArray.length(); i++) {
                                JSONObject categoryObject = categoryJSONArray.getJSONObject(i);
                                CATEGORY category = CATEGORY.fromJson(categoryObject);
                                categoryArrayList.add(category);
                            }
                        }
                        Message msg = new Message();
                        msg.obj = ProtocolConst.CATEGORY;
                        msg.what = responseStatus.getSucceed();
                        handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }



}
