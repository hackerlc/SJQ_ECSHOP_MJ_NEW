package com.ecjia.network.netmodle;

import android.content.Context;
import android.content.res.Resources;

import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecjia.widgets.ToastView;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.PAGINATION;
import com.ecjia.entity.responsemodel.RECHARGE;
import com.ecjia.entity.responsemodel.RECHARGE_INFO;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.LG;
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

/**
 * Created by Administrator on 2015/3/12.
 */
public class RechargeModel extends BaseModel {
    Context c;
    public RECHARGE rechargeinfo;//余额充值所需信息
    public String RaplyReuslt;//提现申请返回内容
    public MyProgressDialog pd;
    public Resources resources;
    public ArrayList<RECHARGE_INFO> rechargelist = new ArrayList<RECHARGE_INFO>();//记录集合
    public PAGINATED paginated;
    public static final int PAGE_COUNT = 10;
    public String account_id;
    public String getpayment_id;

    public RechargeModel(Context context) {
        super(context);
        this.c = context;
        resources = AppConst.getResources(context);
        pd = MyProgressDialog.createDialog(context);
        pd.setMessage(resources.getString(R.string.loading));
    }

    //获取余额充值信息
    public void getRechargeInfo(String amount, String user_note, final String payment_id, String account_id) {
        final String url = ProtocolConst.USER_RECHARGE;
        SESSION session = SESSION.getInstance();
        pd.show();
        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("amount", amount);
            requestJsonObject.put("note", user_note);
            requestJsonObject.put("payment_id", payment_id);
            requestJsonObject.put("account_id", account_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===user/account/deposit传入===" + requestJsonObject.toString());

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
                    LG.i("===user/account/deposit返回===" + jo.toString());
                    RechargeModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject json = jo.optJSONObject("data");
                        JSONObject data = json.optJSONObject("payment");
                        RechargeModel.this.account_id = data.optString("account_id");
                        getpayment_id = data.optString("payment_id");

                    } else {
                        ToastView toast = new ToastView(c, responseStatus.getError_desc());
                        toast.show();
                    }

                    RechargeModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===user/account/deposit返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });


    }

    //提现申请
    public void accountRaply(String amount, String user_note) {
        final String url = ProtocolConst.USER_RAPLY;
        SESSION session = SESSION.getInstance();
        pd.show();
        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("amount", amount);
            requestJsonObject.put("note", user_note);
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===user/account/raply传入===" + requestJsonObject.toString());

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
                    LG.i("===user/account/raply返回===" + jo.toString());
                    RechargeModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        RaplyReuslt = jo.optString("data");
                    } else {
                        ToastView toast = new ToastView(c, responseStatus.getError_desc());
                        toast.show();
                    }

                    RechargeModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===user/account/raply返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });


    }

    //充值提现记录
    public void getAccountRecordList(String process_type) {
        final String url = ProtocolConst.USER_ACCOUNT_RECORD;
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(PAGE_COUNT);

        pd.show();
        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("type", process_type);
            requestJsonObject.put("pagination", pagination.toJson());
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===user/account/record传入===" + requestJsonObject.toString());

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
                    LG.i("===user/account/record返回===" + jo.toString());
                    paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                    RechargeModel.this.callback(jo);
                    STATUS responseStatus = STATUS.newFromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray array = jo.optJSONArray("data");
                        rechargelist.clear();
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject json = array.getJSONObject(i);
                                rechargelist.add(RECHARGE_INFO.fromJson(json));
                            }
                        }

                    } else {
                        ToastView toast = new ToastView(c, responseStatus.getError_desc());
                        toast.show();
                    }
                    RechargeModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===user/account/record返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }

    //更多充值提现记录
    public void getAccountRecordMore( String process_type) {
        final String url = ProtocolConst.USER_ACCOUNT_RECORD;
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(rechargelist.size() / 10 + 1);
        pagination.setCount(10);

        pd.show();
        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("process_type", process_type);
            requestJsonObject.put("pagination", pagination.toJson());
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===user/account/record传入===" + requestJsonObject.toString());

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
                    LG.i("===user/account/record返回===" + jo.toString());
                    paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                    RechargeModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray array = jo.optJSONArray("data");
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject json = array.getJSONObject(i);
                                rechargelist.add(RECHARGE_INFO.fromJson(json));
                            }
                        }
                    } else {
                        ToastView toast = new ToastView(c, responseStatus.getError_desc());
                        toast.show();
                    }
                    RechargeModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===user/account/record返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }


    //余额充值付款
    public void AccountCancel( String acc_id) {
        final String url = ProtocolConst.USER_ACCOUNT_CANCLE;
        SESSION session = SESSION.getInstance();
        if (!pd.isShowing()) pd.show();
        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("account_id", acc_id);
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===user/account/cancel传入===" + requestJsonObject.toString());

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
                    LG.i("===user/account/cancel返回===" + jo.toString());
                    RechargeModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                    } else {
                        ToastView toast = new ToastView(c, "取消失败！");
                        toast.show();
                    }
                    RechargeModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===user/account/cancel返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }

}
