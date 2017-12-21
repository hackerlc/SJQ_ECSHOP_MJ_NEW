package com.ecjia.network.netmodle;


import android.content.Context;

import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.LG;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class CodeModel extends BaseModel {

    private static CodeModel instance;
    private String type = "mobile";

    public static CodeModel getInstance() {
        return instance;
    }

    public CodeModel(Context context) {
        super(context);
        instance = this;
    }

    public void getCode(String mobileNo) {

        final String getCodeUrl = ProtocolConst.GETCODE;//获取验证码的接口
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("type", type);
            requestJsonObject.put("value", mobileNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());

        RequestCallBack<String> requestCallBack = new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("===user/userbind返回===" + jo.toString());
                    CodeModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject data = jo.optJSONObject("data");
                        if (jo.optJSONObject("data") == null) {
                            LG.e("注册信息返回值错误");
                        } else {
                            int registed = data.getInt("registered");

                            if (registed == 1) {//已注册
                                responseStatus.setArgI(1);
                            } else {//未注册
                                responseStatus.setArgI(0);
                            }
                        }
                    } else {
                        responseStatus.setArgI(2);
                    }
                    CodeModel.this.onMessageResponse(getCodeUrl, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===user/userbind返回===" + arg0.result);
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        };
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + getCodeUrl, params, requestCallBack);
    }

    public void checkCode(String mobileNo, String code) {

        final String checkCodeUrl = ProtocolConst.CHECKCODE;//验证
        final JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("type", type);
            requestJsonObject.put("value", mobileNo);
            requestJsonObject.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===validate/bind返回===" + requestJsonObject.toString());

        RequestCallBack<String> requestCallBack = new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("===validate/bind返回===" + jo.toString());
                    CodeModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    JSONObject data = jo.optJSONObject("data");
                    CodeModel.this.onMessageResponse(checkCodeUrl, jo,
                            responseStatus);

                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===validate/bind返回===" + arg0.result);
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        };

        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + checkCodeUrl, params, requestCallBack);
    }

}
