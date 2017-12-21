
package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;


public class SHOPINFO {


    private int id;

    private String image;

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static SHOPINFO fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        SHOPINFO localItem = new SHOPINFO();
        localItem.id = jsonObject.optInt("id");
        localItem.image = jsonObject.optString("image");
        localItem.title = jsonObject.optString("title");
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("id", id);
        localItemObject.put("image", image);
        localItemObject.put("title", title);
        return localItemObject;
    }

}
