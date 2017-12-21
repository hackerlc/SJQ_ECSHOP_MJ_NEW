package com.ecjia.network.netmodle;

import android.content.Context;
import android.text.TextUtils;


import com.ecjia.consts.ProtocolConst;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecjia.consts.AndroidManager;
import com.ecjia.entity.responsemodel.ADSENSE;
import com.ecjia.entity.responsemodel.CATEGORY;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.LOCATION;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.PAGINATION;
import com.ecjia.entity.responsemodel.SELLERINFO;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.SHOPDATA;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.LG;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/22.
 */
public class SellerModel extends BaseModel {
    public ArrayList<SELLERINFO> sellerinfos = new ArrayList<SELLERINFO>();
    public ArrayList<SELLERINFO> sellercollects = new ArrayList<SELLERINFO>();
    public ArrayList<CATEGORY> sellercategory = new ArrayList<CATEGORY>();
    public ArrayList<ADSENSE> adsenseList = new ArrayList<ADSENSE>();
    public MyProgressDialog pd;
    public PAGINATED paginated;
    private Context context;
    public SHOPDATA shopHomeData;

    public SellerModel(Context context) {
        super(context);
        pd = MyProgressDialog.createDialog(context);
        pd.setMessage(res.getString(R.string.loading));
        this.context = context;
    }

    public void getSellerList(String category) {
        getSellerList(category, false);
    }

