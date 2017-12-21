package com.ecjia.network.query;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/2/28 15:24.
 */

public class MainQuery extends NormalQuery {

    public MainQuery() {

    }

    public String getQuery(){
        requestJsonObject = new JSONObject();
        return super.get();
    }

    public String getWTop(int type){
        requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("top_type",type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.get();
    }
}
