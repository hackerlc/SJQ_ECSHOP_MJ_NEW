package com.ecjia.network.netmodle;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.CONFIG;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.PAYMENT;
import com.ecjia.entity.responsemodel.REGIONS;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.ECJIAFileUtil;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ConfigModel extends BaseModel {
    public CONFIG config;

    public Drawable login_bitmap_bg = null;

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    public ArrayList<REGIONS> regionselist = new ArrayList<REGIONS>();

    private static ConfigModel instance;

    public static ConfigModel getInstance() {
        return instance;
    }


    public static ConfigModel getInstance(Context context) {
        if (instance == null) {
            Class c = ConfigModel.class;
            synchronized (ConfigModel.class) {
                if (instance == null) {
                    instance = new ConfigModel(context);
                }
            }

        }
        return instance;
    }


    private ConfigModel() {
        super();
    }

    public ConfigModel(Context context) {
        super(context);
        shared = context.getSharedPreferences(AppConst.SPF_SHOPCONFIG, 0);
        editor = shared.edit();
        instance = this;
    }

    public void getConfig(final Handler handler) {
        final String url = ProtocolConst.CONFIG;
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

        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {

                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("config的返回值==" + jo.toString());
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject dataJsonObject = jo.optJSONObject("data");
                        try {
                            ECJIAFileUtil.saveFile(AndroidManager.SHOPCONFIG, dataJsonObject.toString(), CONFIG.SHOP_CONFIG_FILE_NAME);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ConfigModel.this.config = CONFIG.fromJson(dataJsonObject);
                        JSONArray cityarray = dataJsonObject.optJSONArray("recommend_city");
                        regionselist.clear();
                        if (null != cityarray && cityarray.length() > 0) {
                            for (int i = 0; i < cityarray.length(); i++) {
                                regionselist.add(REGIONS.fromJson(cityarray.optJSONObject(i)));
                            }
                        }

                        int length = shared.getInt("login_bg", 0);
                        if (!TextUtils.isEmpty(config.getMobile_phone_login_bgimage())) {
                            final int finalLength = length;
                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        final int length2 = getFileSize(config.getMobile_phone_login_bgimage());
                                        if (finalLength != length2) {
                                            Message message = new Message();
                                            message.obj = config.getMobile_phone_login_bgimage();
                                            message.what = length2;
                                            handler.sendMessage(message);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                        }

                        Message msg = new Message();
                        msg.obj = ProtocolConst.CONFIG;
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

    /**
     * 获取网络文件的大小
     *
     * @param urlString
     * @return
     * @throws IOException
     * @throws Exception
     */
    public int getFileSize(String urlString) throws IOException {

        int lenght = 0;
        URL mUrl = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept-Encoding", "identity");
        conn.setRequestProperty("Referer", urlString);
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.connect();

        int responseCode = conn.getResponseCode();
        // 判断请求是否成功处理
        if (responseCode == 200) {
            lenght = conn.getContentLength();
            LG.i("图片大小==" + lenght);
        }
        return lenght;
    }

    public void getPayment() {
        String url = ProtocolConst.SHOP_PAYMENT;
        SESSION session = SESSION.getInstance();
        HttpUtils http = new HttpUtils();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("session", session.toJson());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                JSONObject jo;
                try {
                    jo = new JSONObject(objectResponseInfo.result);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    LG.i("shop/payment==" + jo.toString());
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject dataJsonObject = jo.optJSONObject("data");
                        JSONArray array = dataJsonObject.optJSONArray("payment");
                        mApp.Rechargepaymentlist.clear();
                        mApp.onlinepaymentlist.clear();
                        mApp.uplinepaymentlist.clear();
                        mApp.paymentlist.clear();
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                PAYMENT p = PAYMENT.fromJson(array.optJSONObject(i));
                                mApp.paymentlist.add(p);
                                if ("1".equals(p.getIs_online())) {
                                    mApp.onlinepaymentlist.add(p);
                                } else {
                                    mApp.uplinepaymentlist.add(p);
                                }
                                if ("1".equals(p.getIs_online()) && !AppConst.BALANCE.equals(p.getPay_code())) {
                                    LG.i(p.getPay_code());
                                    mApp.Rechargepaymentlist.add(p);
                                }
                            }
                            LG.i("Rechargepaymentlist==" + mApp.Rechargepaymentlist.size());
                        }
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
