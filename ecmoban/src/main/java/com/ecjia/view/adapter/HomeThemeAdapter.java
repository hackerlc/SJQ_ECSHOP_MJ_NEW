package com.ecjia.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.QUICK;
import com.ecjia.base.ECJiaDealUtil;
import com.ecjia.util.ImageLoaderForLV;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/17.
 */
public class HomeThemeAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<QUICK> themelist;
    protected ImageLoaderForLV imageLoader;
    private ECJiaDealUtil ecJiaDealUtil;

    public HomeThemeAdapter(Context context, ArrayList<QUICK> list) {
        this.context = context;
        themelist = list;
        imageLoader = ImageLoaderForLV.getInstance(context);
        ecJiaDealUtil = new ECJiaDealUtil(context);
    }

    @Override
    public int getCount() {
        return themelist.size();
    }

    @Override
    public Object getItem(int i) {
        return themelist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final QUICK quick = themelist.get(i);
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.home_theme_item, null);
            holder = new ViewHolder();
            holder.theme_photo = (ImageView) view.findViewById(R.id.theme_gridview_photo);
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.detail = (TextView) view.findViewById(R.id.detail);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        imageLoader.setImageRes(holder.theme_photo, quick.getImg());
        holder.title.setText(quick.getTitle());
        holder.detail.setText(quick.getDetail());
        holder.theme_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ecJiaDealUtil.dealEcjiaAction(quick.getMap());
            }
        });
        return view;
    }

    private class ViewHolder {
        private ImageView theme_photo;
        private TextView title, detail;
    }

}
