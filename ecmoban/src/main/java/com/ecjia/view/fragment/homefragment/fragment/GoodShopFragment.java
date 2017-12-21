package com.ecjia.view.fragment.homefragment.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.*;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.ecjia.network.netmodle.HomeModel;
import com.ecjia.network.netmodle.SellerModel;
import com.ecmoban.android.sijiqing.R;

import com.ecjia.widgets.MyXListView;
import com.ecjia.consts.AppConst;

import com.ecjia.consts.ProtocolConst;


import com.ecjia.view.adapter.HomeShopListAdapter;
import com.ecjia.base.BaseFragment;
import com.ecjia.view.fragment.HomeFragment;
import com.ecjia.widgets.home.EventView;
import com.ecjia.widgets.home.GoodShopView;
import com.ecjia.widgets.home.GroupBuyView;
import com.ecjia.widgets.home.MobileBuyView;
import com.ecjia.widgets.home.NewBannerView;
import com.ecjia.widgets.home.NewGoodsView;
import com.ecjia.widgets.home.PromotionView;
import com.ecjia.widgets.home.QuickEnter;
import com.ecjia.widgets.home.ThemeStreets;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.PAGINATED;

import com.ecjia.entity.responsemodel.SELLERINFO;

import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.EventBus.MyEvent;

import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

@SuppressLint("ValidFragment")
public class GoodShopFragment extends BaseFragment implements MyXListView.IXListViewListener, AppConst.RegisterApp, HttpResponse {
    private MyXListView mListView;
    private HomeModel dataModel;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    private String uid;
    boolean isRefresh = false;

    private View mainView;

    private ImageView back_top;
    private boolean scrollFlag;
    private boolean back_top_vis;
    private int lastVisibleItemPosition;

    private SellerModel sellerModel;

    private HomeShopListAdapter homeSellerAdapter;


