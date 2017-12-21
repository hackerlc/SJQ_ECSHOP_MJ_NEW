package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-02-21.
 */

public class SHOOPSERVICES implements Serializable {

    private String service_name;// "七天退换"

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public static SHOOPSERVICES fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        SHOOPSERVICES localItem = new SHOOPSERVICES();
        localItem.service_name = jsonObject.optString("service_name");
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("service_name", service_name);
        return localItemObject;
    }
}
