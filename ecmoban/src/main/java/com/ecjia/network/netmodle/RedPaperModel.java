package com.ecjia.network.netmodle;

import android.content.Context;

import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.BONUS;
import com.ecjia.entity.responsemodel.BONUSINFO;
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

/**
 * Created by Administrator on 2015/11/26.
 */
public class RedPaperModel extends BaseModel {
    public BONUSINFO bonusinfo;

    public ArrayList<BONUS> bonuslist = new ArrayList<BONUS>();

    public RedPaperModel(Context context) {
        super(context);
    }

    public void getBonusValidate(String bonus_sn) {
        final String URL = ProtocolConst.BONUS_VALIDATE;
        final JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", SESSION.getInstance().toJson());
            requestJsonObject.put("token", SESSION.getInstance().sid);
            requestJsonObject.put("bonus_sn", bonus_sn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===bonus/validate传入===" + requestJsonObject.toString());

        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + URL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> objectResponseInfo) {

                JSONObject jo = null;
                try {
                    jo = new JSONObject(objectResponseInfo.result);
                    LG.i("===bonus/validate返回===" + jo.toString());
                    RedPaperModel.this.callback(jo);

                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    JSONObject dataJsonObject = jo.optJSONObject("data");
                    if (responseStatus.getSucceed() == 1) {
                        bonusinfo = BONUSINFO.fromJson(dataJsonObject);
                    }
                    RedPaperModel.this.onMessageResponse(URL, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===bonus/validate返回===" + objectResponseInfo.result);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    public void bindBonus(String bonus_sn) {
        final String URL = ProtocolConst.BONUS_BIND;
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", SESSION.getInstance().toJson());
            requestJsonObject.put("token", SESSION.getInstance().sid);
            requestJsonObject.put("bonus_sn", bonus_sn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===bonus/bind返回===" + requestJsonObject.toString());

        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + URL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                JSONObject jo = null;
                try {
                    jo = new JSONObject(objectResponseInfo.result);
                    LG.i("===bonus/bind返回===" + jo.toString());
                    RedPaperModel.this.callback(jo);

                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    RedPaperModel.this.onMessageResponse(URL, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===bonus/bind返回===" + objectResponseInfo.result);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    public void receiveRedpaper(String bonus_type_id) {
        final String url = ProtocolConst.REDPAPER_RECEIVE;
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("token", SESSION.getInstance().getSid());
            requestJsonObject.put("bonus_type_id", bonus_type_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===bonus/bind返回===" + requestJsonObject.toString());

        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                JSONObject jo = null;
                try {
                    jo = new JSONObject(objectResponseInfo.result);
                    LG.i("===领取红包返回===" + jo.toString());
                    RedPaperModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    RedPaperModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===领取红包返回===" + objectResponseInfo.result);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });


    }


    // 获取用户红包列表
    public void getRedPaperList(String bonus_type) {
        final String url = ProtocolConst.USER_BONUS;
        pd.show();
        SESSION session = SESSION.getInstance();

        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("bonus_type", bonus_type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===user/bonus传入===" + requestJsonObject.toString());

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {


            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {

                pd.dismiss();
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("===user/bonus返回===" + jo.toString());
                    RedPaperModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray dataJsonArray = jo.optJSONArray("data");

                        bonuslist.clear();
                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
                            for (int i = 0; i < dataJsonArray.length(); i++) {
                                JSONObject JsonObject = dataJsonArray.getJSONObject(i);
                                BONUS item = BONUS.fromJson(JsonObject);
                                bonuslist.add(item);
                            }
                        }
                    }
                    RedPaperModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===user/bonus返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });


    }

}
