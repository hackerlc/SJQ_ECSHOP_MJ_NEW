package com.ecjia.view.activity.newspecialoffer;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecjia.base.NewBaseFragment;
import com.ecjia.entity.model.News;
import com.ecjia.network.base.BaseRes;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.query.NewsQuery;
import com.ecjia.view.activity.HelpWebActivity;
import com.ecjia.view.activity.WebViewActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.ActivityNewsBinding;

import java.util.ArrayList;
import java.util.List;

import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ToastUtil;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/4/6 16:30.
 */

public class NewsSpecialFragment extends NewBaseFragment {
    ActivityNewsBinding mBinding;

    int page = 1;

    NewsAdapter mAdapter;
    NewsQuery mQueryt = new NewsQuery();
    private List<News> mNewses;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.activity_news, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
        getListData();
    }

    private void initRecyclerView() {
        mBinding.rvData.setLayoutManager(new LinearLayoutManager(getContext()));

        mNewses = new ArrayList<>();
        mAdapter = new NewsAdapter(getContext(), mNewses);
        mAdapter.setOnLoadMoreListener(() -> getListData());
        mAdapter.setOnItemClickListener((v, h, p) -> {
            Intent intent = new Intent(getActivity(), HelpWebActivity.class);
            intent.putExtra("id", Integer.parseInt(mNewses.get(p).getArticleId()));
            intent.putExtra("title", mNewses.get(p).getTitle());
            getActivity().startActivity(intent);
        });
        mBinding.rvData.setAdapter(mAdapter.adapter());

    }

    private void getListData() {
        RetrofitAPIManager.getAPIPublic()
                .getNews(mQueryt.getQuery(page, "2004"))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResultJsonRet())
                .subscribe(d -> {
                    pageHandler(d.getPaginated());
                    showGoodsData(d.getData());
                }, e -> showError(e));
    }

    private void getNewsDetail(String article_id) {
        RetrofitAPIManager.getAPIPublic()
                .getNewsDetail(mQueryt.getNewsDetail(article_id))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResultJsonRet())
                .subscribe(d -> {
                    pageHandler(d.getPaginated());
                    showGoodsData(d.getData());
                }, e -> showError(e));
    }

    private void pageHandler(BaseRes.Paginated p) {
        if (p.getMore() == 0) {
            mAdapter.noMore(true);
        } else {
            mAdapter.noMore(false);
        }
    }

    private void showGoodsData(List<News> obj) {
        if (obj != null) {
            if (page == 1) {
                mNewses.clear();
            }
            mNewses.addAll(obj);
            mAdapter.notifyDataSetChanged();
            page++;
        } else {
            ToastUtil.getInstance().makeShortToast(getContext(), "服务器异常请重新加载");
        }
    }

    private void showError(Throwable error) {
        ToastUtil.getInstance().makeLongToast(getContext(), "暂无数据");
    }
}
