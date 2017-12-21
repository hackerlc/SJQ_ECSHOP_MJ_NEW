package com.ecjia.consts;

/**
 * Created by Adam on 2016/10/19.
 */
public interface OrderType {
    String AWAIT_PAY = "await_pay";//待付款
    String AWAIT_SHIP = "await_ship";//待发货
    String SHIPPED = "shipped";//待收货
    String FINISHED = "finished";//已完成
    String ALLOW_COMMENT = "allow_comment";//待评价
    String CANCEL = "cancel";//取消订单
    String ALL = "all";//所有订单

    String AWAIT_TUANPI = "await_groupbuy";//待成团
    String RETURN_GOOD = "order_cancel";//退换货

//    allow_use; //可使用；expired已过期，is_used
//    已使用 //红包类型
    String ALLOW_USE="allow_use";
    String EXPIRED="expired";//expired已过期
    String IS_USED="is_used";//已使用
}
