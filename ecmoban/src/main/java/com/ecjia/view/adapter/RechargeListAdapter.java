package com.ecjia.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.ECJiaApplication;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.view.activity.RechargeDetailActivity;
import com.ecjia.entity.responsemodel.RECHARGE_INFO;
import com.ecjia.util.ProfilePhotoUtil;
import com.ecjia.util.TimeUtil;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Administrator on 2015/3/16.
 */
public class RechargeListAdapter extends BaseAdapter {
    private Context c;
    public ArrayList<RECHARGE_INFO> list;
    Calendar nowtime, rechargetime, lastrechargetime, nextrechargetime;//当前时间和获得的时间
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Resources res;
    ECJiaApplication mApp;

    public RechargeListAdapter(Context context, ArrayList<RECHARGE_INFO> list) {
        this.c = context;
        mApp = (ECJiaApplication) c.getApplicationContext();
        this.list = list;
        res = c.getResources();
        nowtime = Calendar.getInstance();
        rechargetime = Calendar.getInstance();
        lastrechargetime = Calendar.getInstance();
        nextrechargetime = Calendar.getInstance();
        profileBitmap = ProfilePhotoUtil.getInstance().getProfileBitmap(mApp.getUser().getId());
    }

    public Bitmap profileBitmap;

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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(c, R.layout.rechargeitem, null);
            holder.topview = view.findViewById(R.id.top_line);
            holder.buttomview = view.findViewById(R.id.buttom_line);
            holder.shortview = view.findViewById(R.id.top_short_line);

            holder.head_img = (ImageView) view.findViewById(R.id.head_img);

            holder.title_item = (LinearLayout) view.findViewById(R.id.title_item);
            holder.title_time = (TextView) view.findViewById(R.id.title_time);
            holder.recharge_type = (TextView) view.findViewById(R.id.process_type);
            holder.amount = (TextView) view.findViewById(R.id.recharge_amount);
            holder.create_time = (TextView) view.findViewById(R.id.create_time);
            holder.status = (TextView) view.findViewById(R.id.pay_status);
            holder.re_item = (LinearLayout) view.findViewById(R.id.re_item);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        try {
            rechargetime.setTime(df.parse(list.get(i).getAdd_time()));
            if (i > 0) {
                lastrechargetime.setTime(df.parse(list.get(i - 1).getAdd_time()));
            }
            if (i < list.size() - 1) {
                nextrechargetime.setTime(df.parse(list.get(i + 1).getAdd_time()));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (list.size() == 1) { //只有一条
            holder.title_item.setVisibility(View.VISIBLE);
            holder.topview.setVisibility(View.VISIBLE);
            holder.shortview.setVisibility(View.GONE);
            holder.buttomview.setVisibility(View.VISIBLE);
        } else {//多条
            if (i == 0) { //第一项
                holder.title_item.setVisibility(View.VISIBLE);
                holder.topview.setVisibility(View.VISIBLE);
                holder.shortview.setVisibility(View.GONE);
                if (rechargetime.get(Calendar.MONTH) == nextrechargetime.get(Calendar.MONTH) && nextrechargetime.get(Calendar.YEAR) == rechargetime.get(Calendar.YEAR)) {  //是否在同年同月
                    holder.buttomview.setVisibility(View.GONE);
                } else {
                    holder.buttomview.setVisibility(View.VISIBLE);
                }


            } else if (i == list.size() - 1) { //最后一项

                holder.buttomview.setVisibility(View.VISIBLE);
                if (rechargetime.get(Calendar.MONTH) == lastrechargetime.get(Calendar.MONTH) && lastrechargetime.get(Calendar.YEAR) == rechargetime.get(Calendar.YEAR)) {  //是否在同年同月
                    holder.shortview.setVisibility(View.VISIBLE);
                    holder.topview.setVisibility(View.GONE);
                    holder.title_item.setVisibility(View.GONE);
                } else {
                    holder.shortview.setVisibility(View.GONE);
                    holder.topview.setVisibility(View.VISIBLE);
                    holder.title_item.setVisibility(View.VISIBLE);
                }

            } else { //中间项

                if (rechargetime.get(Calendar.MONTH) == lastrechargetime.get(Calendar.MONTH) && lastrechargetime.get(Calendar.YEAR) == rechargetime.get(Calendar.YEAR)) {  //是否在同年同月
                    holder.shortview.setVisibility(View.VISIBLE);
                    holder.topview.setVisibility(View.GONE);
                    holder.title_item.setVisibility(View.GONE);
                } else {
                    holder.shortview.setVisibility(View.GONE);
                    holder.topview.setVisibility(View.VISIBLE);
                    holder.title_item.setVisibility(View.VISIBLE);
                }

                if (rechargetime.get(Calendar.MONTH) == nextrechargetime.get(Calendar.MONTH) && nextrechargetime.get(Calendar.YEAR) == rechargetime.get(Calendar.YEAR)) {  //是否在同年同月
                    holder.buttomview.setVisibility(View.GONE);
                } else {
                    holder.buttomview.setVisibility(View.VISIBLE);
                }


            }
        }

        holder.title_time.setText(TimeUtil.getFomartMonth(rechargetime));
        holder.create_time.setText(TimeUtil.getFomartDate(rechargetime, c));
        holder.recharge_type.setText(mApp.getUser().getName() + "-" + list.get(i).getType_lable());
        setImage(holder.head_img);

        if ("deposit".equals(list.get(i).getType())) {
            holder.amount.setText(list.get(i).getAmount());
        } else if ("raply".equals(list.get(i).getType())) {
            holder.amount.setText(list.get(i).getAmount());
        }

        if ("1".equals(list.get(i).getIs_paid())) {
            holder.status.setTextColor(Color.parseColor("#999999"));
            holder.status.setText(list.get(i).getPay_status());
        } else {
            holder.status.setTextColor(Color.parseColor("#999999"));
            holder.status.setText(c.getResources().getString(R.string.running));
        }
        holder.re_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(c, RechargeDetailActivity.class);
                try {
                    intent.putExtra("data", list.get(i).toJson().toString());
                    c.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        return view;
    }

    private void setImage(ImageView imageView) {
        if (profileBitmap != null) {
            imageView.setImageBitmap(profileBitmap);
        } else {
            imageView.setImageResource(R.drawable.profile_no_avarta_icon_light);
        }
    }

    class ViewHolder {
        ImageView head_img;
        View topview, shortview, buttomview;
        LinearLayout title_item, re_item;
        TextView title_time;
        TextView recharge_type;
        TextView create_time;
        TextView amount;
        TextView status;
    }

}
