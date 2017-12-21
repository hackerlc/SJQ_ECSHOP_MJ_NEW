package com.ecjia.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ecjia.entity.responsemodel.SELLERGOODS;
import com.ecjia.util.ImageLoaderForLV;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.ItemGoodsBinding;

import java.util.ArrayList;

import gear.yc.com.gearlibrary.ui.adapter.GearRecyclerViewAdapter;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/2/24 16:27.
 */

public class GoodsAdapter extends GearRecyclerViewAdapter<SELLERGOODS,GoodsAdapter.Holder> {

    public GoodsAdapter(Context mContext, ArrayList<SELLERGOODS> mData) {
        super(mContext, mData);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemGoodsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_goods,parent,false);
        Holder holder =new Holder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        super.onBindViewHolder(holder, position);
        SELLERGOODS goods = mData.get(position);
        ImageLoaderForLV.getInstance(mContext).setImageRes(holder.mBinding.ivGoodsImg, goods.getImg().getThumb());
        holder.mBinding.tvName.setText(goods.getName());
        holder.mBinding.tvShowPrice.setText(goods.getShop_price());
    }

    class Holder extends RecyclerView.ViewHolder {
        ItemGoodsBinding mBinding;
        public Holder(ItemGoodsBinding binding) {
            super(binding.getRoot());
            mBinding=binding;
        }
    }
}
