package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/31
 * 搜索商品商铺，返回结果
 */
public class SEARCHRESULT {

    private String type;
    private ArrayList<JSONObject> objectses=new ArrayList<JSONObject>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<JSONObject> getObjectses() {
        return objectses;
    }

    public void setObjectses(ArrayList<JSONObject> objectses) {
        this.objectses = objectses;
    }

    public static SEARCHRESULT fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        SEARCHRESULT localItem = new SEARCHRESULT();

        localItem.type = jsonObject.optString("type");

        JSONArray array=jsonObject.optJSONArray("result");
        if(array!=null&&array.length()>0){
            for(int i=0;i<array.length();i++){
            localItem.objectses.add(array.getJSONObject(i));
            }
        }

        return localItem;
    }
}
