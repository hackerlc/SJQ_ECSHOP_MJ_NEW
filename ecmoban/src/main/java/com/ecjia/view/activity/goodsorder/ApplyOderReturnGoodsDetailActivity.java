package com.ecjia.view.activity.goodsorder;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.LibActivity;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.entity.intentmodel.OrderGoodInrtent;
import com.ecjia.entity.responsemodel.ODERRETURNGOODSDETAIL;
import com.ecjia.entity.responsemodel.RETURNSTATUS;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.query.GoodsQuery;
import com.ecjia.util.common.DialogUtils;
import com.ecjia.util.common.JsonUtil;
import com.ecjia.util.common.T;
import com.ecjia.view.adapter.OderReturnGoodsDetailAdapter;
import com.ecjia.widgets.MyPullRefreshFrameLayout;
import com.ecjia.widgets.ToastView;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecmoban.android.sijiqing.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 类名介绍：退换货详情页面
 * Created by sun
 * Created time 2017-03-09.
 */

public class ApplyOderReturnGoodsDetailActivity extends LibActivity {
    @BindView(R.id.textView_title)
    TextView textView_title;
    @BindView(R.id.imageView_back)
    ImageView imageView_back;
    @BindView(R.id.textView_more)
    TextView textView_more;

    @BindView(R.id.null_pager)
    FrameLayout null_pager;

    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.tv_apply_time)
    TextView tv_apply_time;

    @BindView(R.id.my_pullrefresh)
    MyPullRefreshFrameLayout my_pullrefresh;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.ly_bottom)
    LinearLayout ly_bottom;
    @BindView(R.id.tv_contact)
    TextView tv_contact;
    @BindView(R.id.tv_writeinfo)
    TextView tv_writeinfo;
    @BindView(R.id.tv_applyagin)
    TextView tv_applyagin;
    @BindView(R.id.tv_confirm)
    TextView tv_confirm;


    private OderReturnGoodsDetailAdapter mAdapter;
    private List<ODERRETURNGOODSDETAIL.ReturnGoodInfoList> datas;
    private ODERRETURNGOODSDETAIL detailDatas;

    private String retId;
    private String recId;
    private String orderTypeFlag;
    private OrderGoodInrtent orderGoodInrtent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_return_goodsdetail;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        textView_title.setText("申请进度");
        Bundle bundle = getIntent().getExtras();
        retId = bundle.getString(IntentKeywords.RET_ID);
        recId = bundle.getString(IntentKeywords.REC_ID);
        orderTypeFlag = bundle.getString(IntentKeywords.ORDER_TYPE);//订单当前的状态
        orderGoodInrtent = JsonUtil.getObj(bundle.getString(IntentKeywords.ORDER_GOODINRTENT), OrderGoodInrtent.class);
        if (TextUtils.isEmpty(retId)) {
            finish();
            return;
        }
        datas = new ArrayList<>();
        datas.clear();
        mAdapter = new OderReturnGoodsDetailAdapter(mActivity, datas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setAdapter(mAdapter);

        my_pullrefresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //执行刷新操作
                getHttpData(retId);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, mRecyclerView == null ? content : mRecyclerView, header);
                //return super.checkCanDoRefresh(frame, 需要刷新的当前View == null ? content : 需要刷新的当前View , header);
            }
        });
        getHttpData(retId);
    }

    private void getHttpData(String ret_id) {
        ProgressDialogUtil.getInstance().build(mActivity).show();
        RetrofitAPIManager.getAPIGoods()
                .getReturnDetail(GoodsQuery.getInstance().getReturnDetail(ret_id))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<ODERRETURNGOODSDETAIL>() {
                    @Override
                    public void onNext(ODERRETURNGOODSDETAIL oderreturngoodsdetail) {
                        detailDatas = oderreturngoodsdetail;
                        tv_number.setText("订单编号：  " + oderreturngoodsdetail.getReturn_sn());
                        tv_apply_time.setText("申请时间：   " + oderreturngoodsdetail.getApply_time());
                        if (detailDatas != null) {
                            setLy_bottomView();
                        }
                        datas.clear();
                        if (oderreturngoodsdetail.getLog().size() > 0) {
                            datas.addAll(oderreturngoodsdetail.getLog());
                            mAdapter.notifyDataSetChanged();
                            null_pager.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            null_pager.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        }
                        my_pullrefresh.refreshComplete();
                    }

                    @Override
                    public void onError(Throwable t) {
                        null_pager.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        T.show(mActivity, t.getMessage());
                        my_pullrefresh.refreshComplete();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setLy_bottomView() {
        if ("5".equals(detailDatas.getReturn_status())) { //0为已申请,1为卖家收到，2为卖家寄出（分单），3为卖家寄出，4为完成，5为同意申请，6为买家寄出，9为卖家拒绝
            ly_bottom.setVisibility(View.VISIBLE);
            tv_writeinfo.setVisibility(View.VISIBLE);
            tv_contact.setVisibility(View.GONE);
            tv_applyagin.setVisibility(View.GONE);
            tv_confirm.setVisibility(View.GONE);
            if ("0".equals(detailDatas.getReturn_type())) {//紧退款  "return_type": "2",//(退换货类型0仅退款 1退货 2换货)
                ly_bottom.setVisibility(View.GONE);
            }
        } else if ("9".equals(detailDatas.getReturn_status())) {//9为卖家拒绝
            if ("1".equals(detailDatas.getRepeat())) {
                ly_bottom.setVisibility(View.VISIBLE);
                tv_writeinfo.setVisibility(View.GONE);
                tv_contact.setVisibility(View.VISIBLE);
                tv_applyagin.setVisibility(View.VISIBLE);
                tv_confirm.setVisibility(View.GONE);
            }
        } else if ("3".equals(detailDatas.getReturn_status())) {//3为卖家寄出
            ly_bottom.setVisibility(View.VISIBLE);
            tv_writeinfo.setVisibility(View.GONE);
            tv_contact.setVisibility(View.GONE);
            tv_applyagin.setVisibility(View.GONE);
            tv_confirm.setVisibility(View.VISIBLE);
        } else {
            ly_bottom.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.imageView_back, R.id.tv_writeinfo, R.id.tv_contact, R.id.tv_applyagin, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_back:
                finish();
                break;
            case R.id.tv_confirm:
                DialogUtils.showDialog(mActivity, "提示", "是否确认收到货物", new DialogUtils.ButtonClickListener() {
                    @Override
                    public void negativeButton() {//取消
                    }

                    @Override
                    public void positiveButton() {//确定
                        getReturnGoodsReceivedShipping(retId);
                    }
                });

                break;
            case R.id.tv_writeinfo://填写物流信息
                Intent i = new Intent(mActivity, ApplyAddShippingInfoActivity.class);
                i.putExtra(IntentKeywords.RET_ID, retId);
                startActivity(i);
                break;
            case R.id.tv_contact://联系商家
                Resources resources = mActivity.getResources();
                if (!TextUtils.isEmpty(mApp.getConfig().getService_phone())) {
                    String call = resources.getString(R.string.setting_call_or_not);
                    final MyDialog mDialog = new MyDialog(mActivity, call, mApp.getConfig().getService_phone());
                    mDialog.setStyle(MyDialog.STYLE_POS_AND_NEG);
                    mDialog.setPositiveListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mApp.getConfig().getService_phone()));
                            startActivity(intent);
                        }
                    });
                    mDialog.setNegativeListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                        }
                    });
                    mDialog.show();

                } else {
                    String call_cannot_empty = resources.getString(R.string.setting_call_cannot_empty);
                    ToastView toast = new ToastView(mActivity, call_cannot_empty);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;
            case R.id.tv_applyagin://再次申请  //退换货类型  0仅退款   1退货    2换货)
                Intent intent = new Intent(this, ApplyBaseCustomerServiceActivity.class);
                intent.putExtra(IntentKeywords.RET_ID, retId);
                intent.putExtra(IntentKeywords.REC_ID, recId);
                intent.putExtra(IntentKeywords.ORDER_TYPE, orderTypeFlag);
                intent.putExtra(IntentKeywords.RETURN_TYPE, detailDatas.getReturn_type());
                orderGoodInrtent.setReturn_type(detailDatas.getReturn_type());
                intent.putExtra(IntentKeywords.ORDER_GOODINRTENT, JsonUtil.toString(orderGoodInrtent));
                startActivity(intent);
                break;
        }
    }

    //确认收货买家
    private void getReturnGoodsReceivedShipping(String ret_id) {
        ProgressDialogUtil.getInstance().build(mActivity).show();
        RetrofitAPIManager.getAPIGoods()
                .getReturnGoodsReceivedShipping(GoodsQuery.getInstance().getReturnDetail(ret_id))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<RETURNSTATUS>() {
                    @Override
                    public void onNext(RETURNSTATUS returnstatus) {
                        ly_bottom.setVisibility(View.GONE);
//                        if ("1".equals(returnstatus.getReturn_status())) {//1为买家收到
//                            //0为仅退款,1为退款退货,2为换货
//                            if ("2".equals(detailDatas.getReturn_type())) {
//                                ly_bottom.setVisibility(View.GONE);
//                                tv_writeinfo.setVisibility(View.VISIBLE);
//                                tv_contact.setVisibility(View.GONE);
//                            }else{
//                                ly_bottom.setVisibility(View.GONE);
//                                tv_writeinfo.setVisibility(View.GONE);
//                                tv_contact.setVisibility(View.GONE);
//                            }
//                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        T.show(mActivity, t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(retId)) {
            getHttpData(retId);
        }
    }

    @Override
    public void dispose() {

    }
}
