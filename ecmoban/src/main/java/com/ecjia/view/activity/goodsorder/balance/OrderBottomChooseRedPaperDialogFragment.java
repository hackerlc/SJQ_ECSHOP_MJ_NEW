package com.ecjia.view.activity.goodsorder.balance;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.entity.eventmodel.EventShopCoupons;
import com.ecjia.entity.eventmodel.GoodRedPaperEvent;
import com.ecjia.entity.responsemodel.BONUS;
import com.ecjia.view.adapter.adapter.MultiItemTypeAdapter;
import com.ecjia.widgets.dialog.MyBottomSheetDialog;
import com.ecmoban.android.sijiqing.R;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;

/**
 * 类名介绍：订单提交选择商城红包
 * Created by sun
 * Created time 2017-03-31.
 */

public class OrderBottomChooseRedPaperDialogFragment extends BottomSheetDialogFragment {

    private RecyclerView mRecyclerView;
    private List<BONUS> listData = new ArrayList<>();
    private OrderBottomChooseRedPaperAdapter adapter;
    private String goodId;
    private LinearLayout ly_redpage;
    private TextView tv_title;


    public OrderBottomChooseRedPaperDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MyBottomSheetDialog(getActivity(), getTheme());
    }

    public static OrderBottomChooseRedPaperDialogFragment newInstance() {
        return new OrderBottomChooseRedPaperDialogFragment();
    }

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_order_bottom_chooesgood_coupons, null, false);
        ly_redpage = (LinearLayout) view.findViewById(R.id.ly_redpage);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
//        ly_redpage.setVisibility(View.VISIBLE);
        tv_title.setText("红包选择");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new OrderBottomChooseRedPaperAdapter(getActivity(), listData);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener<BONUS>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                if(listData.get(position)){}
                for (int i = 0; i < listData.size(); i++) {
                    listData.get(i).setIschecked(false);
                }
//                if(!listData.get(position).isIschecked()){
                    listData.get(position).setIschecked(true);
//                }
                adapter.notifyDataSetChanged();
                sendRxbus();
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
    public void showDialog(FragmentManager manager, List<BONUS> listData) {
        this.listData.clear();
        this.listData.addAll(listData);
//        adapter.notifyDataSetChanged();
        show(manager, "tag");
    }

    public void sendRxbus() {
        RxBus.getInstance().post(RxBus.TAG_DEFAULT, listData);
//        EventBus.getDefault().post(new GoodRedPaperEvent("REDPAPER",listData));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
