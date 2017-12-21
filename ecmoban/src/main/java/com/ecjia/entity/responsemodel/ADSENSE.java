package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/23.
 */
public class ADSENSE {
    private String id;
    private String name;
    private String link;
    private String img;
    private String start_time;
    private String end_time;
    private Map<String,String> map=new HashMap<String, String>();
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public static ADSENSE fromJson(JSONObject jsonObject)  throws JSONException{
        if(null == jsonObject){
            return null;
        }

        ADSENSE adsense=new ADSENSE();
        adsense.id=jsonObject.optString("id");
        adsense.name=jsonObject.optString("text");
        adsense.link=jsonObject.optString("url");
        if(adsense.link.contains("ecjiaopen://app")){
            adsense.initOpenType(adsense.link);
        }
        adsense.img=jsonObject.optString("image");
        adsense.start_time=jsonObject.optString("start_time");
        adsense.end_time=jsonObject.optString("end_time");


        return  adsense;

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
