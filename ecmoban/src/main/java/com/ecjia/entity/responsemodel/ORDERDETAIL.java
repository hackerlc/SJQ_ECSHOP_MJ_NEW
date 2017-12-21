package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/1/29.
 */
public class ORDERDETAIL {

    public String getFormated_surplus() {
        return formated_surplus;
    }

    public void setFormated_surplus(String formated_surplus) {
        this.formated_surplus = formated_surplus;
    }

    private String formated_money_paid;

    public String getFormated_money_paid() {
        return formated_money_paid;
    }

    public void setFormated_money_paid(String formated_money_paid) {
        this.formated_money_paid = formated_money_paid;
    }

    private String formated_surplus;
    private String order_sn;
    private String order_time;
    private String formated_total_fee;
    private String goods_total_fee;
    private String formated_shipping_fee;
    private String consignee;
    private String province;
    private String city;
    private String address;
    private String pay_name;
    private String mobile;
    private String order_id;
    private String formated_tax;
    private String formated_integral_money;
    private String formated_discount;
    private String formated_bonus;
    private String formated_coupons;
    private String shipping_time;
    private String postscript;
    private String formated_order_amount;
    private String bonus;
    private String coupons;
    private String integral;
    private String integral_money;
    private String total_fee;
    private String order_amount;
    private String surplus;
    private String money_paid;
    private String label_order_status;
    private String status_code;

    private String pay_id;//1.17需要用到

    private int is_commented;  //是否已评论（0代表可评论）
    private int is_showorder;  //是否已晒单（0代表可晒单）

    private String formated_has_pay;//已支付金额

    private String is_cac;
    private String cac_mobile;
    private String cac_address;
    private String cac_user_name;

    private String extension_code;

    public String getExtension_code() {
        return extension_code;
    }

    public void setExtension_code(String extension_code) {
        this.extension_code = extension_code;
    }

    public String getIs_cac() {
        return is_cac;
    }

    public void setIs_cac(String is_cac) {
        this.is_cac = is_cac;
    }

    public String getCac_mobile() {
        return cac_mobile;
    }

    public void setCac_mobile(String cac_mobile) {
        this.cac_mobile = cac_mobile;
    }

    public String getCac_address() {
        return cac_address;
    }

    public void setCac_address(String cac_address) {
        this.cac_address = cac_address;
    }

    public String getCac_user_name() {
        return cac_user_name;
    }

    public void setCac_user_name(String cac_user_name) {
        this.cac_user_name = cac_user_name;
    }

    public String getFormated_coupons() {
        return formated_coupons;
    }

    public void setFormated_coupons(String formated_coupons) {
        this.formated_coupons = formated_coupons;
    }

    public String getCoupons() {
        return coupons;
    }

    public void setCoupons(String coupons) {
        this.coupons = coupons;
    }

    public String getFormated_has_pay() {
        return formated_has_pay;
    }

    public void setFormated_has_pay(String formated_has_pay) {
        this.formated_has_pay = formated_has_pay;
    }

    public String getFormated_order_amount() {
        return formated_order_amount;
    }

    public void setFormated_order_amount(String formated_order_amount) {
        this.formated_order_amount = formated_order_amount;
    }

    public String getPostscript() {
        return postscript;
    }

    public void setPostscript(String postscript) {
        this.postscript = postscript;
    }

    public String getShipping_time() {
        return shipping_time;
    }

    public void setShipping_time(String shipping_time) {
        this.shipping_time = shipping_time;
    }

    public String getFormated_integral_money() {
        return formated_integral_money;
    }

    public void setFormated_integral_money(String formated_integral_money) {
        this.formated_integral_money = formated_integral_money;
    }

    public String getFormated_discount() {
        return formated_discount;
    }

    public void setFormated_discount(String formated_discount) {
        this.formated_discount = formated_discount;
    }

    public String getFormated_bonus() {
        return formated_bonus;
    }

