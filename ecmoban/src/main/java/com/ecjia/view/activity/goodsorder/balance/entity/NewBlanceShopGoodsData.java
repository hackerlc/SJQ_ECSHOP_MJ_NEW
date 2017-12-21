package com.ecjia.view.activity.goodsorder.balance.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-04-06.
 */

public class NewBlanceShopGoodsData implements Serializable {
    /**
     * goods_id : 2497
     * goods_name : 2017夏新款休闲中长款T恤印花T恤短袖
     * goods_number : 1
     * goods_price : 54.00
     * spec : [{"rec_id":"99","color":"黑色","size":"L","product_number":"1"}]
     */

    private String goods_id;
    private String goods_name;
    private String goods_image;
    private int goods_number;
    private String goods_price;
    private List<SpecBean> spec;

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_image() {
        return goods_image;
    }

    public void setGoods_image(String goods_image) {
        this.goods_image = goods_image;
    }

    public int getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(int goods_number) {
        this.goods_number = goods_number;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public List<SpecBean> getSpec() {
        return spec;
    }

    public void setSpec(List<SpecBean> spec) {
        this.spec = spec;
    }

    public static class SpecBean {
        /**
         * rec_id : 99
         * color : 黑色
         * size : L
         * product_number : 1
         */

        private String rec_id;
        private String color;
        private String size;
        private int product_number;

        public String getRec_id() {
            return rec_id;
        }

        public void setRec_id(String rec_id) {
            this.rec_id = rec_id;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public int getProduct_number() {
            return product_number;
        }

        public void setProduct_number(int product_number) {
            this.product_number = product_number;
        }
    }
}
