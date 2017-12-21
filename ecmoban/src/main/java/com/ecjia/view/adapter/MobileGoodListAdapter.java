package com.ecjia.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.AutoReturnView;
import com.ecjia.widgets.SelectableRoundedImageView;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.entity.responsemodel.MOBILEGOODS;
import com.ecjia.util.FormatUtil;
import com.ecjia.util.ImageLoaderForLV;

import java.util.ArrayList;

/**手机专享列表的adapter
 * Created by Administrator on 2016/9/29.
 */
public class MobileGoodListAdapter extends BaseAdapter{
    private final int distance;
    private Context context;
    private ArrayList<MOBILEGOODS> mobilegoodses=new ArrayList<MOBILEGOODS>();
    public MobileGoodListAdapter(Context context, ArrayList<MOBILEGOODS> mobilegoodses){
        this.context=context;
        this.mobilegoodses=mobilegoodses;
        distance=(int)context.getResources().getDimension(R.dimen.good_list_distance);
    }
    @Override
    public int getCount() {
        return mobilegoodses.size();
    }

    @Override
    public Object getItem(int i) {
        return mobilegoodses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setData(ArrayList<MOBILEGOODS> list){
        mobilegoodses=list;
    }


    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        final MOBILEGOODS mobilegoods=mobilegoodses.get(position);
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.goods_list_item, null);

            holder.GoodImgLeft= (SelectableRoundedImageView) view.findViewById(R.id.goods_img_left);
            holder.llGoodImgLeft= (LinearLayout) view.findViewById(R.id.ll_goods_img_left);
            holder.llItemLeft= (LinearLayout) view.findViewById(R.id.ll_goods_item_left);
            holder.GoodImgRight= (SelectableRoundedImageView) view.findViewById(R.id.goods_img_right);
            holder.llGoodImgRight=(LinearLayout) view.findViewById(R.id.ll_goods_img_right);
            holder.llItemRight= (LinearLayout) view.findViewById(R.id.ll_goods_item_right);
            holder.llBothItem= (LinearLayout) view.findViewById(R.id.ll_both_item);
            holder.goodsItemTop= (View) view.findViewById(R.id.goods_item_top);
            holder.good_cell_name_one= (AutoReturnView) view.findViewById(R.id.goodlist_goodname_left);
            holder.good_cell_name_two= (AutoReturnView) view.findViewById(R.id.goodlist_goodname_right);
            holder.market_price_one= (TextView) view.findViewById(R.id.goodlist_origin_price_left);
            holder.market_price_two= (TextView) view.findViewById(R.id.goodlist_origin_price_right);
            holder.good_cell_price_one= (TextView) view.findViewById(R.id.goodlist_shop_price_left);
            holder.good_cell_price_two= (TextView) view.findViewById(R.id.goodlist_shop_price_right);
            holder.tv_saving_one= (TextView) view.findViewById(R.id.tv_saving_left);
            holder.tv_saving_two= (TextView) view.findViewById(R.id.tv_saving_right);
            holder.ll_goodlist_mb_one= (LinearLayout) view.findViewById(R.id.ll_goodlist_mb_left);
            holder.ll_goodlist_mb_two= (LinearLayout) view.findViewById(R.id.ll_goodlist_mb_right);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        if(position==0){
            holder.goodsItemTop.setVisibility(View.VISIBLE);
        }else{
            holder.goodsItemTop.setVisibility(View.GONE);
        }

        ViewGroup.LayoutParams params = holder.llGoodImgLeft.getLayoutParams();
        params.width = (getDisplayMetricsWidth()-distance)/2;
        params.height = params.width;
        holder.llGoodImgLeft.setLayoutParams(params);

        holder.llGoodImgRight.setLayoutParams(params);

        MOBILEGOODS left=null;
        MOBILEGOODS right=null;
        if(position*2<mobilegoodses.size()){
            left= (MOBILEGOODS) mobilegoodses.get(position*2);
        }

        if (position*2 + 1 < mobilegoodses.size()) {
            right = (MOBILEGOODS) mobilegoodses.get(position*2 + 1);
        }