    public void setFormated_bonus(String formated_formated_bonus) {
        this.formated_bonus = formated_formated_bonus;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getFormated_total_fee() {
        return formated_total_fee;
    }

    public void setFormated_total_fee(String formated_total_fee) {
        this.formated_total_fee = formated_total_fee;
    }

    public String getGoods_total_fee() {
        return goods_total_fee;
    }

    public void setGoods_total_fee(String goods_total_fee) {
        this.goods_total_fee = goods_total_fee;
    }

    public String getFormated_shipping_fee() {
        return formated_shipping_fee;
    }

    public void setFormated_shipping_fee(String formated_shipping_fee) {
        this.formated_shipping_fee = formated_shipping_fee;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPay_id() {
        return pay_id;
    }

    public void setPay_id(String pay_id) {
        this.pay_id = pay_id;
    }

    public String getPay_name() {
        return pay_name;
    }

    public void setPay_name(String pay_name) {
        this.pay_name = pay_name;
    }

    public String getFormated_tax() {
        return formated_tax;
    }

    public void setFormated_tax(String formated_tax) {
        this.formated_tax = formated_tax;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getIntegral_money() {
        return integral_money;
    }

    public void setIntegral_money(String integral_money) {
        this.integral_money = integral_money;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }

    public String getMoney_paid() {
        return money_paid;
    }

    public void setMoney_paid(String money_paid) {
        this.money_paid = money_paid;
    }

    public String getLabel_order_status() {
        return label_order_status;
    }

    public void setLabel_order_status(String label_order_status) {
        this.label_order_status = label_order_status;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public int getIs_commented() {
        return is_commented;
    }

    public void setIs_commented(int is_commented) {
        this.is_commented = is_commented;
    }

    public int getIs_showorder() {
        return is_showorder;
    }

    public void setIs_showorder(int is_showorder) {
        this.is_showorder = is_showorder;
    }

    public static ORDERDETAIL fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        ORDERDETAIL localItem = new ORDERDETAIL();
        localItem.order_sn = jsonObject.optString("order_sn");
        localItem.order_time = jsonObject.optString("formated_add_time");
        localItem.formated_total_fee = jsonObject.optString("formated_total_fee");
        localItem.goods_total_fee = jsonObject.optString("formated_goods_amount");
        localItem.formated_shipping_fee = jsonObject.optString("formated_shipping_fee");
        localItem.consignee = jsonObject.optString("consignee");
        localItem.province = jsonObject.optString("province");
        localItem.city = jsonObject.optString("city");
        localItem.address = jsonObject.optString("address");
        localItem.pay_name = jsonObject.optString("pay_name");
        localItem.mobile = jsonObject.optString("mobile");
        localItem.order_id = jsonObject.optString("order_id");
        localItem.formated_discount = jsonObject.optString("formated_discount");
        localItem.formated_bonus = jsonObject.optString("formated_bonus");
        localItem.formated_integral_money = jsonObject.optString("formated_integral_money");
        localItem.shipping_time = jsonObject.optString("shipping_time");
        localItem.postscript = jsonObject.optString("postscript");
        localItem.formated_order_amount = jsonObject.optString("formated_order_amount");
        localItem.formated_tax = jsonObject.optString("formated_tax");
        localItem.bonus = jsonObject.optString("bonus");
        localItem.integral = jsonObject.optString("integral");
        localItem.integral_money = jsonObject.optString("integral_money");
        localItem.total_fee = jsonObject.optString("total_fee");

        localItem.formated_surplus = jsonObject.optString("formated_surplus");//剩余金额
        localItem.formated_money_paid = jsonObject.optString("formated_money_paid");//已付款金额
        localItem.pay_id = jsonObject.optString("pay_id");

        localItem.order_amount = jsonObject.optString("order_amount");
        localItem.surplus = jsonObject.optString("surplus");//剩余金额
        localItem.money_paid = jsonObject.optString("money_paid");//已付款金额

        localItem.status_code = jsonObject.optString("status_code");//订单状态
        localItem.label_order_status = jsonObject.optString("label_order_status");//订单状态

        localItem.is_commented = jsonObject.optInt("is_commented");//是否已评论（0代表可评论）
        localItem.is_showorder = jsonObject.optInt("is_showorder");//是否已晒单（0代表可晒单）

        localItem.formated_has_pay = jsonObject.optString("formated_has_pay");//已支付金额
        localItem.coupons = jsonObject.optString("coupons ");
        localItem.formated_coupons = jsonObject.optString("formated_coupons");

        localItem.is_cac = jsonObject.optString("is_cac");
        localItem.cac_mobile = jsonObject.optString("cac_mobile");
        localItem.cac_address = jsonObject.optString("cac_address");
        localItem.cac_user_name = jsonObject.optString("cac_user_name");
        localItem.extension_code = jsonObject.optString("extension_code");//订单商品活动

        return localItem;
    }

}
