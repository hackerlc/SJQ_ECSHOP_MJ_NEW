
package com.ecjia.entity.responsemodel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class BRAND
{


    private int brand_id;


    private String brand_name;


    private String url;

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static BRAND fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        BRAND   localItem = new BRAND();

        JSONArray subItemArray;

        localItem.brand_id = jsonObject.optInt("brand_id");

        localItem.brand_name = jsonObject.optString("brand_name");

        localItem.url = jsonObject.optString("url");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("brand_id", brand_id);
        localItemObject.put("brand_name", brand_name);
        localItemObject.put("url", url);
        return localItemObject;
    }

}
