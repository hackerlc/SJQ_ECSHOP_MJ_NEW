
package com.ecjia.entity.model;

public class SimpleGoods extends Goods{

    public String showPrice(){
        String showPrice="";
        if (promote_price != null && promote_price.length() > 0) {
            String str = promote_price.replace("￥", "").replace("元", "").replace("yuan", "").replace("¥", "");
            if ("免费".equals(str) || "0.00".equals(str)) {
                showPrice="免费";
            } else {
                showPrice=promote_price;
            }
        } else if (shop_price != null && shop_price.length() > 0) {
            String str = shop_price.replace("￥", "").replace("元", "").replace("yuan", "").replace("¥", "");
            if ("免费".equals(str) || "0.00".equals(str)) {
                showPrice="免费";
            } else {
                showPrice=shop_price;
            }
        }
        return showPrice;
    }

    @Override
    public String toString() {
        return super.toString()+"SimpleGoods{}";
    }
}
