package com.ecjia.entity.model;

import com.ecjia.entity.responsemodel.PLAYER;
import com.ecjia.entity.responsemodel.QUICK;

import java.util.ArrayList;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/2/28 14:41.
 */

public class MainData {
    public ArrayList<PLAYER> player;
    public ArrayList<QUICK> mobile_menu;
    public ArrayList<QUICK> activity_topic;
    public ArrayList<Market> yun_market;
    public ArrayList<GroupGoods> time_group;

    public ArrayList<PLAYER> getPlayer() {
        return player;
    }

    public void setPlayer(ArrayList<PLAYER> player) {
        this.player = player;
    }

    public ArrayList<QUICK> getMobile_menu() {
        return mobile_menu;
    }

    public void setMobile_menu(ArrayList<QUICK> mobile_menu) {
        this.mobile_menu = mobile_menu;
    }

    public ArrayList<QUICK> getActivity_topic() {
        return activity_topic;
    }

    public void setActivity_topic(ArrayList<QUICK> activity_topic) {
        this.activity_topic = activity_topic;
    }

    public ArrayList<Market> getYun_market() {
        return yun_market;
    }

    public void setYun_market(ArrayList<Market> yun_market) {
        this.yun_market = yun_market;
    }

    public ArrayList<GroupGoods> getTime_group() {
        return time_group;
    }

    public void setTime_group(ArrayList<GroupGoods> time_group) {
        this.time_group = time_group;
    }
}
