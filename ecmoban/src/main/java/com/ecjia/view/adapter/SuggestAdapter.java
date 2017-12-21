package com.ecjia.view.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.HomeModel;
import com.ecjia.widgets.SelectableRoundedImageView;
import com.ecjia.widgets.webimageview.WebImageView;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.view.activity.GoodsListActivity;
import com.ecjia.entity.responsemodel.CATEGORYGOODS;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.entity.responsemodel.SIMPLEGOODS;
import com.ecjia.entity.responsemodel.SUGGEST;
import com.ecjia.util.ImageLoaderForLV;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SuggestAdapter extends BaseAdapter {
    protected final int TYPE_SUGGEST = 2;
    protected final int TYPE_CATEGORYSELL = 1;
    private Context context;
    protected ImageLoaderForLV imageLoader;
    private ArrayList<Object> mylist = new ArrayList<Object>();
    private int categorysize, suggestsize;
    private String recommend;//

    public SuggestAdapter(Context context, HomeModel dataModel) {
        this.context = context;
        imageLoader = ImageLoaderForLV.getInstance(context);
        categorysize = dataModel.categorygoodsList.size();
        recommend = context.getResources().getString(R.string.home_recommended);
        suggestsize = dataModel.suggests.size();
        for (int i = 0; i < categorysize; i++) {
            mylist.add(dataModel.categorygoodsList.get(i));
        }
        for (int i = 0; i < suggestsize; i++) {
            mylist.add(dataModel.suggests.get(i));

        }
    }

    public void setData(HomeModel dataModel) {
        mylist.clear();
        categorysize = dataModel.categorygoodsList.size();
        suggestsize = dataModel.suggests.size();
        for (int i = 0; i < categorysize; i++) {
            mylist.add(dataModel.categorygoodsList.get(i));
        }
        for (int i = 0; i < suggestsize; i++) {
            mylist.add(dataModel.suggests.get(i));

        }
    }


    @Override
    public Object getItem(int position) {
        return mylist.get(position);
    }

    @Override
    public int getCount() {
        if (suggestsize % 2 > 0) {
            return categorysize + suggestsize / 2 + 1;
        } else {
            return categorysize + suggestsize / 2;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ViewHolder2 holder2 = null;

        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_CATEGORYSELL:
                    holder2 = new ViewHolder2();
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.categoryselling_cell, null);
                    holder2.good_cell_one = (LinearLayout) convertView
                            .findViewById(R.id.good_cell_one);
                    holder2.good_cell_photo_one = (WebImageView) convertView
                            .findViewById(R.id.good_cell_photo_one);
                    holder2.good_cell_name_one = (TextView) convertView
                            .findViewById(R.id.good_cell_name_one);

                    holder2.good_cell_two = (LinearLayout) convertView
                            .findViewById(R.id.good_cell_two);
                    holder2.good_cell_photo_two = (WebImageView) convertView
                            .findViewById(R.id.good_cell_photo_two);
                    holder2.good_cell_name_two = (TextView) convertView
                            .findViewById(R.id.good_cell_name_two);
                    holder2.good_cell_price_two = (TextView) convertView
                            .findViewById(R.id.good_cell_price_two);

                    holder2.good_cell_three = (LinearLayout) convertView
                            .findViewById(R.id.good_cell_three);
                    holder2.good_cell_photo_three = (WebImageView) convertView
                            .findViewById(R.id.good_cell_photo_three);
                    holder2.good_cell_name_three = (TextView) convertView
                            .findViewById(R.id.good_cell_name_three);
                    holder2.good_cell_price_three = (TextView) convertView
                            .findViewById(R.id.good_cell_price_three);
                    holder2.category_foot_view = convertView.findViewById(R.id.category_foot_view);
                    holder2.first_suggest_top = (RelativeLayout) convertView.findViewById(R.id.category_suggest_title);
                    holder2.tv_suggest = (TextView) convertView.findViewById(R.id.tv_suggest);
                    convertView.setTag(holder2);
                    break;
                case TYPE_SUGGEST:
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.suggest_good_item, null);
                    holder.suggest_good_name_left = (TextView) convertView.findViewById(R.id.suggest_good_name_left);
                    holder.suggest_good_name_right = (TextView) convertView.findViewById(R.id.suggest_good_name_right);
                    holder.suggest_good_price_left = (TextView) convertView.findViewById(R.id.suggest_good_price_left);
                    holder.suggest_good_price_right = (TextView) convertView.findViewById(R.id.suggest_good_price_right);
                    holder.ll_suggest_item_left = (LinearLayout) convertView.findViewById(R.id.ll_suggest_item_left);
                    holder.ll_suggest_item_right = (LinearLayout) convertView.findViewById(R.id.ll_suggest_item_right);
                    holder.suggest_good_img_left = (SelectableRoundedImageView) convertView.findViewById(R.id.suggest_good_img_left);
                    holder.suggest_good_img_right = (ImageView) convertView.findViewById(R.id.suggest_good_img_right);

                    convertView.setTag(holder);
                    break;
            }

        } else {
            switch (type) {
                case TYPE_SUGGEST:
                    holder = (ViewHolder) convertView.getTag();
                    break;
                case TYPE_CATEGORYSELL:
                    holder2 = (ViewHolder2) convertView.getTag();
                    break;
            }
        }

        switch (type) {
            case TYPE_SUGGEST:
                initSuggest(holder, position);
                break;
            case TYPE_CATEGORYSELL:
                initCategorysell(holder2, position);


                break;
        }
        return convertView;
    }

    private void initCategorysell(ViewHolder2 holder2, final int position) {

        if (position == categorysize - 1) {
            holder2.category_foot_view.setVisibility(View.GONE);
            holder2.first_suggest_top.setVisibility(View.VISIBLE);
            Date date = new Date();
            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
            String time = format1.format(date);
            holder2.tv_suggest.setText(time + recommend);
        } else {
            holder2.category_foot_view.setVisibility(View.VISIBLE);
            holder2.first_suggest_top.setVisibility(View.GONE);
        }
        ArrayList<SIMPLEGOODS> listDatathree = ((CATEGORYGOODS) mylist
                .get(position)).getGoods();
        holder2.good_cell_name_one.setText(((CATEGORYGOODS) mylist
                .get(position)).getName());
        if (listDatathree.size() > 0) {
            holder2.good_cell_photo_one.setVisibility(View.VISIBLE);
            final SIMPLEGOODS goodone = listDatathree.get(0);
            holder2.good_cell_one.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent it = new Intent(context, GoodsListActivity.class);
                    FILTER filter = new FILTER();
                    filter.setCategory_id(String
                            .valueOf(((CATEGORYGOODS) mylist.get(position)).getId()));
                    try {
                        it.putExtra("filter", filter
                                .toJson().toString());
                    } catch (JSONException e) {

                    }
                    it.putExtra("search_content", ((CATEGORYGOODS) mylist
                            .get(position)).getName());

                    context.startActivity(it);
                    ((Activity) context).overridePendingTransition(
                            R.anim.push_right_in, R.anim.push_right_out);
                }
            });
            holder2.good_cell_name_one.setText(((CATEGORYGOODS) mylist
                    .get(position)).getName());
            imageLoader.setImageRes(holder2.good_cell_photo_one, listDatathree.get(0).getImg().getThumb());

            if (listDatathree.size() > 1) {
                holder2.good_cell_photo_one.setVisibility(View.VISIBLE);
                holder2.good_cell_two.setVisibility(View.VISIBLE);
                final SIMPLEGOODS goodtwo = listDatathree.get(1);
                holder2.good_cell_two
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent it = new Intent(context, GoodsDetailActivity.class);
                                it.putExtra("goods_id", goodtwo.getId()+"");
                                context.startActivity(it);
                                ((Activity) context).overridePendingTransition(
                                        R.anim.push_right_in,
                                        R.anim.push_right_out);
                            }
                        });
                holder2.good_cell_name_two.setText(goodtwo.getName());
                imageLoader.setImageRes(holder2.good_cell_photo_two, goodtwo.getImg().getThumb());
                holder2.good_cell_price_two.setText(goodtwo.getShop_price());

                if (listDatathree.size() > 2) {
                    holder2.good_cell_photo_one.setVisibility(View.VISIBLE);
                    holder2.good_cell_two.setVisibility(View.VISIBLE);
                    holder2.good_cell_three.setVisibility(View.VISIBLE);
                    final SIMPLEGOODS goodthree = listDatathree.get(2);
                    holder2.good_cell_three
                            .setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent it = new Intent(context, GoodsDetailActivity.class);
                                    it.putExtra("goods_id", goodthree.getId()+"");
                                    context.startActivity(it);
                                    ((Activity) context).overridePendingTransition(
                                            R.anim.push_right_in,
                                            R.anim.push_right_out);

                                }
                            });
                    holder2.good_cell_name_three.setText(goodthree.getName());
                    imageLoader.setImageRes(holder2.good_cell_photo_three, goodthree.getImg().getThumb());
                    holder2.good_cell_price_three
                            .setText(goodthree.getShop_price());
                    holder2.good_cell_three.setVisibility(View.VISIBLE);
                } else {
                    holder2.good_cell_three.setVisibility(View.INVISIBLE);
                }

            } else {
                holder2.good_cell_two.setVisibility(View.INVISIBLE);
                holder2.good_cell_three.setVisibility(View.INVISIBLE);
            }

        } else {
            holder2.good_cell_one.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent it = new Intent(context, GoodsListActivity.class);
                    FILTER filter = new FILTER();
                    filter.setCategory_id(String
                            .valueOf(((CATEGORYGOODS) mylist.get(position)).getId()));
                    try {
                        it.putExtra("filter", filter
                                .toJson().toString());
                    } catch (JSONException e) {

                    }
                    it.putExtra("search_content", ((CATEGORYGOODS) mylist
                            .get(position)).getName());

                    context.startActivity(it);
                    ((Activity) context).overridePendingTransition(
                            R.anim.push_right_in, R.anim.push_right_out);
                }
            });
            holder2.good_cell_photo_one.setVisibility(View.INVISIBLE);
            holder2.good_cell_two.setVisibility(View.INVISIBLE);
            holder2.good_cell_three.setVisibility(View.INVISIBLE);
        }
    }

    private void initSuggest(ViewHolder holder, int position) {
        SUGGEST left = (SUGGEST) mylist.get(categorysize + (position - categorysize) * 2);
        SUGGEST right = null;
        if (categorysize + (position - categorysize) * 2 + 1 < mylist.size()) {
            right = (SUGGEST) mylist.get(categorysize + (position - categorysize) * 2 + 1);
        }
        holder.suggest_good_name_left.setText(left.getName());
        if (!TextUtils.isEmpty(left.getPromote_price())) {
            holder.suggest_good_price_left.setText(left.getPromote_price());
        } else {
            holder.suggest_good_price_left.setText(left.getShop_price());
        }
        imageLoader.setImageRes(holder.suggest_good_img_left, left.getPhoto().getThumb());
        if (categorysize + (position - categorysize) * 2 + 1 < mylist.size()) {
            final String id = right.getGoods_id();
            if (TextUtils.isEmpty(id) || "0".equals(id)) {
                holder.ll_suggest_item_right.setVisibility(View.INVISIBLE);
            } else {
                holder.ll_suggest_item_right.setVisibility(View.VISIBLE);
                holder.suggest_good_name_right.setText(right.getName());
                if (!TextUtils.isEmpty(right.getPromote_price())) {
                    holder.suggest_good_price_right.setText(right.getPromote_price());
                } else {
                    holder.suggest_good_price_right.setText(right.getShop_price());
                }
                imageLoader.setImageRes(holder.suggest_good_img_right, right.getPhoto().getThumb());
            }
            final SUGGEST finalRight = right;
            holder.ll_suggest_item_right.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, GoodsDetailActivity.class);
                    intent.putExtra("goods_id", finalRight.getGoods_id());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }
            });
        } else {
            holder.ll_suggest_item_right.setVisibility(View.INVISIBLE);
        }
        final SUGGEST finalLeft = left;
        holder.ll_suggest_item_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, GoodsDetailActivity.class);
                intent.putExtra("goods_id", finalLeft.getGoods_id());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            }
        });
    }

    private class ViewHolder {
        ImageView suggest_good_img_right;
        SelectableRoundedImageView suggest_good_img_left;
        TextView suggest_good_name_left, suggest_good_name_right;
        TextView suggest_good_price_left, suggest_good_price_right;
        LinearLayout ll_suggest_item_left, ll_suggest_item_right;


    }

    private class ViewHolder2 {
        private WebImageView good_cell_photo_one;
        private WebImageView good_cell_photo_two;
        private WebImageView good_cell_photo_three;
        private TextView good_cell_name_one;
        private TextView good_cell_name_two;
        private TextView good_cell_name_three;
        private TextView good_cell_price_two;
        private TextView good_cell_price_three;

        private LinearLayout good_cell_one;
        private LinearLayout good_cell_two;
        private LinearLayout good_cell_three;
        private View category_foot_view;
        RelativeLayout first_suggest_top;
        private TextView tv_suggest;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < categorysize) {
            return TYPE_CATEGORYSELL;
        } else {
            return TYPE_SUGGEST;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 1000;
    }
}
