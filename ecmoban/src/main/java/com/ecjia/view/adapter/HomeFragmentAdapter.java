package com.ecjia.view.adapter;


import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.view.activity.GoodsListActivity;
import com.ecjia.view.adapter.adaptercell.MyHotCell;
import com.ecjia.network.netmodle.HomeModel;
import com.ecjia.entity.responsemodel.CATEGORYGOODS;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.entity.responsemodel.SIMPLEGOODS;
import com.ecjia.widgets.webimageview.WebImageView;


public class HomeFragmentAdapter extends BaseAdapter {
    protected static final int TYPE_HOTSELL = 0;
    protected static final int TYPE_CATEGORYSELL = 1;
    public ArrayList<Object> mylist;
    private HomeModel dataModel;
    private Context c;
    protected ImageLoaderForLV imageLoader;

    public HomeFragmentAdapter(Context c, HomeModel dataModel) {
        this.dataModel = dataModel;
        this.c = c;
        imageLoader = ImageLoaderForLV.getInstance(c);
        this.mylist = new ArrayList<Object>();
        if (dataModel.simplegoodsList.size() > 0) {
            this.mylist.add(0, dataModel.simplegoodsList);
        }
        for (int i = 0; i < dataModel.categorygoodsList.size(); i++) {
            this.mylist.add(dataModel.categorygoodsList.get(i));
        }
    }


    @Override
    public int getViewTypeCount() {

        return 1000;
    }

    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public View getView(final int position, View cellView, ViewGroup parent) {

        ViewHolder0 holder0 = null;
        ViewHolder2 holder2 = null;
        int type = getItemViewType(position);
        if (cellView == null) {
            switch (type) {
                case TYPE_HOTSELL:
                    holder0 = new ViewHolder0();
                    cellView = LayoutInflater.from(c).inflate(
                            R.layout.home_hotsell, null);
                    holder0.myhotItem = (LinearLayout) cellView
                            .findViewById(R.id.myhot_item);
                    cellView.setTag(holder0);
                    break;
                case TYPE_CATEGORYSELL:
                    holder2 = new ViewHolder2();
                    cellView = LayoutInflater.from(c).inflate(
                            R.layout.categoryselling_cell, null);
                    holder2.good_cell_one = (LinearLayout) cellView
                            .findViewById(R.id.good_cell_one);
                    holder2.good_cell_photo_one = (WebImageView) cellView
                            .findViewById(R.id.good_cell_photo_one);
                    holder2.good_cell_name_one = (TextView) cellView
                            .findViewById(R.id.good_cell_name_one);

                    holder2.good_cell_two = (LinearLayout) cellView
                            .findViewById(R.id.good_cell_two);
                    holder2.good_cell_photo_two = (WebImageView) cellView
                            .findViewById(R.id.good_cell_photo_two);
                    holder2.good_cell_name_two = (TextView) cellView
                            .findViewById(R.id.good_cell_name_two);
                    holder2.good_cell_price_two = (TextView) cellView
                            .findViewById(R.id.good_cell_price_two);

                    holder2.good_cell_three = (LinearLayout) cellView
                            .findViewById(R.id.good_cell_three);
                    holder2.good_cell_photo_three = (WebImageView) cellView
                            .findViewById(R.id.good_cell_photo_three);
                    holder2.good_cell_name_three = (TextView) cellView
                            .findViewById(R.id.good_cell_name_three);
                    holder2.good_cell_price_three = (TextView) cellView
                            .findViewById(R.id.good_cell_price_three);

                    cellView.setTag(holder2);
                    break;
            }
        } else {
            switch (type) {
                case TYPE_HOTSELL:
                    holder0 = (ViewHolder0) cellView.getTag();
                    break;
                case TYPE_CATEGORYSELL:
                    holder2 = (ViewHolder2) cellView.getTag();
                    break;
            }
        }

        switch (type) {
            case TYPE_HOTSELL:
                inithotcell(holder0);
                break;
            case TYPE_CATEGORYSELL:

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
                            Intent it = new Intent(c, GoodsListActivity.class);
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

                            c.startActivity(it);
                            ((Activity) c).overridePendingTransition(
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
                                        Intent it = new Intent(c, GoodsDetailActivity.class);
                                        it.putExtra("goods_id", goodtwo.getId());
                                        c.startActivity(it);
                                        ((Activity) c).overridePendingTransition(
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
                                            // TODO Auto-generated method stub
                                            Intent it = new Intent(c,
                                                    GoodsDetailActivity.class);
                                            it.putExtra("goods_id", goodthree.getId());
                                            c.startActivity(it);
                                            ((Activity) c).overridePendingTransition(
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
                            Intent it = new Intent(c, GoodsListActivity.class);
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

                            c.startActivity(it);
                            ((Activity) c).overridePendingTransition(
                                    R.anim.push_right_in, R.anim.push_right_out);
                        }
                    });
                    holder2.good_cell_photo_one.setVisibility(View.INVISIBLE);
                    holder2.good_cell_two.setVisibility(View.INVISIBLE);
                    holder2.good_cell_three.setVisibility(View.INVISIBLE);
                }
                break;
        }

