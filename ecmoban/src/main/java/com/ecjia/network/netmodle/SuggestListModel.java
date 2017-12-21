package com.ecjia.network.netmodle;

import android.content.Context;

import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.PAGINATION;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.entity.responsemodel.SUGGEST;
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


public class SuggestListModel extends BaseModel {

    public static final String ACTION_TYPE_HOT = "hot";
    public static final String ACTION_TYPE_NEW = "new";
    public static final String ACTION_TYPE_BEST = "best";
    public static final String ACTION_TYPE_PROMOTION = "promotion";

    public ArrayList<SUGGEST> suggests = new ArrayList<SUGGEST>();

    public PAGINATED paginated;
    String pkName;

    public SuggestListModel(Context context) {
        super(context);
    }

    //获取每日推荐
    public void getSuggestList(String type) {
        final String url = ProtocolConst.GOODS_SUGGESTLIST;
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(8);
        SESSION session = SESSION.getInstance();
        HttpUtils http = new HttpUtils();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("action_type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===goods/suggestlist传入===" + requestJsonObject.toString());

        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                JSONObject jo;
                try {
                    jo = new JSONObject(objectResponseInfo.result);
                    LG.i("===goods/suggestlist返回===" + jo.toString());
                    callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                    if (responseStatus.getSucceed() == 1) {
                        suggests.clear();
                        JSONArray jsonArray = jo.optJSONArray("data");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                suggests.add(SUGGEST.fromJson(jsonArray.optJSONObject(i)));
                            }
                        }
                    }
                    onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===goods/suggestlist返回===" + objectResponseInfo.result);
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

    public void getSuggestListMore(String type) {
        final String url = ProtocolConst.GOODS_SUGGESTLIST;
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(suggests.size() / 8 + 1);
        pagination.setCount(8);
        SESSION session = SESSION.getInstance();
        HttpUtils http = new HttpUtils();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("action_type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===goods/suggestlist传入===" + requestJsonObject.toString());

        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                JSONObject jo;
                try {

                    jo = new JSONObject(objectResponseInfo.result);
                    LG.i("===goods/suggestlist返回===" + jo.toString());
                    SuggestListModel.this.callback(jo);

                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray jsonArray = jo.optJSONArray("data");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                suggests.add(SUGGEST.fromJson(jsonArray.optJSONObject(i)));
                            }
                        }
                    }
                    SuggestListModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===goods/suggestlist返回===" + objectResponseInfo.result);
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
