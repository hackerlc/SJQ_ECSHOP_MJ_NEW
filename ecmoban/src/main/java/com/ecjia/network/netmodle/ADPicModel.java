package com.ecjia.network.netmodle;

import android.content.Context;
import android.text.TextUtils;

import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.ADPIC;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.DataCleanManager;
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
 * Created by Adam on 2015/6/18.
 */
public class ADPicModel extends BaseModel {

    public ArrayList<ADPIC> adPics = new ArrayList<ADPIC>();
    public ArrayList<ADPIC> adPicsForStartPage = new ArrayList<ADPIC>();

    public ADPicModel(Context context) {
        super(context);
    }

    public void getADPIC() {
        String url = ProtocolConst.ADPIC;
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
                LG.i("广告图的返回值==========" + arg0.result);
                try {
                    jo = new JSONObject(arg0.result);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONArray idPicsJsonArray = jo.optJSONArray("data");
                        //比较获取的data与本地的data值是否相同，是下载
                        adPics.clear();
                        if (null != idPicsJsonArray && idPicsJsonArray.length() > 0) {
                            for (int i = 0; i < idPicsJsonArray.length(); i++) {
                                JSONObject idPicJsonObject = idPicsJsonArray.getJSONObject(i);
                                ADPIC simpleadpic = ADPIC.fromJson(idPicJsonObject);
                                adPics.add(simpleadpic);
                            }
                            int num = 0;
                            for (int i = 0; i < adPics.size(); i++) {
                                if (!TextUtils.isEmpty(adPics.get(i).getAd_img())) {
                                    num += 1;
                                }
                            }
                            if (num == adPics.size()) {
                                fileSave(AndroidManager.APPCACHEPATH, arg0.result, "image.dat");//保存到缓存
                                saveADpic();//下载图片
                            }
                        } else {
                            fileSave(AndroidManager.APPCACHEPATH, "", "image.dat");//保存到缓存
                        }

                    } else {

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

    private PrintStream ps = null;

    public void fileSave(String path, String result, String name) {

        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        } else {
            LG.i("文件存在");
            if (TextUtils.isEmpty(result)) {
                LG.i("广告图数据是空的");
                DataCleanManager.deleteFile(path, name);
                return;
            }
        }
        File file = new File(path + "/" + name);
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


    public void readADPICCache() {
        String path = AndroidManager.APPCACHEPATH + "/image.dat";
        File f1 = new File(path);
        if (f1.exists()) {
            try {
                InputStream is = new FileInputStream(f1);
                InputStreamReader input = new InputStreamReader(is, "UTF-8");
                BufferedReader bf = new BufferedReader(input);
                ADPICCache(bf.readLine());
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

    private void ADPICCache(String result) {
        try {
            if (result != null) {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray adpicArray = jsonObject.optJSONArray("data");
                adPicsForStartPage.clear();
                for (int i = 0; i < adpicArray.length(); i++) {
                    JSONObject adjsonObject = adpicArray.getJSONObject(i);
                    ADPIC adpic = ADPIC.fromJson(adjsonObject);
                    adpic.setAd_img(AndroidManager.ADPICSPATH + "/" + i + ".png");//地址改为本地地址
                    adPicsForStartPage.add(adpic);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void saveADpic() {//下载并保存
        for (int i = 0; i < adPics.size(); i++) {
            String mFileName = AndroidManager.ADPICSPATH + "/" + i + ".png";
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.download(adPics.get(i).getAd_img(), mFileName, false, true, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> fileResponseInfo) {
                    LG.i("===============下载图片成功");
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    LG.i("===============下载图片失败");
                }
            });
        }

    }

}
