
package com.ecjia.entity.responsemodel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class FILTER
{


    private String keywords;


    private String sort_by;


    private String brand_id;

    private String shop_id;

    private String category_id;
    private String is_one;
    private String is_new;


    private PRICE_RANGE price_range;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public PRICE_RANGE getPrice_range() {
        return price_range;
    }

    public void setPrice_range(PRICE_RANGE price_range) {
        this.price_range = price_range;
    }

    public String getSort_by() {
        return sort_by;
    }

    public void setSort_by(String sort_by) {
        this.sort_by = sort_by;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getIs_one() {
        return is_one;
    }

    public void setIs_one(String is_one) {
        this.is_one = is_one;
    }

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public static FILTER fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        FILTER localItem = new FILTER();

        JSONArray subItemArray;

        localItem.keywords = jsonObject.optString("keywords");

        localItem.sort_by = jsonObject.optString("sort_by");

        localItem.brand_id = jsonObject.optString("brand_id");

        localItem.shop_id=jsonObject.optString("shop_id");

        localItem.category_id = jsonObject.optString("category_id");
        localItem.is_one = jsonObject.optString("is_one");
        localItem.is_new = jsonObject.optString("is_new");
        localItem.price_range = PRICE_RANGE.fromJson(jsonObject.optJSONObject("price_range"));
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("keywords", keywords);
        localItemObject.put("sort_by", sort_by);
        localItemObject.put("brand_id", brand_id);
        localItemObject.put("category_id", category_id);
        localItemObject.put("shop_id",shop_id);
        localItemObject.put("is_one",is_one);
        localItemObject.put("is_new",is_new);

        if (null != price_range)
        {
            localItemObject.put("price_range",price_range.toJson());
        }

        return localItemObject;
    }

}
