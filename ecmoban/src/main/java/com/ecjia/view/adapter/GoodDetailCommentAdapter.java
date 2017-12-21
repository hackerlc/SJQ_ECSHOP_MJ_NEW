package com.ecjia.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.view.activity.goodsdetail.model.COMMENT_LIST;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/3/18.
 */
public class GoodDetailCommentAdapter extends BaseAdapter {

    ArrayList<COMMENT_LIST> list;
    Context c;
    int count = 0;

    public GoodDetailCommentAdapter(Context context, ArrayList<COMMENT_LIST> list) {
        this.c = context;
        this.list = list;
        count = list.size() > 5 ? 5 : list.size();
    }


    @Override
    public int getCount() {
        return count;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(c, R.layout.good_comment_item, null);
            holder.uname = (TextView) view.findViewById(R.id.user_name);
            holder.user_rank = (RatingBar) view.findViewById(R.id.user_rank);
            holder.date = (TextView) view.findViewById(R.id.add_date);
            holder.user_content = (TextView) view.findViewById(R.id.user_content);
            holder.top = view.findViewById(R.id.top_line);
            holder.buttom = view.findViewById(R.id.buttom_line);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        if (count == 1) {
            holder.buttom.setVisibility(View.VISIBLE);
        } else {
            if (i == count - 1) {
                holder.buttom.setVisibility(View.VISIBLE);
            } else {
                holder.buttom.setVisibility(View.GONE);
            }
        }

        if (list.get(i).getUser_name().length() == 11) {
            try {
                long phone = Long.valueOf(list.get(i).getUser_name());
                String fname = (phone + "").substring(0, 3) + "****" + (phone + "").substring(7, 11);
                holder.uname.setText(fname);

            } catch (NumberFormatException e) {
                e.printStackTrace();
                holder.uname.setText(list.get(i).getUser_name());
            }

        } else {
            holder.uname.setText(list.get(i).getUser_name());
        }
        holder.user_rank.setRating(Float.valueOf(list.get(i).getComment_rank()));
        holder.date.setText(list.get(i).getComment_time());
        holder.user_content.setText(list.get(i).getComment_content());


        return view;
    }

    class ViewHolder {
        TextView uname, user_content, date;
        RatingBar user_rank;
        View top, buttom;
    }
}
