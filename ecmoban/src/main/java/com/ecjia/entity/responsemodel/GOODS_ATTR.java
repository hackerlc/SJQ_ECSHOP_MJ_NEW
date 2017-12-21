
package com.ecjia.entity.responsemodel;
import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





public class GOODS_ATTR  implements Serializable
{

    private String name;


    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static GOODS_ATTR fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        GOODS_ATTR   localItem = new GOODS_ATTR();

        JSONArray subItemArray;

        localItem.name = jsonObject.optString("name");

        localItem.value = jsonObject.optString("value");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("name", name);
        localItemObject.put("value", value);
        return localItemObject;
    }

}
