
package com.ecjia.entity.responsemodel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class INV_TYPE_LIST
{


    private String id;

    private String value;

    public String getLabel_value() {
        return label_value;
    }

    public void setLabel_value(String label_value) {
        this.label_value = label_value;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    private String label_value;
    private String rate;
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

    public static INV_TYPE_LIST fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        INV_TYPE_LIST localItem = new INV_TYPE_LIST();

        JSONArray subItemArray;
        localItem.id = jsonObject.optString("id");
        localItem.value = jsonObject.optString("value");
        localItem.label_value = jsonObject.optString("label_value");
        localItem.rate = jsonObject.optString("rate");

        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("id", id);
        localItemObject.put("value", value);
        localItemObject.put("label_value", label_value);
        localItemObject.put("rate", rate);
        return localItemObject;
    }

}
