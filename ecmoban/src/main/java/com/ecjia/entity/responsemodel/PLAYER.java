
package com.ecjia.entity.responsemodel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PLAYER
{


    private String description;


    private PHOTO   photo;


    private String url;


    private String action;


    private int action_id;

    private Map<String,String> map=new HashMap<String, String>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PHOTO getPhoto() {
        return photo;
    }

    public void setPhoto(PHOTO photo) {
        this.photo = photo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getAction_id() {
        return action_id;
    }

    public void setAction_id(int action_id) {
        this.action_id = action_id;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public static PLAYER fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        PLAYER   localItem = new PLAYER();

        JSONArray subItemArray;

        localItem.description = jsonObject.optString("description");
        localItem.photo = PHOTO.fromJson(jsonObject.optJSONObject("photo"));

        localItem.url = jsonObject.optString("url");
        if(localItem.url.contains("ecjiaopen://app")){
            localItem.initOpenType(localItem.url);
        }
        localItem.action = jsonObject.optString("action");
        localItem.action_id = jsonObject.optInt("action_id");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("description", description);
        if(null!=photo)
        {
            localItemObject.put("photo", photo.toJson());
        }
        localItemObject.put("url", url);
        localItemObject.put("action",action);
        localItemObject.put("action_id",action_id);
        return localItemObject;
    }
    //ecjia跳转解析
    private  void initOpenType(String url) {
        //"ecjia://app?open_type=good&good_id=1"
        url=url.replace("ecjiaopen://app?","");
        if(url.contains("&")) {//有多个参数
            String[] parentstring = url.split("&");
            for(int i=0;i<parentstring.length;i++){
                String[] string=parentstring[i].split("=");
                this.map.put(string[0],string[1]);
            }
        }else{//只有一个参数
            String[] string=url.split("=");
            this.map.put(string[0],string[1]);
        }
    }
}
