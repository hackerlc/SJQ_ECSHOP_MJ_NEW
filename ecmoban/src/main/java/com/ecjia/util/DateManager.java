package com.ecjia.util;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/3/6 17:11.
 */

public class DateManager {

    public static String formatDateTime(long mss) {
        StringBuffer dateTimes = new StringBuffer("距离结束还有");
        if(mss<=0){
            dateTimes =new StringBuffer("活动已开始");
            return dateTimes.toString();
        }
        long days = mss / ( 60 * 60 * 24);
        long hours = (mss % ( 60 * 60 * 24)) / (60 * 60);
        long minutes = (mss % ( 60 * 60)) /60;
        long seconds = mss % 60;
        if(days>0){
            dateTimes.append(days).append("天")
                    .append(hours).append("时")
                    .append(minutes).append("分")
                    .append(seconds).append("秒");
        }else if(hours>0){
            dateTimes.append(hours).append("时")
                    .append(minutes).append("分")
                    .append(seconds).append("秒");
        }else if(minutes>0){
            dateTimes.append(minutes).append("分")
                    .append(seconds).append("秒");
        }else{
            dateTimes.append(seconds).append("秒");
        }

        return dateTimes.toString();
    }

    private static final String TIME_LINE=":";
    public static String formatDateTime4Snatch(long mss) {
        StringBuffer dateTimes = new StringBuffer();
        long hours = mss / (60 * 60);
        long minutes = (mss % ( 60 * 60)) /60;
        long seconds = mss % 60;
        if(hours>0){
            dateTimes.append(hours).append(TIME_LINE)
                    .append(minutes).append(TIME_LINE)
                    .append(seconds);
        }else if(minutes>0){
            dateTimes.append(minutes).append(TIME_LINE)
                    .append(seconds);
        }else{
            dateTimes.append(seconds);
        }

        return dateTimes.toString();
    }
}
