package com.ecjia.entity.model;

import com.google.gson.annotations.SerializedName;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/3/16 14:15.
 */

public class Category {
    @SerializedName("category_id")
    protected String categoryId;
    @SerializedName("cat_name")
    protected String catName;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }
}
