package com.ecjia.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.view.activity.TopicDetailActivity;
import com.ecjia.entity.responsemodel.TOPIC;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/18.
 */
public class TopiclistAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<TOPIC> topics;
    private int width, distance;

    public TopiclistAdapter(Context context, ArrayList<TOPIC> topic_list) {
        this.context = context;
        width = getDisplayMetricsWidth();
        topics = topic_list;
        distance = (int) context.getResources().getDimension(R.dimen.dp_15);
    }


    @Override
    public int getCount() {
        return topics.size();
    }

    @Override
    public Object getItem(int i) {
        return topics.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final TOPIC topic = topics.get(i);
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.topic_list_item, null);
            holder = new ViewHolder();
            holder.img_topic = (ImageView) view.findViewById(R.id.img_topic);
            holder.text_topic = (TextView) view.findViewById(R.id.text_topic);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.width = width - 2 * distance;
            params.height = (width - 2 * distance) * 2 / 5;
            params.gravity = Gravity.CENTER_HORIZONTAL;
            params.setMargins(0, distance, 0, 0);
            holder.img_topic.setLayoutParams(params);
            FrameLayout.LayoutParams textparams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            textparams.height = (width - 2 * distance) * 2 / 5;
            textparams.width = textparams.height * 3 / 4;
            textparams.gravity = Gravity.RIGHT;
            textparams.setMargins(0, distance, distance, 0);
            holder.text_topic.setLayoutParams(textparams);
            holder.text_topic.setGravity(Gravity.CENTER);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ImageLoader.getInstance().displayImage(topic.getTopic_image(), holder.img_topic);
        holder.text_topic.setText(topic.getTopic_title());
        holder.img_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TopicDetailActivity.class);
                intent.putExtra("topic_id", topic.getTopic_id());
                context.startActivity(intent);
            }
        });
        return view;
    }

    private class ViewHolder {
        private ImageView img_topic;
        private TextView text_topic;
    }

    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        int j = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j);
    }
}
