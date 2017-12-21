package com.ecjia.entity.model;

import java.io.Serializable;

/**
 * 类名介绍：运费实体类
 * Created by sun
 * Created time 2017-02-23.
 */

public class AddressData implements Serializable{

    private String shipping_fee;
    private String is_shipping;//1为包邮

    public String getShipping_fee() {
        if("1".equals(is_shipping)){
            return "包邮";
        }else{
            if(Integer.parseInt(shipping_fee)<=0){
                return "包邮";
            }else{
                return shipping_fee;
            }
        }
    }

    public void setShipping_fee(String shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public String getIs_shipping() {
        return is_shipping;
    }

    public void setIs_shipping(String is_shipping) {
        this.is_shipping = is_shipping;
    }
}
