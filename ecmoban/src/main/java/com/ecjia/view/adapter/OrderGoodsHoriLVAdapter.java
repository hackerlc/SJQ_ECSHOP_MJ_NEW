package com.ecjia.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.ORDER_GOODS_LIST;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Adam on 2015/9/11.
 */
public class OrderGoodsHoriLVAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    public ArrayList<ORDER_GOODS_LIST> list;
    protected ImageLoader imageLoader=ImageLoader.getInstance();
    public OrderGoodsHoriLVAdapter(Context context, ArrayList<ORDER_GOODS_LIST> list){
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
            convertView = mInflater.inflate(R.layout.goods_hori_item, null);
            holder.photo=(ImageView)convertView.findViewById(R.id.iv_new_goods);
            holder.ll_new_goods=(LinearLayout)convertView.findViewById(R.id.ll_new_goods);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        imageLoader.displayImage(list.get(position).getImg().getThumb(),holder.photo);

        return convertView;
    }

    private static class ViewHolder {
        private ImageView photo;
        private LinearLayout ll_new_goods;
    }
}