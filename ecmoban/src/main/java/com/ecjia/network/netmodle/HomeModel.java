package com.ecjia.network.netmodle;

import android.content.Context;

import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.ADSENSE;
import com.ecjia.entity.responsemodel.ADSENSE_GROUP;
import com.ecjia.entity.responsemodel.CATEGORYGOODS;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.GROUPGOODS;
import com.ecjia.entity.responsemodel.MOBILEGOODS;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.PAGINATION;
import com.ecjia.entity.responsemodel.PLAYER;
import com.ecjia.entity.responsemodel.QUICK;
import com.ecjia.entity.responsemodel.SELLERINFO;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.SIMPLEGOODS;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.entity.responsemodel.SUGGEST;
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


public class HomeModel extends BaseModel {
    public ArrayList<SIMPLEGOODS> simplegoodsList = new ArrayList<SIMPLEGOODS>();
    public ArrayList<SIMPLEGOODS> newGoodsList = new ArrayList<SIMPLEGOODS>();
    public ArrayList<CATEGORYGOODS> categorygoodsList = new ArrayList<CATEGORYGOODS>();
    public ArrayList<PLAYER> playersList = new ArrayList<PLAYER>();
    public ArrayList<ADSENSE> adsenseList = new ArrayList<ADSENSE>();
    public ArrayList<GROUPGOODS> groupgoodsArrayList = new ArrayList<GROUPGOODS>();
    public ArrayList<MOBILEGOODS> mobilegoodsArrayList = new ArrayList<MOBILEGOODS>();
    public ArrayList<QUICK> quicklist = new ArrayList<QUICK>();
    public ArrayList<SUGGEST> suggests = new ArrayList<SUGGEST>();
    public PAGINATED paginated;
    public ArrayList<SELLERINFO> home_sellerlist = new ArrayList<SELLERINFO>();
    public ArrayList<QUICK> home_theme_list = new ArrayList<QUICK>();
    public ArrayList<ADSENSE_GROUP> adsenseGroup = new ArrayList<ADSENSE_GROUP>();
    String pkName;

    public String rootpath;

    public HomeModel(Context context) {
        super(context);
        pkName = mContext.getPackageName();
        rootpath = context.getCacheDir() + "/ECJia/cache";//获取缓存信息
    }

