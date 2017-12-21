package com.ecjia.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.GOODS_ACTIVE;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/3/24.
 */
public class ActiveAdapter extends BaseAdapter{
    private Context c;
    public ArrayList<GOODS_ACTIVE> list;

    public Handler handler;

    public ActiveAdapter(Context c,ArrayList<GOODS_ACTIVE> list){
        this.c=c;
        this.list=list;
    }

    public void setHandler(Handler handler){
        this.handler=handler;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(null==view){
            holder=new ViewHolder();
            view=View.inflate(c, R.layout.active_item,null);
            holder.title= (TextView) view.findViewById(R.id.item_title);
            holder.content= (TextView) view.findViewById(R.id.item_content);
            holder.item= (LinearLayout) view.findViewById(R.id.active_item);
            holder.buttom= view.findViewById(R.id.buttom_view);
            holder.top= view.findViewById(R.id.top_view);
            view.setTag(holder);
        }else{
            holder=(ViewHolder)view.getTag();
        }

        holder.title.setText(list.get(i).getType_label());
        holder.content.setText(list.get(i).getName());

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.sendMessage(new Message());
            }
        });

        if(i==list.size()-1){
            holder.buttom.setVisibility(View.GONE);
        }else{
            holder.buttom.setVisibility(View.VISIBLE);
        }

        if(i==0){
            holder.top.setVisibility(View.GONE);
        }else{
            holder.top.setVisibility(View.VISIBLE);
        }

        return view;
    }

    class ViewHolder{
        TextView title;
        TextView content;
        View buttom,top;
        LinearLayout item;
    }
}
