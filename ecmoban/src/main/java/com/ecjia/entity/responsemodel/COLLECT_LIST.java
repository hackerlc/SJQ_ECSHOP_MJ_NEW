
package com.ecjia.entity.responsemodel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class COLLECT_LIST
{


    private String shop_price;


    private String market_price;


    private String name;


    private int goods_id;


    private PHOTO   img;


    private String promote_price;


    private int rec_id;

    private String activity_type;
    private String formatted_saving_price; //已省xx元
    private int saving_price;

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

    public String getShop_price() {
        return shop_price;
    }

    public void setShop_price(String shop_price) {
        this.shop_price = shop_price;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public PHOTO getImg() {
        return img;
    }

    public void setImg(PHOTO img) {
        this.img = img;
    }

    public String getPromote_price() {
        return promote_price;
    }

    public void setPromote_price(String promote_price) {
        this.promote_price = promote_price;
    }

    public int getRec_id() {
        return rec_id;
    }

    public void setRec_id(int rec_id) {
        this.rec_id = rec_id;
    }

    public static COLLECT_LIST fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        COLLECT_LIST   localItem = new COLLECT_LIST();

        JSONArray subItemArray;

        localItem.shop_price = jsonObject.optString("shop_price");

        localItem.market_price = jsonObject.optString("market_price");

        localItem.name = jsonObject.optString("name");

        localItem.goods_id = jsonObject.optInt("goods_id");
        localItem.img = PHOTO.fromJson(jsonObject.optJSONObject("img"));

        localItem.promote_price = jsonObject.optString("promote_price");

        localItem.rec_id = jsonObject.optInt("rec_id");

        localItem.activity_type = jsonObject.optString("activity_type");
        localItem.saving_price = jsonObject.optInt("saving_price");
        localItem.formatted_saving_price = jsonObject.optString("formatted_saving_price");

        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("shop_price", shop_price);
        localItemObject.put("market_price", market_price);
        localItemObject.put("name", name);
        localItemObject.put("goods_id", goods_id);
        if(null!=img)
        {
            localItemObject.put("img", img.toJson());
        }
        localItemObject.put("promote_price", promote_price);
        localItemObject.put("rec_id", rec_id);
        localItemObject.put("activity_type", activity_type);
        localItemObject.put("saving_price", saving_price);
        localItemObject.put("formatted_saving_price", formatted_saving_price);

        return localItemObject;
    }

}
