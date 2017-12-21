package com.ecjia.view.activity.newspecialoffer;

import android.content.Context;

import com.ecjia.entity.model.News;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.view.adapter.adapter.base.ViewHolder;
import com.ecjia.view.adapter.adapter.my.HeaderFooterLoadMoreAdapter;
import com.ecmoban.android.sijiqing.R;

import java.util.List;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/4/7 10:00.
 */

public class NewsAdapter extends HeaderFooterLoadMoreAdapter<News> {
    private Context mContext;
    public NewsAdapter(Context context, List<News> datas) {
        super(context, R.layout.item_news, datas);
        mContext = context;
    }

    @Override
    public void convert(ViewHolder holder, News news, int position) {
        ImageLoaderForLV.getInstance(mContext).setImageRes(holder.getView(R.id.iv_img), news.getFileUrl());
        holder.setText(R.id.tv_title, news.getTitle());
    }
}
