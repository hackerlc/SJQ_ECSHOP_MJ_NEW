package com.ecjia.entity.responsemodel;

import java.io.Serializable;
import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-09.
 */

public class ODERRETURNGOODSDETAIL implements Serializable {

//    "data": {
//        "ret_id": "46",
//                "user_id": "82",
//                "back_shipping_name": "23",
//                "back_invoice_no": "sdfasdfa",
//                "out_shipping_name": "23",
//                "out_invoice_no": "sdf",
//                "cause_id": "18",
//                "return_sn": "2017030113596465820",
//                "apply_time": "1488318769",
//                "should_return": "5005.00",
//                "return_shipping_fee": "0.00",
//                "return_status": "4",//0为已申请,1为卖家收到，2为卖家寄出（分单），3为卖家寄出，4为完成，5为同意申请，6为买家寄出，9为卖家拒绝
//                "refound_status": "0",//0为未退款（退货，换货），1为已退款(退货，换货)
//                "return_type": "2",//(退换货类型0仅退款 1退货 2换货)
//                "goods_name": "乐视超级电视 S50 Air 3D 高配版 50英寸智能LED液晶电视 网络智能电视 官方标配+超薄可调壁挂架 需要自行安装",
//                "goods_thumb": "images/201512/thumb_img/477_thumb_G_1451090267635.jpg",
//                "goods_id": "477",
//                "return_number": "1",

    private String data;
    private String ret_id;
    private String user_id;
    private String back_shipping_name;
    private String back_invoice_no;
    private String out_shipping_name;
    private String out_invoice_no;
    private String cause_id;
    private String return_sn;
    private String apply_time;
    private String should_return;
    private String return_shipping_fee;
    private String return_status;
    private String refound_status;
    private String return_type;
    private String goods_name;
    private String goods_thumb;
    private String goods_id;
    private String return_number;
    private String repeat;

    private List<ReturnGoodInfoList> log;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getRet_id() {
        return ret_id;
    }

    public void setRet_id(String ret_id) {
        this.ret_id = ret_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBack_shipping_name() {
        return back_shipping_name;
    }

    public void setBack_shipping_name(String back_shipping_name) {
        this.back_shipping_name = back_shipping_name;
    }

    public String getBack_invoice_no() {
        return back_invoice_no;
    }

    public void setBack_invoice_no(String back_invoice_no) {
        this.back_invoice_no = back_invoice_no;
    }

    public String getOut_shipping_name() {
        return out_shipping_name;
    }

    public void setOut_shipping_name(String out_shipping_name) {
        this.out_shipping_name = out_shipping_name;
    }

    public String getOut_invoice_no() {
        return out_invoice_no;
    }

    public void setOut_invoice_no(String out_invoice_no) {
        this.out_invoice_no = out_invoice_no;
    }

    public String getCause_id() {
        return cause_id;
    }

    public void setCause_id(String cause_id) {
        this.cause_id = cause_id;
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

    public List<ReturnGoodInfoList> getLog() {
        return log;
    }

    public void setLog(List<ReturnGoodInfoList> log) {
        this.log = log;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public  class ReturnGoodInfoList{

//        "return_status": "卖家已将换货商品寄出",
//                "action_note": "中通速递:sdf",
//                "shipping_code": "ship_zto",//如果有快递显示快递公司简号
//                "invoice_no": "sdf",//快递单号
//                "log_time": "2017-03-01 13:54:32"
        private String  return_status;
        private String  action_note;
        private String  shipping_code;
        private String  invoice_no;
        private String  log_time;

        public String getReturn_status() {
            return return_status;
        }

        public void setReturn_status(String return_status) {
            this.return_status = return_status;
        }

        public String getAction_note() {
            return action_note;
        }

        public void setAction_note(String action_note) {
            this.action_note = action_note;
        }

        public String getShipping_code() {
            return shipping_code;
        }

        public void setShipping_code(String shipping_code) {
            this.shipping_code = shipping_code;
        }

        public String getInvoice_no() {
            return invoice_no;
        }

        public void setInvoice_no(String invoice_no) {
            this.invoice_no = invoice_no;
        }

        public String getLog_time() {
            return log_time;
        }

        public void setLog_time(String log_time) {
            this.log_time = log_time;
        }
    }
}
