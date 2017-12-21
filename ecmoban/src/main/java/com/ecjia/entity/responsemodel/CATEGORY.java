
package com.ecjia.entity.responsemodel;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CATEGORY implements Serializable{


    private int id;

    private String name;

    private boolean ischecked;

    private String image;

    public boolean isIschecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    private boolean isChoose;

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean isChoose) {
        this.isChoose = isChoose;
    }

    private ArrayList<CATEGORY> children = new ArrayList<CATEGORY>();

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CATEGORY> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<CATEGORY> children) {
        this.children = children;
    }


    public static CATEGORY fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        CATEGORY localItem = new CATEGORY();

        JSONArray subItemArray;

        localItem.id = jsonObject.optInt("id");

        localItem.name = jsonObject.optString("name");

        localItem.image = jsonObject.optString("image");

        localItem.ischecked = false;
        subItemArray = jsonObject.optJSONArray("children");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                CATEGORY subItem = CATEGORY.fromJson(subItemObject);
                localItem.children.add(subItem);
            }
        }

        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("id", id);
        localItemObject.put("name", name);
        localItemObject.put("image", image);

        for (int i = 0; i < children.size(); i++) {
            CATEGORY itemData = children.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("children", itemJSONArray);
        return localItemObject;
    }

}
