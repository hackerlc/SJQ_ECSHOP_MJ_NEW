
package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;


public class ORDER_COMMENTS implements Serializable
{

    private int comment_conformity_of_goods;  //商品符合度评价
    private int comment_merchant_service;  //店家服务态度评价
    private int comment_delivery;  //物流发货速度评价
    private int comment_delivery_sender;  //配送员服务态度评价
    private ArrayList<ORDER_COMMENTS_LIST>  comment_order_list=new ArrayList<ORDER_COMMENTS_LIST>();

    public int getComment_conformity_of_goods() {
        return comment_conformity_of_goods;
    }

    public void setComment_conformity_of_goods(int comment_conformity_of_goods) {
        this.comment_conformity_of_goods = comment_conformity_of_goods;
    }

    public int getComment_merchant_service() {
        return comment_merchant_service;
    }

    public void setComment_merchant_service(int comment_merchant_service) {
        this.comment_merchant_service = comment_merchant_service;
    }

    public int getComment_delivery() {
        return comment_delivery;
    }

    public void setComment_delivery(int comment_delivery) {
        this.comment_delivery = comment_delivery;
    }

    public int getComment_delivery_sender() {
        return comment_delivery_sender;
    }

    public void setComment_delivery_sender(int comment_delivery_sender) {
        this.comment_delivery_sender = comment_delivery_sender;
    }

    public ArrayList<ORDER_COMMENTS_LIST> getComment_order_list() {
        return comment_order_list;
    }

    public void setComment_order_list(ArrayList<ORDER_COMMENTS_LIST> comment_order_list) {
        this.comment_order_list = comment_order_list;
    }

    public static ORDER_COMMENTS fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        ORDER_COMMENTS localItem = new ORDER_COMMENTS();

        JSONArray subItemArray;

        localItem.comment_conformity_of_goods = jsonObject.optInt("comment_conformity_of_goods");
        localItem.comment_merchant_service = jsonObject.optInt("comment_merchant_service");
        localItem.comment_delivery = jsonObject.optInt("comment_delivery");
        localItem.comment_delivery_sender = jsonObject.optInt("comment_delivery_sender");
        subItemArray = jsonObject.optJSONArray("comment_order_list");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                ORDER_COMMENTS_LIST subItem = ORDER_COMMENTS_LIST.fromJson(subItemObject);
                localItem.comment_order_list.add(subItem);
            }
        }

        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("comment_conformity_of_goods", comment_conformity_of_goods);
        localItemObject.put("comment_merchant_service", comment_merchant_service);
        localItemObject.put("comment_delivery", comment_delivery);
        localItemObject.put("comment_delivery_sender", comment_delivery_sender);

        for (int i = 0; i < comment_order_list.size(); i++) {
            ORDER_COMMENTS_LIST itemData = comment_order_list.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("comment_order_list", itemJSONArray);

        return localItemObject;
    }

}
