package com.ecjia.view.adapter.search;

import android.content.Context;

import com.ecjia.entity.responsemodel.HOTSEARCH;
import com.ecjia.view.adapter.adapter.CommonAdapter;
import com.ecjia.view.adapter.adapter.base.ViewHolder;
import com.ecmoban.android.sijiqing.R;

import java.util.List;

/**
 * 类名介绍：热门搜索列表adapter
 * Created by sun
 * Created time 2017-02-14.
 */
public class SearchHotAdapter extends CommonAdapter<HOTSEARCH> {

    private Context context;

    public SearchHotAdapter(Context context, List<HOTSEARCH> datas) {
        super(context, R.layout.item_search_hot, datas);
        this.context=context;
    }

    @Override
    protected void convert(ViewHolder holder, HOTSEARCH hotsearch, int position) {
        holder.setText(R.id.name, hotsearch.getName()+"");
    }
}
