package com.ecjia.entity.responsemodel;

import java.io.Serializable;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-04-27.
 */

public class LOGINDATA implements Serializable {
    private SESSION session;
    private USER user;

    public SESSION getSession() {
        return session;
    }

    public void setSession(SESSION session) {
        this.session = session;
    }

    public USER getUser() {
        return user;
    }

    public void setUser(USER user) {
        this.user = user;
    }
}
