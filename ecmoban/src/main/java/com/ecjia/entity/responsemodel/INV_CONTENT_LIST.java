
package com.ecjia.entity.responsemodel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class INV_CONTENT_LIST
{


    private String id;

    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static INV_CONTENT_LIST fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        INV_CONTENT_LIST localItem = new INV_CONTENT_LIST();
        JSONArray subItemArray;
        localItem.id = jsonObject.optString("id");
        localItem.value = jsonObject.optString("value");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("id", id);
        localItemObject.put("value", value);
        return localItemObject;
    }

}
