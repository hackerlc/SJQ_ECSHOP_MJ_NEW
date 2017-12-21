package com.ecjia.view.activity.snatch;


import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.ecjia.base.NewBaseActivity;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.view.fragment.FilterListFragment;
import com.ecjia.view.fragment.snatch.SnatchWholesaleFragment;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.SnatchWholesaleDataBindingActivityBinding;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;
import gear.yc.com.gearlibrary.rxjava.rxbus.annotation.Subscribe;

/**
 * SJQ_ECSHOP_MJ_NEW
 * 抢批
 * Created by YichenZ on 2017/2/9 10:20.
 */

public class SnatchWholesaleActivity extends NewBaseActivity implements OnClickListener {
    SnatchWholesaleDataBindingActivityBinding mBinding;

    public boolean isFirst = false;
    private FilterListFragment flFragment;
    SlidingMenu menu;
    private FragmentManager fm;
    private FragmentTransaction ft;

    SnatchWholesaleFragment todayFragment;
    SnatchWholesaleFragment tomorrowFragment;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.getInstance().register(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.snatch_wholesale_data_binding_activity);
        mBinding.setOnClick(this);

        initTitleView();
        initFilterFragment();
        initFragment();
    }

    @Override
    protected void onDestroy() {
        RxBus.getInstance().unRegister(this);
        super.onDestroy();
    }

    private void initFragment() {
        if (fm == null) {
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
        }
        if (todayFragment == null) {
            todayFragment = new SnatchWholesaleFragment();
        }
        if (tomorrowFragment == null) {
            tomorrowFragment = new SnatchWholesaleFragment(false);
        }
        ft.add(mBinding.flDiv.getId(),todayFragment,todayFragment.getClass().getName())
                .add(mBinding.flDiv.getId(),tomorrowFragment,tomorrowFragment.getClass().getName())
                .hide(tomorrowFragment)
                .commit();
    }

    private void initTitleView() {
        mBinding.icHead.tvTitle.setText("限时抢批");
    }

    private void initFilterFragment() {
        if (flFragment == null) {
            flFragment = new FilterListFragment(R.string.filter_wholesale_today, R.string.filter_wholesale_tomorrow);

            flFragment.getTabThreeCellHolder().show = View.GONE;
            flFragment.getTabFourCellHolder().show = View.GONE;

            flFragment.getTabOneCellHolder().setTitleCellOnClickListener(v -> chooseFilter(0));
            flFragment.getTabTwoCellHolder().setTitleCellOnClickListener(v -> chooseFilter(1));
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(mBinding.flFilter.getId(), flFragment).commit();
        }
    }

    private void chooseFilter(int i) {
        ft=fm.beginTransaction();
        if (i == 0) {
            ft.hide(tomorrowFragment).show(todayFragment);
        } else {
            ft.hide(todayFragment).show(tomorrowFragment);
        }
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_button://返回按钮
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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

    @Subscribe(tag = RxBus.TAG_UPDATE)
    public void showHeadImg(String url){
        ImageLoaderForLV.getInstance(this).setImageRes(mBinding.ivHead, url);
    }

}

