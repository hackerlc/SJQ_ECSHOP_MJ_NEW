package com.ecjia.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.consts.IntentKeywords;
import com.ecjia.entity.responsemodel.CATEGORY;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.view.activity.business.BusinessGoodsListActivity;
import com.ecmoban.android.sijiqing.R;

import java.util.ArrayList;

public class SearchRightAdapter extends BaseAdapter {
    public ArrayList<CATEGORY> list;
    private Context context;
    private ImageLoaderForLV imageLoader;
    private int distance;

    public SearchRightAdapter(Context context, ArrayList<CATEGORY> list) {
        this.context = context;
        this.list = list;
        imageLoader = ImageLoaderForLV.getInstance(context);
        distance = (int) context.getResources().getDimension(R.dimen.seven_dp);
    }

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
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        final CATEGORY category = list.get(position);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.gridview_item, null);
            holder = new ViewHolder();
            holder.text = (TextView) view.findViewById(R.id.tv_text);
            holder.img = (ImageView) view.findViewById(R.id.iv_img);
            holder.ll_item = (LinearLayout) view.findViewById(R.id.ll_item);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.text.setText(category.getName());

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(getDisplayMetricsWidth() * 5 / 14 - distance, getDisplayMetricsWidth() * 5 / 14 - distance);
        holder.img.setLayoutParams(mParams);

        imageLoader.setImageRes(holder.img, category.getImage());
        holder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BusinessGoodsListActivity.class);
                intent.putExtra(IntentKeywords.CATEGORY_ID, category.getId() + "");
                intent.putExtra("search_content", category.getName());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

            }
        });

        return view;
    }

    private class ViewHolder {
        private TextView text;
        private ImageView img;
        private LinearLayout ll_item;
    }

    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        int j = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j);
    }
}
