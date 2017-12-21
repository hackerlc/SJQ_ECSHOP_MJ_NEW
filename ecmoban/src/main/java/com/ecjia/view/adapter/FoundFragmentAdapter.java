package com.ecjia.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.QUICK;
import com.ecjia.base.ECJiaDealUtil;
import com.ecjia.util.MyBitmapUtils;
import com.ecjia.util.ecjiaopentype.ECJiaOpenType;

import java.util.ArrayList;

/**
 * Created by Adam on 2016-03-14.
 */
public class FoundFragmentAdapter extends BaseAdapter {


    private ECJiaDealUtil ecJiaDealUtil;

    ArrayList<QUICK> mList;

    Context mContext;
    AbsListView.LayoutParams params;

    int width;

    public FoundFragmentAdapter(Context context, ArrayList<QUICK> quickArrayList) {
        this.mContext = context;
        width = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
        params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.MATCH_PARENT);
        params.height = width / 3;
        this.mList = quickArrayList;
        ecJiaDealUtil = new ECJiaDealUtil(mContext);
    }

    @Override
    public int getCount() {
        int count = 0;
        if (mList.size() > 0 && mList.size() % 3 != 0) {
            count = ((mList.size() / 3) + 1) * 3;
        } else {
            count = mList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        if (position >= mList.size()) {
            return mList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (holder == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_foundadapter_item, null);
            holder.item = convertView.findViewById(R.id.found_item);
            holder.image = (ImageView) convertView.findViewById(R.id.found_item_image);
            holder.name = (TextView) convertView.findViewById(R.id.found_item_name);
            holder.add = (ImageView) convertView.findViewById(R.id.found_item_add);
            holder.rightLine = convertView.findViewById(R.id.found_item_rightline);
            convertView.setLayoutParams(params);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position >= mList.size()) {

        } else {
            if (position != mList.size()) {
                holder.add.setVisibility(View.GONE);
                holder.name.setVisibility(View.VISIBLE);
                holder.image.setVisibility(View.VISIBLE);
                MyBitmapUtils.getInstance(mContext).displayImage(holder.image, mList.get(position).getImg());
                holder.name.setText(mList.get(position).getText());
                holder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ECJiaOpenType.getDefault().startAction(mContext, mList.get(position).getUrl());
                    }
                });
                if (position % 3 == 2) {
                    holder.rightLine.setVisibility(View.GONE);
                }
            } else {
                holder.add.setVisibility(View.VISIBLE);
                holder.name.setVisibility(View.GONE);
                holder.image.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    class ViewHolder {
        public View item;
        public TextView name;
        public ImageView image;
        public ImageView add;
        public View rightLine;
    }

}
