package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-31.
 */

public class GOODSCOUPONS implements Serializable {

    /*{
        "cou_name": "测试优惠券",//优惠券名称
            "cou_man": "20",//满多少
            "cou_money": "10",//减多少
            "cou_user_num": "1",//单用户可领数
            "cou_start_time": "1487664000",//开始时间
            "cou_end_time": "1498032000"//结束时间
            "can_receive": 1//可领取状态 0为可领取，1为已领取，2为已领完
    },*/

    private String cou_id;
    private String cou_name;
    private String cou_man;
    private String cou_money;
    private int cou_user_num;
    private String cou_start_time;
    private String cou_end_time;
    private String can_receive;


    public String getCou_id() {
        return cou_id;
    }

    public void setCou_id(String cou_id) {
        this.cou_id = cou_id;
    }

    public String getCou_name() {
        return cou_name;
    }

    public void setCou_name(String cou_name) {
        this.cou_name = cou_name;
    }

    public String getCou_man() {
        return cou_man;
    }

    public void setCou_man(String cou_man) {
        this.cou_man = cou_man;
    }

    public String getCou_money() {
        return cou_money;
    }

    public void setCou_money(String cou_money) {
        this.cou_money = cou_money;
    }

    public int getCou_user_num() {
        return cou_user_num;
    }

    public void setCou_user_num(int cou_user_num) {
        this.cou_user_num = cou_user_num;
    }

    public String getCou_start_time() {
        return cou_start_time;
    }

    public void setCou_start_time(String cou_start_time) {
        this.cou_start_time = cou_start_time;
    }

    public String getCou_end_time() {
        return cou_end_time;
    }

    public void setCou_end_time(String cou_end_time) {
        this.cou_end_time = cou_end_time;
    }

    public String getCan_receive() {
        return can_receive;
    }

    public void setCan_receive(String can_receive) {
        this.can_receive = can_receive;
    }

    public static GOODSCOUPONS fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        GOODSCOUPONS localItem = new GOODSCOUPONS();
        JSONArray subItemArray;
        localItem.cou_id = jsonObject.optString("cou_id");
        localItem.cou_name = jsonObject.optString("cou_name");
        localItem.cou_man = jsonObject.optString("cou_man");
        localItem.cou_money = jsonObject.optString("cou_money");
        localItem.cou_user_num = jsonObject.optInt("cou_user_num");
        localItem.cou_start_time = jsonObject.optString("cou_start_time");
        localItem.cou_end_time = jsonObject.optString("cou_end_time");
        localItem.can_receive = jsonObject.optString("can_receive");
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("cou_id", cou_id);
        localItemObject.put("cou_name", cou_name);
        localItemObject.put("cou_man", cou_man);
        localItemObject.put("cou_money", cou_money);
        localItemObject.put("cou_user_num", cou_user_num);
        localItemObject.put("cou_start_time", cou_start_time);
        localItemObject.put("cou_end_time", cou_end_time);
        localItemObject.put("can_receive", can_receive);
        return localItemObject;
    }
}
