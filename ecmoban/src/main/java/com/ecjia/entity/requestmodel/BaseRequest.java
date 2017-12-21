package com.ecjia.entity.requestmodel;

import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.SESSION;

import org.json.JSONException;

import java.io.Serializable;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-10.
 */

public class BaseRequest  implements Serializable {
//    public String  session=SESSION.getInstance().toJson().toString();
//    public String  device=DEVICE.getInstance().toJson().toString();

    private String session;
    private String device;

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void setInfo(){
        try {
            setSession(SESSION.getInstance().toJson().toString());
            setDevice(DEVICE.getInstance().toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
