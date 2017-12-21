package com.ecjia.view.fragment.snatch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecjia.base.NewBaseFragment;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.entity.model.BannerSnatch;
import com.ecjia.network.base.BaseRes;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.query.GoodsQuery;
import com.ecjia.entity.model.SnatchGoods;
import com.ecjia.util.DateManager;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.LayoutFragmentSnatchWholesaleBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;
import gear.yc.com.gearlibrary.ui.custom.GearRecyclerItemDecoration;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * SJQ_ECSHOP_MJ_NEW
 * 抢批list模块
 * Created by YichenZ on 2017/2/14 16:32.
 */

public class SnatchWholesaleFragment extends NewBaseFragment {
    //head time
    View headView;
    TextView titleStr, timerStr;
    LayoutFragmentSnatchWholesaleBinding mBinding;
    List<SnatchGoods> data;
    SnatchWholesaleAdapter adapter;
    GoodsQuery query = new GoodsQuery();

    int page = 1;
    /**
     * true 今日抢批
     * false 明日预告
     */
    private boolean isToday = true;
    BannerSnatch mBannerSnatch;

    public SnatchWholesaleFragment() {
    }

    @SuppressLint("ValidFragment")
    public SnatchWholesaleFragment(boolean isToday) {
        super();
        this.isToday = isToday;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_fragment_snatch_wholesale, container, false);
        mBinding.rlDataView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rlDataView.addItemDecoration(
                new GearRecyclerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 1));
        data = new ArrayList<>();
        adapter = new SnatchWholesaleAdapter(getContext(), data, isToday);

        headView = inflater.inflate(R.layout.div_snatch_wholesale_time, container, false);
        titleStr = (TextView) headView.findViewById(R.id.tv_text);
        timerStr = (TextView) headView.findViewById(R.id.tv_time);
        if (isToday) {
            titleStr.setText("距离本场结束：");
        } else {
            titleStr.setText("距离本场开始：");
        }
        adapter.addHeaderView(headView);
        adapter.setOnLoadMoreListener(() -> getData());
        adapter.setOnItemClickListener((view, holder, position) -> {
            position-=1;
            Intent intent = new Intent();
            intent.setClass(getContext(), GoodsDetailActivity.class);
            intent.putExtra(IntentKeywords.GOODS_ID, data.get(position).getId());
            intent.putExtra(IntentKeywords.OBJECT_ID, data.get(position).getObject_id());
            intent.putExtra(IntentKeywords.REC_TYPE, data.get(position).getRec_type());
            getContext().startActivity(intent);
        });
        mBinding.rlDataView.setAdapter(adapter.adapter());
        getData();

        return mBinding.getRoot();
    }

    Disposable mDisposable;

    public void getData() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        ProgressDialogUtil.getInstance().build(getContext()).show();
        RetrofitAPIManager.getAPIGoods()
                .getTimePanic(query.getTimePanic(page, isToday))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResultJsonRet())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(d -> {
                            pageHandler(d.getPaginated());
                            showData(d.getData());
                        }
                        , e -> showError(e));
    }

    private void pageHandler(BaseRes.Paginated p) {
        if (p.getMore() == 0) {
            adapter.noMore(true);
        }else{
            adapter.noMore(false);
        }
    }

    private void showData(BannerSnatch obj) {
        if (obj != null) {
            mBannerSnatch = obj;
            if (page == 1) {
                data.clear();
            }
            data.addAll(obj.getSnatchGoods());
            adapter.notifyDataSetChanged();
            page++;
            RxBus.getInstance().post(RxBus.TAG_UPDATE,obj.getBanner());
            mDisposable = Flowable.interval(0, 1, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.trampoline())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(liToDestroy())
                    .subscribe(d -> {
                        timerStr.setText(DateManager
                                .formatDateTime4Snatch(mBannerSnatch.getLeft_scond()));
                        mBannerSnatch.lessLeftSecond();
                        if (mBannerSnatch.getLeft_scond() == 0) {
                            getData();
                        }
                    });
        } else {
            ToastUtil.getInstance().makeShortToast(getContext(), "服务器异常请重新加载");
        }
    }

    private void showError(Throwable e) {
        ToastUtil.getInstance().makeShortToast(getContext(), e.getMessage());
    }
}
