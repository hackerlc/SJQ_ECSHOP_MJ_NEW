package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

public class CITY {

    private String id;
    private String ishot;
    private String name;
    private String pinyin;
    private String parent_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIshot() {
        return ishot;
    }

    public void setIshot(String ishot) {
        this.ishot = ishot;
    }

    public CITY(){

    }
	public CITY(String name, String pinyin) {
		super();
		this.name = name;
		this.pinyin = pinyin;
	}

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public static CITY fromJson(JSONObject json){
        if(null==json){
            return null;
        }
        CITY city=new CITY();
        city.id=json.optString("id");
        city.ishot=json.optString("ishot");
        city.name=json.optString("name");
        city.pinyin=json.optString("pinyin");
        city.parent_id=json.optString("parent_id");
        return city;
    }


    public JSONObject toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("id",id);
        localItemObject.put("ishot",ishot);
        localItemObject.put("name",name);
        localItemObject.put("pinyin",pinyin);
        localItemObject.put("parent_id", parent_id);
        return localItemObject;
    }
}