    public void readHomeDataCache() {

        String path = rootpath + "/" + pkName + "/homeData.dat";
        File f1 = new File(path);
        if (f1.exists()) {
            try {
                InputStream is = new FileInputStream(f1);
                InputStreamReader input = new InputStreamReader(is, "UTF-8");
                BufferedReader bf = new BufferedReader(input);

                homeDataCache(bf.readLine());

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

    }



    public void homeDataCache(String result) {

        try {
            if (result != null) {
                JSONObject jo = new JSONObject(result);

                STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                if (responseStatus.getSucceed() == 1) {
                    JSONObject dataJsonObject = jo.optJSONObject("data");
                    if (null != dataJsonObject) {
                        JSONArray playerJSONArray = dataJsonObject.optJSONArray("player");
                        if (null != playerJSONArray && playerJSONArray.length() > 0) {
                            playersList.clear();
                            for (int i = 0; i < playerJSONArray.length(); i++) {
                                JSONObject playerJsonObject = playerJSONArray.getJSONObject(i);
                                PLAYER playItem = PLAYER.fromJson(playerJsonObject);
                                playersList.add(playItem);
                            }
                        }

                        JSONArray quickJSONArray = dataJsonObject.optJSONArray("mobile_menu");
                        if (null != quickJSONArray && quickJSONArray.length() > 0) {
                            quicklist.clear();
                            for (int i = 0; i < quickJSONArray.length(); i++) {
                                JSONObject quickJsonObject = quickJSONArray.getJSONObject(i);
                                QUICK quickItem = QUICK.fromJson(quickJsonObject);
                                quicklist.add(quickItem);
                            }
                        }

                        JSONArray groupJSONArray = dataJsonObject.optJSONArray("adsense_group");
                        if (null != groupJSONArray && groupJSONArray.length() > 0) {
                            adsenseGroup.clear();
                            for (int i = 0; i < groupJSONArray.length(); i++) {
                                ADSENSE_GROUP adsense = ADSENSE_GROUP.fromJson(groupJSONArray.getJSONObject(i));
                                adsenseGroup.add(adsense);
                            }
                        }

                        JSONArray simpleGoodsJsonArray = dataJsonObject.optJSONArray("promote_goods");

                        if (null != simpleGoodsJsonArray && simpleGoodsJsonArray.length() > 0) {
                            simplegoodsList.clear();
                            for (int i = 0; i < simpleGoodsJsonArray.length(); i++) {
                                JSONObject simpleGoodsJsonObject = simpleGoodsJsonArray.getJSONObject(i);
                                SIMPLEGOODS simplegoods = SIMPLEGOODS.fromJson(simpleGoodsJsonObject);
                                simplegoodsList.add(simplegoods);
                            }
                        }
                        JSONArray newGoodsJsonArray = dataJsonObject.optJSONArray("new_goods");

                        if (null != newGoodsJsonArray && newGoodsJsonArray.length() > 0) {
                            newGoodsList.clear();
                            for (int i = 0; i < newGoodsJsonArray.length(); i++) {
                                JSONObject newGoodsJsonObject = newGoodsJsonArray.getJSONObject(i);
                                SIMPLEGOODS newgoods = SIMPLEGOODS.fromJson(newGoodsJsonObject);
                                newGoodsList.add(newgoods);
                            }
                        }

                        adsenseList.clear();
                        JSONObject jsonObject = dataJsonObject.optJSONObject("mobile_home_adsense1");
                        if (jsonObject != null) {
                            adsenseList.add(0, ADSENSE.fromJson(jsonObject));
                        }
                        JSONArray adsenseArray = dataJsonObject.optJSONArray("mobile_home_adsense2");
                        if (null != adsenseArray && adsenseArray.length() > 0) {
                            for (int i = 0; i < adsenseArray.length(); i++) {
                                adsenseList.add(ADSENSE.fromJson(adsenseArray.optJSONObject(i)));
                            }
                        }
                        //团购
                        groupgoodsArrayList.clear();
                        JSONArray groupGoodsjson = dataJsonObject.optJSONArray("group_goods");
                        if (null != groupGoodsjson && groupGoodsjson.length() > 0) {
                            int size = groupGoodsjson.length();
                            for (int i = 0; i < size; i++) {
                                groupgoodsArrayList.add(GROUPGOODS.fromJson(groupGoodsjson.optJSONObject(i)));
                            }
                        }

                        //手机专享
                        mobilegoodsArrayList.clear();
                        JSONArray mobileGoodsjson = dataJsonObject.optJSONArray("mobile_buy_goods");
                        if (null != mobileGoodsjson && mobileGoodsjson.length() > 0) {
                            int size = mobileGoodsjson.length();
                            for (int i = 0; i < size; i++) {
                                mobilegoodsArrayList.add(MOBILEGOODS.fromJson(mobileGoodsjson.optJSONObject(i)));
                            }
                        }

                        //首页发现店铺
                        home_sellerlist.clear();
                        JSONArray sellerjsonarray = dataJsonObject.optJSONArray("seller_recommend");
                        if (sellerjsonarray != null && sellerjsonarray.length() > 0) {
                            for (int i = 0; i < sellerjsonarray.length(); i++) {
                                home_sellerlist.add(SELLERINFO.fromJson(sellerjsonarray.optJSONObject(i)));
                            }
                        }

                        //首页主题类
                        JSONArray topicjsonarray = dataJsonObject.optJSONArray("mobile_topic_adsense");
                        if (null != topicjsonarray && topicjsonarray.length() > 0) {
                            home_theme_list.clear();
                            int size = topicjsonarray.length();
                            for (int i = 0; i < size; i++) {
                                home_theme_list.add(QUICK.fromJson(topicjsonarray.optJSONObject(i)));
                            }
                        }

                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void fetchHotSelling() {
        final String url = ProtocolConst.HOMEDATA;
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", SESSION.getInstance().toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                JSONObject jo;
                try {
                    LG.i("jo==" + objectResponseInfo.result);
                    jo = new JSONObject(objectResponseInfo.result);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        pd.dismiss();

                        JSONObject dataJsonObject = jo.optJSONObject("data");
                        if (null != dataJsonObject) {
                            //轮播图
                            JSONArray playerJSONArray = dataJsonObject.optJSONArray("player");
                            if (null != playerJSONArray && playerJSONArray.length() > 0) {
                                playersList.clear();
                                for (int i = 0; i < playerJSONArray.length(); i++) {
                                    JSONObject playerJsonObject = playerJSONArray.getJSONObject(i);
                                    PLAYER playItem = PLAYER.fromJson(playerJsonObject);
                                    playersList.add(playItem);
                                }
                            }


                            JSONArray quickJSONArray = dataJsonObject.optJSONArray("mobile_menu");
                            if (null != quickJSONArray && quickJSONArray.length() > 0) {
                                quicklist.clear();
                                for (int i = 0; i < quickJSONArray.length(); i++) {
                                    JSONObject quickJsonObject = quickJSONArray.getJSONObject(i);
                                    QUICK quickItem = QUICK.fromJson(quickJsonObject);
                                    quicklist.add(quickItem);
                                }
                            }

                            JSONArray groupJSONArray = dataJsonObject.optJSONArray("adsense_group");
                            if (null != groupJSONArray && groupJSONArray.length() > 0) {
                                adsenseGroup.clear();
                                for (int i = 0; i < groupJSONArray.length(); i++) {
                                    ADSENSE_GROUP adsense = ADSENSE_GROUP.fromJson(groupJSONArray.getJSONObject(i));
                                    adsenseGroup.add(adsense);
                                }
                            }

                            //促销商品
                            JSONArray simpleGoodsJsonArray = dataJsonObject.optJSONArray("promote_goods");

                            if (null != simpleGoodsJsonArray && simpleGoodsJsonArray.length() > 0) {
                                simplegoodsList.clear();
                                for (int i = 0; i < simpleGoodsJsonArray.length(); i++) {
                                    JSONObject simpleGoodsJsonObject = simpleGoodsJsonArray.getJSONObject(i);
                                    SIMPLEGOODS simplegoods = SIMPLEGOODS.fromJson(simpleGoodsJsonObject);
                                    simplegoodsList.add(simplegoods);
                                }
                            } else {
                                simplegoodsList.clear();
                            }
                            //最新商品
                            JSONArray newGoodsJsonArray = dataJsonObject.optJSONArray("new_goods");
                            if (null != newGoodsJsonArray && newGoodsJsonArray.length() > 0) {
                                newGoodsList.clear();
                                for (int i = 0; i < newGoodsJsonArray.length(); i++) {
                                    JSONObject newGoodsJsonObject = newGoodsJsonArray.getJSONObject(i);
                                    SIMPLEGOODS newgoods = SIMPLEGOODS.fromJson(newGoodsJsonObject);
                                    newGoodsList.add(newgoods);
                                }
                            } else {
                                newGoodsList.clear();
                            }
                            //广告位
                            adsenseList.clear();
                            JSONObject jsonObject = dataJsonObject.optJSONObject("mobile_home_adsense1");
                            if (jsonObject != null) {
                                adsenseList.add(0, ADSENSE.fromJson(jsonObject));
                            }
                            JSONArray adsenseArray = dataJsonObject.optJSONArray("mobile_home_adsense2");
                            if (null != adsenseArray && adsenseArray.length() > 0) {
                                for (int i = 0; i < adsenseArray.length(); i++) {
                                    adsenseList.add(ADSENSE.fromJson(adsenseArray.optJSONObject(i)));
                                }
                            }
                            //团购
                            groupgoodsArrayList.clear();
                            JSONArray groupGoodsjson = dataJsonObject.optJSONArray("group_goods");
                            if (null != groupGoodsjson && groupGoodsjson.length() > 0) {
                                int size = groupGoodsjson.length();
                                for (int i = 0; i < size; i++) {
                                    groupgoodsArrayList.add(GROUPGOODS.fromJson(groupGoodsjson.optJSONObject(i)));
                                }
                            }
                            //手机专享
                            mobilegoodsArrayList.clear();
                            JSONArray mobileGoodsjson = dataJsonObject.optJSONArray("mobile_buy_goods");
                            if (null != mobileGoodsjson && mobileGoodsjson.length() > 0) {
                                int size = mobileGoodsjson.length();
                                for (int i = 0; i < size; i++) {
                                    mobilegoodsArrayList.add(MOBILEGOODS.fromJson(mobileGoodsjson.optJSONObject(i)));
                                }
                            }

                            //首页发现店铺
                            home_sellerlist.clear();
                            JSONArray sellerjsonarray = dataJsonObject.optJSONArray("seller_recommend");
                            if (sellerjsonarray != null && sellerjsonarray.length() > 0) {
                                for (int i = 0; i < sellerjsonarray.length(); i++) {
                                    home_sellerlist.add(SELLERINFO.fromJson(sellerjsonarray.optJSONObject(i)));
                                }
                            }
                            //首页主题类
                            JSONArray topicjsonarray = dataJsonObject.optJSONArray("mobile_topic_adsense");
                            if (null != topicjsonarray && topicjsonarray.length() > 0) {
                                home_theme_list.clear();
                                int size = topicjsonarray.length();
                                for (int i = 0; i < size; i++) {
                                    home_theme_list.add(QUICK.fromJson(topicjsonarray.optJSONObject(i)));
                                }
                            }

                        }
                    }
                    HomeModel.this.onMessageResponse(url, jo, responseStatus);

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

    public String web;

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

    //获取每日推荐
    public void getSuggestList(String type) {
        final String url = ProtocolConst.HOME_SUGGESTLIST;
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(8);
        SESSION session = SESSION.getInstance();
        HttpUtils http = new HttpUtils();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("action_type", type);

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
                    paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                    if (responseStatus.getSucceed() == 1) {
                        suggests.clear();
                        JSONArray jsonArray = jo.optJSONArray("data");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                suggests.add(SUGGEST.fromJson(jsonArray.optJSONObject(i)));
                            }
                        }
                    }
                    HomeModel.this.onMessageResponse(url, jo, responseStatus);
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

    public void getSuggestListMore(String type) {
        final String url = ProtocolConst.HOME_SUGGESTLIST;
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(suggests.size() / 8 + 1);
        pagination.setCount(8);
        SESSION session = SESSION.getInstance();
        HttpUtils http = new HttpUtils();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("action_type", type);

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
                    LG.i("=======suggest=========" + objectResponseInfo.result);
                    jo = new JSONObject(objectResponseInfo.result);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray jsonArray = jo.optJSONArray("data");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                suggests.add(SUGGEST.fromJson(jsonArray.optJSONObject(i)));
                            }
                        }

                    }
                    HomeModel.this.onMessageResponse(url, jo, responseStatus);
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
