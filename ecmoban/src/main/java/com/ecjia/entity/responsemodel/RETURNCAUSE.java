package com.ecjia.entity.responsemodel;

import java.io.Serializable;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-08.
 */

public class RETURNCAUSE implements Serializable {

    private String cause_id;
    private String cause_name;

    public String getCause_id() {
        return cause_id;
    }

    public void setCause_id(String cause_id) {
        this.cause_id = cause_id;
    }

    public String getCause_name() {
        return cause_name;
    }

    public void setCause_name(String cause_name) {
        this.cause_name = cause_name;
    }
}
