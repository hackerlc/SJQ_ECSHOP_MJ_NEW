package com.ecjia.network.query;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/3/1 13:39.
 */

public class NewsQuery extends PageQuery{
    protected static GoodsQuery instance;

    public static GoodsQuery getInstance() {
        if (instance == null) {
            synchronized (GoodsQuery.class) {
                if (instance == null) {
                    instance = new GoodsQuery();
                }
            }
        }
        return instance;
    }

    public NewsQuery() {
    }

    public String getQuery(int page,String id){
        requestJsonObject=new JSONObject();
        try {
            requestJsonObject.put("cat_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getQuery(page);
    }

    public String getNewsDetail(String article_id){
        requestJsonObject=new JSONObject();
        try {
            requestJsonObject.put("article_id", article_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.get();
    }

}