        return cellView;
    }

    @Override
    public Object getItem(int position) {
        if (dataModel.simplegoodsList.size() > 0) {
            if (position == 0) {
                return mylist.get(position);
            } else
                return (CATEGORYGOODS) mylist.get(position);

        } else {
            return (CATEGORYGOODS) mylist.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (dataModel.simplegoodsList.size() > 0) {
            if (position == 0) {
                return TYPE_HOTSELL;
            } else {
                return TYPE_CATEGORYSELL;
            }
        } else {
            if (mylist.size() - dataModel.categorygoodsList.size() == 1) {
                mylist.remove(0);
            }
            return TYPE_CATEGORYSELL;

        }
    }

    class ViewHolder2 {
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
    }

    class ViewHolder0 {
        public LinearLayout myhotItem;
    }

    @SuppressWarnings("unchecked")
    public void inithotcell(ViewHolder0 holder0) {
        holder0.myhotItem.removeAllViews();
        ArrayList<SIMPLEGOODS> cellData = dataModel.simplegoodsList;
        if (dataModel.simplegoodsList.size() > 0) {
            holder0.myhotItem.setVisibility(View.VISIBLE);
            MyHotCell cell = null;
            for (int i = 0; i < dataModel.simplegoodsList.size(); i = i + 2) {
                cell = (MyHotCell) LayoutInflater.from(c).inflate(
                        R.layout.home_myhotcell, null);
                cell.cellinit();

                if (cellData.size() > 0) {
                    final SIMPLEGOODS goodOne = cellData.get(i);
                    if (null != goodOne && null != goodOne.getImg()
                            && null != goodOne.getImg().getThumb()
                            && null != goodOne.getImg().getSmall()) {
                        imageLoader.setImageRes(cell.good_cell_photo_one, goodOne.getImg().getSmall());

                    }

                    if (null != goodOne.getPromote_price()
                            && goodOne.getPromote_price().length() > 0) {
                        cell.good_cell_price_one.setText(goodOne.getPromote_price());
                    } else {
                        cell.good_cell_price_one.setText(goodOne.getShop_price());
                    }

                    cell.good_info_one.setText(goodOne.getName());
                    cell.good_cell_photo_one
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    Intent it = new Intent(c,
                                            GoodsDetailActivity.class);
                                    it.putExtra("goods_id", goodOne.getId());
                                    c.startActivity(it);
                                    ((Activity) c).overridePendingTransition(
                                            R.anim.push_right_in,
                                            R.anim.push_right_out);
                                }
                            });

                }
                if (i < cellData.size() - 1) {
                    final SIMPLEGOODS goodTwo = cellData.get(i + 1);
                    if (null != goodTwo && null != goodTwo.getImg()
                            && null != goodTwo.getImg().getThumb()
                            && null != goodTwo.getImg().getSmall()) {
                        imageLoader.setImageRes(cell.good_cell_photo_two, goodTwo.getImg().getThumb());
                    }

                    if (null != goodTwo.getPromote_price()
                            && goodTwo.getPromote_price().length() > 0) {
                        cell.good_cell_price_two.setText(goodTwo.getPromote_price());
                    } else {
                        cell.good_cell_price_two.setText(goodTwo.getShop_price());
                    }

                    cell.good_info_two.setText(goodTwo.getName());
                    cell.good_cell_photo_two
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    Intent it = new Intent(c,
                                            GoodsDetailActivity.class);
                                    it.putExtra("goods_id", goodTwo.getId());
                                    c.startActivity(it);
                                    ((Activity) c).overridePendingTransition(
                                            R.anim.push_right_in,
                                            R.anim.push_right_out);
                                }
                            });

                } else {
                    cell.myhotcell2.setVisibility(View.GONE);
                }
                holder0.myhotItem.addView(cell);
            }
        } else {
            if (cellData != null) {
                cellData.clear();
            }
            holder0.myhotItem.removeAllViews();
        }
    }

}
