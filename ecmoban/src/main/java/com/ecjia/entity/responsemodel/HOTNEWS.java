package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/16.
 */
public class HOTNEWS {
    private String id;
    private String title;
    private String description;
    private String image;
    private String content_url;
    private String create_time;
    private Map<String,String> map=new HashMap<String, String>();
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public static HOTNEWS fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        HOTNEWS   localItem = new HOTNEWS();

        localItem.id = jsonObject.optString("id");
        localItem.title=jsonObject.optString("title");
        localItem.description=jsonObject.optString("description");
        localItem.image=jsonObject.optString("image");
        localItem.content_url=jsonObject.optString("content_url");
        localItem.create_time=jsonObject.optString("create_time");
        if(localItem.content_url.contains("ecjiaopen://app")){
            localItem.initOpenType(localItem.content_url);
        }
        return localItem;
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
