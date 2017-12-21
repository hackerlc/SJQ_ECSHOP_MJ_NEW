
package com.ecjia.entity.responsemodel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class REGIONS
{


    private String id;


    private String name;


    private String parent_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public static REGIONS fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        REGIONS   localItem = new REGIONS();

        JSONArray subItemArray;

        localItem.id = jsonObject.optString("id");

        localItem.name = jsonObject.optString("name");

        localItem.parent_id = jsonObject.optString("parent_id");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("id", id);
        localItemObject.put("name", name);
        localItemObject.put("parent_id", parent_id);
        return localItemObject;
    }

}
