package com.ecjia.entity.eventmodel;

import com.ecjia.entity.responsemodel.SHIPPING;
import com.ecjia.view.activity.goodsorder.balance.entity.ShopCouponsData;

import java.io.Serializable;
import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-04-06.
 */

public class EventShopCoupons implements Serializable {

    private int position;

    private List<ShopCouponsData> listData;

    private List<SHIPPING> shippingList;

    public List<SHIPPING> getShippingList() {
        return shippingList;
    }

    public void setShippingList(List<SHIPPING> shippingList) {
        this.shippingList = shippingList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<ShopCouponsData> getListData() {
        return listData;
    }

    public void setListData(List<ShopCouponsData> listData) {
        this.listData = listData;
    }
}
