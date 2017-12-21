package com.ecjia.view.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.view.activity.CommentCreateActivity;
import com.ecjia.entity.responsemodel.ORDER_COMMENTS_LIST;
import com.ecjia.util.FormatUtil;
import com.ecmoban.android.sijiqing.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class OrderDetailCommentAdapter extends BaseAdapter {

    private Resources res;
    private Context context;
    public ArrayList<ORDER_COMMENTS_LIST> list;
    private LayoutInflater inflater;
    private int type;

    public OrderDetailCommentAdapter(Context context, ArrayList<ORDER_COMMENTS_LIST> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.res=context.getResources();
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        if (list == null) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (list == null) {
            return 0;
        }
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.order_detail_comment, null);
            holder.goodsImage = (ImageView) convertView.findViewById(R.id.comment_goods_img);
            holder.goodsDsc = (TextView) convertView.findViewById(R.id.comment_goods_title);
            holder.goodsPrice = (TextView) convertView.findViewById(R.id.comment_goods_price);
            holder.comment = (TextView) convertView.findViewById(R.id.comment_item_edit);
            holder.comment_item_below_long = (View) convertView.findViewById(R.id.comment_item_below_long);
            holder.comment_item_below = (View) convertView.findViewById(R.id.comment_item_below);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == list.size()-1) {
            holder.comment_item_below.setVisibility(View.GONE);
            holder.comment_item_below_long.setVisibility(View.VISIBLE);
        } else {
            holder.comment_item_below.setVisibility(View.VISIBLE);
            holder.comment_item_below_long.setVisibility(View.GONE);
        }
        ImageLoader.getInstance().displayImage(list.get(position).getImg().getThumb(), holder.goodsImage);
        holder.goodsDsc.setText(list.get(position).getGoods_name());

        if(FormatUtil.formatStrToFloat(list.get(position).getGoods_price())==0){
            holder.goodsPrice.setText("免费");
        }else{
            holder.goodsPrice.setText(list.get(position).getGoods_price());
        }

        holder.comment.setVisibility(View.VISIBLE);
        if(list.get(position).getIs_commented()==0){
            holder.comment.setText(res.getString(R.string.comment_create));
            type=1;
        }else{
            if(list.get(position).getIs_showorder()==0){
                holder.comment.setText(res.getString(R.string.comment_create_showorder));
                type=2;
            }else{
                holder.comment.setText(res.getString(R.string.see_comment));
                type=0;
            }
        }
        list.get(position).setType(type);


        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentCreateActivity.class);
                intent.putExtra("rec_id",list.get(position).getRec_id());
                intent.putExtra("goods_price",list.get(position).getGoods_price());
                intent.putExtra("goods_name",list.get(position).getGoods_name());
                intent.putExtra("type",list.get(position).getType());
                intent.putExtra("goods_img",list.get(position).getImg().getThumb());
                ((Activity) context).startActivityForResult(intent, 1);
            }
        });

        return convertView;
    }

    class ViewHolder {
        private ImageView goodsImage;
        private TextView goodsDsc;
        private TextView goodsPrice;
        private TextView comment;
        private View comment_item_below_long,comment_item_below;
    }

}
