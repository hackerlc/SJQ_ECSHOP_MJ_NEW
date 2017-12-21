package com.ecjia.view.adapter.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ecjia.entity.responsemodel.CATEGORY;
import com.ecjia.util.common.DensityUtil;
import com.ecjia.view.activity.GoodsListActivity;
import com.ecjia.view.adapter.adapter.CommonAdapter;
import com.ecjia.view.adapter.adapter.MultiItemTypeAdapter;
import com.ecjia.view.adapter.adapter.base.ViewHolder;
import com.ecjia.view.adapter.adapter.my.HeaderFooterAdapter;
import com.ecjia.view.adapter.adapter.my.HeaderFooterLoadMoreAdapter;
import com.ecmoban.android.sijiqing.R;

import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-02-09.
 */

public class SearchParentAdapter extends CommonAdapter<CATEGORY> {
    private Context context;

    public SearchParentAdapter(Context context, List<CATEGORY> datas) {
        super(context, R.layout.item_searchparent, datas);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, final CATEGORY category, int position) {
        holder.setText(R.id.tv_choose, category.getName());
        holder.setOnClickListener(R.id.rl_choose, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        RecyclerView mRecyclerView = holder.getView(R.id.mRecyclerView);
        SearchChildAdapter adapter = new SearchChildAdapter(context, category.getChildren());
        //LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, adapter.getImageWith() * (category.getChildren().size() % 2 + 1));
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mRecyclerView.setLayoutParams(mParams);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        //        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(adapter);
        //        RecycleViewDivider itemDecoration = new RecycleViewDivider(context, LinearLayoutManager.VERTICAL);
        //        mRecyclerView.addItemDecoration(itemDecoration);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener<CATEGORY>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(context, GoodsListActivity.class);
                intent.putExtra("category_id", category.getChildren().get(position).getId() + "");
                context.startActivity(intent);
                //                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
    }

}

//        extends RecyclerView.Adapter<SearchParentAdapter.ViewHolder> {
//
//    public List<Objects> datas;
//    public Context context;
//    private OnItemClickLitener mOnItemClickLitener;
//
//    public interface OnItemClickLitener {
//        void onItemClick(View view, int position);
//    }
//
//    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
//        this.mOnItemClickLitener = mOnItemClickLitener;
//    }
//
//    public SearchParentAdapter(Context context, List<Objects> datas) {
//        this.datas = datas;
//        this.context = context;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_custommenu, viewGroup, false);
//        ViewHolder vh = new ViewHolder(view);
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        //处理逻辑数据
//    }
//
//    @Override
//    public int getItemCount() {
//        if (datas == null) {
//            return 0;
//        } else {
//            return datas.size();
//        }
//    }
//
//    //自定义的ViewHolder，持有每个Item的的所有界面元素
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public ViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//}
