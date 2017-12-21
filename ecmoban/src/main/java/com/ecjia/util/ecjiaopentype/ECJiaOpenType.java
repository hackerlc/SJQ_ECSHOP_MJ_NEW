package com.ecjia.util.ecjiaopentype;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.ecjia.base.ECJiaApplication;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.EventKeywords;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.util.ECJiaBaseIntent;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.ecjia.view.ECJiaMainActivity;
import com.ecjia.view.activity.AccountActivity;
import com.ecjia.view.activity.AddressManageActivity;
import com.ecjia.view.activity.CollectActivity;
import com.ecjia.view.activity.ConsultActivity;
import com.ecjia.view.activity.FindHotNewsActivity;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.view.activity.GoodsListActivity;
import com.ecjia.view.activity.GroupbuyGoodsActivity;
import com.ecjia.view.activity.HelpListActivity;
import com.ecjia.view.activity.InvitationWinRewardActivity;
import com.ecjia.view.activity.LanguageActivity;
import com.ecjia.view.activity.LastBrowseActivity;
import com.ecjia.view.activity.MapActivity;
import com.ecjia.view.activity.MobilebuyGoodsActivity;
import com.ecjia.view.activity.MyCaptureActivity;
import com.ecjia.view.activity.MyPurseActivity;
import com.ecjia.view.activity.PromotionalGoodsActivity;
import com.ecjia.view.activity.SearchAllActivity;
import com.ecjia.view.activity.SettingActivity;
import com.ecjia.view.activity.ShareQRCodeActivity;
import com.ecjia.view.activity.ShopListActivity;
import com.ecjia.view.activity.ShoppingCartActivity;
import com.ecjia.view.activity.TopicDetailActivity;
import com.ecjia.view.activity.TopicListActivity;
import com.ecjia.view.activity.goodsorder.OrderListActivity;
import com.ecjia.view.activity.goodsorder.OrderListAllActivity;
import com.ecjia.view.activity.goodsorder.OrderdetailActivity;
import com.ecjia.view.activity.newspecialoffer.NewSpecialOfferActivity;
import com.ecjia.view.activity.newwholesale.NewWholesaleActivity;
import com.ecjia.view.activity.push.PushActivity;
import com.ecjia.view.activity.snatch.SnatchWholesaleActivity;
import com.ecjia.view.activity.substation.SubstationGoodsListActivity;
import com.ecjia.view.activity.together.TogetherWholesaleActivity;
import com.ecjia.view.activity.user.ChangePasswordActivity;
import com.ecjia.view.activity.user.CustomercenterActivity;
import com.ecjia.view.activity.user.LoginActivity;
import com.ecjia.view.activity.user.RegisterActivity;
import com.ecjia.view.activity.user.RegisterInputPhoneActivity;
import com.ecjia.view.activity.webwholesale.WebWholesaleActivity;
import com.ecmoban.android.sijiqing.R;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/9/15.
 */
public class ECJiaOpenType {

    private static ECJiaOpenType mOpenType;
    private static HashMap<String, Class> openType = new HashMap<>();
    private static HashMap<String, Class> needLoginMap = new HashMap<>();

    public static ECJiaOpenType getDefault() {
        if (mOpenType == null) {
            synchronized (ECJiaOpenType.class) {
                if (mOpenType == null) {
                    mOpenType = new ECJiaOpenType();
                }
            }
        }
        return mOpenType;
    }

    private ECJiaOpenType() {
        register();
    }


    public static final String ACTIVITY_PATH = "com.ecjia.hamster.activity.";
    public static final String PACKGE_PATH = AndroidManager.PACKNAME + ".";

