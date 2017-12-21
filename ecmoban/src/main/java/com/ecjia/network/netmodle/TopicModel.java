package com.ecjia.network.netmodle;

import android.content.Context;

import com.ecjia.consts.ProtocolConst;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecjia.consts.AndroidManager;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.PAGINATION;
import com.ecjia.entity.responsemodel.SELLERGOODS;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.entity.responsemodel.TOPIC;
import com.ecjia.util.LG;
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
 * Created by Administrator on 2015/12/18.
 */
public class TopicModel extends BaseModel{
    private int page = 1;
    public PAGINATED paginated;
    public MyProgressDialog pd;
    public ArrayList<TOPIC> topics=new ArrayList<TOPIC>();
    public ArrayList<SELLERGOODS> topic_info_list=new ArrayList<SELLERGOODS>();
    public TOPIC topic;
    public TopicModel(Context context){
        super(context);
        pd=MyProgressDialog.createDialog(context);
        pd.setMessage(res.getString(R.string.loading));
    }
    public void getTopicList(){
        final String url= ProtocolConst.TOPIC_LIST;
        pd.show();
        final SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(8);

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("session",session.toJson());
            requestJsonObject.put("pagination",pagination.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        LG.e("result=="+arg0.result);
                        pd.dismiss();
                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));

                            if (responseStatus.getSucceed() == 1) {
                                topics.clear();
                                JSONArray dataJsonArray = jo.optJSONArray("data");
                                if (null != dataJsonArray && dataJsonArray.length() > 0) {
                                    for (int i = 0; i < dataJsonArray.length(); i++) {
                                        topics.add(TOPIC.fromJson(dataJsonArray.optJSONObject(i)));
                                    }
                                }
                                paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
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

    public void getTopicListMore(){
        final String url=ProtocolConst.TOPIC_LIST;
        final SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(topics.size()/8+1);
        pagination.setCount(8);

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("session",session.toJson());
            requestJsonObject.put("pagination",pagination.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));

                            if (responseStatus.getSucceed() == 1) {
                                JSONArray dataJsonArray = jo.optJSONArray("data");
                                if (null != dataJsonArray && dataJsonArray.length() > 0) {
                                    for (int i = 0; i < dataJsonArray.length(); i++) {
                                        topics.add(TOPIC.fromJson(dataJsonArray.optJSONObject(i)));
                                    }
                                }
                                paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
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

    public void getTopicInfo(String topic_id,String type,String sort_by){
        final String url=ProtocolConst.TOPIC_INFO;
        final SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(8);
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("topic_id",topic_id);
            requestJsonObject.put("type",type);
            requestJsonObject.put("sort_by",sort_by);
            requestJsonObject.put("session",session.toJson());
            requestJsonObject.put("pagination",pagination.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.e("request=="+requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        pd.dismiss();
                        JSONObject jo;
                        try {
                            LG.e("result=="+arg0.result);
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                topic_info_list.clear();
                                topic = TOPIC.fromJson(jo.optJSONObject("data"));
                                if (null!=topic&&topic.getSellergoods().size()>0) {
                                    int size=topic.getSellergoods().size();
                                    for (int i = 0; i < size; i++) {
                                        topic_info_list.add(topic.getSellergoods().get(i));
                                    }
                                }
                                paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
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

    public void getTopicInfoMore(String topic_id,String type,String sort_by){
        final String url=ProtocolConst.TOPIC_INFO;
        final SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(topic_info_list.size()/8+1);
        pagination.setCount(8);

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("topic_id",topic_id);
            requestJsonObject.put("type",type);
            requestJsonObject.put("sort_by",sort_by);
            requestJsonObject.put("session",session.toJson());
            requestJsonObject.put("pagination",pagination.toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            LG.i("request=="+requestJsonObject.toString());
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        LG.e("result=="+arg0.result);
                        pd.dismiss();
                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                topic = TOPIC.fromJson(jo.optJSONObject("data"));
                                if (null!=topic&&topic.getSellergoods().size()>0) {
                                    int size=topic.getSellergoods().size();
                                    for (int i = 0; i < size; i++) {
                                        topic_info_list.add(topic.getSellergoods().get(i));
                                    }
                                }
                                paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
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

}
