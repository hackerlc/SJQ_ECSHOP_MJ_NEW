
package com.ecjia.entity.responsemodel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class SIGNUPFILEDS
{


    private String need;


    private int id;


    private String name;

    public String getNeed() {
        return need;
    }

    public void setNeed(String need) {
        this.need = need;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static SIGNUPFILEDS fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        SIGNUPFILEDS   localItem = new SIGNUPFILEDS();

        JSONArray subItemArray;

        localItem.need = jsonObject.optString("need");

        localItem.id = jsonObject.optInt("id");

        localItem.name = jsonObject.optString("name");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("need", need);
        localItemObject.put("id", id);
        localItemObject.put("name", name);
        return localItemObject;
    }

}
