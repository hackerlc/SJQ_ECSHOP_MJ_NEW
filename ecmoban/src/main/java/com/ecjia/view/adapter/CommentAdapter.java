package com.ecjia.view.adapter;


import java.util.List;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.view.activity.goodsdetail.model.COMMENT_LIST;
import com.ecjia.util.LG;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {

    private Context context;
    public List<COMMENT_LIST> list;
    private LayoutInflater inflater;

    public CommentAdapter(Context context, List<COMMENT_LIST> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.comment_item, null);
            holder.rank = (RatingBar) convertView.findViewById(R.id.comment_rank);
            holder.name = (TextView) convertView.findViewById(R.id.user_name);
            holder.time = (TextView) convertView.findViewById(R.id.comment_time);
            holder.cont = (TextView) convertView.findViewById(R.id.comment_content);
            holder.top = convertView.findViewById(R.id.comment_div);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            holder.top.setVisibility(View.VISIBLE);
        } else {
            holder.top.setVisibility(View.GONE);
        }
        holder.rank.setRating(Float.valueOf(list.get(position).getComment_rank()));
        if (list.get(position).getUser_name().length() == 11) {
            try {
                long phone = Long.valueOf(list.get(position).getUser_name());
                String fname = (phone + "").substring(0, 3) + "****" + (phone + "").substring(7, 11);
                holder.name.setText(fname + ":");

            } catch (NumberFormatException e) {
                e.printStackTrace();
                holder.name.setText(list.get(position).getUser_name() + ":");
            }

        } else {
            holder.name.setText(list.get(position).getUser_name() + ":");
        }
        holder.cont.setText(list.get(position).getComment_content());
        LG.i("获取时间:" + list.get(position).getComment_time());
        holder.time.setText(list.get(position).getComment_time());

        return convertView;
    }

    class ViewHolder {
        private TextView name;
        private TextView time;
        private TextView cont;
        private RatingBar rank;
        private View top;
    }

}
