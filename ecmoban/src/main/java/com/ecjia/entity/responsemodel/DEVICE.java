package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/17.
 */
public class DEVICE {
    private String udid;
    private String client;
    private String code;

    private String city="hz";


    private static DEVICE instance;

    public DEVICE() {
        instance = this;
    }

    public static DEVICE getInstance() {
        if (instance == null) {
            synchronized (DEVICE.class) {
                if (instance == null) {
                    instance = new DEVICE();
                }
            }
        }
        return instance;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public static DEVICE fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        DEVICE localItem = DEVICE.getInstance();

        localItem.udid = jsonObject.optString("udid");

        localItem.client = jsonObject.optString("client");
        localItem.code = jsonObject.optString("code");
        localItem.city = jsonObject.optString("city");
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("client", client);
        localItemObject.put("udid", udid);
        localItemObject.put("code", code);
        localItemObject.put("city", city);
        return localItemObject;
    }
}
