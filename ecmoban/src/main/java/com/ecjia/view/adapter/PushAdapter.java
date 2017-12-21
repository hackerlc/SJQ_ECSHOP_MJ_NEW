package com.ecjia.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.MYMESSAGE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2014/12/12.
 */
public class PushAdapter extends BaseAdapter{
    public ArrayList<MYMESSAGE> list;
    private Context context;
    Date date1,date2;
    public PushAdapter(Context context,ArrayList<MYMESSAGE> list){
        this.context=context;
        this.list=list;
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
        final ViewHolder holder;
        if(view==null){
            holder=new ViewHolder();
            view= LayoutInflater.from(context).inflate(R.layout.pushmsg_item,null);
            holder.title= (TextView) view.findViewById(R.id.push_title);
            holder.content= (TextView) view.findViewById(R.id.push_content);
            holder.time= (TextView) view.findViewById(R.id.push_time);
            holder.toptext= (TextView) view.findViewById(R.id.pushitem_toptext);
            holder.buttom=view.findViewById(R.id.pushitem_buttomview);
            holder.top=view.findViewById(R.id.pushitem_topview);
           view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        if(list.size()==1){
            holder.top.setVisibility(View.VISIBLE);
        }else{
            if(i==0){
                holder.top.setVisibility(View.VISIBLE);
            }else{
                holder.top.setVisibility(View.GONE);
            }
        }
        if(i==0){
            holder.toptext.setText(list.get(i).getTime().substring(0, 10));
            holder.toptext.setVisibility(View.VISIBLE);
        }else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date1 = df.parse(list.get(i - 1).getTime().substring(0, 10));
                date2 = df.parse(list.get(i).getTime().substring(0, 10));
                if (date1.getTime() > date2.getTime()) {
                    holder.toptext.setText(list.get(i).getTime().substring(0, 10));
                    holder.toptext.setVisibility(View.VISIBLE);
                } else if(date1.getTime()==date2.getTime()){
                    holder.toptext.setVisibility(View.GONE);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        holder.title.setText(list.get(i).getTitle());
        holder.content.setText(list.get(i).getContent());
        holder.time.setText(list.get(i).getTime());
        return view;
    }
    class ViewHolder{
        private TextView title;
        private TextView content;
        private TextView time,toptext;
        private View top,buttom;
    }

}
