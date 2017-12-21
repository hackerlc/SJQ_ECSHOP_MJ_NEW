package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class INVITE_REWARD implements Serializable {

    private String invite_data; //奖励时间日期
    private String label_invite_data; //奖励时间日期
    private int invite_count; //受邀用户总数
    private int invite_bouns_reward; //邀请好友获得的红包奖励
    private int invite_integral_reward; //邀请好友获得的积分奖励
    private String invite_balance_reward; //邀请好友获得的现金奖励

    public String getInvite_data() {
        return invite_data;
    }

    public void setInvite_data(String invite_data) {
        this.invite_data = invite_data;
    }

    public String getLabel_invite_data() {
        return label_invite_data;
    }

    public void setLabel_invite_data(String label_invite_data) {
        this.label_invite_data = label_invite_data;
    }

    public int getInvite_count() {
        return invite_count;
    }

    public void setInvite_count(int invite_count) {
        this.invite_count = invite_count;
    }

    public int getInvite_bouns_reward() {
        return invite_bouns_reward;
    }

    public void setInvite_bouns_reward(int invite_bouns_reward) {
        this.invite_bouns_reward = invite_bouns_reward;
    }

    public int getInvite_integral_reward() {
        return invite_integral_reward;
    }

    public void setInvite_integral_reward(int invite_integral_reward) {
        this.invite_integral_reward = invite_integral_reward;
    }

    public String getInvite_balance_reward() {
        return invite_balance_reward;
    }

    public void setInvite_balance_reward(String invite_balance_reward) {
        this.invite_balance_reward = invite_balance_reward;
    }

    public static INVITE_REWARD fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        INVITE_REWARD localItem = new INVITE_REWARD();

        JSONArray subItemArray;

        localItem.invite_data = jsonObject.optString("invite_data");
        localItem.label_invite_data = jsonObject.optString("label_invite_data");
        localItem.invite_count = jsonObject.optInt("invite_count");
        localItem.invite_bouns_reward = jsonObject.optInt("invite_bouns_reward");
        localItem.invite_integral_reward = jsonObject.optInt("invite_integral_reward");
        localItem.invite_balance_reward = jsonObject.optString("invite_balance_reward");

        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("invite_data", invite_data);
        localItemObject.put("label_invite_data", label_invite_data);
        localItemObject.put("invite_count", invite_count);
        localItemObject.put("invite_bouns_reward", invite_bouns_reward);
        localItemObject.put("invite_integral_reward", invite_integral_reward);
        localItemObject.put("invite_balance_reward", invite_balance_reward);
        return localItemObject;
    }

}

