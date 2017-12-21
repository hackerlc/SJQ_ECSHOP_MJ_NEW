package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 相当于domain对象
 *
 * @author Administrator
 */
public class ADPIC {

    private String ad_link;

    private String start_time;

    private String end_time;

    private String ad_img;

    private Map<String,String> map=new HashMap<String, String>();

    public static ADPIC fromJson(JSONObject jsonObject) throws JSONException {

        if (null == jsonObject) {

            return null;

        }

        ADPIC localItem = new ADPIC();

        JSONArray subItemArray;

        localItem.ad_link = jsonObject.optString("ad_link");

        if(localItem.ad_link.contains("ecjiaopen://app")){
            localItem.initOpenType(localItem.ad_link);
        }

        localItem.start_time = jsonObject.optString("start_time");

        localItem.end_time = jsonObject.optString("end_time");

        localItem.ad_img = jsonObject.optString("ad_img");



        return localItem;
    }

    public JSONObject toJson() throws JSONException {

        JSONObject localItemObject = new JSONObject();

        JSONArray itemJSONArray = new JSONArray();

        localItemObject.put("ad_link", ad_link);

        localItemObject.put("start_time", start_time);

        localItemObject.put("end_time", end_time);

        localItemObject.put("ad_img", ad_img);

        return localItemObject;
    }


    public String getAd_link() {
        return ad_link;
    }

    public void setAd_link(String ad_link) {
        this.ad_link = ad_link;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getAd_img() {
        return ad_img;
    }

    public void setAd_img(String ad_img) {
        this.ad_img = ad_img;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
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

