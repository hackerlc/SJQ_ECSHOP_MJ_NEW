package com.ecjia.view.activity.newwholesale;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.ecjia.base.NewBaseActivity;
import com.ecjia.entity.model.Category;
import com.ecjia.entity.model.ShopInfo;
import com.ecjia.entity.model.WholesaleTop;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.entity.responsemodel.PLAYER;
import com.ecjia.network.base.BaseRes;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.query.MainQuery;
import com.ecjia.network.query.ShopQuery;
import com.ecjia.view.activity.user.LoginActivity;
import com.ecjia.widgets.home.NewBannerView;
import com.ecjia.base.ECJiaApplication;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.ActivityGoodShopBinding;

import java.util.ArrayList;
import java.util.List;

import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.ui.custom.GearRecyclerItemDecoration;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;

import static com.ecjia.util.ecjiaopentype.ECJiaOpenType.ECJIA_OPEN_TYPE_REQUSTCODE;

/**
 * SJQ_ECSHOP_MJ_NEW
 * 新批发
 * Created by YichenZ on 2017/2/17 13:42.
 */

public class NewWholesaleActivity extends NewBaseActivity implements NewWholesaleAdapter.OnClickFollow {
    ActivityGoodShopBinding mBinding;

    public String title = "新批发";
    public String marketId = "";

    NewWholesaleAdapter adapter;
    List<ShopInfo> data;

    FILTER filter;
    ShopQuery query;
    MainQuery mainQuery;
    int page = 1;
    String type = "1", catName = "", categoryId = "";

    boolean isFirst = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_good_shop);
        mBinding.setOnClick(this);
        title = getIntent().getStringExtra("title");
        marketId = getIntent().getStringExtra("marketId");
        filter = new FILTER();
        query = new ShopQuery();
        mainQuery = new MainQuery();
        isFirst = true;
        initTitle();
        initRecycler();
        loadTopData();
        loadData();
    }

    private void loadTopData() {
        ProgressDialogUtil.getInstance().build(this).show();
        RetrofitAPIManager.getAPIPublic()
                .getWholesaleTop(mainQuery.getWTop(1))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .subscribe(d -> showTopData(d), e -> showError(e));
    }

    private void loadData() {
        RetrofitAPIManager.getAPIShop()
                .getList(query.getQuery(page, marketId, type, catName, categoryId))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResultJsonRet())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(d -> {
                    pageHandler(d.getPaginated());
                    showData(d.getData());
                }, e -> showError(e));
    }

    private void setFollow(int follow, String id) {
        if (((ECJiaApplication) getApplicationContext()).getUser() == null ||
                TextUtils.isEmpty(((ECJiaApplication) getApplicationContext()).getUser().getId())) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, ECJIA_OPEN_TYPE_REQUSTCODE);
            overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            return;
        }
        ProgressDialogUtil.getInstance().build(this).show();
        if (follow == 0) {
            RetrofitAPIManager.getAPIShop()
                    .putSellerCollectCreate(query.getFollow(id))
                    .compose(liToDestroy())
                    .compose(RxSchedulersHelper.io_main())
                    .compose(SchedulersHelper.handleResult())
                    .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                    .subscribe(d -> {
                        ToastUtil.getInstance().makeShortToast(this, "收藏成功");
                    }, e -> showError(e));
        } else {
            RetrofitAPIManager.getAPIShop()
                    .putSellerCollectDelete(query.getFollow(id))
                    .compose(liToDestroy())
                    .compose(RxSchedulersHelper.io_main())
                    .compose(SchedulersHelper.handleResult())
                    .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                    .subscribe(d -> {
                        ToastUtil.getInstance().makeShortToast(this, "取消收藏");
                    }, e -> showError(e));
        }
    }

    private void initRecycler() {
        mBinding.rvDataView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvDataView.addItemDecoration(
                new GearRecyclerItemDecoration(this, LinearLayoutManager.VERTICAL, 1));
        data = new ArrayList<>();
        adapter = new NewWholesaleAdapter(this, data, this);
        adapter.setOnLoadMoreListener(() -> loadData());

        initHeadView();

        mBinding.rvDataView.setAdapter(adapter.adapter());
    }

    private NewBannerView bannerViews;
    private View llTab;
    private TabLayout tabLayout;

    private void initHeadView() {
        bannerViews = new NewBannerView(mActivity);
        adapter.addHeaderView(bannerViews.getBannerView());

        llTab = LayoutInflater.from(this).inflate(R.layout.layout_tab, null);
        tabLayout = (TabLayout) llTab.findViewById(R.id.tl_tablayout);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(isFirst){
                    isFirst=false;
                }else{
                    categoryId = tab.getTag().toString();
                    String str = tab.getText().toString();
                    catName = "全部".equals(str) ? "" : str;
                    ProgressDialogUtil.getInstance().build(NewWholesaleActivity.this).show();
                    page=1;
                    loadData();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        adapter.addHeaderView(llTab);
    }

    private void initTitle() {
        if (title == null || "".equals(title)) {
            title = "新批发";
        }
        mBinding.icHead.tvTitle.setText(title);
    }


    private void pageHandler(BaseRes.Paginated p) {
        if (p.getMore() == 0) {
            adapter.noMore(true);
        } else {
            adapter.noMore(false);
        }
    }

    ArrayList<PLAYER> player = new ArrayList<>();
    List<Category> category = new ArrayList<>();

    private void showTopData(WholesaleTop wholesaleTop) {
        player.clear();
        player.addAll(wholesaleTop.getPlayer());
        bannerViews.createView(player);

        category.clear();
        category.addAll(wholesaleTop.getCategory());
        for (int i = 0; i < category.size(); i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(category.get(i).getCatName());
            tab.setTag(category.get(i).getCategoryId());
            tabLayout.addTab(tab);
        }
    }

    private void showData(List<ShopInfo> obj) {
        if (page == 1) {
            data.clear();
        }
        data.addAll(obj);
        adapter.notifyDataSetChanged();
        page++;
    }

    private void showError(Throwable e) {
        ToastUtil.getInstance().makeShortToast(this, e.getMessage());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back_button:
                finish();
                break;
        }
    }

    @Override
    public void onclick(int pos) {
        pos -= 2;
        if (data != null && data.size() > 0) {
            setFollow(data.get(pos).getFollow(), data.get(pos).getShop_id());
        }
    }
}
