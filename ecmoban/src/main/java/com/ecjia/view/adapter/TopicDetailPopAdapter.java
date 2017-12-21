package com.ecjia.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.TOPIC_TYPE;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/22.
 */
public class TopicDetailPopAdapter extends BaseAdapter{
    private Context context;
    public ArrayList<TOPIC_TYPE> types;
    private int selected_color;
    public TopicDetailPopAdapter(Context context,ArrayList<TOPIC_TYPE> type){
        this.context=context;
        selected_color=context.getResources().getColor(R.color.public_theme_color_normal);
        types=type;
        types.add(0, new TOPIC_TYPE(context.getResources().getString(R.string.all)));
    }
    @Override
    public int getCount() {
        return types.size();
    }

    public void setSelectedPosition(int position){
        for(int i=0;i<types.size();i++){
            if(i==position){
                types.get(i).setSelected(true);
            }else{
                types.get(i).setSelected(false);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int i) {
        return types.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        TOPIC_TYPE topic_type=types.get(i);
        ViewHolder holder;
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.topic_detail_pop_item,null);
            holder=new ViewHolder();
            holder.type_text= (TextView) view.findViewById(R.id.detail_text);
            holder.short_line=view.findViewById(R.id.buttom_short_line);
            holder.long_line=view.findViewById(R.id.buttom_long_line);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        holder.type_text.setText(topic_type.getType_text());
        if(types.size()==1){
            holder.short_line.setVisibility(View.GONE);
            holder.long_line.setVisibility(View.VISIBLE);
        }else{
            if(i<types.size()-1){
                holder.short_line.setVisibility(View.VISIBLE);
                holder.long_line.setVisibility(View.GONE);
            }else{
                holder.short_line.setVisibility(View.GONE);
                holder.long_line.setVisibility(View.VISIBLE);
            }
        }

        holder.type_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener!=null){
                    mListener.onRightItemClick(view,i);
                }
            }
        });

        if(topic_type.isSelected()){
            holder.type_text.setTextColor(selected_color);
        }else{
            holder.type_text.setTextColor(Color.BLACK);
        }

        return view;
    }
    private class ViewHolder{
        private TextView type_text;
        private View short_line,long_line;
    }

    /**
     * 单击事件监听器
     */
    private onRightItemClickListener mListener = null;

    public void setOnRightItemClickListener(onRightItemClickListener listener) {
        mListener = listener;
    }

    public interface onRightItemClickListener {
        void onRightItemClick(View v, int position);
    }

}
