package com.ecjia.network.netmodle;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.content.res.Resources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.ecjia.consts.ProtocolConst;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.util.LG;
import com.ecjia.entity.responsemodel.*;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import gear.yc.com.gearlibrary.utils.ToastUtil;

public class ShoppingCartModel extends BaseModel {
    private SharedPreferences shared;

    public TOTAL total;
    public int goods_num;
    public int goodId;
    // 结算（提交订单前的订单预览）
    public ADDRESS address;
    public ArrayList<GOODS_LIST> balance_goods_list = new ArrayList<GOODS_LIST>();
    public ArrayList<SHIPPING> shipping_list = new ArrayList<SHIPPING>();

    public String orderInfoJsonString;

    public String bonusinfo, discount_formated, discount_fee, allow_can_invoice;


    public int all_use_integral, your_integral, order_max_integral;
    private static ShoppingCartModel instance;
    public ArrayList<GOODS_LIST> cachelist = new ArrayList<GOODS_LIST>();//缓存已选中的goodid
    public String rec_id;
    private int goodsNum;

    public static ShoppingCartModel getInstance() {
        return instance;
    }

    public ArrayList<NEWGOODITEM> newcartList = new ArrayList<NEWGOODITEM>();

    public ArrayList<PAYMENT> payment_list = new ArrayList<PAYMENT>();

    public ArrayList<PAYMENT> onlinepaymentlist = new ArrayList<PAYMENT>();

    public ArrayList<PAYMENT> uplinepaymentlist = new ArrayList<PAYMENT>();

    public ShoppingCartModel() {
        super();
    }

    public MyProgressDialog pd;

    String pkName;

    public String rootpath;

    public ShoppingCartModel(Context context) {
        super(context);
        pkName = mContext.getPackageName();
        rootpath = context.getCacheDir() + "/ECJia/cache";
        readcartGoodsDataCache();//读取缓存
        pd = MyProgressDialog.createDialog(context);
        Resources resources = AppConst.getResources(context);
        pd.setMessage(resources.getString(R.string.loading));
        shared = context.getSharedPreferences("userInfo", 0);
        instance = this;
    }

    public JSONObject flowdownjson, Integraljson, redjson, listjson;

