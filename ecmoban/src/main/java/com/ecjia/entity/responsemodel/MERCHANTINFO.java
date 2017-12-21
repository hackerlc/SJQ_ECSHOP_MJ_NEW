package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/29.
 */
public class MERCHANTINFO {
    private String id;
    private String seller_name;
    private String seller_logo;
    private String goods_count;
    private String follower;
    private String comment;
    private String comment_server;
    private String comment_delivery;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getSeller_logo() {
        return seller_logo;
    }

    public void setSeller_logo(String seller_logo) {
        this.seller_logo = seller_logo;
    }

    public String getGoods_count() {
        return goods_count;
    }

    public void setGoods_count(String goods_count) {
        this.goods_count = goods_count;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_server() {
        return comment_server;
    }

    public void setComment_server(String comment_server) {
        this.comment_server = comment_server;
    }

    public String getComment_delivery() {
        return comment_delivery;
    }

    public void setComment_delivery(String comment_delivery) {
        this.comment_delivery = comment_delivery;
    }

    public static MERCHANTINFO fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        MERCHANTINFO merchantinfo=new MERCHANTINFO();
        JSONObject commentjson=jsonObject.optJSONObject("comment");
        merchantinfo.id=jsonObject.optString("id");
        merchantinfo.seller_name=jsonObject.optString("seller_name");
        merchantinfo.seller_logo=jsonObject.optString("seller_logo");
        merchantinfo.goods_count=jsonObject.optString("goods_count");
        merchantinfo.follower=jsonObject.optString("follower");
        merchantinfo.comment=commentjson.optString("comment_goods");
        merchantinfo.comment_server=commentjson.optString("comment_server");
        merchantinfo.comment_delivery=commentjson.optString("comment_delivery");


        return merchantinfo;
    }
}
