package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;


public class ADSENSE_GROUP implements Serializable {


    private String title;


    private ArrayList<ADSENSE> adsense = new ArrayList<ADSENSE>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<ADSENSE> getAdsense() {
        return adsense;
    }

    public void setAdsense(ArrayList<ADSENSE> adsense) {
        this.adsense = adsense;
    }

    public static ADSENSE_GROUP fromJson(JSONObject jsonObject)
            throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        ADSENSE_GROUP localItem = new ADSENSE_GROUP();

        JSONArray subItemArray;

        localItem.title = jsonObject.optString("title");

        subItemArray = jsonObject.optJSONArray("adsense");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                ADSENSE subItem = ADSENSE.fromJson(subItemObject);
                localItem.adsense.add(subItem);
            }
        }

        return localItem;
    }

}
