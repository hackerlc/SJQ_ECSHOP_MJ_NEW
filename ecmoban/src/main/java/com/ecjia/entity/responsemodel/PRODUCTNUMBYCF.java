package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 类名介绍：商品规格的库存
 * Created by sun
 * Created time 2017-02-23.
 */

public class PRODUCTNUMBYCF implements Serializable {
    private String goods_attr;//颜色|尺码
    private String product_number;//库存

    private int sizeSelectCount = 0;
    private String product_id;
    private String goods_attr_str;

    public String getShowSize(String colorID) {
        if (goods_attr.split("\\|")[0].equals(colorID)) {
            String s=goods_attr_str.split(",")[1];
            return s;
        }else{
            String s=goods_attr_str.split(",")[0];
            return s;
        }
    }

//    public void setSelect(String sizeSelectCount){
//        this.product_number=sizeSelectCount;
//    }

    public String getGoods_attr_str() {
        return goods_attr_str;
    }

    public void setGoods_attr_str(String goods_attr_str) {
        this.goods_attr_str = goods_attr_str;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getGoods_attr() {
        return goods_attr;
    }

    public void setGoods_attr(String goods_attr) {
        this.goods_attr = goods_attr;
    }

    public String getProduct_number() {
        if (product_number == null) {
            return "0";
        } else {
            return product_number;
        }
    }

    public void setProduct_number(String product_number) {
        this.product_number = product_number;
    }

    public int getSizeSelectCount() {
        return sizeSelectCount;
    }

    public void setSizeSelectCount(int sizeSelectCount) {
        this.sizeSelectCount = sizeSelectCount;
    }

    public static PRODUCTNUMBYCF fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        PRODUCTNUMBYCF localItem = new PRODUCTNUMBYCF();
        localItem.goods_attr = jsonObject.optString("goods_attr");
        localItem.product_number = jsonObject.optString("product_number");
        localItem.sizeSelectCount = jsonObject.optInt("sizeSelectCount");
        localItem.product_id = jsonObject.optString("product_id");
        localItem.goods_attr_str = jsonObject.optString("goods_attr_str");
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("goods_attr", goods_attr);
        localItemObject.put("product_number", product_number);
        localItemObject.put("sizeSelectCount", sizeSelectCount);
        localItemObject.put("product_id", product_id);
        localItemObject.put("goods_attr_str", goods_attr_str);
        return localItemObject;
    }

}
