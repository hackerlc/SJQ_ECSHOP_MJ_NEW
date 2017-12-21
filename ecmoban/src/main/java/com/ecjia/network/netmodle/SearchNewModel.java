package com.ecjia.network.netmodle;

import android.content.Context;

import com.ecjia.consts.ProtocolConst;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecjia.consts.AndroidManager;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.PAGINATION;
import com.ecjia.entity.responsemodel.SEARCHRESULT;
import com.ecjia.entity.responsemodel.SELLERINFO;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.SIMPLEGOODS;
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
 * Created by Administrator on 2015/8/31.
 * 新的搜索页（店铺/商品）
 */
public class SearchNewModel extends BaseModel {
    public ArrayList<SIMPLEGOODS> searchGood = new ArrayList<SIMPLEGOODS>();
    public ArrayList<SELLERINFO> sellerlist = new ArrayList<SELLERINFO>();
    public MyProgressDialog pd;
    public PAGINATED paginated;
    private Context context;
    public int type = 0;

    public SearchNewModel(Context context) {
        super(context);
        pd = MyProgressDialog.createDialog(context);
        pd.setMessage(res.getString(R.string.loading));
        this.context = context;
    }

    public void getSearchALLList(String keywords) {
        final String url = ProtocolConst.SEARCH_NEW;
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(10);
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("keywords", keywords);
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());

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
                    LG.i(jo.toString());

                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        SEARCHRESULT searchresult = SEARCHRESULT.fromJson(jo.optJSONObject("data"));

                        JSONObject Jsonobj = jo.optJSONObject("data");

                        if (null == Jsonobj) {

                        } else {
                            JSONArray dataJsonArray = Jsonobj.optJSONArray("result");

                            if ("seller".equals(searchresult.getType())) {
                                type = 1;
                                sellerlist.clear();
                                if (null != dataJsonArray && dataJsonArray.length() > 0) {
                                    for (int i = 0; i < dataJsonArray.length(); i++) {
                                        JSONObject jsonObject = dataJsonArray.getJSONObject(i);
                                        SELLERINFO sellerinfo = SELLERINFO.fromJson(jsonObject);
                                        sellerlist.add(sellerinfo);
                                    }
                                }
                            } else if ("goods".equals(searchresult.getType())) {
                                type = 2;
                                searchGood.clear();
                                if (null != dataJsonArray && dataJsonArray.length() > 0) {
                                    for (int i = 0; i < dataJsonArray.length(); i++) {
                                        JSONObject jsonObject = dataJsonArray.getJSONObject(i);
                                        SIMPLEGOODS simplegoods = SIMPLEGOODS.fromJson(jsonObject);
                                        searchGood.add(simplegoods);
                                    }
                                }
                            }
                        }

                        paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                        SearchNewModel.this.onMessageResponse(url, jo, responseStatus);
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

    public void getSearchALLListMore(String keywords) {
        final String url = ProtocolConst.SEARCH_NEW;
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        if (type == 1) {
            pagination.setPage(sellerlist.size() / 10 + 1);
        } else if (type == 2) {
            pagination.setPage(searchGood.size() / 10 + 1);
        }
        pagination.setCount(10);
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("keywords", keywords);
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            // TODO: handle exception
        }


        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("json===" + requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {

                        pd.dismiss();
                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            LG.i(jo.toString());

                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                SEARCHRESULT searchresult = SEARCHRESULT.fromJson(jo.optJSONObject("data"));

                                JSONObject Jsonobj = jo.optJSONObject("data");
                                if (null == Jsonobj) {

                                } else {
                                    JSONArray dataJsonArray = Jsonobj.optJSONArray("result");

                                    if ("seller".equals(searchresult.getType())) {
                                        type = 1;
                                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
                                            for (int i = 0; i < dataJsonArray.length(); i++) {
                                                JSONObject jsonObject = dataJsonArray.getJSONObject(i);
                                                SELLERINFO sellerinfo = SELLERINFO.fromJson(jsonObject);
                                                sellerlist.add(sellerinfo);
                                            }
                                        }
                                    } else if ("goods".equals(searchresult.getType())) {
                                        type = 2;
                                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
                                            for (int i = 0; i < dataJsonArray.length(); i++) {
                                                JSONObject jsonObject = dataJsonArray.getJSONObject(i);
                                                SIMPLEGOODS simplegoods = SIMPLEGOODS.fromJson(jsonObject);
                                                searchGood.add(simplegoods);
                                            }
                                        }
                                    }
                                }
                                paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                                SearchNewModel.this.onMessageResponse(url, jo, responseStatus);
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

    public void getSearchSellerList(String keywords) {
        final String url = ProtocolConst.SEARCH_SELLER;
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(10);
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("keywords", keywords);
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
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

                        pd.dismiss();
                        JSONObject jo;
                        try {
                            LG.i("jo==" + arg0.result);
                            jo = new JSONObject(arg0.result);

                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray dataJsonArray = jo.optJSONArray("data");
                                sellerlist.clear();
                                if (null != dataJsonArray && dataJsonArray.length() > 0) {
                                    for (int i = 0; i < dataJsonArray.length(); i++) {
                                        JSONObject jsonObject = dataJsonArray.getJSONObject(i);
                                        SELLERINFO sellerinfo = SELLERINFO.fromJson(jsonObject);
                                        sellerlist.add(sellerinfo);
                                    }
                                }

                                paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                                SearchNewModel.this.onMessageResponse(url, jo, responseStatus);
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

    public void getSearchSellerListMore(String keywords) {
        final String url = ProtocolConst.SEARCH_SELLER;
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(sellerlist.size() / 10 + 1);
        pagination.setCount(10);
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("keywords", keywords);
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            // TODO: handle exception
        }


        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("json===" + requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {

                        pd.dismiss();
                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            LG.i(jo.toString());

                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray dataJsonArray = jo.optJSONArray("data");
                                if (null != dataJsonArray && dataJsonArray.length() > 0) {
                                    for (int i = 0; i < dataJsonArray.length(); i++) {
                                        JSONObject jsonObject = dataJsonArray.getJSONObject(i);
                                        SELLERINFO sellerinfo = SELLERINFO.fromJson(jsonObject);
                                        sellerlist.add(sellerinfo);
                                    }
                                }
                                paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                                SearchNewModel.this.onMessageResponse(url, jo, responseStatus);
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
