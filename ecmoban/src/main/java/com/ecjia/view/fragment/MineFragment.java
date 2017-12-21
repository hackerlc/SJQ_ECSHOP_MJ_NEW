package com.ecjia.view.fragment;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.ECJiaBaseFragment;
import com.ecjia.consts.EventKeywords;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.consts.OrderType;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.network.HttpResponse;
import com.ecjia.network.netmodle.HelpModel;
import com.ecjia.network.netmodle.UserInfoModel;
import com.ecjia.util.CheckInternet;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.FormatUtil;
import com.ecjia.util.ProfilePhotoUtil;
import com.ecjia.view.activity.AccountActivity;
import com.ecjia.view.activity.AddressManageActivity;
import com.ecjia.view.activity.CollectActivity;
import com.ecjia.view.activity.HelpListActivity;
import com.ecjia.view.activity.LastBrowseActivity;
import com.ecjia.view.activity.MyPurseActivity;
import com.ecjia.view.activity.RedpapperListActivity;
import com.ecjia.view.activity.SettingActivity;
import com.ecjia.view.activity.ShareQRCodeActivity;
import com.ecjia.view.activity.ShopCollectActivity;
import com.ecjia.view.activity.WebViewActivity;
import com.ecjia.view.activity.goodsorder.OrderListActivity;
import com.ecjia.view.activity.goodsorder.OrderListAllActivity;
import com.ecjia.view.activity.push.PushActivity;
import com.ecjia.view.activity.user.ChangePasswordActivity;
import com.ecjia.view.activity.user.CustomercenterActivity;
import com.ecjia.view.activity.user.LoginActivity;
import com.ecjia.view.activity.user.UserApplyMenberActivity;
import com.ecjia.view.activity.user.UserBonusActivity;
import com.ecjia.view.activity.user.UserCouponActivity;
import com.ecjia.view.adapter.MineHelpListAdapter;
import com.ecjia.view.adapter.Sqlcl;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.MyListView;
import com.ecjia.widgets.ScrollView_Main;
import com.ecjia.widgets.ToastView;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecmoban.android.sijiqing.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 个人中心页面
 */
public class MineFragment extends ECJiaBaseFragment implements View.OnClickListener, HttpResponse {
    private View view;
    private LinearLayout userLL;
    private TextView user_login, tv_password;
    public ImageView user_image;
    private TextView user_name;
    private TextView user_level;
    private LinearLayout collection_goods_LL;
    private TextView collection_goods_num;
    private LinearLayout historyLL;
    private TextView history_num;
    private LinearLayout orderLL;

    private LinearLayout order_waitpay;
    private LinearLayout order_waitship;
    private LinearLayout order_shipped;
    private LinearLayout order_finished;
    private TextView order_waitpay_num;
    private TextView order_waitship_num;
    private TextView order_shipped_num;
    private TextView order_finished_num;

    private LinearLayout walletLL;
    private TextView wallet_balance;
    private TextView wallet_redpaper;
    private TextView wallet_integral;
    private LinearLayout addressLL;
    private LinearLayout changepasswordLL;
    private LinearLayout official_serviceLL;
    private LinearLayout official_websiteLL;
    private TextView official_phone;
    private TextView official_siteurl;
    private Resources resources;
    private FrameLayout headLL;
    private LinearLayout wallet_balance_LL;
    private LinearLayout wallet_redpaper_LL;
    private LinearLayout wallet_integral_LL;
    private LinearLayout feedback;
    private LinearLayout mine_help;
    private MyListView mListView;
    private MineHelpListAdapter mineListAdapter;
    private TextView collection_shop_num;
    private LinearLayout collection_shop_LL;

    private LinearLayout mine_order_tuanpi;//待成团
    private LinearLayout ll_my_code;//邀请码
    private TextView mine_order_tuanpi_num;//
    private LinearLayout mine_order_returngood;//退换货
    private TextView mine_order_returngood_num;//
    private LinearLayout mine_apply_menber;//采购商认证
    private TextView tv_apply_menber;//
    private LinearLayout mine_mycoupon;//优惠券
    private TextView tv_mycoupon;//
    private LinearLayout mine_redpapper;//红包
    private TextView tv_redpapper;//


