package com.ecjia.network.netmodle;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.EventKeywords;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.entity.responsemodel.USER;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecjia.util.ProfilePhotoUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import de.greenrobot.event.EventBus;

public class LoginModel extends BaseModel {

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    public MyProgressDialog pd;
    private USER user;

    public LoginModel(Context context) {
        super(context);
        pd = MyProgressDialog.createDialog(context);
        pd.setMessage(res.getString(R.string.loading));
        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();
    }

    public STATUS responseStatus;

    public void login(final String name, String password) {
        final String url = ProtocolConst.SIGNIN;
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("name", name);
            requestJsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===user/signin传入===" + requestJsonObject.toString());

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
                    LG.i("===user/signin返回===" + jo.toString());
                    LoginModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        ProfilePhotoUtil.getInstance().clearBitmap();
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
                        EventBus.getDefault().post(new MyEvent("CART_UPDATE"));
                        EventBus.getDefault().post(new MyEvent(EventKeywords.USER_LOGIN_SUCCESS));
                    }
                    LoginModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===user/signin返回===" + arg0.result);
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }

    //第三方登录
    public void ThirdLogin(final String name, String id, String type) {
        final String url = ProtocolConst.CONNECT_SIGNIN;
        //记录第三方登录方式
        editor.putString("thirdWay", type);
        editor.commit();
        JSONObject requestJsonObject = new JSONObject();
        if (!pd.isShowing()) pd.show();
        try {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("username", name);
            requestJsonObject.put("openid", id);
            requestJsonObject.put("code", type);
        } catch (JSONException e) {

        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===connect/signin传入===" + requestJsonObject.toString());
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
                    LG.i("===connect/signin返回===" + jo.toString());
                    LoginModel.this.callback(jo);
                    STATUS responseStatus = STATUS.newFromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject data = jo.optJSONObject("data");
                        String token = data.optString("token");
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
                    LoginModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===connect/signin返回===" + arg0.result);
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }

    //第三方登录绑定
    public void ThirdLoginBind(String name,String pwd, String id, String type) {
        final String url = ProtocolConst.CONNECT_BIND;
        //记录第三方登录方式
        editor.putString("thirdWay", type);
        editor.commit();
        JSONObject requestJsonObject = new JSONObject();
        if (!pd.isShowing()) pd.show();
        try {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("username", name);
            requestJsonObject.put("password", pwd);
            requestJsonObject.put("openid", id);
            requestJsonObject.put("code", type);
        } catch (JSONException e) {

        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===connect/bind传入===" + requestJsonObject.toString());
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
                    LG.i("===connect/bind返回===" + jo.toString());
                    LoginModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject data = jo.optJSONObject("data");
                        String token = data.optString("token");
                        user = USER.fromJson(data.optJSONObject("user"));
                        if(shared.getBoolean("thirdLog",false)) {
                            //如果第三方登录后用户又去设定了app头像，那就取app头像
                            if (TextUtils.isEmpty(user.getAvatar_img())) {
                                if (AppConst.THIRD_QQ.equals(shared.getString("thirdWay", ""))) {
                                    user.setAvatar_img(shared.getString("qq_log_img", ""));
                                } else if (AppConst.THIRD_WX.equals(shared.getString("thirdWay", ""))) {
                                    user.setAvatar_img(shared.getString("wx_log_img", ""));
                                }
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
                    }
                    LoginModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===connect/bind返回===" + arg0.result);
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }
}