    public void getSellerList(String category, boolean isShow) {
        final String url = ProtocolConst.SELLER_LIST;
        SESSION session = SESSION.getInstance();

        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(6);
        if (isShow) {
            pd.show();
        }
        JSONObject requestJsonObject = new JSONObject();
        try {
            if (!TextUtils.isEmpty(category) && !"-1".equals(category)) { //-1代表全部分类
                requestJsonObject.put("category_id", category);
            }
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("location", location.toJson());
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===seller/list传入==="+requestJsonObject.toString());

        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("===seller/list返回==="+jo.toString());

                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray dataJsonArray = jo.optJSONArray("data");
                        sellerinfos.clear();
                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
                            for (int i = 0; i < dataJsonArray.length(); i++) {
                                JSONObject jsonObject = dataJsonArray.getJSONObject(i);
                                SELLERINFO sellerinfo = SELLERINFO.fromJson(jsonObject);
                                sellerinfos.add(sellerinfo);
                            }
                        }
                        paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                    }
                    SellerModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===seller/list返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }

    public void getSellerListMore(String category) {
        final String url = ProtocolConst.SELLER_LIST;
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(sellerinfos.size() / 6 + 1);
        pagination.setCount(6);
        JSONObject requestJsonObject = new JSONObject();
        try {
            if (!TextUtils.isEmpty(category) && !"-1".equals(category)) { //-1代表全部分类
                requestJsonObject.put("category_id", category);
            }
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("location", location.toJson());
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("json===" + requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {

                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i(jo.toString());

                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray dataJsonArray = jo.optJSONArray("data");
                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
                            for (int i = 0; i < dataJsonArray.length(); i++) {
                                JSONObject jsonObject = dataJsonArray.getJSONObject(i);
                                SELLERINFO sellerinfo = SELLERINFO.fromJson(jsonObject);
                                sellerinfos.add(sellerinfo);
                            }
                        }
                        paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                        SellerModel.this.onMessageResponse(url, jo, responseStatus);
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

    public void getSellerCategory() {
        final String url = ProtocolConst.SELLER_CATEGORY;
        SESSION session = SESSION.getInstance();
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());

        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {

                pd.dismiss();
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i(jo.toString());

                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray dataJsonArray = jo.optJSONArray("data");
                        sellercategory.clear();
                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
                            for (int i = 0; i < dataJsonArray.length(); i++) {
                                JSONObject jsonObject = dataJsonArray.getJSONObject(i);
                                CATEGORY category = CATEGORY.fromJson(jsonObject);
                                sellercategory.add(category);
                            }
                            sellercategory.get(0).setIschecked(true);
                        }
                        SellerModel.this.onMessageResponse(url, jo, responseStatus);
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

    //获取收藏店铺列表
    public void getShopCollectList() {
        final String url = ProtocolConst.SELLER_COLLECT;
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(10);
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {

                pd.dismiss();
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i(jo.toString());

                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    sellercollects.clear();
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray dataJsonArray = jo.optJSONArray("data");

                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
                            sellercollects.clear();
                            for (int i = 0; i < dataJsonArray.length(); i++) {
                                JSONObject jsonObject = dataJsonArray.getJSONObject(i);
                                SELLERINFO sellerinfo = SELLERINFO.fromJson(jsonObject);
                                sellercollects.add(sellerinfo);
                            }
                        }
                        paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                        SellerModel.this.onMessageResponse(url, jo, responseStatus);
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

    public void getShopCollectListMore() {
        final String url = ProtocolConst.SELLER_COLLECT;
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(sellercollects.size() / 10 + 1);
        pagination.setCount(10);
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("jo=====" + requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {

                pd.dismiss();
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i(jo.toString());

                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray dataJsonArray = jo.optJSONArray("data");
                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
                            for (int i = 0; i < dataJsonArray.length(); i++) {
                                JSONObject jsonObject = dataJsonArray.getJSONObject(i);
                                SELLERINFO sellerinfo = SELLERINFO.fromJson(jsonObject);
                                sellercollects.add(sellerinfo);
                            }
                        }
                        paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                        SellerModel.this.onMessageResponse(url, jo, responseStatus);
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


    //收藏店铺
    public void sellerCollectCreate(String seller_id) {
        final String url = ProtocolConst.SELLER_COLLECTCREATE;
        SESSION session = SESSION.getInstance();
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("seller_id", Integer.parseInt(seller_id));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("=======SELLER_COLLECTCREATE传入参数=======" + requestJsonObject.toString());

        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {

                pd.dismiss();
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("=======SELLER_COLLECTCREATE返回参数=======" + jo.toString());
                    SellerModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    SellerModel.this.onMessageResponse(url, jo, responseStatus);
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

    //取消收藏店铺
    public void sellerCollectDelete(String seller_id) {
        final String url = ProtocolConst.SELLER_COLLECTDELETE;
        SESSION session = SESSION.getInstance();
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("seller_id", Integer.parseInt(seller_id));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("=======SELLER_COLLECTDELETE传入参数=======" + requestJsonObject.toString());

        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {

                pd.dismiss();
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("=======SELLER_COLLECTDELETE返回参数=======" + jo.toString());
                    SellerModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    SellerModel.this.onMessageResponse(url, jo, responseStatus);
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


    //店铺列表广告图
    public void sellerAdsense() {
        final String url = ProtocolConst.SELLER_HOMEDATA;
        SESSION session = SESSION.getInstance();
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===SELLER_HOMEDATA===" + requestJsonObject.toString());

        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {

                pd.dismiss();
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("===SELLER_HOMEDATA返回===" + jo.toString());
                    SellerModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    JSONObject dataJsonObject = jo.optJSONObject("data");
                    adsenseList.clear();
                    JSONArray adsenseArray = dataJsonObject.optJSONArray("adsense");
                    if (null != adsenseArray && adsenseArray.length() > 0) {
                        for (int i = 0; i < adsenseArray.length(); i++) {
                            adsenseList.add(ADSENSE.fromJson(adsenseArray.optJSONObject(i)));
                        }
                    }

                    SellerModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===SELLER_HOMEDATA返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }

    //店铺详情页数据
    public void getMerchantHomeData(String seller_id, String lat, String lng) {
        final String url = ProtocolConst.MERCHAT_HOMEDATA;
        SESSION session = SESSION.getInstance();
//        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        LOCATION location = new LOCATION();
        location.setLatitude(lat);
        location.setLongitude(lng);
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("seller_id", Integer.parseInt(seller_id));
            requestJsonObject.put("location", location.toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("=======MERCHAT_HOMEDATA传入参数=======" + requestJsonObject.toString());

        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {

                pd.dismiss();
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("=======MERCHAT_HOMEDATA返回参数=======" + jo.toString());
                    SellerModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    shopHomeData = SHOPDATA.fromJson(jo.optJSONObject("data"));
                    SellerModel.this.onMessageResponse(url, jo, responseStatus);
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
}
