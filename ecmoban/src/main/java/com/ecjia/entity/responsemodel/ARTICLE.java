
package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;


public class ARTICLE {

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    private String parentName;

    private int id;

    private String short_title;

    private String title;

    public String getShort_title() {
        return short_title;
    }

    public void setShort_title(String short_title) {
        this.short_title = short_title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static ARTICLE fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        ARTICLE localItem = new ARTICLE();
        localItem.short_title = jsonObject.optString("short_title");
        localItem.id = jsonObject.optInt("id");
        localItem.title = jsonObject.optString("title");
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("short_title", short_title);
        localItemObject.put("id", id);
        localItemObject.put("title", title);
        return localItemObject;
    }

}