    private Activity mActivity;
    private ThemeStreets themeStreets;
    private NewBannerView bannerViews;
    private PromotionView promotionView;
    private MobileBuyView mobileBuy;
    private QuickEnter quickEnter;
    private NewGoodsView newGoods;
    private GroupBuyView groupbuy;
    private EventView eventView;
    HomeFragment parentFragment;
    @SuppressLint("ValidFragment")
    public GoodShopFragment(HomeFragment parentFragment){
        this.parentFragment = parentFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        mActivity = activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shared = mActivity.getSharedPreferences("userInfo", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (null != mainView) {
            ViewGroup parent = (ViewGroup) mainView.getParent();
            if (null != parent) {
                parent.removeView(mainView);
            }
        } else {
            mainView = inflater.inflate(R.layout.fragment_good_shop, null);

            initBackToTop();
            initXlistView();
            /**
             *
             */
            initConfig();

            initHeadView();

            controlViewVisible();
            if (sellerModel.paginated != null) {
                if (sellerModel.paginated.getMore() == 0) {
                    mListView.setPullLoadEnable(false);
                } else {
                    mListView.setPullLoadEnable(true);
                }
            }
        }

        return mainView;
    }

    private void initConfig() {
        if (null == dataModel) {
            dataModel = new HomeModel(mActivity);
            dataModel.addResponseListener(this);
        }

        dataModel.fetchHotSelling();

        if (null == sellerModel) {
            sellerModel = new SellerModel(mActivity);
            sellerModel.addResponseListener(this);
        }

        sellerModel.getSellerList("");
        homeSellerAdapter = new HomeShopListAdapter(mActivity, sellerModel.sellerinfos, getDisplayMetricsWidth1());
    }

    private void initXlistView() {
        mListView = (MyXListView) mainView.findViewById(R.id.home_listview);
        mListView.setPullLoadEnable(true, true);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this, 0);
        mListView.setRefreshTime();
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        scrollFlag = false;
                        // 判断滚动到底部
                        if (mListView.getLastVisiblePosition() == (mListView.getCount() - 1)) {
                            back_top.setVisibility(View.VISIBLE);
                            back_top_vis = true;
                        }
                        // 判断滚动到顶部
                        if (mListView.getFirstVisiblePosition() == 0) {
                            back_top.setVisibility(View.GONE);
                            back_top_vis = false;
                        }
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                        scrollFlag = true;
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                        scrollFlag = false;
                        break;
                }
            }

            /**
             * firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
             * visibleItemCount：当前能看见的列表项个数（小半个也算） totalItemCount：列表项共数
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 当开始滑动且ListView底部的Y轴点超出屏幕最大范围时，显示或隐藏顶部按钮
                if (scrollFlag) {
                    if (firstVisibleItem > lastVisibleItemPosition) {// 上滑
                        back_top.setVisibility(View.VISIBLE);
                        back_top_vis = true;
                    } else if (firstVisibleItem < lastVisibleItemPosition) {// 下滑

                    } else {
                        return;
                    }
                    lastVisibleItemPosition = firstVisibleItem;
                }
            }
        });

    }


    void initBackToTop() {
        /**
         * 回到顶部
         */
        back_top = (ImageView) mainView.findViewById(R.id.back_top);
        scrollFlag = false;
        lastVisibleItemPosition = 0;
        if (back_top_vis) {
            back_top.setVisibility(View.VISIBLE);
        } else {
            back_top.setVisibility(View.GONE);
        }

        back_top.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.setSelection(0);
                back_top.setVisibility(View.GONE);
                back_top_vis = false;
                scrollFlag = false;
                lastVisibleItemPosition = 0;
            }
        });
    }

    //初始化头部
    private void initHeadView() {

        //头部轮播图
        initBannerView();

        // 添加快速进入
        initQuickView();

        //主题街
        initTheme();

        //添加活动
        initEvent();

        //添加团购布局
        initGroupGoods();

        //促销商品
        initPromotion();

        //手机专享商品
        initMobileBuy();

        //添加新品上架
        initNewGoodsPutAway();

        //发现好店
        initGoodShopView();

    }

    /**
     * 添加头部轮播图布局
     */
    private void initBannerView() {
        bannerViews = new NewBannerView(mActivity);
        bannerViews.addToListView(mListView);
    }

    GoodShopView goodShopView;

    private void initGoodShopView() {
        goodShopView = new GoodShopView(mActivity);
        goodShopView.addToListView(mListView);
    }

    private void initQuickView() {
        quickEnter = new QuickEnter(mActivity);
        quickEnter.addToListView(mListView);
    }

    private void initNewGoodsPutAway() {
        newGoods = new NewGoodsView(mActivity);
        newGoods.addToListView(mListView);
    }

    private void initMobileBuy() {
        mobileBuy = new MobileBuyView(mActivity);
        mobileBuy.addToListView(mListView);
    }

    private void initPromotion() {
        promotionView = new PromotionView(mActivity);
        promotionView.addToListView(mListView);
    }

    private void initTheme() {
        themeStreets = new ThemeStreets(mActivity);
        themeStreets.addToListView(mListView);
    }


    private void initEvent() {
        eventView = new EventView(mActivity);
        eventView.addToListView(mListView);
    }

    private void initGroupGoods() {
        groupbuy = new GroupBuyView(mActivity);
        groupbuy.addToListView(mListView);
    }


    public boolean isActive = false;

    @Override
    public void onResume() {
        super.onResume();
        if (!isActive) {
            isActive = true;
            AppConst.registerApp(this);
        }
        uid = shared.getString("uid", "");
        MobclickAgent.onPageStart("Home");


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void controlViewVisible() {
        bannerViews.createView(dataModel.playersList);
        quickEnter.createView(dataModel.quicklist);
        themeStreets.createView(dataModel.home_theme_list);
        eventView.createView(dataModel.adsenseGroup);
        groupbuy.createView(dataModel.groupgoodsArrayList);
        promotionView.createView(dataModel.simplegoodsList);
        mobileBuy.createView(dataModel.mobilegoodsArrayList);
        newGoods.createView(dataModel.newGoodsList);
        mListView.setAdapter(homeSellerAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onRefresh(int id) {
        isRefresh = true;
        mListView.setPullLoadEnable(true, true);
        dataModel.fetchHotSelling();
    }

    @Override
    public void onLoadMore(int id) {
        sellerModel.getSellerListMore("");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("Home");
    }


    @Override
    public void onEvent(MyEvent event) {

        if ("add_collect_seller".equals(event.getMsg())) {
            for (SELLERINFO sellerinfo : dataModel.home_sellerlist) {
                if (sellerinfo.getId().equals(event.getValue())) {
                    sellerinfo.setFollower(sellerinfo.getFollower() + 1);
                    homeSellerAdapter.notifyDataSetChanged();
                    break;
                }
            }
        } else if ("minus_collect_seller".equals(event.getMsg())) {
            for (SELLERINFO sellerinfo : dataModel.home_sellerlist) {
                if (sellerinfo.getId().equals(event.getValue())) {
                    sellerinfo.setFollower(sellerinfo.getFollower() - 1);
                    homeSellerAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    @Override
    public void onRegisterResponse(boolean success) {

    }

    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = mActivity.getWindowManager().getDefaultDisplay().getWidth();
        int j = mActivity.getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            // app 进入后台
            isActive = false;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) mActivity.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = mActivity.getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.SELLER_LIST) {
            if (status.getSucceed() == 1) {
                mListView.stopLoadMore();
                mListView.stopRefresh();
                mListView.setRefreshTime();//更新刷新时间

                PAGINATED paginated = sellerModel.paginated;
                if (paginated.getMore() == 0) {
                    mListView.setPullLoadEnable(false);
                } else {
                    mListView.setPullLoadEnable(true);
                }

                if (sellerModel.sellerinfos.size() == 0) {
                    goodShopView.createView(null);
                } else {
                    goodShopView.createView(sellerModel.sellerinfos);
                }

                homeSellerAdapter.notifyDataSetChanged();
            }

        } else if (url.equals(ProtocolConst.HOMEDATA)) {
            if (status.getSucceed() == 1) {
                mListView.stopRefresh();
                mListView.setRefreshTime();

                bannerViews.createView(dataModel.playersList);

                quickEnter.createView(dataModel.quicklist);

                themeStreets.createView(dataModel.home_theme_list);

                eventView.createView(dataModel.adsenseGroup);

                groupbuy.createView(dataModel.groupgoodsArrayList);

                promotionView.createView(dataModel.simplegoodsList);

                mobileBuy.createView(dataModel.mobilegoodsArrayList);

                newGoods.createView(dataModel.newGoodsList);

                sellerModel.getSellerList("");

                bannerViews.startPlay();
            }
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    public int getScrollY() {
        View c = mListView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = mListView.getFirstVisiblePosition();
        int top = c.getTop();
        if (firstVisiblePosition > 1) {
            return 2000;
        } else {
            return -top + firstVisiblePosition * c.getHeight();
        }
    }

    public int getDisplayMetricsWidth1() {
        int i = mActivity.getWindowManager().getDefaultDisplay().getWidth();
        int j = mActivity.getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j) - ((int) getResources().getDimension(R.dimen.eight_margin)) * 2;
    }


}
