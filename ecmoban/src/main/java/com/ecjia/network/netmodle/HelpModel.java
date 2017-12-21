package com.ecjia.network.netmodle;

import android.content.Context;
import android.content.res.Resources;

import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.SHOPHELP;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.widgets.dialog.MyProgressDialog;
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

import java.io.*;
import java.util.ArrayList;


public class HelpModel extends BaseModel {
    public ArrayList<SHOPHELP> shophelpsList = new ArrayList<SHOPHELP>();
    String pkName;
    public MyProgressDialog pd;
    public String rootpath;

    public HelpModel(Context context) {
        super(context);
        pd = MyProgressDialog.createDialog(context);
        Resources resources = AppConst.getResources(context);
        pd.setMessage(resources.getString(R.string.loading));
        pkName = mContext.getPackageName();
        rootpath = context.getCacheDir() + "/ECMoban/cache";
        readHelpDataCache();
    }

    public void readHelpDataCache() {
        String path = rootpath + "/" + pkName + "/helpData.dat";
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
                    JSONArray shopHelpJsonArray = jsonObject.optJSONArray("data");
                    data = jsonObject.toString();
                    shophelpsList.clear();
                    if (null != shopHelpJsonArray && shopHelpJsonArray.length() > 0) {
                        for (int i = 0; i < shopHelpJsonArray.length(); i++) {
                            JSONObject shopHelpJsonObject = shopHelpJsonArray.getJSONObject(i);
                            SHOPHELP shopHelpItem = SHOPHELP.fromJson(shopHelpJsonObject);
                            shophelpsList.add(shopHelpItem);
                        }
                    }

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

    public void fetchShopHelp() {
        final String url = ProtocolConst.SHOPHELP;
        pd.show();
        SESSION session = SESSION.getInstance();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("token", session.sid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===shop/help传入===" + requestJsonObject.toString());

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                pd.dismiss();
                JSONObject jo;
                try {
                    jo = new JSONObject(objectResponseInfo.result);
                    LG.i("===shop/help返回===" + jo.toString());
                    HelpModel.this.callback(jo);

                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray shopHelpJsonArray = jo.optJSONArray("data");
                        data = jo.toString();
                        if (null != shopHelpJsonArray && shopHelpJsonArray.length() > 0) {
                            shophelpsList.clear();
                            for (int i = 0; i < shopHelpJsonArray.length(); i++) {
                                JSONObject shopHelpJsonObject = shopHelpJsonArray.getJSONObject(i);
                                SHOPHELP shopHelpItem = SHOPHELP.fromJson(shopHelpJsonObject);
                                shophelpsList.add(shopHelpItem);
                            }
                        }
                    }
                    HelpModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===shop/help返回===" + objectResponseInfo.result);
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

    public String web;

    public void helpArticle(int article_id) {
        final String url = ProtocolConst.SHOP_HELP_DETAIL;
        SESSION session = SESSION.getInstance();
        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("article_id", article_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params=addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===shop/help/detail传入===" + requestJsonObject.toString());

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
                    LG.i("===shop/help/detail返回===" + jo.toString());
                    HelpModel.this.callback(jo);

                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        web = jo.getString("data").toString();
                    }
                    HelpModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===shop/help/detail返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }

}
