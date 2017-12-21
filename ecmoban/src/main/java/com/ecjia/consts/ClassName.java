package com.ecjia.consts;

/**
 * Created by Adam on 2016-06-14.
 */
public class ClassName {

    static final String ACTIVITY_PATH = "com.ecjia.hamster.activity.";

    static final String PACKGE_PATH = AndroidManager.PACKNAME + ".";

    private static final String ACTIVITY_NAME_MOBILEBUY = ACTIVITY_PATH + "MobilebuyGoodsActivity";//手机专享
    private static final String ACTIVITY_NAME_THEME = ACTIVITY_PATH + "TopicListActivity";//专题列表
    private static final String ACTIVITY_NAME_PROMOTIONAL = ACTIVITY_PATH + "PromotionalGoodsActivity";//促销
    private static final String ACTIVITY_NAME_NEWGOODS = ACTIVITY_PATH + "PromotionalGoodsActivity";//新品
    private static final String ACTIVITY_NAME_GROUPBUY = ACTIVITY_PATH + "GroupbuyGoodsActivity";//团购
    private static final String ACTIVITY_NAME_ORDERS = ACTIVITY_PATH + "OrderListActivity";//订单
    private static final String ACTIVITY_NAME_ADDRESS = ACTIVITY_PATH + "AddressManageActivity";//收货地址
    private static final String ACTIVITY_NAME_COLLECT_GOODS = ACTIVITY_PATH + "CollectActivity";//关注商品
    private static final String ACTIVITY_NAME_COLLECT_SHOP = ACTIVITY_PATH + "ShopCollectActivity";//关注店铺
    private static final String ACTIVITY_NAME_HISTORY = ACTIVITY_PATH + "LastBrowseActivity";//浏览记录
    private static final String ACTIVITY_NAME_QRCODE = ACTIVITY_PATH + "MyCaptureActivity";//扫码
    private static final String ACTIVITY_NAME_MAP = ACTIVITY_PATH + "MapActivity";//地图
    private static final String ACTIVITY_NAME_TODAYHOT = ACTIVITY_PATH + "FindHotNewsActivity";//今日热点
    private static final String ACTIVITY_NAME_MESSAGE = PACKGE_PATH + "PushActivity";//消息
    private static final String ACTIVITY_NAME_FEEDBACK = ACTIVITY_PATH + "ConsultActivity";//咨询
    private static final String ACTIVITY_NAME_HELP = ACTIVITY_PATH + "HelpListActivity";//帮助中心
    private static final String ACTIVITY_NAME_STREETS = ACTIVITY_PATH + "ShopListFragmentActivity";//店铺街
    private static final String ACTIVITY_NAME_QRSHARE = ACTIVITY_PATH + "ShareQRCodeActivity";//推广

    public enum ActivityName {

        MOBILEBUY(ACTIVITY_NAME_MOBILEBUY),//手机专项

        NEWGOODS(ACTIVITY_NAME_NEWGOODS),

        QRSHARE(ACTIVITY_NAME_QRSHARE),

        STREETS(ACTIVITY_NAME_STREETS),

        THEME(ACTIVITY_NAME_THEME),

        PROMOTIONAL(ACTIVITY_NAME_PROMOTIONAL),//促销

        GROUPBUY(ACTIVITY_NAME_GROUPBUY),

        ORDERS(ACTIVITY_NAME_ORDERS),

        ADDRESS(ACTIVITY_NAME_ADDRESS),

        COLLECT_GOODS(ACTIVITY_NAME_COLLECT_GOODS),

        COLLECT_SHOP(ACTIVITY_NAME_COLLECT_SHOP),

        HISTORY(ACTIVITY_NAME_HISTORY),//浏览记录

        QRCODE(ACTIVITY_NAME_QRCODE),//扫码

        MAP(ACTIVITY_NAME_MAP),//地图

        TODAYHOT(ACTIVITY_NAME_TODAYHOT),//主题街

        MESSAGE(ACTIVITY_NAME_MESSAGE),//消息

        FEEDBACK(ACTIVITY_NAME_FEEDBACK),//咨询

        HELP(ACTIVITY_NAME_HELP);//帮助

        private String activityName;

        ActivityName(String p) {
            activityName = p;
        }

        public String getActivityName() {
            return activityName;
        }

    }

}