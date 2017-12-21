package com.ecjia.consts;


import android.os.Environment;


public class ProtocolConst {

    public static final String GOODSDETAIL = "goods/detail";                        // 商品详情
    public static final String HOMEDATA = "home/data";                    // 主页数据
    public static final String CATEGORYGOODS = "home/category";            // 主页数据
    public static final String SEARCH = "goods/list";                            // 搜索及商品列表页面与之前的serach接口功能相同

    public static final String SHOPHELP = "shop/help";                        // 帮助列表

    public static final String GOODSDESC = "goods/desc";                    // 商品详情，商品描述

    public static final String SIGNIN = "user/signin";                    // 登录

    public static final String SIGNUPFIELDS = "user/signupFields";        // 注册字段

    public static final String SIGNUP = "user/signup";                    // 注册

    public static final String SEARCHKEYWORDS = "searchKeywords";            // 获取搜索推荐关键字

    public static final String CARTLIST = "cart/list";                    // 购物车列表

    public static final String USERINFO = "user/info";                    // 获取用户中心相关信息

    public static final String COLLECT_LIST = "user/collect/list";        // 收藏列表

    public static final String COLLECT_DELETE = "user/collect/delete";    // 收藏删除

    public static final String CARTCREATE = "cart/create";                // 添加到购物车

    public static final String CARTDELETE = "cart/delete";                // 从购物车删除一件商品

    public static final String CARTUPDATA = "cart/update";                // 更新购物车

    public static final String ADDRESS_LIST = "address/list";                // 获取用户地址列表

    public static final String ADDRESS_ADD = "address/add";                // 添加用户地址

    public static final String REGION = "shop/region";                            // 获取地区城市

    public static final String CHECKORDER = "flow/checkOrder";            // 提交订单前的订单预览

    public static final String ADDRESS_INFO = "address/info";                // 读取地址详情

    public static final String ADDRESS_DEFAULT = "address/setDefault";    // 设置该地址为默认

    public static final String ADDRESS_DELETE = "address/delete";            // 删除一个地址

    public static final String ADDRESS_UPDATE = "address/update";            // 更新地址

    public static final String COLLECTION_CREATE = "user/collect/create";    // 收藏

    public static final String COLLECTION_DELETE = "user/collect/delete";    // 取消收藏

    public static final String FLOW_DOWN = "flow/done";                    // 订单生成

    public static final String ORDER_LIST = "order/list";                    // 订单列表

    public static final String ORDER_RETURN_LIST = "order/return_list";      // 退换货订单列表

    public static final String ORDER_UPDATE = "order/update";             //更改订单支付方式

    public static final String VALIDATE_INTEGRAL = "validate/integral";    // 验证积分

    public static final String VALIDATE_BONUS = "validate/bonus";            // 验证红包

    public static final String ORDER_PAY = "order/pay";                    // 在线支付

    public static final String ORDER_DETAIL = "order/detail";             // 获得Logid

    public static final String ORDER_CANCLE = "order/cancel";                // 取消订单

    public static final String AFFIRMRECEIVED = "order/affirmReceived";    // 确认收货

    public static final String ARTICLE = "article";                        // 获取文章内容

    public static final String COMMENTS = "comments";                        // 获取评论列表

    public static final String CONFIG = "shop/config";                            // 获取配置

    public static final String CATEGORY = "goods/category";                     // 获取所有分类

    public static final String BRAND = "goods/brand";                           // 获取所有品牌

    public static final String PRICE_RANGE = "goods/price_range";               // 根据分类获取价格区间

    public static final String SHOP_PAYMENT = "shop/payment";              //支付方式列表

    public static final String CHANGE_PASSWORD = "user/password";          //修改密码

    public static final String USER_RECHARGE = "user/account/deposit";          //获取余额充值信息

    public static final String USER_ACCOUNT_PAY = "user/account/pay";          //充值付款

    public static final String USER_ACCOUNT_CANCLE = "user/account/cancel";          //订单或充值取消

    public static final String USER_RAPLY = "user/account/raply";               //提现申请

    public static final String USER_ACCOUNT_RECORD = "user/account/record";       //充值提现记录

    public static final String SELLER_LIST = "seller/list";                    //商家列表

    public static final String SELLER_CATEGORY = "seller/category";                    //店铺分类

    public static final String SHOPDETIAL = "merchant/goods/list";                    //商家详情

    public static final String SHOPFILTER = "merchant/goods/category";               //商家筛选

