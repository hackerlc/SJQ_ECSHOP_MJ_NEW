package com.ecjia.widgets.home;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.view.activity.PromotionalGoodsActivity;
import com.ecjia.view.adapter.HomeNewGoodsAdapter;
import com.ecjia.entity.responsemodel.SIMPLEGOODS;

import java.util.ArrayList;

/**
 * 最新商品
 * Created by Adam on 2016/7/13.
 */
public class NewGoodsView extends HomeView<SIMPLEGOODS> implements IgnoredInterface {
    private LinearLayout newgoods_putaway;
    private LinearLayout newgoods_putaway_in;
    private LinearLayout newgoods_getmore;
    private RecyclerView hlv;
    private HomeNewGoodsAdapter hlva;

    public NewGoodsView(Activity activity) {
        super(activity);
    }

    @Override
    protected void init() {
        newgoods_putaway = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.new_goods_putaway, null);
        newgoods_putaway_in = (LinearLayout) newgoods_putaway.findViewById(R.id.new_goods_putaway_in_layout);
        newgoods_getmore = (LinearLayout) newgoods_putaway.findViewById(R.id.newgoods_getmore);
        hlv = (RecyclerView) newgoods_putaway_in.findViewById(R.id.horizontallistview1);
        newgoods_putaway_in.setVisibility(View.GONE);

        newgoods_getmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, PromotionalGoodsActivity.class);
                intent.putExtra("type", "new");
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hlv.setLayoutManager(mLayoutManager);
    }

    @Override
    public void setVisible() {
    }

    @Override
    public void addToListView(ListView listView) {
        listView.addHeaderView(newgoods_putaway);
    }

    @Override
    public void createView(final ArrayList<SIMPLEGOODS> dataList) {
        if (dataList == null || dataList.size() == 0) {
            newgoods_putaway_in.setVisibility(View.GONE);
            return;
        }
        newgoods_putaway_in.setVisibility(View.VISIBLE);
        if (null == hlva) {
            hlva = new HomeNewGoodsAdapter(mActivity, dataList);
            hlv.setAdapter(hlva);
        } else {
            hlva.notifyDataSetChanged();
        }
    }

    @Override
    public View getIgnoredView() {
        return hlv;
    }
}
