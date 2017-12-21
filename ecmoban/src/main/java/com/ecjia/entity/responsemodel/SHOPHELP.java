
package com.ecjia.entity.responsemodel;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SHOPHELP {

    public ArrayList<ARTICLE> article = new ArrayList<ARTICLE>();

    private String name;

    public ArrayList<ARTICLE> getArticle() {
        return article;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArticle(ArrayList<ARTICLE> article) {
        this.article = article;
    }

    public static SHOPHELP fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        SHOPHELP localItem = new SHOPHELP();

        JSONArray subItemArray;
        localItem.name = jsonObject.optString("name");
        subItemArray = jsonObject.optJSONArray("article");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                ARTICLE subItem = ARTICLE.fromJson(subItemObject);
                subItem.setParentName(localItem.name);
                localItem.article.add(subItem);
            }
        }


        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();

        for (int i = 0; i < article.size(); i++) {
            ARTICLE itemData = article.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("article", itemJSONArray);
        localItemObject.put("name", name);
        return localItemObject;
    }

}
