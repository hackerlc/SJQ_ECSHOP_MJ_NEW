package com.ecjia.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.ecjia.consts.AndroidManager;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.view.ECJiaMainActivity;
import com.ecjia.view.activity.AccountActivity;
import com.ecjia.view.activity.AddressManageActivity;
import com.ecjia.view.activity.CollectActivity;
import com.ecjia.view.activity.CommentActivity;
import com.ecjia.view.activity.ConsultActivity;
import com.ecjia.view.activity.FindHotNewsActivity;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.view.activity.GoodsListActivity;
import com.ecjia.view.activity.GroupbuyGoodsActivity;
import com.ecjia.view.activity.HelpListActivity;
import com.ecjia.view.activity.LanguageActivity;
import com.ecjia.view.activity.LastBrowseActivity;
import com.ecjia.view.activity.MapActivity;
import com.ecjia.view.activity.MobilebuyGoodsActivity;
import com.ecjia.view.activity.MyCaptureActivity;
import com.ecjia.view.activity.MyFindActivity;
import com.ecjia.view.activity.MyPurseActivity;
import com.ecjia.view.activity.SearchAllActivity;
import com.ecjia.view.activity.SettingActivity;
import com.ecjia.view.activity.ShareQRCodeActivity;
import com.ecjia.view.activity.ShoppingCartActivity;
import com.ecjia.view.activity.TopicDetailActivity;
import com.ecjia.view.activity.TopicListActivity;
import com.ecjia.view.activity.WebViewActivity;
import com.ecjia.view.activity.goodsorder.OrderListActivity;
import com.ecjia.view.activity.goodsorder.OrderdetailActivity;
import com.ecjia.view.activity.push.PushActivity;
import com.ecjia.view.activity.user.ChangePasswordActivity;
import com.ecjia.view.activity.user.CustomercenterActivity;
import com.ecjia.view.activity.user.GetPasswordActivity;
import com.ecjia.view.activity.user.LoginActivity;
import com.ecjia.view.activity.user.MobileRegisterActivity;
import com.ecjia.view.activity.user.RegisterActivity;
import com.ecjia.view.activity.user.RegisterInputPhoneActivity;
import com.ecjia.view.fragment.TabsFragment;
import com.ecjia.widgets.ToastView;
import com.ecmoban.android.sijiqing.R;

import org.json.JSONException;

import java.util.Map;

/**
 * Created by Administrator on 2015/9/15.
 */
public class ECJiaDealUtil {
    private Context context;

    public ECJiaDealUtil(Context context) {
        this.context = context;
    }


