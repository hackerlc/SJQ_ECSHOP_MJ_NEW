package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class INVITE_RECORD implements Serializable {

    private String invitee_name; //被推荐人名称
    private String reg_time; //被推荐人注册时间
    private String label_award_type; //奖励描述
    private String award_type; //奖励类型
    private String give_award; //给予的奖励
    private String award_time; //奖励时间

    public String getInvitee_name() {
        return invitee_name;
    }

    public void setInvitee_name(String invitee_name) {
        this.invitee_name = invitee_name;
    }

    public String getReg_time() {
        return reg_time;
    }

    public void setReg_time(String reg_time) {
        this.reg_time = reg_time;
    }

    public String getLabel_award_type() {
        return label_award_type;
    }

    public void setLabel_award_type(String label_award_type) {
        this.label_award_type = label_award_type;
    }

    public String getAward_type() {
        return award_type;
    }

    public void setAward_type(String award_type) {
        this.award_type = award_type;
    }

    public String getGive_award() {
        return give_award;
    }

    public void setGive_award(String give_award) {
        this.give_award = give_award;
    }

    public String getAward_time() {
        return award_time;
    }

    public void setAward_time(String award_time) {
        this.award_time = award_time;
    }

    public static INVITE_RECORD fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        INVITE_RECORD localItem = new INVITE_RECORD();

        JSONArray subItemArray;

        localItem.invitee_name = jsonObject.optString("invitee_name");
        localItem.reg_time = jsonObject.optString("reg_time");
        localItem.label_award_type = jsonObject.optString("label_reward_type");
        localItem.award_type = jsonObject.optString("reward_type");
        localItem.give_award = jsonObject.optString("give_reward");
        localItem.award_time = jsonObject.optString("reward_time");

        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("invitee_name", invitee_name);
        localItemObject.put("reg_time", reg_time);
        localItemObject.put("label_reward_type", label_award_type);
        localItemObject.put("reward_type", award_type);
        localItemObject.put("give_reward", give_award);
        localItemObject.put("reward_time", award_time);
        return localItemObject;
    }

}

