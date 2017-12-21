package com.ecjia.consts;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.HashMap;

public class AppConst {

    //缓存类型-----------------------------------------------------------
    public static final int BALL_COLOR_RED = 0;
    public static final int BALL_COLOR_BLUE = 0;
    public static final Boolean DEBUG = true;
    /**
     * 下载图片保存目录
     */
    public static final String PIC_DIR_PATH = "/ECJia/pic";
    /**
     * 允许发送的最大字数
     */
    public static final int MAX_CONTENT_LEN = 140;

    public static final int RESULT_PHOTO_PREVIEW = 2;

    //    /** 程序运行期间产生的文件，缓存根目录 */
    //    public static final String ROOT_DIR_PATH = "/ECJia/cache";
    //    /** 缓存文件保存的根目录 */
    //    public static final String CACHE_DIR_PATH = ROOT_DIR_PATH + "/file";
    //    public static final String LOG_DIR_PATH = ROOT_DIR_PATH + "log";

    public static final int REQUEST_CODE_CAMERO = 731;//相机
    public static final int REQUEST_CODE_PHOTOS = 732;//相册


    //errorcode类-----------------------------------------------------------
    public final static int ResponseSucceed = 1;//请求成功
    public final static int InvalidUsernameOrPassword = 6;//用户名或密码错误
    public final static int ProcessFailed = 8;//处理失败
    public final static int UserOrEmailExist = 11; //用户名或email已使用
    public final static int UnexistInformation = 13;//不存在的信息
    public final static int BuyFailed = 14;//购买失败

    public final static int InvalidSession = 100;//session 不合理
    public final static int InvalidParameter = 101;//错误的参数提交

    public final static int InvalidPagination = 501;//没有pagination结构
    public final static int InvalidCode = 502;//code错误
    public final static int CodeExpire = 503;//合同期终止

    public final static int SelectedDeliverMethod = 10001;//您必须选定一个配送方式
    public final static int NoGoodInCart = 10002;//购物车中没有商品
    public final static int InsufficientBalance = 10003;//您的余额不足以支付整个订单，请选择其他支付方式
    public final static int InsufficientNumberOfPackage = 10005;//您选择的超值礼包数量已经超出库存。请您减少购买量或联系商家
    public final static int CashOnDeliveryDisable = 10006;//如果是团购，且保证金大于0，不能使用货到付款
    public final static int AlreadyCollected = 10007;//您已收藏过此商品
    public final static int InventoryShortage = 10008;//库存不足
    public final static int NoShippingInformation = 10009;//订单无发货信息

    //字符串替换标识
    public static final String REPLACE = "#replace#";

    //------------关于加载图片--------------
    //类型
    public static final int NORMALIMAGE = 9000;
    public static final int RECTIMAGE = 9001;
    public static final int SHOPINFOIMAGE = 9002;
    public static final int USERAVATER = 9003;

    //支付种类
    public static final String TENPAY = "tenpay";//财付通支付
    public static final String ALIPAY_WAP = "pay_alipay";//网页支付宝支付
    public static final String ALIPAY_MOBILE = "pay_alipay";//手机支付宝支付
    public static final String ALIPAY = "pay_alipay";//pc端网页支付
    public static final String WXPAY = "pay_wxpay";//微信支付
    public static final String UPPAY = "pay_upmp";//银联支付
    public static final String BANK = "pay_bank";//银行转账
    public static final String BALANCE = "pay_balance";//余额支付

    //第三方登录方式
    public static final String THIRD_WX = "sns_wx";//weixin
    public static final String THIRD_QQ = "sns_qq";//qq

    public static final String SPF_SHOPCONFIG = "spd_shopconfig";

    //一些常量
    public static final String PROMOTE_GOODS = "PROMOTE_GOODS";//促销商品详情

    public static final String GROUPBUY_GOODS = "GROUPBUY_GOODS";//团批商品详情
    public static final String MOBILEBUY_GOODS = "MOBILEBUY_GOODS";//手机专享商品详情
    public static final String GENERAL_GOODS = "GENERAL_GOODS";//普通商品类型
    public static final String GRAB_GOODS = "GRAB_GOODS";//为抢批商品类型

    public static final String COD = "pay_cod";//货到付款
    public static boolean iscod = false;

    public static ArrayList<HashMap<String, String>> datalist;//存放收藏商品的集合
    public static ArrayList<HashMap<String, String>> paymentlist;//存放支付方式的集合
    public static ArrayList<HashMap<String, String>> shippinglist;//存放配送方式的集合
    public static ArrayList<HashMap<String, Boolean>> brandlist;// 品牌状态集合
    public static ArrayList<HashMap<String, Boolean>> categorylist;// 分类集合
    public static ArrayList<HashMap<String, Boolean>> pricelist;// 价格集合
    public static ArrayList<HashMap<String, String>> shopcarlist;// 购物车集合


    //首页监听-------------------------------------------------------

    private static RegisterApp registerApp;

    public static void registerApp(RegisterApp register) {
        registerApp = register;
    }

    public static interface RegisterApp {
        public void onRegisterResponse(boolean success);
    }

    // 获取SDK的版本
    public static Integer getSDKVERSION() {
        return Integer.valueOf(android.os.Build.VERSION.SDK_INT);
    }


    public static double[] LNG_LAT = new double[]{0, 0};
    public static String[] ADDRESS = new String[]{"", "", ""};

    // 获取Resources
    public static Resources getResources(Context context) {
        return context.getResources();
    }

}
