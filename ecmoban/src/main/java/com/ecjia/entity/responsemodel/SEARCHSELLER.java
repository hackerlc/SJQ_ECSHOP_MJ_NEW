package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/31
 * 搜索店铺
 */
public class SEARCHSELLER {

    private String type;
    private ArrayList<SELLERINFO> searchSeller=new ArrayList<SELLERINFO>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<SELLERINFO> getSearchSeller() {
        return searchSeller;
    }

    public void setSearchSeller(ArrayList<SELLERINFO> searchSeller) {
        this.searchSeller = searchSeller;
    }

    public static SEARCHSELLER fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        SEARCHSELLER localItem = new SEARCHSELLER();

        localItem.type = jsonObject.optString("type");

        JSONArray array=jsonObject.optJSONArray("result");
        if(array!=null&&array.length()>0){
            for(int i=0;i<array.length();i++){
            localItem.searchSeller.add(SELLERINFO.fromJson(array.getJSONObject(i)));
            }
        }

        return localItem;
    }
}
