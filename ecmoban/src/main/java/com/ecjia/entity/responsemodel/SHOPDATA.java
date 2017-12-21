
package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//商品详情页数据
public class SHOPDATA {
    private String id;
    private String seller_name;
    private String seller_logo;
    private String seller_banner;
    private String shop_name;
    private String telephone;
    private String seller_description;
    private Integer follower;
    private String is_follower;
    private String collect_id;
    private String shop_address;
    private String distance;
    private SHOPCOMMENT comment;
    private LOCATION location;
    private GOODSCOUNT goods_count;
    private ArrayList<MENU_BUTTON> menu_button = new ArrayList<MENU_BUTTON>();
    private ArrayList<FAVOUR> favours = new ArrayList<FAVOUR>();

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

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getSeller_banner() {
        return seller_banner;
    }

    public void setSeller_banner(String seller_banner) {
        this.seller_banner = seller_banner;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSeller_description() {
        return seller_description;
    }

    public void setSeller_description(String seller_description) {
        this.seller_description = seller_description;
    }


    public String getIs_follower() {
        return is_follower;
    }

    public void setIs_follower(String is_follower) {
        this.is_follower = is_follower;
    }

    public String getCollect_id() {
        return collect_id;
    }

    public void setCollect_id(String collect_id) {
        this.collect_id = collect_id;
    }

    public SHOPCOMMENT getComment() {
        return comment;
    }

    public void setComment(SHOPCOMMENT comment) {
        this.comment = comment;
    }

    public GOODSCOUNT getGoods_count() {
        return goods_count;
    }

    public void setGoods_count(GOODSCOUNT goods_count) {
        this.goods_count = goods_count;
    }

    public LOCATION getLocation() {
        return location;
    }

    public void setLocation(LOCATION location) {
        this.location = location;
    }


    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Integer getFollower() {
        return follower;
    }

    public void setFollower(Integer follower) {
        this.follower = follower;
    }

    public ArrayList<MENU_BUTTON> getMenu_button() {
        return menu_button;
    }

    public void setMenu_button(ArrayList<MENU_BUTTON> menu_button) {
        this.menu_button = menu_button;
    }

    public ArrayList<FAVOUR> getFavours() {
        return favours;
    }

    public void setFavours(ArrayList<FAVOUR> favours) {
        this.favours = favours;
    }

    public static SHOPDATA fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        SHOPDATA localItem = new SHOPDATA();
        JSONArray subItemArray;

        localItem.id = jsonObject.optString("id");
        localItem.seller_name = jsonObject.optString("seller_name");
        localItem.seller_logo = jsonObject.optString("seller_logo");
        localItem.seller_banner = jsonObject.optString("seller_banner");
        localItem.shop_name = jsonObject.optString("shop_name");
        localItem.shop_address = jsonObject.optString("shop_address");
        localItem.telephone = jsonObject.optString("telephone");
        localItem.seller_description = jsonObject.optString("seller_description");
        localItem.follower = jsonObject.optInt("follower");
        localItem.is_follower = jsonObject.optString("is_follower");
        localItem.collect_id = jsonObject.optString("collect_id");
        localItem.comment = SHOPCOMMENT.fromJson(jsonObject.optJSONObject("comment"));
        localItem.location = LOCATION.fromJson(jsonObject.optJSONObject("location"));
        localItem.goods_count = GOODSCOUNT.fromJson(jsonObject.optJSONObject("goods_count"));
        localItem.distance = jsonObject.optString("distance");

        subItemArray = jsonObject.optJSONArray("menu_button");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                MENU_BUTTON subItem = MENU_BUTTON.fromJson(subItemObject);
                localItem.menu_button.add(subItem);
            }
        }

        subItemArray = jsonObject.optJSONArray("favourable_list");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                FAVOUR subItem = FAVOUR.fromJson(subItemObject);
                localItem.favours.add(subItem);
            }
        }

        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("id", id);
        localItemObject.put("seller_name", seller_name);
        localItemObject.put("seller_logo", seller_logo);
        localItemObject.put("seller_banner", seller_banner);
        localItemObject.put("shop_name", shop_name);
        localItemObject.put("shop_address", shop_address);
        localItemObject.put("telephone", telephone);
        localItemObject.put("seller_description", seller_description);
        localItemObject.put("follower", follower);
        localItemObject.put("is_follower", is_follower);
        localItemObject.put("collect_id", collect_id);
        localItemObject.put("distance", distance);
        if (null != comment) {
            localItemObject.put("comment", comment.toJson());
        }
        if (null != location) {
            localItemObject.put("location", location.toJson());
        }
        if (null != goods_count) {
            localItemObject.put("goods_count", goods_count.toJson());
        }

        for (int i = 0; i < menu_button.size(); i++) {
            MENU_BUTTON itemData = menu_button.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("menu_button", itemJSONArray);

        JSONArray favourable_listJSONArray = new JSONArray();
        for (int i = 0; i < favours.size(); i++) {
            FAVOUR itemData = favours.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            favourable_listJSONArray.put(itemJSONObject);
        }
        localItemObject.put("favourable_list", favourable_listJSONArray);

        return localItemObject;
    }

}