    /**
     * 注册
     *
     * @return
     */
    public ECJiaOpenType register() {
        needLoginMap.clear();
        openType.put("main", ECJiaMainActivity.class);//主页
        openType.put("help", HelpListActivity.class);//帮助中心
        openType.put("history", LastBrowseActivity.class);//浏览记录
        openType.put("qrcode", MyCaptureActivity.class);//扫码
        openType.put("qrshare", ShareQRCodeActivity.class);//分享码
        openType.put("user_wallet", MyPurseActivity.class);//我的钱包
        openType.put("feedback", ConsultActivity.class);//咨询
        openType.put("map", MapActivity.class);//地图
        openType.put("message", PushActivity.class);//消息
        openType.put("setting", SettingActivity.class);//设置
        openType.put("language", LanguageActivity.class);//语言
        openType.put("webview", com.ecjia.view.activity.web.WebViewActivity.class);//浏览器
        openType.put("cart", ShoppingCartActivity.class);//购物车
        openType.put("goods_list", GoodsListActivity.class);//商品列表
        openType.put("goods_comment", GoodsDetailActivity.class);//评论列表
        openType.put("goods_detail", GoodsDetailActivity.class);//商品详情
        openType.put("orders_list", OrderListActivity.class);//订单列表
        openType.put("user_collect", CollectActivity.class);//收藏
        openType.put("order_detail", OrderdetailActivity.class);//订单详情
        openType.put("user_address", AddressManageActivity.class);//地址管理
        openType.put("user_account", AccountActivity.class);//账户余额
        openType.put("user_password", ChangePasswordActivity.class);//修改密码
        openType.put("user_center", CustomercenterActivity.class);//用户中心
        openType.put("mobilebuy", MobilebuyGoodsActivity.class);//手机专享
        openType.put("discover", ECJiaMainActivity.class);//发现
        openType.put("search", SearchAllActivity.class);//搜索
        openType.put("goods_suggest", PromotionalGoodsActivity.class);//suggestList
        openType.put("sign_up", RegisterActivity.class);//注册(默认)
        openType.put("topic", TopicDetailActivity.class);//主题详情
        openType.put("topic_list", TopicListActivity.class);//主题列表
        openType.put("get_integral", InvitationWinRewardActivity.class);//赚积分
        openType.put("merchant", ShopListActivity.class);//店铺详情
        openType.put("groupbuy", GroupbuyGoodsActivity.class);//团购列表
        openType.put("seller", ECJiaMainActivity.class);//首页店铺街
        openType.put("mobile_news", FindHotNewsActivity.class);//今日热点
        openType.put("together_wholesale", TogetherWholesaleActivity.class);//限时团批
        openType.put("snatch_wholesale", SnatchWholesaleActivity.class);//抢批
        openType.put("substation_goods", SubstationGoodsListActivity.class);//有好货
        openType.put("business_goods", WebWholesaleActivity.class);//商家促销
        openType.put("wholesale_shop", NewWholesaleActivity.class);//新批发
        openType.put("wholesale_goods", WebWholesaleActivity.class);//新网批
        openType.put("together_temai", NewSpecialOfferActivity.class);//新特供
        openType.put("sign_in", LoginActivity.class);

        //需要登录
        needLoginMap.put("cart", ShoppingCartActivity.class);//购物车
        needLoginMap.put("orders_list", OrderListAllActivity.class);//订单列表
        needLoginMap.put("order_detail", OrderdetailActivity.class);//订单详情
        needLoginMap.put("user_wallet", MyPurseActivity.class);//用户钱包
        needLoginMap.put("user_collect", CollectActivity.class);//收藏
        needLoginMap.put("user_address", AddressManageActivity.class);//地址管理
        needLoginMap.put("user_account", AccountActivity.class);//账户余额
        needLoginMap.put("user_password", ChangePasswordActivity.class);//修改密码
        needLoginMap.put("user_center", CustomercenterActivity.class);//用户中心
        needLoginMap.put("get_integral", InvitationWinRewardActivity.class);//主题列表
        needLoginMap.put("qrshare", ShareQRCodeActivity.class);//分享码

        return mOpenType;
    }

    private static final Class CUSTOMERCENTER_ACTIVITY = CustomercenterActivity.class;
    //    private static final Class GETCODE_ACTIVITY = GetCodeActivity.class;
    private static final Class GETCODE_ACTIVITY = RegisterInputPhoneActivity.class;

