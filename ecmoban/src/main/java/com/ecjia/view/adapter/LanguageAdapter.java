package com.ecjia.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ecmoban.android.sijiqing.R;

/**
 * Created by Administrator on 2015/4/27.
 */
public class LanguageAdapter extends BaseAdapter {

    private Context context;
    private String[] data;
    public Boolean[] selected;
    public Handler handler;
    Resources res;

    public LanguageAdapter(Context context, String[] data) {
        this.context = context;
        this.data = data;
        res = context.getResources();
    }


    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int i) {
        return data[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (null == view) {
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.language_item, null);
            holder.lan_text = (TextView) view.findViewById(R.id.lanitem_text);
            holder.lan_used = (ImageView) view.findViewById(R.id.lanitem_selected);
            holder.topline = view.findViewById(R.id.topline);
            holder.lan_item = (LinearLayout) view.findViewById(R.id.lan_item);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.lan_text.setText(data[i]);
        if (selected[i]) {
            holder.lan_used.setVisibility(View.VISIBLE);
        } else {
            holder.lan_used.setVisibility(View.GONE);
        }

        if ("zh".equalsIgnoreCase(data[i])) {
            holder.lan_text.setText(res.getString(R.string.Chinese));
        } else if ("en".equalsIgnoreCase(data[i])) {
            holder.lan_text.setText(res.getString(R.string.English));
        } else {
            holder.lan_text.setText(res.getString(R.string.local));
        }

        if (i == data.length - 1 || data.length == 0) {
            holder.topline.setVisibility(View.GONE);
        } else {
            holder.topline.setVisibility(View.VISIBLE);
        }

        holder.lan_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int j = 0; j < selected.length; j++) {
                    if (j == i) {
                        selected[j] = true;
                    } else {
                        selected[j] = false;
                    }
                }
                handler.sendMessage(new Message());
            }
        });

        return view;
    }

    class ViewHolder {
        TextView lan_text;
        ImageView lan_used;
        View topline;
        LinearLayout lan_item;
    }

}
