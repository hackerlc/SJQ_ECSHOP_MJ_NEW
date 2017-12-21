package com.ecjia.view.activity.goodsorder.balance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.LibActivity;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.entity.eventmodel.EventShopCoupons;
import com.ecjia.entity.eventmodel.GoodRedPaperEvent;
import com.ecjia.entity.requestmodel.SubOrderBase;
import com.ecjia.entity.requestmodel.SubOrderCondition;
import com.ecjia.entity.responsemodel.ADDRESS;
import com.ecjia.entity.responsemodel.BONUS;
import com.ecjia.entity.responsemodel.ORDER_INFO;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.query.UserQuery;
import com.ecjia.util.EventBus.Event;
import com.ecjia.util.LG;
import com.ecjia.util.common.JsonUtil;
import com.ecjia.view.activity.AddressManageActivity;
import com.ecjia.view.activity.ChoosePayActivity;
import com.ecjia.view.activity.goodsorder.balance.entity.NewBalanceBaseData;
import com.ecjia.view.activity.goodsorder.balance.entity.NewBlanceShopData;
import com.ecjia.view.adapter.adapter.wrapper.HeaderAndFooterWrapper;
import com.ecmoban.android.sijiqing.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;
import gear.yc.com.gearlibrary.rxjava.rxbus.annotation.Subscribe;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-04-06.
 */

public class NewBalanceActivity extends LibActivity {
    @BindView(R.id.textView_title)
    TextView textView_title;
    @BindView(R.id.imageView_back)
    ImageView imageView_back;
    @BindView(R.id.textView_more)
    TextView textView_more;

    @BindView(R.id.balance_submit)
    LinearLayout balance_submit;//提交订单
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;//列表

    private HeaderAndFooterWrapper wrapper;
    private NewBalanceAdapter mAdapter;
    //用户地址
    private View headView;
    private LinearLayout balance_user;
    private TextView balance_name;
    private TextView balance_phoneNum;
    private TextView balance_address;

    //红包
    private View bottomView;
    private LinearLayout balance_redPaper;
    private TextView balance_redPaper_name;
    ////运费总计
    private TextView tv_shipping_fee;
    ////z支付商品总金额总计
    private TextView tv_good_amount_all;
    //
    private TextView tv_good_allnumber;
    private TextView tv_good_allmoney;

    private String rec_ids = "";//需要购买的商品id列表
    private String address_id = "";
    private NewBalanceBaseData newBalanceBaseData;
    private List<NewBlanceShopData> shopListData = new ArrayList<>();
    boolean isSelectBons;

    @Override
    public int getLayoutId() {
        return R.layout.act_new_balance;
    }

    @Override
    public void init(Bundle savedInstanceState) {
//        EventBus.getDefault().register(this);
        RxBus.getInstance().register(this);
        textView_title.setText("结算");
        rec_ids = getIntent().getStringExtra("rec_ids");
        address_id = getIntent().getStringExtra(IntentKeywords.ADDRESS_ID);
        getHttpData(rec_ids, address_id);
    }

    private void initView() {
        initHeadView();
        iniBottomView();
        setUpdataData();
//        mAdapter = new NewBalanceAdapter(mActivity, newBalanceBaseData.getShop_list(), getSupportFragmentManager());
        setData();
    }

    public void setData() {
        mAdapter = new NewBalanceAdapter(mActivity, shopListData, getSupportFragmentManager());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        wrapper = new HeaderAndFooterWrapper(mAdapter);/**/
        wrapper.addHeaderView(headView);
        wrapper.addFootView(bottomView);
        mRecyclerView.setAdapter(wrapper);
    }


