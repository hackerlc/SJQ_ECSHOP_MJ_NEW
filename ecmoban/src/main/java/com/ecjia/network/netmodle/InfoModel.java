package com.ecjia.network.netmodle;

import android.content.Context;
import android.content.res.Resources;

import com.ecjia.consts.ProtocolConst;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.entity.responsemodel.ACCESS_TOKEN;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.SHOPINFO;
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


public class InfoModel extends BaseModel {
    String pkName;
    public MyProgressDialog pd;
    public String rootpath;
    private ACCESS_TOKEN access_token;
    public ArrayList<SHOPINFO> shopinfos = new ArrayList<SHOPINFO>();

    public InfoModel(Context context) {
        super(context);
        pd = MyProgressDialog.createDialog(context);
        Resources resources = AppConst.getResources(context);
        pd.setMessage(resources.getString(R.string.loading));
        pkName = mContext.getPackageName();
        rootpath = context.getCacheDir() + "/ECMoban/cache";
        readDataCache();
    }

    public void readDataCache() {
        String path = rootpath + "/" + pkName + "/infoData.dat";
        File f1 = new File(path);
        try {
            InputStream is = new FileInputStream(f1);
            InputStreamReader input = new InputStreamReader(is, "UTF-8");
            BufferedReader bf = new BufferedReader(input);

            helpDataCache(bf.readLine());

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

    public void helpDataCache(String result) {

        try {
            if (result != null) {
                JSONObject jsonObject = new JSONObject(result);
                STATUS responseStatus = STATUS.fromJson(jsonObject.optJSONObject("status"));
                if (responseStatus.getSucceed() == 1) {
                    JSONArray JsonArray = jsonObject.optJSONArray("data");
                    data = jsonObject.toString();
                    shopinfos.clear();
                    if (null != JsonArray && JsonArray.length() > 0) {
                        for (int i = 0; i < JsonArray.length(); i++) {
                            JSONObject JsonObject = JsonArray.getJSONObject(i);
                            SHOPINFO obj = SHOPINFO.fromJson(JsonObject);
                            shopinfos.add(obj);
                        }
                    }
                    mApp.setShopinfos(shopinfos);
                }

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // 缓存数据
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


    public String data;

    public void getShopInfo() {
        final String url = ProtocolConst.SHOP_INFO;
        SESSION session = SESSION.getInstance();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("token", mApp.getShop_token());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===shop/info传入===" + requestJsonObject.toString());

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                JSONObject jo;
                try {
                    jo = new JSONObject(objectResponseInfo.result);
                    LG.i("===shop/info返回===" + jo.toString());
                    InfoModel.this.callback(jo);

                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        fileSave(jo.toString(), "infoData");
                        JSONArray JsonArray = jo.optJSONArray("data");
                        data = jo.toString();
                        shopinfos.clear();
                        if (null != JsonArray && JsonArray.length() > 0) {
                            for (int i = 0; i < JsonArray.length(); i++) {
                                JSONObject JsonObject = JsonArray.getJSONObject(i);
                                SHOPINFO obj = SHOPINFO.fromJson(JsonObject);
                                shopinfos.add(obj);
                            }
                        }
                        mApp.setShopinfos(shopinfos);
                    }
                    InfoModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===shop/info返回===" + objectResponseInfo.result);
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


    public void getShopToken() {
        final String url = ProtocolConst.SHOP_TOKEN;
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, new RequestParams(),
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            LG.i("===shop/token返回===" + jo.toString());
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                access_token = ACCESS_TOKEN.fromJson(jo.optJSONObject("data"));
                                mApp.setShop_token(access_token.getAccess_token());
                            }
                            InfoModel.this.onMessageResponse(url, jo, responseStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            LG.i("===shop/token返回===" + arg0.result);
                        }

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                });
    }

    public String web;

    public void infoArticle(int article_id) {
        final String url = ProtocolConst.SHOP_INFO_DETAIL;
        SESSION session = SESSION.getInstance();
        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("token", mApp.getShop_token());
            requestJsonObject.put("article_id", article_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===shop/info/detail传入===" + requestJsonObject.toString());

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
                    LG.i("===shop/info/detail返回===" + jo.toString());
                    InfoModel.this.callback(jo);

                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        web = jo.getString("data").toString();
                    }
                    InfoModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===shop/info/detail返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }

}
