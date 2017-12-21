package com.ecjia.entity.responsemodel;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class FAVOUR implements Serializable {

    private String name; //5.1诺基亚优惠活动开始
    private String type; //price_reduction
    private String type_label;  //满减

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_label() {
        return type_label;
    }

    public void setType_label(String type_label) {
        this.type_label = type_label;
    }

    public static FAVOUR fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        FAVOUR localItem = new FAVOUR();

        localItem.name = jsonObject.optString("name");
        localItem.type = jsonObject.optString("type");
        localItem.type_label = jsonObject.optString("type_label");

        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("name", name);
        localItemObject.put("type", type);
        localItemObject.put("type_label", type_label);

        return localItemObject;
    }


}
