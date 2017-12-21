
package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class SESSION implements Serializable {


    public String uid;

    public String sid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    private static SESSION instance;

    public static SESSION getInstance() {
        if (instance == null) {

            instance = new SESSION();

//            Class a = SESSION.class;
//            synchronized (SESSION.class) {
//                if (instance == null) {
//                    instance = new SESSION();
//                }
//            }
        }
        return instance;
    }


    public static SESSION fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        SESSION localItem = SESSION.getInstance();
        localItem.uid = jsonObject.optString("uid");
        localItem.sid = jsonObject.optString("sid");
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("uid", uid);
        localItemObject.put("sid", sid);
        return localItemObject;
    }

}
