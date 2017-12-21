package com.ecjia.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.PAYMENT;
import com.ecjia.util.LG;

import java.util.ArrayList;



public class YuepaytypeAdapter extends BaseAdapter {
    public Handler parentHandler;
    private Context c;
    private ArrayList<PAYMENT> list;

    @Override
    public int getCount() {
        return list.size();
    }

    public YuepaytypeAdapter(Context c, ArrayList<PAYMENT> list,Handler handler) {
        this.c = c;
        this.list = list;
        this.parentHandler=handler;
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
        if(view==null){
            holder=new ViewHolder();
            view= LayoutInflater.from(c).inflate(R.layout.yue_item,null);
            holder.item= (LinearLayout) view.findViewById(R.id.pay_item);
            holder.paytext= (TextView) view.findViewById(R.id.pay_text);
            holder.shortline=view.findViewById(R.id.shortline);
            holder.longline=view.findViewById(R.id.longline);
            view.setTag(holder);
        }else{
            holder=(ViewHolder)view.getTag();
        }

        holder.paytext.setText(list.get(i).getPay_name());

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg=new Message();
                msg.arg1=i;
                LG.i("payid==" +i);
                parentHandler.sendMessage(msg);
            }
        });

        if(i<list.size()-1){
            holder.longline.setVisibility(View.GONE);
            holder.shortline.setVisibility(View.VISIBLE);
        }else{
            holder.longline.setVisibility(View.VISIBLE);
            holder.shortline.setVisibility(View.GONE);
        }

        return view;
    }

    class ViewHolder{
        TextView paytext;
        View shortline,longline;
        LinearLayout item;
    }
}

