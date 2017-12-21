package com.ecjia.entity.responsemodel;

import java.io.Serializable;

/**
 * 类名介绍：红包
 * Created by sun
 * Created time 2017-03-02.
 */

public class USERBONUS implements Serializable{

    private String bonus_amount;//红包金额
    private String formatted_bonus_amount;

    private String bonus_id;
    private String bonus_name;

    private String start_date;
    private String formatted_start_date;
    private String end_date;
    private String formatted_end_date;

    private String label_status;//可使用
    private String request_amount;//需消费总额超过
    private String formatted_request_amount;

    private String status ;//allow_use; //可使用；   expired已过期，   is_used已使用 //红包类型

    public String getBonus_amount() {
        return bonus_amount;
    }

    public void setBonus_amount(String bonus_amount) {
        this.bonus_amount = bonus_amount;
    }

    public String getFormatted_bonus_amount() {
        return formatted_bonus_amount;
    }

    public void setFormatted_bonus_amount(String formatted_bonus_amount) {
        this.formatted_bonus_amount = formatted_bonus_amount;
    }

    public String getBonus_id() {
        return bonus_id;
    }

    public void setBonus_id(String bonus_id) {
        this.bonus_id = bonus_id;
    }

    public String getBonus_name() {
        return bonus_name;
    }

    public void setBonus_name(String bonus_name) {
        this.bonus_name = bonus_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getFormatted_start_date() {
        return formatted_start_date;
    }

    public void setFormatted_start_date(String formatted_start_date) {
        this.formatted_start_date = formatted_start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getFormatted_end_date() {
        return formatted_end_date;
    }

    public void setFormatted_end_date(String formatted_end_date) {
        this.formatted_end_date = formatted_end_date;
    }

    public String getLabel_status() {
        return label_status;
    }

    public void setLabel_status(String label_status) {
        this.label_status = label_status;
    }

    public String getRequest_amount() {
        return request_amount;
    }

    public void setRequest_amount(String request_amount) {
        this.request_amount = request_amount;
    }

    public String getFormatted_request_amount() {
        return formatted_request_amount;
    }

    public void setFormatted_request_amount(String formatted_request_amount) {
        this.formatted_request_amount = formatted_request_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
