package com.ecjia.view.activity.goodsdetail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.view.adapter.ECJiaBaseAdapter;
import com.ecjia.entity.responsemodel.GOODS;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Adam on 2015/3/26.
 */
public class GoodsdetailHLAdapter extends ECJiaBaseAdapter<GOODS> {

    private LayoutInflater mInflater;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    public GoodsdetailHLAdapter(Context context, ArrayList<GOODS> list) {
        super(context, list);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    protected ECJiaCellHolder createCellHolder(View cellView) {
        return null;
    }

    @Override
    protected View bindData(int position, View cellView, ViewGroup parent, ECJiaCellHolder h) {
        return null;
    }


    @Override
    public View createCellView() {
        return null;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public GOODS getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.new_goods_putaway_item, null);
            holder.photo = (ImageView) convertView.findViewById(R.id.new_goods_putaway_photo);
            holder.name = (TextView) convertView.findViewById(R.id.new_goods_putaway_name);
            holder.price = (TextView) convertView.findViewById(R.id.new_goods_putaway_price);
            holder.rightView = convertView.findViewById(R.id.right_empty);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == dataList.size() - 1 || dataList.size() == 1) {
            holder.rightView.setVisibility(View.VISIBLE);
        } else {
            holder.rightView.setVisibility(View.GONE);
        }
        holder.name.setText(dataList.get(position).getGoods_name());
        holder.price.setText(dataList.get(position).getShop_price());
        final GOODS goods = dataList.get(position);

        if (Float.parseFloat(goods.getPromote_price()) != 0) {
            String str = goods.getPromote_price().replace("￥", "").replace("元", "").replace("yuan", "").replace
                    ("¥", "");
            if ("免费".equals(str)) {
                holder.price.setText("免费");
            } else {
                holder.price.setText(goods.getFormated_promote_price());
            }
        } else if (Float.parseFloat(goods.getUnformatted_shop_price()) != 0) {
            String str = goods.getShop_price().replace("￥", "").replace("元", "").replace("yuan", "").replace
                    ("¥", "");
            if ("免费".equals(str)) {
                holder.price.setText("免费");
            } else {
                holder.price.setText(goods.getShop_price());
            }
        }

        ImageLoader.getInstance().displayImage(dataList.get(position).getImg().getThumb(), holder.photo);
        return convertView;
    }

    private static class ViewHolder {
        private TextView name;
        private TextView price;
        private ImageView photo;
        private View rightView;

    }
}