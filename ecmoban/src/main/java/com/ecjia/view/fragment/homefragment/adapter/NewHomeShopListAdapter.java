package com.ecjia.view.fragment.homefragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ecjia.consts.IntentKeywords;
import com.ecjia.entity.model.ShopInfo;
import com.ecjia.entity.model.SimpleGoods;
import com.ecjia.entity.responsemodel.SELLERGOODS;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.view.activity.shopdetail.ShopDetailActivity;
import com.ecjia.widgets.NpaLinearLayoutManager;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.NewSellerlistItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/22.
 */
public class NewHomeShopListAdapter extends BaseAdapter {
    private Resources res;
    private Context context;
    private List<ShopInfo> sellerlist;
    private ArrayList<SELLERGOODS> mGoodsList;
    private MainGoodsAdapter mMainGoodsAdapter;
    private ImageLoaderForLV imageloader;
    private int width, distance;

    public Handler messageHandler;

    public int eight_margin;

    public NewHomeShopListAdapter(Context context, List<ShopInfo> sellerlist, int width) {
        this.context = context;
        this.sellerlist = sellerlist;
        this.width = width;
        eight_margin = (int) context.getResources().getDimension(R.dimen.eight_margin);
        res = context.getResources();
        distance = (int) res.getDimension(R.dimen.dp_5);
        imageloader = ImageLoaderForLV.getInstance(context);
        mGoodsList = new ArrayList<>();

    }

    public void setList(ArrayList<ShopInfo> sellerlist) {
        this.sellerlist = sellerlist;
    }

    public List<ShopInfo> getList() {
        return this.sellerlist;
    }

    @Override
    public int getCount() {
        return sellerlist.size();
    }

    @Override
    public Object getItem(int position) {
        return sellerlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ShopInfo sellerinfo = sellerlist.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            holder.mBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                    R.layout.new_sellerlist_item, viewGroup, false);
            convertView = holder.mBinding.getRoot();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mBinding.setData(sellerinfo);
        holder.mBinding.rlShop.setOnClickListener(v -> {
            Intent intent = new Intent(context, com.ecjia.view.activity.ShopListActivity.class);
//            Intent intent = new Intent(context, ShopDetailActivity.class);
            intent.putExtra(IntentKeywords.MERCHANT_ID, sellerinfo.getShop_id());
            context.startActivity(intent);
        });
        NpaLinearLayoutManager lm = new NpaLinearLayoutManager(context);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.mBinding.rvGoods.setLayoutManager(lm);
        mMainGoodsAdapter = new MainGoodsAdapter(context, sellerlist.get(position).getSeller_goods());

        mMainGoodsAdapter.setOnItemClickListener((view, data) -> {
            SimpleGoods goods = (SimpleGoods)data;
            Intent intent = new Intent(context, GoodsDetailActivity.class);
            intent.putExtra(IntentKeywords.GOODS_ID,goods.getId());
            intent.putExtra(IntentKeywords.OBJECT_ID,goods.getObject_id());
            intent.putExtra(IntentKeywords.REC_TYPE,goods.getRec_type());
            context.startActivity(intent);
        });
        holder.mBinding.rvGoods.setAdapter(mMainGoodsAdapter);
        return convertView;
    }

    private class ViewHolder {
        private NewSellerlistItemBinding mBinding;
    }

    void initImageView(ImageView img) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                width / 5 - distance * 2, width / 5 - distance * 2);
        lp.setMargins(distance, distance, distance, distance);
        lp.height = width / 5 - distance * 2;
        img.setLayoutParams(lp);
    }

}
