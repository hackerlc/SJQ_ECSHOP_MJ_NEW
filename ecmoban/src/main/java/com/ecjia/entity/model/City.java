package com.ecjia.entity.model;

import com.google.gson.annotations.SerializedName;

/**
 * SJQ_FXB_XiangMuDi_Application
 * Created by YichenZ on 2016/9/8 10:19.
 */

public class City {
    private String alias;
    private String name;
    private String label;
    @SerializedName("des")
    private String description;
    @SerializedName("img")
    private String imgUrl;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public String[] getLabelArray(){
        if(label==null)
            label="";
        return label.split(",");
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
