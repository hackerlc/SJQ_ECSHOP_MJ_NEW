package com.ecjia.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.GOODS_COUPON;
import com.ecjia.util.LG;

import java.util.List;

public class ReceiveRedpaperAdapter extends BaseAdapter {
    private Context context;
    public List<GOODS_COUPON> list;

    public ReceiveRedpaperAdapter(Context c, List<GOODS_COUPON> list) {
        context = c;
        this.list = list;
    }

    public void setDate(List<GOODS_COUPON> list) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_receive_redpaper_item, null);
            holder.redpaper_price_left = (TextView) convertView.findViewById(R.id.bonus_amount_left);
            holder.redpaper_price_right = (TextView) convertView.findViewById(R.id.bonus_amount_right);
            holder.redpaper_request_amount = (TextView) convertView.findViewById(R.id.request_amount);
            holder.redpaper_time = (TextView) convertView.findViewById(R.id.activity_time);
            holder.redpaper_receive = (TextView) convertView.findViewById(R.id.receiver_sure);
            holder.middle_line = convertView.findViewById(R.id.middle_line);
            holder.buttom_line = convertView.findViewById(R.id.bottom_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String str = list.get(position).getBonus_amount();
        String[] strings = str.split("\\.");

        holder.redpaper_price_left.setText(strings[0]);
        holder.redpaper_price_right.setText("." + strings[1] + "元");
        holder.redpaper_request_amount.setText(list.get(position).getRequest_amount());
        holder.redpaper_time.setText(list.get(position).getFormatted_start_date() + "--" + list.get(position).getFormatted_end_date());
        if (list.size() == 1 || position == list.size() - 1) {
            holder.buttom_line.setVisibility(View.VISIBLE);
            holder.middle_line.setVisibility(View.GONE);

        } else {
            holder.buttom_line.setVisibility(View.GONE);
            holder.middle_line.setVisibility(View.VISIBLE);

        }

        if (list.get(position).getReceived_coupon().equals("1")) {
            holder.redpaper_receive.setBackgroundResource(R.drawable.shape_gray_stroke_white_bg_corner);
            holder.redpaper_receive.setTextColor(Color.parseColor("#999999"));
            holder.redpaper_receive.setText(R.string.receive_received);
            holder.redpaper_receive.setEnabled(false);
        }
        holder.redpaper_receive.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mListener != null) {
                    mListener.receive(position, list.get(position).getBonus_type_id());
                    LG.i("list.get(position).getBonus_id()" + list.get(position).getBonus_type_id());
                }
            }
        });

        return convertView;
    }

    class ViewHolder {

        private TextView redpaper_price_left;
        private TextView redpaper_price_right;
        private TextView redpaper_request_amount;
        private TextView redpaper_time;
        private TextView redpaper_receive;
        public View middle_line;
        public View buttom_line;
    }


    public interface ReceiveRedpaperListener {
        void receive(int position, String bonus_id);
    }

    ReceiveRedpaperListener mListener;

    public void setReceiveRedpaperListener(ReceiveRedpaperListener listener) {
        mListener = listener;
    }
}
