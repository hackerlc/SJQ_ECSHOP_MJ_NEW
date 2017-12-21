package com.ecjia.network.netmodle;

import android.content.Context;

import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.ACCESS_TOKEN;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.widgets.ToastView;
import com.ecjia.consts.AndroidManager;
import com.ecjia.util.LG;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/12/23.
 */
public class GetPasswordModel extends BaseModel {
    private Context context;

    public GetPasswordModel(Context context) {
        super(context);
        this.context = context;
        intance=this;
    }

    public ACCESS_TOKEN access_token;
    private static GetPasswordModel intance;
    private SESSION access_session=new SESSION();
    public static GetPasswordModel getIntacne(Context context) {

        return intance;
    }

    public void getShopToken() {
        final String url = ProtocolConst.SHOP_TOKEN;
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, new RequestParams(),
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        JSONObject jo;
                        try {
                            LG.e("shopToken==" + arg0.result);
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                access_token = ACCESS_TOKEN.fromJson(jo.optJSONObject("data"));
                                access_session.setSid(access_token.getAccess_token());
                                access_session.setUid(access_token.getExpires_in());
                            } else {
                                ToastView toastView = new ToastView(context, responseStatus.getError_desc());
                                toastView.show();
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

    public void getUser_forget_password(String type, String value) {
        final String url = ProtocolConst.USER_FORGET_PASSWORD;
        JSONObject requestJsonObject = new JSONObject();
        try {

            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("session", access_session.toJson());
            requestJsonObject.put("type", type);
            requestJsonObject.put("value", value);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams();
        LG.e("传入参数=="+requestJsonObject.toString());
        params.addBodyParameter("json", requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        JSONObject jo;
                        try {
                            LG.e("user_forget_password==" + arg0.result);
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {

                            } else {
                                ToastView toastView = new ToastView(context, responseStatus.getError_desc());
                                toastView.show();
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

    public void getValidate_forget_password(String type, String value, String code) {
        final String url = ProtocolConst.VALIDATE_FORGET_PASSWORD;
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("session", access_session.toJson());
            requestJsonObject.put("type", type);
            requestJsonObject.put("value", value);
            requestJsonObject.put("code", code);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams();
        LG.e("传入参数=="+requestJsonObject.toString());
        params.addBodyParameter("json", requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        JSONObject jo;
                        try {
                            LG.e("Validate_forget_password==" + arg0.result);
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {

                            } else {
                                ToastView toastView = new ToastView(context, responseStatus.getError_desc());
                                toastView.show();
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

    public void getUser_reset_password(String type, String value, String password) {
        final String url = ProtocolConst.USER_RESET_PASSWORD;
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("session", access_session.toJson());
            requestJsonObject.put("type", type);
            requestJsonObject.put("value", value);
            requestJsonObject.put("password", password);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams();
        LG.e("传入参数=="+requestJsonObject.toString());
        params.addBodyParameter("json", requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        JSONObject jo;
                        try {
                            LG.e("User_reset_password==" + arg0.result);
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {

                            } else {
                                ToastView toastView = new ToastView(context, responseStatus.getError_desc());
                                toastView.show();
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
}
