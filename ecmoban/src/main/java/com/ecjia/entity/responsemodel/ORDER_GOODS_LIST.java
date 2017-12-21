
package com.ecjia.entity.responsemodel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class ORDER_GOODS_LIST   implements Serializable
{


    private String goods_number;


    private String goods_id;


    private String name;


    private PHOTO   img;


    private String formated_shop_price;


    private String subtotal;

    private String formatted_saving_price; //已省xx元
    private int saving_price;
    private String activity_type;

    private int is_commented;

    private String rec_id;

    private String ceiling;

    private String shipping_fee;

   /* "return": 1,//是否有退换货
            "return_status": 0,//当前有退换货状态
            "ret_id":14,//退换货ID*/
//    @SerializedName("return")
    private String return_id;
    private String return_status;
    private String ret_id;

    public String getReturn_id() {
        return return_id;
    }

    public void setReturn_id(String return_id) {
        this.return_id = return_id;
    }

    public String getReturn_status() {
        return return_status;
    }

    public void setReturn_status(String return_status) {
        this.return_status = return_status;
    }

    public String getRet_id() {
        return ret_id;
    }

    public void setRet_id(String ret_id) {
        this.ret_id = ret_id;
    }

    public String getCeiling() {
        return ceiling;
    }

    public void setCeiling(String ceiling) {
        this.ceiling = ceiling;
    }

    public String getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(String shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public String getRec_id() {
        return rec_id;
    }

    public void setRec_id(String rec_id) {
        this.rec_id = rec_id;
    }

    public int getIs_commented() {
        return is_commented;
    }

    public void setIs_commented(int is_commented) {
        this.is_commented = is_commented;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public String getFormatted_saving_price() {
        return formatted_saving_price;
    }

    public void setFormatted_saving_price(String formatted_saving_price) {
        this.formatted_saving_price = formatted_saving_price;
    }

    public int getSaving_price() {
        return saving_price;
    }

    public void setSaving_price(int saving_price) {
        this.saving_price = saving_price;
    }

    public String getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(String goods_number) {
        this.goods_number = goods_number;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PHOTO getImg() {
        return img;
    }

    public void setImg(PHOTO img) {
        this.img = img;
    }

    public String getFormated_shop_price() {
        return formated_shop_price;
    }

    public void setFormated_shop_price(String formated_shop_price) {
        this.formated_shop_price = formated_shop_price;
    }

    public static ORDER_GOODS_LIST fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        ORDER_GOODS_LIST   localItem = new ORDER_GOODS_LIST();

        JSONArray subItemArray;

        localItem.goods_number = jsonObject.optString("goods_number");

        localItem.goods_id = jsonObject.optString("goods_id");

        localItem.name = jsonObject.optString("name");
        localItem.img = PHOTO.fromJson(jsonObject.optJSONObject("img"));

        localItem.formated_shop_price = jsonObject.optString("formated_shop_price");

        localItem.subtotal = jsonObject.optString("subtotal");

        localItem.activity_type = jsonObject.optString("activity_type");
        localItem.saving_price = jsonObject.optInt("saving_price");
        localItem.formatted_saving_price = jsonObject.optString("formatted_saving_price");

        localItem.is_commented = jsonObject.optInt("is_commented");
        localItem.rec_id=jsonObject.optString("rec_id");
        localItem.ceiling=jsonObject.optString("ceiling");
        localItem.shipping_fee=jsonObject.optString("shipping_fee");
        localItem.return_id=jsonObject.optString("return");
        localItem.return_status=jsonObject.optString("return_status");
        localItem.ret_id=jsonObject.optString("ret_id");

        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("goods_number", goods_number);
        localItemObject.put("goods_id", goods_id);
        localItemObject.put("name", name);
        if(null!=img)
        {
            localItemObject.put("img", img.toJson());
        }
        localItemObject.put("formated_shop_price", formated_shop_price);
        localItemObject.put("subtotal", subtotal);

        localItemObject.put("activity_type", activity_type);
        localItemObject.put("saving_price", saving_price);
        localItemObject.put("formatted_saving_price", formatted_saving_price);
        localItemObject.put("is_commented", is_commented);
        localItemObject.put("rec_id", rec_id);
        localItemObject.put("ceiling", ceiling);
        localItemObject.put("shipping_fee", shipping_fee);
        localItemObject.put("return", return_id);
        localItemObject.put("return_status", return_status);
        localItemObject.put("ret_id", ret_id);
        return localItemObject;
    }

}
