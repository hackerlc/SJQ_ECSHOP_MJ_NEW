package com.ecjia.network.netmodle;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.TextUtils;

import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.EventKeywords;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.ACCESS_TOKEN;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.SIGNUPFILEDS;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.entity.responsemodel.USER;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecmoban.android.sijiqing.R;
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

import de.greenrobot.event.EventBus;

public class RegisterModel extends BaseModel {

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    public ArrayList<SIGNUPFILEDS> signupfiledslist = new ArrayList<SIGNUPFILEDS>();
    public MyProgressDialog pd;
    private USER user;
    private String token;

    public RegisterModel(Context context) {
        super(context);
        pd = MyProgressDialog.createDialog(context);
        Resources resources = AppConst.getResources(context);
        pd.setMessage(resources.getString(R.string.loading));
        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();
    }

    public STATUS responseStatus;

    public void signup(final String name, String password, String email, JSONArray field, String mobile, String invite_code) {
        final String url = ProtocolConst.SIGNUP;
        pd.show();

        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("name", name);
            requestJsonObject.put("password", password);
            requestJsonObject.put("email", email);
            requestJsonObject.put("mobile", mobile);
            requestJsonObject.put("invite_code", invite_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params = addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===user/signup传入===" + requestJsonObject.toString());

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
                    LG.i("===user/signup返回===" + jo.toString());
                    RegisterModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject data = jo.optJSONObject("data");
                        SESSION session = SESSION.fromJson(data.optJSONObject("session"));
                        user = USER.fromJson(data.optJSONObject("user"));
                        mApp.setUser(user);
                        editor.putString("uid", session.uid);
                        editor.putString("sid", session.sid);
                        editor.putString("local_uid", session.uid);
                        editor.putString("uname", name);
                        editor.putString("level", user.getRank_name());
                        editor.putString("email", user.getEmail());
                        editor.commit();
                        EventBus.getDefault().post(new MyEvent(EventKeywords.USER_LOGIN_SUCCESS));
                    }
                    RegisterModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===user/signup返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }


    public String isBind = "";
    //第三方绑定注册
    public void ThirdLoginRegister(final String username, String mobile, String invite_code
            , String openid, String code, String validate_code) {
        final String url = ProtocolConst.CONNECT_SIGNUP;
        pd.show();

        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("username", username);
//            requestJsonObject.put("password", password);
            requestJsonObject.put("mobile", mobile);
            requestJsonObject.put("invite_code", invite_code);
            requestJsonObject.put("openid", openid);
            requestJsonObject.put("code", code);
            requestJsonObject.put("validate_code", validate_code);
            requestJsonObject.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params = addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===connect/signup传入===" + requestJsonObject.toString());

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
                    LG.i("===connect/signup返回===" + jo.toString());
                    RegisterModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject data = jo.optJSONObject("data");
                        String token = data.optString("token");
                        isBind = data.optString("is_bind");
                        user = USER.fromJson(data.optJSONObject("user"));
                        //如果第三方登录后用户又去设定了app头像，那就取app头像
                        if (TextUtils.isEmpty(user.getAvatar_img())) {
                            if (AppConst.THIRD_QQ.equals(shared.getString("thirdWay", ""))) {
                                user.setAvatar_img(shared.getString("qq_log_img", ""));
                            } else if (AppConst.THIRD_WX.equals(shared.getString("thirdWay", ""))) {
                                user.setAvatar_img(shared.getString("wx_log_img", ""));
                            }
                        }
                        mApp.setUser(user);
                        SESSION session=SESSION.getInstance();
                        session.setUid(user.getId());
                        session.setSid(token);
                        editor.putString("uid", user.getId());
                        editor.putString("sid", token);
                        editor.putString("local_uid", user.getId());
                        editor.putString("uname", user.getName());
                        editor.putString("level", user.getRank_name());
                        editor.putString("email", user.getEmail());
                        editor.commit();
                        EventBus.getDefault().post(new MyEvent(EventKeywords.USER_LOGIN_SUCCESS));
                    }
                    RegisterModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===user/signup返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }


    public String bindCode = "";
    public void getCode(String mobileNo) {

        final String getCodeUrl = ProtocolConst.GETCODE;//获取验证码的接口
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("type", "mobile");
            requestJsonObject.put("value", mobileNo);
            requestJsonObject.put("token", token);
            requestJsonObject.put("come", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
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
                    RegisterModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject data = jo.optJSONObject("data");
                        if (jo.optJSONObject("data") == null) {
                            LG.e("注册信息返回值错误");
                        } else {
                            int registed = data.optInt("registered");

                            bindCode = data.optString("bind_code");
                            if (registed == 1) {//已注册
                                responseStatus.setArgI(1);
                            } else {//未注册
                                responseStatus.setArgI(0);
                            }
                        }
                    } else {
                        responseStatus.setArgI(2);
                    }
                    RegisterModel.this.onMessageResponse(getCodeUrl, jo, responseStatus);
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

    public void getShopToken() {
        final String url = ProtocolConst.SHOP_TOKEN;
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, new RequestParams(), new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("===shop/token返回===" + jo.toString());
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        ACCESS_TOKEN access_token = ACCESS_TOKEN.fromJson(jo.optJSONObject("data"));
                        token = access_token.getAccess_token();
                    }
                    RegisterModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===shop/token返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }

}
