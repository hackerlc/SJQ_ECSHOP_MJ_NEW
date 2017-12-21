package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/12/23.
 */
public class ACCESS_TOKEN {

    private String access_token;
    private String expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public static ACCESS_TOKEN fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        ACCESS_TOKEN localItem=new ACCESS_TOKEN();
        localItem.access_token=jsonObject.optString("access_token");
        localItem.expires_in=jsonObject.optString("expires_in");
        return  localItem;
    }

}
