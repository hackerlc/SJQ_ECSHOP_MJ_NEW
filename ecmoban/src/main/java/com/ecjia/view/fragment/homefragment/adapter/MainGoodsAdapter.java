package com.ecjia.view.fragment.homefragment.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ecjia.entity.model.SimpleGoods;
import com.ecjia.util.ImageLoaderForLV;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.ItemMainGoodsBinding;

import java.util.ArrayList;

import gear.yc.com.gearlibrary.ui.adapter.GearRecyclerViewAdapter;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/2/24 16:27.
 */

public class MainGoodsAdapter extends GearRecyclerViewAdapter<SimpleGoods,MainGoodsAdapter.Holder> {

    public MainGoodsAdapter(Context mContext, ArrayList<SimpleGoods> mData) {
        super(mContext, mData);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMainGoodsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_main_goods,parent,false);
        Holder holder =new Holder(binding);
        holder.mBinding.getRoot().setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        super.onBindViewHolder(holder, position);
        SimpleGoods goods = mData.get(position);
        holder.mBinding.getRoot().setTag(goods);
        ImageLoaderForLV.getInstance(mContext)
                .setImageRes(holder.mBinding.ivGoodsImg, goods.getImg().getThumb());
        holder.mBinding.tvName.setText(goods.getName());
        holder.mBinding.tvShowPrice.setText(goods.getShop_price());
    }

    class Holder extends RecyclerView.ViewHolder {
        ItemMainGoodsBinding mBinding;
        public Holder(ItemMainGoodsBinding binding) {
            super(binding.getRoot());
            mBinding=binding;
        }
    }
}