    public void dealEcjiaAction(Map<String, String> map) { //处理Ecjia跳转规则

        if ("main".equals(map.get("open_type"))) {//主页
            Intent intent = new Intent(context, ECJiaMainActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        } else if ("discover".equals(map.get("open_type"))) {//发现
            Intent intent = new Intent(context, MyFindActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        } else if ("qrcode".equals(map.get("open_type"))) {//二维码扫描
            Intent intent = new Intent(context, MyCaptureActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        } else if ("qrshare".equals(map.get("open_type"))) {//二维码分享
            Intent intent = new Intent(context, ShareQRCodeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("startType", 1);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        } else if ("history".equals(map.get("open_type"))) {//浏览记录
            Intent intent = new Intent(context, LastBrowseActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        } else if ("feedback".equals(map.get("open_type"))) {//咨询
            Intent intent = new Intent(context, ConsultActivity.class);
            intent.putExtra("type", "all_consult");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        } else if ("map".equals(map.get("open_type"))) {//地图
            Intent intent = new Intent(context, MapActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        } else if ("message".equals(map.get("open_type"))) {//消息
            Intent intent = new Intent(context, PushActivity.class);
            intent.putExtra("refresh", true);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        } else if ("setting".equals(map.get("open_type"))) {//设置
            Intent intent = new Intent(context, SettingActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        } else if ("language".equals(map.get("open_type"))) {//语言选择
            Intent intent = new Intent(context, LanguageActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        } else if ("webview".equals(map.get("open_type"))) {//浏览器
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("url", map.get("url"));
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        } else if ("cart".equals(map.get("open_type"))) {//购物车
            if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                Intent intent = new Intent(context, ShoppingCartActivity.class);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra("from", map.get("open_type"));
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }

        } else if ("search".equals(map.get("open_type"))) {//搜素
            Intent intent = new Intent(context, SearchAllActivity.class);
            intent.putExtra("keywords", map.get("keyword"));
            context.startActivity(intent);
        } else if ("help".equals(map.get("open_type"))) {//帮助中心
            Intent intent = new Intent(context, HelpListActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        } else if ("goods_list".equals(map.get("open_type"))) {//商品列表
            Intent intent = new Intent(context, GoodsListActivity.class);
            FILTER filter = new FILTER();
            filter.setCategory_id(map.get("category_id"));
            try {
                intent.putExtra("filter", filter.toJson().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        } else if ("goods_comment".equals(map.get("open_type"))) {//商品评论
            Intent intent = new Intent(context, CommentActivity.class);
            intent.putExtra("id", map.get("goods_id"));
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        } else if ("goods_detail".equals(map.get("open_type"))) {//商品详情
            Intent intent = new Intent(context, GoodsDetailActivity.class);
            intent.putExtra("goods_id", map.get("goods_id"));
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        } else if ("signin".equals(map.get("open_type"))) {//登录
            if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                Intent intent = new Intent(context, CustomercenterActivity.class);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }

        } else if ("signup".equals(map.get("open_type"))) {//注册
            if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                Intent intent = new Intent(context, CustomercenterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            } else {
                Intent intent = new Intent(context, RegisterInputPhoneActivity.class);
//                Intent intent = new Intent(context, GetCodeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }
        } else if ("forget_password".equals(map.get("open_type"))) {//忘记密码
            if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                Intent intent = new Intent(context, CustomercenterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            } else {
                Intent intent = new Intent(context, GetPasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }

        } else if ("orders_list".equals(map.get("open_type"))) {//我的订单
            if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                Intent intent = new Intent(context, OrderListActivity.class);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra("from", map.get("open_type"));
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }
        } else if ("orders_detail".equals(map.get("open_type"))) {//订单详情
            if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                Intent intent = new Intent(context, OrderdetailActivity.class);
                intent.putExtra("orderid", map.get("order_id"));
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra("from", map.get("open_type"));
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }

        } else if ("user_wallet".equals(map.get("open_type"))) {//我的钱包
            if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                Intent intent = new Intent(context, MyPurseActivity.class);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra("from", map.get("open_type"));
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }

        } else if ("user_address".equals(map.get("open_type"))) {//地址管理
            if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                Intent intent = new Intent(context, AddressManageActivity.class);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra("from", map.get("open_type"));
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }

        } else if ("user_account".equals(map.get("open_type"))) {//账户余额
            if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                Intent intent = new Intent(context, AccountActivity.class);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            } else {
                Intent intent = new Intent(context, LoginActivity.class);

                intent.putExtra("from", map.get("open_type"));
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }

        } else if ("user_password".equals(map.get("open_type"))) {//修改密码
            if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                Intent intent = new Intent(context, ChangePasswordActivity.class);

                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra("from", map.get("open_type"));
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }
        } else if ("user_collect".equals(map.get("open_type"))) {//收藏
            if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                Intent intent = new Intent(context, CollectActivity.class);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra("from", map.get("open_type"));
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }

        } else if ("user_center".equals(map.get("open_type"))) {//用户中心
            if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                Intent intent = new Intent(context, CustomercenterActivity.class);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra("from", map.get("open_type"));
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }
        } else if ("topic".equals(map.get("open_type"))) {   //专题详情
            Intent intent = new Intent(context, TopicDetailActivity.class);
            intent.putExtra("topic_id", map.get("topic_id"));
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        } else if ("topic_list".equals(map.get("open_type"))) {   //专题列表
            Intent intent = new Intent(context, TopicListActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        } else if ("mobile_news".equals(map.get("open_type"))) {   //今日热点
            Intent intent = new Intent(context, FindHotNewsActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        } else if ("groupbuy".equals(map.get("open_type"))) {   //团购
            Intent intent = new Intent(context, GroupbuyGoodsActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        } else if ("mobilebuy".equals(map.get("open_type"))) {   //手机专享
            Intent intent = new Intent(context, MobilebuyGoodsActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);

        } else if ("seller".equals(map.get("open_type"))) {  //店铺街
            TabsFragment.getInstance().select_three(map.get("category_id"));
        } else if ("category_list".equals(map.get("open_type"))) {  //分类
            TabsFragment.getInstance().OnTabSelected("tab_two");
        } else if ("sign_in".equals(map.get("open_type"))) {//登录
            if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                Intent intent = new Intent(context, CustomercenterActivity.class);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra("from", map.get("open_type"));
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }
        } else if ("sign_up".equals(map.get("open_type"))) {//注册
            if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                Intent intent = new Intent(context, CustomercenterActivity.class);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            } else {
                Intent intent = new Intent();
                if (AndroidManager.SUPPORT_MOBILE_SININ) {
                    intent.setClass(context, MobileRegisterActivity.class);
                } else {
                    intent.setClass(context, RegisterActivity.class);
                }
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }
        } else if ("forget_password".equals(map.get("open_type"))) {//忘记密码
            if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                Intent intent = new Intent(context, ChangePasswordActivity.class);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            } else {
                Intent intent = new Intent(context, GetPasswordActivity.class);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }
        } else if ("user_password".equals(map.get("open_type"))) {//修改密码
            if (SESSION.getInstance() != null && !TextUtils.isEmpty(SESSION.getInstance().getSid())) {
                Intent intent = new Intent(context, ChangePasswordActivity.class);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra("from", map.get("open_type"));
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }
        } else {
            ToastView toast = new ToastView(context, context.getResources().getString(R.string.not_support_feature));
            toast.show();
        }
    }
}
