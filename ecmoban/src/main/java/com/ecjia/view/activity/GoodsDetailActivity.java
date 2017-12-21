package com.ecjia.view.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseFragmentActivity;
import com.ecjia.view.activity.user.LoginActivity;
import com.ecjia.base.ECJiaApplication;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.GoodsDetailModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.ShoppingCartModel;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.ErrorView;
import com.ecjia.widgets.GoodsViewPager;
import com.ecjia.widgets.dialog.RedpaperDialog;
import com.ecjia.widgets.ToastView;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.activity.goodsdetail.adapter.ProductDetailPagerAdapter;
import com.ecjia.view.activity.goodsdetail.fragment.OnScrollTabChangeListener;
import com.ecjia.view.activity.goodsdetail.fragment.ProductCommonFragment;
import com.ecjia.view.activity.goodsdetail.fragment.ProductDetailFragment;
import com.ecjia.view.activity.goodsdetail.fragment.ProductFragment;
import com.ecjia.view.adapter.GoodDetailDraft;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.EventBus.Event;
import com.ecjia.util.EventBus.EventBusReceiver;
import com.ecjia.util.ShareHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import de.greenrobot.event.EventBus;
/**
 * 类名介绍：商品详情主页
 * Created by sun
 * Created time 2017-02-13.
 */
public class GoodsDetailActivity extends BaseFragmentActivity implements HttpResponse, EventBusReceiver, OnClickListener, OnScrollTabChangeListener {
    public GoodsDetailModel dataModel;

    public TextView addToCartTextView;
    public TextView buyNowTextView;
    public ImageView collectionButton;

    private ImageView goodDetailShoppingCart;
    private SharedPreferences shared;
    private LinearLayout ll_send_messg;

    Resources resource;
    private TextView shopping_cart_num;
    private LinearLayout shopping_cart_num_bg_one;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private ECJiaApplication mApp;
    RedpaperDialog dialog;
    public boolean isLogin = false;
    ShoppingCartModel cartModel;
    int position;

    private final int LOGIN_COLLECT = 10010;//收藏登录
    private final int LOGIN_REDPAPER = 10011;//红包登录
    private final int LOGIN_ADDCART = 10012;//加入购物车登录
    private final int LOGIN_BALANCE = 10013;//去结算登录
    private final int LOGIN_SHOPCART = 10014;//购物车登录
    private final int SPACIFICATION = 10015;//商品属性
    private final int SPACIFICATION_ADDCART = 10016;//商品属性
    private final int SPACIFICATION_BALANCE = 10017;//商品属性
    private final int ADDRESS_BALANCE = 10018;//去结算地址;
    private final int  SEND_MESSAGE= 10020;//去聊天页面


