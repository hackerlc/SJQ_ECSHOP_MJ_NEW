package com.ecjia.consts;

import android.content.Context;

public class AndroidManager {

    public static final String AppName = "sijiqing";

    public static final String CLOUD_URL = "https://cloud.ecjia.com/sites/api/index.php?url=";

    /**
     * * 获得API地址
     */
    public static final String SERVICE_URL="http://app.sjq.cn/";//正式地址
//    public static final String SERVICE_URL="http://app.topws.cn/";//测试地址
    public static final String SERVICE_TAG="?url=";
    public static final String getURLAPI() {
        return SERVICE_URL+SERVICE_TAG;
//        return "http://test.b2b2c.ecjia.com/?url=";
    }

    public final static String OFFOCIALWEB = "www.sjq.cn";

    public static final String APP_UPDATE_KEY = "a83582ad47d43d1eebbd5bc09b15645b";//用于版本更新

    //缓存路径
//    public static final String PACKNAME = "com.sjq.cn.newmojie";
    public static final String PACKNAME = "cn.sjq.city";


    public final static String APPCACHEPATH = "sdcard/Android/data/" + PACKNAME + "/cache";//app缓存路径（广告图缓存位置在其中）.
    public static final String QRCODEFILE = "sdcard/android/data/" + PACKNAME + "/cache/qrcode/qrcode.jpg";//二维码
    public final static String PROFILE_PHOTO = "sdcard/android/data/" + PACKNAME + "/profile_photo";//头像存储路径
    public final static String LOGIN_BG = "sdcard/android/data/" + PACKNAME + "/login_bg";//登录背景路径
    public final static String ADPICSPATH = "sdcard/android/data/" + PACKNAME + "/cache/ad_image";//广告图路径
    public final static String PICTURE_CACHE = "/ecjia/b2b2c/DCIM/Camera";//晒单图路径


    //登录背景路径
    public final static String SHOPCONFIG = "sdcard/android/data/" + PACKNAME + "/shop_config";

    public static String getWebUrl() {
        return "";
    }


    public static int getSDKCode(Context context) {  //获取sdk的等级
        return android.os.Build.VERSION.SDK_INT;
    }


    //是否是调试模式
    public static boolean isDebug = true;

    //是否是调试TAG
    public static final String DebugTAG = "SIJIQING";

    /**
     * 支持引导图
     */
    public static final boolean SUPPORT_GALLERY = true;
    /**
     * 支付手机宝支付类型
     */
    public static final boolean SUPPORT_ALIPAY_MOBILE = true;
    /**
     * 支持手机微信支付
     */
    public static final boolean SUPPORT_WXPAY = true;
    /**
     * 支持银联本地支付
     */
    public static final boolean SUPPORT_UPMP = false;
    /**
     * 支持分享功能
     */
    public static final boolean SUPPORT_SHARE = true;
    /**
     * 支持第三方登录
     */
    public static final boolean SUPPORT_OUTER_LOGIN = true;
    /**
     * 支持手机号注册
     */
    public static final boolean SUPPORT_MOBILE_SININ = true;
    /**
     * 支持推送
     */
    public static boolean SUPPORT_PUSH = false;
    /**
     * debug模式推送
     */
    public static boolean PUSH_DEBUG = false;

    //银联测试环境  01表示测试环境	00为正式环境
    public static final String SERVERMODE = "00";//

    //新浪微博appID
    public final static String SINAAPPID = "";
    public final static String SINAAPPSECRET = "";

    //微信appID
    public static final String WXAPPID = "wx067733762d11d298";
    public static final String WXAPPSECRET = "66e339e0d5df7abcddc8f346d003f21d";

    //qq的appID
    public static final String QQAPPID = "1105642733";
    public static final String QQAPPSECRET = "IY9Iw6o0j4AjmsFv";

    //支付宝支付参数------------------------------------
    //支付宝本地支付成功标志
    public static final int LOCALPAY_SUCCESS = 9000;
    public static final String ALIPAY_SUCCESS = "9000";


}
