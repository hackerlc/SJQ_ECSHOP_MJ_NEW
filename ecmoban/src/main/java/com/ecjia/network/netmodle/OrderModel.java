package com.ecjia.network.netmodle;

import android.content.Context;
import android.content.res.Resources;

import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.EXPRESS;
import com.ecjia.entity.responsemodel.GOODORDER;
import com.ecjia.entity.responsemodel.GOODRETURNORDER;
import com.ecjia.entity.responsemodel.KUAIDI;
import com.ecjia.entity.responsemodel.ORDERDETAIL;
import com.ecjia.entity.responsemodel.ORDER_GOODS_LIST;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.PAGINATION;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.LG;
import com.ecjia.util.common.JsonUtil;
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

public class OrderModel extends BaseModel {

    public PAGINATED paginated;
    public ArrayList<GOODORDER> order_list = new ArrayList<GOODORDER>();
    public ArrayList<GOODRETURNORDER> order_return_list = new ArrayList<GOODRETURNORDER>();
    public ArrayList<EXPRESS> express_list = new ArrayList<EXPRESS>();
    public ArrayList<KUAIDI> kuaidilist = new ArrayList<KUAIDI>();
    public MyProgressDialog pd;
    public JSONObject myjson, payjson;
    public String pay_code;

    public String pay_upmp_tn;//银联支付参数

    public String subject, partner, seller_id, order_amount, notify_url, pay_order_sn, pay_online, pay_status, user_money;

    public String error_message;

    public String pay_name;

    public ORDERDETAIL orderdetail;

    public ArrayList<ORDER_GOODS_LIST> goods_list = new ArrayList<ORDER_GOODS_LIST>();

    public OrderModel(Context context) {
        super(context);
        pd = MyProgressDialog.createDialog(context);
        Resources resources = AppConst.getResources(context);
        pd.setMessage(resources.getString(R.string.loading));
    }

    public void getOrder(String type, String keywords, boolean isfalse) {
        final String url = ProtocolConst.ORDER_LIST;
        if (isfalse) {
            pd.show();
        }
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(8);

        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("keywords", keywords);
            requestJsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===order/list传入===" + requestJsonObject.toString());

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
                    LG.i("===order/list返回===" + jo.toString());
                    OrderModel.this.callback(jo);
                    myjson = jo;
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {

//                        JSONArray dataJsonArray = jo.optJSONArray("data");

                        order_list.clear();
                        order_list.addAll(JsonUtil.getListObj(jo.getString("data"),GOODORDER.class));
//                        order_list.addAll(GsonUtils.getInstance().parseJsonArray(jo.getString("data"),GOODORDER.class));
//                        L.d(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>",jo.getString("data").toString());
//                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
//                            for (int i = 0; i < dataJsonArray.length(); i++) {
//                                JSONObject orderJsonObject = dataJsonArray.getJSONObject(i);
//                                GOODORDER order_list_Item = GOODORDER.fromJson(orderJsonObject);
//                                order_list.add(order_list_Item);
//                            }
//                        }
                        paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                    }
                    OrderModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===order/list返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }


    public void getOrderMore(String type,String keywords) {
        final String url = ProtocolConst.ORDER_LIST;
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(order_list.size() / 8 + 1);
        pagination.setCount(8);

        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("type", type);
            requestJsonObject.put("keywords", keywords);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===order/list传入===" + requestJsonObject.toString());

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
                    LG.i("===order/list返回===" + jo.toString());
                    OrderModel.this.callback(jo);
                    myjson = jo;
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray dataJsonArray = jo.optJSONArray("data");

                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
                            for (int i = 0; i < dataJsonArray.length(); i++) {
                                JSONObject orderJsonObject = dataJsonArray.getJSONObject(i);
                                GOODORDER order_list_Item = GOODORDER.fromJson(orderJsonObject);
                                order_list.add(order_list_Item);
                            }
                        }

