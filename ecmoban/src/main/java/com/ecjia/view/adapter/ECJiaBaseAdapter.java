package com.ecjia.view.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public abstract class ECJiaBaseAdapter<T> extends BaseAdapter {
    protected LayoutInflater mInflater = null;
    protected Context mContext;

    public ArrayList<T> dataList = new ArrayList();
    protected static final int TYPE_ITEM = 0;
    protected static final int TYPE_FOOTER = 1;
    protected static final int TYPE_HEADER = 2;

    public class ECJiaCellHolder {
        public int position;
    }

    public ECJiaBaseAdapter(Context c, ArrayList<T> dataList) {
        mContext = c;
        mInflater = LayoutInflater.from(c);
        this.dataList = dataList;
    }

    protected abstract ECJiaCellHolder createCellHolder(View cellView);

    protected abstract View bindData(int position, View cellView, ViewGroup parent, ECJiaCellHolder h);

    public abstract View createCellView();

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getCount() {
        int count = dataList != null ? dataList.size() : 0;
        return count;
    }

    @Override
    public Object getItem(int position) {
        if (0 <= position && position < getCount()) {
            return dataList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


    public void update(int newState) {
        notifyDataSetInvalidated();
    }


    @Override
    public View getView(int position, View cellView, ViewGroup parent) {

        ECJiaCellHolder holder = null;
        if (cellView == null) {
            cellView = createCellView();
            holder = createCellHolder(cellView);
            if (null != holder) {
                cellView.setTag(holder);
            }

        } else {
            holder = (ECJiaCellHolder) cellView.getTag();
            if (holder == null) {
                Log.v("lottery", "error");
            }
        }
        if (null != holder) {
            holder.position = position;
        }

        bindData(position, cellView, parent, holder);
        return cellView;
    }
}
