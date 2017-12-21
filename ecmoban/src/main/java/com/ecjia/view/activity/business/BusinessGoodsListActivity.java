package com.ecjia.view.activity.business;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ecjia.base.NewBaseActivity;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.consts.SharedPKeywords;
import com.ecjia.entity.model.SimpleGoods;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.query.GoodsQuery;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.view.activity.SearchNewActivity;
import com.ecjia.view.activity.ShoppingCartActivity;
import com.ecjia.view.activity.user.LoginActivity;
import com.ecjia.view.fragment.FilterListFragment;
import com.ecjia.widgets.ToastView;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.NewBusinessGoodslistActivityBinding;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umeng.message.PushAgent;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;

import static android.R.attr.type;

/**
 * SJQ_ECSHOP_MJ_NEW
 * 商家促销
 * Created by YichenZ on 2017/2/9 10:20.
 */

public class BusinessGoodsListActivity extends NewBaseActivity implements OnClickListener {
    NewBusinessGoodslistActivityBinding mBinding;

    BusinessGoodsListRecyclerAdapter dataAdapter;
    GoodsQuery goodsQuery = new GoodsQuery();
    private List<SimpleGoods> goodsData;

    private int page = 1;

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    public static final String CATEGORY_ID = "category_id";

    public static String PRICE_DESC = "price_desc";
    public static String PRICE_ASC = "price_asc";
    public static String IS_HOT = "is_hot";
    public static String IS_NEW = "is_new";

    public static final int IS_HOT_INT = 0;
    public static final int PRICE_DESC_INT = 1;
    public static final int PRICE_ASC_INT = 2;
    public static final int IS_NEW_INT = -1;

    public static final int IS_FILTER = 3;

    public String predefine_category_id;

    public boolean isFirst = false;
    private String category_id;

    FILTER filter = new FILTER();

    SlidingMenu menu;

    private float y;
    private String keyword;

