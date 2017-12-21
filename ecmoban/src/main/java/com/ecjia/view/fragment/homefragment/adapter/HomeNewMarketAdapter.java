package com.ecjia.view.fragment.homefragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ecjia.consts.IntentKeywords;
import com.ecjia.entity.model.Market;
import com.ecjia.entity.model.ShopInfo;
import com.ecjia.view.activity.shop.NewHomeShopListActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.ItemMainMarketBinding;

import java.util.ArrayList;

import gear.yc.com.gearlibrary.ui.adapter.GearRecyclerViewAdapter;

/**
 * Created by Adam on 2015/3/26.
 */
public class HomeNewMarketAdapter extends GearRecyclerViewAdapter<Market, HomeNewMarketAdapter.Holder> {

    public HomeNewMarketAdapter(Context mContext, ArrayList<Market> mData) {
        super(mContext, mData);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMainMarketBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.item_main_market, parent, false);
        Holder holder = new Holder(mBinding);
        return holder;
    }

    /**
     * 这里布局写的麻烦了，后期改成add layout
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(Holder holder, int position) {
        super.onBindViewHolder(holder, position);
        Market market = mData.get(position);
        holder.mBinding.setData(market);
        if (market.getShop().size() >= 2) {
            holder.mBinding.setShop1(market.getShop().get(0));
            holder.mBinding.setShop2(market.getShop().get(1));
        } else if (market.getShop().size() == 1) {
            holder.mBinding.setShop1(market.getShop().get(0));
            holder.mBinding.setShop2(new ShopInfo());
        } else {
            holder.mBinding.setShop1(new ShopInfo());
            holder.mBinding.setShop2(new ShopInfo());
        }

        holder.mBinding.ivImg.setOnClickListener(v -> {
            mContext.startActivity(new Intent(mContext, NewHomeShopListActivity.class)
                    .putExtra("title", market.getName())
                    .putExtra("marketId", market.getId()));
        });
        holder.mBinding.rlDiv1.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, com.ecjia.view.activity.ShopListActivity.class);
            intent.putExtra(IntentKeywords.MERCHANT_ID, market.getShop().get(0).getShop_id());
            mContext.startActivity(intent);
        });
        holder.mBinding.rlDiv2.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, com.ecjia.view.activity.ShopListActivity.class);
            intent.putExtra(IntentKeywords.MERCHANT_ID, market.getShop().get(1).getShop_id());
            mContext.startActivity(intent);
        });
    }

    public class Holder extends RecyclerView.ViewHolder {
        ItemMainMarketBinding mBinding;

        public Holder(ItemMainMarketBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }

}