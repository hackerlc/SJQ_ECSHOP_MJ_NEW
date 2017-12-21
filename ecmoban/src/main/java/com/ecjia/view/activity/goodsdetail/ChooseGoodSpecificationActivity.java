package com.ecjia.view.activity.goodsdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.LibActivity;
import com.ecjia.consts.AppConst;
import com.ecjia.entity.eventmodel.GoodSpecificationEvent;
import com.ecjia.entity.responsemodel.SPECIFICATION;
import com.ecjia.entity.responsemodel.SPECIFICATION_VALUE;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.FormatUtil;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.util.common.JsonUtil;
import com.ecjia.view.adapter.GoodDetailDraft;
import com.ecjia.view.adapter.adapter.my.MyFragmentPagerAdapter;
import com.ecmoban.android.sijiqing.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybk.intent.inject.annotation.Extra;
import com.ybk.intent.inject.api.IntentInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;
import gear.yc.com.gearlibrary.rxjava.rxbus.annotation.Subscribe;

/**
 * 类名介绍：商品详情尺码选择页面
 * Created by sun
 * Created time 2017-03-30.
 */

public class ChooseGoodSpecificationActivity extends LibActivity {
    @BindView(R.id.goods_image_iv)
    ImageView goods_image_iv;
    @BindView(R.id.goods_name_tv)
    TextView goods_name_tv;
    @BindView(R.id.goods_price_tv)
    TextView goods_price_tv;
    @BindView(R.id.btClose)
    Button btClose;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tv_selectall_count)
    TextView tv_selectall_count;

    @BindView(R.id.spec_add_to_cart)
    TextView spec_add_to_cart;
    @BindView(R.id.spec_buy_now)
    TextView spec_buy_now;

    @Extra("imgurl")
    String imgurl;
    @Extra("price")
    String price;
    @Extra("goodDes")
    String goodDes;

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    //存放以选择的颜色尺码数据
//    HashMap<String, GwcColorAndSize> hashMap = new HashMap<>();
    private StringBuffer putSpecIds = new StringBuffer();
    private StringBuffer putSpecIdsCount = new StringBuffer();
    private int selectAllCount = 0;
    private final int SPACIFICATION_ADDCART = 10016;//商品属性
    private final int SPACIFICATION_BALANCE = 10017;//商品属性
    private final int SPACIFICATION = 10015;//商品属性

    private List<SPECIFICATION> specificationList = new ArrayList<>();//list0 颜色  list1 尺码

    //存放以选择的颜色尺码数据
//    HashMap<String, GwcColorAndSize> hashMap = new HashMap<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_activity_chooesgood_specification;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        IntentInject.inject(this);//add this line code
        RxBus.getInstance().register(this);
        goods_name_tv.setText(goodDes);
        String tag = "";
        if (AppConst.GROUPBUY_GOODS.equals(GoodDetailDraft.getInstance().goodDetail.getActivity_type())) {
            tag = "定金: ";
        }
        if ("免费".equals(price)) {
            goods_price_tv.setText(tag + "免费");
        } else {
            try {
                goods_price_tv.setText(tag + FormatUtil.formatToSymbolPrice(price));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (imgurl != null) {
            ImageLoader.getInstance().displayImage(imgurl, goods_image_iv);
        }
        specificationList.clear();
        specificationList.addAll(GoodDetailDraft.getInstance().goodDetail.specification);
        initView();
    }

    private void initView() {//颜色，颜色分类    尺寸 尺码
        for (SPECIFICATION_VALUE data : specificationList.get(0).getValue()) {
            fragments.add(ChooseSizeAndCountFragment_Builder.builder()
                    .colorId(data.getId() + "")
                    .valueString(JsonUtil.toString(GoodDetailDraft.getInstance().getSizeListByColorId(data.getId() + ""))).build());
            titles.add(data.getLabel());
        }
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments, titles));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("isbynow", false);
            setResult(SPACIFICATION, intent);
            finish();
        }
        return true;
    }

    @OnClick({R.id.btClose, R.id.spec_add_to_cart, R.id.spec_buy_now})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btClose:
                intent.putExtra("isbynow", false);
                setResult(SPACIFICATION, intent);
                finish();
                break;
            case R.id.spec_add_to_cart:
                intent.putExtra("isbynow", false);
                setResult(SPACIFICATION_ADDCART, intent);
                finish();
                break;
            case R.id.spec_buy_now:
                intent.putExtra("isbynow", true);
                setResult(SPACIFICATION_BALANCE, intent);
                finish();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }

    @Subscribe(tag = RxBus.TAG_UPDATE)
    public void updateSelcetCount(GoodSpecificationEvent goodSpecificationEvent) {
        GoodDetailDraft.getInstance().setSelectData(goodSpecificationEvent.getDatas());
        selectAllCount = GoodDetailDraft.getInstance().goodAllQuantity;
        tv_selectall_count.setText(Html.fromHtml("共选择商品<font color='#f5337d'>" + selectAllCount + "</font>件"));
        EventBus.getDefault().post(new MyEvent("" + selectAllCount, 1000));
    }

    @Override
    public void dispose() {

    }
}