    private LinearLayout flFilter;
    private FilterListFragment flFragment;
    LinearLayout headLayout;
    ImageView headImg;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.new_business_goodslist_activity);
        mBinding.setOnClick(this);
        PushAgent.getInstance(this).onAppStart();

        initTitleView();
        initRecyclerView();

        category_id = getIntent().getStringExtra(IntentKeywords.CATEGORY_ID);

        if (!TextUtils.isEmpty(category_id)) {
            filter.setCategory_id(category_id);
            predefine_category_id = category_id;
        }

        shared = getSharedPreferences(SharedPKeywords.SPUSER, 0);
        editor = shared.edit();

        selectedTab(IS_NEW_INT);
    }

    private void initRecyclerView() {
        GridLayoutManager lm = new GridLayoutManager(this,2);
        mBinding.rlGoods.setLayoutManager(lm);

        goodsData = new ArrayList<>();
        dataAdapter = new BusinessGoodsListRecyclerAdapter(this, goodsData);
        dataAdapter.setOnLoadMoreListener(() -> getListData());
        dataAdapter.setOnItemClickListener((view, holder, position) -> {
            Intent intent = new Intent(this, GoodsDetailActivity.class);
            SimpleGoods simpleGoods = goodsData.get(position);
            intent.putExtra(IntentKeywords.GOODS_ID, simpleGoods.getGoods_id());
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        });
        initAddListHeader();
        mBinding.rlGoods.setAdapter(dataAdapter.adapter());

    }

    private void initTitleView() {
        mBinding.icHead.searchInput.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mBinding.icHead.searchInput.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        mBinding.icHead.searchInput.setFocusable(false);

        keyword = getIntent().getStringExtra(IntentKeywords.KEYWORDS);
        if (StringUtils.isNotEmpty(keyword)) {
            mBinding.icHead.searchInput.setText(keyword);
            filter.setKeywords(keyword);
        }
    }

    private void initAddListHeader() {
        headLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.list_head_business, null);
        headImg = (ImageView) headLayout.findViewById(R.id.iv_activity);
        flFilter = (LinearLayout) headLayout.findViewById(R.id.fl_filter);

        dataAdapter.addHeaderView(headLayout);
    }

    private void initFilterFragment() {
        if (flFragment == null) {
            flFragment = new FilterListFragment(R.string.filter_all, R.string.filter_full_reduction,  R.string.filter_free_postage);
            flFragment.getTabOneCellHolder().setTitleCellOnClickListener(v -> selectedTab(IS_NEW_INT));
            flFragment.getTabTwoCellHolder().setTitleCellOnClickListener(v -> selectedTab(IS_HOT_INT));
            flFragment.getTabThreeCellHolder().setTitleCellOnClickListener(v -> selectedTab(PRICE_DESC_INT));
            flFragment.getTabFourCellHolder().show=View.GONE;
            FragmentManager fm =getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            try {
                ft.replace(flFilter.getId(), flFragment).commit();
            } catch (NullPointerException e) {
                ToastUtil.getInstance().makeShortToast(this, "暂无数据");
            }
        }
    }

    void selectedTab(int index) {
        if (index == IS_NEW_INT) {
            filter.setSort_by(IS_NEW);
        } else if (index == IS_HOT_INT) {
            filter.setSort_by(IS_HOT);
        } else if (index == PRICE_DESC_INT) {
            filter.setSort_by(PRICE_ASC);
        } else if (index == IS_FILTER) {
            filter.setSort_by(PRICE_DESC);
        }
        page = 1;
        getListData();
    }

    public void setContent() {
        if (goodsData.size() == 0) {
            mBinding.goodslistBg.setVisibility(View.GONE);
            mBinding.nullPager.setVisibility(View.VISIBLE);
        } else {
            mBinding.goodslistBg.setVisibility(View.GONE);
            mBinding.nullPager.setVisibility(View.GONE);
        }

        updateCartNum();
    }

    private void updateCartNum() {
        String uid = shared.getString(SharedPKeywords.SPUSER_KUID, "");
        if ("".equals(uid) || mApp.getGoodsNum() == 0) {
            mBinding.shoppingCartNum.setVisibility(View.GONE);
            mBinding.shoppingCartNumBgOne.setVisibility(View.GONE);
        } else {
            mBinding.shoppingCartNum.setVisibility(View.VISIBLE);
            mBinding.shoppingCartNumBgOne.setVisibility(View.VISIBLE);
            if (mApp.getGoodsNum() < 10) {
                mBinding.setCarNum(mApp.getGoodsNum() + "");
            } else if (mApp.getGoodsNum() < 100 && mApp.getGoodsNum() > 9) {
                mBinding.setCarNum(mApp.getGoodsNum() + "");
            } else if (mApp.getGoodsNum() > 99) {
                mBinding.setCarNum("99+");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_input://搜索输入框
                if (TextUtils.isEmpty(keyword)) {
                    Intent intent = new Intent();
                    intent.setClass(BusinessGoodsListActivity.this, SearchNewActivity.class);
                    intent.putExtra("filter", BusinessGoodsListActivity.this.filter.toString());
                    startActivityForResult(intent, 100);
                } else {
                    finish();
                }
                break;
            case R.id.nav_back_button://返回按钮
                EventBus.getDefault().post(new MyEvent("search_back"));
                CloseKeyBoard();
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.good_list_shopping_cart://购物车按钮
                String uid = shared.getString(SharedPKeywords.SPUSER_KUID, "");
                CloseKeyBoard();
                if (uid.equals("")) {
                    Intent intent = new Intent(BusinessGoodsListActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_buttom_in,
                            R.anim.push_buttom_out);
                    Resources resource = (Resources) getBaseContext()
                            .getResources();
                    String nol = resource.getString(R.string.no_login);
                    ToastView toast = new ToastView(BusinessGoodsListActivity.this, nol);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    Intent it = new Intent(BusinessGoodsListActivity.this,
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: {
                if (null != data) {
                    String filter_string = data.getStringExtra("filter");
                    if (null != filter_string) {
                        try {
                            JSONObject filterJSONObject = new JSONObject(
                                    filter_string);
                            FILTER filter = FILTER
                                    .fromJson(filterJSONObject);
                            this.filter.setCategory_id(filter.getCategory_id());
                            this.filter.setPrice_range(filter.getPrice_range());
                            this.filter.setBrand_id(filter.getBrand_id());
                            getListData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
        TranslateAnimation animation = new TranslateAnimation(0, 0, -y, 0);
        ScaleAnimation animation1 = new ScaleAnimation(0.85f, 1f, 1f, 1f);
        TranslateAnimation animation2 = new TranslateAnimation(-150, 0, 0, 0);
        animation.setDuration(200);
        animation1.setDuration(150);
        animation2.setDuration(150);
        animation.setFillAfter(true);
        animation1.setFillAfter(true);
        animation2.setFillAfter(true);
        mBinding.icHead.searchSearch.startAnimation(animation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartNum();
    }

    // 关闭键盘
    public void CloseKeyBoard() {
        String s = getIntent().getStringExtra(IntentKeywords.KEYWORDS);
        if (StringUtils.isEmpty(s)) {
            mBinding.icHead.searchInput.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mBinding.icHead.searchInput.getWindowToken(), 0);
        }
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
                .getBusinessGoodsList(goodsQuery.getQuery(page, filter))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(d -> showGoodsData(d),
                        e -> showError(e));
    }

    private void showGoodsData(List<SimpleGoods> obj) {
        if (obj != null) {
            if (page == 1) {
                if(obj.size()==0){
                    setContent();
                }
                goodsData.clear();
            }
            goodsData.addAll(obj);
            dataAdapter.notifyDataSetChanged();
            page++;
            initFilterFragment();
        } else {
            ToastUtil.getInstance().makeShortToast(this,"服务器异常请重新加载");
        }
    }

    private void showError(Throwable error) {
        ToastUtil.getInstance().makeLongToast(this, "暂无数据");
    }
}

