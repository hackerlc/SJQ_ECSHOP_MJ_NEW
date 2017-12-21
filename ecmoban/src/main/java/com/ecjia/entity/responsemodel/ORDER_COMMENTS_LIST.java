
package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class ORDER_COMMENTS_LIST implements Serializable
{

    private String rec_id;   //订单商品id
    private String goods_name;   //商品名称
    private String goods_price;   //商品价格
    private PHOTO   img;
    private int is_commented;  //是否已评论（0代表可评论）
    private int is_showorder;  //是否已晒单（0代表可晒单）
    private int type;  //已评论已晒单0  未评论未晒单1  已评论未晒单2

    public String getRec_id() {
        return rec_id;
    }

    public void setRec_id(String rec_id) {
        this.rec_id = rec_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public PHOTO getImg() {
        return img;
    }

    public void setImg(PHOTO img) {
        this.img = img;
    }

    public int getIs_commented() {
        return is_commented;
    }

    public void setIs_commented(int is_commented) {
        this.is_commented = is_commented;
    }

    public int getIs_showorder() {
        return is_showorder;
    }

    public void setIs_showorder(int is_showorder) {
        this.is_showorder = is_showorder;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static ORDER_COMMENTS_LIST fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        ORDER_COMMENTS_LIST localItem = new ORDER_COMMENTS_LIST();

        JSONArray subItemArray;

        localItem.rec_id = jsonObject.optString("rec_id");
        localItem.goods_name = jsonObject.optString("goods_name");
        localItem.goods_price = jsonObject.optString("goods_price");
        localItem.img = PHOTO.fromJson(jsonObject.optJSONObject("img"));
        localItem.is_commented = jsonObject.optInt("is_commented");
        localItem.is_showorder = jsonObject.optInt("is_showorder");

        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("rec_id", rec_id);
        localItemObject.put("goods_name", goods_name);
        localItemObject.put("goods_price", goods_price);
        if(null!=img)
        {
            localItemObject.put("img", img.toJson());
        }
        localItemObject.put("is_commented", is_commented);
        localItemObject.put("is_showorder", is_showorder);

        return localItemObject;
    }

}
