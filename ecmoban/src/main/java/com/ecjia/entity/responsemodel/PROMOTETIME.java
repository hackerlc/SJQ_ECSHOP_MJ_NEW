package com.ecjia.entity.responsemodel;

/**
 * Created by Administrator on 2016/9/29.
 */
public class PROMOTETIME {
    private String day;
    private String hour;
    private String minute;
    private String second;

    public PROMOTETIME() {
    }

    public PROMOTETIME(String day, String hour, String minute, String second) {
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }
}
