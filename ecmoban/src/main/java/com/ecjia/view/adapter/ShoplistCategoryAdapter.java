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
 * Created by Administrator on 2015/8/28.
 */
public class ShoplistCategoryAdapter extends BaseAdapter{
    private ArrayList<CATEGORY> list=new ArrayList<CATEGORY>();
    private Context context;

    public ShoplistCategoryAdapter(Context context,ArrayList<CATEGORY> list){
        this.list=list;
        this.context=context;
    }
    //设置选择的item
    public void setCheckitem(int id){
        for(int i=0;i<list.size();i++){
            if(i==id){
                list.get(i).setIschecked(true);
            }else{
                list.get(i).setIschecked(false);
            }
        }
    }
    public ArrayList<CATEGORY> getDataList(){
        return list;
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
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        holder.tv_name.setText(list.get(position).getName());
        if(category.isIschecked()){
            holder.tv_name.setTextColor(context.getResources().getColor(R.color.public_theme_color_normal));
            holder.checkedview.setVisibility(View.VISIBLE);
        }else{
            holder.checkedview.setVisibility(View.GONE);
            holder.tv_name.setTextColor(context.getResources().getColor(R.color.TextColorGray));
        }
        return view;
    }

    private class ViewHolder{
        private TextView tv_name;
        private View checkedview;
    }
}
