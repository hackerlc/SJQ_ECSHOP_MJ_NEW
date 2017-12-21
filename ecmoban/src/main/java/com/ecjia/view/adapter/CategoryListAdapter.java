package com.ecjia.view.adapter;



import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.CATEGORY;

import java.util.ArrayList;

public class CategoryListAdapter extends ECJiaBaseAdapter {

    public class CategoryHolder extends ECJiaCellHolder
    {
        TextView categoryName;
        ImageView rightArrow;
        View topview;
    }
    public CategoryListAdapter(Context c, ArrayList dataList) {
        super(c, dataList);
    }
    @Override
    protected ECJiaCellHolder createCellHolder(View cellView) {
        CategoryHolder holder = new CategoryHolder();
        holder.categoryName = (TextView)cellView.findViewById(R.id.category_name);
        holder.rightArrow = (ImageView)cellView.findViewById(R.id.right_arrow);
        holder.topview=cellView.findViewById(R.id.category_top);
        return holder;
    }

    @Override
    protected View bindData(int position, View cellView, ViewGroup parent, ECJiaCellHolder h) {
        CATEGORY categoryItem = (CATEGORY)dataList.get(position);
        CategoryHolder holder = (CategoryHolder)h;
        holder.categoryName.setText(categoryItem.getName());
        if(position==0){
        	holder.topview.setVisibility(View.VISIBLE);
        }else{
        	holder.topview.setVisibility(View.GONE);
        }
        if (categoryItem.getChildren().size() > 0)
        {
            holder.rightArrow.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.rightArrow.setVisibility(View.GONE);
        }
        return cellView;
    }

    @Override
    public View createCellView() {
        View cellView = mInflater.inflate(R.layout.category_list_item,null);
        return cellView;
    }
}
