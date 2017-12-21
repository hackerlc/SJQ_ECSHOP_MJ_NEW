package com.ecjia.network.netmodle;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.GOODS;
import com.ecjia.entity.responsemodel.GOODS_ACTIVE;
import com.ecjia.entity.responsemodel.MERCHANTINFO;
import com.ecjia.entity.responsemodel.PHOTO;
import com.ecjia.entity.responsemodel.RELATED_GOOD;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.LG;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecmoban.android.sijiqing.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * 类名介绍：商品详情modle
 * Created by sun
 * Created time 2017-02-13.
 */
public class GoodsDetailModel extends BaseModel {
    public ArrayList<PHOTO> photoList = new ArrayList<PHOTO>();
    public int goodId;
    public GOODS goodDetail = new GOODS();
    public MyProgressDialog pd;
    public List<GOODS> goodDetaillist = new ArrayList<GOODS>();
    public MERCHANTINFO merchantinfo = null;//店铺信息
    public String seller_id, server_desc;
    public Boolean is_warehouse = false;//是否是仓库模式
    public ArrayList<GOODS_ACTIVE> activelist = new ArrayList<GOODS_ACTIVE>();
    public String rec_id;


    public GoodsDetailModel(Context context) {
        super(context);
        pd = MyProgressDialog.createDialog(context);
        Resources resources = AppConst.getResources(context);
        pd.setMessage(resources.getString(R.string.loading));
    }
    //猜你喜欢数据填充
    public ArrayList<RELATED_GOOD> related_list = new ArrayList<RELATED_GOOD>();

    public void fetchGoodDetail(String goodId, String area_id, String object_id, String rec_type, boolean isfalse) {
        final String url = ProtocolConst.GOODSDETAIL;
        if (isfalse) {
            pd.show();
        }
        SESSION session = SESSION.getInstance();
        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("goods_id", goodId);
            requestJsonObject.put("area_id", area_id);
            requestJsonObject.put("object_id", object_id);
            requestJsonObject.put("rec_type", rec_type);
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===gooddetail传入===" + requestJsonObject.toString());
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
                    LG.i("gooddetail===" + jo.toString());
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {

                        JSONObject dataJSONObject = jo.optJSONObject("data");

                        if (null != dataJSONObject) {
                            GoodsDetailModel.this.goodDetail = GOODS.fromJson(dataJSONObject);
//                            GoodsDetailModel.this.goodDetail = JsonUtil.getObj(jo.getString("data"),GOODS.class);
                            seller_id = dataJSONObject.optString("seller_id");
                            server_desc = dataJSONObject.optString("server_desc");
                            is_warehouse = dataJSONObject.optBoolean("is_warehouse");
                            JSONArray array = dataJSONObject.optJSONArray("related_goods");
                            related_list.clear();
                            if (null != array && array.length() > 0)
                                for (int i = 0; i < array.length(); i++) {
                                    related_list.add(RELATED_GOOD.fromJson(array.optJSONObject(i)));
                                }
                            JSONArray favarray = dataJSONObject.optJSONArray("favourable_list");
                            activelist.clear();
                            if (favarray != null && favarray.length() > 0) {
                                int size = favarray.length();
                                for (int i = 0; i < size; i++) {
                                    activelist.add(GOODS_ACTIVE.fromJson(favarray.optJSONObject(i)));
                                }
                            }

                            if (!TextUtils.isEmpty(seller_id) && !"0".equals(seller_id)) {
                                if (null != dataJSONObject.optJSONObject("merchant_info")) {
                                    merchantinfo = MERCHANTINFO.fromJson(dataJSONObject.optJSONObject("merchant_info"));
                                }
                            }
                            goodDetaillist.add(goodDetail);
                        }
                    }
                    onMessageResponse(url, jo, responseStatus);
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

    public void collectCreate(String goodId) {
        final String url = ProtocolConst.COLLECTION_CREATE;
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        SESSION session = SESSION.getInstance();

        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("goods_id", goodId);
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===COLLECTION_CREATE传入===" + requestJsonObject.toString());
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
                    LG.i("===COLLECTION_CREATE返回===" + jo.toString());
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        onMessageResponse(url, jo, responseStatus);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===COLLECTION_CREATE返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });

    }

    //取消收藏
    public void collectDelete(String goodId) {
        final String url = ProtocolConst.COLLECTION_DELETE;
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        SESSION session = SESSION.getInstance();

        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("goods_id", goodId);
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {

        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===COLLECTION_DELETE传入===" + requestJsonObject.toString());
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
                    LG.i("===COLLECTION_DELETE返回===" + jo.toString());
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        onMessageResponse(url, jo, responseStatus);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===COLLECTION_DELETE返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });

    }


    public String goodsWeb;

    public void goodsDesc(String goodId) {
        final String url = ProtocolConst.GOODSDESC;
        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("goods_id", goodId);
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {

        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("jo===goodsDesc::" + jo.toString());
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        goodsWeb = jo.optString("data");
                        onMessageResponse(url, jo, responseStatus);
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

}
