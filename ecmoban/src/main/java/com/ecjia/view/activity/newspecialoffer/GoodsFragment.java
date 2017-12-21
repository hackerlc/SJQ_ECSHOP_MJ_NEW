package com.ecjia.view.activity.newspecialoffer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecjia.base.NewBaseFragment;
import com.ecjia.entity.model.SimpleGoods;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.network.base.BaseRes;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.query.GoodsQuery;
import com.ecjia.network.query.PageQuery;
import com.ecjia.view.activity.webwholesale.WebWholesaleAdapter;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.GoodslistLayoutBinding;

import java.util.ArrayList;
import java.util.List;

import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/4/6 16:30.
 */

public class GoodsFragment extends NewBaseFragment{
    GoodslistLayoutBinding mBinding;
    private List<SimpleGoods> goodsData;
    WebWholesaleAdapter dataAdapter;

    PageQuery mQuery;
    FILTER mFILTER;
    GoodsQuery goodsQuery = new GoodsQuery();

    int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.goodslist_layout, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mQuery = new PageQuery();
        mFILTER = new FILTER();
        mFILTER.setIs_new("1");

        initRecyclerView();
        getListData();
    }

    private void initRecyclerView() {
        GridLayoutManager lm = new GridLayoutManager(getContext(), 2);
        mBinding.rlGoods.setLayoutManager(lm);

        goodsData = new ArrayList<>();
        dataAdapter = new WebWholesaleAdapter(getContext(), goodsData, true);
        dataAdapter.setOnLoadMoreListener(() -> getListData());

        mBinding.rlGoods.setAdapter(dataAdapter.adapter());

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
            ToastUtil.getInstance().makeShortToast(getContext(), "服务器异常请重新加载");
        }
    }

    private void showError(Throwable error) {
        ToastUtil.getInstance().makeLongToast(getContext(), "暂无数据");
    }
}