    private ArrayList<Fragment> mFragments;
    private ProductDetailPagerAdapter productPagerAdapter;
    private String goods_id;
    private String object_id;
    int tab_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        mApp = (ECJiaApplication) getApplication();
        setContentView(R.layout.activity_goods_detial_new);
        EventBus.getDefault().register(this);
        shared = getSharedPreferences("userInfo", 0);
        resource = getResources();
        getData();
        initView();
    }

    private void getData() {
        goods_id = getIntent().getStringExtra(IntentKeywords.GOODS_ID);
        object_id = getIntent().getStringExtra(IntentKeywords.OBJECT_ID);
        tab_id = getIntent().getIntExtra(IntentKeywords.TAB_ID, 0);

    }


    @Override
    protected void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.goodslist_navbar);
        topView.setLeftBackImage(BACKIMAGE_ID, new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (productFragment.noGoods) {
                    finish();
                    return;
                }

                if (viewPager.getCurrentItem() != 0) {
                    viewPager.setCurrentItem(0);
                    return;
                }
                finish();
            }
        });
        topView.setTitleType(ECJiaTopView.TitleType.TABLAYOUT);
        topView.getTitleTextView().setEllipsize(TextUtils.TruncateAt.END);
        topView.getTitleTextView().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);//16sp 
        topView.setTitleText("图文详情");
        topView.setRightType(ECJiaTopView.RIGHT_TYPE_GONE);
        topView.setTitleAnimationEnable(true);
        topView.setOnTitleAnimationEndListener(new ECJiaTopView.OnTitleAnimationEndListener() {
            @Override
            public void upAnimationStart() {

            }

            @Override
            public void downAnimationStart() {
                topView.getTabLayout().setupWithViewPager(viewPager);
            }

            @Override
            public void upAnimationEnd() {
                topView.getTabLayout().setupWithViewPager(null);
            }

            @Override
            public void downAnimationEnd() {
            }
        });
    }

    private void initBootomView() {
        //底部的收藏
        collectionButton = (ImageView) findViewById(R.id.collection_button);
        collectionButton.setOnClickListener(this);

        //加入购物车
        addToCartTextView = (TextView) findViewById(R.id.add_to_cart);
        addToCartTextView.setOnClickListener(this);

        ll_send_messg= (LinearLayout) findViewById(R.id.ll_send_messg);
        ll_send_messg.setOnClickListener(this);

        //立即购买
        buyNowTextView = (TextView) findViewById(R.id.buy_now);
        buyNowTextView.setOnClickListener(this);

        //底部的购物车
        goodDetailShoppingCart = (ImageView) findViewById(R.id.good_detail_shopping_cart);
        goodDetailShoppingCart.setOnClickListener(this);
        shopping_cart_num = (TextView) findViewById(R.id.shopping_cart_num);
        shopping_cart_num_bg_one = (LinearLayout) findViewById(R.id.shopping_cart_num_bg_one);


//        if (!TextUtils.isEmpty(object_id)) {
//            addToCartTextView.setEnabled(false);
////            addToCartTextView.setBackgroundResource(R.color._999999);
//            addToCartTextView.setBackgroundColor(R.color._999999);
//        }
    }

    private void initView() {

        errorView = (ErrorView) findViewById(R.id.no_goods_undercarriage);
        initTopView();
        initBootomView();
        initViewPager();
        setShoppingcartNum();
    }

    private String[] titles;

    public GoodsViewPager viewPager;

    ProductFragment productFragment;

    private void initViewPager() {

        viewPager = (GoodsViewPager) findViewById(R.id.viewPager);
        mFragments = new ArrayList<>();
        productFragment = new ProductFragment();
        mFragments.add(productFragment);
        mFragments.add(new ProductDetailFragment());
        mFragments.add(new ProductCommonFragment());
        titles = new String[]{getResources().getString(R.string.goods_tab_goods), getResources().getString(R.string.goods_tab_details), getResources().getString(R.string.goods_tab_comment)};
        productPagerAdapter = new ProductDetailPagerAdapter(getSupportFragmentManager(), mFragments, Arrays.asList(titles));
        viewPager.setOffscreenPageLimit(productPagerAdapter.getCount());
        viewPager.setAdapter(productPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        topView.setupWithViewPager(viewPager);
    }

    public void addTablayoutListener() {
        viewPager.setScanScroll(true);
        topView.startDownAnimation();
    }

    public void removeTablayoutListener() {
        viewPager.setScanScroll(false);
        topView.startUpAnimation();
    }

    /**
     * 显示购物车数量
     */
    public void setShoppingcartNum() {
        if (mApp.getUser() == null || mApp.getGoodsNum() == 0) {
            shopping_cart_num_bg_one.setVisibility(View.GONE);
            shopping_cart_num.setVisibility(View.GONE);
        } else {
            shopping_cart_num_bg_one.setVisibility(View.VISIBLE);
            shopping_cart_num.setVisibility(View.VISIBLE);
            if (mApp.getGoodsNum() < 100) {
                shopping_cart_num.setText(mApp.getGoodsNum() + "");
            } else if (mApp.getGoodsNum() >= 100) {
                shopping_cart_num.setText("99+");
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            if (productFragment.noGoods) {
                finish();
            }

            if (viewPager.getCurrentItem() != 0) {
                viewPager.setCurrentItem(0);
                return true;
            }
            finish();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        setShoppingcartNum();
        currentTab(tab_id);

    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        GoodDetailDraft.getInstance().clear();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        productFragment.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 点击去分享
     */
    private void toShare() {
        new ShareHelper(GoodsDetailActivity.this, productFragment.dataModel).share();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {

        switch (url) {
            case ProtocolConst.GOODSDETAIL:
                break;
        }
    }


    @Override
    public void onEvent(Event event) {
        if (event.getMsg().equals("CART_UPDATE")) {
            cartModel.cartList(false);
        }
        if ("refresh_price".equals(event.getMsg())) {
            isLogin = true;
        }
    }

    /**
     * 去登录
     */
    void toLogin(int requsetCode) {
        Intent intent = new Intent(GoodsDetailActivity.this, LoginActivity.class);
        startActivityForResult(intent, requsetCode);
        overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
        String nol = resource.getString(R.string.no_login);
        ToastView toast = new ToastView(GoodsDetailActivity.this, nol);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 去购物车页面
     */
    public void toShopCart() {
        Intent it = new Intent(GoodsDetailActivity.this, ShoppingCartActivity.class);
        startActivityForResult(it, 6);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy_now://立即够买
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    toLogin(LOGIN_BALANCE);
                } else {
                    productFragment.cartCreate(true);
                }
                break;

            case R.id.add_to_cart://加入购物车
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    toLogin(LOGIN_ADDCART);
                } else {
                    productFragment.cartCreate(false);
                }
                break;
            case R.id.collection_button://收藏
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    toLogin(LOGIN_COLLECT);
                } else {
                    if (!productFragment.collectFlag) {
                        productFragment.collectCreate();
                    } else {
                        productFragment.collectDelete();
                    }
                }
                break;
            case R.id.good_detail_shopping_cart://去购物车
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    toLogin(LOGIN_SHOPCART);
                } else {
                    toShopCart();
                }
                break;
            case R.id.ll_send_messg://去聊天
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    toLogin(SEND_MESSAGE);
                } else {
                    productFragment.toConsult();
                }
                break;
        }
    }

    @Override
    public void currentTab(int tabId) {
        viewPager.setCurrentItem(tabId);
    }


    ErrorView errorView;

    public void showNoGoodsView(String s) {
        errorView.setErrorText(s);
        errorView.setVisibility(View.VISIBLE);
    }

    public void showViewPager() {
        findViewById(R.id.viewPager_parent).setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        if (AndroidManager.SUPPORT_SHARE) {
            topView.setRightType(ECJiaTopView.RIGHT_TYPE_IMAGE);
            topView.setRightImage(R.drawable.gooddetail_share, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toShare();
                }
            });
        }
    }
}
