package com.ecjia.entity.model;

import com.google.gson.annotations.SerializedName;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/4/7 10:01.
 */

public class News {


    /**
     * article_id : 23
     * title : 产品质量保证
     * link_url :
     * file_url : http://test.sjq.cn/
     */

    @SerializedName("article_id")
    private String articleId;
    @SerializedName("title")
    private String title;
    @SerializedName("link_url")
    private String linkUrl;
    @SerializedName("file_url")
    private String fileUrl;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
