
package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 相当于domain对象
 * @author Administrator
 *
 */
public class ADDRESS implements Serializable
{


    private int default_address;

    private String sign_building;

    private String city_name;


    private String consignee;

    private String tel;

    private String zipcode;


    private String country_name;


    private String country;


    private String city;

    private int id;


    private String province_name;


    private String district_name;


    private String email;

    private String address;


    private String province;

    private String district;


    private String best_time;

    private String mobile;

    public static ADDRESS fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        ADDRESS   localItem = new ADDRESS();

        JSONArray subItemArray;

        localItem.default_address = jsonObject.optInt("default_address");

        localItem.sign_building = jsonObject.optString("sign_building");

        localItem.city_name = jsonObject.optString("city_name");

        localItem.consignee = jsonObject.optString("consignee");

        localItem.tel = jsonObject.optString("tel");

        localItem.zipcode = jsonObject.optString("zipcode");

        localItem.country_name = jsonObject.optString("country_name");

        localItem.country = jsonObject.optString("country");

        localItem.city = jsonObject.optString("city");

        localItem.id = jsonObject.optInt("id");

        localItem.province_name = jsonObject.optString("province_name");

        localItem.district_name = jsonObject.optString("district_name");

        localItem.email = jsonObject.optString("email");

        localItem.address = jsonObject.optString("address");

        localItem.province = jsonObject.optString("province");

        localItem.district = jsonObject.optString("district");

        localItem.best_time = jsonObject.optString("best_time");

        localItem.mobile = jsonObject.optString("mobile");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("default_address", default_address);
        localItemObject.put("sign_building", sign_building);
        localItemObject.put("city_name", city_name);
        localItemObject.put("consignee", consignee);
        localItemObject.put("tel", tel);
        localItemObject.put("zipcode", zipcode);
        localItemObject.put("country_name", country_name);
        localItemObject.put("country", country);
        localItemObject.put("city", city);
        localItemObject.put("id", id);
        localItemObject.put("province_name", province_name);
        localItemObject.put("district_name", district_name);
        localItemObject.put("email", email);
        localItemObject.put("address", address);
        localItemObject.put("province", province);
        localItemObject.put("district", district);
        localItemObject.put("best_time", best_time);
        localItemObject.put("mobile", mobile);
        return localItemObject;
    }

    public int getDefault_address() {
        return default_address;
    }

    public void setDefault_address(int default_address) {
        this.default_address = default_address;
    }

    public String getSign_building() {
        return sign_building;
    }

    public void setSign_building(String sign_building) {
        this.sign_building = sign_building;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBest_time() {
        return best_time;
    }

    public void setBest_time(String best_time) {
        this.best_time = best_time;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

