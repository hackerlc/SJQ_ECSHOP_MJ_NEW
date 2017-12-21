package com.ecjia.util.common;

import android.content.Context;
import android.os.Build;
import android.webkit.WebSettings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类名介绍:公共校验类
 * Created by sun
 * Created time 2017-04-27.
 */

public class Verification {

    /**
     * 密码校验
     * @param pasd 密码输入
     */
    public static boolean isPsd(String pasd) {
        String reg = "^[A-Za-z0-9*#@.&_]+$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(pasd);
        return m.matches();
    }
    /**
     * 手机号校验
     * @param mobiles 手机号
     */
    public static boolean isMobileNO(String mobiles) {
//        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Pattern p = Pattern.compile("(1)\\d{10}$");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();
    }

    public static String getUserAgent(Context context) {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(context);
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
