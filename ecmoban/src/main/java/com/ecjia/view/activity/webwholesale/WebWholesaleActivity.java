package com.ecjia.view.activity.webwholesale;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ecjia.base.NewBaseActivity;
import com.ecjia.consts.SharedPKeywords;
import com.ecjia.entity.model.Category;
import com.ecjia.entity.model.SimpleGoods;
import com.ecjia.entity.model.WholesaleTop;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.network.base.BaseRes;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.query.GoodsQuery;
import com.ecjia.network.query.MainQuery;
import com.ecjia.network.query.PageQuery;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.view.activity.ShoppingCartActivity;
import com.ecjia.view.activity.user.LoginActivity;
import com.ecjia.widgets.ToastView;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.SubstationGoodslistActivityBinding;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;

/**
 * SJQ_ECSHOP_MJ_NEW
 * 新网供
 * Created by YichenZ on 2017/2/9 10:20.
 */

public class WebWholesaleActivity extends NewBaseActivity implements OnClickListener {
    SubstationGoodslistActivityBinding mBinding;

    WebWholesaleAdapter dataAdapter;
    GoodsQuery goodsQuery = new GoodsQuery();
    private List<SimpleGoods> goodsData;
    PageQuery mQuery;
    MainQuery mainQuery;
    int page = 1;
    String type = "", catName = "";

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    public boolean isFirst = true;

    SlidingMenu menu;
    private float y;
    LinearLayout headLayout;
    public Handler Intenthandler;
    String city;

    FILTER mFILTER;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.substation_goodslist_activity);
        mBinding.setOnClick(this);
        PushAgent.getInstance(this).onAppStart();
        mQuery = new PageQuery();
        mainQuery = new MainQuery();
        mFILTER = new FILTER();
        city = DEVICE.getInstance().getCity();
        isFirst = true;
        initTitleView();
        initRecyclerView();


        shared = getSharedPreferences(SharedPKeywords.SPUSER, 0);
        editor = shared.edit();
        loadTopData();
        getListData();
    }

    private void initRecyclerView() {
        GridLayoutManager lm = new GridLayoutManager(this, 2);
        mBinding.rlGoods.setLayoutManager(lm);

        goodsData = new ArrayList<>();
        dataAdapter = new WebWholesaleAdapter(this, goodsData);
        dataAdapter.setOnLoadMoreListener(() -> getListData());

        initAddListHeader();
        mBinding.rlGoods.setAdapter(dataAdapter.adapter());

    }

    private void initTitleView() {
        mBinding.icHead.tvTitle.setText("新网供");
    }

    ImageView banner;
    ImageView bannerLine;
    private View llTab;
    private TabLayout tabLayout;

    private void initAddListHeader() {
        headLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_activity_banner, null);
        banner = (ImageView) headLayout.findViewById(R.id.iv_banner);
        bannerLine = (ImageView) headLayout.findViewById(R.id.iv_banner_line);
        bannerLine.setVisibility(View.GONE);
        dataAdapter.addHeaderView(headLayout);

        llTab = LayoutInflater.from(this).inflate(R.layout.layout_tab, null);
        tabLayout = (TabLayout) llTab.findViewById(R.id.tl_tablayout);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mFILTER.setCategory_id(tab.getTag().toString());
                mFILTER.setIs_one("1");//新网供是一件代发下的商品
                ProgressDialogUtil.getInstance().build(WebWholesaleActivity.this).show();
                page = 1;
                getListData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        dataAdapter.addHeaderView(llTab);
    }

    public void setContent() {
        if (goodsData.size() == 0) {
            mBinding.goodslistBg.setVisibility(View.GONE);
            mBinding.nullPager.setVisibility(View.VISIBLE);
        } else {
            mBinding.goodslistBg.setVisibility(View.GONE);
            mBinding.nullPager.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_button://返回按钮
                EventBus.getDefault().post(new MyEvent("search_back"));
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.good_list_shopping_cart://购物车按钮
                String uid = shared.getString(SharedPKeywords.SPUSER_KUID, "");
                if (uid.equals("")) {
                    Intent intent = new Intent(WebWholesaleActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_buttom_in,
                            R.anim.push_buttom_out);
                    Resources resource = (Resources) getBaseContext()
                            .getResources();
                    String nol = resource.getString(R.string.no_login);
                    ToastView toast = new ToastView(WebWholesaleActivity.this, nol);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    Intent it = new Intent(WebWholesaleActivity.this,
                            ShoppingCartActivity.class);
                    startActivity(it);
                }
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (menu != null && menu.isMenuShowing()) {
                menu.toggle();
            } else {
                finish();
                overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
                return false;
            }
        }
        return true;
    }

    List<Category> category = new ArrayList<>();

    private void showTopData(WholesaleTop wholesaleTop) {
        if(wholesaleTop.getBanners()!=null && wholesaleTop.getBanners().size()>0){
            ImageLoaderForLV.getInstance(this).setImageRes(banner,wholesaleTop.getBanners().get(0).getImg());
        }

        category.clear();
        category.addAll(wholesaleTop.getCategory());
        for (int i = 0; i < category.size(); i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(category.get(i).getCatName());
            tab.setTag(category.get(i).getCategoryId());
            tabLayout.addTab(tab);
        }
    }


    private void loadTopData() {
        ProgressDialogUtil.getInstance().build(this).show();
        RetrofitAPIManager.getAPIPublic()
                .getWholesaleTop(mainQuery.getWTop(2))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .subscribe(d -> showTopData(d), e -> showError(e));
    }

    private void getListData() {
        RetrofitAPIManager.getAPIGoods()
                .getGoodsList(goodsQuery.getQuery(page,mFILTER))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResultJsonRet())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(d -> {
                            pageHandler(d.getPaginated());
                            showGoodsData(d.getData());
                        },
                        e -> showError(e));
    }


    private void pageHandler(BaseRes.Paginated p) {
        if (p.getMore() == 0) {
            dataAdapter.noMore(true);
        } else {
            dataAdapter.noMore(false);
        }
    }

    private void showGoodsData(List<SimpleGoods> obj) {
        if (obj != null) {
            if (page == 1) {
                goodsData.clear();
            }
            goodsData.addAll(obj);
            dataAdapter.notifyDataSetChanged();
            page++;
        } else {
            ToastUtil.getInstance().makeShortToast(this, "服务器异常请重新加载");
        }
    }

    private void showError(Throwable error) {
        ToastUtil.getInstance().makeLongToast(this, "暂无数据");
    }

}

