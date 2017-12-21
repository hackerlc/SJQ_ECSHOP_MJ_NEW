package com.ecjia.network.netmodle;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;

import com.ecjia.consts.ProtocolConst;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.entity.responsemodel.BRAND;
import com.ecjia.entity.responsemodel.CATEGORY;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.PAGINATION;
import com.ecjia.entity.responsemodel.PRICE_RANGE;
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
 * Created by Administrator on 2015/5/21.
 */
public class ShopDetialModel extends BaseModel {
    public ArrayList<BRAND> brandList = new ArrayList<BRAND>();
    public ArrayList<PRICE_RANGE> priceRangeArrayList = new ArrayList<PRICE_RANGE>();
    public ArrayList<CATEGORY> categoryArrayList = new ArrayList<CATEGORY>();

    public static String PRICE_DESC = "price_desc";
    public static String PRICE_ASC = "price_asc";
    public static String IS_HOT = "is_hot";
    public static String IS_NEW = "is_new";
    public ArrayList<SIMPLEGOODS> simplegoodsList = new ArrayList<SIMPLEGOODS>();
    public static final int PAGE_COUNT = 6;
    public PAGINATED paginated;
    private Context c;
    public MyProgressDialog pd;


    public ShopDetialModel(Context context) {
        super(context);
        this.c = context;
        pd = MyProgressDialog.createDialog(context);
        Resources resources = AppConst.getResources(context);
        pd.setMessage(resources.getString(R.string.loading));
    }

    /**
     * 获取商铺的商品列表
     *
     * @param filter
     * @param sellerid
     */
    public void fetchGoodList(FILTER filter, String sellerid) {
        final String url = ProtocolConst.SHOPDETIAL;
        pd.show();
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(PAGE_COUNT);

        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("seller_id", sellerid);
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("filter", filter.toJson());
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            LG.i("传入参数==" + requestJsonObject.toString());
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

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("data==" + arg0.result);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray jsonArray = jo.optJSONArray("data");
                        simplegoodsList.clear();
                        if (null != jsonArray && jsonArray.length() > 0) {
                            int size = jsonArray.length();
                            for (int i = 0; i < size; i++) {
                                simplegoodsList.add(SIMPLEGOODS.fromJson(jsonArray.optJSONObject(i)));
                            }
                        }
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

    public void fetchListMore(FILTER filter, String sellerid) {
        final String url = ProtocolConst.SHOPDETIAL;
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage((int) Math.ceil((double) simplegoodsList.size() * 1.0 / PAGE_COUNT) + 1);
        pagination.setCount(PAGE_COUNT);

        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("seller_id", sellerid);
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("filter", filter.toJson());
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            LG.i("传入参数==" + filter.toJson().toString());
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
                try {
                    jo = new JSONObject(arg0.result);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray jsonArray = jo.optJSONArray("data");
                        if (null != jsonArray && jsonArray.length() > 0) {
                            int size = jsonArray.length();
                            for (int i = 0; i < size; i++) {
                                simplegoodsList.add(SIMPLEGOODS.fromJson(jsonArray.optJSONObject(i)));
                            }
                        }
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


    public void getFilter(String seller_id, final Handler handler) {
        String url = ProtocolConst.SHOPFILTER;
        SESSION session = SESSION.getInstance();

        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("seller_id", seller_id);
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LG.i("url==" + AndroidManager.getURLAPI() + url);
        LG.i("传入参数==" + requestJsonObject.toString());
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                JSONObject jo;
                try {
                    LG.i("jo==" + objectResponseInfo.result);
                    jo = new JSONObject(objectResponseInfo.result);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray jsonArray = jo.optJSONArray("data");
                        categoryArrayList.clear();
                        if (jsonArray != null && jsonArray.length() > 0) {
                            int size = jsonArray.length();
                            for (int i = 0; i < size; i++) {
                                categoryArrayList.add(CATEGORY.fromJson(jsonArray.optJSONObject(i)));
                            }
                        }
                        Message msg = new Message();
                        msg.obj = ProtocolConst.SHOPFILTER;
                        msg.what = responseStatus.getSucceed();
                        handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }
}
