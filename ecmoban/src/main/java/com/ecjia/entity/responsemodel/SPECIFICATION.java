
package com.ecjia.entity.responsemodel;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SPECIFICATION {

    public static String SINGLE_SELECT = "1"; //单选属性
    public static String MULTIPLE_SELECT = "2";//复选属性

    private String name;

    public ArrayList<SPECIFICATION_VALUE> value = new ArrayList<SPECIFICATION_VALUE>();


    private String attr_type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttr_type() {
        return attr_type;
    }

    public void setAttr_type(String attr_type) {
        this.attr_type = attr_type;
    }

    public ArrayList<SPECIFICATION_VALUE> getValue() {
        return value;
    }

    public void setValue(ArrayList<SPECIFICATION_VALUE> value) {
        this.value = value;
    }

    public static SPECIFICATION fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        SPECIFICATION localItem = new SPECIFICATION();

        JSONArray subItemArray;

        localItem.name = jsonObject.optString("name");

        subItemArray = jsonObject.optJSONArray("value");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                SPECIFICATION_VALUE subItem = SPECIFICATION_VALUE.fromJson(subItemObject);
                subItem.specification = localItem;
                localItem.value.add(subItem);
            }
        }


        localItem.attr_type = jsonObject.optString("attr_type");
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("name", name);

        for (int i = 0; i < value.size(); i++) {
            SPECIFICATION_VALUE itemData = value.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("value", itemJSONArray);
        localItemObject.put("attr_type", attr_type);
        return localItemObject;
    }

}
