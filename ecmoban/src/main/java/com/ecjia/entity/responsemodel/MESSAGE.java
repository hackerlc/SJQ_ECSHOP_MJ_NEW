
package com.ecjia.entity.responsemodel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class MESSAGE
{

    private int id;


    private String content;


    private String action;


    private String parameter;


    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public static MESSAGE fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        MESSAGE localItem = new MESSAGE();

        JSONArray subItemArray;

        localItem.content = jsonObject.optString("content");

        localItem.action = jsonObject.optString("action");

        localItem.parameter = jsonObject.optString("parameter");

        localItem.time = jsonObject.optString("time");

        localItem.id = jsonObject.optInt("id");

        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("content", content);
        localItemObject.put("action", action);
        localItemObject.put("parameter",parameter);
        localItemObject.put("time", time);
        localItemObject.put("id",id);

        return localItemObject;
    }

}
