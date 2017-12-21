package com.ecjia.widgets.home;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.view.activity.PromotionalGoodsActivity;
import com.ecjia.view.adapter.adaptercell.MyHotCell;
import com.ecjia.entity.responsemodel.SIMPLEGOODS;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Adam on 2016/10/14.
 */
public class PromotionView extends HomeView<SIMPLEGOODS> {
    private LinearLayout hot_big_item;
    private LinearLayout hotViewItem;
    private LinearLayout hotView;
    private LinearLayout hotView_getmore;

    public PromotionView(final Activity activity) {
        super(activity);
    }

    @Override
    protected void init() {
        super.init();
        hotView = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.home_hotsell, null);
        hotView_getmore = (LinearLayout) hotView.findViewById(R.id.home_promote_getmore);
        hotView_getmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, PromotionalGoodsActivity.class);
                intent.putExtra("type", "promotion");
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        hot_big_item = (LinearLayout) hotView.findViewById(R.id.hot_big_item);
        hotViewItem = (LinearLayout) hotView.findViewById(R.id.myhot_item);
    }

    private void setHotCellView() {
        hotViewItem.removeAllViews();
        if (mDataList.size() > 0) {
            hotViewItem.setVisibility(View.VISIBLE);
            MyHotCell cell = null;
            for (int i = 0; i < mDataList.size(); i = i + 2) {
                cell = (MyHotCell) LayoutInflater.from(mActivity).inflate(R.layout.home_myhotcell2, null);
                cell.cellinit();
                if (mDataList.size() > 0) {
                    final SIMPLEGOODS goodOne = mDataList.get(i);
                    if (null != goodOne && null != goodOne.getImg() && null != goodOne.getImg().getThumb() && null !=
                            goodOne.getImg().getSmall()) {
                        ImageLoader.getInstance().displayImage(goodOne.getImg().getThumb(), cell.good_cell_photo_one);
                    }

                    if (null != goodOne.getPromote_price() && goodOne.getPromote_price().length() > 0) {
                        cell.good_cell_price_one.setText(goodOne.getPromote_price());
                    } else {
                        cell.good_cell_price_one.setText(goodOne.getShop_price());
                    }

                    cell.good_info_one.setText(goodOne.getName());
                    cell.good_cell_one.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent it = new Intent(mActivity, GoodsDetailActivity.class);
                            it.putExtra(IntentKeywords.GOODS_ID, goodOne.getId() + "");
                            mActivity.startActivity(it);
                            mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                        }
                    });

                }
                if (i < mDataList.size() - 1) {
                    final SIMPLEGOODS goodTwo = mDataList.get(i + 1);
                    if (null != goodTwo && null != goodTwo.getImg() && null != goodTwo.getImg().getThumb() && null !=
                            goodTwo.getImg().getSmall()) {
                        ImageLoader.getInstance().displayImage(goodTwo.getImg().getThumb(), cell.good_cell_photo_two);
                    }

                    if (null != goodTwo.getPromote_price() && goodTwo.getPromote_price().length() > 0) {
                        cell.good_cell_price_two.setText(goodTwo.getPromote_price());
                    } else {
                        cell.good_cell_price_two.setText(goodTwo.getShop_price());
                    }

                    cell.good_info_two.setText(goodTwo.getName());
                    cell.good_cell_two.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent it = new Intent(mActivity, GoodsDetailActivity.class);
                            it.putExtra(IntentKeywords.GOODS_ID, goodTwo.getId() + "");
                            mActivity.startActivity(it);
                            mActivity.overridePendingTransition(R.anim.push_right_in, R.anim
                                    .push_right_out);
                        }
                    });

                } else {
                    cell.good_cell_two.setVisibility(View.INVISIBLE);
                }
                hotViewItem.addView(cell);
            }
        } else {
            if (mDataList != null) {
                mDataList.clear();
            }
            hotViewItem.removeAllViews();
        }
    }

    @Override
    public void setVisible() {

    }

    @Override
    public void addToListView(ListView listView) {
        listView.addHeaderView(hotView);
    }

    @Override
    public void createView(ArrayList<SIMPLEGOODS> dataList) {
        if (dataList == null || dataList.size() == 0) {
            hot_big_item.setVisibility(View.GONE);
            return;
        }
        hot_big_item.setVisibility(View.VISIBLE);
        mDataList = dataList;
        setHotCellView();
    }


}
