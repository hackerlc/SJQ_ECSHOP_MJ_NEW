
package com.ecjia.entity.responsemodel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class PRICE
{


    private int id;


    private String price;


    private String rank_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRank_name() {
        return rank_name;
    }

    public void setRank_name(String rank_name) {
        this.rank_name = rank_name;
    }

    public static PRICE fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        PRICE   localItem = new PRICE();

        JSONArray subItemArray;

        localItem.id = jsonObject.optInt("id");

        localItem.price = jsonObject.optString("price");

        localItem.rank_name = jsonObject.optString("rank_name");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("id", id);
        localItemObject.put("price", price);
        localItemObject.put("rank_name", rank_name);
        return localItemObject;
    }

}
