
package com.ecjia.entity.responsemodel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class SIMPLEORDER
{


    private int id;


    private String order_time;


    private String total_fee;


    private String order_sn;


    private String order_status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public static SIMPLEORDER fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        SIMPLEORDER   localItem = new SIMPLEORDER();

        JSONArray subItemArray;

        localItem.id = jsonObject.optInt("id");

        localItem.order_time = jsonObject.optString("order_time");

        localItem.total_fee = jsonObject.optString("total_fee");

        localItem.order_sn = jsonObject.optString("order_sn");

        localItem.order_status = jsonObject.optString("order_status");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("id", id);
        localItemObject.put("order_time", order_time);
        localItemObject.put("total_fee", total_fee);
        localItemObject.put("order_sn", order_sn);
        localItemObject.put("order_status", order_status);
        return localItemObject;
    }

}
