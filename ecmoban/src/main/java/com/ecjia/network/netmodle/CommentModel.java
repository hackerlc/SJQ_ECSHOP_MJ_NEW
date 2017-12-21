package com.ecjia.network.netmodle;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.ORDER_COMMENTS_LIST;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.PAGINATION;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.view.activity.goodsdetail.model.COMMENT_LIST;
import com.ecjia.util.LG;

import com.ecjia.widgets.dialog.MyProgressDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class CommentModel extends BaseModel {

    public PAGINATED paginated;

    //    public ORDER_COMMENTS order_comments=new ORDER_COMMENTS();
    public int comment_conformity_of_goods;  //商品符合度评价
    public int comment_merchant_service;  //店家服务态度评价
    public int comment_delivery;  //物流发货速度评价
    public int comment_delivery_sender;  //配送员服务态度评价
    public ArrayList<ORDER_COMMENTS_LIST> comment_order_list = new ArrayList<ORDER_COMMENTS_LIST>();
    public ArrayList<String> comment_pic_list = new ArrayList<String>();
    public MyProgressDialog pd;
    public int comment_goods;
    public String comment_content;

    public CommentModel(Context context) {
        super(context);
        pd = MyProgressDialog.createDialog(context);
        Resources resources = AppConst.getResources(context);
        pd.setMessage(resources.getString(R.string.loading));
    }


    public void getOrderCommentList(String order_id) {
        final String url = ProtocolConst.ORDERS_COMMENT;
        SESSION session = SESSION.getInstance();

        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("order_id", order_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params = addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===orders/comment传入===" + requestJsonObject.toString());

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
                    LG.i("===orders/comment返回===" + jo.toString());
                    CommentModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject dataJsonObject = jo.optJSONObject("data");

                        CommentModel.this.comment_conformity_of_goods = dataJsonObject.optInt("comment_conformity_of_goods");
                        CommentModel.this.comment_merchant_service = dataJsonObject.optInt("comment_merchant_service");
                        CommentModel.this.comment_delivery = dataJsonObject.optInt("comment_delivery");
                        CommentModel.this.comment_delivery_sender = dataJsonObject.optInt("comment_delivery_sender");
                        JSONArray dataJsonArray = dataJsonObject.optJSONArray("comment_order_list");
                        comment_order_list.clear();
                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
                            for (int i = 0; i < dataJsonArray.length(); i++) {
                                ORDER_COMMENTS_LIST JsonObject = ORDER_COMMENTS_LIST.fromJson(dataJsonArray.getJSONObject(i));
                                comment_order_list.add(JsonObject);
                            }
                        }

                    }
                    CommentModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===comments返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });


    }

    public void getOrderCommentsDetail(String rec_id) {
        pd.show();
        final String url = ProtocolConst.ORDERS_COMMENT_DETAIL;
        SESSION session = SESSION.getInstance();
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("rec_id", rec_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params = addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===orders/comment/detail传入===" + requestJsonObject.toString());

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
                    LG.i("===orders/comment/detail返回===" + jo.toString());
                    callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject dataJsonObject = jo.optJSONObject("data");
                        CommentModel.this.comment_goods = dataJsonObject.optInt("comment_goods");
                        CommentModel.this.comment_content = dataJsonObject.optString("comment_content");
                        JSONArray dataJsonArray = dataJsonObject.optJSONArray("comment_image");
                        comment_pic_list.clear();
                        if (null != dataJsonArray && dataJsonArray.length() > 0) {
                            for (int i = 0; i < dataJsonArray.length(); i++) {
                                String JsonObject = (String) dataJsonArray.get(i);
                                comment_pic_list.add(JsonObject);
                            }
                        }
                    }
                    CommentModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===orders/comment/detail返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });


    }


    public ArrayList<COMMENT_LIST> goods_comment_list = new ArrayList<>();
    public int goods_comment_count;
    public int goods_comment_positive;
    public int goods_comment_moderate;
    public int goods_comment_negative;
    public int goods_comment_showorder;

    public void getCommentList(String goods_id,String type, boolean isPdShow) {
        final String url = ProtocolConst.GOODS_COMMENT_LIST;
        if (isPdShow) {
            pd.show();
        }
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(10);

        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("goods_id", goods_id);
            requestJsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params = addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===comments传入===" + requestJsonObject.toString());

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
                    LG.i("===comments返回===" + jo.toString());
                    CommentModel.this.callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject dataJsonArray = jo.optJSONObject("data");
                        goods_comment_count = dataJsonArray.optInt("comment_count");
                        goods_comment_positive = dataJsonArray.optInt("comment_positive");
                        goods_comment_moderate = dataJsonArray.optInt("comment_moderate");
                        goods_comment_negative = dataJsonArray.optInt("comment_negative");
                        goods_comment_showorder= dataJsonArray.optInt("comment_showorder");
                        JSONArray array = dataJsonArray.optJSONArray("comment_list");
                        goods_comment_list.clear();
                        if (array!=null && array.length()>0){
                            for ( int i = 0 ;i<array.length();i++){
                                goods_comment_list.add(COMMENT_LIST.fromJson(array.optJSONObject(i)));
                            }
                        }
                        paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                    }
                    CommentModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===comments返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }

    public void getCommentsMore(String goods_id ,String type ) {
        pd.show();
        final String url = ProtocolConst.GOODS_COMMENT_LIST;
        SESSION session = SESSION.getInstance();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(goods_comment_list.size() / 10 + 1);
        pagination.setCount(10);
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("goods_id", goods_id);
            requestJsonObject.put("type", type);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params = addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===comments传入===" + requestJsonObject.toString());

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
                    LG.i("===comments返回===" + jo.toString());
                    callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject dataJsonArray = jo.optJSONObject("data");
                        goods_comment_count = dataJsonArray.optInt("comment_count");
                        goods_comment_positive = dataJsonArray.optInt("comment_positive");
                        goods_comment_moderate = dataJsonArray.optInt("comment_moderate");
                        goods_comment_negative = dataJsonArray.optInt("comment_negative");
                        goods_comment_showorder= dataJsonArray.optInt("comment_showorder");
                        JSONArray array = dataJsonArray.optJSONArray("comment_list");
                        if (array!=null && array.length()>0){
                            for ( int i = 0 ;i<array.length();i++){
                                goods_comment_list.add(COMMENT_LIST.fromJson(array.optJSONObject(i)));
                            }
                        }
                        paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                    }
                    CommentModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===comments返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });


    }

    //创建商品评价(1.7.0)
    public void createComments(String rec_id, String content, int comment_rank, int is_anonymous,
                               final ArrayList<String> comment_image, final ArrayList<String> stringArrayList) {
        final String url = ProtocolConst.COMMENTS_CREATE;
        SESSION session = SESSION.getInstance();

        pd.show();


        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("token", session.sid);
            requestJsonObject.put("rec_id", rec_id);
            requestJsonObject.put("is_anonymous", is_anonymous);
            requestJsonObject.put("content", content);
            requestJsonObject.put("comment_rank", comment_rank);
            requestJsonObject.put("comment_image", "upload_imgs");
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams();
        params = addHeader(params);
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===comment/create传入===" + requestJsonObject.toString());

        for (int i = 0; i < stringArrayList.size(); i++) {
            final File imageFile = new File(stringArrayList.get(i));
            params.addBodyParameter("upload_imgs["+i+"]", imageFile, "image/png");
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
                    LG.i("===comment/create返回===" + jo.toString());
                    callback(jo);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    CommentModel.this.onMessageResponse(url, jo, responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===comment/create返回===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                if (isUploading) {
                    LG.e("==uploadresult==" + current + "/" + total);
                }
                super.onLoading(total, current, isUploading);
            }
        });


    }

    /**
     * 保存方法
     *
     * @param filePath 文件路径
     * @param picName  文件大小
     * @param bitmap   bitmap图
     */
    public static void saveBitmap(String filePath, String picName, Bitmap bitmap) {

        File path = new File(filePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        File f = new File(filePath, picName);
        if (f.exists()) {
            // 如果路径不存在,则创建
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
