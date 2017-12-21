package com.ecjia.view.adapter.search;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ecjia.entity.responsemodel.CATEGORY;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.util.common.DensityUtil;
import com.ecjia.view.adapter.adapter.CommonAdapter;
import com.ecjia.view.adapter.adapter.base.ViewHolder;
import com.ecjia.view.adapter.adapter.my.HeaderFooterAdapter;
import com.ecmoban.android.sijiqing.R;

import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-02-10.
 */

public class SearchChildAdapter extends CommonAdapter<CATEGORY> {
    private Context context;
    private int distance;

    public SearchChildAdapter(Context context, List<CATEGORY> datas) {
        super(context, R.layout.gridview_item, datas);
        this.context = context;
        distance = (int) context.getResources().getDimension(R.dimen.seven_dp);
    }

    @Override
    public void convert(ViewHolder holder, CATEGORY category, int position) {
        ImageView img = holder.getView(R.id.iv_img);
        holder.setText(R.id.tv_text, category.getName());
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                DensityUtil.getDisplayMetricsWidth(context) * 1 / 4 - distance,
                DensityUtil.getDisplayMetricsWidth(context) * 1 / 4 - distance);
        img.setLayoutParams(mParams);
        ImageLoaderForLV.getInstance(context).setImageRes(img, category.getImage());
    }

    public  int getImageWith(){
       return (DensityUtil.getDisplayMetricsWidth(context) * 4 / 14 - distance)+(int)context.getResources().getDimension(R.dimen.px112);
    }
}
