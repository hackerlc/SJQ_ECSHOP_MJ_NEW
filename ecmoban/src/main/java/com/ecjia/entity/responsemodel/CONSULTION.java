package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CONSULTION {

    private String content;

    private String time;



    public String getIs_myself() {
        return is_myself;
    }

    public void setIs_myself(String is_myself) {
        this.is_myself = is_myself;
    }

    private String is_myself;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static CONSULTION fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        CONSULTION localItem = new CONSULTION();

        JSONArray subItemArray;

        localItem.content = jsonObject.optString("content");

        localItem.time = jsonObject.optString("time");

        localItem.is_myself = jsonObject.optString("is_myself");

        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();

        JSONArray itemJSONArray = new JSONArray();

        localItemObject.put("content", content);

        localItemObject.put("time", time);

        localItemObject.put("is_myself", is_myself);

        return localItemObject;
    }

}

