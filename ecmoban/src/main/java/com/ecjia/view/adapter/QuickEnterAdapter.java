package com.ecjia.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.util.LG;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.QUICK;
import com.ecjia.util.ecjiaopentype.ECJiaOpenType;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/26.
 */
public class QuickEnterAdapter extends BaseAdapter {
    private ArrayList<QUICK> quickArrayList;
    private Context context;

    public QuickEnterAdapter(Context context, ArrayList<QUICK> list) {
        this.context = context;
        this.quickArrayList = list;
    }

    @Override
    public int getCount() {
        return quickArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return quickArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        final QUICK quick = quickArrayList.get(position);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.quick_gridview_item, null);
            holder = new ViewHolder();
            holder.text = (TextView) view.findViewById(R.id.new_quick_text);
            holder.img = (ImageView) view.findViewById(R.id.new_quick_img);
            holder.quick_item = (LinearLayout) view.findViewById(R.id.quick_item);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.text.setText(quick.getText());
        ImageLoader.getInstance().displayImage(quick.getImg(), holder.img);
        holder.quick_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ECJiaOpenType.getDefault().startAction(context, quick.getUrl());
                LG.i("ECJiaOpenType==" + quick.getUrl());
            }
        });

        return view;
    }

    private class ViewHolder {
        private TextView text;
        private ImageView img;
        private LinearLayout quick_item;
    }

}
