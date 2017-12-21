package com.ecjia.entity.responsemodel;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CODEVALID
{

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static CODEVALID fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        CODEVALID   localItem = new CODEVALID();

        JSONArray subItemArray;

        localItem.code = jsonObject.optString("code");

        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();

        localItemObject.put("code", code);
        return localItemObject;
    }
}
