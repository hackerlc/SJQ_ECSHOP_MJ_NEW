package com.ecjia.view.activity.goodsdetail.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.ecjia.entity.responsemodel.GOODSCOUPONS;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.query.UserQuery;
import com.ecjia.util.common.DialogUtils;
import com.ecjia.view.activity.goodsdetail.adapter.ReceiveCouponsAdapter;
import com.ecjia.view.adapter.adapter.MultiItemTypeAdapter;
import com.ecjia.widgets.dialog.MyBottomSheetDialog;
import com.ecmoban.android.sijiqing.R;

import java.util.ArrayList;
import java.util.List;

import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;
import io.reactivex.disposables.Disposable;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-31.
 */

public class BottomChooseCouponsDialog extends BottomSheetDialogFragment {

    private RecyclerView mRecyclerView;
    private List<GOODSCOUPONS> listData = new ArrayList<>();
    private ReceiveCouponsAdapter adapter;
    private FrameLayout null_pager;
    private String goodId;

    public BottomChooseCouponsDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MyBottomSheetDialog(getActivity(), getTheme());
    }

    public static BottomChooseCouponsDialog newInstance() {
        return new BottomChooseCouponsDialog();
    }

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_bottom_chooesgood_coupons, null, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        null_pager = (FrameLayout) view.findViewById(R.id.null_pager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ReceiveCouponsAdapter(getActivity(), listData);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener<GOODSCOUPONS>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if("0".equals(listData.get(position).getCan_receive())){
                    DialogUtils.showDialog(getActivity(), "提示", "是否领取优惠券", new DialogUtils.ButtonClickListener() {
                        @Override
                        public void negativeButton() {

                        }

                        @Override
                        public void positiveButton() {
                            getReceiveGoodCoupon(position);
                        }
                    });
                }
            }
        });

        view.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消dialog
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (disposable != null) {
            disposable.dispose();
        }
        if (disposableCoupon != null) {
            disposableCoupon.dispose();
        }
    }

    /**
     * 底部弹框
     */
    public void showDialog(FragmentManager manager, String goodId) {
        getGoodCoupon(goodId);
        show(manager, "tag");
    }

    /**
     * 底部弹框
     */
    public void showDialog(FragmentManager manager, List<GOODSCOUPONS> listData) {
        this.listData.clear();
        this.listData.addAll(listData);
        adapter.notifyDataSetChanged();
        show(manager, "tag");
    }

    Disposable disposableCoupon;

    public void getGoodCoupon(String id) {
        disposableCoupon = RetrofitAPIManager.getAPIUser()
                .getGoodCoupon(UserQuery.getInstance().getGoodCoupon(id))
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(d -> onCouponSuccess(d), e -> onCouponShowError(e));
    }

    public void onCouponSuccess(List<GOODSCOUPONS> goodscoupons) {
        if (goodscoupons.size() > 0) {
            null_pager.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            listData.clear();
            listData.addAll(goodscoupons);
            adapter.notifyDataSetChanged();
        } else {
            null_pager.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    public void onCouponShowError(Throwable e) {
        ToastUtil.getInstance().makeShortToast(getActivity(), e.getMessage());
        null_pager.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    Disposable disposable;

    public void getReceiveGoodCoupon(int position) {
        disposable = RetrofitAPIManager.getAPIUser()
                .getReceiveGoodCoupon(UserQuery.getInstance().getReceiveGoodCoupon(listData.get(position).getCou_id()))
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(d -> onSuccess(position, d), e -> onShowError(e));

    }

    public void onSuccess(int position, GOODSCOUPONS data) {
        ToastUtil.getInstance().makeShortToast(getActivity(), "领取成功");
        listData.get(position).setCan_receive(data.getCan_receive());
        adapter.notifyDataSetChanged();
        dismiss();
    }

    public void onShowError(Throwable e) {
        ToastUtil.getInstance().makeShortToast(getActivity(), e.getMessage());
    }

    @Override
    public void onDestroy() {
        if (disposable != null) {
            disposable.dispose();
        }
        if (disposableCoupon != null) {
            disposableCoupon.dispose();
        }
        super.onDestroy();
    }
}
