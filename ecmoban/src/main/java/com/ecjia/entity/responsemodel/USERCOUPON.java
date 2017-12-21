package com.ecjia.entity.responsemodel;

import java.io.Serializable;

/**
 * 类名介绍：红包
 * Created by sun
 * Created time 2017-03-02.
 */

public class USERCOUPON implements Serializable{

    private String coupon_amount;//红包金额
    private String formatted_coupon_amount;

    private String coupon_id;
    private String coupon_name;

    private String start_date;
    private String formatted_start_date;
    private String end_date;
    private String formatted_end_date;

    private String label_status;//可使用
    private String request_amount;//需消费总额超过
    private String formatted_request_amount;

    private String status  ;//allow_use; //可使用；   expired已过期，   is_used已使用 //红包类型

    private String ru_id;
    private String shop_name;

    public String getCoupon_amount() {
        return coupon_amount;
    }

    public void setCoupon_amount(String coupon_amount) {
        this.coupon_amount = coupon_amount;
    }

    public String getFormatted_coupon_amount() {
        return formatted_coupon_amount;
    }

    public void setFormatted_coupon_amount(String formatted_coupon_amount) {
        this.formatted_coupon_amount = formatted_coupon_amount;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getFormatted_start_date() {
        return formatted_start_date;
    }

    public void setFormatted_start_date(String formatted_start_date) {
        this.formatted_start_date = formatted_start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getFormatted_end_date() {
        return formatted_end_date;
    }

    public void setFormatted_end_date(String formatted_end_date) {
        this.formatted_end_date = formatted_end_date;
    }

    public String getLabel_status() {
        return label_status;
    }

    public void setLabel_status(String label_status) {
        this.label_status = label_status;
    }

    public String getRequest_amount() {
        return request_amount;
    }

    public void setRequest_amount(String request_amount) {
        this.request_amount = request_amount;
    }

    public String getFormatted_request_amount() {
        return formatted_request_amount;
    }

    public void setFormatted_request_amount(String formatted_request_amount) {
        this.formatted_request_amount = formatted_request_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRu_id() {
        return ru_id;
    }

    public void setRu_id(String ru_id) {
        this.ru_id = ru_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }
}
