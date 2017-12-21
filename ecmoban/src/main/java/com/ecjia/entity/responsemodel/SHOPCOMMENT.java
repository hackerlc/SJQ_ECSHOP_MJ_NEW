
package com.ecjia.entity.responsemodel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//店铺详情页评价
public class SHOPCOMMENT
{


    private String comment_goods; //商品评价


    private String comment_server; //服务评价


    private String comment_delivery; //物流评价

    public String getComment_goods() {
        return comment_goods;
    }

    public void setComment_goods(String comment_goods) {
        this.comment_goods = comment_goods;
    }

    public String getComment_server() {
        return comment_server;
    }

    public void setComment_server(String comment_server) {
        this.comment_server = comment_server;
    }

    public String getComment_delivery() {
        return comment_delivery;
    }

    public void setComment_delivery(String comment_delivery) {
        this.comment_delivery = comment_delivery;
    }

    public static SHOPCOMMENT fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        SHOPCOMMENT localItem = new SHOPCOMMENT();

        JSONArray subItemArray;

        localItem.comment_goods = jsonObject.optString("comment_goods");

        localItem.comment_server = jsonObject.optString("comment_server");

        localItem.comment_delivery = jsonObject.optString("comment_delivery");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("comment_goods", comment_goods);
        localItemObject.put("comment_server", comment_server);
        localItemObject.put("comment_delivery", comment_delivery);
        return localItemObject;
    }

}
