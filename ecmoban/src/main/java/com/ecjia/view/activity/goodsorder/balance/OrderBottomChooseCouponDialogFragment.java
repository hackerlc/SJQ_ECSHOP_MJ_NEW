package com.ecjia.view.activity.goodsorder.balance;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.entity.eventmodel.EventShopCoupons;
import com.ecjia.entity.responsemodel.GOODSCOUPONS;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.query.UserQuery;
import com.ecjia.util.common.DialogUtils;
import com.ecjia.view.activity.goodsdetail.adapter.ReceiveCouponsAdapter;
import com.ecjia.view.activity.goodsorder.balance.entity.NewBlanceShopData;
import com.ecjia.view.activity.goodsorder.balance.entity.ShopCouponsData;
import com.ecjia.view.adapter.adapter.MultiItemTypeAdapter;
import com.ecjia.widgets.dialog.MyBottomSheetDialog;
import com.ecmoban.android.sijiqing.R;

import java.util.ArrayList;
import java.util.List;

import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;
import io.reactivex.disposables.Disposable;

/**
 * 类名介绍：订单提交选择店铺优惠券
 * Created by sun
 * Created time 2017-03-31.
 */

public class OrderBottomChooseCouponDialogFragment extends BottomSheetDialogFragment {

    private RecyclerView mRecyclerView;
    private List<ShopCouponsData> listData = new ArrayList<>();
    private OrderBottomChooseCouponAdapter adapter;
    private String goodId;
    private LinearLayout ly_redpage;
    private TextView tv_title;

    private int postion;

    public OrderBottomChooseCouponDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MyBottomSheetDialog(getActivity(), getTheme());
    }

    public static OrderBottomChooseCouponDialogFragment newInstance() {
        return new OrderBottomChooseCouponDialogFragment();
    }

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_order_bottom_chooesgood_coupons, null, false);
        ly_redpage = (LinearLayout) view.findViewById(R.id.ly_redpage);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        tv_title.setText("优惠券选择");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new OrderBottomChooseCouponAdapter(getActivity(), listData);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener<ShopCouponsData>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                for (int i = 0; i <listData.size() ; i++) {
                    listData.get(i).setSelect(false);
                }
                if(!listData.get(position).isSelect()){
                    listData.get(position).setSelect(true);
                }
                adapter.notifyDataSetChanged();
                sendRxbus();
            }
        });

        view.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRxbus();
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
    public void showDialog(FragmentManager manager, List<ShopCouponsData> listData,int postion) {
        this.listData.clear();
        this.listData.addAll(listData);
        this.postion=postion;
//        adapter.notifyDataSetChanged();
        show(manager, "tag");
    }

    public void sendRxbus(){
        EventShopCoupons eventShopCoupons=new EventShopCoupons();
        eventShopCoupons.setPosition(postion);
        eventShopCoupons.setListData(listData);
        RxBus.getInstance().post(RxBus.TAG_CHANGE, eventShopCoupons);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