                        paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));

                    }
                    OrderModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===order/list返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });

    }

    //获取退换货订单列表开始
    public void getRejectedOrder( boolean isfalse) {
        final String url = ProtocolConst.ORDER_RETURN_LIST;
        if (isfalse) {
            pd.show();
        }
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(8);

        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("pagination", pagination.toJson());
//            requestJsonObject.put("keywords", keywords);
//            requestJsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===order/list传入===" + requestJsonObject.toString());

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
                    LG.i("===order/list返回===" + jo.toString());
                    OrderModel.this.callback(jo);
                    myjson = jo;
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
//                        JSONArray dataJsonArray = jo.optJSONArray("data");
                        order_return_list.clear();
                        if(jo.getString("data")==null){
                            order_return_list.addAll(JsonUtil.getListObj("[]",GOODRETURNORDER.class));
                        }else{
                            order_return_list.addAll(JsonUtil.getListObj(jo.getString("data"),GOODRETURNORDER.class));
                        }

//                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
//                            for (int i = 0; i < dataJsonArray.length(); i++) {
//                                JSONObject orderJsonObject = dataJsonArray.getJSONObject(i);
//                                GOODORDER order_list_Item = GOODORDER.fromJson(orderJsonObject);
//                                order_list.add(order_list_Item);
//                            }
//                        }
                        paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                    }
                    OrderModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===order/list返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }


    public void getRejectedOrderMore() {
        final String url = ProtocolConst.ORDER_RETURN_LIST;
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(order_list.size() / 8 + 1);
        pagination.setCount(8);

        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("pagination", pagination.toJson());
//            requestJsonObject.put("type", type);
//            requestJsonObject.put("keywords", keywords);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===order/list传入===" + requestJsonObject.toString());

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
                    LG.i("===order/list返回===" + jo.toString());
                    OrderModel.this.callback(jo);
                    myjson = jo;
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        order_return_list.clear();
                        order_return_list.addAll(JsonUtil.getListObj(jo.getString("data"),GOODRETURNORDER.class));
//                        JSONArray dataJsonArray = jo.optJSONArray("data");
//
//                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
//                            for (int i = 0; i < dataJsonArray.length(); i++) {
//                                JSONObject orderJsonObject = dataJsonArray.getJSONObject(i);
//                                GOODORDER order_list_Item = GOODORDER.fromJson(orderJsonObject);
//                                order_list.add(order_list_Item);
//                            }
//                        }
                        paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));

                    }
                    OrderModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===order/list返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });

    }

    //获取退换货订单列表结束


    public String order_id;

    public void orderPay(String order_id) {
        this.order_id = order_id;
        final String url = ProtocolConst.ORDER_PAY;
        pd.show();
        SESSION session = SESSION.getInstance();
        final JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("order_id", order_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===order/pay传入===" + requestJsonObject.toString());

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {


            @Override
            public void onFailure(HttpException arg0, String arg1) {
                LG.e("支付接口失败");
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                pd.dismiss();
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("===order/pay返回===" + jo.toString());
                    OrderModel.this.callback(jo);
                    payjson = jo;
                    STATUS responseStatus = STATUS.newFromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject dataJsonObject = jo.optJSONObject("data");
                        JSONObject payment = dataJsonObject.optJSONObject("payment");
                        pay_name = payment.optString("pay_name");
                        pay_code = payment.optString("pay_code");
                        if (AppConst.ALIPAY_MOBILE.equals(pay_code)) {
                            subject = payment.optString("subject");
                            partner = payment.optString("partner");
                            order_amount = payment.optString("order_amount");
                            seller_id = payment.optString("seller_id");
                            notify_url = payment.optString("notify_url");
                            pay_order_sn = payment.optString("pay_order_sn");
                        } else if (AppConst.UPPAY.equals(pay_code)) {
                            pay_upmp_tn = payment.optString("pay_upmp_tn");
                        } else if (AppConst.BANK.equals(pay_code)) {
                            pay_online = payment.optString("pay_online");
                        } else if (AppConst.BALANCE.equals(pay_code)) {
                            pay_status = payment.optString("pay_status");
                            error_message = payment.optString("error_message");
                            user_money = payment.optString("user_money");
                        } else if (AppConst.WXPAY.equals(pay_code)) {
                            subject = payment.optString("subject");
                            order_amount = payment.optString("order_amount");
                            notify_url = payment.optString("notify_url");
                            pay_order_sn = payment.optString("pay_order_sn");
                        }
                        pay_online = payment.optString("pay_online");
                    }
                    OrderModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===order/pay返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });

    }

    public String shippingname, shipping_number, status;

    public void getOrderDetail(String order_id) {
        this.order_id = order_id;
        final String url = ProtocolConst.ORDER_DETAIL;
        pd.show();
        SESSION session = SESSION.getInstance();
        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("order_id", order_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===order/detail传入===" + requestJsonObject.toString());

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
                    LG.i("===order/detail返回===" + jo.toString());
                    OrderModel.this.callback(jo);

                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject dataJsonObject = jo.optJSONObject("data");
                        shippingname = dataJsonObject.optString("shipping_name");
                        shipping_number = dataJsonObject.optString("invoice_no");
                        JSONArray array = dataJsonObject.getJSONArray("goods_list");
                        orderdetail = ORDERDETAIL.fromJson(dataJsonObject);
                        goods_list.clear();
                        for (int i = 0; i < array.length(); i++) {
                            goods_list.add(ORDER_GOODS_LIST.fromJson(array.getJSONObject(i)));
                        }
                    }
                    OrderModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===order/detail返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });


    }

    public void orderCancle(String order_id) {
        this.order_id = order_id;
        final String url = ProtocolConst.ORDER_CANCLE;
        SESSION session = SESSION.getInstance();
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("order_id", order_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===order/cancel传入===" + requestJsonObject.toString());
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
                    LG.i("===order/cancel返回===" + jo.toString());
                    OrderModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    OrderModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===order/cancel返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }


    public void affirmReceived(String order_id) {
        this.order_id = order_id;
        final String url = ProtocolConst.AFFIRMRECEIVED;
        SESSION session = SESSION.getInstance();
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("order_id", order_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===order/affirmReceived传入===" + requestJsonObject.toString());
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
                    LG.i("===order/affirmReceived返回===" + jo.toString());
                    OrderModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    OrderModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===order/affirmReceived返回===" + arg0.result);
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }

    public void UpdateOrder(String order_id, String pay_id) {
        this.order_id = order_id;
        final String url = ProtocolConst.ORDER_UPDATE;
        SESSION session = SESSION.getInstance();
        pd.show();
        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("order_id", order_id);
            requestJsonObject.put("pay_id", pay_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===order/update传入===" + requestJsonObject.toString());
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
                    LG.i("===order/update返回===" + jo.toString());
                    OrderModel.this.callback(jo);

                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    OrderModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===order/update返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }

    /**
     * 快递查询
     */
    public void getExpressInfo(String shipping_name,String invoice_no) {
        final String url = ProtocolConst.ORDER_EXPRESS;
        SESSION session = SESSION.getInstance();
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("shipping_name", shipping_name);
            requestJsonObject.put("invoice_no", invoice_no);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===order/express传入===" + requestJsonObject.toString());
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
                    LG.i("===order/express返回===" + jo.toString());
                    OrderModel.this.callback(jo);

                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject json = jo.optJSONObject("data");
                        if (json.optInt("status") == 1) {
                            status = "签收成功";
                        } else if (json.optInt("status") == 0) {
                            status = "未签收";
                        }
                        JSONArray array = json.optJSONArray("content");
                        shippingname = json.optString("shipping_name");
                        shipping_number = json.optString("shipping_number");
                        express_list.clear();
                        if (null != array && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                express_list.add(EXPRESS.fromJson(array.optJSONObject(i)));
                            }
                        }
                    } else {
                        ToastView toast = new ToastView(mContext, responseStatus.getError_desc());
                        toast.show();
                    }
                    OrderModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===order/express返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }


}
