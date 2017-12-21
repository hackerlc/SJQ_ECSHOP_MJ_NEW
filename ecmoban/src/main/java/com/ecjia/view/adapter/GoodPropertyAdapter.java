package com.ecjia.view.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.PROPERTY;

import java.util.ArrayList;


public class GoodPropertyAdapter extends ECJiaBaseAdapter {
    public GoodPropertyAdapter(Context c, ArrayList dataList) {
        super(c, dataList);
    }

    public class PropertyCellHolder extends ECJiaCellHolder {
        TextView property_name;
        TextView property_value;
        View top, normal, bottom;
    }

    @Override
    protected ECJiaCellHolder createCellHolder(View cellView) {
        PropertyCellHolder cellHolder = new PropertyCellHolder();
        cellHolder.property_name = (TextView) cellView.findViewById(R.id.property_name);
        cellHolder.property_value = (TextView) cellView.findViewById(R.id.property_value);
        cellHolder.top = cellView.findViewById(R.id.pro_top);
        cellHolder.bottom = cellView.findViewById(R.id.bottom_line);
        cellHolder.normal = cellView.findViewById(R.id.normal_line);
        return cellHolder;
    }

    @Override
    protected View bindData(final int position, View cellView, ViewGroup parent, ECJiaCellHolder h) {
        PROPERTY property = (PROPERTY) dataList.get(position);
        ((PropertyCellHolder) h).property_name.setText(property.getName());
        ((PropertyCellHolder) h).property_value.setText(property.getValue());
        if (position == 0) {
            ((PropertyCellHolder) h).top.setVisibility(View.VISIBLE);
        } else {
            ((PropertyCellHolder) h).top.setVisibility(View.GONE);
        }
        if (dataList.size() == 1 || position == (dataList.size() - 1)) {
            ((PropertyCellHolder) h).bottom.setVisibility(View.VISIBLE);
            ((PropertyCellHolder) h).normal.setVisibility(View.GONE);
        } else {
            ((PropertyCellHolder) h).bottom.setVisibility(View.GONE);
            ((PropertyCellHolder) h).normal.setVisibility(View.VISIBLE);
        }

        return cellView;
    }

    @Override
    public View createCellView() {
        return LayoutInflater.from(mContext).inflate(R.layout.good_property_cell, null);
    }
}
