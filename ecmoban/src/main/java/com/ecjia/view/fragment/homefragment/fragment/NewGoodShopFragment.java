package com.ecjia.view.fragment.homefragment.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.ecjia.base.NewBaseFragment;
import com.ecjia.consts.AppConst;
import com.ecjia.entity.model.MainData;
import com.ecjia.entity.model.ShopInfo;
import com.ecjia.network.base.BaseRes;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.query.MainQuery;
import com.ecjia.network.query.ShopQuery;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.view.fragment.homefragment.adapter.NewHomeShopListAdapter;
import com.ecjia.widgets.MyXListView;
import com.ecjia.widgets.home.NewBannerView;
import com.ecjia.widgets.home.NewEventView;
import com.ecjia.widgets.home.NewMarketView;
import com.ecjia.widgets.home.NewPromotionView;
import com.ecjia.widgets.home.NewQuickEnter;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.FragmentGoodShopBinding;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;
import gear.yc.com.gearlibrary.rxjava.rxbus.annotation.Subscribe;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;

@SuppressLint("ValidFragment")
public class NewGoodShopFragment extends NewBaseFragment implements MyXListView.IXListViewListener, AppConst.RegisterApp {
    FragmentGoodShopBinding mBinding;

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    boolean isRefresh = false;

    private View mainView;


    private boolean scrollFlag;
    private boolean back_top_vis;
    private int lastVisibleItemPosition;

    private List<ShopInfo> mShopInfos = new ArrayList<>();
    private ShopQuery mShopQuery;
    private int page = 1;

    private NewHomeShopListAdapter homeSellerAdapter;


    private Activity mActivity;
    private NewBannerView bannerViews;
    private NewPromotionView promotionView;
    private NewQuickEnter quickEnter;
    private NewMarketView newGoods;
    private NewEventView eventView;
//    NewHomeFragment parentFragment;

    MainQuery mMainQuery;
//
//    @SuppressLint("ValidFragment")
//    public NewGoodShopFragment(NewHomeFragment parentFragment) {
//        this.parentFragment = parentFragment;
//    }

    @Override
    public void onAttach(Activity activity) {
        mActivity = activity;
        RxBus.getInstance().register(this);
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shared = mActivity.getSharedPreferences("userInfo", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_good_shop, container, false);
        if (null != mainView) {
            ViewGroup parent = (ViewGroup) mainView.getParent();
            if (null != parent) {
                parent.removeView(mainView);
            }
        } else {
            mainView = mBinding.getRoot();

            initBackToTop();
            initXlistView();

            initConfig();

            initHeadView();

            controlViewVisible();
        }

        return mainView;
    }

    private void initConfig() {
        mMainQuery = new MainQuery();
        mShopQuery = new ShopQuery();
        loadData();
        loadSellers();
        homeSellerAdapter = new NewHomeShopListAdapter(mActivity, mShopInfos, getDisplayMetricsWidth1());
    }

    private void loadData() {
        ProgressDialogUtil.getInstance().build(getContext()).show();
        RetrofitAPIManager.getAPIPublic()
                .getMainData(mMainQuery.getQuery())
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(d -> showMainData(d), e -> showError(e));
    }

    private void loadSellers() {
        RetrofitAPIManager.getAPIShop()
                .getSellers(mShopQuery.getQuery(page,3))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResultJsonRet())
                .subscribe(d -> showListData(d), e -> showError(e));
    }

    private void initXlistView() {
        mBinding.homeListview.setPullLoadEnable(true, true);
        mBinding.homeListview.setPullRefreshEnable(true);
        mBinding.homeListview.setXListViewListener(this, 0);
        mBinding.homeListview.setRefreshTime();
        mBinding.homeListview.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        scrollFlag = false;
                        // 判断滚动到底部
                        if (mBinding.homeListview.getLastVisiblePosition() == (mBinding.homeListview.getCount() - 1)) {
                            mBinding.backTop.setVisibility(View.VISIBLE);
                            back_top_vis = true;
                        }
                        // 判断滚动到顶部
                        if (mBinding.homeListview.getFirstVisiblePosition() == 0) {
                            mBinding.backTop.setVisibility(View.GONE);
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
                        mBinding.backTop.setVisibility(View.VISIBLE);
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
        scrollFlag = false;
        lastVisibleItemPosition = 0;
        if (back_top_vis) {
            mBinding.backTop.setVisibility(View.VISIBLE);
        } else {
            mBinding.backTop.setVisibility(View.GONE);
        }

        mBinding.backTop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.homeListview.setSelection(0);
                mBinding.backTop.setVisibility(View.GONE);
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

        //添加活动
        initEvent();

        //限时团批
        initPromotion();

        //实体市场
        initNewMarket();
    }

    /**
     * 添加头部轮播图布局
     */
    private void initBannerView() {
        bannerViews = new NewBannerView(mActivity);
        bannerViews.addToListView(mBinding.homeListview);
    }

    private void initQuickView() {
        quickEnter = new NewQuickEnter(mActivity);
        quickEnter.addToListView(mBinding.homeListview);
    }

    private void initNewMarket() {
        newGoods = new NewMarketView(mActivity);
        newGoods.addToListView(mBinding.homeListview);
    }

    private void initPromotion() {
        promotionView = new NewPromotionView(mActivity);
        promotionView.addToListView(mBinding.homeListview);
    }

    private void initEvent() {
        eventView = new NewEventView(mActivity);
        eventView.addToListView(mBinding.homeListview);
    }

    public boolean isActive = false;

    @Override
    public void onResume() {
        super.onResume();
        if (!isActive) {
            isActive = true;
            AppConst.registerApp(this);
        }
        MobclickAgent.onPageStart("Home");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void controlViewVisible() {
        mBinding.homeListview.setAdapter(homeSellerAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        RxBus.getInstance().unRegister(this);
        super.onDestroy();
    }

    @Subscribe(tag = RxBus.TAG_UPDATE)
    public void onRefresh(int id) {
        mBinding.homeListview.setPullLoadEnable(true, true);
        page = 1;
        loadData();
        loadSellers();
    }

    @Override
    public void onLoadMore(int id) {
        loadSellers();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("Home");
    }

    public void onEvent(MyEvent event) {
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

    private void showMainData(MainData mainData) {
        mBinding.homeListview.stopRefresh();
        mBinding.homeListview.setRefreshTime();

        bannerViews.createView(mainData.getPlayer());

        quickEnter.createView(mainData.getMobile_menu());


        eventView.createView(mainData.getActivity_topic());


        promotionView.createView(mainData.getTime_group());


        newGoods.createView(mainData.getYun_market());

        bannerViews.startPlay();
    }

    private void showListData(BaseRes<List<ShopInfo>> data) {
        List<ShopInfo> shopInfos = data.getData();
        mBinding.homeListview.stopLoadMore();
        mBinding.homeListview.stopRefresh();
        mBinding.homeListview.setRefreshTime();//更新刷新时间

        if (data.getPaginated().getMore() == 0) {
            mBinding.homeListview.setPullLoadEnable(false);
        } else {
            mBinding.homeListview.setPullLoadEnable(true);
        }
        if (page == 1) {
            mShopInfos.clear();
        }
        mShopInfos.addAll(shopInfos);
        page++;
        homeSellerAdapter.notifyDataSetChanged();
    }

    private void showError(Throwable e) {
        ToastUtil.getInstance().makeShortToast(getContext(), e.getMessage());
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    public int getScrollY() {
        View c = mBinding.homeListview.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = mBinding.homeListview.getFirstVisiblePosition();
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
