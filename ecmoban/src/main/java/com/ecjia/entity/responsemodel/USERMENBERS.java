package com.ecjia.entity.responsemodel;

import java.io.Serializable;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-03.
 */

public class USERMENBERS implements Serializable {

    private String users_real;

    private String purchaser_valid;

    public String getPurchaser_valid() {
        return purchaser_valid;
    }

    public void setPurchaser_valid(String purchaser_valid) {
        this.purchaser_valid = purchaser_valid;
    }

    public String getUsers_real() {
        return users_real;
    }

    public void setUsers_real(String users_real) {
        this.users_real = users_real;
    }
}
