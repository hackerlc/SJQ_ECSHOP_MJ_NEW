package com.ecjia.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.AppConst;
import com.ecjia.entity.responsemodel.SELLERGOODS;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Adam on 2015/3/26.
 */
public class ShopCollectHorizLVAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    public ArrayList<SELLERGOODS> list;
    protected ImageLoader imageLoader=ImageLoader.getInstance();
    public ShopCollectHorizLVAdapter(Context context, ArrayList<SELLERGOODS> list){
        mInflater=LayoutInflater.from(context);
        this.context=context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder =null;
        if(convertView==null){
            holder =new ViewHolder();
            convertView = mInflater.inflate(R.layout.goods_collecthoriz_item, null);
            holder.photo=(ImageView)convertView.findViewById(R.id.iv_new_goods);
            holder.iv_mb=(ImageView)convertView.findViewById(R.id.iv_mb);
            holder.price=(TextView)convertView.findViewById(R.id.new_goods_price);
            holder.ll_new_goods=(LinearLayout)convertView.findViewById(R.id.ll_new_goods);
            holder.rl_goods_more=(RelativeLayout)convertView.findViewById(R.id.rl_goods_more);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        final String finid=list.get(position).getId();
        if(!TextUtils.isEmpty(finid)&&"+1".equals(finid)){
            holder.photo.setVisibility(View.GONE);
            holder.rl_goods_more.setVisibility(View.VISIBLE);
        }else{
            holder.photo.setVisibility(View.VISIBLE);
            holder.rl_goods_more.setVisibility(View.GONE);
        }

        if(AppConst.MOBILEBUY_GOODS.equals(list.get(position).getActivity_type())){
            holder.iv_mb.setVisibility(View.VISIBLE);
        }else{
            holder.iv_mb.setVisibility(View.GONE);
        }

        holder.price.setText(list.get(position).getShop_price());
        imageLoader.displayImage(list.get(position).getImg().getThumb(),holder.photo);


        return convertView;
    }

    private static class ViewHolder {
        private TextView price ;
        private ImageView photo;
        private ImageView iv_mb;
        private LinearLayout ll_new_goods;
        private RelativeLayout rl_goods_more;
    }
}