package com.ecjia.network.netmodle;

import android.content.Context;
import android.content.res.Resources;

import com.ecjia.consts.ProtocolConst;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.entity.responsemodel.INVITE_RECORD;
import com.ecjia.entity.responsemodel.INVITE_REWARD;
import com.ecjia.entity.responsemodel.INVITE_TOTAL;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.PAGINATION;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.entity.responsemodel.USER_INVITE;
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

public class InviteModel extends BaseModel {

    public PAGINATED paginated=new PAGINATED();
    private final int page=10;
    public ArrayList<INVITE_RECORD> invite_records = new ArrayList<INVITE_RECORD>();
    public ArrayList<INVITE_REWARD> invite_rewards = new ArrayList<INVITE_REWARD>();
    public MyProgressDialog pd;
    public INVITE_TOTAL inviteTotal=new INVITE_TOTAL();
    public USER_INVITE userInvite=new USER_INVITE();
    public String invite_code;
    private String token;

    public InviteModel(Context context) {
        super(context);
        pd = MyProgressDialog.createDialog(context);
        Resources resources = AppConst.getResources(context);
        pd.setMessage(resources.getString(R.string.loading));
    }

    public void getInviteReward() {
        final String url = ProtocolConst.INVITE_REWARD;
        pd.show();
        SESSION session = SESSION.getInstance();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("token", session.sid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===invite/reward传入===" + requestJsonObject.toString());

        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {


            @Override
            public void onFailure(HttpException arg0, String arg1) {
                showXutilsFailure();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                pd.dismiss();
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("===invite/reward返回===" + jo.toString());
                    InviteModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject dataJsonObject = jo.optJSONObject("data");
                        JSONObject dataJsonObj = dataJsonObject.optJSONObject("invite_total");
                        inviteTotal = INVITE_TOTAL.fromJson(dataJsonObj);
                        JSONArray dataJsonArray = dataJsonObject.optJSONArray("invite_record");

                        invite_rewards.clear();
                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
                            for (int i = 0; i < dataJsonArray.length(); i++) {
                                JSONObject JsonObject = dataJsonArray.getJSONObject(i);
                                INVITE_REWARD obj = INVITE_REWARD.fromJson(JsonObject);
                                invite_rewards.add(obj);
                            }
                        }
                    }
                    InviteModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===invite/reward返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }


    public void getInviteRecord(String date,boolean isfalse) {
        final String url = ProtocolConst.INVITE_RECORD;
        if (isfalse) {
            pd.show();
        }
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(page);

        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("date", date);
            requestJsonObject.put("pagination", pagination.toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===invite/record传入===" + requestJsonObject.toString());

        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {


            @Override
            public void onFailure(HttpException arg0, String arg1) {
                showXutilsFailure();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                pd.dismiss();
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("===invite/record返回===" + jo.toString());
                    InviteModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray dataJsonArray = jo.optJSONArray("data");

                        invite_records.clear();
                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
                            for (int i = 0; i < dataJsonArray.length(); i++) {
                                JSONObject orderJsonObject = dataJsonArray.getJSONObject(i);
                                INVITE_RECORD obj = INVITE_RECORD.fromJson(orderJsonObject);
                                invite_records.add(obj);
                            }
                        }
                        paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                    }
                    InviteModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===invite/record返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }


    public void getInviteRecordMore(String date) {
        final String url = ProtocolConst.INVITE_RECORD;
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(invite_records.size() / page + 1);
        pagination.setCount(page);

        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("date", date);
            requestJsonObject.put("pagination", pagination.toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===invite/record传入===" + requestJsonObject.toString());

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {


            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("===invite/record返回===" + jo.toString());
                    InviteModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray dataJsonArray = jo.optJSONArray("data");

                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
                            for (int i = 0; i < dataJsonArray.length(); i++) {
                                JSONObject orderJsonObject = dataJsonArray.getJSONObject(i);
                                INVITE_RECORD obj = INVITE_RECORD.fromJson(orderJsonObject);
                                invite_records.add(obj);
                            }
                        }

                        paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));

                    }
                    InviteModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===invite/record返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });

    }


    public void getInviteUser() {
        final String url = ProtocolConst.INVITE_USER;
        pd.show();
        SESSION session = SESSION.getInstance();
        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("token", session.sid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===invite/user传入===" + requestJsonObject.toString());

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
                    LG.i("===invite/user返回===" + jo.toString());
                    InviteModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject dataJsonObject = jo.optJSONObject("data");
                        userInvite=USER_INVITE.fromJson(dataJsonObject);
                    }
                    InviteModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===invite/user返回===" + arg0.result);
                }
            }
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }

    public void getInviteValidate(String mobile) {
        final String url = ProtocolConst.INVITE_VALIDATE;
        SESSION session = SESSION.getInstance();
        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("mobile", mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===invite/validate传入===" + requestJsonObject.toString());

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
                    LG.i("===invite/validate返回===" + jo.toString());
                    InviteModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject dataJsonObject = jo.optJSONObject("data");
                        invite_code = dataJsonObject.optString("invite_code");
                        userInvite=USER_INVITE.fromJson(dataJsonObject);
                    }
                    InviteModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===invite/validate返回===" + arg0.result);
                }
            }
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }



}
