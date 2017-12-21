
package com.ecjia.entity.responsemodel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GOODSCOUNT
{


    public String count;

    public String new_goods ;

    public String best_goods;

    public String hot_goods;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getNew_goods() {
        return new_goods;
    }

    public void setNew_goods(String new_goods) {
        this.new_goods = new_goods;
    }

    public String getBest_goods() {
        return best_goods;
    }

    public void setBest_goods(String best_goods) {
        this.best_goods = best_goods;
    }

    public String getHot_goods() {
        return hot_goods;
    }

    public void setHot_goods(String hot_goods) {
        this.hot_goods = hot_goods;
    }

    public static GOODSCOUNT fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        GOODSCOUNT localItem =new GOODSCOUNT();

        JSONArray subItemArray;

        localItem.count = jsonObject.optString("count");

        localItem.new_goods = jsonObject.optString("new_goods");

        localItem.hot_goods = jsonObject.optString("hot_goods");

        localItem.best_goods = jsonObject.optString("best_goods");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("count", count);
        localItemObject.put("new_goods", new_goods);
        localItemObject.put("hot_goods", hot_goods);
        localItemObject.put("best_goods", best_goods);
        return localItemObject;
    }

}
