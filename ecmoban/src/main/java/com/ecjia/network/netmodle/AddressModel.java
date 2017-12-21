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
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;

import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.ADDRESS;
import com.ecjia.entity.responsemodel.CITY;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.REGIONS;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.AppConst;
import com.ecjia.util.HanziToPinyin;
import com.ecjia.util.LG;
import com.ecjia.consts.AndroidManager;

import com.ecjia.widgets.dialog.MyProgressDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class AddressModel extends BaseModel {

    public ArrayList<ADDRESS> addressList = new ArrayList<ADDRESS>();
    public ArrayList<REGIONS> regionsList0 = new ArrayList<REGIONS>();
    public ArrayList<CITY> allCityList = new ArrayList<CITY>();
    public ArrayList<CITY> hotCityList = new ArrayList<CITY>();
    public MyProgressDialog pd;
    public Resources resources;
    HanziToPinyin toPinyin = HanziToPinyin.getInstance();
    String pkName;
    public String rootpath;
    private ArrayList<REGIONS> confighotlist;
    private boolean fromGetAddressList = false;

    public AddressModel(Context context) {
        super(context);
        pkName = mContext.getPackageName();
        rootpath = context.getCacheDir() + "/ECJia/cache";//获取缓存信息
        resources = AppConst.getResources(context);
        pd = MyProgressDialog.createDialog(context);
        confighotlist = ConfigModel.getInstance().regionselist;
        pd.setMessage(resources.getString(R.string.loading));
        readCityDataCache();
    }

    private void readCityDataCache() {
        String path = rootpath + "/" + pkName + "/citylistData.dat";
        File f1 = new File(path);
        if (f1.exists()) {
            try {
                InputStream is = new FileInputStream(f1);
                InputStreamReader input = new InputStreamReader(is, "UTF-8");
                BufferedReader bf = new BufferedReader(input);

                cityDataCache(bf.readLine());

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

    //读取城市列表的缓存
    private void cityDataCache(String result) {

        if (result != null) {
            try {
                JSONArray regionsJsonArray = new JSONArray(result);
                regionsList0.clear();
                if (null != regionsJsonArray && regionsJsonArray.length() > 0) {
                    regionsList0.clear();
                    for (int i = 0; i < regionsJsonArray.length(); i++) {
                        JSONObject regionsJsonObject = regionsJsonArray.getJSONObject(i);
                        CITY city = CITY.fromJson(regionsJsonObject);

                        allCityList.add(city);
                        Collections.sort(allCityList, comparator);

                    }

                }
                hotCityList.clear();
                if (confighotlist.size() > 0) {
                    for (int i = 0; i < confighotlist.size(); i++) {
                        CITY city = new CITY();
                        city.setId(confighotlist.get(i).getId());
                        city.setPinyin(toPinyin.getPinYin(confighotlist.get(i).getName()));
                        city.setIshot("1");
                        city.setName(confighotlist.get(i).getName());
                        city.setParent_id(confighotlist.get(i).getParent_id());
                        if (confighotlist.get(i).getName().contains("重庆")) {
                            city.setPinyin("chongqing");
                        }
                        hotCityList.add(city);
                        Collections.sort(hotCityList, comparator);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // 获取地址列表
    public void getAddressList() {
        pd.show();
        final String url = ProtocolConst.ADDRESS_LIST;
        SESSION session = SESSION.getInstance();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
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
                pd.dismiss();
                JSONObject jo;
                try {
                    LG.i("jo==" + arg0.result);
                    jo = new JSONObject(arg0.result);
                    AddressModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray dataJsonArray = jo.optJSONArray("data");
                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
                            addressList.clear();
                            for (int i = 0; i < dataJsonArray.length(); i++) {
                                JSONObject addressJsonObject = dataJsonArray.getJSONObject(i);
                                ADDRESS addressItem = ADDRESS.fromJson(addressJsonObject);
                                if (addressItem.getDefault_address() == 1) {
                                    addressList.add(0, addressItem);
                                } else {
                                    if (!addressItem.getProvince_name().equals("null")) {
                                        addressList.add(addressItem);
                                    }
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

    public void addAddress(String consignee, String tel, String email, String mobile, String zipcode, String address, String country, String province, String city, String district
        ) {
        final String url = ProtocolConst.ADDRESS_ADD;
        SESSION session = SESSION.getInstance();
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        ADDRESS add = new ADDRESS();
        add.setConsignee(consignee);
        add.setTel(tel);
        add.setEmail(email);
        add.setMobile(mobile);
        add.setZipcode(zipcode);
        add.setAddress(address);
        add.setCountry(country);
        add.setProvince(province);
        add.setCity(city);
        add.setDistrict(district);
        RequestParams params = new RequestParams();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("address", add.toJson());
            params.addBodyParameter("json", requestJsonObject.toString());
        } catch (JSONException e) {
            // TODO: handle exception
        }
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
                    AddressModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray dataJsonArray = jo.optJSONArray("data");
                        addressList.clear();
                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
                            for (int i = 0; i < dataJsonArray.length(); i++) {
                                JSONObject addressJsonObject = dataJsonArray.getJSONObject(i);
                                ADDRESS addressItem = ADDRESS.fromJson(addressJsonObject);
                                addressList.add(addressItem);
                            }
                        }
                    }
                    AddressModel.this.onMessageResponse(url, jo, responseStatus);
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

    public void region(String parent_id, final int i, final Handler handler) {

        pd.show();
        String url = ProtocolConst.REGION;
        SESSION session = SESSION.getInstance();

        RequestParams params = new RequestParams();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("parent_id", parent_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.addBodyParameter("json", requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Message msg = new Message();
                msg.obj = ProtocolConst.REGION;
                msg.what = 0;
                msg.arg1 = i;
                handler.sendMessage(msg);
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                pd.dismiss();
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    AddressModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject data = jo.optJSONObject("data");
                        JSONArray regionsJsonArray = data.optJSONArray("regions");
                        regionsList0.clear();
                        if (null != regionsJsonArray && regionsJsonArray.length() > 0) {
                            regionsList0.clear();
                            for (int i = 0; i < regionsJsonArray.length(); i++) {
                                JSONObject regionsJsonObject = regionsJsonArray.getJSONObject(i);
                                REGIONS regions = REGIONS.fromJson(regionsJsonObject);
                                regionsList0.add(regions);
                            }
                        }
                        Message msg = new Message();
                        msg.obj = ProtocolConst.REGION;
                        msg.what = responseStatus.getSucceed();
                        msg.arg1 = i;
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


    public void newRegion(String parent_id) {

        SESSION session = SESSION.getInstance();
        final String url = ProtocolConst.REGION;
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("parent_id", parent_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("parent_id==" + parent_id);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                LG.i("地址===:" + arg0.result);
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    AddressModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject data = jo.optJSONObject("data");
                        JSONArray regionsJsonArray = data.optJSONArray("regions");
                        regionsList0.clear();
                        if (null != regionsJsonArray && regionsJsonArray.length() > 0) {
                            regionsList0.clear();
                            for (int i = 0; i < regionsJsonArray.length(); i++) {
                                JSONObject regionsJsonObject = regionsJsonArray.getJSONObject(i);
                                REGIONS regions = REGIONS.fromJson(regionsJsonObject);
                                regionsList0.add(regions);
                            }
                        }
                        AddressModel.this.onMessageResponse(url, jo, responseStatus);
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

    //获取城市选择列表
    public void regionall(String type, final Handler handler) {
        //添加热门城市
        hotCityList.clear();
        if (confighotlist.size() > 0) {
            for (int i = 0; i < confighotlist.size(); i++) {
                CITY city = new CITY();
                city.setId(confighotlist.get(i).getId());
                city.setPinyin(toPinyin.getPinYin(confighotlist.get(i).getName()));
                city.setIshot("1");
                city.setName(confighotlist.get(i).getName());
                city.setParent_id(confighotlist.get(i).getParent_id());
                if (confighotlist.get(i).getName().contains("重庆")) {
                    city.setPinyin("chongqing");
                }
                hotCityList.add(city);
                Collections.sort(hotCityList, comparator);
            }
        }
        if (allCityList.size() > 0) {
            Message msg = new Message();
            msg.obj = ProtocolConst.REGION;
            msg.what = 1;
            handler.sendMessage(msg);
        } else {
            SESSION session = SESSION.getInstance();
            String url = ProtocolConst.REGION;
            JSONObject requestJsonObject = new JSONObject();
            try {
                requestJsonObject.put("device", DEVICE.getInstance().toJson());
                requestJsonObject.put("session", session.toJson());
                requestJsonObject.put("type", type);

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
                            Message msg = new Message();
                            msg.obj = ProtocolConst.REGION;
                            msg.what = 0;
                            handler.sendMessage(msg);
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {

                            LG.i("地址:" + arg0.result);
                            JSONObject jo;
                            try {
                                jo = new JSONObject(arg0.result);

                                STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                                if (responseStatus.getSucceed() == 1) {


                                    JSONObject data = jo.optJSONObject("data");
                                    JSONArray regionsJsonArray = data.optJSONArray("regions");
                                    regionsList0.clear();
                                    if (null != regionsJsonArray && regionsJsonArray.length() > 0) {
                                        int size = regionsJsonArray.length();
                                        for (int i = 0; i < size; i++) {
                                            JSONObject regionsJsonObject = regionsJsonArray.getJSONObject(i);
                                            REGIONS regions = REGIONS.fromJson(regionsJsonObject);

                                            CITY city = new CITY();
                                            city.setId(regions.getId());
                                            city.setPinyin(toPinyin.getPinYin(regions.getName()));
                                            if (regions.getName().contains("重庆")) {
                                                city.setPinyin("chongqing");
                                            }
                                            city.setParent_id(regions.getParent_id());
                                            city.setName(regions.getName());
                                            city.setIshot("0");
                                            allCityList.add(city);
                                            Collections.sort(allCityList, comparator);

                                        }
                                        JSONArray jsonArray = new JSONArray();
                                        int citylistsize = allCityList.size();
                                        for (int i = 0; i < citylistsize; i++) {
                                            jsonArray.put(allCityList.get(i).toJson());
                                        }
                                        fileSave(jsonArray.toString(), "citylistData");
                                    }
                                    Message msg = new Message();
                                    msg.obj = ProtocolConst.REGION;
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

    public ADDRESS address;

    // 获取地址详细信息
    public void getAddressInfo(String address_id) {
        final String url = ProtocolConst.ADDRESS_INFO;
        SESSION session = SESSION.getInstance();
        JSONObject requestJsonObject = new JSONObject();
        pd.show();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("address_id", address_id);
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
                            jo = new JSONObject(arg0.result);
                            LG.i("===address/info返回===" + jo.toString());
                            AddressModel.this.callback(jo);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                address = ADDRESS.fromJson(jo.optJSONObject("data"));
                            }
                            AddressModel.this.onMessageResponse(url, jo, responseStatus);
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


    // 设置默认地址
    public void addressDefault(final String address_id) {
        String url = ProtocolConst.ADDRESS_DEFAULT;
        pd.show();
        SESSION session = SESSION.getInstance();
        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("address_id", address_id);
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            // TODO: handle exception
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===address/setDefault传入===" + requestJsonObject.toString());

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {


            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                pd.dismiss();
                JSONObject jo;
                String furl="";
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("===address/setDefault返回===" + jo.toString());
                    AddressModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {

                        jo.put("address_id", address_id);
                        if (fromGetAddressList) {
                            furl = ProtocolConst.ADDRESS_LIST;
                            fromGetAddressList = false;
                        } else {
                            furl = ProtocolConst.ADDRESS_DEFAULT;
                        }

                    }
                    AddressModel.this.onMessageResponse(furl, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===address/setDefault返回===" + arg0.result);
                }

            }
                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                });

    }


    // 删除地址
    public void addressDelete(String address_id) {
        final String url = ProtocolConst.ADDRESS_DELETE;
        pd.show();
        SESSION session = SESSION.getInstance();
        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("address_id", address_id);
        } catch (JSONException e) {
            // TODO: handle exception
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===address/delete传入===" + requestJsonObject.toString());

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
                    LG.i("===address/delete返回===" + jo.toString());
                    AddressModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    AddressModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===address/delete返回===" + arg0.result);
                }

            }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                });
    }


    // 修改地址
    public void addressUpdate(String address_id, String consignee, String tel, String email, String mobile, String zipcode, String address, String country, String province, String city, String
            district) {
        final String url = ProtocolConst.ADDRESS_UPDATE;
        pd.show();
        SESSION session = SESSION.getInstance();
        ADDRESS add = new ADDRESS();

        add.setConsignee(consignee);
        add.setTel(tel);
        add.setEmail(email);
        add.setMobile(mobile);
        add.setZipcode(zipcode);
        add.setAddress(address);
        add.setCountry(country);
        add.setProvince(province);
        add.setCity(city);
        add.setDistrict(district);

        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("address_id", address_id);
            requestJsonObject.put("address", add.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            e.printStackTrace();
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
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                            }
                            AddressModel.this.onMessageResponse(url, jo, responseStatus);
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

    Comparator comparator = new Comparator<CITY>() {
        @Override
        public int compare(CITY lhs, CITY rhs) {
            String a = lhs.getPinyin().substring(0, 1);
            String b = rhs.getPinyin().substring(0, 1);
            int flag = a.compareTo(b);
            if (flag == 0) {
                return a.compareTo(b);
            } else {
                return flag;
            }

        }
    };
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
