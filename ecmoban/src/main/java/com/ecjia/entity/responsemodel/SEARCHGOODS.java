package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/31
 * 搜索商品
 */
public class SEARCHGOODS {

    private String type;
    private ArrayList<SELLERGOODS> searchGood=new ArrayList<SELLERGOODS>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<SELLERGOODS> getSearchGood() {
        return searchGood;
    }

    public void setSearchGood(ArrayList<SELLERGOODS> searchGood) {
        this.searchGood = searchGood;
    }

    public static SEARCHGOODS fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        SEARCHGOODS localItem = new SEARCHGOODS();

        localItem.type = jsonObject.optString("type");

        JSONArray array=jsonObject.optJSONArray("result");
        if(array!=null&&array.length()>0){
            for(int i=0;i<array.length();i++){
            localItem.searchGood.add(SELLERGOODS.fromJson(array.getJSONObject(i)));
            }
        }

        return localItem;
    }
}