    private void initHeadView() {
        headView = LayoutInflater.from(mActivity).inflate(R.layout.layout_newbalance_head, null);
        balance_user = (LinearLayout) headView.findViewById(R.id.balance_user);
        balance_name = (TextView) headView.findViewById(R.id.balance_name);
        balance_phoneNum = (TextView) headView.findViewById(R.id.balance_phoneNum);
        balance_address = (TextView) headView.findViewById(R.id.balance_address);
        balance_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, AddressManageActivity.class);
                intent.putExtra("flag", 1);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
    }

    private void iniBottomView() {
        bottomView = LayoutInflater.from(mActivity).inflate(R.layout.layout_newbalance_bottom, null);
        balance_redPaper = (LinearLayout) bottomView.findViewById(R.id.balance_redPaper);
        balance_redPaper_name = (TextView) bottomView.findViewById(R.id.balance_redPaper_name);
        tv_shipping_fee = (TextView) bottomView.findViewById(R.id.tv_shipping_fee);
        tv_good_amount_all = (TextView) bottomView.findViewById(R.id.tv_good_amount_all);

        tv_good_allnumber = (TextView) bottomView.findViewById(R.id.tv_good_allnumber);
        tv_good_allmoney = (TextView) bottomView.findViewById(R.id.tv_good_allmoney);
    }

    private void setUpdataData() {
        ADDRESS consignee = newBalanceBaseData.getConsignee();
        balance_name.setText(consignee.getConsignee() + "");
        balance_phoneNum.setText(consignee.getMobile() + "");
        StringBuffer sbf = new StringBuffer();
        sbf.append(consignee.getProvince_name() + " ");
        sbf.append(consignee.getCity_name() + " ");
        sbf.append(consignee.getDistrict_name() + " ");
        sbf.append(consignee.getAddress());
//        address.setText(sbf.toString());
        balance_address.setText(sbf.toString());
        isSelectBons = false;
        List<BONUS> bonusList = new ArrayList<>();
        if (newBalanceBaseData.getBonus().size() > 0) {
            for (BONUS bonu : newBalanceBaseData.getBonus()) {
                LG.d(bonu.getType_name()+"bonu++++>>>>"+bonu.isIschecked()+">>>>>>"+newBalanceBaseData.getShowAllGoodsMoney());
                if (newBalanceBaseData.getShowAllGoodsMoney() >= Float.parseFloat(bonu.getRequest_amount())) {
                    isSelectBons = true;
                    bonusList.add(bonu);
                }else{
                    bonu.setIschecked(false);
                }
            }
        }

        if (bonusList.size() > 0) {
            balance_redPaper_name.setText("点击选择红包");
            for (BONUS bonu : bonusList) {
                LG.d(bonu.getType_name()+"bonu++++>>>>"+bonu.isIschecked()+">>>>>>");
                if (bonu.isIschecked()) {
                    balance_redPaper_name.setText("省" + bonu.getType_money() + ":" + bonu.getType_name());
                }else{
                    bonu.setIschecked(false);
                }
            }
            balance_redPaper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSelectBons) {
                        OrderBottomChooseRedPaperDialogFragment.newInstance().showDialog(getSupportFragmentManager(), bonusList);
                    }
                }
            });
        } else {
            balance_redPaper_name.setText("暂无可用红包");
        }
        tv_shipping_fee.setText(newBalanceBaseData.getShowAllFee() + "");
        tv_good_amount_all.setText(newBalanceBaseData.getShowAllGoodsMoney() + "");
        tv_good_allnumber.setText("共" + newBalanceBaseData.getTotal_goods_num() + "件商品(含运费) ： ");
        tv_good_allmoney.setText(newBalanceBaseData.getShowAllMoney() + "");
    }

    private void getHttpData(String array, String address_id) {
        ProgressDialogUtil.getInstance().build(mActivity).show();
        RetrofitAPIManager.getAPIGoods()
                .getChekOrder(UserQuery.getInstance().getChekOrder(array, address_id))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<NewBalanceBaseData>() {
                    @Override
                    public void onNext(NewBalanceBaseData data) {
                        newBalanceBaseData = data;
                        shopListData.clear();
                        shopListData.addAll(newBalanceBaseData.getShop_list());
                        initView();
                    }

                    @Override
                    public void onError(Throwable t) {
                        ToastUtil.getInstance().makeShortToast(mActivity, t.getMessage());
//                        finish();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void submitOrder() {
        ProgressDialogUtil.getInstance().build(mActivity).show();
        RetrofitAPIManager.getAPIGoods()
                .getSubmitOrder(JsonUtil.toHttpFormatString(getPostString()))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<ORDER_INFO>() {
                    @Override
                    public void onNext(ORDER_INFO order_info) {
                        Intent intent = new Intent(NewBalanceActivity.this, ChoosePayActivity.class);
                        intent.putExtra(IntentKeywords.PAY_TYPE, IntentKeywords.ORDER_ID);
                        intent.putExtra(IntentKeywords.ORDER_ID, order_info.getOrder_id() + "");
                        intent.putExtra(IntentKeywords.PAY_IS_CREATE, true);
                        intent.putExtra(IntentKeywords.PAY_BODY, getBody());
                        intent.putExtra(IntentKeywords.PAY_AMOUNT, order_info.getOrder_info().getOrder_amount() + "");
                        intent.putExtra(IntentKeywords.PAY_CODE, newBalanceBaseData.getPay_info().getPay_code());
                        startActivity(intent);
                        finish();
                        mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    }

                    @Override
                    public void onError(Throwable t) {
                        ToastUtil.getInstance().makeShortToast(mActivity, t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private String getBody() {
        final String order_incloud = "该订单含有【";
        final String deng = "】等";
        final String zhong_goods = "件商品。";
        String body = order_incloud + newBalanceBaseData.getShop_list().get(0).getGoods_list().get(0).getGoods_name() + deng + newBalanceBaseData.getTotal_goods_num() + zhong_goods;
        return body;
    }

    @OnClick({R.id.imageView_back, R.id.balance_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_back:
                finish();
                break;
            case R.id.balance_submit:
                submitOrder();
                break;
        }
    }

    private SubOrderBase getPostString() {
        SubOrderBase subOrderBase = new SubOrderBase();
        subOrderBase.setRec_id(rec_ids);
        subOrderBase.setAddress_id(address_id);
        subOrderBase.setPay_id(newBalanceBaseData.getPay_info().getPay_id());
        newBalanceBaseData.getUseBonus();
        subOrderBase.setBonus_id(newBalanceBaseData.getAllow_use_bonus_id());
        List<SubOrderCondition> list = new ArrayList<>();
        for (NewBlanceShopData shop : newBalanceBaseData.getShop_list()) {
            SubOrderCondition condition = new SubOrderCondition();
            shop.getShop_shipping_fee();
            shop.getCoupons_money();
            condition.setShipping_id(shop.getUse_shipping_id());
            condition.setCou_id(shop.getUse_coupons_id());
            condition.setShop_id(shop.getShop_id());
            condition.setAct_id(shop.getShop_favourable().getAct_id());
            list.add(condition);
        }
        subOrderBase.setShop_use_condition(list);
        return subOrderBase;
    }

    @Subscribe(tag = RxBus.TAG_CHANGE)
    public void changeHandler(EventShopCoupons eventShopCoupons) {
        if (eventShopCoupons != null) {
            newBalanceBaseData.getShop_list().get(eventShopCoupons.getPosition()).setCoupons(eventShopCoupons.getListData());
            shopListData.clear();
            shopListData.addAll(newBalanceBaseData.getShop_list());
            setData();
            setUpdataData();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(tag = RxBus.TAG_DEFAULT)
    public void changeHandler(List<BONUS> listData) {
        if (listData != null) {
            newBalanceBaseData.setBonus(listData);
//            shopListData.clear();
//            shopListData.addAll(newBalanceBaseData.getShop_list());
//            setData();
            setUpdataData();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(tag = RxBus.TAG_UPDATE)
    public void changeUpdatHandler(EventShopCoupons eventShopCoupons) {
        if (eventShopCoupons != null) {
            newBalanceBaseData.getShop_list().get(eventShopCoupons.getPosition()).setShipping_list(eventShopCoupons.getShippingList());
            shopListData.clear();
            shopListData.addAll(newBalanceBaseData.getShop_list());
            setData();
            setUpdataData();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (data != null) {
                address_id = data.getStringExtra("address_id");
                getHttpData(rec_ids, address_id);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(mActivity);
//        EventBus.getDefault().unregister(mActivity);
    }

//    public void onEvent(GoodRedPaperEvent event) {
//        if (event.getListData() != null) {
//            newBalanceBaseData.setBonus(event.getListData());
//            setUpdataData();
//            mAdapter.notifyDataSetChanged();
//        }
//    }

    @Override
    public void dispose() {

    }
}
