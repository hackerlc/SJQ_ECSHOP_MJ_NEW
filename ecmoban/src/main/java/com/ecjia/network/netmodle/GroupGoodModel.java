package com.ecjia.network.netmodle;

import android.content.Context;
import android.content.res.Resources;

import com.ecjia.consts.ProtocolConst;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecjia.consts.AndroidManager;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.GROUPGOODS;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.PAGINATION;
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

import java.util.ArrayList;

/**
 * 团购Model
 * Created by Administrator on 2015/8/18.
 */
public class GroupGoodModel extends BaseModel {
    public PAGINATED paginated;
    private Context context;
    private MyProgressDialog pd;
    public ArrayList<GROUPGOODS> groupgoodsArrayList = new ArrayList<GROUPGOODS>();

    public GroupGoodModel(Context context) {
        super();
        pd = MyProgressDialog.createDialog(context);
        Resources resources = context.getResources();
        pd.setMessage(resources.getString(R.string.loading));
    }

    //获取团购列表
    public void getGroupGoods() {
        final String url = ProtocolConst.GOODS_GROUPBUY;
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(10);
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("pagination", pagination.toJson());
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
                        LG.i(arg0.result);
                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                groupgoodsArrayList.clear();
                                JSONArray dataJsonArray = jo.optJSONArray("data");
                                if (null != dataJsonArray && dataJsonArray.length() > 0) {
                                    int size = dataJsonArray.length();
                                    for (int i = 0; i < size; i++) {
                                        groupgoodsArrayList.add(GROUPGOODS.fromJson(dataJsonArray.optJSONObject(i)));
                                    }
                                }
                            }
                            GroupGoodModel.this.onMessageResponse(url, jo, responseStatus);
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

    //获取团购列表更多
    public void getGroupGoodsMore() {
        final String url = ProtocolConst.GOODS_GROUPBUY;
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(groupgoodsArrayList.size() / 10 + 1);
        pagination.setCount(10);

        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("pagination", pagination.toJson());
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
                        LG.i(arg0.result);
                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray dataJsonArray = jo.optJSONArray("data");
                                if (null != dataJsonArray && dataJsonArray.length() > 0) {
                                    int size = dataJsonArray.length();
                                    for (int i = 0; i < size; i++) {
                                        groupgoodsArrayList.add(GROUPGOODS.fromJson(dataJsonArray.optJSONObject(i)));
                                    }
                                }
                            }
                            GroupGoodModel.this.onMessageResponse(url, jo, responseStatus);
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