        if(left==null){
            holder.llBothItem.setVisibility(View.GONE);
        }else {
            holder.llBothItem.setVisibility(View.VISIBLE);

            ImageLoaderForLV.getInstance(context).setImageRes(holder.GoodImgLeft, left.getImg().getThumb());
            final MOBILEGOODS finalLeft = left;

            float promote = FormatUtil.formatStrToFloat(left.getPromote_price());
            if (promote!=0) {
                holder.good_cell_price_one.setText(left.getPromote_price());
                holder.market_price_one.setText(left.getShop_price());
            } else {
                float str = FormatUtil.formatStrToFloat(left.getShop_price());
                if (str==0) {
                    holder.good_cell_price_one.setText("免费");
                    holder.market_price_one.setText("");
                } else {
                    holder.good_cell_price_one.setText(left.getShop_price());
                    holder.market_price_one.setText(left.getMarket_price());
                }
            }

            if (AppConst.MOBILEBUY_GOODS.equals(left.getActivity_type())) {
                holder.ll_goodlist_mb_one.setVisibility(View.VISIBLE);
                holder.tv_saving_one.setText(left.getFormatted_saving_price());
            } else {
                holder.ll_goodlist_mb_one.setVisibility(View.GONE);
            }

            holder.good_cell_name_one.setContent(left.getName());

            holder.llItemLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, GoodsDetailActivity.class);
                    intent.putExtra("goods_id", finalLeft.getId());
                    intent.putExtra("rec_type",finalLeft.getActivity_type());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }
            });
            if (null == right) {
                holder.llItemRight.setVisibility(View.INVISIBLE);
            } else {
                holder.llItemRight.setVisibility(View.VISIBLE);

                ImageLoaderForLV.getInstance(context).setImageRes(holder.GoodImgRight, right.getImg().getThumb());
                final MOBILEGOODS finalRight = right;

                float promote2 = FormatUtil.formatStrToFloat(right.getPromote_price());
                if (promote2!=0) {
                    holder.good_cell_price_two.setText(right.getPromote_price());
                    holder.market_price_two.setText(right.getShop_price());
                } else {
                    float str = FormatUtil.formatStrToFloat(right.getShop_price());
                    if (str==0) {
                        holder.good_cell_price_two.setText("免费");
                        holder.market_price_two.setText("");
                    } else {
                        holder.good_cell_price_two.setText(right.getShop_price());
                        holder.market_price_two.setText(right.getMarket_price());
                    }
                }

                if (AppConst.MOBILEBUY_GOODS.equals(right.getActivity_type())) {
                    holder.ll_goodlist_mb_two.setVisibility(View.VISIBLE);
                    holder.tv_saving_two.setText(right.getFormatted_saving_price());
                } else {
                    holder.ll_goodlist_mb_two.setVisibility(View.GONE);
                }

                holder.good_cell_name_two.setContent(right.getName());

                holder.llItemRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(context, GoodsDetailActivity.class);
                        intent.putExtra(IntentKeywords.GOODS_ID, finalRight.getId());
//                        intent.putExtra(IntentKeywords.REC_TYPE,finalRight.getActivity_type());
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                                R.anim.push_right_out);
                    }
                });
            }

        }

        return view;
    }
    private class ViewHolder{
        private SelectableRoundedImageView GoodImgLeft;
        private LinearLayout llGoodImgLeft;
        private LinearLayout llItemLeft;
        private SelectableRoundedImageView GoodImgRight;
        private LinearLayout llGoodImgRight;
        private LinearLayout llItemRight;
        private LinearLayout llBothItem;
        private View goodsItemTop;
        private AutoReturnView good_cell_name_one;
        private AutoReturnView good_cell_name_two;
        private TextView market_price_one;
        private TextView market_price_two;
        private TextView good_cell_price_one;
        private TextView good_cell_price_two;
        private TextView tv_saving_one;
        private TextView tv_saving_two;
        private LinearLayout ll_goodlist_mb_one;
        private LinearLayout ll_goodlist_mb_two;
    }

    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        int j = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j);
    }

}
