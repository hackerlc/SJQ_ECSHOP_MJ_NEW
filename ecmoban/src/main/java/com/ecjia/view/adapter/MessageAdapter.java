package com.ecjia.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.entity.responsemodel.MYMESSAGE;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.util.PushUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2015/4/27.
 */
public class MessageAdapter extends BaseAdapter {
    public ArrayList<MYMESSAGE> list;
    private Context context;
    Resources res;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");

    Date date = new Date();
    String today = df2.format(date);

    public MessageAdapter(Context context, ArrayList<MYMESSAGE> list) {
        this.context = context;
        this.list = list;
        res = context.getResources();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (null == view) {
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.layout_item_message, null);
            holder.message_content_layout = (LinearLayout) view.findViewById(R.id.message_content_layout);
            holder.date = (TextView) view.findViewById(R.id.message_date);
            holder.content = (TextView) view.findViewById(R.id.message_content);
            holder.time = (TextView) view.findViewById(R.id.message_time);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        final MYMESSAGE mMsg = list.get(list.size() - 1 - position);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(df.parse(mMsg.getDate() + " " + mMsg.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (mMsg.getUn_read() == 1) {
            holder.content.setTextColor(Color.parseColor("#111111"));
        } else {
            holder.content.setTextColor(Color.parseColor("#999999"));
        }

        holder.content.setText(mMsg.getContent());

        holder.time.setText(mMsg.getTime());

        if (position == 0) {
            holder.date.setText(getFomartDate(calendar));
        } else {
            if (!mMsg.getDate().equals(today)) {
                holder.date.setText(getFomartDate(calendar));
            } else {
                if (mMsg.getDate().equals(list.get(list.size() - position).getDate())) {
                    holder.date.setText(mMsg.getTime().substring(0, 5));
                } else {
                    holder.date.setText(getFomartDate(calendar));
                }
            }
        }
        final ViewHolder finalHolder = holder;
        holder.message_content_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushUtil.oPendAction(context, mMsg);
                mMsg.setUn_read(0);
                finalHolder.content.setTextColor(Color.parseColor("#999999"));
                if (mListener != null) {
                    mListener.changeReadStatus(mMsg.getMsg_id());
                }
            }
        });
        return view;
    }

    class ViewHolder {
        LinearLayout message_content_layout;
        TextView date;
        TextView content;
        TextView time;
    }

    //获取格式化的显示时间
    public String getFomartDate(Calendar gettime) {

        if (gettime.get(Calendar.MONTH) + 1 < 10) {

        }
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.YEAR) != gettime.get(Calendar.YEAR)) {
            return gettime.get(Calendar.YEAR) + "-" + fomarShowTime(gettime.get(Calendar.MONTH) + 1) + "-" + fomarShowTime(gettime.get(Calendar.DATE));
        } else {
            return fomarShowTime(gettime.get(Calendar.YEAR)) + "-" + fomarShowTime(gettime.get(Calendar.MONTH) + 1) + "-" + fomarShowTime(gettime.get(Calendar.DAY_OF_MONTH)) + " " + fomarShowTime
                    (gettime.get(Calendar.HOUR_OF_DAY)) + ":" + fomarShowTime(gettime.get(Calendar.MINUTE));
        }
    }

    private String fomarShowTime(int time) {
        if (time < 10) {
            return "0" + time;
        }
        return "" + time;
    }

    public String getFomartMonth(Calendar gettime) {
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.YEAR) == now.get(Calendar.YEAR) && now.get(Calendar.MONTH) == gettime.get(Calendar.MONTH)) {
            return "本月";
        } else {
            return (gettime.get(Calendar.MONTH) + 1) + "月";
        }
    }


    UpdateSql mListener;

    public interface UpdateSql {
        void changeReadStatus(String msg_id);
    }

    public void setUpdateSqlListener(UpdateSql listener) {
        mListener = listener;
    }
}
