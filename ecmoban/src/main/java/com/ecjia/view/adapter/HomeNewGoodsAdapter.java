package com.ecjia.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.SelectableRoundedImageView;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.entity.responsemodel.SIMPLEGOODS;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Adam on 2015/3/26.
 */
public class HomeNewGoodsAdapter extends RecyclerView.Adapter<HomeNewGoodsAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private Context context;
    public List<SIMPLEGOODS> list;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    public HomeNewGoodsAdapter(Context context, List<SIMPLEGOODS> list) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_goods_putaway_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(list.get(position).getName());
        if (!TextUtils.isEmpty(list.get(position).getPromote_price())) {
            holder.price.setText(list.get(position).getPromote_price());
        } else {
            holder.price.setText(list.get(position).getShop_price());
        }
        imageLoader.displayImage(list.get(position).getImg().getSmall(), holder.photo);

        if (list.size() == 1 || position == list.size() - 1) {
            holder.rightView.setVisibility(View.VISIBLE);
        } else {
            holder.rightView.setVisibility(View.GONE);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra(IntentKeywords.GOODS_ID, list.get(position).getGoods_id() + "");
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View linearLayout;
        private TextView name;
        private TextView price;
        private SelectableRoundedImageView photo;
        private View rightView;

        public ViewHolder(View convertView) {
            super(convertView);
            linearLayout = convertView.findViewById(R.id.new_goods_putaway_ll);
            photo = (SelectableRoundedImageView) convertView.findViewById(R.id.new_goods_putaway_photo);
            name = (TextView) convertView.findViewById(R.id.new_goods_putaway_name);
            price = (TextView) convertView.findViewById(R.id.new_goods_putaway_price);
            rightView = convertView.findViewById(R.id.right_empty);
        }
    }
}