package com.ecjia.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.PAYMENT;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/2/3.
 */
public class PayListAdapter extends BaseAdapter {
    private Context c;
    public ArrayList<PAYMENT> list = new ArrayList<PAYMENT>();

    public PayListAdapter(Context c, ArrayList<PAYMENT> list) {
        this.c = c;
        this.list = list;
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

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(c).inflate(R.layout.choose_changepay_item, null);
            holder.item = (LinearLayout) view.findViewById(R.id.pay_item);
            holder.paytext = (TextView) view.findViewById(R.id.pay_text);
            holder.shortline = view.findViewById(R.id.shortline);
            holder.longline = view.findViewById(R.id.longline);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.paytext.setText(list.get(i).getPay_name());

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemChange(view,i);
                }
                notifyDataSetChanged();
            }
        });

        if (i < list.size() - 1) {
            holder.longline.setVisibility(View.GONE);
            holder.shortline.setVisibility(View.VISIBLE);
        } else {
            holder.longline.setVisibility(View.VISIBLE);
            holder.shortline.setVisibility(View.GONE);
        }

        return view;
    }

    class ViewHolder {
        TextView paytext;
        View shortline, longline;
        LinearLayout item;
    }

    /**
     * 单击事件监听器
     */
    private onItemChangeListener mListener = null;

    public void setOnItemListener(onItemChangeListener listener) {
        mListener = listener;
    }

    public interface onItemChangeListener {
        void onItemChange(View v, int position);
    }
}
