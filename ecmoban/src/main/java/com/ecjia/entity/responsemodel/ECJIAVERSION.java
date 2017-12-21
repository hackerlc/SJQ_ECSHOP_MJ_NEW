package com.ecjia.entity.responsemodel;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ECJIAVERSION implements Serializable {

    private String name;
    private String version;
    private String build;
    private String install_url;
    private String update_url;
    private String changelog;
    private String update_time;
    private Binary binary;


    public Binary getBinary() {
        return binary;
    }

    public String getName() {
        return name;
    }

    public void setBinary(Binary binary) {
        this.binary = binary;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getUpdate_url() {
        return update_url;
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }

    public ECJIAVERSION() {
    }


    public static ECJIAVERSION fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        ECJIAVERSION localItem = new ECJIAVERSION();
        localItem.name = jsonObject.optString("name");
        localItem.version = jsonObject.optString("version");
        localItem.build = jsonObject.optString("build");
        localItem.install_url = jsonObject.optString("install_url");
        localItem.update_url = jsonObject.optString("update_url");
        localItem.changelog = jsonObject.optString("changelog");
        localItem.update_time = jsonObject.optString("update_time");
        localItem.binary = Binary.fromJson(jsonObject.optJSONObject("binary"));
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("name", name);
        localItemObject.put("version", version);
        localItemObject.put("build", build);
        localItemObject.put("install_url", install_url);
        localItemObject.put("update_url", update_url);
        localItemObject.put("changelog", changelog);
        localItemObject.put("update_time", update_time);
        localItemObject.put("binary", binary.toJson());
        return localItemObject;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInstall_url() {
        return install_url;
    }

    public void setInstall_url(String install_url) {
        this.install_url = install_url;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }


    public static class Binary implements Serializable {
        public String getFsize() {
            return fsize;
        }

        public void setFsize(String fsize) {
            this.fsize = fsize;
        }

        public String fsize;

        public Binary() {
        }

        public Binary(String fsize) {
            this.fsize = fsize;
        }

        public static Binary fromJson(JSONObject jsonObject) throws JSONException {
            if (null == jsonObject) {
                return null;
            }
            Binary localItem = new Binary();
            localItem.fsize = jsonObject.optString("fsize");
            return localItem;
        }

        public JSONObject toJson() throws JSONException {
            JSONObject localItemObject = new JSONObject();
            localItemObject.put("fsize", fsize);
            return localItemObject;
        }

    }
}
