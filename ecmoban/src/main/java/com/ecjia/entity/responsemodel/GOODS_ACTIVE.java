package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/30.
 */
public class GOODS_ACTIVE {
    private String name;
    private String type;
    private String type_label;

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

    public static GOODS_ACTIVE fromJson(JSONObject jsonObject)  throws JSONException {
        if(null == jsonObject){
            return null;
        }

        GOODS_ACTIVE favourable=new GOODS_ACTIVE();
        favourable.name=jsonObject.optString("name");
        favourable.type=jsonObject.optString("type");
        favourable.type_label=jsonObject.optString("type_label");
        return favourable;
    }
}
