
package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;


public class EXPRESS {

    private String time;

    private String context;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public static EXPRESS fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        EXPRESS localItem = new EXPRESS();
        localItem.time = jsonObject.optString("time");
        localItem.context = jsonObject.optString("context");
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("time", time);
        localItemObject.put("context", context);
        return localItemObject;
    }

}
