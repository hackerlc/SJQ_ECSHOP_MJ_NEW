package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 猜你喜欢数据填充
 * Created by Administrator on 2015/3/25.
 */
public class RELATED_GOOD {



    private PHOTO   img;

    private String promote_price;


    private String shop_price;


    private String market_price;


    private String formated_market_price;


    private String formated_promote_price;

    private String formated_shop_price;

    private int id;

    private String good_name;

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

    public String getFormated_market_price() {
        return formated_market_price;
    }

    public void setFormated_market_price(String formated_market_price) {
        this.formated_market_price = formated_market_price;
    }

    public String getFormated_promote_price() {
        return formated_promote_price;
    }

    public void setFormated_promote_price(String formated_promote_price) {
        this.formated_promote_price = formated_promote_price;
    }

    public String getFormated_shop_price() {
        return formated_shop_price;
    }

    public void setFormated_shop_price(String formated_shop_price) {
        this.formated_shop_price = formated_shop_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGood_name() {
        return good_name;
    }

    public void setGood_name(String good_name) {
        this.good_name = good_name;
    }

    public static RELATED_GOOD fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        RELATED_GOOD   localItem = new RELATED_GOOD();

        try {
            localItem.img = PHOTO.fromJson(jsonObject.optJSONObject("img"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        localItem.good_name = jsonObject.optString("name");

        localItem.shop_price = jsonObject.optString("shop_price");

        localItem.id = jsonObject.optInt("goods_id");

        localItem.promote_price = jsonObject.optString("promote_price");

        localItem.market_price = jsonObject.optString("market_price");

        localItem.formated_promote_price = jsonObject.optString("formated_promote_price");

        localItem.formated_shop_price = jsonObject.optString("formated_shop_price");

        localItem.formated_market_price = jsonObject.optString("formated_market_price");


        return localItem;
    }


}
