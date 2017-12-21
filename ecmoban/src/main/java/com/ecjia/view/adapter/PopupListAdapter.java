package com.ecjia.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.CATEGORY;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/20.
 */
public class PopupListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CATEGORY> list;

    public PopupListAdapter(Context context, ArrayList<CATEGORY> list) {
        this.context = context;
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        CATEGORY category=list.get(position);
        ViewHolder holder;
        if(null==view){
            view= LayoutInflater.from(context).inflate(R.layout.shoplist_headview_item,null);
            holder=new ViewHolder();
            holder.checkedview=view.findViewById(R.id.select_view);
            holder.tv_name= (TextView) view.findViewById(R.id.headview_category_name);
            holder.tv_name.setTextSize(14);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        holder.tv_name.setText(list.get(position).getName());
        if(category.isIschecked()){
            holder.checkedview.setVisibility(View.GONE);
            holder.tv_name.setTextColor(context.getResources().getColor(R.color.public_theme_color_normal));
        }else{
            holder.checkedview.setVisibility(View.GONE);
            holder.tv_name.setTextColor(context.getResources().getColor(R.color.TextColorGray));
        }
        return view;
    }

    class ViewHolder {
        private TextView tv_name;
        private View checkedview;

    }
}
