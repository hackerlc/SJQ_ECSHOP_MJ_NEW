package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/3.
 */
public class NEWGOODITEM implements Serializable {
    private String id;
    private String name;
    private ArrayList<GOODS_LIST> goodslist = new ArrayList<GOODS_LIST>();
    private Boolean isCheckedbuy;//选中购买
    private Boolean ischeckDelete;//选中删除

    public Boolean getIsCheckedbuy() {
        return isCheckedbuy;
    }

    public void setIsCheckedbuy(Boolean isCheckedbuy) {
        this.isCheckedbuy = isCheckedbuy;
    }

    public Boolean getIscheckDelete() {
        return ischeckDelete;
    }

    public void setIscheckDelete(Boolean ischeckDelete) {
        this.ischeckDelete = ischeckDelete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<GOODS_LIST> getGoodslist() {
        return goodslist;
    }

    public void setGoodslist(ArrayList<GOODS_LIST> goodslist) {
        this.goodslist = goodslist;
    }

    public static NEWGOODITEM fromJson(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }
        NEWGOODITEM item = new NEWGOODITEM();
        item.id = jsonObject.optString("id");
        item.name = jsonObject.optString("name");
        item.isCheckedbuy=true;
        item.ischeckDelete=false;
        JSONArray array = jsonObject.optJSONArray("goods_list");
        int size = array.length();
        try {
            if (null != array && size > 0) {
                item.goodslist.clear();
                for (int i = 0; i < size; i++) {
                    item.goodslist.add(GOODS_LIST.fromJson(array.optJSONObject(i)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();

        return localItemObject;
    }
}
