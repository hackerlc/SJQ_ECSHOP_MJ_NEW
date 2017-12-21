package com.ecjia.network.query;

import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-02-20.
 */

public class NormalQuery extends AbstractQuery {

    public NormalQuery() {
        requestJsonObject = new JSONObject();
    }

    public String get() {

        try {
            requestJsonObject.put("session", SESSION.getInstance().toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
        } catch (JSONException e) {
            e.getMessage();
        }

        return requestJsonObject.toString();
    }

}