    private SharedPreferences spf;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spf = parentActivity.getSharedPreferences("userInfo", 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        setInfo();
        return view;
    }

    UserInfoModel userInfoModel;

    @Override
    public void onResume() {
        super.onResume();
        if (mApp.getUser() != null) {
            if (userInfoModel == null) {
                userInfoModel = new UserInfoModel(getActivity());
                userInfoModel.addResponseListener(this);
            }
            userInfoModel.getUserInfo();
            userLL.setVisibility(View.VISIBLE);
            user_login.setVisibility(View.GONE);
            setUserInfoOnline();
        } else {
            userLL.setVisibility(View.GONE);
            user_login.setVisibility(View.VISIBLE);
            setUserInfoOffLine();
        }
        setBrowseRecordNum();
        if (mApp.getConfig() != null) {
            official_phone.setText(mApp.getConfig().getService_phone());
            official_siteurl.setText(mApp.getConfig().getSite_url());
        }
        if (userInfoModel != null) {
            userInfoModel.addResponseListener(this);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (userInfoModel != null) {
            userInfoModel.removeResponseListener(this);
        }
    }

    private void setUserInfoOffLine() {
        user_name.setText("");
        user_level.setText("");
        user_image.setImageResource(R.drawable.profile_no_avarta_icon);
        user_image_top.setImageResource(R.drawable.profile_no_avarta_icon);
        collection_goods_num.setText("0");
        collection_shop_num.setText("0");
        order_waitpay_num.setText("");
        order_waitpay_num.setVisibility(View.GONE);

        order_waitship_num.setText("");
        order_waitship_num.setVisibility(View.GONE);

        order_shipped_num.setText("");
        order_shipped_num.setVisibility(View.GONE);

        order_finished_num.setText("");
        order_finished_num.setVisibility(View.GONE);

        mine_order_tuanpi_num.setText("");
        mine_order_tuanpi_num.setVisibility(View.GONE);

        mine_order_returngood_num.setText("");
        mine_order_returngood_num.setVisibility(View.GONE);

        tv_mycoupon.setText("");
        tv_apply_menber.setText("认证即可享受批发价");
        tv_redpapper.setText("");

        view.findViewById(R.id.mine_lottery).setVisibility(View.GONE);
        wallet_balance.setText("￥0.00");
        wallet_redpaper.setText("0");
        wallet_integral.setText("0");
    }

    public void setUserInfoOnline() {
        user_name.setText(mApp.getUser().getName());
        if ("2".equals(mApp.getUser().getPurchaser_valid())) {
            user_level.setText("认证会员");
        } else {
            user_level.setText("普通会员");
        }
        if (ProfilePhotoUtil.getInstance().isProfilePhotoExist(mApp.getUser().getId())) {
            user_image.setImageBitmap(ProfilePhotoUtil.getInstance().getProfileBitmap(mApp.getUser().getId()));
            user_image_top.setImageBitmap(ProfilePhotoUtil.getInstance().getProfileBitmap(mApp.getUser().getId()));
        } else {
            user_image.setImageResource(R.drawable.profile_no_avarta_icon_light);
            user_image_top.setImageResource(R.drawable.profile_no_avarta_icon_light);
        }

        collection_goods_num.setText(mApp.getUser().getCollection_num() + "");
        collection_shop_num.setText(mApp.getUser().getCollect_merchant_num() + "");
        order_waitpay_num.setText(mApp.getUser().getOrder_num().getAwait_pay() + "");
        if (mApp.getUser().getOrder_num().getAwait_pay() > 0) {
            if (mApp.getUser().getOrder_num().getAwait_pay() > 99) {
                order_waitpay_num.setText("99+");
            }
            order_waitpay_num.setVisibility(View.VISIBLE);
        } else {
            order_waitpay_num.setVisibility(View.GONE);
        }

        order_waitship_num.setText(mApp.getUser().getOrder_num().getAwait_ship() + "");
        if (mApp.getUser().getOrder_num().getAwait_ship() > 0) {
            if (mApp.getUser().getOrder_num().getAwait_ship() > 99) {
                order_waitship_num.setText("99+");
            }
            order_waitship_num.setVisibility(View.VISIBLE);
        } else {
            order_waitship_num.setVisibility(View.GONE);
        }

        order_shipped_num.setText(mApp.getUser().getOrder_num().getShipped() + "");
        if (mApp.getUser().getOrder_num().getShipped() > 0) {

            if (mApp.getUser().getOrder_num().getShipped() > 99) {
                order_shipped_num.setText("99+");
            }

            order_shipped_num.setVisibility(View.VISIBLE);
        } else {
            order_shipped_num.setVisibility(View.GONE);
        }


        order_finished_num.setText(mApp.getUser().getOrder_num().getAllow_comment() + "");
        if (mApp.getUser().getOrder_num().getAllow_comment() > 0) {

            if (mApp.getUser().getOrder_num().getAllow_comment() > 99) {
                order_finished_num.setText("99+");
            }

            order_finished_num.setVisibility(View.VISIBLE);
        } else {
            order_finished_num.setVisibility(View.GONE);
        }

        mine_order_tuanpi_num.setText(mApp.getUser().getOrder_num().getAwait_groupbuy() + "");
        if (mApp.getUser().getOrder_num().getAwait_groupbuy() > 0) {

            if (mApp.getUser().getOrder_num().getAwait_groupbuy() > 99) {
                mine_order_tuanpi_num.setText("99+");
            }

            mine_order_tuanpi_num.setVisibility(View.VISIBLE);
        } else {
            mine_order_tuanpi_num.setVisibility(View.GONE);
        }

        mine_order_returngood_num.setText(mApp.getUser().getOrder_num().getOrder_cancel() + "");
        if (mApp.getUser().getOrder_num().getOrder_cancel() > 0) {

            if (mApp.getUser().getOrder_num().getOrder_cancel() > 99) {
                mine_order_returngood_num.setText("99+");
            }

            mine_order_returngood_num.setVisibility(View.VISIBLE);
        } else {
            mine_order_returngood_num.setVisibility(View.GONE);
        }
        //设置优惠券个数
        if (Integer.parseInt(mApp.getUser().getUser_coupon_count()) > 0) {
            tv_mycoupon.setText(mApp.getUser().getUser_coupon_count() + "张优惠券");
        } else {
            tv_mycoupon.setText("暂无优惠券");
        }
        //设置认证状态
        //采购商验证 0未提交 1认证已提交未审核 2已审核认证   3认证不通过
        switch (mApp.getUser().getPurchaser_valid()) {
            case "0":
                tv_apply_menber.setText("认证即可享受批发价");
                break;
            case "1":
                tv_apply_menber.setText("认证审核中");
                break;
            case "2":
                tv_apply_menber.setText("已认证");
                break;
            case "3":
                tv_apply_menber.setText("认证未通过");
                break;
        }
        tv_redpapper.setText(mApp.getUser().getUser_bonus_count());

//        view.findViewById(R.id.mine_lottery).setVisibility(View.VISIBLE);
        view.findViewById(R.id.mine_lottery).setVisibility(View.GONE);
        wallet_balance.setText("￥" + FormatUtil.formatToDigetPrice(mApp.getUser().getFormated_user_money()));
        wallet_redpaper.setText(mApp.getUser().getUser_bonus_count());
        wallet_integral.setText(mApp.getUser().getUser_points());
    }

    ScrollView_Main contextitem;
    ECJiaTopView topView;
    int lHeight = 200;
    ImageView user_image_top;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setInfo() {
        resources = getActivity().getResources();
        contextitem = (ScrollView_Main) view.findViewById(R.id.main_sc);
        contextitem.setOnScrollListener(new ScrollView_Main.OnScrollListener() {
            @Override
            public void onScroll(int l, int t, int oldl, int oldt) {
                if (t == 0) {
                    topView.getBackground().setAlpha(0);
                    user_image_top.setEnabled(false);
                    user_image_top.setVisibility(View.INVISIBLE);
                    topView.getTitleTextView().setVisibility(View.INVISIBLE);

                } else if (t >= lHeight) {
                    topView.getBackground().setAlpha(255);
                    user_image_top.setEnabled(true);
                    user_image_top.setVisibility(View.VISIBLE);
                    topView.getTitleTextView().setVisibility(View.VISIBLE);

                } else if (t < lHeight) {//=F
                    int progress = (int) (new Float(t) / new Float(lHeight) * 250);//255
                    topView.getBackground().setAlpha(progress);
                    user_image_top.getDrawable().setAlpha(progress);
                    if (t > 100) {
                        user_image_top.setEnabled(true);
                        user_image_top.setVisibility(View.VISIBLE);
                        topView.getTitleTextView().setVisibility(View.VISIBLE);
                    } else {
                        user_image_top.setEnabled(false);
                        user_image_top.setVisibility(View.INVISIBLE);
                        topView.getTitleTextView().setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        topView = (ECJiaTopView) view.findViewById(R.id.mine_topview);
        topView.setLeftType(ECJiaTopView.LEFT_TYPE_BACKIMAGE);
        topView.setTopViewBackground(R.color.public_theme_color_normal_2);
        topView.getBackground().setAlpha(0);
//        topView.setLeftBackImage(R.drawable.icon_main_list_white, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                parentActivity.dl.open();
//            }
//        });
        topView.setLeftBackImage(R.drawable.profile_refresh_site_icon2, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
//        setLeftBackImage  home_messege_icon  startActivity(new Intent(getActivity(), PushActivity.class));
        //mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        topView.setRightType(ECJiaTopView.RIGHT_TYPE_IMAGE);
        topView.setRightImage(R.drawable.home_messege_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PushActivity.class));
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
//        topView.setRightImage(R.drawable.profile_refresh_site_icon2, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), SettingActivity.class);
//                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
//            }
//        });
        topView.setTitleType(ECJiaTopView.TitleType.TEXT);
        topView.setTitleText(R.string.main_mine);
        topView.getTitleTextView().setVisibility(View.INVISIBLE);
        user_image_top = (ImageView) topView.findViewById(R.id.user_image_top);
        user_image_top.setVisibility(View.GONE);
        user_image_top.setOnClickListener(this);
        headLL = (FrameLayout) view.findViewById(R.id.mine_head);
        headLL.getLayoutParams().width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        headLL.getLayoutParams().height = getActivity().getWindowManager().getDefaultDisplay().getWidth() * 220 / 480;

        userLL = (LinearLayout) view.findViewById(R.id.mine_user);
        user_image = (ImageView) view.findViewById(R.id.mine_user_image);
        user_name = (TextView) view.findViewById(R.id.mine_user_name);
        user_level = (TextView) view.findViewById(R.id.mine_user_level);
        user_login = (TextView) view.findViewById(R.id.mine_login);
        tv_password = (TextView) view.findViewById(R.id.tv_password);
        if("1".equals(mApp.getUser().getPassword_null())){
            tv_password.setText(getResources().getString(R.string.customer_setting_password));
        } else {
            tv_password.setText(getResources().getString(R.string.customer_change_password));
        }

        view.findViewById(R.id.mine_lottery).setOnClickListener(this);

        collection_goods_LL = (LinearLayout) view.findViewById(R.id.mine_collection_goods);
        collection_goods_num = (TextView) view.findViewById(R.id.mine_collection_goods_num);

        collection_shop_LL = (LinearLayout) view.findViewById(R.id.mine_collection_shop);
        collection_shop_num = (TextView) view.findViewById(R.id.mine_collection_shop_num);

        historyLL = (LinearLayout) view.findViewById(R.id.mine_history);
        history_num = (TextView) view.findViewById(R.id.mine_history_num);

        orderLL = (LinearLayout) view.findViewById(R.id.mine_order_ll);

        order_waitpay = (LinearLayout) view.findViewById(R.id.mine_order_waitpay);
        order_waitship = (LinearLayout) view.findViewById(R.id.mine_order_waitship);
        order_shipped = (LinearLayout) view.findViewById(R.id.mine_order_shipped);
        order_finished = (LinearLayout) view.findViewById(R.id.mine_order_finished);
        mine_order_tuanpi = (LinearLayout) view.findViewById(R.id.mine_order_tuanpi);
        ll_my_code = (LinearLayout) view.findViewById(R.id.ll_my_code);
        mine_order_returngood = (LinearLayout) view.findViewById(R.id.mine_order_returngood);

        order_waitpay_num = (TextView) view.findViewById(R.id.mine_order_waitpay_num);
        order_waitship_num = (TextView) view.findViewById(R.id.mine_order_waitship_num);
        order_shipped_num = (TextView) view.findViewById(R.id.mine_order_shipped_num);
        order_finished_num = (TextView) view.findViewById(R.id.mine_order_finished_num);
        mine_order_tuanpi_num = (TextView) view.findViewById(R.id.mine_order_tuanpi_num);
        mine_order_returngood_num = (TextView) view.findViewById(R.id.mine_order_returngood_num);

        walletLL = (LinearLayout) view.findViewById(R.id.mine_wallet);

        wallet_balance_LL = (LinearLayout) view.findViewById(R.id.mine_wallet_balance_ll);
        wallet_redpaper_LL = (LinearLayout) view.findViewById(R.id.mine_wallet_redpaper_ll);
        wallet_integral_LL = (LinearLayout) view.findViewById(R.id.mine_wallet_integral_ll);


        wallet_balance = (TextView) view.findViewById(R.id.mine_wallet_balance);
        wallet_redpaper = (TextView) view.findViewById(R.id.mine_wallet_redpaper);
        wallet_integral = (TextView) view.findViewById(R.id.mine_wallet_integral);

        addressLL = (LinearLayout) view.findViewById(R.id.mine_address);
        mine_help = (LinearLayout) view.findViewById(R.id.mine_help);
        changepasswordLL = (LinearLayout) view.findViewById(R.id.mine_changepassword);
        official_serviceLL = (LinearLayout) view.findViewById(R.id.mine_official_service);
        official_phone = (TextView) view.findViewById(R.id.mine_official_phone);

        official_websiteLL = (LinearLayout) view.findViewById(R.id.mine_official_website);
        official_siteurl = (TextView) view.findViewById(R.id.mine_official_siteurl);

        mine_apply_menber = (LinearLayout) view.findViewById(R.id.mine_apply_menber);//采购商认证
        tv_apply_menber = (TextView) view.findViewById(R.id.tv_apply_menber);
        mine_mycoupon = (LinearLayout) view.findViewById(R.id.mine_mycoupon);//优惠券
        tv_mycoupon = (TextView) view.findViewById(R.id.tv_mycoupon);
        mine_redpapper = (LinearLayout) view.findViewById(R.id.mine_redpapper);//红包
        tv_redpapper = (TextView) view.findViewById(R.id.tv_redpapper);


        mine_order_tuanpi.setOnClickListener(this);
        mine_order_returngood.setOnClickListener(this);
        mine_apply_menber.setOnClickListener(this);
        mine_mycoupon.setOnClickListener(this);
        mine_redpapper.setOnClickListener(this);
        ll_my_code.setOnClickListener(this);

        userLL.setOnClickListener(this);
        user_login.setOnClickListener(this);
        collection_goods_LL.setOnClickListener(this);
        collection_shop_LL.setOnClickListener(this);
        historyLL.setOnClickListener(this);
        mine_help.setOnClickListener(this);
        orderLL.setOnClickListener(this);
        order_waitpay.setOnClickListener(this);
        order_waitship.setOnClickListener(this);
        order_shipped.setOnClickListener(this);
        order_finished.setOnClickListener(this);
        walletLL.setOnClickListener(this);

        wallet_balance_LL.setOnClickListener(this);
        wallet_redpaper_LL.setOnClickListener(this);
        wallet_integral_LL.setOnClickListener(this);

        addressLL.setOnClickListener(this);
        changepasswordLL.setOnClickListener(this);
        official_serviceLL.setOnClickListener(this);
        official_websiteLL.setOnClickListener(this);


        mListView = (MyListView) view.findViewById(R.id.fragment_mine_help_list);

        dataModel = new HelpModel(parentActivity);

        dataModel.addResponseListener(this);

        dataModel.fetchShopHelp();
        mineListAdapter = new MineHelpListAdapter(parentActivity, mApp.getShopinfos());
        mListView.setAdapter(mineListAdapter);
        mListView.setVisibility(View.GONE);
        contextitem.smoothScrollTo(0, 0);

    }

    private HelpModel dataModel;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {

        if (url.equals(ProtocolConst.USERINFO)) {
            if (mApp.getUser() != null) {
                userLL.setVisibility(View.VISIBLE);
                user_login.setVisibility(View.GONE);
                setUserInfoOnline();
            } else {
                userLL.setVisibility(View.GONE);
                user_login.setVisibility(View.VISIBLE);
                setUserInfoOffLine();
            }
        } else if (url.equals(ProtocolConst.SHOPHELP)) {
            if (status.getSucceed() == 1) {
                mineListAdapter.notifyDataSetChanged();
                contextitem.smoothScrollTo(0, 0);
            }
        }
    }


    public void setBrowseRecordNum() {
        history_num.setText(getBrowse() + "");
    }

    private Sqlcl s;

    // 获取缓存
    private int getBrowse() {
        s = new Sqlcl(getActivity());
        Cursor c = s.getGoodBrowse();
        int num = 0;
        while (c.moveToNext()) {
            num++;
        }
        return num;
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.mine_login://登录
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
                }
                break;
            case R.id.mine_user://用户信息
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {

                } else {
                    intent = new Intent(getActivity(), CustomercenterActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
                break;
            case R.id.user_image_top://用户信息
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
                } else {
                    intent = new Intent(getActivity(), CustomercenterActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
                break;

            case R.id.mine_lottery:
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
                } else {
                    intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra(IntentKeywords.WEB_URL, mApp.getUser().getSignup_reward_url());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
                break;
            case R.id.mine_collection_goods://关注商品
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
                } else {
                    intent = new Intent(getActivity(), CollectActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
                break;
            case R.id.mine_collection_shop://关注店铺
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim
                            .push_buttom_out);
                } else {
                    intent = new Intent(getActivity(), ShopCollectActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
                break;


            case R.id.mine_history://浏览记录
                intent = new Intent(getActivity(), LastBrowseActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
                break;
            case R.id.mine_order_ll://我的订单
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);

                } else {
                    intent = new Intent(getActivity(), OrderListAllActivity.class);
                    intent.putExtra(IntentKeywords.ORDER_TYPE, OrderType.ALL);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
                break;
            case R.id.mine_order_waitpay://待付款
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);

                } else {
                    intent = new Intent(getActivity(), OrderListActivity.class);
                    intent.putExtra(IntentKeywords.ORDER_TYPE, OrderType.AWAIT_PAY);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
                break;
            case R.id.mine_order_waitship://待发货
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);

                } else {
                    intent = new Intent(getActivity(), OrderListActivity.class);
                    intent.putExtra(IntentKeywords.ORDER_TYPE, OrderType.AWAIT_SHIP);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
                break;
            case R.id.mine_order_shipped://待收货
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim
                            .push_buttom_out);

                } else {
                    intent = new Intent(getActivity(), OrderListActivity.class);
                    intent.putExtra(IntentKeywords.ORDER_TYPE, OrderType.SHIPPED);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
                break;
            case R.id.mine_order_finished://已完成--待评价
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim
                            .push_buttom_out);

                } else {
                    intent = new Intent(getActivity(), OrderListActivity.class);
                    intent.putExtra(IntentKeywords.ORDER_TYPE, OrderType.ALLOW_COMMENT);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }
                break;
            case R.id.mine_order_tuanpi://待成团
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim
                            .push_buttom_out);

                } else {
                    intent = new Intent(getActivity(), OrderListActivity.class);
                    intent.putExtra(IntentKeywords.ORDER_TYPE, OrderType.AWAIT_TUANPI);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }
                break;
            case R.id.mine_order_returngood://退换货
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim
                            .push_buttom_out);

                } else {
                    intent = new Intent(getActivity(), OrderListActivity.class);
                    intent.putExtra(IntentKeywords.ORDER_TYPE, OrderType.RETURN_GOOD);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }
                break;
            case R.id.mine_apply_menber://采购商认证
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim
                            .push_buttom_out);

                } else {
                    //采购商验证 0未提交 1认证已提交未审核 2已审核认证   3认证不通过
                    //switch (mApp.getUser().getUsers_real()){
                    if ("1".equals(mApp.getUser().getPurchaser_valid()) || "2".equals(mApp.getUser().getPurchaser_valid())) {
                        return;
                    } else {
                        intent = new Intent(getActivity(), UserApplyMenberActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.mine_mycoupon://优惠券
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim
                            .push_buttom_out);

                } else {
                    intent = new Intent(getActivity(), UserCouponActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.mine_redpapper://红包
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim
                            .push_buttom_out);

                } else {
                    intent = new Intent(getActivity(), UserBonusActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.mine_wallet://我的钱包
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim
                            .push_buttom_out);

                } else {
                    intent = new Intent(getActivity(), MyPurseActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }
                break;

            case R.id.mine_wallet_balance_ll:
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim
                            .push_buttom_out);

                } else {
                    intent = new Intent(getActivity(), AccountActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }
                break;
            case R.id.mine_help:
                startActivity(new Intent(getActivity(), HelpListActivity.class));
                getActivity().overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
                break;
            case R.id.mine_wallet_redpaper_ll:
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim
                            .push_buttom_out);

                } else {
                    intent = new Intent(getActivity(), RedpapperListActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }
                break;
            case R.id.mine_wallet_integral_ll:
                break;
            case R.id.mine_address://收货地址
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim
                            .push_buttom_out);

                } else {
                    intent = new Intent(getActivity(), AddressManageActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }
                break;
            case R.id.mine_changepassword://修改密码
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim
                            .push_buttom_out);

                } else {
                    intent = new Intent(getActivity(), ChangePasswordActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }
                break;
            case R.id.mine_official_service://官方客服
                if (!TextUtils.isEmpty(official_phone.getText().toString())) {
                    String call = resources.getString(R.string.setting_call_or_not);
                    final MyDialog mDialog = new MyDialog(getActivity(), call, mApp.getConfig().getService_phone());
                    mDialog.setStyle(MyDialog.STYLE_POS_AND_NEG);
                    mDialog.setPositiveListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mApp.getConfig().getService_phone()));
                            startActivity(intent);
                        }
                    });
                    mDialog.setNegativeListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                        }
                    });
                    mDialog.show();

                } else {
                    String call_cannot_empty = resources.getString(R.string.setting_call_cannot_empty);
                    ToastView toast = new ToastView(getActivity(), call_cannot_empty);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;
            case R.id.mine_official_website://官网
                if (!CheckInternet.isConnectingToInternet(getActivity()) || mApp.getConfig() == null) {//检查网络连接
                    String network_problem = resources.getString(R.string.goodlist_network_problem);
                    ToastView toast = new ToastView(getActivity(), network_problem);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    String off = resources.getString(R.string.setting_website);
                    Intent it = new Intent(getActivity(), WebViewActivity.class);
                    it.putExtra(IntentKeywords.WEB_URL, mApp.getConfig().getSite_url());
                    it.putExtra(IntentKeywords.WEB_TITLE, off);
                    startActivity(it);
                    getActivity().overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }
                break;
            case R.id.ll_my_code:
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
                } else {
                    Intent it = new Intent(getContext(), ShareQRCodeActivity.class);
                    startActivity(it);
                }
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void onEvent(MyEvent event) {
        if (event.getMsg().equals(EventKeywords.USER_PHOTO_DOWNLOAD_SUCCESS)) {
            user_image.setImageBitmap(ProfilePhotoUtil.getInstance().getProfileBitmap(mApp.getUser().getId()));
            user_image_top.setImageBitmap(ProfilePhotoUtil.getInstance().getProfileBitmap(mApp.getUser().getId()));
        }

        if (event.getMsg().equals(EventKeywords.USER_CHANGE_PHOTO)) {
            user_image.setImageBitmap(ProfilePhotoUtil.getInstance().getProfileBitmap(mApp.getUser().getId()));
            user_image_top.setImageBitmap(ProfilePhotoUtil.getInstance().getProfileBitmap(mApp.getUser().getId()));
        }
    }
}