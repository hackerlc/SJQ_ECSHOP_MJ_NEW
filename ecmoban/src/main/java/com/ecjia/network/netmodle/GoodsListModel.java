package com.ecjia.network.netmodle;


import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;

import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.PAGINATION;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.SIMPLEGOODS;
import com.ecjia.entity.responsemodel.STATUS;
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

public class GoodsListModel extends BaseModel {

    public ArrayList<SIMPLEGOODS> simplegoodsList = new ArrayList<SIMPLEGOODS>();

    public static String PRICE_DESC = "price_desc";
    public static String PRICE_ASC = "price_asc";
    public static String IS_HOT = "is_hot";
    public static String IS_NEW = "is_new";
    public MyProgressDialog pd;
    public static final int PAGE_COUNT = 6;

    public GoodsListModel(Context context) {
        super(context);
        pd = MyProgressDialog.createDialog(context);
        Resources resources = AppConst.getResources(context);
        pd.setMessage(resources.getString(R.string.loading));
    }

    public PAGINATED paginated;

    public void fetchPreSearch(FILTER filter, final Handler handler, final boolean isfalse) {
        String url = ProtocolConst.SEARCH;
        if (isfalse) {
            pd.show();
        }
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(PAGE_COUNT);

        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("filter", filter.toJson());
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===goods/list传入===" + requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        if (isfalse) {
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                        }
                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray simpleGoodsJsonArray = jo.optJSONArray("data");
                                LG.i("===goods/list传入===" + jo.toString());
                                simplegoodsList.clear();
                                if (null != simpleGoodsJsonArray && simpleGoodsJsonArray.length() > 0) {
                                    simplegoodsList.clear();
                                    for (int i = 0; i < simpleGoodsJsonArray.length(); i++) {
                                        JSONObject simpleGoodsJsonObject = simpleGoodsJsonArray.getJSONObject(i);
                                        SIMPLEGOODS simplegoods = SIMPLEGOODS.fromJson(simpleGoodsJsonObject);
                                        simplegoodsList.add(simplegoods);
                                    }
                                }

                                Message msg = new Message();
                                msg.obj = ProtocolConst.SEARCH;
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

    public void fetchPreSearchMore(FILTER filter, final Handler handler) {
        String url = ProtocolConst.SEARCH;
        //  pd.show();
        PAGINATION pagination = new PAGINATION();
        SESSION session = SESSION.getInstance();
        pagination.setPage((int) Math.ceil((double) simplegoodsList.size() * 1.0 / PAGE_COUNT) + 1);
        pagination.setCount(PAGE_COUNT);

        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("filter", filter.toJson());
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("session", session.toJson());


        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        //   pd.dismiss();
                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray simpleGoodsJsonArray = jo.optJSONArray("data");

                                if (null != simpleGoodsJsonArray && simpleGoodsJsonArray.length() > 0) {
                                    for (int i = 0; i < simpleGoodsJsonArray.length(); i++) {
                                        JSONObject simpleGoodsJsonObject = simpleGoodsJsonArray.getJSONObject(i);
                                        SIMPLEGOODS simplegoods = SIMPLEGOODS.fromJson(simpleGoodsJsonObject);
                                        simplegoodsList.add(simplegoods);
                                    }
                                }

                                Message msg = new Message();
                                msg.obj = ProtocolConst.SEARCH;
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
