package com.ecjia.view.activity.market;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.ecjia.base.NewBaseActivity;
import com.ecjia.entity.model.Banner;
import com.ecjia.entity.model.Market;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.network.base.BaseRes;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.query.PageQuery;
import com.ecjia.view.activity.shop.NewHomeShopListActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.ActivityMarketBinding;

import java.util.ArrayList;
import java.util.List;

import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.ui.custom.GearRecyclerItemDecoration;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;

/**
 * SJQ_ECSHOP_MJ_NEW
 * 逛市场
 * Created by YichenZ on 2017/2/20 10:52.
 */

public class MarketActivity extends NewBaseActivity {
    ActivityMarketBinding mBinding;
    List<Market> data;

    MarketAdapter adapter;

    FILTER filter;
    PageQuery query;
    int page = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_market);
        mBinding.setOnClick(this);
        filter=new FILTER();
        query=new PageQuery();

        initTitle();
        initRecycler();
        setData();
    }

    private void initRecycler() {
        mBinding.rvDataView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvDataView.addItemDecoration(
                new GearRecyclerItemDecoration(this,LinearLayoutManager.VERTICAL,16));
        data=new ArrayList<>();
        adapter=new MarketAdapter(this,data);
        adapter.setOnLoadMoreListener(() -> {
            setData();
        });
        adapter.setOnItemClickListener((view, holder, position) -> {
            startActivity(new Intent(this, NewHomeShopListActivity.class)
                    .putExtra("title", data.get(position).getName())
                    .putExtra("marketId", data.get(position).getId()));
        });
        mBinding.rvDataView.setAdapter(adapter.adapter());
    }

    private void initTitle() {
        mBinding.icHead.tvTitle.setText("逛市场");
        mBinding.icHead.ivBackButton.setOnClickListener(v -> finish());
    }

    private void setData() {
        ProgressDialogUtil.getInstance().build(this).show();
        RetrofitAPIManager.getAPIGoods()
                .getMarket(query.getQuery(page))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResultJsonRet())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(d -> {pageHandler(d.getPaginated());showData(d.getData());}, e-> showError(e));
    }

    private void pageHandler(BaseRes.Paginated p) {
        if (p.getMore() == 0) {
            adapter.noMore(true);
        }else{
            adapter.noMore(false);
        }
    }

    private void showData(Banner obj){
        if(page == 1) {
            mBinding.setData(obj);
            data.clear();
        }
        data.addAll(obj.getMarket());
        if(obj.getMarket().size()>0) {
            adapter.notifyDataSetChanged();
        }else{
            adapter.setOnLoadMoreListener(null);
        }
        page++;
    }

    private void showError(Throwable e){
        ToastUtil.getInstance().makeShortToast(this,e.getMessage());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.iv_back_button:finish();break;
        }
    }
}
