package com.ecjia.view.activity.newspecialoffer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;

import com.ecjia.base.NewBaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.ActivityPushMessageBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 新特供
 */
public class NewSpecialOfferActivity extends NewBaseActivity{
    ActivityPushMessageBinding mBinding;

    private NewSpecialOfferAdapter mAdapter;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();

    private GoodsFragment mGoodsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_push_message);
        mBinding.setOnClick(this);
        initData();
        initUI();
    }

    private void initData() {
        mTitles.add("尾货特卖");
        mTitles.add("对接资讯");

        mGoodsFragment = new GoodsFragment();
        mFragmentList.add(mGoodsFragment);
        mFragmentList.add(new NewsSpecialFragment());

        mAdapter = new NewSpecialOfferAdapter(getSupportFragmentManager(), mFragmentList, mTitles);
    }


    private void initUI() {
        mBinding.icHead.tvTitle.setText("新特卖");

        mBinding.vpMessage.setAdapter(mAdapter);
        mBinding.icTab.tlTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mBinding.icTab.tlTablayout.setupWithViewPager(mBinding.vpMessage);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.iv_back_button:finish();
        }
    }
}
