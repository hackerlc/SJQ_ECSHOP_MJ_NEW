package com.ecjia.network.query;

import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.entity.responsemodel.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/2/10 15:28.
 */

public class GoodsQuery extends PageQuery{

    protected static GoodsQuery instance;

    public static GoodsQuery getInstance() {
        if (instance == null) {
            synchronized (GoodsQuery.class) {
                if (instance == null) {
                    instance = new GoodsQuery();
                }
            }
        }
        return instance;
    }

    public GoodsQuery(){

    }

    public String getSubWeb(int page,String city){
        requestJsonObject=new JSONObject();
        try {
            requestJsonObject.put("cityalias", city);
        }catch (JSONException e){
            e.getMessage();
        }
        return super.getQuery(page);
    }
    public String getTimePanic(int page,boolean type){
        requestJsonObject=new JSONObject();
        try {
            requestJsonObject.put("type", type? 1:2);
        }catch (JSONException e){
            e.getMessage();
        }
        return super.getQuery(page);
    }

    public String getQuery(int page, FILTER filter){
        requestJsonObject=new JSONObject();
        try {
            requestJsonObject.put("filter", filter.toJson());
        }catch (JSONException e){
            e.getMessage();
        }
        return super.getQuery(page);
    }

    public String getShippingFee(String goodId, String area_id){
        requestJsonObject=new JSONObject();
        try {
            requestJsonObject.put("session", SESSION.getInstance().toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("goods_id", goodId);
            requestJsonObject.put("area_id", area_id);
        }catch (JSONException e){
            e.getMessage();
        }
        return requestJsonObject.toString();
    }

    //获取退换货原因
    public String getReturnause(){
        requestJsonObject=new JSONObject();
//        try {
//            requestJsonObject.put("session", SESSION.getInstance().toJson());
//            requestJsonObject.put("device", DEVICE.getInstance().toJson());
//        }catch (JSONException e){
//            e.getMessage();
//        }
//        return requestJsonObject.toString();
        return super.get();
    }

    //订单退款、退货退款
    public String getRejected(String order_id , String rejected_type, String rejected_reason  ,
                              String rejected_value , String rejected_explain ,
                              String first_image, String second_image, String third_image){
        requestJsonObject=new JSONObject();
        try {
//            requestJsonObject.put("session", SESSION.getInstance().toJson());
//            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("order_id", order_id);
            requestJsonObject.put("rejected_type", rejected_type);
            requestJsonObject.put("rejected_reason", rejected_reason);
            requestJsonObject.put("rejected_value", rejected_value);
            requestJsonObject.put("rejected_explain", rejected_explain);
            requestJsonObject.put("first_image", first_image);
            requestJsonObject.put("second_image", second_image);
            requestJsonObject.put("third_image", third_image);
        }catch (JSONException e){
            e.getMessage();
        }
        return super.get();
    }

    //订单退款、退货退款 进度详情
    public String getReturnDetail(String ret_id  ){
        requestJsonObject=new JSONObject();
        try {
            requestJsonObject.put("ret_id", ret_id);
        }catch (JSONException e){
            e.getMessage();
        }
        return super.get();
    }

    //订单退款、退货退款，提交退货物流信息
    public String getSendCourier(String ret_id,String shipping_id,String invoice_no,String images){
        requestJsonObject=new JSONObject();
        try {
            requestJsonObject.put("ret_id", ret_id);
            requestJsonObject.put("shipping_id", shipping_id);
            requestJsonObject.put("invoice_no", invoice_no);
            requestJsonObject.put("images", images);
        }catch (JSONException e){
            e.getMessage();
        }
        return super.get();
    }

}
