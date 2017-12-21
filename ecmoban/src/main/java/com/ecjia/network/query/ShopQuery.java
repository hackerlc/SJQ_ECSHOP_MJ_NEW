package com.ecjia.network.query;

import com.ecjia.entity.responsemodel.FILTER;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/3/1 13:39.
 */

public class ShopQuery extends PageQuery{
    public ShopQuery() {
    }

    public String getQuery(int page,String... filter){
        requestJsonObject=new JSONObject();
        try {
            requestJsonObject.put("market_id", filter.length>=1 ? filter[0]:"");
            requestJsonObject.put("select_type",filter.length>=2 ? filter[1]:"");
            requestJsonObject.put("cat_name", filter.length>=3 ? filter[2]:"");
            requestJsonObject.put("category_id", filter.length>=4 ? filter[3]:"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getQuery(page);
    }

    public String getFollow(String id){
        requestJsonObject=new JSONObject();
        try {
            requestJsonObject.put("seller_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.get();
    }
    public String getGoods(FILTER filter, String id, int page){
        requestJsonObject=new JSONObject();
        try {
            requestJsonObject.put("seller_id", id);
            requestJsonObject.put("filter", filter.toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getQuery(page);
    }
}
