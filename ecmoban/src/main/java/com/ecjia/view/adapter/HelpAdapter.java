package com.ecjia.view.adapter;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.ARTICLE;

public class HelpAdapter extends BaseAdapter {

    private Context context;
    private List<ARTICLE> list;
    private LayoutInflater inflater;

    public HelpAdapter(Context context, List<ARTICLE> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.help_item, null);

            holder.title = (TextView) convertView.findViewById(R.id.help_item_title);
            holder.firstline = convertView.findViewById(R.id.help_first_line);
            holder.endline = convertView.findViewById(R.id.help_end_line);
            holder.middlelinetop = convertView
                    .findViewById(R.id.help_middle_line_top);
            holder.middlelinebuttom = convertView
                    .findViewById(R.id.help_middle_line_buttom);

            if (list.size() == 0) {

            } else if (list.size() == 1) {
                holder.firstline.setVisibility(View.VISIBLE);
                holder.endline.setVisibility(View.VISIBLE);
            } else {
                if (position == 0) {// 多项时的第一项
                    holder.firstline.setVisibility(View.VISIBLE);
                } else if (position == list.size() - 1) {// 多项时的最后一项
                    holder.endline.setVisibility(View.VISIBLE);
                    holder.middlelinetop.setVisibility(View.VISIBLE);
                } else {// 中间项
                    holder.middlelinetop.setVisibility(View.VISIBLE);
                }
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(list.get(position).getTitle());
        return convertView;
    }

    class ViewHolder {
        private TextView title;
        View firstline, endline, middlelinetop, middlelinebuttom;
    }

}
