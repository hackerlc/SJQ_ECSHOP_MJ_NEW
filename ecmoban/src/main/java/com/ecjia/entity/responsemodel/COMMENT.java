
package com.ecjia.entity.responsemodel;

import com.ecjia.view.activity.goodsdetail.model.COMMENT_LIST;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class COMMENT {

    private int comment_count;

    private int comment_positive;

    private int comment_moderate;

    private int comment_negative;

    private int comment_showorder;

    private ArrayList<COMMENT_LIST> comment_list = new ArrayList<>();

    public ArrayList<COMMENT_LIST> getComment_list() {
        return comment_list;
    }

    public void setComment_list(ArrayList<COMMENT_LIST> comment_list) {
        this.comment_list = comment_list;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getComment_showorder() {
        return comment_showorder;
    }

    public void setComment_showorder(int comment_showorder) {
        this.comment_showorder = comment_showorder;
    }

    public int getComment_positive() {
        return comment_positive;
    }

    public void setComment_positive(int comment_positive) {
        this.comment_positive = comment_positive;
    }

    public int getComment_moderate() {
        return comment_moderate;
    }

    public void setComment_moderate(int comment_moderate) {
        this.comment_moderate = comment_moderate;
    }

    public int getComment_negative() {
        return comment_negative;
    }

    public void setComment_negative(int comment_negative) {
        this.comment_negative = comment_negative;
    }

    public static COMMENT fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        COMMENT localItem = new COMMENT();
        localItem.comment_count = jsonObject.optInt("comment_count");
        localItem.comment_positive = jsonObject.optInt("comment_positive");
        localItem.comment_moderate = jsonObject.optInt("comment_moderate");
        localItem.comment_negative = jsonObject.optInt("comment_negative");
        localItem.comment_showorder = jsonObject.optInt("comment_showorder");
        JSONArray subItemArray = jsonObject.optJSONArray("comment_list");
        if (subItemArray != null && subItemArray.length() > 0) {
            for (int i = 0; i < subItemArray.length(); i++) {
                localItem.comment_list.add(COMMENT_LIST.fromJson(subItemArray.optJSONObject(i)));
            }
        }
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("comment_count", comment_count);
        localItemObject.put("comment_positive", comment_positive);
        localItemObject.put("comment_moderate", comment_moderate);
        localItemObject.put("comment_negative", comment_negative);
        localItemObject.put("comment_showorder", comment_showorder);
        return localItemObject;

        //到家升级
    }

}