    /**
     * 判断是否需要签到
     */
    private static boolean isNeedLogin(Context context, String actName) {
        if (getType(actName).equals(getNeedLoginType(actName))) {
            if (!isUserExist(context)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 用户是否登录
     */
    private static boolean isUserExist(Context context) {
        if (((ECJiaApplication) context.getApplicationContext()).getUser() == null ||
                TextUtils.isEmpty(((ECJiaApplication) context.getApplicationContext()).getUser().getId())) {
            return false;
        }
        return true;
    }

    /**
     * 返回类名
     */
    private static Class getType(String type) {
        return openType.get(type);
//        return openTypeMap.get(type);
    }

    /**
     * 返回需要登录的类名
     */
    private static Class getNeedLoginType(String openType) {
        return needLoginMap.get(openType);
    }

    public static final int ECJIA_OPEN_TYPE_REQUSTCODE = 999;

    /**
     * 启动登录页
     *
     * @param context
     * @param actName
     */
    private static void startLogin(Context context, String actName) {
        Intent intent = new Intent(context, openType.get("sign_in"));
        intent.putExtra("from", actName);
        ((Activity) context).startActivityForResult(intent, ECJIA_OPEN_TYPE_REQUSTCODE);
        ((Activity) context).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);

    }

    /**
     * 执行动作
     *
     * @param context     上下文
     * @param openTypeUrl 获取到的字符串
     */
    public static void startAction(Context context, String openTypeUrl) {

        LG.i("opentype==" + openTypeUrl);
        if (TextUtils.isEmpty(openTypeUrl)) {
            LG.i("please set url and do not null");
            return;
        }

        try {
            ArrayList<String[]> paramas = ECJiaOpenTypeAnalyzer.analyze(openTypeUrl);
            if (paramas != null) {

                if (paramas.size() > 0) {

                    Intent intent;
                    String open_type = paramas.get(0)[1];
                    Class clazz = getType(open_type);
                    if (TextUtils.isEmpty(open_type)) {
                        gotoWebActivity(context, openTypeUrl);
                        return;
                    }

                    //1.判断登录\注册
                    switch (open_type) {
                        case "sign_in"://登录
                            if (isUserExist(context)) {
                                clazz = CUSTOMERCENTER_ACTIVITY;
                            }
                            intent = new Intent(context, clazz);
                            context.startActivity(intent);
                            ((Activity) context).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
                            return;
                        case "sign_up"://注册
                            if (isUserExist(context)) {
                                clazz = CUSTOMERCENTER_ACTIVITY;
                            } else if (AndroidManager.SUPPORT_MOBILE_SININ) {
                                clazz = GETCODE_ACTIVITY;
                            }
                            intent = new Intent(context, clazz);
                            context.startActivity(intent);
                            ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                            return;
                    }

                    //2.判断是否需要登录（没有登陆的，先登录）
                    if (isNeedLogin(context, paramas.get(0)[1])) {
                        LG.i("需要登录");
                        startLogin(context, clazz.getName());
                        return;
                    }

                    if (open_type.equals("get_integral")) {
                        ECJiaBaseIntent ecJiaBaseIntent = new ECJiaBaseIntent(context, clazz);
                        context.startActivity(ecJiaBaseIntent);
                        ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    } else {

                        //3.判断是否是特殊页面(需要多传参数)
                        intent = new Intent(context, clazz);
                        switch (open_type) {
                            case "goods_comment":
                                intent.putExtra(IntentKeywords.TAB_ID, 2);
                                break;
                            case "qrcode":
                                intent.putExtra("startType", 1);
                                break;
                            case "seller":
                                MyEvent event = new MyEvent(EventKeywords.OPENTYPE_SELLER);
                                if (paramas.size() == 2) {
                                    event.setValue(paramas.get(1)[1]);
                                }
                                EventBus.getDefault().post(event);
                                break;
                            case "discover":
                                EventBus.getDefault().post(new MyEvent(EventKeywords.ECJIAMAIN_FIND));
                                LG.i("ECJiaOpenType==" + 2);
                                break;
                            case "main":
                                EventBus.getDefault().post(new MyEvent(EventKeywords.ECJIAMAIN_MAIN));
                                break;
                        }

                        //4.是否有多个参数
                        if (paramas.size() > 1) {
                            for (int i = 1; i < paramas.size(); i++) {
                                intent.putExtra(paramas.get(i)[0], paramas.get(i)[1]);
                            }
                        }

                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    }


                }

            } else {
                if (openTypeUrl.contains("token=token")) {
                    gotoTouchWebActivity(context, openTypeUrl);
                } else {
                    gotoWebActivity(context, openTypeUrl);
                }

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 跳网页
     *
     * @param context
     * @param openTypeUrl
     */
    private static void gotoWebActivity(Context context, String openTypeUrl) {
        Intent intent = new Intent(context, com.ecjia.view.activity.web.WebViewActivity.class);
        intent.putExtra("url", openTypeUrl);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }


    /**
     * 跳网页
     *
     * @param context
     * @param openTypeUrl
     */
    private static void gotoTouchWebActivity(Context context, String openTypeUrl) {
        Intent intent = new Intent(context, openType.get("webview"));
        intent.putExtra(IntentKeywords.WEB_URL, initECJiaTouchUrl(openTypeUrl));
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    /**
     * 处理touch登录
     *
     * @param url
     * @return
     */
    private static String initECJiaTouchUrl(String url) {
        String touchUrl = "";
        if (!TextUtils.isEmpty(SESSION.getInstance().getSid())) {
            touchUrl = url.replace("token=token", "token=" + SESSION.getInstance().getSid());
        } else {
            touchUrl = url.replace("?token=token", "");
        }
        return touchUrl;
    }

}
