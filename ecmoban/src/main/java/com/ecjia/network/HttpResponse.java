package com.ecjia.network;

import com.ecjia.entity.responsemodel.STATUS;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 2014/12/30.
 */
public interface HttpResponse {
    void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException;
}
