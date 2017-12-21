package com.ecjia.entity.responsemodel;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/3/30.
 */
public class KUAIDI {

    private String time;
    private String context;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public static KUAIDI fromJson(JSONObject json) {
        KUAIDI kuaidi = new KUAIDI();
        kuaidi.time = json.optString("time");
        kuaidi.context = json.optString("context");
        return kuaidi;
    }

}
