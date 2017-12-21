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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;

import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.CATEGORY;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecjia.util.LG;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class SearchModel extends BaseModel {
    public String rootpath;

    public ArrayList<String> list = new ArrayList<String>();
    public ArrayList<CATEGORY> categoryArrayList = new ArrayList<CATEGORY>();
    String pkName;
    public MyProgressDialog pd;

    public SearchModel(Context context) {
        super(context);
        pkName = mContext.getPackageName();
        rootpath = context.getCacheDir() + "/ECJia/cache";//获取缓存信息
        pd = MyProgressDialog.createDialog(context);
        Resources resources = AppConst.getResources(context);
        pd.setMessage(resources.getString(R.string.loading));
        pkName = mContext.getPackageName();
        readSearchDataCache();
    }

    // 读取缓存
    public void readSearchDataCache() {
        String path = rootpath + "/" + pkName + "/searchData.dat";
        File f1 = new File(path);
        try {
            InputStream is = new FileInputStream(f1);
            InputStreamReader input = new InputStreamReader(is, "UTF-8");
            BufferedReader bf = new BufferedReader(input);

            searchDataCache(bf.readLine());

            bf.close();
            input.close();
            is.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchDataCache(String result) {
        try {
            if (result != null) {
                JSONObject jsonObject = new JSONObject(result);

                STATUS responseStatus = STATUS.fromJson(jsonObject.optJSONObject("status"));
                list.clear();
                if (responseStatus.getSucceed() == 1) {
                    JSONArray playerJSONArray = jsonObject.optJSONArray("data");
                    if (null != playerJSONArray && playerJSONArray.length() > 0) {
                        for (int i = 0; i < playerJSONArray.length(); i++) {
                            list.add(playerJSONArray.getString(i));
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchCategory() {
        final String url = ProtocolConst.CATEGORY;
        SESSION session = SESSION.getInstance();
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===goods/category传入===" + requestJsonObject.toString());

        pd.show();
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                pd.dismiss();
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("===goods/category返回===" + jo.toString());
                    SearchModel.this.callback(jo);

                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    categoryArrayList.clear();
                    if (responseStatus.getSucceed() == 1) {
                        fileSave(jo.toString(), "searchData");
                        JSONArray categoryJSONArray = jo.optJSONArray("data");
                        if (null != categoryJSONArray && categoryJSONArray.length() > 0) {
                            for (int i = 0; i < categoryJSONArray.length(); i++) {
                                JSONObject categoryObject = categoryJSONArray.getJSONObject(i);
                                CATEGORY category = CATEGORY.fromJson(categoryObject);
                                categoryArrayList.add(category);
                            }
                        }
                    }
                    SearchModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===goods/category返回===" + arg0.result);
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


    // 获取搜索推荐关键字

    public void searchKeywords(final Handler handler) {
        String url = ProtocolConst.SEARCHKEYWORDS;
        pd.show();
        SESSION session = SESSION.getInstance();
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                pd.dismiss();
                JSONObject jo;
                try {
                    jo = new JSONObject(objectResponseInfo.result);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    list.clear();
                    if (responseStatus.getSucceed() == 1) {
                        //fileSave(jo.toString(),"searchData");

                        JSONArray playerJSONArray = jo.optJSONArray("data");

                        if (null != playerJSONArray && playerJSONArray.length() > 0) {
                            for (int i = 0; i < playerJSONArray.length(); i++) {
                                list.add(playerJSONArray.getString(i));
                            }
                        }
                        Message msg = new Message();
                        msg.obj = ProtocolConst.SEARCHKEYWORDS;
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
}
