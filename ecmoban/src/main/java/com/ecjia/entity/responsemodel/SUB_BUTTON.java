
package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 相当于domain对象
 * @author Administrator
 *
 */
public class SUB_BUTTON
{


    private String name;

    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static SUB_BUTTON fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        SUB_BUTTON localItem = new SUB_BUTTON();

        JSONArray subItemArray;


        localItem.name = jsonObject.optString("name");

        localItem.url = jsonObject.optString("url");

        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("name", name);
        localItemObject.put("url", url);
        return localItemObject;
    }


}

