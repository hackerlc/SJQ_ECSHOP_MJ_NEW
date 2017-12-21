package com.ecjia.view.activity.substation;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ecjia.base.NewBaseActivity;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.consts.SharedPKeywords;
import com.ecjia.entity.model.City;
import com.ecjia.entity.model.SimpleGoods;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.network.base.BaseRes;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.query.GoodsQuery;
import com.ecjia.network.query.PageQuery;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.view.activity.ShoppingCartActivity;
import com.ecjia.view.activity.user.LoginActivity;
import com.ecjia.widgets.ToastView;
import com.ecjia.base.ECJiaApplication;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.SubstationGoodslistActivityBinding;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;
import gear.yc.com.gearlibrary.utils.ConvertPadPlus;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;

import static android.R.attr.type;

/**
 * SJQ_ECSHOP_MJ_NEW
 * 有好货
 * Created by YichenZ on 2017/2/9 10:20.
 */

public class SubstationGoodsListActivity extends NewBaseActivity implements OnClickListener {
    SubstationGoodslistActivityBinding mBinding;

    SubstationGoodsListAdapter dataAdapter;
    GoodsQuery goodsQuery = new GoodsQuery();
    private List<SimpleGoods> goodsData;
    PageQuery mQuery;
    private int page = 1;

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    public String predefine_category_id;

    public boolean isFirst = false;
    private String category_id;

    FILTER filter = new FILTER();
    ArrayList<City> cities = new ArrayList<>();
    SubstationVPAdapter cityAdapter;
    SlidingMenu menu;
    private float y;
    private FrameLayout goodslistToolbar;
    private ViewPager vpSubstation;
    private FrameLayout flSubstationDiv;
    LinearLayout headLayout;
    public Handler Intenthandler;
    String city;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.substation_goodslist_activity);
        mBinding.setOnClick(this);
        PushAgent.getInstance(this).onAppStart();
        mQuery = new PageQuery();
        city = DEVICE.getInstance().getCity();
        initTitleView();
        initRecyclerView();
        initVPSubstation();

        category_id = getIntent().getStringExtra(IntentKeywords.CATEGORY_ID);

        if (!TextUtils.isEmpty(category_id)) {
            filter.setCategory_id(category_id);
            predefine_category_id = category_id;
        }

        shared = getSharedPreferences(SharedPKeywords.SPUSER, 0);
        editor = shared.edit();
        getCityData();
        getListData();
    }

    private void initRecyclerView() {
        GridLayoutManager lm = new GridLayoutManager(this, 2);
        mBinding.rlGoods.setLayoutManager(lm);

        goodsData = new ArrayList<>();
        dataAdapter = new SubstationGoodsListAdapter(this, goodsData);
        dataAdapter.setOnLoadMoreListener(() -> getListData());

        initAddListHeader();
        mBinding.rlGoods.setAdapter(dataAdapter.adapter());

    }

    private void initTitleView() {
        mBinding.icHead.tvTitle.setText("有好货");
    }

    private void initAddListHeader() {
        headLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.list_head_substation, null);
        goodslistToolbar = (FrameLayout) headLayout.findViewById(R.id.goodslist_toolbar);
        flSubstationDiv = (FrameLayout) headLayout.findViewById(R.id.fl_substation_div);
        vpSubstation = (ViewPager) headLayout.findViewById(R.id.vp_substation);

        dataAdapter.addHeaderView(headLayout);
    }

    private void initVPSubstation() {
        vpSubstation.setOffscreenPageLimit(3);
        vpSubstation.setPageMargin(ConvertPadPlus.dip2px(this, 40));

        flSubstationDiv.setOnTouchListener((v, e) -> vpSubstation.dispatchTouchEvent(e));

        cityAdapter = new SubstationVPAdapter(this, cities, (v, pos) -> {
            SharedPreferences.Editor edit = shared.edit();
            edit.putString(SharedPKeywords.LOCAL_NAME, ECJiaApplication.cityName);
            edit.putString(SharedPKeywords.LOCAL_ALIAS, DEVICE.getInstance().getCity());
            edit.commit();
            RxBus.getInstance().post(RxBus.TAG_CHANGE, "city");
            RxBus.getInstance().post(RxBus.TAG_UPDATE, -1);
            finish();
        });

        vpSubstation.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page = 1;
                city = cities.get(position).getAlias();
                getListData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vpSubstation.setAdapter(cityAdapter);
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
                    Intent intent = new Intent(SubstationGoodsListActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_buttom_in,
                            R.anim.push_buttom_out);
                    Resources resource = (Resources) getBaseContext()
                            .getResources();
                    String nol = resource.getString(R.string.no_login);
                    ToastView toast = new ToastView(SubstationGoodsListActivity.this, nol);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    Intent it = new Intent(SubstationGoodsListActivity.this,
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

    @Override
    protected void onPause() {
        super.onPause();
        editor.putInt("goodlist_type", type);
        editor.commit();
    }

    private void getListData() {
        ProgressDialogUtil.getInstance().build(this).show();
        RetrofitAPIManager.getAPIGoods()
                .getSubWeb(goodsQuery.getSubWeb(page, city))
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

    public void getCityData() {
        RetrofitAPIManager.getAPIPublic()
                .getSubstations(mQuery.getQuery(1, 100))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(d -> showCityData(d), e -> showError(e));
    }

    private void showCityData(List<City> datas) {
        if (datas != null && datas.size() > 0) {
            cities.clear();
            cities.addAll(datas);
            cityAdapter.notifyDataSetChanged();

            for (int i = 0; i < cities.size(); i++) {
                if (ECJiaApplication.cityName.equals(cities.get(i).getName())) {
                    vpSubstation.setCurrentItem(i);
                }
            }
        }
    }
}

