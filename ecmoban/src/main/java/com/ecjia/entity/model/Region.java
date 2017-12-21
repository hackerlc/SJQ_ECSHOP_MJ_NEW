package com.ecjia.entity.model;

import com.google.gson.annotations.SerializedName;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/4/15 13:21.
 */

public class Region {

    /**
     * region : 华东
     * city : 上海市
     */

    @SerializedName("region")
    private String region;
    @SerializedName("city")
    private String city;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
