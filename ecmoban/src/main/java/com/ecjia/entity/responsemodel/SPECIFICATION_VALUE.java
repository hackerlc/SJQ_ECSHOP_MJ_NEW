
package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SPECIFICATION_VALUE {


    private int id;


    private String price;


    private String label;


    private String format_price;

//    private String goods_attr;//颜色|尺码
//    private String product_number;//库存
//
//    private int sizeSelectCount = 0;
//
//    public String getGoods_attr() {
//        return goods_attr;
//    }
//
//    public void setGoods_attr(String goods_attr) {
//        this.goods_attr = goods_attr;
//    }
//
//    public int getSizeSelectCount() {
//        return sizeSelectCount;
//    }
//
//    public void setSizeSelectCount(int sizeSelectCount) {
//        this.sizeSelectCount = sizeSelectCount;
//    }
//
//    public String getProduct_number() {
//        return product_number;
//    }
//
//    public void setProduct_number(String product_number) {
//        this.product_number = product_number;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFormat_price() {
        return format_price;
    }

    public void setFormat_price(String format_price) {
        this.format_price = format_price;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public SPECIFICATION specification;

    public static SPECIFICATION_VALUE fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        SPECIFICATION_VALUE localItem = new SPECIFICATION_VALUE();

        JSONArray subItemArray;

        localItem.id = jsonObject.optInt("id");

        localItem.price = jsonObject.optString("price");

        localItem.label = jsonObject.optString("label");

        localItem.format_price = jsonObject.optString("format_price");
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("id", id);
        localItemObject.put("price", price);
        localItemObject.put("label", label);
        localItemObject.put("format_price", format_price);
        return localItemObject;
    }

}
