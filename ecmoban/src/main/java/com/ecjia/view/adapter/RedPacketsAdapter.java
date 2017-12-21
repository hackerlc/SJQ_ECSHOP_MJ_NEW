package com.ecjia.view.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.BONUS;

import java.util.ArrayList;

public class RedPacketsAdapter extends ECJiaBaseAdapter {

    private int selectedPosition = -1;// 选中的位置

    public RedPacketsAdapter(Context c, ArrayList dataList) {
        super(c, dataList);
    }

    public class RedPacketsHolder extends ECJiaCellHolder {
        public TextView redCodeTextView;
        public TextView changeMoneyTextView;
        public ImageView redPacketCheck;
    }

    @Override
    protected ECJiaCellHolder createCellHolder(View cellView) {
        RedPacketsHolder cellHolder = new RedPacketsHolder();
        cellHolder.redCodeTextView = (TextView) cellView.findViewById(R.id.red_code);
        cellHolder.changeMoneyTextView = (TextView) cellView.findViewById(R.id.change_money);
        cellHolder.redPacketCheck = (ImageView) cellView.findViewById(R.id.red_packet_check);
        return cellHolder;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    @Override
    protected View bindData(int position, View cellView, ViewGroup parent, ECJiaCellHolder h) {
        BONUS bonus = (BONUS) dataList.get(position);
        RedPacketsHolder holder = (RedPacketsHolder) h;
        holder.redCodeTextView.setText(bonus.getType_name());
        holder.changeMoneyTextView.setText(bonus.getBonus_money_formated());
        if (selectedPosition - 1 == position) {
            holder.redPacketCheck.setBackgroundResource(R.drawable.payment_selected);
        } else {
            holder.redPacketCheck.setBackgroundResource(R.drawable.payment_unselected);
        }
        return null;
    }

    @Override
    public View createCellView() {
        return LayoutInflater.from(mContext).inflate(R.layout.red_packets_cell, null);
    }
}
