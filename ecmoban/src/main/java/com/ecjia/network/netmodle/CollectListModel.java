package com.ecjia.network.netmodle;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;

import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.COLLECT_LIST;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.PAGINATION;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.util.LG;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class CollectListModel extends BaseModel {

	public ArrayList<COLLECT_LIST> collectList = new ArrayList<COLLECT_LIST>();
	public MyProgressDialog pd;
	public PAGINATED paginated;
	public CollectListModel(Context context) {
		super(context);
		pd=MyProgressDialog.createDialog(context);
        Resources resources = AppConst.getResources(context);
        pd.setMessage(resources.getString(R.string.loading));
	}


    public void  getCollectList(final Handler handler,boolean isfalse){
        String url = ProtocolConst.COLLECT_LIST;
        if(isfalse){
            pd.show();
        }

        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(10);

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("session",session.toJson());
            requestJsonObject.put("pagination",pagination.toJson());
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
                            LG.i(jo.toString());
                            CollectListModel.this.callback(jo);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray dataJsonArray = jo.optJSONArray("data");
                                collectList.clear();
                                if (null != dataJsonArray && dataJsonArray.length() > 0) {
                                    collectList.clear();
                                    for (int i = 0; i < dataJsonArray.length(); i++) {
                                        JSONObject collectJsonObject = dataJsonArray.getJSONObject(i);
                                        COLLECT_LIST collectItem = COLLECT_LIST.fromJson(collectJsonObject);
                                        collectList.add(collectItem);
                                    }
                                }
                                paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                                Message msg = new Message();
                                msg.obj = ProtocolConst.COLLECT_LIST;
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


    public void  getCollectListMore(final Handler handler){
        pd.show();

        String url = ProtocolConst.COLLECT_LIST;
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(collectList.size()/10+1);
        pagination.setCount(10);

        JSONObject requestJsonObject = new JSONObject();

        try
        {
            requestJsonObject.put("session",session.toJson());
            requestJsonObject.put("pagination",pagination.toJson());
            requestJsonObject.put("device",DEVICE.getInstance().toJson());
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
                            LG.i(jo.toString());
                            CollectListModel.this.callback(jo);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray dataJsonArray = jo.optJSONArray("data");
                                if (null != dataJsonArray && dataJsonArray.length() > 0) {
                                    for (int i = 0; i < dataJsonArray.length(); i++) {
                                        JSONObject collectJsonObject = dataJsonArray.getJSONObject(i);
                                        COLLECT_LIST collectItem = COLLECT_LIST.fromJson(collectJsonObject);
                                        collectList.add(collectItem);
                                    }
                                }
                                paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                                Message msg = new Message();
                                msg.obj = ProtocolConst.COLLECT_LIST;
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

	
	
	// 删除收藏商品
    public  void collectDelete(String rec_id,final Handler handler){
        String url = ProtocolConst.COLLECT_DELETE;
        pd.show();

        SESSION session = SESSION.getInstance();
        JSONObject requestJsonObject = new JSONObject();

        try
        {
            requestJsonObject.put("session",session.toJson());
            requestJsonObject.put("rec_id",rec_id);
            requestJsonObject.put("device",DEVICE.getInstance().toJson());
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
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {

                                Message msg = new Message();
                                msg.obj = ProtocolConst.COLLECT_DELETE;
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
