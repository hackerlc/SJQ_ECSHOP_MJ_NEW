package com.ecjia.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.SelectableRoundedImageView;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.entity.responsemodel.SUGGEST;
import com.ecjia.util.FormatUtil;
import com.ecjia.util.ImageLoaderForLV;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/29.
 */
public class NewPromoteListAdapter extends BaseAdapter {
    private final int distance;
    private Context context;
    private ArrayList<SUGGEST> list = new ArrayList<SUGGEST>();

    public NewPromoteListAdapter(Context context, ArrayList<SUGGEST> list) {
        this.context = context;
        this.list = list;
        distance=(int)context.getResources().getDimension(R.dimen.dp_10);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setData(ArrayList<SUGGEST> list) {
        this.list = list;
    }


    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        final SUGGEST suggest=list.get(position);
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.promote_list_item, null);

            holder.ll_promote_item = (LinearLayout) view.findViewById(R.id.ll_promote_item);
            holder.fl_promote_item= (FrameLayout) view.findViewById(R.id.fl_promote_item);
            holder.iv_promote = (SelectableRoundedImageView) view.findViewById(R.id.iv_promote);
            holder.ll_promote_time = (LinearLayout) view.findViewById(R.id.ll_promote_time);
            holder.tv_promote_time_day= (TextView) view.findViewById(R.id.tv_promote_time_day);
            holder.tv_promote_time_hour= (TextView) view.findViewById(R.id.tv_promote_time_hour);
            holder.tv_promote_time_min= (TextView) view.findViewById(R.id.tv_promote_time_min);
            holder.tv_promote_time_sec= (TextView) view.findViewById(R.id.tv_promote_time_sec);
            holder.tv_promote_goods_name= (TextView) view.findViewById(R.id.tv_promote_goods_name);
            holder.tv_promote_goods_price= (TextView) view.findViewById(R.id.tv_promote_goods_price);
            holder.tv_promote_goods_origin_price= (TextView) view.findViewById(R.id.tv_promote_goods_origin_price);
            holder.tv_promote_goods_origin_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            holder.bottom_view=  view.findViewById(R.id.bottom_view);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if(position==list.size()-1){
            holder.bottom_view.setVisibility(View.VISIBLE);
        }else{
            holder.bottom_view.setVisibility(View.GONE);
        }

        ViewGroup.LayoutParams params = holder.fl_promote_item.getLayoutParams();
        params.width = (getDisplayMetricsWidth()-distance*2);
        params.height = (int)(params.width*0.8);
        holder.fl_promote_item.setLayoutParams(params);

        holder.tv_promote_goods_name.setText(suggest.getName());
        ImageLoaderForLV.getInstance(context).setImageRes(holder.iv_promote, suggest.getPhoto().getThumb());


        float promote = FormatUtil.formatStrToFloat(suggest.getPromote_price());
        if (promote!=0) {
            holder.tv_promote_goods_price.setText(suggest.getPromote_price());
            holder.tv_promote_goods_origin_price.setText(suggest.getShop_price());
        } else {
            float str = FormatUtil.formatStrToFloat(suggest.getShop_price());
            if (str==0) {
                holder.tv_promote_goods_price.setText("免费");
                holder.tv_promote_goods_origin_price.setText("");
            } else {
                holder.tv_promote_goods_price.setText(suggest.getShop_price());
                holder.tv_promote_goods_origin_price.setText(suggest.getMarket_price());
            }
        }

        if(null==suggest.getPromotetime()|| TextUtils.isEmpty(suggest.getPromote_start_date())
                || TextUtils.isEmpty(suggest.getPromote_end_date())){
            holder.tv_promote_time_day.setText("");
            holder.tv_promote_time_hour.setText("00");
            holder.tv_promote_time_min.setText("00");
            holder.tv_promote_time_sec.setText("00");
        }else{
            holder.tv_promote_time_day.setText(suggest.getPromotetime().getDay());
            holder.tv_promote_time_hour.setText(suggest.getPromotetime().getHour());
            holder.tv_promote_time_min.setText(suggest.getPromotetime().getMinute());
            holder.tv_promote_time_sec.setText(suggest.getPromotetime().getSecond());
        }


        holder.ll_promote_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, GoodsDetailActivity.class);
                intent.putExtra("goods_id", suggest.getGoods_id());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            }
        });

        return view;
    }

    private class ViewHolder {
        private LinearLayout ll_promote_item;
        private FrameLayout fl_promote_item;
        private SelectableRoundedImageView iv_promote;
        private LinearLayout ll_promote_time;
        private TextView tv_promote_time_day;
        private TextView tv_promote_time_hour;
        private TextView tv_promote_time_min;
        private TextView tv_promote_time_sec;
        private TextView tv_promote_goods_name;
        private TextView tv_promote_goods_price;
        private TextView tv_promote_goods_origin_price;
        private View bottom_view;
    }

    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        int j = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j);
    }
}
