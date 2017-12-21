package com.ecjia.view.activity.goodsdetail.fragment.interfaces;

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
import android.widget.TextView;

import com.ecjia.entity.responsemodel.FAVOUR;
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

public class BottomShowShopDiscountDialog extends BottomSheetDialogFragment {

    private RecyclerView mRecyclerView;
    private List<FAVOUR> listData = new ArrayList<>();
    private BottomShowShopDiscountAdapter adapter;
    private TextView tv_title;


    public BottomShowShopDiscountDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MyBottomSheetDialog(getActivity(), getTheme());
    }

    public static BottomShowShopDiscountDialog newInstance() {
        return new BottomShowShopDiscountDialog();
    }

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_order_bottom_chooesgood_coupons, null, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("店铺优惠");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BottomShowShopDiscountAdapter(getActivity(), listData);
        mRecyclerView.setAdapter(adapter);
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
    }

    /**
     * 底部弹框
     */
    public void showDialog(FragmentManager manager, String goodId) {
        show(manager, "tag");
    }

    /**
     * 底部弹框
     */
    public void showDialog(FragmentManager manager, List<FAVOUR> listData) {
        this.listData.clear();
        this.listData.addAll(listData);
//        adapter.notifyDataSetChanged();
        show(manager, "tag");
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
