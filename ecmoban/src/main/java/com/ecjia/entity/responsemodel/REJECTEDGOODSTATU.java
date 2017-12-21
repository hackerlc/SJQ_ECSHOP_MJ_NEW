package com.ecjia.entity.responsemodel;

import java.io.Serializable;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-08.
 */

public class REJECTEDGOODSTATU implements Serializable{

    private String rejected_status;//(0: 退款失败；1: 退款成功；2: 退款中；3：拒绝退款)

    public String getRejected_status() {
        return rejected_status;
    }

    public void setRejected_status(String rejected_status) {
        this.rejected_status = rejected_status;
    }
}
