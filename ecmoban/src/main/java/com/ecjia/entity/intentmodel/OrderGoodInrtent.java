package com.ecjia.entity.intentmodel;

import com.ecjia.entity.requestmodel.BaseRequest;

import java.io.Serializable;

/**
 * 类名介绍：提交参数
 * Created by sun
 * Created time 2017-03-14.
 */

public class OrderGoodInrtent extends BaseRequest implements Serializable {

    public OrderGoodInrtent() {
        super.setInfo();
    }
   /* rec_id = "22";(订单商品特定ID)
    cause_id= "1";(退换原因,此处传ID而非文本)
    return_number= "2";(退换货数量 )
    return_type= "0";(退换货类型0仅退款 1退货 2换货)
    credentials= "0";(是否有检测报告，不传默认为0 可不传)
    return_brief="因为有破损，所以退了吧"; //问题描述
    should_return=“26.00”; //退款金额
    return_shipping_fee=“5”; //退运费金额

    country=1; //国家
    province=6; //省
    city=76; //市
    district=694; //区
    street = 0; //街
    address = "金星路888号"; //联系地址
    zipcode="311222"; //邮编
    addressee='张芙蓉' //联系人
    phone=“13588446622”; //手机号码

    first_image:第一张凭证图片 ,
    second_image:第二张凭证图片 ,
    third_image:第三张凭证图片*/

    private String rec_id;
    private String cause_id;
    private String return_number;//数量
    private String return_type;//类型
    private String credentials;
    private String return_brief;
    private String should_return;//退款金额
    private String return_shipping_fee;////退运费金额

    private String country;
    private String province;
    private String city;
    private String district;
    private String street;
    private String address;
    private String zipcode;
    private String addressee;
    private String phone;

    private String first_image;
    private String second_image;
    private String third_image;

    private String ceiling;

    public String getCeiling() {
        return ceiling;
    }

    public void setCeiling(String ceiling) {
        this.ceiling = ceiling;
    }

    public String getRec_id() {
        return rec_id;
    }

    public void setRec_id(String rec_id) {
        this.rec_id = rec_id;
    }

    public String getCause_id() {
        return cause_id;
    }

    public void setCause_id(String cause_id) {
        this.cause_id = cause_id;
    }

    public String getReturn_number() {
        return return_number;
    }

    public void setReturn_number(String return_number) {
        this.return_number = return_number;
    }

    public String getReturn_type() {
        return return_type;
    }

    public void setReturn_type(String return_type) {
        this.return_type = return_type;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public String getReturn_brief() {
        return return_brief;
    }

    public void setReturn_brief(String return_brief) {
        this.return_brief = return_brief;
    }

    public String getShould_return() {
        return should_return;
    }

    public void setShould_return(String should_return) {
        this.should_return = should_return;
    }

    public String getReturn_shipping_fee() {
        return return_shipping_fee;
    }

    public void setReturn_shipping_fee(String return_shipping_fee) {
        this.return_shipping_fee = return_shipping_fee;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirst_image() {
        return first_image;
    }

    public void setFirst_image(String first_image) {
        this.first_image = first_image;
    }

    public String getSecond_image() {
        return second_image;
    }

    public void setSecond_image(String second_image) {
        this.second_image = second_image;
    }

    public String getThird_image() {
        return third_image;
    }

    public void setThird_image(String third_image) {
        this.third_image = third_image;
    }
}
