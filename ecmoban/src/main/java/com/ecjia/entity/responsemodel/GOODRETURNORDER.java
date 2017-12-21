package com.ecjia.entity.responsemodel;

import java.io.Serializable;

/**
 * 类名介绍：退换货订单实体类
 * Created by sun
 * Created time 2017-03-09.
 */

public class GOODRETURNORDER implements Serializable {
//            "ret_id": "53",//退换货ID
//            "return_sn": "2017030314125223253",//退换货流水号
//            "apply_time": "2017-03-03 14:05:09",//申请时间
//            "should_return": "0.01",//退款金额
//            "return_shipping_fee": "0.00",//退邮费金额
//            "return_status": "0",//订单状态
//            "refound_status": "0",//退款状态
//            "return_type": "0",//退换货方式
//            "goods_name": "测试0.01商品 包邮",//退换货商品名称
//            "goods_thumb": "http://test.sjq.cn/images/201702/thumb_img/577_thumb_G_1487899198782.jpg",//退换货商品图片
//            "goods_id": "585",//商品ID
//            "return_number": "1", //退货数量
//            "ff_return_status": "已申请",//订单状态-文本
//            "ff_refound_status": "未退款",//退款状态-文本
//            "ff_return_type": "仅退款"//退换货方式-文本

    private String ret_id;//退换货ID
    private String return_sn;//退换货流水号
    private String apply_time;//申请时间
    private String should_return;//退款金额
    private String return_shipping_fee;//退邮费金额
    private String return_status;//订单状态
    private String refound_status;//退款状态
    private String return_type;//退换货方式
    private String goods_name;//退换货商品名称
    private String goods_thumb;//退换货商品图片
    private String goods_id;//商品ID
    private String return_number;//退货数量
    private String ff_return_status;//订单状态-文本
    private String ff_refound_status;//退款状态-文本
    private String ff_return_type;//退换货方式-文本

    //再次申请需要参数
    private String rec_id;//退换货方式-文本
    private String goods_number;//可退数量
    private String ceiling;//可退金额
    private String shipping_fee;//可退运费
    private String status_code;//:'await_ship'

    public String getRec_id() {
        return rec_id;
    }

    public void setRec_id(String rec_id) {
        this.rec_id = rec_id;
    }

    public String getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(String goods_number) {
        this.goods_number = goods_number;
    }

    public String getCeiling() {
        return ceiling;
    }

    public void setCeiling(String ceiling) {
        this.ceiling = ceiling;
    }

    public String getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(String shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getRet_id() {
        return ret_id;
    }

    public void setRet_id(String ret_id) {
        this.ret_id = ret_id;
    }

    public String getReturn_sn() {
        return return_sn;
    }

    public void setReturn_sn(String return_sn) {
        this.return_sn = return_sn;
    }

    public String getApply_time() {
        return apply_time;
    }

    public void setApply_time(String apply_time) {
        this.apply_time = apply_time;
    }

    public String getShould_return() {
        return should_return;
    }

    public void setShould_return(String should_return) {
        this.should_return = should_return;
    }

    public String getReturn_shipping_fee() {
        return return_shipping_fee;
    }

    public void setReturn_shipping_fee(String return_shipping_fee) {
        this.return_shipping_fee = return_shipping_fee;
    }

    public String getReturn_status() {
        return return_status;
    }

    public void setReturn_status(String return_status) {
        this.return_status = return_status;
    }

    public String getRefound_status() {
        return refound_status;
    }

    public void setRefound_status(String refound_status) {
        this.refound_status = refound_status;
    }

    public String getReturn_type() {
        return return_type;
    }

    public void setReturn_type(String return_type) {
        this.return_type = return_type;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_thumb() {
        return goods_thumb;
    }

    public void setGoods_thumb(String goods_thumb) {
        this.goods_thumb = goods_thumb;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getReturn_number() {
        return return_number;
    }

    public void setReturn_number(String return_number) {
        this.return_number = return_number;
    }

    public String getFf_return_status() {
        return ff_return_status;
    }

    public void setFf_return_status(String ff_return_status) {
        this.ff_return_status = ff_return_status;
    }

    public String getFf_refound_status() {
        return ff_refound_status;
    }

    public void setFf_refound_status(String ff_refound_status) {
        this.ff_refound_status = ff_refound_status;
    }

    public String getFf_return_type() {
        return ff_return_type;
    }

    public void setFf_return_type(String ff_return_type) {
        this.ff_return_type = ff_return_type;
    }
}
