package com.ecjia.entity.responsemodel;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GOODS {

    private String promote_end_date;//倒计时时间

    private int click_count;

    private String goods_sn;

    private String promote_start_date;

    private String goods_number;

    private String hx_text;
    private String hxkf_id;
    private String hxkf_name;

    private ArrayList<PRICE> rank_prices = new ArrayList<PRICE>();

    private PHOTO img;

    private String brand_id;

    private ArrayList<PHOTO> pictures = new ArrayList<PHOTO>();

    private String goods_name;

    private ArrayList<PROPERTY> properties = new ArrayList<PROPERTY>();

    private String goods_weight;

    private String promote_price;//抢批促销价格

    private String formated_promote_price;//抢批促销价格 带符号

    private String formatted_saving_price;

    private String integral;

    private int id;

    private int goods_id;

    private int rec_id;

    private String cat_id;

    private String is_shipping;

    private String shop_price;

    private String market_price;//市场价=>建议零售价

    private int collected;

    private String activity_type;  //"GROUPBUY_GOODS";// 商品活动类型 GROUPBUY_GOODS 为团批商品 GRAB_GOODS 为抢批商品

    private String object_id;

    private String give_integral;//送积分

    private double deposit;//保证金(仅团批商品展示)

    private List<GOODSPRICELADDER> price_ladder = new ArrayList<GOODSPRICELADDER>();//团批商品展示阶梯价

    private int valid_goods;//团批商品 当前销售数量

    private List<GOODSVOLUMEPRICE> volume_price = new ArrayList<GOODSVOLUMEPRICE>();//商品类型为GENERAL_GOODS时并且用户已认证需要返回阶梯价，最多3个

    private List<SHOOPSERVICES> shop_services = new ArrayList<SHOOPSERVICES>();//商品服务

    private int shipping_fee;// 运费 传了area_id 才会正常传递

    private int batch_amount;//"batch_amount":10,//起批量

    private List<PRODUCTNUMBYCF> products= new ArrayList<PRODUCTNUMBYCF>();//"products" = (//多规格商品库存

    public ArrayList<SPECIFICATION> specification = new ArrayList<SPECIFICATION>();

    private String group_status;//团批抢批状态 0为未开始，1为进行中，2为过期
    private String starttime;//距开始时差单位为秒
    private String endtime;//距结束时差单位为秒

//    "group_status": 1,//团批抢批状态 0为未开始，1为进行中，2为过期
//            "starttime": 0,//距开始时差单位为秒
//            "endtime": 140247529,//距结束时差单位为秒

    /**
     * 分享链接
     */
    private String share_link;

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    private ArrayList<GOODSCOUPONS> coupons = new ArrayList<GOODSCOUPONS>();

    public ArrayList<GOODSCOUPONS> getCoupons() {
        return coupons;
    }

    public void setCoupons(ArrayList<GOODSCOUPONS> coupons) {
        this.coupons = coupons;
    }

    public List<PRODUCTNUMBYCF> getProducts() {
        return products;
    }

    public void setProducts(List<PRODUCTNUMBYCF> products) {
        this.products = products;
    }

    public int getBatch_amount() {
        return batch_amount;
    }

    public void setBatch_amount(int batch_amount) {
        this.batch_amount = batch_amount;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public List<GOODSPRICELADDER> getPrice_ladder() {
        return price_ladder;
    }

    public String getHxkf_id() {
        return hxkf_id;
    }

    public void setHxkf_id(String hxkf_id) {
        this.hxkf_id = hxkf_id;
    }

    public String getHxkf_name() {
        return hxkf_name;
    }

    public void setHxkf_name(String hxkf_name) {
        this.hxkf_name = hxkf_name;
    }

    public void setPrice_ladder(List<GOODSPRICELADDER> price_ladder) {
        this.price_ladder = price_ladder;
    }

    public String getHx_text() {
        return hx_text;
    }

    public void setHx_text(String hx_text) {
        this.hx_text = hx_text;
    }

    public int getValid_goods() {
        return valid_goods;
    }

    public void setValid_goods(int valid_goods) {
        this.valid_goods = valid_goods;
    }

    public List<GOODSVOLUMEPRICE> getVolume_price() {
        return volume_price;
    }

    public void setVolume_price(List<GOODSVOLUMEPRICE> volume_price) {
        this.volume_price = volume_price;
    }

    public List<SHOOPSERVICES> getShop_services() {
        return shop_services;
    }

    public void setShop_services(List<SHOOPSERVICES> shop_services) {
        this.shop_services = shop_services;
    }

    public int getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(int shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public String getShare_link() {
        return share_link;
    }

    public void setShare_link(String share_link) {
        this.share_link = share_link;
    }

    private ArrayList<FAVOUR> favours = new ArrayList<FAVOUR>();

    private ArrayList<GOODS_COUPON> goods_coupon = new ArrayList<GOODS_COUPON>();

    public ArrayList<GOODS_COUPON> getGoods_coupon() {
        return goods_coupon;
    }

    public void setGoods_coupon(ArrayList<GOODS_COUPON> goods_coupon) {
        this.goods_coupon = goods_coupon;
    }

    public ArrayList<FAVOUR> getFavours() {
        return favours;
    }

    public void setFavours(ArrayList<FAVOUR> favours) {
        this.favours = favours;
    }

    public String getGive_integral() {
        return give_integral;
    }

    public void setGive_integral(String give_integral) {
        this.give_integral = give_integral;
    }

    public String getUnformatted_shop_price() {
        return unformatted_shop_price;
    }

    public void setUnformatted_shop_price(String unformatted_shop_price) {
        this.unformatted_shop_price = unformatted_shop_price;
    }

    private String unformatted_shop_price;

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

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public int getRec_id() {
        return rec_id;
    }

    public void setRec_id(int rec_id) {
        this.rec_id = rec_id;
    }

    public String getPromote_end_date() {
        return promote_end_date;
    }

    public void setPromote_end_date(String promote_end_date) {
        this.promote_end_date = promote_end_date;
    }

    public String getShop_price() {
        return shop_price;
    }

    public void setShop_price(String shop_price) {
        this.shop_price = shop_price;
    }

    public int getCollected() {
        return collected;
    }

    public void setCollected(int collected) {
        this.collected = collected;
    }

    public int getClick_count() {
        return click_count;
    }

    public void setClick_count(int click_count) {
        this.click_count = click_count;
    }

    public String getGoods_sn() {
        return goods_sn;
    }

    public void setGoods_sn(String goods_sn) {
        this.goods_sn = goods_sn;
    }

    public String getPromote_start_date() {
        return promote_start_date;
    }

    public void setPromote_start_date(String promote_start_date) {
        this.promote_start_date = promote_start_date;
    }

    public String getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(String goods_number) {
        this.goods_number = goods_number;
    }

    public ArrayList<PRICE> getRank_prices() {
        return rank_prices;
    }

    public void setRank_prices(ArrayList<PRICE> rank_prices) {
        this.rank_prices = rank_prices;
    }

    public PHOTO getImg() {
        return img;
    }

    public void setImg(PHOTO img) {
        this.img = img;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public ArrayList<PHOTO> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<PHOTO> pictures) {
        this.pictures = pictures;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public ArrayList<PROPERTY> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<PROPERTY> properties) {
        this.properties = properties;
    }

    public String getGoods_weight() {
        return goods_weight;
    }

    public void setGoods_weight(String goods_weight) {
        this.goods_weight = goods_weight;
    }

    public String getPromote_price() {
        return promote_price;
    }

    public void setPromote_price(String promote_price) {
        this.promote_price = promote_price;
    }

    public String getFormated_promote_price() {
        return formated_promote_price;
    }

    public void setFormated_promote_price(String formated_promote_price) {
        this.formated_promote_price = formated_promote_price;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getIs_shipping() {
        return is_shipping;
    }

    public void setIs_shipping(String is_shipping) {
        this.is_shipping = is_shipping;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getGroup_status() {
        return group_status;
    }

    public void setGroup_status(String group_status) {
        this.group_status = group_status;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public ArrayList<SPECIFICATION> getSpecification() {
        return specification;
    }

    public void setSpecification(ArrayList<SPECIFICATION> specification) {
        this.specification = specification;
    }

    public static GOODS fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        GOODS localItem = new GOODS();

        JSONArray subItemArray;

        localItem.promote_end_date = jsonObject.optString("promote_end_date");
        localItem.hxkf_id = jsonObject.optString("hxkf_id");
        localItem.hxkf_name = jsonObject.optString("hxkf_name");

        localItem.click_count = jsonObject.optInt("click_count");

        localItem.goods_sn = jsonObject.optString("goods_sn");

        localItem.promote_start_date = jsonObject.optString("promote_start_date");

        localItem.goods_number = jsonObject.optString("goods_number");
        localItem.hx_text = jsonObject.optString("hx_text");

        subItemArray = jsonObject.optJSONArray("rank_prices");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                PRICE subItem = PRICE.fromJson(subItemObject);
                localItem.rank_prices.add(subItem);
            }
        }

        localItem.img = PHOTO.fromJson(jsonObject.optJSONObject("img"));

        localItem.brand_id = jsonObject.optString("brand_id");

        subItemArray = jsonObject.optJSONArray("pictures");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                PHOTO subItem = PHOTO.fromJson(subItemObject);
                localItem.pictures.add(subItem);
            }
        }


        localItem.goods_name = jsonObject.optString("goods_name");

        subItemArray = jsonObject.optJSONArray("properties");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                PROPERTY subItem = PROPERTY.fromJson(subItemObject);
                localItem.properties.add(subItem);
            }
        }


        localItem.goods_weight = jsonObject.optString("goods_weight");

        localItem.promote_price = jsonObject.optString("promote_price");

        localItem.formated_promote_price = jsonObject.optString("formated_promote_price");

        localItem.formatted_saving_price = jsonObject.optString("formatted_saving_price");

        localItem.integral = jsonObject.optString("integral");

        localItem.id = jsonObject.optInt("id");

        localItem.rec_id = jsonObject.optInt("rec_id");

        localItem.goods_id = jsonObject.optInt("goods_id");

        localItem.cat_id = jsonObject.optString("cat_id");

        localItem.is_shipping = jsonObject.optString("is_shipping");

        localItem.shop_price = jsonObject.optString("shop_price");

        localItem.unformatted_shop_price = jsonObject.optString("unformatted_shop_price");

        localItem.market_price = jsonObject.optString("market_price");

        localItem.collected = jsonObject.optInt("collected");

        localItem.activity_type = jsonObject.optString("activity_type");

        subItemArray = jsonObject.optJSONArray("specification");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                SPECIFICATION subItem = SPECIFICATION.fromJson(subItemObject);
                localItem.specification.add(subItem);
            }
        }

        localItem.give_integral = jsonObject.optString("give_integral");


        subItemArray = jsonObject.optJSONArray("favourable_list");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                FAVOUR subItem = FAVOUR.fromJson(subItemObject);
                localItem.favours.add(subItem);
            }
        }

        subItemArray = jsonObject.optJSONArray("goods_coupon");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                GOODS_COUPON subItem = GOODS_COUPON.fromJson(subItemObject);
                localItem.goods_coupon.add(subItem);
            }
        }

        localItem.share_link = jsonObject.optString("share_link");

        localItem.deposit = jsonObject.optDouble("deposit");

        subItemArray = jsonObject.optJSONArray("price_ladder");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                GOODSPRICELADDER subItem = GOODSPRICELADDER.fromJson(subItemObject);
                localItem.price_ladder.add(subItem);
            }
        }

        localItem.valid_goods = jsonObject.optInt("valid_goods");

        subItemArray = jsonObject.optJSONArray("volume_price");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                GOODSVOLUMEPRICE subItem = GOODSVOLUMEPRICE.fromJson(subItemObject);
                localItem.volume_price.add(subItem);
            }
        }

        subItemArray = jsonObject.optJSONArray("shop_services");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                SHOOPSERVICES subItem = SHOOPSERVICES.fromJson(subItemObject);
                localItem.shop_services.add(subItem);
            }
        }

        localItem.shipping_fee = jsonObject.optInt("shipping_fee");
        localItem.batch_amount = jsonObject.optInt("batch_amount");

        subItemArray = jsonObject.optJSONArray("products");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                PRODUCTNUMBYCF subItem = PRODUCTNUMBYCF.fromJson(subItemObject);
                localItem.products.add(subItem);
            }
        }
        localItem.group_status = jsonObject.optString("group_status");
        localItem.starttime = jsonObject.optString("starttime");
        localItem.endtime = jsonObject.optString("endtime");
        localItem.object_id = jsonObject.optString("object_id");

        subItemArray = jsonObject.optJSONArray("coupons");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                GOODSCOUPONS subItem = GOODSCOUPONS.fromJson(subItemObject);
                localItem.coupons.add(subItem);
            }
        }
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("promote_end_date", promote_end_date);
        localItemObject.put("click_count", click_count);
        localItemObject.put("goods_sn", goods_sn);
        localItemObject.put("promote_start_date", promote_start_date);
        localItemObject.put("goods_number", goods_number);
        localItemObject.put("hx_text", hx_text);
        localItemObject.put("hxkf_id", hxkf_id);
        localItemObject.put("hxkf_name", hxkf_name);

        for (int i = 0; i < rank_prices.size(); i++) {
            PRICE itemData = rank_prices.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("rank_prices", itemJSONArray);
        if (null != img) {
            localItemObject.put("img", img.toJson());
        }
        localItemObject.put("brand_id", brand_id);

        for (int i = 0; i < pictures.size(); i++) {
            PHOTO itemData = pictures.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("pictures", itemJSONArray);
        localItemObject.put("goods_name", goods_name);

        for (int i = 0; i < properties.size(); i++) {
            PROPERTY itemData = properties.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("properties", itemJSONArray);
        localItemObject.put("goods_weight", goods_weight);
        localItemObject.put("promote_price", promote_price);
        localItemObject.put("formated_promote_price", formated_promote_price);
        localItemObject.put("formatted_saving_price", formatted_saving_price);
        localItemObject.put("integral", integral);
        localItemObject.put("id", id);
        localItemObject.put("rec_id", rec_id);
        localItemObject.put("goods_id", goods_id);
        localItemObject.put("cat_id", cat_id);
        localItemObject.put("is_shipping", is_shipping);
        localItemObject.put("shop_price", shop_price);
        localItemObject.put("unformatted_shop_price", unformatted_shop_price);
        localItemObject.put("market_price", market_price);
        localItemObject.put("collected", collected);
        localItemObject.put("activity_type", activity_type);

        for (int i = 0; i < specification.size(); i++) {
            SPECIFICATION itemData = specification.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("specification", itemJSONArray);

        localItemObject.put("give_integral", give_integral);


        JSONArray favourable_listJSONArray = new JSONArray();

        for (int i = 0; i < favours.size(); i++) {
            FAVOUR itemData = favours.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            favourable_listJSONArray.put(itemJSONObject);
        }
        localItemObject.put("favourable_list", favourable_listJSONArray);


        JSONArray goods_couponJSONArray = new JSONArray();

        for (int i = 0; i < goods_coupon.size(); i++) {
            GOODS_COUPON itemData = goods_coupon.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            goods_couponJSONArray.put(itemJSONObject);
        }
        localItemObject.put("goods_coupon", goods_couponJSONArray);
        localItemObject.put("share_link", share_link);

        localItemObject.put("deposit", deposit);

        JSONArray price_ladderJSONArray = new JSONArray();
        for (int i = 0; i < price_ladder.size(); i++) {
            GOODSPRICELADDER itemData = price_ladder.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            price_ladderJSONArray.put(itemJSONObject);
        }
        localItemObject.put("price_ladder", price_ladderJSONArray);

        localItemObject.put("valid_goods", valid_goods);

        JSONArray volume_priceJSONArray = new JSONArray();
        for (int i = 0; i < volume_price.size(); i++) {
            GOODSVOLUMEPRICE itemData = volume_price.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            volume_priceJSONArray.put(itemJSONObject);
        }
        localItemObject.put("volume_price", volume_priceJSONArray);

        JSONArray shop_servicesJSONArray = new JSONArray();
        for (int i = 0; i < shop_services.size(); i++) {
            SHOOPSERVICES itemData = shop_services.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            shop_servicesJSONArray.put(itemJSONObject);
        }
        localItemObject.put("shop_services", shop_servicesJSONArray);

        localItemObject.put("shipping_fee", shipping_fee);

        localItemObject.put(" batch_amount", batch_amount);

        JSONArray productsJSONArray = new JSONArray();
        for (int i = 0; i < products.size(); i++) {
            PRODUCTNUMBYCF itemData = products.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            productsJSONArray.put(itemJSONObject);
        }
        localItemObject.put("group_status", group_status);
        localItemObject.put("starttime", starttime);
        localItemObject.put("endtime", endtime);
        localItemObject.put("object_id", object_id);

        JSONArray couponsJSONArray = new JSONArray();
        for (int i = 0; i < coupons.size(); i++) {
            GOODSCOUPONS itemData = coupons.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            couponsJSONArray.put(itemJSONObject);
        }
        return localItemObject;
    }

    public String getSelectProductsNumByID(String idColor, String idSize) {
        String num = "0";
        for (int i = 0; i < products.size(); i++) {
            PRODUCTNUMBYCF itemData = products.get(i);
            if (!TextUtils.isEmpty(idSize)) {
                if (itemData.getGoods_attr().contains(idColor) && itemData.getGoods_attr().contains(idSize)) {
                    num = itemData.getProduct_number();
                }
            }else{
                if (itemData.getGoods_attr().contains(idColor)) {
                    num = itemData.getProduct_number();
                }
            }
        }
        return num;
    }

    public List<PRODUCTNUMBYCF> getSizeListByColorId(String idColor){
        List<PRODUCTNUMBYCF> list=new ArrayList<>();
        list.clear();
        for (int i = 0; i < products.size(); i++) {
            PRODUCTNUMBYCF itemData = products.get(i);
            if (!TextUtils.isEmpty(idColor)) {
                if (itemData.getGoods_attr().contains(idColor)) {
                    list.add(itemData);
                }
            }
        }
        return list;
    }

}
