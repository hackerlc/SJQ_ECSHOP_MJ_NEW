package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/22.
 */
public class SELLERINFO {
    private String id;
    private String rec_id;
    private int new_goods;
    private String seller_name;
    private String seller_category;
    private String seller_logo;
    private ArrayList<SELLERGOODS> sellergoods = new ArrayList<SELLERGOODS>();
    private Integer follower;
    private String is_follower;
    private boolean isChoose; //用于店铺收藏时，编辑删除，判断是否选中
    private boolean isClicked;

    private String seller_banner;//大图

    private String seller_desc;//暂定

    private String distance;//距离

    private LOCATION location;

    public String getSeller_banner() {
        return seller_banner;
    }

    public void setSeller_banner(String seller_banner) {
        this.seller_banner = seller_banner;
    }

    public String getSeller_desc() {
        return seller_desc;
    }

    public void setSeller_desc(String seller_desc) {
        this.seller_desc = seller_desc;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean isChoose) {
        this.isChoose = isChoose;
    }

    public String getRec_id() {
        return rec_id;
    }

    public void setRec_id(String rec_id) {
        this.rec_id = rec_id;
    }

    public int getNew_goods() {
        return new_goods;
    }

    public void setNew_goods(int new_goods) {
        this.new_goods = new_goods;
    }

    public String getId() {
        return id;
    }

    public String getSeller_category() {
        return seller_category;
    }

    public void setSeller_category(String seller_category) {
        this.seller_category = seller_category;
    }

    public ArrayList<SELLERGOODS> getSellergoods() {
        return sellergoods;
    }

    public void setSellergoods(ArrayList<SELLERGOODS> sellergoods) {
        this.sellergoods = sellergoods;
    }

    public Integer getFollower() {
        return follower;
    }

    public void setFollower(Integer follower) {
        this.follower = follower;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getIs_follower() {
        return is_follower;
    }

    public void setIs_follower(String is_follower) {
        this.is_follower = is_follower;
    }

    public LOCATION getLocation() {
        return location;
    }

    public void setLocation(LOCATION location) {
        this.location = location;
    }

    public static SELLERINFO fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        SELLERINFO localItem = new SELLERINFO();

        JSONArray array = jsonObject.optJSONArray("seller_goods");

        localItem.id = jsonObject.optString("id");
        localItem.rec_id = jsonObject.optString("rec_id");
        localItem.new_goods = jsonObject.optInt("new_goods");
        localItem.seller_name = jsonObject.optString("seller_name");

        localItem.seller_category = jsonObject.optString("seller_category");
        localItem.seller_logo = jsonObject.optString("seller_logo");
        if (array != null && array.length() > 0) {
            for (int i = 0; i < array.length(); i++) {
                localItem.sellergoods.add(SELLERGOODS.fromJson(array.getJSONObject(i)));
            }
        }
        localItem.isClicked = false;
        localItem.follower = jsonObject.optInt("follower");
        localItem.is_follower = jsonObject.optString("is_follower");

        localItem.seller_banner = jsonObject.optString("seller_banner");

        localItem.seller_desc = jsonObject.optString("seller_desc");

        localItem.distance = jsonObject.optString("distance");

        localItem.location = LOCATION.fromJson(jsonObject.optJSONObject("location"));

        return localItem;
    }
}
