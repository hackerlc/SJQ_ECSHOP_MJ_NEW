package com.ecjia.entity.responsemodel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class CLIENT
{
    private String token;

    private String client_type;

    private String version;

    private String code;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClient_type() {
        return client_type;
    }

    public void setClient_type(String client_type) {
        this.client_type = client_type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static CLIENT fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        CLIENT   localItem = new CLIENT();

        JSONArray subItemArray;

        localItem.token   = jsonObject.optString("token");

        localItem.client_type         = jsonObject.optString("client_type");

        localItem.version        = jsonObject.optString("version");

        localItem.code = jsonObject.optString("code");

        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();

        localItemObject.put("token",token);
        localItemObject.put("client_type",client_type);
        localItemObject.put("version",version);
        localItemObject.put("code", code);
        return localItemObject;
    }
}
