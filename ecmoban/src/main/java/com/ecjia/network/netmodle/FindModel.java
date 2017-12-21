package com.ecjia.network.netmodle;

import android.content.Context;
import android.content.res.Resources;

import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.HOTNEWS;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.PAGINATION;
import com.ecjia.entity.responsemodel.QUICK;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
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

/**
 * Created by Administrator on 2015/9/15.
 */
public class FindModel extends BaseModel {
    private Context context;
    public ArrayList<QUICK> findItemlist = new ArrayList<QUICK>();
    public ArrayList<ArrayList<HOTNEWS>> hotnewslist = new ArrayList<ArrayList<HOTNEWS>>();
    private ArrayList<ArrayList<HOTNEWS>> temp = new ArrayList<ArrayList<HOTNEWS>>();
    String pkName;
    public String rootpath;
    private JSONObject jsonObject;
    private STATUS status;
    public MyProgressDialog pd;
    public PAGINATED paginated;
    private int page = 1;

    public FindModel(Context context) {
        super(context);
        this.context = context;
        pd = MyProgressDialog.createDialog(context);
        Resources resources = AppConst.getResources(context);
        pd.setMessage(resources.getString(R.string.loading));
        pkName = mContext.getPackageName();
        rootpath = context.getCacheDir() + "/ECJia/cache";//获取缓存信息
    }

    //获取自定义发现
    public void getHomeDiscover() {
        final String url = ProtocolConst.HOME_DISCOVER;
        JSONObject requestJsonObject = new JSONObject();
        try {
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
                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            LG.i("find==" + jo.toString());
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                fileSave(jo.toString(), "findData");
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

    //获取新闻
    public void getHomeNews() {
        pd.show();
        page = 1;
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(8);
        final String url = ProtocolConst.HOME_HOT_NEWS;
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("pagination", pagination.toJson());
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
                        pd.dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        pd.dismiss();
                        hotnewslist.clear();
                        temp.clear();
                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            LG.i("news==" + jo.toString());
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray jsonArray = jo.optJSONArray("data");
                                if (null != jsonArray && jsonArray.length() > 0) {
                                    int size = jsonArray.length();
                                    ArrayList<HOTNEWS> arrayList = null;
                                    for (int i = 0; i < size; i++) {
                                        JSONArray array = jsonArray.optJSONArray(i);
                                        if (null != array && array.length() > 0) {
                                            int arraysize = array.length();
                                            arrayList = new ArrayList<HOTNEWS>();
                                            for (int j = 0; j < arraysize; j++) {
                                                arrayList.add(HOTNEWS.fromJson(array.optJSONObject(j)));
                                            }
                                        }
                                        temp.add(arrayList);
                                    }
                                }
                                /**
                                 * 暂时不需要做倒序
                                 */
                                if (null != temp && temp.size() > 0) {
                                    for (int k = temp.size() - 1; k >= 0; k--) {
                                        hotnewslist.add(temp.get(k));
                                    }
                                }
                                paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                            }
                            FindModel.this.onMessageResponse(url, jo, responseStatus);
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

    public void getHomeNewsMore() {
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(hotnewslist.size() / 8 + 1);
        pagination.setCount(8);
        final String url = ProtocolConst.HOME_HOT_NEWS;
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("pagination", pagination.toJson());
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
                        pd.dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        hotnewslist.clear();
                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            LG.i("news==" + jo.toString());
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray jsonArray = jo.optJSONArray("data");
                                if (null != jsonArray && jsonArray.length() > 0) {
                                    int size = jsonArray.length();
                                    ArrayList<HOTNEWS> arrayList = null;
                                    for (int i = 0; i < size; i++) {
                                        JSONArray array = jsonArray.optJSONArray(i);
                                        if (null != array && array.length() > 0) {
                                            int arraysize = array.length();
                                            arrayList = new ArrayList<HOTNEWS>();
                                            for (int j = 0; j < arraysize; j++) {
                                                arrayList.add(HOTNEWS.fromJson(array.optJSONObject(j)));
                                            }
                                        }
                                        temp.add(arrayList);
                                    }
                                }
                                if (null != temp && temp.size() > 0) {
                                    for (int k = temp.size() - 1; k >= 0; k--) {
                                        hotnewslist.add(temp.get(k));
                                    }
                                }
                                paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                            }
                            FindModel.this.onMessageResponse(url, jo, responseStatus);
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

    //获取自定义发现数据
    public void getFindCacheList() {
        if (findItemlist.size() > 0) {//从内存中读取
            try {
                FindModel.this.onMessageResponse(ProtocolConst.HOME_DISCOVER, jsonObject, status);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {//从文件中读取
            readfindDataCache();
        }
    }

    private void readfindDataCache() {

        String path = rootpath + "/" + pkName + "/findData.dat";
        File f1 = new File(path);
        if (f1.exists()) {
            try {
                InputStream is = new FileInputStream(f1);
                InputStreamReader input = new InputStreamReader(is, "UTF-8");
                BufferedReader bf = new BufferedReader(input);

                findDataCache(bf.readLine());

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

    private void findDataCache(String result) {
        final String url = ProtocolConst.HOME_DISCOVER;
        try {
            if (result != null) {
                jsonObject = new JSONObject(result);
                status = STATUS.fromJson(jsonObject.optJSONObject("status"));
                if (status.getSucceed() == 1) {
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    findItemlist.clear();
                    if (null != jsonArray && jsonArray.length() > 0) {
                        int size = jsonArray.length();
                        for (int i = 0; i < size; i++) {
                            findItemlist.add(QUICK.fromJson(jsonArray.optJSONObject(i)));
                        }
                    }
                }
                FindModel.this.onMessageResponse(url, jsonObject, status);

            } else {
                LG.i("运行");
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

}
