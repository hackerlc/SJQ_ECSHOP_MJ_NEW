package com.ecjia.view.activity.goodsdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ecjia.base.LibFragment;
import com.ecjia.entity.responsemodel.PRODUCTNUMBYCF;
import com.ecjia.entity.responsemodel.SPECIFICATION_VALUE;
import com.ecjia.util.common.JsonUtil;
import com.ecjia.view.activity.goodsdetail.adapter.ChooseSizeAndCountAdapter;
import com.ecmoban.android.sijiqing.R;
import com.ybk.intent.inject.annotation.ArgExtra;
import com.ybk.intent.inject.api.IntentInject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-30.
 */

public class ChooseSizeAndCountFragment extends LibFragment{
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @ArgExtra
    String colorId;
    @ArgExtra
    String valueString;

    public List<PRODUCTNUMBYCF> selectedGoodSpec;

    ChooseSizeAndCountAdapter  adapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_choose_sizeandacount;
    }

    @Override
    public void init(View view, @Nullable Bundle savedInstanceState) {
        IntentInject.inject(this);//add this line code
        selectedGoodSpec= JsonUtil.getListObj(valueString,PRODUCTNUMBYCF.class);
        adapter = new ChooseSizeAndCountAdapter(getActivity(), selectedGoodSpec,colorId);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickLitener(new ChooseSizeAndCountAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, String circleID, int position) {

            }
        });
    }

    @Override
    public void dispose() {

    }
}
