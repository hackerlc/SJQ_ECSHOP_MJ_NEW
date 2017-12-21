package com.ecjia.entity.responsemodel;

import java.io.Serializable;

/**
 * 类名介绍：买家提交寄回快递信息 返回
 * Created by sun
 * Created time 2017-03-10.
 */

public class RETURNSTATUS implements Serializable{

    private String return_status;

    public String getReturn_status() {
        return return_status;
    }

    public void setReturn_status(String return_status) {
        this.return_status = return_status;
    }
}
