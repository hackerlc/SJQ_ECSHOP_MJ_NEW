package com.ecjia.entity.responsemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class USER_INVITE  implements Serializable {

    private String invite_code; //邀请码
    private String invite_qrcode_image; //邀请码二维码图片
    private String invite_template; //邀请码分享模板
    private String invite_explain; //邀请码分享说明
    private String invite_url; //邀请url链接

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }

    public String getInvite_qrcode_image() {
        return invite_qrcode_image;
    }

    public void setInvite_qrcode_image(String invite_qrcode_image) {
        this.invite_qrcode_image = invite_qrcode_image;
    }

    public String getInvite_template() {
        return invite_template;
    }

    public void setInvite_template(String invite_template) {
        this.invite_template = invite_template;
    }

    public String getInvite_explain() {
        return invite_explain;
    }

    public void setInvite_explain(String invite_explain) {
        this.invite_explain = invite_explain;
    }

    public String getInvite_url() {
        return invite_url;
    }

    public void setInvite_url(String invite_url) {
        this.invite_url = invite_url;
    }

    public static USER_INVITE fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        USER_INVITE localItem = new USER_INVITE();

        JSONArray subItemArray;

        localItem.invite_code = jsonObject.optString("invite_code");
        localItem.invite_qrcode_image = jsonObject.optString("invite_qrcode_image");
        localItem.invite_template = jsonObject.optString("invite_template");
        localItem.invite_explain = jsonObject.optString("invite_explain");
        localItem.invite_url = jsonObject.optString("invite_url");

        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("invite_code", invite_code);
        localItemObject.put("invite_qrcode_image", invite_qrcode_image);
        localItemObject.put("invite_template", invite_template);
        localItemObject.put("invite_explain", invite_explain);
        localItemObject.put("invite_url", invite_url);
        return localItemObject;
    }

}

