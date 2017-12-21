
package com.ecjia.view.activity.goodsdetail.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class COMMENT_LIST {

    private String comment_id;

    private String user_name;

    private String avatar_img;

    private String comment_rank;

    private String comment_content;

    private String comment_time;

    private String order_time;

    private String goods_attr;

    private ArrayList<String> comment_image = new ArrayList<>();

    public String getGoods_attr() {
        return goods_attr;
    }

    public void setGoods_attr(String goods_attr) {
        this.goods_attr = goods_attr;
    }

    public ArrayList<String> getComment_image() {
        return comment_image;
    }

    public void setComment_image(ArrayList<String> comment_image) {
        this.comment_image = comment_image;
    }

    public String getComment_rank() {
        return comment_rank;
    }

    public void setComment_rank(String comment_rank) {
        this.comment_rank = comment_rank;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAvatar_img() {
        return avatar_img;
    }

    public void setAvatar_img(String avatar_img) {
        this.avatar_img = avatar_img;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public static COMMENT_LIST fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        COMMENT_LIST localItem = new COMMENT_LIST();
        localItem.comment_id = jsonObject.optString("comment_id");
        localItem.user_name = jsonObject.optString("user_name");
        localItem.comment_rank = jsonObject.optString("comment_rank");
        localItem.avatar_img = jsonObject.optString("avatar_img");
        localItem.comment_content = jsonObject.optString("comment_content");
        localItem.comment_time = jsonObject.optString("comment_time");
        localItem.order_time = jsonObject.optString("order_time");
        localItem.goods_attr = jsonObject.optString("goods_attr");
        JSONArray array = jsonObject.optJSONArray("comment_image");
        localItem.comment_image.clear();
        if (array != null && array.length() > 0) {

            for (int i = 0; i < array.length(); i++) {
                localItem.comment_image.add(array.optString(i));
            }
        }
        return localItem;
    }

}
