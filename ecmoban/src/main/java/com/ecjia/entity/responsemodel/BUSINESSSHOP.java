package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

public class BUSINESSSHOP {
    private String shopid;
    private String shopname;
    private String shoptitle;
    private String img;
    private String distance;
    private String street_cate;//店铺分类
    private String shop_address;
    private String shop_tagname;//店铺标签名称
    private String cate_name;
    private String tel;
    private String lat;
    private String lng;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public String getStreet_cate() {
        return street_cate;
    }

    public void setStreet_cate(String street_cate) {
        this.street_cate = street_cate;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getShop_tagname() {
        return shop_tagname;
    }

    public void setShop_tagname(String shop_tagname) {
        this.shop_tagname = shop_tagname;
    }

    public String getShoptitle() {
        return shoptitle;
    }

    public void setShoptitle(String shoptitle) {
        this.shoptitle = shoptitle;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public static BUSINESSSHOP fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        BUSINESSSHOP localItem = new BUSINESSSHOP();

        //localItem.setImg(SHOPIMG.fromJson(jsonObject.optJSONObject("street_logo")));
        localItem.setImg(jsonObject.optString("street_logo"));
        localItem.setShopid(jsonObject.optString("id"));
        localItem.setShopname(jsonObject.optString("shop_name"));
        localItem.setDistance(jsonObject.optString("distance"));
        localItem.setShoptitle(jsonObject.optString("shop_title"));
        localItem.setStreet_cate(jsonObject.optString("street_cate"));
        localItem.setShop_tagname(jsonObject.optString("tag_name"));
        localItem.setCate_name(jsonObject.optString("cate_name"));
        localItem.setShop_address(jsonObject.optString("shop_address"));
        localItem.setTel(jsonObject.optString("tel"));
        localItem.setLat(jsonObject.optString("lat"));
        localItem.setLng(jsonObject.optString("lng"));
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
//	     if(null!=img)
//	     {
//	       localItemObject.put("street_logo", img.toJson());
//	     }
        localItemObject.put("street_logo", img);
        localItemObject.put("id", shopid);
        localItemObject.put("shop_name", shopname);
        localItemObject.put("distance", distance);
        localItemObject.put("shop_title", shoptitle);
        localItemObject.put("street_cate", street_cate);
        localItemObject.put("tag_name", shop_tagname);
        localItemObject.put("cate_name", cate_name);
        localItemObject.put("shop_address", shop_address);
        localItemObject.put("tel", tel);
        localItemObject.put("lat",lat);
        localItemObject.put("lng",lng);
        return localItemObject;
    }
}
