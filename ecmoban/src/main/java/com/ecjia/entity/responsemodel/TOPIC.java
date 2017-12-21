package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/18.
 */
public class TOPIC {
    private String topic_id;
    private String topic_title;
    private String topic_description;
    private String topic_image;
    private ArrayList<TOPIC_TYPE> topic_types=new ArrayList<TOPIC_TYPE>();
    private ArrayList<SELLERGOODS> sellergoods=new ArrayList<SELLERGOODS>();

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getTopic_title() {
        return topic_title;
    }

    public void setTopic_title(String topic_title) {
        this.topic_title = topic_title;
    }

    public String getTopic_description() {
        return topic_description;
    }

    public void setTopic_description(String topic_description) {
        this.topic_description = topic_description;
    }

    public String getTopic_image() {
        return topic_image;
    }

    public void setTopic_image(String topic_image) {
        this.topic_image = topic_image;
    }

    public ArrayList<TOPIC_TYPE> getTopic_types() {
        return topic_types;
    }

    public void setTopic_types(ArrayList<TOPIC_TYPE> topic_types) {
        this.topic_types = topic_types;
    }

    public ArrayList<SELLERGOODS> getSellergoods() {
        return sellergoods;
    }

    public void setSellergoods(ArrayList<SELLERGOODS> sellergoods) {
        this.sellergoods = sellergoods;
    }

    public static TOPIC fromJson(JSONObject jsonObject)  throws JSONException {
        TOPIC localItem = new TOPIC();
        if(null == jsonObject){
            return null;
        }
        JSONArray jsonArray=jsonObject.optJSONArray("sub_goods");
        JSONArray typeArray=jsonObject.optJSONArray("topic_type");
        localItem.topic_id=jsonObject.optString("topic_id");
        localItem.topic_title=jsonObject.optString("topic_title");
        localItem.topic_description=jsonObject.optString("topic_description");
        localItem.topic_image=jsonObject.optString("topic_image");

        if(null!=typeArray&&typeArray.length()>0){
            for(int i=0;i<typeArray.length();i++){
                TOPIC_TYPE topic_type=new TOPIC_TYPE();
                topic_type.setType_text(typeArray.optString(i));
                topic_type.setSelected(false);
                localItem.topic_types.add(topic_type);
            }
        }

        if(null!=jsonArray&&jsonArray.length()>0){
            for(int i=0;i<jsonArray.length();i++){
                localItem.sellergoods.add(SELLERGOODS.fromJson(jsonArray.optJSONObject(i)));
            }
        }
        return localItem;
    }

}
