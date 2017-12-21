package com.ecjia.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.SelectableRoundedImageView;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.entity.responsemodel.GROUPGOODS;
import com.ecjia.util.ImageLoaderForLV;

import java.util.ArrayList;

/**
 * 首页团购适配器
 * Created by Administrator on 2015/8/18.
 */
public class HomeGroupGoodsAdapter extends RecyclerView.Adapter<HomeGroupGoodsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<GROUPGOODS> goodslist;
    private ImageLoaderForLV imageLoader;

    public HomeGroupGoodsAdapter(Context context, ArrayList<GROUPGOODS> groupgoodsArrayList) {
        this.context = context;
        goodslist = groupgoodsArrayList;
        imageLoader = ImageLoaderForLV.getInstance(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_groupgood_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        GROUPGOODS good = goodslist.get(position);
        imageLoader.setImageRes(holder.good_img, good.getImg().getThumb());
        holder.group_good_name.setText(good.getName());
        holder.good_promoteprice.setText(good.getPromote_price());
        holder.homefragment_hot_des.setText(good.getRaminTime());


        if (position == goodslist.size() - 1 || goodslist.size() == 1) {
            holder.rightView.setVisibility(View.VISIBLE);
        } else {
            holder.rightView.setVisibility(View.GONE);
        }

        holder.good_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra(IntentKeywords.GOODS_ID, goodslist.get(position).getId() + "");
                intent.putExtra(IntentKeywords.OBJECT_ID, goodslist.get(position).getObject_id());
                intent.putExtra(IntentKeywords.REC_TYPE, goodslist.get(position).getRec_type());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return goodslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout good_item;
        private View rightView;
        private SelectableRoundedImageView good_img;
        private TextView good_promoteprice, good_shopprice, homefragment_hot_des, group_good_name;

        //
        public ViewHolder(View view) {
            super(view);
            good_item = (LinearLayout) view.findViewById(R.id.groupgood_item);
            good_img = (SelectableRoundedImageView) view.findViewById(R.id.img_groupgood);
            homefragment_hot_des = (TextView) view.findViewById(R.id.homefragment_hot_des);
            group_good_name = (TextView) view.findViewById(R.id.group_good_name);
            good_promoteprice = (TextView) view.findViewById(R.id.goods_promoteprice);
            rightView = view.findViewById(R.id.right_empty);
        }
    }

}