    public static final String SELLER_COLLECTCREATE = "seller/collect/create";   //店铺收藏

    public static final String SELLER_COLLECTDELETE = "seller/collect/delete";   //店铺取消收藏

    public static final String MERCHAT_HOMEDATA = "merchant/home/data";   //店铺首页数据及基本信息

    public static final String SELLER_HOMEDATA = "seller/home/data";   //店铺列表广告图

    public static final String GOODS_GROUPBUY = "goods/groupbuygoods";  //团购商品

    public static final String GOODS_MOBILEBUY = "goods/mobilebuygoods";  //手机专享商品

    public static final String SELLER_COLLECT = "seller/collect/list";  //店铺收藏列表

    public static final String SEARCH_NEW = "goods/search";  //新的搜索页（店铺/商品）

    public static final String SEARCH_SELLER = "seller/search";  //搜索店铺

    public static final String USER_SNSBIND = "user/snsbind";   //第三方登录

    public static final String GETCODE = "user/userbind";    //手机快速注册入口

    public static final String GETNEWCODE = "connect/code";    //手机快速注册入口

    public static final String CHECKCODE = "validate/bind";   //手机验证码验证

    public static final String ORDER_EXPRESS = "order/express";    //物流查询


    //2.7.0
    public static final String UPDATE_USERINFO = "user/update";               //更新用户信息

    public static final String SETDEVICE_TOKEN = "device/setDeviceToken";

    public static final String HOME_DISCOVER = "home/discover";//自定义发现

    public static final String HOME_HOT_NEWS = "home/news";//今日热点

    public static final String COMMENTS_CREATE = "comment/create";           //评论商品

    public static final String CONSULTION_GET = "feedback/list";                  //商品咨询(获取留言反馈)

    public static final String CONSULTION_CREATE = "feedback/create";             //商品咨询(提交留言反馈)

    public static final String HOME_SUGGESTLIST = "goods/suggestlist";             //首页推荐商品
    //多商户1.4
    public static final String TOPIC_LIST = "topic/list";//主题活动列表

    public static final String TOPIC_INFO = "topic/info";//主题活动详情

    public static final String SHOP_TOKEN = "shop/token";//获取token

    public static final String USER_FORGET_PASSWORD = "user/forget_password";//找回密码请求

    public static final String VALIDATE_FORGET_PASSWORD = "validate/forget_password";//找回密码验证请求

    public static final String USER_RESET_PASSWORD = "user/reset_password";//重置密码

    public static final String ADPIC = "home/adsense";                            //广告图

    public static final String GOODS_SUGGESTLIST = "goods/suggestlist";             //首页推荐商品


    public static final String APP_UPGRADE_CHECK ="app/upgrade/check";//检测更新


    public static final String SIGNOUT = "user/signout";                  // 退出登录

    //1.7.0
    public static final String INVITE_USER ="invite/user";//获取用户邀请信息

    public static final String INVITE_RECORD ="invite/record";//获取我所推荐的记录

    public static final String INVITE_REWARD ="invite/reward";//获取我所推荐的统计

    public static final String INVITE_VALIDATE ="invite/validate";//验证注册会员用户是否受邀

    public static final String CONNECT_BIND ="connect/bind";  //关联登录绑定已有用户

    public static final String CONNECT_SIGNIN = "connect/signin";   //第三方登录

    public static final String CONNECT_SIGNUP ="connect/signup";  //关联注册

    public static final String SHOP_INFO ="shop/info";  //店家相关信息

    public static final String SHOP_INFO_DETAIL ="shop/info/detail";  //店家相关信息详情

    public static final String ORDER_REMINDER = "order/reminder";    // 提醒发货

    public static final String REDPAPER_RECEIVE = "receive/coupon";    // 领取红包


    //2.8.0
    public static final String BONUS_VALIDATE = "bonus/validate";//红包验证

    public static final String BONUS_BIND = "bonus/bind";//积分兑换红包
    
    public static final String SHOP_HELP_DETAIL = "shop/help/detail"; // 获取帮助内容详情

    //1.7.0
    public static final String ORDERS_COMMENT = "orders/comment"; // 获取评论晒单列表

    public static final String ORDERS_COMMENT_DETAIL = "orders/comment/detail"; // 获取单个订单的评论详情

    public static final String GOODS_COMMENT_LIST = "goods/comment/list"; // 获取单个商品的评论详情

    public static final String USER_BONUS = "user/bonus"; // 获取用户红包列表


}
