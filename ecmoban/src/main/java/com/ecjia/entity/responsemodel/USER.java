package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;


public class USER implements Serializable {


    private String id;

    private String name;

    private String rank_name;//（名称：rank_level为0（未认证会员），rank_level为1（认证会员））

    private int rank_level;//是否采购商认证：0未认证（默认），1已认证）

    private String collection_num;

    private String email;

    private String mobile_phone;

    private String avatar_img;//20150623更新，用户头像

    private ORDER_NUM order_num;

    private String formated_user_money;

    private String user_points;

    private String user_bonus_count;// 红包个数

    private String collect_merchant_num;

    private String signup_reward_url;

    private String users_real;  //是否采购商实名认证(四季青) -1为未认证   0为认证已提交未审核    1为已认证   2为认证不通过

    private String purchaser_valid;  //采购商验证 0未提交 1认证已提交未审核 2已审核认证   3认证不通过

    private String user_coupon_count;//优惠券个数

    private String password_null;//优惠券个数

    public String getUser_coupon_count() {
        return user_coupon_count;
    }

    public void setUser_coupon_count(String user_coupon_count) {
        this.user_coupon_count = user_coupon_count;
    }

    public String getUsers_real() {
        return users_real;
    }

    public void setUsers_real(String users_real) {
        this.users_real = users_real;
    }

    public String getPurchaser_valid() {
        return purchaser_valid;
    }

    public void setPurchaser_valid(String purchaser_valid) {
        this.purchaser_valid = purchaser_valid;
    }

    public String getSignup_reward_url() {
        return signup_reward_url;
    }

    public void setSignup_reward_url(String signup_reward_url) {
        this.signup_reward_url = signup_reward_url;
    }

    public String getCollect_merchant_num() {
        return collect_merchant_num;
    }

    public void setCollect_merchant_num(String collect_merchant_num) {
        this.collect_merchant_num = collect_merchant_num;
    }

    public ArrayList<BONUS> getBonus_list() {
        return bonus_list;
    }

    public void setBonus_list(ArrayList<BONUS> bonus_list) {
        this.bonus_list = bonus_list;
    }

    private ArrayList<BONUS> bonus_list = new ArrayList<BONUS>();

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFormated_user_money() {
        return formated_user_money;
    }

    public void setFormated_user_money(String formated_user_money) {
        this.formated_user_money = formated_user_money;
    }

    public String getUser_points() {
        return user_points;
    }

    public void setUser_points(String user_points) {
        this.user_points = user_points;
    }

    public String getUser_bonus_count() {
        return user_bonus_count;
    }

    public void setUser_bonus_count(String user_bonus_count) {
        this.user_bonus_count = user_bonus_count;
    }

    public String getCollection_num() {
        return collection_num;
    }

    public void setCollection_num(String collection_num) {
        this.collection_num = collection_num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ORDER_NUM getOrder_num() {
        return order_num;
    }

    public void setOrder_num(ORDER_NUM order_num) {
        this.order_num = order_num;
    }

    public String getRank_name() {
        return rank_name;
    }

    public void setRank_name(String rank_name) {
        this.rank_name = rank_name;
    }

    public int getRank_level() {
        return rank_level;
    }

    public void setRank_level(int rank_level) {
        this.rank_level = rank_level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword_null() {
        return password_null;
    }

    public void setPassword_null(String password_null) {
        this.password_null = password_null;
    }

    public String getAvatar_img() {
        return avatar_img;
    }

    public void setAvatar_img(String avatar_img) {
        this.avatar_img = avatar_img;
    }


    public static USER fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        USER localItem = new USER();
        localItem.id = jsonObject.optString("id");
        localItem.name = jsonObject.optString("name");
        localItem.rank_name = jsonObject.optString("rank_name");
        localItem.rank_level = jsonObject.optInt("rank_level");
        localItem.collection_num = jsonObject.optString("collection_num");
        localItem.collect_merchant_num = jsonObject.optString("collect_merchant_num");
        localItem.email = jsonObject.optString("email");
        localItem.mobile_phone = jsonObject.optString("mobile_phone");
        localItem.avatar_img = jsonObject.optString("avatar_img");
        localItem.password_null = jsonObject.optString("password_null");
        localItem.order_num = ORDER_NUM.fromJson(jsonObject.optJSONObject("order_num"));
        localItem.formated_user_money = jsonObject.optString("formated_user_money");
        localItem.user_points = jsonObject.optString("user_points");
        localItem.user_bonus_count = jsonObject.optString("user_bonus_count");
        localItem.signup_reward_url = jsonObject.optString("signup_reward_url");
        localItem.users_real = jsonObject.optString("users_real");
        localItem.user_coupon_count = jsonObject.optString("user_coupon_count");
        localItem.purchaser_valid = jsonObject.optString("purchaser_valid");

        JSONArray subItemArray = jsonObject.optJSONArray("bonus_list");
        if (subItemArray != null && subItemArray.length() > 0) {
            for (int i = 0; i < subItemArray.length(); i++) {
                localItem.bonus_list.add(BONUS.fromJson(subItemArray.getJSONObject(i)));
            }
        }
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("id", id);
        localItemObject.put("name", name);
        localItemObject.put("rank_name", rank_name);
        localItemObject.put("rank_level", rank_level);
        localItemObject.put("collection_num", collection_num);
        localItemObject.put("collect_merchant_num", collect_merchant_num);
        localItemObject.put("email", email);
        localItemObject.put("password_null", password_null);
        localItemObject.put("mobile_phone", mobile_phone);
        localItemObject.put("avatar_img", avatar_img);
        if (null != order_num) {
            localItemObject.put("order_num", order_num.toJson());
        }
        localItemObject.put("formated_user_money", formated_user_money);
        localItemObject.put("user_points", user_points);
        localItemObject.put("user_bonus_count", user_bonus_count);
        for (int i = 0; i < bonus_list.size(); i++) {
            itemJSONArray.put(bonus_list.get(i).toJson());
        }
        localItemObject.put("bonus_list", itemJSONArray);
        localItemObject.put("signup_reward_url", signup_reward_url);
        localItemObject.put("users_real", users_real);
        localItemObject.put("user_coupon_count", user_coupon_count);
        localItemObject.put("purchaser_valid", purchaser_valid);

        return localItemObject;
    }

}
