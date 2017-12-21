package com.ecjia.view.activity.together;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.ecjia.base.NewBaseActivity;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.entity.model.Banner;
import com.ecjia.entity.model.GroupGoods;
import com.ecjia.network.base.BaseRes;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.query.PageQuery;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.ActivityTogetherWholesaleBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * SJQ_ECSHOP_MJ_NEW
 * 团批
 * Created by YichenZ on 2017/2/15 15:31.
 */

public class TogetherWholesaleActivity extends NewBaseActivity {
    private ActivityTogetherWholesaleBinding mBinding;

    private ImageView headImg;

    private TogetherWholesaleAdapter mAdapter;
    private List<GroupGoods> data;
    private PageQuery query;
    private int page=1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this, R.layout.activity_together_wholesale);
        mBinding.setOnClick(this);
        init();
        initTitleView();
        initAdapter();
        initHeadImage();
        mBinding.rvDataView.setAdapter(mAdapter.adapter());
        getData();
    }

    @Override
    protected void onDestroy() {
        if(mDisposable!=null){
            mDisposable.dispose();
        }
        super.onDestroy();
    }

    private void initTitleView() {
        mBinding.lcHead.tvTitle.setText("团批区");
    }

    private void init() {
        query=new PageQuery();
    }

    private void initAdapter() {
        data=new ArrayList<>();
        mBinding.rvDataView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new TogetherWholesaleAdapter(this,data);
        mAdapter.setOnLoadMoreListener(() -> getData());

        mAdapter.setOnItemClickListener((view, holder, position) -> {
            Intent it = new Intent(mActivity, GoodsDetailActivity.class);
            it.putExtra(IntentKeywords.GOODS_ID, data.get(position-1).getId());
            it.putExtra(IntentKeywords.OBJECT_ID, data.get(position-1).getObjectId());
            it.putExtra(IntentKeywords.REC_TYPE, data.get(position-1).getRecType());
            mActivity.startActivity(it);
        });

    }

    private void initHeadImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_activity_banner,null);
        headImg=(ImageView) view.findViewById(R.id.iv_banner);
        mAdapter.addHeaderView(view);
    }

    public void getData() {
        ProgressDialogUtil.getInstance().build(this).show();
        RetrofitAPIManager.getAPIGoods()
                .getTimeGroup(query.getQuery(page))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResultJsonRet())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(d -> {pageHandler(d.getPaginated());showData(d.getData());},e -> showError(e));
    }

    private void pageHandler(BaseRes.Paginated p) {
        if (p.getMore() == 0) {
            mAdapter.noMore(true);
        } else {
            mAdapter.noMore(false);
        }
    }

    Disposable mDisposable;
    private void showData(Banner obj){
        if(mDisposable!=null){
            mDisposable.dispose();
        }
        if(page==1){
            ImageLoaderForLV.getInstance(this).setImageRes(headImg,obj.getBanner());
            data.clear();
        }
        data.addAll(obj.getGroup_goods());
        page++;
        //定时器开启
        mDisposable=Flowable.interval(0,1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    for(int i=0;i<data.size();i++){
                        data.get(i).lessLeftSecond();
                    }
                    mAdapter.notifyDataSetChanged();
                },e -> ToastUtil.getInstance().makeShortToast(mActivity,e.getMessage()));
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
