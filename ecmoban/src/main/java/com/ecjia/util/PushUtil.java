package com.ecjia.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.ecjia.base.ECJiaApplication;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.EventKeywords;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.consts.PushKeywords;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.MYMESSAGE;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.view.ECJiaMainActivity;
import com.ecjia.view.activity.AccountActivity;
import com.ecjia.view.activity.AddressManageActivity;
import com.ecjia.view.activity.BannerWebActivity;
import com.ecjia.view.activity.ConsultActivity;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.view.activity.GoodsListActivity;
import com.ecjia.view.activity.HelpListActivity;
import com.ecjia.view.activity.LanguageActivity;
import com.ecjia.view.activity.LastBrowseActivity;
import com.ecjia.view.activity.MapActivity;
import com.ecjia.view.activity.MyCaptureActivity;
import com.ecjia.view.activity.MyFindActivity;
import com.ecjia.view.activity.MyPurseActivity;
import com.ecjia.view.activity.SearchAllActivity;
import com.ecjia.view.activity.SettingActivity;
import com.ecjia.view.activity.ShareQRCodeActivity;
import com.ecjia.view.activity.ShoppingCartActivity;
import com.ecjia.view.activity.WebViewActivity;
import com.ecjia.view.activity.goodsorder.OrderListAllActivity;
import com.ecjia.view.activity.goodsorder.OrderdetailActivity;
import com.ecjia.view.activity.push.PushActivity;
import com.ecjia.view.activity.user.ChangePasswordActivity;
import com.ecjia.view.activity.user.CustomercenterActivity;
import com.ecjia.view.activity.user.LoginActivity;
import com.ecjia.view.activity.user.RegisterInputPhoneActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.message.entity.UMessage;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by Adam on 2016/7/25.
 */
public class PushUtil {

