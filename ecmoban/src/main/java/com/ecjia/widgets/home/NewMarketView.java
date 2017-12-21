package com.ecjia.widgets.home;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.ecjia.entity.model.Market;
import com.ecjia.view.activity.market.MarketActivity;
import com.ecjia.view.activity.newwholesale.NewWholesaleActivity;
import com.ecjia.view.fragment.homefragment.adapter.HomeNewMarketAdapter;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.MainNewMarketBinding;

import java.util.ArrayList;

/**
 * 最新商品
 * Created by Adam on 2016/7/13.
 */
public class NewMarketView extends HomeView<Market> implements IgnoredInterface {
    MainNewMarketBinding mBinding;
    private HomeNewMarketAdapter hlva;

    ArrayList<Market> mDataList=new ArrayList<>();

    public NewMarketView(Activity activity) {
        super(activity);
    }

    @Override
    protected void init() {
        mBinding= DataBindingUtil.inflate(LayoutInflater.from(mActivity),
                R.layout.main_new_market,null,false);
        mBinding.icHead.hotBigItem.setVisibility(View.GONE);
        mBinding.icHead.tvTitle.setText("逛市场");
        mBinding.icHead.tvTitleTag.setVisibility(View.GONE);
        mBinding.icHead2.tvTitle.setText("新批发");
        mBinding.icHead2.tvTitleTag.setVisibility(View.GONE);
        mBinding.icHead.homePromoteGetmore.setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, MarketActivity.class);
            mActivity.startActivity(intent);
            mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        });
        mBinding.icHead2.homePromoteGetmore.setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, NewWholesaleActivity.class);
            mActivity.startActivity(intent);
            mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBinding.horizontallistview1.setLayoutManager(mLayoutManager);

        hlva = new HomeNewMarketAdapter(mActivity, mDataList);
        mBinding.horizontallistview1.setAdapter(hlva);
    }

    @Override
    public void setVisible() {
    }

    @Override
    public void addToListView(ListView listView) {
        listView.addHeaderView(mBinding.getRoot());
    }

    @Override
    public void createView(ArrayList<Market> dataList) {
        if (dataList == null || dataList.size() == 0) {
            mBinding.icHead.hotBigItem.setVisibility(View.GONE);
            return;
        }
        hlva.setData(dataList);
        mBinding.icHead.hotBigItem.setVisibility(View.VISIBLE);
        
        hlva.notifyDataSetChanged();
    }

    @Override
    public View getIgnoredView() {
        return mBinding.getRoot();
    }
}
