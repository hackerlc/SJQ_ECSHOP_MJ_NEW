
package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 相当于domain对象
 * @author Administrator
 *
 */
public class MENU_BUTTON
{

    private String name;

    private String url;

    private ArrayList<SUB_BUTTON> sub_button=new ArrayList<SUB_BUTTON>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<SUB_BUTTON> getSub_button() {
        return sub_button;
    }

    public void setSub_button(ArrayList<SUB_BUTTON> sub_button) {
        this.sub_button = sub_button;
    }

    public static MENU_BUTTON fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        MENU_BUTTON localItem = new MENU_BUTTON();

        JSONArray subItemArray;


        localItem.name = jsonObject.optString("name");

        localItem.url = jsonObject.optString("url");

        subItemArray = jsonObject.optJSONArray("sub_button");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                SUB_BUTTON subItem = SUB_BUTTON.fromJson(subItemObject);
                localItem.sub_button.add(subItem);
            }
        }

        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("name", name);
        localItemObject.put("url", url);

        for (int i = 0; i < sub_button.size(); i++) {
            SUB_BUTTON itemData = sub_button.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("sub_button", itemJSONArray);

        return localItemObject;
    }


}

