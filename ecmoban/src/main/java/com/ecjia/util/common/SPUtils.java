package com.ecjia.util.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.ecjia.consts.SharedPKeywords;
import com.ecjia.entity.responsemodel.USER;

/**
 * Created by ssd on 2016/9/28.
 */
public class SPUtils {
    public static final String FILE_NAME = "ECJIA";//文件名
    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;
    //SharePreferenceName
    public static final String SPUSER = "userInfo";
    //KEY//用户信息-start
    public static final String SPUSER_KUID = "uid";
    public static final String SPUSER_KSID = "sid";
    public static final String SPUSER_KSTOKEN = "shop_token";
    public static final String SPUSER_KLOCUID = "local_uid";
    public static final String SPUSER_KNAME = "uname";
    public static final String SPUSER_KLISTTYPE = "goodlist_type";
    public static final String LOCAL_NAME = "local_name";
    public static final String LOCAL_ALIAS = "local_alias";
    public static final String LEVEL = "level";
    public static final String EMAIL = "email";


    //客服电话
    private final static String SERVICE_MOBILE = "service_mobile";//String

    /**
     * 初始化-Application中初始化
     *
     * @param context
     */
    public static void init(Context context) {
        sp = context.getSharedPreferences(SPUSER, 0);
        editor = sp.edit();
    }

    public static void setUid(String value) {
        editor.putString(SPUSER_KUID, value).apply();
        String uidString =sp.getString(SharedPKeywords.SPUSER_KUID, "");
    }

    public static void setSid(String value) {
        editor.putString(SPUSER_KSID, value).apply();
    }

    public static void setLocalUid(String value) {
        editor.putString(SPUSER_KLOCUID, value).apply();
    }

    public static void setUname(String value) {
        editor.putString(SPUSER_KNAME, value).apply();
    }

    public static void setLevel(String value) {
        editor.putString(LEVEL, value).apply();
    }

    public static void setEmail(String value) {
        editor.putString(EMAIL, value).apply();
    }


    /**
     * 判断是否含有该key
     *
     * @param key 键
     * @return
     */
    public static boolean contains(String key) {
        return sp.contains(key);
    }

    public static boolean hasLogined() {
        return sp.getBoolean("", false);
    }


    /**
     * 移除 " 账号密码 " 和 " 用户信息 "
     */
    public static void loginOut() {
        //        editor.remove(ACCOUNT).remove(PWD);
//        editor.remove(isLogined);
//        for (String key : userDatas) {
//            editor.remove(key);
//        }
//        editor.apply();
    }


}
