package com.ecjia.network.netmodle;


import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;

import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.CONSULTION;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.PAGINATION;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
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

public class ConsultModel extends BaseModel {

    public PAGINATED paginated;

    public ArrayList<CONSULTION> consultionList = new ArrayList<CONSULTION>();

    public MyProgressDialog pd;

    public ConsultModel(Context context) {
        super(context);
        pd = MyProgressDialog.createDialog(context);
        Resources resources = AppConst.getResources(context);
        pd.setMessage(resources.getString(R.string.loading));
    }

    public void getConsultionList(String object_id, String object_type,  final Handler handler) {
        String url = ProtocolConst.CONSULTION_GET;
        pd.show();
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(10);

        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("pagination", pagination.toJson());
            if (object_id != null) {
                requestJsonObject.put("object_id", object_id);
            }
            requestJsonObject.put("object_type", object_type);


            LG.i("======获取传参=====" + requestJsonObject.toString());


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
                LG.i("获取聊天记录返回值==========" + arg0.result);
                try {
                    jo = new JSONObject(arg0.result);
                    ConsultModel.this.callback(jo);
                    LG.i(jo.toString());
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        LG.i("responseStatus.getSucceed() ===" + responseStatus.getSucceed());
                        JSONArray dataJsonArray = jo.optJSONArray("data");
                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
                            consultionList.clear();
                            for (int i = 0; i < dataJsonArray.length(); i++) {
                                JSONObject consultionJsonObject = dataJsonArray.getJSONObject(i);
                                CONSULTION consultion_Item = CONSULTION.fromJson(consultionJsonObject);
                                consultionList.add(consultion_Item);
                            }
                        }

                        paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                        Message msg = new Message();
                        msg.obj = ProtocolConst.CONSULTION_GET;
                        msg.what = responseStatus.getSucceed();
                        handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.obj = ProtocolConst.CONSULTION_GET;
                    msg.what = 0;
                    handler.sendMessage(msg);

                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });


    }

    public void getConsultionListMore(String object_id, String object_type,  final Handler handler) {
        String url = ProtocolConst.CONSULTION_GET;
        pd.show();
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(consultionList.size() / 10 + 1);
        pagination.setCount(10);

        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("pagination", pagination.toJson());
            if (object_id != null) {
                requestJsonObject.put("object_id", object_id);
            }
            requestJsonObject.put("object_type", object_type);


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
                LG.i("获取更多聊天记录返回值==========" + arg0.result);
                try {
                    jo = new JSONObject(arg0.result);
                    ConsultModel.this.callback(jo);
                    LG.i(jo.toString());
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray dataJsonArray = jo.optJSONArray("data");
                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
                            for (int i = 0; i < dataJsonArray.length(); i++) {
                                JSONObject consultionJsonObject = dataJsonArray.getJSONObject(i);
                                CONSULTION consultion_Item = CONSULTION.fromJson(consultionJsonObject);
                                consultionList.add(consultion_Item);
                                LG.i("====聊天的长度===" + consultionList.size());
                            }
                        }
                        paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                        Message msg = new Message();
                        msg.obj = ProtocolConst.CONSULTION_GET;
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.obj = ProtocolConst.CONSULTION_GET;
                    msg.what = 0;
                    handler.sendMessage(msg);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });


    }

    //创建评价(2.6.0)
    public void createConsultion(String object_id, String object_type, String user_name, String content, final Handler handler) {
        String url = ProtocolConst.CONSULTION_CREATE;
        SESSION session = SESSION.getInstance();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("session", session.toJson());
            if (object_id != null) {
                requestJsonObject.put("object_id", object_id);
            }
            requestJsonObject.put("object_type", object_type);
            requestJsonObject.put("user_name", user_name);
            requestJsonObject.put("content", content);

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

                JSONObject jo;

                LG.i("提交聊天内容后的返回值" + arg0.result);
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i(jo.toString());
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray dataJsonArray = jo.optJSONArray("data");
                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
                            for (int i = 0; i < dataJsonArray.length(); i++) {
                                JSONObject consultionJsonObject = dataJsonArray.getJSONObject(i);
                                CONSULTION consultion_Item = CONSULTION.fromJson(consultionJsonObject);
                            }
                        }
                        Message msg = new Message();
                        msg.obj = ProtocolConst.CONSULTION_CREATE;
                        msg.what = responseStatus.getSucceed();
                        handler.sendMessage(msg);
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
