package com.ecjia.util;

import android.content.Context;
import android.text.TextUtils;

import com.ecmoban.android.sijiqing.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.lang.Math;

/**
 * User: howie
 * Date: 13-5-11
 * Time: 下午4:09
 */
public class TimeUtil {

    public static String timeAgo(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }


        long timeStamp = date.getTime();

        Date currentTime = new Date();
        long currentTimeStamp = currentTime.getTime();
        long seconds = (currentTimeStamp - timeStamp) / 1000;

        long minutes = Math.abs(seconds / 60);
        long hours = Math.abs(minutes / 60);
        long days = Math.abs(hours / 24);


        if (seconds <= 15) {
            return "刚刚";
        } else if (seconds < 60) {
            return seconds + "秒前";
        } else if (seconds < 120) {
            return "1分钟前";
        } else if (minutes < 60) {
            return minutes + "分钟前";
        } else if (minutes < 120) {
            return "一小时前";
        } else if (hours < 24) {
            return hours + "小时前";
        } else if (hours < 24 * 2) {
            return "一天前";
        } else if (days < 30) {
            return days + "天前";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
            String dateString = formatter.format(date);
            return dateString;
        }

    }

    public static String timeLeft(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }


        long timeStamp = date.getTime();

        Date currentTime = new Date();
        long currentTimeStamp = currentTime.getTime();

        long total_seconds = (timeStamp - currentTimeStamp) / 1000;

        if (total_seconds <= 0) {
            return "";
        }

        long days = Math.abs(total_seconds / (24 * 60 * 60));

        long hours = Math.abs((total_seconds - days * 24 * 60 * 60) / (60 * 60));
        long minutes = Math.abs((total_seconds - days * 24 * 60 * 60 - hours * 60 * 60) / 60);
        long seconds = Math.abs((total_seconds - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60));
        String leftTime;
        if (days > 0) {
            leftTime = days + "天" + hours + "小时" + minutes + "分" + seconds + "秒";
        } else if (hours > 0) {
            leftTime = hours + "小时" + minutes + "分" + seconds + "秒";
        } else if (minutes > 0) {
            leftTime = minutes + "分" + seconds + "秒";
        } else if (seconds > 0) {
            leftTime = seconds + "秒";
        } else {
            leftTime = "0秒";
        }

        return leftTime;
    }

    public static int timePromote(String timeStr,String timeEnd) {

        Date dateS = null;
        Date dateE = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            dateS = format.parse(timeStr);
            dateE = format.parse(timeEnd);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }


        long start = dateS.getTime();
        long end = dateE.getTime();

        Date currentTime = new Date();
        long currentTimeStamp = currentTime.getTime();

        if(currentTimeStamp<start){
            return 1;
        }else if(currentTimeStamp>start&&currentTimeStamp<end){
            return 2;
        }else{
            return 0;
        }

    }

    public static String timeLeft1(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }


        long timeStamp = date.getTime();

        Date currentTime = new Date();
        long currentTimeStamp = currentTime.getTime();

        long total_seconds = (timeStamp - currentTimeStamp) / 1000;

        if (total_seconds <= 0) {
            return "";
        }

        long days = Math.abs(total_seconds / (24 * 60 * 60));

        long hours = Math.abs((total_seconds - days * 24 * 60 * 60) / (60 * 60));
        long minutes = Math.abs((total_seconds - days * 24 * 60 * 60 - hours * 60 * 60) / 60);
        long seconds = Math.abs((total_seconds - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60));
        String leftTime;
        if (days > 0) {
            leftTime = days + "天" + hours + "小时" + minutes + "分" + seconds + "秒";
        } else if (hours > 0) {
            leftTime = hours + "小时" + minutes + "分" + seconds + "秒";
        } else if (minutes > 0) {
            leftTime = minutes + "分" + seconds + "秒";
        } else if (seconds > 0) {
            leftTime = seconds + "秒";
        } else {
            leftTime = "0秒";
        }

        return leftTime;
    }

    /**
     * 日期统一格式
     */
    private final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 日期统一格式
     */
    private final static SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");


    /**
     * 获取下一秒的时间
     *
     * @param currentDate
     * @return
     */
    public static String getDateAddOneSecond(String currentDate) {

        String nextSecondDate = "";

        if (currentDate != null && !currentDate.equals("")) {

            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = format.parse(currentDate); // 将当前时间格式化
                // System.out.println("front:" + format.format(date)); //
                // 显示输入的日期
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.SECOND, 1); // 当前时间加1秒
                date = cal.getTime();
                // System.out.println("after:" + format.format(date));
                nextSecondDate = format.format(date); // 加一秒后的时间
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return nextSecondDate;
    }



    /**
     * 获取剩余时间 几天几时几分几秒
     *
     * @param startTime
     * @return
     */
    public static long getRemainTime(String startTime) {

        String remainTime = "0"; // 剩余时间

        long dayMsec = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long hourMsec = 1000 * 60 * 60;// 一小时的毫秒数
        long minuteMsec = 1000 * 60;// 一分钟的毫秒数
        long secondMsec = 1000;// 一秒钟的毫秒数
        long diffMsec; // 毫秒差

        if (startTime != null && !startTime.equals("") ) {
            try {
                Date currentTime = new Date();
                long currentTimeStamp = currentTime.getTime();
                // 获得两个时间的毫秒时间差异
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                diffMsec =  format.parse(startTime).getTime()-currentTimeStamp;
                if (diffMsec > 0) {
                    return diffMsec;
                }
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return 0;
    }


    /**
     * 获取剩余时间 几天几时几分几秒
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getRemainTime(String startTime, String endTime) {

        String remainTime = "0"; // 剩余时间

        long dayMsec = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long hourMsec = 1000 * 60 * 60;// 一小时的毫秒数
        long minuteMsec = 1000 * 60;// 一分钟的毫秒数
        long secondMsec = 1000;// 一秒钟的毫秒数
        long diffMsec; // 毫秒差

        if (startTime != null && !startTime.equals("") && endTime != null && !endTime.equals("")) {
            try {
                // 获得两个时间的毫秒时间差异
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                diffMsec = format.parse(endTime).getTime() - format.parse(startTime).getTime();
                if (diffMsec > 0) {
                    /*判断结束时间是否大于开始时间*/
                    long diffDay = diffMsec / dayMsec;// 计算差多少天
                    long diffHour = diffMsec % dayMsec / hourMsec;// 计算差多少小时
                    long diffMin = diffMsec % dayMsec % hourMsec / minuteMsec;// 计算差多少分钟
                    long diffSec = diffMsec % dayMsec % dayMsec % minuteMsec / secondMsec;// 计算差多少秒//输出结果
                    remainTime = "剩余" + diffDay + "天" + diffHour + "时" + diffMin + "分" + diffSec + "秒";
                }

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return remainTime;
    }

    /**
     * 格式化日期格式
     *
     * @param dateTimeString
     * @return
     */
    public static String formatDateType(String dateTimeString) {

        String formatAfterDateTimeString = "";
        // System.out.println(dateTimeString);

        if (dateTimeString != null && !dateTimeString.equals("")) {
			/* 判断字符串是否有值 */
            formatAfterDateTimeString = dateTimeString;

            if (formatAfterDateTimeString.contains("/")) {
				/* 判断日期中是否包含'/' */
                formatAfterDateTimeString = formatAfterDateTimeString.replace("/", "-");
            }

            if ((formatAfterDateTimeString.lastIndexOf("-") - formatAfterDateTimeString.indexOf("-")) == 2) {
				/* 判断月份格式是否是MM格式 */
                String frontSubString = formatAfterDateTimeString.substring(0, formatAfterDateTimeString.indexOf("-") + 1);
                String afterSubString = "0" + formatAfterDateTimeString.substring(formatAfterDateTimeString.indexOf("-") + 1, formatAfterDateTimeString.length());

                formatAfterDateTimeString = frontSubString + afterSubString; //拼接字符串
            }
        }
        return formatAfterDateTimeString;
    }

    /**
     * 获取下一秒的时间
     *
     * @param currentDate
     * @return
     */
    public static String getDateAddOneDay(String currentDate) {

        String nextSecondDate = "";

        if (currentDate != null && !currentDate.equals("")) {

            try {
                Date date = formatDate.parse(currentDate); // 将当前时间格式化
                // System.out.println("front:" + format.format(date)); //
                // 显示输入的日期
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DAY_OF_YEAR, 1); // 当前时间加1天
                date = cal.getTime();
                // System.out.println("after:" + format.format(date));
                nextSecondDate = formatDate.format(date); // 加一秒后的时间
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return nextSecondDate;
    }

    //获取格式化的显示时间
    public static String getFomartDate(Calendar gettime,Context context){

        if(gettime.get(Calendar.MONTH)+1<10){

        }
        Calendar now=Calendar.getInstance();
        if(now.get(Calendar.YEAR)!=gettime.get(Calendar.YEAR)){
            return gettime.get(Calendar.YEAR)+"-"+fomarShowTime(gettime.get(Calendar.MONTH)+1)+"-"+fomarShowTime(gettime.get(Calendar.DATE));
        }else{
            if(now.get(Calendar.DAY_OF_YEAR)==gettime.get(Calendar.DAY_OF_YEAR)){
                return context.getResources().getString(R.string.today)+fomarShowTime(gettime.get(Calendar.HOUR_OF_DAY))+":"+fomarShowTime(gettime.get(Calendar.MINUTE));
            }else if(now.get(Calendar.DAY_OF_YEAR)-gettime.get(Calendar.DAY_OF_YEAR)==1){
                return context.getResources().getString(R.string.yesterday)+fomarShowTime(gettime.get(Calendar.HOUR_OF_DAY))+":"+fomarShowTime(gettime.get(Calendar.MINUTE));
            }else{
                return fomarShowTime(gettime.get(Calendar.MONTH)+1)+"-"+fomarShowTime(gettime.get(Calendar.DATE));
            }
        }
    }
    private static String fomarShowTime (int time){
        if(time<10){
            return "0"+time;
        }
        return  ""+time;
    }

    public static String getFomartMonth(Calendar gettime){
        Calendar now=Calendar.getInstance();
        if(now.get(Calendar.YEAR)==now.get(Calendar.YEAR)&&now.get(Calendar.MONTH)==gettime.get(Calendar.MONTH)){
            return "本月";
        }else{
            return (gettime.get(Calendar.MONTH)+1)+"月";
        }
    }

    //判断是否本周
    public static boolean getFomartWeek(Calendar gettime){
        Calendar now=Calendar.getInstance();
        if(now.get(Calendar.WEEK_OF_YEAR)==gettime.get(Calendar.WEEK_OF_YEAR)){
            return true;
        }else{
            return false;
        }
    }

    //判断今天还是昨天
    public static boolean getFomartDay(Calendar gettime){
        Calendar now=Calendar.getInstance();
        if(now.get(Calendar.DAY_OF_YEAR)==(gettime.get(Calendar.DAY_OF_YEAR))){
            return true;
        }else {
            return false;
        }
    }
    public static boolean getFomartYesterday(Calendar gettime){
        Calendar now=Calendar.getInstance();
        if(now.get(Calendar.DAY_OF_YEAR)==(gettime.get(Calendar.DAY_OF_YEAR)+1)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * User: leming
     * Date: 2015-09-18
     * 今日热点时间转化
     */
    public static String getFomartNewsTopTime(String time) {
        Date date=new Date();
        try {
            date=format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, 0);
        date = cal.getTime();

        SimpleDateFormat formatNowAll = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat formatNowDay = new SimpleDateFormat("MM月dd日");
        SimpleDateFormat formatNowTime = new SimpleDateFormat("HH:mm");

        if(getFomartWeek(cal)){
            if(getFomartDay(cal)){
                return formatNowTime.format(date);
            }else if (getFomartYesterday(cal)){
                return "昨天 "+formatNowTime.format(date);
            }else{
                return getWeekDay(cal)+" "+formatNowTime.format(date);
            }

        }else{
            return formatNowAll.format(date)+" "+getAMPM(cal)+formatNowTime.format(date);
        }

    }

    public static String getFormatNowDay(String time){
        Date date=new Date();
        try {
            date=format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatNowDay = new SimpleDateFormat("MM月dd日");
        return formatNowDay.format(date);
    }
    public static String getAMPM(Calendar gettime){
        int ampm=gettime.get(Calendar.AM_PM);
        int hour=gettime.get(Calendar.HOUR_OF_DAY);
        LG.e("====hour====="+hour);
        if(ampm==Calendar.AM){
            return "早上";
        }else{
            if(hour>=18){
                return "晚上";
            }else{
                return "下午";
            }
        }
    }

    public static String getWeekDay(Calendar gettime){
        int dayOfWeek = gettime.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek){
            case 1:
                return "周日";
            case 2:
                return "周一";
            case 3:
                return "周二";
            case 4:
                return "周三";
            case 5:
                return "周四";
            case 6:
                return "周五";
            case 7:
                return "周六";
            default:
                return "";
        }
    }

    /**
     * 获取剩余时间 几天几时几分几秒
     *
     * @param startTime
     * @param type 1:时  2:分  3:秒
     * @return
     */
    public static String getRemainTime(String startTime,int type) {

        String remainTime = "0"; // 剩余时间

        long dayMsec = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long hourMsec = 1000 * 60 * 60;// 一小时的毫秒数
        long minuteMsec = 1000 * 60;// 一分钟的毫秒数
        long secondMsec = 1000;// 一秒钟的毫秒数
        long diffMsec; // 毫秒差

        if (startTime != null && !startTime.equals("") ) {
            try {
                Date currentTime = new Date();
                long currentTimeStamp = currentTime.getTime();
                // 获得两个时间的毫秒时间差异
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z");
                diffMsec =  format.parse(startTime).getTime()-currentTimeStamp;
                if (diffMsec > 0) {
                    /*判断结束时间是否大于开始时间*/
                    long diffDay = diffMsec / dayMsec;// 计算差多少天
                    long diffHour = diffMsec % dayMsec / hourMsec;// 计算差多少小时
                    long diffMin = diffMsec % dayMsec % hourMsec / minuteMsec;// 计算差多少分钟
                    long diffSec = diffMsec % dayMsec % dayMsec % minuteMsec / secondMsec;// 计算差多少秒//输出结果

                    String finDay=""+diffDay;
                    String finHour=diffHour<10?"0"+diffHour:""+diffHour;
                    String finMin=diffMin<10?"0"+diffMin:""+diffMin;
                    String finSec=diffSec<10?"0"+diffSec:""+diffSec;

                    switch (type){
                        case 0:
                            remainTime =  finDay;
                            break;
                        case 1:
                            remainTime =  finHour;
                            break;
                        case 2:
                            remainTime =  finMin;
                            break;
                        case 3:
                            remainTime =  finSec;
                            break;
                        default:
                            remainTime="00";
                    }
                }
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return remainTime;
    }
    public static String getRemainTime(String startTime,String endTime,int type) {

        String remainTime = "0"; // 剩余时间

        long dayMsec = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long hourMsec = 1000 * 60 * 60;// 一小时的毫秒数
        long minuteMsec = 1000 * 60;// 一分钟的毫秒数
        long secondMsec = 1000;// 一秒钟的毫秒数
        long diffMsec; // 毫秒差

        if (startTime != null && !startTime.equals("") && endTime != null && !endTime.equals("")) {
            try {
                // 获得两个时间的毫秒时间差异
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                diffMsec = format.parse(endTime).getTime() - format.parse(startTime).getTime();
                if (diffMsec > 0) {
                    /*判断结束时间是否大于开始时间*/
                    long diffDay = diffMsec / dayMsec;// 计算差多少天
                    long diffHour = diffMsec % dayMsec / hourMsec;// 计算差多少小时
                    long diffMin = diffMsec % dayMsec % hourMsec / minuteMsec;// 计算差多少分钟
                    long diffSec = diffMsec % dayMsec % dayMsec % minuteMsec / secondMsec;// 计算差多少秒//输出结果

                    String finDay=""+diffDay;
                    String finHour=diffHour<10?"0"+diffHour:""+diffHour;
                    String finMin=diffMin<10?"0"+diffMin:""+diffMin;
                    String finSec=diffSec<10?"0"+diffSec:""+diffSec;

                    switch (type){
                        case 0:
                            remainTime =  finDay;
                            break;
                        case 1:
                            remainTime =  finHour;
                            break;
                        case 2:
                            remainTime =  finMin;
                            break;
                        case 3:
                            remainTime =  finSec;
                            break;
                        default:
                            remainTime="00";
                    }
                }
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return remainTime;
    }

    //比较两个时间先后 0一样  1前一个时间大  -1后一个时间大
    public static int compareTime(String oneTime,String twoTime) {

        if (!TextUtils.isEmpty(oneTime) && !TextUtils.isEmpty(twoTime) ) {
            try {
                // 获得两个时间的毫秒时间差异
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                long time = format.parse(oneTime).getTime() - format.parse(twoTime).getTime();
                if (time>0){
                    return 1;
                }else if(time==0){
                    return 0;
                }else {
                    return -1;
                }
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return 0;
            }
        }else{
            return 0;
        }

    }


}
