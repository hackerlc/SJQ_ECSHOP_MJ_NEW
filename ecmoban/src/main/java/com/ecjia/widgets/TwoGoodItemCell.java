package com.ecjia.widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.AppConst;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.entity.responsemodel.SIMPLEGOODS;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.util.LG;

import java.util.List;

/**
 * Created by Adam on 2015/3/17.
 */
public class TwoGoodItemCell extends LinearLayout {


    Context mContext;
    private ImageView good_cell_photo_one;
    private ImageView good_cell_photo_two;
    private AutoReturnView good_cell_name_one;
    private AutoReturnView good_cell_name_two;
    private TextView market_price_one;
    private TextView market_price_two;
    private TextView good_cell_price_one;
    private TextView good_cell_price_two;
    private TextView tv_saving_one;
    private TextView tv_saving_two;
    private LinearLayout good_cell_one;
    private LinearLayout good_cell_two;
    private LinearLayout ll_goodlist_mb_one;
    private LinearLayout ll_goodlist_mb_two;


    public TwoGoodItemCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    void init() {
        if (null == good_cell_one) {
            good_cell_one = (LinearLayout) findViewById(R.id.good_item_one);
        }

        if (null == good_cell_two) {
            good_cell_two = (LinearLayout) findViewById(R.id.good_item_two);
        }

        if (null == ll_goodlist_mb_one) {
            ll_goodlist_mb_one = (LinearLayout) good_cell_one.findViewById(R.id.ll_goodlist_mb);
        }

        if (null == ll_goodlist_mb_two) {
            ll_goodlist_mb_two = (LinearLayout) good_cell_two.findViewById(R.id.ll_goodlist_mb);
        }

        if (null == good_cell_photo_one) {
            good_cell_photo_one = (ImageView) good_cell_one.findViewById(R.id.goodlist_img);
        }

        if (null == good_cell_photo_two) {
            good_cell_photo_two = (ImageView) good_cell_two.findViewById(R.id.goodlist_img);
        }

        if (null == good_cell_name_one) {
            good_cell_name_one = (AutoReturnView) good_cell_one.findViewById(R.id.goodlist_goodname);
        }

        if (null == good_cell_name_two) {
            good_cell_name_two = (AutoReturnView) good_cell_two.findViewById(R.id.goodlist_goodname);
        }

        if (null == good_cell_price_one) {
            good_cell_price_one = (TextView) good_cell_one.findViewById(R.id.goodlist_shop_price);
        }

        if (null == good_cell_price_two) {
            good_cell_price_two = (TextView) good_cell_two.findViewById(R.id.goodlist_shop_price);
        }

        if (null == tv_saving_one) {
            tv_saving_one = (TextView) good_cell_one.findViewById(R.id.tv_saving);
        }

        if (null == tv_saving_two) {
            tv_saving_two = (TextView) good_cell_two.findViewById(R.id.tv_saving);
        }

        if (null == market_price_one) {
            market_price_one = (TextView) good_cell_one.findViewById(R.id.goodlist_promote_price);
            market_price_one.getPaint().setAntiAlias(true);
            market_price_one.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (null == market_price_two) {
            market_price_two = (TextView) good_cell_two.findViewById(R.id.goodlist_promote_price);
            market_price_two.getPaint().setAntiAlias(true);
            market_price_two.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

    }

    public void bindData(List<SIMPLEGOODS> listData) {
        init();


        if (listData.size() > 0) {
            final SIMPLEGOODS goodOne = listData.get(0);
            if (null != goodOne && null != goodOne.getImg()) {

                ImageLoaderForLV.getInstance(mContext).setImageRes(good_cell_photo_one, goodOne.getImg().getThumb());

            }

            if (null != goodOne.getShop_price() && goodOne.getShop_price().length() > 0) {
                good_cell_price_one.setText(goodOne.getShop_price());
            } else {
                good_cell_price_one.setText(goodOne.getShop_price());
            }

            LG.e("===goodOne.getActivity_type()===" + goodOne.getActivity_type());
            if (AppConst.MOBILEBUY_GOODS.equals(goodOne.getActivity_type())) {
                ll_goodlist_mb_one.setVisibility(View.VISIBLE);
                tv_saving_one.setText(goodOne.getFormatted_saving_price());
            } else {
                ll_goodlist_mb_one.setVisibility(View.GONE);
            }

            if ("免费".equals(goodOne.getShop_price())) {
                market_price_one.setVisibility(View.INVISIBLE);
            } else {
                market_price_one.setVisibility(View.VISIBLE);
                market_price_one.setText(goodOne.getMarket_price());
            }
            good_cell_name_one.setContent(goodOne.getName());

            good_cell_one.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    // TODO Auto-generated method stub
                    Intent it = new Intent(mContext, GoodsDetailActivity.class);
                    int id = goodOne.getGoods_id();
                    if (id == 0) {
                        id = goodOne.getId();
                    }
                    it.putExtra("goods_id", id);
                    mContext.startActivity(it);
                }
            });

            if (listData.size() > 1) {
                good_cell_two.setVisibility(View.VISIBLE);
                final SIMPLEGOODS goodTwo = listData.get(1);
                if (null != goodTwo && null != goodTwo.getImg()) {

                    ImageLoaderForLV.getInstance(mContext).setImageRes(good_cell_photo_two, goodTwo.getImg().getThumb());

                }

                if (null != goodTwo.getShop_price() && goodTwo.getShop_price().length() > 0) {
                    good_cell_price_two.setText(goodTwo.getShop_price());
                } else {
                    good_cell_price_two.setText(goodTwo.getShop_price());
                }

                LG.e("===goodTwo.getActivity_type()===" + goodTwo.getActivity_type());
                if (AppConst.MOBILEBUY_GOODS.equals(goodTwo.getActivity_type())) {
                    ll_goodlist_mb_two.setVisibility(View.VISIBLE);
                    tv_saving_two.setText(goodTwo.getFormatted_saving_price());
                } else {
                    ll_goodlist_mb_two.setVisibility(View.GONE);
                }

                if ("免费".equals(goodTwo.getShop_price())) {
                    market_price_two.setVisibility(View.INVISIBLE);
                } else {
                    market_price_two.setVisibility(View.VISIBLE);
                    market_price_two.setText(goodTwo.getMarket_price());
                }
                good_cell_name_two.setContent(goodTwo.getName());
                good_cell_two.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent it = new Intent(mContext, GoodsDetailActivity.class);
                        int id = goodTwo.getGoods_id();
                        if (id == 0) {
                            id = goodTwo.getId();
                        }
                        it.putExtra("goods_id", id);
                        mContext.startActivity(it);
                    }
                });

            } else {
                good_cell_two.setVisibility(View.INVISIBLE);
            }
        }
    }


}
