package com.ecjia.entity.responsemodel;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GOODS_LIST implements Serializable {


    private String can_handsel;


    private String goods_sn;


    private String formated_subtotal;


    private String is_gift;


    private int goods_number;


    private String is_real;


    private PHOTO img;


    private String goods_name;


    private String pid;


    private String subtotal;


    private String is_shipping;


    private String goods_price;

    private ArrayList<GOODS_ATTR> goods_attr = new ArrayList<GOODS_ATTR>();


    private String formated_goods_price;


    private String goods_attr_id;


    private String market_price;


    private String rec_type;


    private int goods_id;


    private String extension_code;


    private String formated_market_price;


    private int rec_id;

    private String seller_id;

    private Boolean isCheckedbuy;//选中购买

    private Boolean ischeckDelete;//选中删除

    private String is_valid;////规格是否有效，1：有效正常显示 0：失效，灰色显示

    private String is_arrive;//是否到达批发价 1：到达 2：未达到 未到达就显示arrive_warn的内容

    private String arrive_warn;//满足起批量能享受批发价" //

    private String attr;//规格

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getIs_valid() {
        return is_valid;
    }

    public void setIs_valid(String is_valid) {
        this.is_valid = is_valid;
    }

    public String getIs_arrive() {
        return is_arrive;
    }

    public void setIs_arrive(String is_arrive) {
        this.is_arrive = is_arrive;
    }

    public String getArrive_warn() {
        return arrive_warn;
    }

    public void setArrive_warn(String arrive_warn) {
        this.arrive_warn = arrive_warn;
    }

    public Boolean getIsCheckedbuy() {
        return isCheckedbuy;
    }

    public void setIsCheckedbuy(Boolean isCheckedbuy) {
        if ("0".equals(is_valid)) {//0为失效
            this.isCheckedbuy = false;
        } else {
            this.isCheckedbuy = isCheckedbuy;
        }
    }

    public Boolean getIscheckDelete() {
        return ischeckDelete;
    }

    public void setIscheckDelete(Boolean ischeckDelete) {
        this.ischeckDelete = ischeckDelete;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    private String parent_id;

    public String getCan_handsel() {
        return can_handsel;
    }

    public void setCan_handsel(String can_handsel) {
        this.can_handsel = can_handsel;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getGoods_sn() {
        return goods_sn;
    }

    public void setGoods_sn(String goods_sn) {
        this.goods_sn = goods_sn;
    }

    public String getFormated_subtotal() {
        return formated_subtotal;
    }

    public void setFormated_subtotal(String formated_subtotal) {
        this.formated_subtotal = formated_subtotal;
    }

    public String getIs_gift() {
        return is_gift;
    }

    public void setIs_gift(String is_gift) {
        this.is_gift = is_gift;
    }

    public int getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(int goods_number) {
        this.goods_number = goods_number;
    }

    public String getIs_real() {
        return is_real;
    }

    public void setIs_real(String is_real) {
        this.is_real = is_real;
    }

    public PHOTO getImg() {
        return img;
    }

    public void setImg(PHOTO img) {
        this.img = img;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getIs_shipping() {
        return is_shipping;
    }

    public void setIs_shipping(String is_shipping) {
        this.is_shipping = is_shipping;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public ArrayList<GOODS_ATTR> getGoods_attr() {
        return goods_attr;
    }

    public void setGoods_attr(ArrayList<GOODS_ATTR> goods_attr) {
        this.goods_attr = goods_attr;
    }

    public String getFormated_goods_price() {
        return formated_goods_price;
    }

    public void setFormated_goods_price(String formated_goods_price) {
        this.formated_goods_price = formated_goods_price;
    }

    public String getGoods_attr_id() {
        return goods_attr_id;
    }

    public void setGoods_attr_id(String goods_attr_id) {
        this.goods_attr_id = goods_attr_id;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getRec_type() {
        return rec_type;
    }

    public void setRec_type(String rec_type) {
        this.rec_type = rec_type;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getExtension_code() {
        return extension_code;
    }

    public void setExtension_code(String extension_code) {
        this.extension_code = extension_code;
    }

    public String getFormated_market_price() {
        return formated_market_price;
    }

    public void setFormated_market_price(String formated_market_price) {
        this.formated_market_price = formated_market_price;
    }

    public int getRec_id() {
        return rec_id;
    }

    public void setRec_id(int rec_id) {
        this.rec_id = rec_id;
    }

    public static GOODS_LIST fromJson(JSONObject jsonObject)
            throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        GOODS_LIST localItem = new GOODS_LIST();

        JSONArray subItemArray;

        localItem.can_handsel = jsonObject.optString("can_handsel");

        localItem.goods_sn = jsonObject.optString("goods_sn");

        localItem.formated_subtotal = jsonObject.optString("formated_subtotal");

        localItem.is_gift = jsonObject.optString("is_gift");

        localItem.goods_number = jsonObject.optInt("goods_number");

        localItem.is_real = jsonObject.optString("is_real");
        localItem.img = PHOTO.fromJson(jsonObject.optJSONObject("img"));

        localItem.goods_name = jsonObject.optString("goods_name");

        localItem.pid = jsonObject.optString("pid");

        localItem.subtotal = jsonObject.optString("subtotal");

        localItem.is_shipping = jsonObject.optString("is_shipping");

        localItem.goods_price = jsonObject.optString("goods_price");

        subItemArray = jsonObject.optJSONArray("goods_attr");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                GOODS_ATTR subItem = GOODS_ATTR.fromJson(subItemObject);
                localItem.goods_attr.add(subItem);
            }
        }

        localItem.formated_goods_price = jsonObject
                .optString("formated_goods_price");

        localItem.goods_attr_id = jsonObject.optString("goods_attr_id");

        localItem.market_price = jsonObject.optString("market_price");

        localItem.rec_type = jsonObject.optString("rec_type");

        localItem.goods_id = jsonObject.optInt("goods_id");

        localItem.extension_code = jsonObject.optString("extension_code");

        localItem.formated_market_price = jsonObject
                .optString("formated_market_price");

        localItem.rec_id = jsonObject.optInt("rec_id");
        localItem.seller_id = jsonObject.optString("seller_id");
        localItem.parent_id = jsonObject.optString("parent_id");

        localItem.isCheckedbuy = true;
        localItem.ischeckDelete = false;
        localItem.is_valid = jsonObject.optString("is_valid");
        localItem.is_arrive = jsonObject.optString("is_arrive");
        localItem.arrive_warn = jsonObject.optString("arrive_warn");
        localItem.attr = jsonObject.optString("attr");

        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("can_handsel", can_handsel);
        localItemObject.put("goods_sn", goods_sn);
        localItemObject.put("formated_subtotal", formated_subtotal);
        localItemObject.put("is_gift", is_gift);
        localItemObject.put("goods_number", goods_number);
        localItemObject.put("is_real", is_real);
        if (null != img) {
            localItemObject.put("img", img.toJson());
        }
        localItemObject.put("goods_name", goods_name);
        localItemObject.put("pid", pid);
        localItemObject.put("subtotal", subtotal);
        localItemObject.put("is_shipping", is_shipping);
        localItemObject.put("goods_price", goods_price);

        for (int i = 0; i < goods_attr.size(); i++) {
            GOODS_ATTR itemData = goods_attr.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("goods_attr", itemJSONArray);
        localItemObject.put("formated_goods_price", formated_goods_price);
        localItemObject.put("goods_attr_id", goods_attr_id);
        localItemObject.put("market_price", market_price);
        localItemObject.put("rec_type", rec_type);
        localItemObject.put("goods_id", goods_id);
        localItemObject.put("extension_code", extension_code);
        localItemObject.put("formated_market_price", formated_market_price);
        localItemObject.put("rec_id", rec_id);
        localItemObject.put("parent_id", parent_id);
        localItemObject.put("seller_id", seller_id);
        localItemObject.put("is_valid", is_valid);
        localItemObject.put("is_arrive", is_arrive);
        localItemObject.put("arrive_warn", arrive_warn);
        localItemObject.put("attr", attr);
        return localItemObject;
    }

}
