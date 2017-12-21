
package com.ecjia.entity.responsemodel;

import org.json.JSONException;
import org.json.JSONObject;


public class ORDER_NUM {


    private int shipped;

    private int await_ship;

    private int await_pay;

    private int finished;

    private int allow_comment;

    private int await_groupbuy;//待成团订单数量

    private int order_cancel;//退换货订单数量

    public int getAwait_groupbuy() {
        return await_groupbuy;
    }

    public void setAwait_groupbuy(int await_groupbuy) {
        this.await_groupbuy = await_groupbuy;
    }

    public int getOrder_cancel() {
        return order_cancel;
    }

    public void setOrder_cancel(int order_cancel) {
        this.order_cancel = order_cancel;
    }

    public int getAllow_comment() {
        return allow_comment;
    }

    public void setAllow_comment(int allow_comment) {
        this.allow_comment = allow_comment;
    }

    public int getShipped() {
        return shipped;
    }

    public void setShipped(int shipped) {
        this.shipped = shipped;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public int getAwait_ship() {
        return await_ship;
    }

    public void setAwait_ship(int await_ship) {
        this.await_ship = await_ship;
    }

    public int getAwait_pay() {
        return await_pay;
    }

    public void setAwait_pay(int await_pay) {
        this.await_pay = await_pay;
    }

    public static ORDER_NUM fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        ORDER_NUM localItem = new ORDER_NUM();
        localItem.shipped = jsonObject.optInt("shipped");
        localItem.await_ship = jsonObject.optInt("await_ship");
        localItem.await_pay = jsonObject.optInt("await_pay");
        localItem.finished = jsonObject.optInt("finished");
        localItem.allow_comment = jsonObject.optInt("allow_comment");
        localItem.await_groupbuy = jsonObject.optInt("await_groupbuy");
        localItem.order_cancel = jsonObject.optInt("order_cancel");

        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("shipped", shipped);
        localItemObject.put("await_ship", await_ship);
        localItemObject.put("await_pay", await_pay);
        localItemObject.put("finished", finished);
        localItemObject.put("allow_comment", allow_comment);
        localItemObject.put("await_groupbuy", await_groupbuy);
        localItemObject.put("order_cancel", order_cancel);

        return localItemObject;
    }

}
