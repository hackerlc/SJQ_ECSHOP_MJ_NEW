package com.ecjia.util;

import android.text.TextUtils;

import java.text.DecimalFormat;

public class FormatUtil {

    /**
     * 小数点后
     */
    public static float getFloatTwoDecimal(float data) {
        data = (float) ((Math.round(data) * 100) / 100);
        return data;
    }

    public static String formatFloatTwoDecimal(float data) {

        String distance = "";
        distance = String.format("%.2f", data);

        return distance;
    }

    public static float formatStrToFloat(String data) {

        if(data==null||TextUtils.isEmpty(data)){
            data="0";
        }else{
            data = data.replace("元/公斤", "").replace("yuan", "").replace("¥", "").replace("￥", "").replace("Yuan", "").replace("$", "").replace(" ", "").trim();
        }

        float result = 0f;

        try {
            result = Float.parseFloat(data);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String formatToPrice(String price) {
        String result = "";
        result = "¥" + price.replace("元/公斤", "").replace("yuan", "").replace("¥", "").replace("￥", "").replace("Yuan", "").replace(" ", "");
        return result;
    }

    public static String formatToDigetPrice(String price) {
        String result = "";
        result = price.replace("元/公斤", "").replace("yuan", "").replace("¥", "").replace("￥", "").replace("Yuan", "").replace(" ", "");
        return result;
    }

    public static String stringFormatDecimal(String string) throws Exception {
        float f = Float.valueOf(string);
        return decimalFormatDecimal(f);
    }

    public static String decimalFormatDecimal(float string) throws Exception {
        DecimalFormat df = new DecimalFormat("#######0.00");
        String dff = df.format(string);
        return dff;
    }


    /**
     * 返回带符号的价格
     *
     * @param price
     * @return
     */
    public static String formatToSymbolPrice(String price) throws Exception {
        if (formatStrToFloat(formatToSymbolPriceStr(price))==0) {
            return "免费";
        }
        String result = "¥" + stringFormatDecimal(formatToSymbolPriceStr(price));
        return result;
    }

    /**
     * 返回带符号的价格
     *
     * @param price
     * @return
     */
    public static String formatToSymbolPrice(float price) throws Exception {
        String result = "¥" + decimalFormatDecimal(price);
        return result;
    }

    public static int formatStrToInt(String data) {
        if(data==null||TextUtils.isEmpty(data)){
            data="0";
        }else{
            data = data.replace("元", "").replace("yuan", "").replace("¥", "").replace("￥", "").replace("Yuan", "").replace("$", "").replace(" ", "").trim();
        }

        int result = 0;

        try {
            result = Integer.parseInt(data);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String formatToSymbolPriceStr(String price) throws Exception{
        String result = "";
        result = price.replace("元", "").replace("yuan", "").replace("¥", "").replace("￥", "").replace("Yuan", "").replace("$", "").replace(" ", "").trim();
        return result;
    }
}