    // 获取购物车列表
    public void cartList(boolean isfalse) {

        final String url = ProtocolConst.CARTLIST;
        SESSION session = SESSION.getInstance();
        if (isfalse) {
            pd.show();
        }

        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LG.i("传入参数==" + requestJsonObject.toString());
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                LG.i("返回值====" + arg0.result);
                pd.dismiss();
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    ShoppingCartModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));

                    if (responseStatus.getSucceed() == 1) {

                        listjson = jo;
                        JSONObject dataJsonObject = jo.optJSONObject("data");

                        total = TOTAL.fromJson(dataJsonObject.optJSONObject("total"));
                        JSONArray dataJsonArray = dataJsonObject.optJSONArray("cart_list");
                        newcartList.clear();
                        ShoppingCartModel.this.goods_num = 0;
                        if (null != dataJsonArray && dataJsonArray.length() > 0) {

                            for (int i = 0; i < dataJsonArray.length(); i++) {
                                JSONObject goodsJsonObject = dataJsonArray.getJSONObject(i);
                                NEWGOODITEM goods_list_Item = NEWGOODITEM.fromJson(goodsJsonObject);
                                newcartList.add(goods_list_Item);
                            }
                            int size = newcartList.size();
                            if (size > 0) {
                                for (int i = 0; i < size; i++) {
                                    ArrayList<GOODS_LIST> goods_list = newcartList.get(i).getGoodslist();
                                    for (int j = 0; j < goods_list.size(); j++) {
                                        GOODS_LIST good = goods_list.get(j);
                                        ShoppingCartModel.this.goods_num += Integer.valueOf(good.getGoods_number());
                                        if (cachelist.size() > 0) {
                                            for (int k = 0; k < cachelist.size(); k++) {
                                                if (good.getGoods_id() == cachelist.get(k).getGoods_id()) {
                                                    newcartList.get(i).getGoodslist().get(j).setIsCheckedbuy(false);
                                                    newcartList.get(i).setIsCheckedbuy(false);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        mApp.setGoodsNum(goods_num);
                        onMessageResponse(url, jo, responseStatus);
                    }
                    ShoppingCartModel.this.onMessageResponse(url, jo, responseStatus);
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

    public void readcartGoodsDataCache() {

        String path = rootpath + "/" + pkName + "/cartGoods.dat";
        File f1 = new File(path);
        if (f1.exists()) {
            try {
                InputStream is = new FileInputStream(f1);
                InputStreamReader input = new InputStreamReader(is, "UTF-8");
                BufferedReader bf = new BufferedReader(input);

                cartGoodDataCache(bf.readLine());

                bf.close();
                input.close();
                is.close();

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void cartGoodDataCache(String result) {
        if (result != null) {
            try {
                JSONArray jo = new JSONArray(result);
                if (jo != null && jo.length() > 0) {
                    int size = jo.length();
                    cachelist.clear();
                    for (int i = 0; i < size; i++) {
                        cachelist.add(GOODS_LIST.fromJson(jo.optJSONObject(i)));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    public void deleteGoods(int[] rec_id) {
        final String url = ProtocolConst.CARTDELETE;
        SESSION session = SESSION.getInstance();
        pd.show();
        JSONObject requestJsonObject = new JSONObject();

        try {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < rec_id.length; i++) {
                sb.append(rec_id[i]);
                if (i < rec_id.length - 1)
                    sb.append(",");
            }
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("rec_id", sb.toString());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LG.i("删除商品传入参数==" + requestJsonObject.toString());
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
                LG.i("返回参数==" + arg0.result);
                try {
                    jo = new JSONObject(arg0.result);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        int num = mApp.getGoodsNum();
                        num -= 1;
                        mApp.setGoodsNum(num);
                    }
                    onMessageResponse(url, jo, responseStatus);
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

    public void updateGoods(int rec_id, final int new_number) {
        final String url = ProtocolConst.CARTUPDATA;
        SESSION session = SESSION.getInstance();
        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("rec_id", rec_id);
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("new_number", new_number);
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

                JSONObject jo;
                try {
                    LG.i("商品数量修改==" + arg0.result);
                    jo = new JSONObject(arg0.result);
                    ShoppingCartModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));

                    if (responseStatus.getSucceed() == 1) {
                    }
                    onMessageResponse(url, jo, responseStatus);
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

    public void checkOrder(String array, String address_id) {
        final String url = ProtocolConst.CHECKORDER;
        SESSION session = SESSION.getInstance();
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            if (!TextUtils.isEmpty(array) && null != array) {
                requestJsonObject.put("rec_id", array);
            }
            if (!TextUtils.isEmpty(address_id)) {
                requestJsonObject.put("address_id", address_id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LG.i("=====" + requestJsonObject.toString());
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
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
                    LG.i("CHECKORDER的返回参数==" + arg0.result);
                    jo = new JSONObject(arg0.result);
                    ShoppingCartModel.this.callback(jo);

                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));

                    if (responseStatus.getSucceed() == 1) {

                        JSONObject dataJsonObject = jo.getJSONObject("data");
                        address = ADDRESS.fromJson(dataJsonObject.optJSONObject("consignee"));
                        JSONArray goodsArray = dataJsonObject.optJSONArray("goods_list");

                        if (null != goodsArray && goodsArray.length() > 0) {

                            balance_goods_list.clear();
                            for (int i = 0; i < goodsArray.length(); i++) {
                                JSONObject goodsJsonObject = goodsArray.getJSONObject(i);
                                GOODS_LIST goods_list_Item = GOODS_LIST.fromJson(goodsJsonObject);
                                balance_goods_list.add(goods_list_Item);
                            }
                        }
                        bonusinfo = dataJsonObject.optString("bonus");
                        discount_formated = dataJsonObject.optString("discount_formated");
                        discount_fee = dataJsonObject.optString("discount");
                        allow_can_invoice = dataJsonObject.optString("allow_can_invoice");

                        /**
                         * 积分
                         */
                        your_integral = dataJsonObject.optInt("your_integral");
                        order_max_integral = dataJsonObject.optInt("order_max_integral");
                        all_use_integral = dataJsonObject.optInt("allow_use_integral");


                        orderInfoJsonString = dataJsonObject.toString();

                        JSONArray shippingArray = dataJsonObject.optJSONArray("shipping_list");
                        shipping_list.clear();
                        if (null != shippingArray && shippingArray.length() > 0) {
                            for (int i = 0; i < shippingArray.length(); i++) {
                                JSONObject shippingJSONObject = shippingArray.getJSONObject(i);
                                SHIPPING shipping = SHIPPING.fromJson(shippingJSONObject);
                                shipping_list.add(shipping);
                            }
                        }

                        JSONArray paymentArray = dataJsonObject.optJSONArray("payment_list");

                        onlinepaymentlist.clear();
                        uplinepaymentlist.clear();
                        payment_list.clear();
                        if (paymentArray != null && paymentArray.length() > 0) {
                            for (int i = 0; i < paymentArray.length(); i++) {
                                PAYMENT p = PAYMENT.fromJson(paymentArray.optJSONObject(i));
                                payment_list.add(p);
                                if ("1".equals(p.getIs_online())) {
                                    onlinepaymentlist.add(p);
                                } else {
                                    uplinepaymentlist.add(p);
                                }
                            }
                        }
                    }
                    onMessageResponse(url, jo, responseStatus);
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


    // 订单生成
    public String order_id;

    public void flowDone(String array, String pay_id, String address_id, String shipping_id, String bonus, String integral, String inv_type, String inv_payee, String inv_content,
                         String remark_content) {
        final String url = ProtocolConst.FLOW_DOWN;
        pd.show();
        SESSION session = SESSION.getInstance();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            if (!TextUtils.isEmpty(array) && null != array) {
                requestJsonObject.put("rec_id", array);
            }
            requestJsonObject.put("pay_id", pay_id);
            requestJsonObject.put("address_id", address_id);
            requestJsonObject.put("shipping_id", shipping_id);
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            if (null != bonus) {
                requestJsonObject.put("bonus", bonus);
            }

            if (null != integral) {
                requestJsonObject.put("integral", integral);
            }

            if (null != inv_type) {
                requestJsonObject.put("inv_type", inv_type);
            }

            if (null != inv_payee) {
                requestJsonObject.put("inv_payee", inv_payee);
            }

            if (null != inv_content) {
                requestJsonObject.put("inv_content", inv_content);
            }

            if (null != inv_type && null != inv_content && null != inv_payee) {
                requestJsonObject.put("need_inv", "1");
            }
            if (null != remark_content) {
                requestJsonObject.put("postscript", remark_content);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());


        LG.i("flowDone传入参数===" + requestJsonObject.toString());

        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                LG.e("onFailure==" + arg0.getExceptionCode() + "---" + arg0.getMessage() + "---" + arg1);

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                pd.dismiss();
                JSONObject jo;
                LG.i("result==" + arg0.result.toString());
                try {
                    jo = new JSONObject(arg0.result);
                    flowdownjson = jo;
                    LG.i("jo==" + jo.toString());
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));

                    if (responseStatus.getSucceed() == 1) {
                        JSONObject json = jo.getJSONObject("data");
                        order_id = json.getString("order_id");
                    }
                    onMessageResponse(url, jo, responseStatus);
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
     * 积分验证
     *
     * @param integral
     */
    public void integral(String integral) {
        final String url = ProtocolConst.VALIDATE_INTEGRAL;
        SESSION session = SESSION.getInstance();
        ;
        pd.show();
        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("integral", integral);
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {

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
                    ShoppingCartModel.this.callback(jo);
                    Integraljson = jo;
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));

                    if (responseStatus.getSucceed() == 1) {
                        JSONObject data = jo.getJSONObject("data");
                        String bonus = data.getString("bonus").toString();
                        String bonus_formated = data.getString("bonus_formated").toString();
                        onMessageResponse(url, jo, responseStatus);
                    } else {
                        ToastUtil.getInstance().makeLongToast(mContext, responseStatus.getError_desc());
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
     * 验证红包
     *
     * @param bonus_sn
     */
    public void bonus(String bonus_sn) {
        final String url = ProtocolConst.VALIDATE_BONUS;
        SESSION session = SESSION.getInstance();
        ;
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("bonus_sn", bonus_sn);
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params,
                new RequestCallBack<String>() {


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
                            ShoppingCartModel.this.callback(jo);
                            redjson = jo;
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));

                            if (responseStatus.getSucceed() == 1) {
                                JSONObject data = jo.getJSONObject("data");
                                String bonus = data.getString("bonus").toString();
                                String bonus_formated = data.getString("bonus_formated").toString();
                                onMessageResponse(url, jo, responseStatus);
                            }
//                            else{
//                                ToastUtil.getInstance().makeLongToast(mContext,responseStatus.getError_desc());
//                            }
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


    // 缓存购物车数据
    private PrintStream ps = null;

    public void fileSave(String result, String name) {

        String path = rootpath + "/" + pkName;

        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        File file = new File(filePath + "/" + name + ".dat");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            ps = new PrintStream(fos);
            ps.print(result);
            ps.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //    public void cartCreate(String goods_id, ArrayList<Integer> specIdList, int goodQuantity, String object_id, String rec_type) {
    public void cartCreate(String goods_id, List<PRODUCTNUMBYCF> specIdList, int goodQuantity, String object_id, String rec_type) {
        final String url = ProtocolConst.CARTCREATE;
        pd.show();
        JSONArray spec = new JSONArray();
        JSONObject requestJsonObject = new JSONObject();
        SESSION session = SESSION.getInstance();
        try {
            requestJsonObject.put("goods_id", goods_id);
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("number", goodQuantity);
            requestJsonObject.put("object_id", object_id);
            requestJsonObject.put("rec_type", rec_type);
            for (int i = 0; i < specIdList.size(); i++) {
                PRODUCTNUMBYCF specId = specIdList.get(i);
                PRODUCTNUMBYCF sp=new PRODUCTNUMBYCF();
                sp.setProduct_number(specId.getSizeSelectCount()+"");
                sp.setProduct_id(specId.getProduct_id());
                sp.setGoods_attr(specId.getGoods_attr());
                sp.setGoods_attr_str(specId.getGoods_attr_str());
                spec.put(sp.toJson());
            }
            requestJsonObject.put("spec", spec);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params = addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===cart/create传入===" + requestJsonObject.toString());
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
                    LG.i("===cart/create返回===" + jo.toString());
                    ShoppingCartModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject dataJSONObject = jo.optJSONObject("data");
                        if (null != dataJSONObject) {
                            rec_id = dataJSONObject.optString("rec_id");
                            goodsNum = dataJSONObject.optInt("goods_number");
                        }
                    }
//                    else {
//                        ToastUtil.getInstance().makeLongToast(mContext,responseStatus.getError_desc());
//                    }
                    onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===cart/create返回===" + arg0.result);
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }

}
