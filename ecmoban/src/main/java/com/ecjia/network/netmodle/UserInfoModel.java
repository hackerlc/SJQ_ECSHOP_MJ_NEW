package com.ecjia.network.netmodle;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.Gravity;

import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.EventKeywords;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.consts.SharedPKeywords;
import com.ecjia.entity.responsemodel.BONUS;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.entity.responsemodel.USER;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.ecjia.widgets.ToastView;
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

public class UserInfoModel extends BaseModel {

    public USER user;

    public static final int RANK_LEVEL_NORMAL = 0;
    public static final int RANK_LEVEL_VIP = 1;
    private SharedPreferences.Editor editor;
    public static UserInfoModel instance;
    public ArrayList<BONUS> bonuslist = new ArrayList<BONUS>();
    public MyProgressDialog pd;

    public UserInfoModel(Context context) {
        super(context);
        pd = MyProgressDialog.createDialog(context);
        Resources resources = AppConst.getResources(context);
        pd.setMessage(resources.getString(R.string.loading));
        shared = context.getSharedPreferences(SharedPKeywords.SPUSER, 0);
        editor = shared.edit();
        instance = this;
    }

    private SharedPreferences shared;


    /**
     * 带进度的获取用户信息
     *
     * @param show
     */
    public void getUserInfo(final boolean show) {
        if (show) {
            pd.show();
        }
        final String url = ProtocolConst.USERINFO;
        SESSION session = SESSION.getInstance();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("获取用户信息" + requestJsonObject.toString());

        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                if (show) {
                    pd.dismiss();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                if (show) {
                    pd.dismiss();
                }
                JSONObject jo;
                try {
                    LG.i("userinfo==" + arg0.result);
                    jo = new JSONObject(arg0.result);
                    UserInfoModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        user = USER.fromJson(jo.optJSONObject("data"));
                        mApp.setUser(user);
                        if (shared.getBoolean("thirdLog", false)) {
                            if (TextUtils.isEmpty(user.getAvatar_img())) {
                                if (AppConst.THIRD_QQ.equals(shared.getString("thirdWay", ""))) {
                                    user.setAvatar_img(shared.getString("qq_log_img", ""));
                                } else if (AppConst.THIRD_WX.equals(shared.getString("thirdWay", ""))) {
                                    user.setAvatar_img(shared.getString("wx_log_img", ""));
                                }
                            }
                        }

                        LG.i("username==" + user.getName());
                        JSONArray array = jo.optJSONObject("data").optJSONArray("bonus_list");
                        bonuslist.clear();
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                bonuslist.add(BONUS.fromJson(array.optJSONObject(i)));
                            }
                        }

                        EventBus.getDefault().post(new MyEvent(EventKeywords.USER_LOGIN_SUCCESS));

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


    /**
     * 获取用户信息
     */
    public void getUserInfo() {
        getUserInfo(false);
    }

    //修改密码
    public void changePassword(String newPsw){
        changePassword("", newPsw);
    }

    public void changePassword(String oldPsd, String newPsw) {
        final String url = ProtocolConst.CHANGE_PASSWORD;
        pd.show();
        SESSION session = SESSION.getInstance();

        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("password", oldPsd);
            requestJsonObject.put("new_password", newPsw);
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
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
                    LG.i("jo========" + jo.toString());
                    UserInfoModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 0) {
                        ToastView toastView = new ToastView(mContext, responseStatus.getError_desc());
                        toastView.show();
                    }
                    onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void upDateUserInfo(String bt) {

        final String url = ProtocolConst.UPDATE_USERINFO;
        SESSION session = SESSION.getInstance();
        final JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("avatar_img", bt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                ToastView toast = new ToastView(mContext, mContext.getResources().getString(R.string.choosepay_network_problem));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                STATUS status = new STATUS();
                status.setSucceed(2);
                try {
                    onMessageResponse(url, null, status);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                JSONObject jo;
                try {
                    LG.i("上传图片的返回值==" + arg0.result);
                    jo = new JSONObject(arg0.result);
                    UserInfoModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject jsonObject = jo.optJSONObject("data");
                        user = USER.fromJson(jsonObject);
                        mApp.setUser(user);
                        JSONArray array = jsonObject.optJSONArray("bonus_list");
                        bonuslist.clear();
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                bonuslist.add(BONUS.fromJson(array.optJSONObject(i)));
                            }
                        }
                        onMessageResponse(url, jo, responseStatus);
                    } else {
                        responseStatus.setSucceed(2);
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


    public void signOut() {

        final String url = ProtocolConst.SIGNOUT;
        SESSION session = SESSION.getInstance();
        final JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("token", session.getSid());
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===user/signout传入===" + requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("===user/signout返回===" + jo.toString());
                    callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        mApp.setUser(null);
                        SESSION.getInstance().uid = "";
                        SESSION.getInstance().sid = "";
                        user = null;
                        editor.putString(SharedPKeywords.SPUSER_KUID, "");
                        editor.putString(SharedPKeywords.SPUSER_KSID, "");
                        editor.commit();
                        EventBus.getDefault().post(new MyEvent(EventKeywords.USER_SINOUT));
                        onMessageResponse(url, jo, responseStatus);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===user/info返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }

}
