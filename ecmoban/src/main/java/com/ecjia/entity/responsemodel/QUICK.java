package com.ecjia.entity.responsemodel;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QUICK {
    @SerializedName("image")
    private String img;
    private String url;
    private String text;
    private String title;
    private String detail;
    private Map<String,String> map=new HashMap<String, String>();

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public static QUICK fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        QUICK localItem = new QUICK();
        localItem.img = jsonObject.optString("image");
        localItem.text = jsonObject.optString("text");
        localItem.url = jsonObject.optString("url");
        if(localItem.url.contains("ecjiaopen://app")){
            localItem.initOpenType(localItem.url);
        }else{

        }
        String[] strings=localItem.text.split("\\|");
        if(null!=strings&&strings.length>1) {
            localItem.title = strings[0];
            localItem.detail = strings[1];
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