    public static void setDeviceToken(final ECJiaApplication mApp, final String device_token) {
        final String url = ProtocolConst.SETDEVICE_TOKEN;
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            if (!TextUtils.isEmpty(device_token)) {
                requestJsonObject.put("device_token", device_token);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===device/setDeviceToken传入===" + requestJsonObject.toString());

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.getURLAPI() + url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("===device/setDeviceToken返回===" + jo.toString());
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        mApp.getSharedPreferences("device", 0).edit()
                                .putString("device_token", device_token)
                                .putLong("time", System.currentTimeMillis()).commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }

    /**
     * 用户自定义消息接收该Handler是在BroadcastReceiver中被调用，
     * 故如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
     */
    public static void oPendAction(Context context, UMessage uMessage) {
        oPendAction(context, uMessage, false);
    }

    public static void oPendAction(Context context, UMessage uMessage, boolean isMessageAdapter) {
        if (!isMessageAdapter) {
            Intent intent1 = new Intent(context, ECJiaMainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }

        if (null != uMessage.extra) {

            String open_type = uMessage.extra.get("open_type");

            if (!TextUtils.isEmpty(open_type)) {
                Intent intent;
                switch (open_type) {

                    case PushKeywords.MAIN:
                        //主页
                        intent = new Intent(context, ECJiaMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case PushKeywords.DISCOVER:
                        //发现
                        intent = new Intent(context, ECJiaMainActivity.class);
                        context.startActivity(intent);
                        EventBus.getDefault().post(new MyEvent(EventKeywords.ECJIAMAIN_FIND));
                        break;
                    case PushKeywords.QRCODE: //二维码扫描
                        intent = new Intent(context, MyCaptureActivity.class);
                        intent.putExtra("startType", 1);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case PushKeywords.QRSHARE://二维码分享
                        if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                            intent = new Intent(context, ShareQRCodeActivity.class);
                            intent.putExtra("startType", 1);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("from", uMessage.extra.get("open_type"));
                            context.startActivity(intent);
                        }
                        break;
                    case PushKeywords.HISTORY://浏览记录
                        intent = new Intent(context, LastBrowseActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case PushKeywords.FEEDBACK://咨询
                        intent = new Intent(context, ConsultActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case PushKeywords.MAP://地图
                        intent = new Intent(context, MapActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case PushKeywords.MESSAGE://消息
                        intent = new Intent(context, PushActivity.class);
                        intent.putExtra("refresh", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case PushKeywords.SETTING://设置
                        intent = new Intent(context, SettingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case PushKeywords.LANGUAGE://语言选择
                        intent = new Intent(context, LanguageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case PushKeywords.WEBVIEW://浏览器
                        intent = new Intent(context, WebViewActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(IntentKeywords.WEB_URL, uMessage.extra.get(PushKeywords.PARAMS_URL));
                        context.startActivity(intent);
                        break;
                    case PushKeywords.CART://购物车
                        if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                            intent = new Intent(context, ShoppingCartActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("from", uMessage.extra.get("open_type"));
                            context.startActivity(intent);
                        }
                        break;
                    case PushKeywords.SEARCH://搜素
                        intent = new Intent(context, SearchAllActivity.class);
                        intent.putExtra(IntentKeywords.KEYWORDS, uMessage.extra.get(PushKeywords.PARAMS_KEYWORD));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case PushKeywords.HELP://帮助中心
                        intent = new Intent(context, HelpListActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case PushKeywords.GOODS_LIST://商品列表
                        intent = new Intent(context, GoodsListActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(IntentKeywords.CATEGORY_ID, uMessage.extra.get(PushKeywords.PARAMS_CATEGORY_ID));
                        context.startActivity(intent);
                        break;
                    case PushKeywords.GOODS_COMMENT://商品评论
                        intent = new Intent(context, GoodsDetailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(IntentKeywords.GOODS_ID, uMessage.extra.get(PushKeywords.PARAMS_GOODS_ID) + "");
                        intent.putExtra(IntentKeywords.TAB_ID, 2);
                        context.startActivity(intent);
                        break;
                    case PushKeywords.GOODS_DETAIL://商品详情
                        intent = new Intent(context, GoodsDetailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(IntentKeywords.GOODS_ID, uMessage.extra.get(PushKeywords.PARAMS_GOODS_ID) + "");
                        context.startActivity(intent);
                        break;
                    case PushKeywords.SIGNIN://登录
                        if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                            intent = new Intent(context, CustomercenterActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        break;
                    case PushKeywords.SIGNUP://注册
                        if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                            intent = new Intent(context, CustomercenterActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            intent = new Intent(context, RegisterInputPhoneActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        break;
                    case PushKeywords.ORDERS_LIST://我的订单
                        if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                            intent = new Intent(context, OrderListAllActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            intent = new Intent(context, LoginActivity.class);
                            intent.putExtra("from", uMessage.extra.get("open_type"));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        break;
                    case PushKeywords.ORDERS_DETAIL://订单详情
                        if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                            intent = new Intent(context, OrderdetailActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(IntentKeywords.ORDER_ID, uMessage.extra.get(PushKeywords.PARAMS_ORDER_ID));
                            context.startActivity(intent);
                        } else {
                            intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("from", uMessage.extra.get("open_type"));
                            context.startActivity(intent);
                        }
                        break;
                    case PushKeywords.USER_WALLET://我的钱包
                        if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                            intent = new Intent(context, MyPurseActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("from", uMessage.extra.get("open_type"));
                            context.startActivity(intent);
                        }
                        break;
                    case PushKeywords.USER_ADDRESS://地址管理
                        if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                            intent = new Intent(context, AddressManageActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("from", uMessage.extra.get("open_type"));
                            context.startActivity(intent);
                        }
                        break;
                    case PushKeywords.USER_ACCOUNT://账户余额
                        if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                            intent = new Intent(context, AccountActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("from", uMessage.extra.get("open_type"));
                            context.startActivity(intent);
                        }
                        break;
                    case PushKeywords.USER_PASSWORD://修改密码
                        if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                            intent = new Intent(context, ChangePasswordActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("from", uMessage.extra.get("open_type"));
                            context.startActivity(intent);
                        }
                        break;
                    case PushKeywords.USER_CENTER://用户中心
                        if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                            intent = new Intent(context, CustomercenterActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("from", uMessage.extra.get("open_type"));
                            context.startActivity(intent);
                        }
                        break;
                    default:
                        intent = new Intent(context, PushActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("refresh", true);
                        context.startActivity(intent);
                        break;
                }
            } else {
                Intent intent = new Intent(context, PushActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("refresh", true);
                context.startActivity(intent);
            }
        } else {
            Intent intent = new Intent(context, PushActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("refresh", true);
            context.startActivity(intent);
        }

    }


    /**
     * 处理adapter的openaction
     *
     * @param context
     * @param message
     */
    public static void oPendAction(Context context, MYMESSAGE message) {

        String pushkeywords = message.getOpen_type();

        if (!TextUtils.isEmpty(pushkeywords)) {
            Intent intent = null;
            switch (pushkeywords) {

                case PushKeywords.MAIN://主页
                    intent = new Intent(context, ECJiaMainActivity.class);
                    context.startActivity(intent);
                    break;

                case PushKeywords.SIGNIN://登录
                    if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                        intent = new Intent(context, CustomercenterActivity.class);
                    } else {
                        intent = new Intent(context, LoginActivity.class);
                    }
                    context.startActivity(intent);
                    break;
                case PushKeywords.SIGNUP://注册
                    if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                        intent = new Intent(context, CustomercenterActivity.class);
                    } else {
                        intent = new Intent(context, RegisterInputPhoneActivity.class);
                    }
                    context.startActivity(intent);
                    break;

                case PushKeywords.DISCOVER://发现
                    EventBus.getDefault().post(new MyEvent(EventKeywords.ECJIAMAIN_FIND));
                    intent = new Intent(context, ECJiaMainActivity.class);
                    context.startActivity(intent);
                    break;
                case PushKeywords.QRCODE://二维码扫描
                    intent = new Intent(context, MyCaptureActivity.class);
                    intent.putExtra("startType", 1);
                    context.startActivity(intent);
                    break;
                case PushKeywords.QRSHARE //暂无二维码分享
                        :
                    if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                        intent = new Intent(context, ShareQRCodeActivity.class);
                        intent.putExtra("startType", 1);
                        context.startActivity(intent);
                    } else {
                        intent = new Intent(context, LoginActivity.class);
                        intent.putExtra("from", pushkeywords);
                        context.startActivity(intent);
                    }
                    break;
                case PushKeywords.HISTORY://浏览历史
                    intent = new Intent(context, LastBrowseActivity.class);
                    context.startActivity(intent);
                    break;
                case PushKeywords.FEEDBACK://咨询
                    intent = new Intent(context, ConsultActivity.class);
                    context.startActivity(intent);
                    break;
                case PushKeywords.MAP://地图
                    intent = new Intent(context, MapActivity.class);
                    context.startActivity(intent);
                    break;
                case PushKeywords.MESSAGE://消息页，不做处理
                    break;

                case PushKeywords.WEBVIEW://浏览器
                    intent = new Intent(context, BannerWebActivity.class);
                    intent.putExtra(IntentKeywords.WEB_URL, message.getWebUrl());
                    context.startActivity(intent);
                    break;
                case PushKeywords.SETTING
                        ://设置页面
                    intent = new Intent(context, SettingActivity.class);
                    context.startActivity(intent);
                    break;
                case PushKeywords.LANGUAGE://语言切换
                    intent = new Intent(context, LanguageActivity.class);
                    context.startActivity(intent);
                    break;

                case PushKeywords.CART://购物车
                    if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                        intent = new Intent(context, ShoppingCartActivity.class);
                        context.startActivity(intent);
                    } else {
                        intent = new Intent(context, LoginActivity.class);
                        intent.putExtra("from", pushkeywords);
                        context.startActivity(intent);
                    }
                    break;
                case PushKeywords.SEARCH://搜索页面
                    intent = new Intent(context, SearchAllActivity.class);
                    intent.putExtra(IntentKeywords.KEYWORDS, message.getKeyword());
                    intent.setClass(context, MyFindActivity.class);
                    context.startActivity(intent);
                    break;
                case PushKeywords.HELP://帮助中心
                    intent = new Intent(context, HelpListActivity.class);
                    context.startActivity(intent);
                    break;
                case PushKeywords.GOODS_LIST://商品列表
                    intent = new Intent(context, GoodsListActivity.class);
                    intent.putExtra(IntentKeywords.CATEGORY_ID, message.getCategory_id());
                    context.startActivity(intent);
                    break;
                case PushKeywords.GOODS_COMMENT://商品评论
                    intent = new Intent(context, GoodsDetailActivity.class);
                    intent.putExtra(IntentKeywords.GOODS_ID, message.getGoods_id_comment() + "");
                    intent.putExtra(IntentKeywords.TAB_ID, 2);
                    context.startActivity(intent);
                    break;
                case PushKeywords.GOODS_DETAIL://商品详情
                    intent = new Intent(context, GoodsDetailActivity.class);
                    intent.putExtra(IntentKeywords.GOODS_ID, message.getGoods_id() + "");
                    context.startActivity(intent);
                    break;
                case PushKeywords.ORDERS_LIST://订单列表
                    if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                        intent = new Intent(context, OrderListAllActivity.class);
                    } else {
                        intent = new Intent(context, LoginActivity.class);
                        intent.putExtra("from", pushkeywords);
                    }
                    context.startActivity(intent);
                    break;
                case PushKeywords.ORDERS_DETAIL: //订单详情
                    if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                        intent = new Intent(context, OrderdetailActivity.class);
                        intent.putExtra(IntentKeywords.ORDER_ID, message.getOrder_id());
                    } else {
                        intent = new Intent(context, LoginActivity.class);
                        intent.putExtra("from", pushkeywords);
                    }
                    context.startActivity(intent);
                    break;

                case PushKeywords.USER_CENTER://用户中心
                    if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                        intent = new Intent(context, CustomercenterActivity.class);
                    } else {
                        intent = new Intent(context, LoginActivity.class);
                        intent.putExtra("from", pushkeywords);
                    }
                    context.startActivity(intent);
                    break;

                case PushKeywords.USER_WALLET: //我的钱包

                    if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                        intent = new Intent(context, MyPurseActivity.class);
                    } else {
                        intent = new Intent(context, LoginActivity.class);
                        intent.putExtra("from", pushkeywords);
                    }
                    context.startActivity(intent);
                    break;
                case PushKeywords.USER_ADDRESS://地址管理
                    if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                        intent = new Intent(context, AddressManageActivity.class);
                    } else {
                        intent = new Intent(context, LoginActivity.class);
                        intent.putExtra("from", pushkeywords);
                    }
                    context.startActivity(intent);
                    break;
                case PushKeywords.USER_ACCOUNT://余额账户
                    if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                        intent = new Intent(context, AccountActivity.class);
                        context.startActivity(intent);
                    } else {
                        intent = new Intent(context, LoginActivity.class);
                        intent.putExtra("from", pushkeywords);
                        context.startActivity(intent);
                    }
                    context.startActivity(intent);
                    break;
                case PushKeywords.USER_PASSWORD://修改密码
                    if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                        intent = new Intent(context, ChangePasswordActivity.class);
                    } else {
                        intent = new Intent(context, LoginActivity.class);
                        intent.putExtra("from", pushkeywords);
                    }
                    context.startActivity(intent);
                    break;
                default:
                    LG.i("其他动作，未做处理");
                    break;
            }
        } else {
            LG.i("其他动作，未做处理");
        }
    }

}

