package com.ecjia.entity.model;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/3/2 15:56.
 */

public class SnatchGoods extends Goods{
    private int all_num;
    private int buy_num;

    public int getAll_num() {
        return all_num;
    }

    public void setAll_num(int all_num) {
        this.all_num = all_num;
    }

    public int getBuy_num() {
        return buy_num;
    }

    public void setBuy_num(int buy_num) {
        this.buy_num = buy_num;
    }

    public String getRemaining() {
        int num=all_num - buy_num;
        if(num<=0){
            num=0;
        }
        return "仅剩"+num+"件";
    }
}
