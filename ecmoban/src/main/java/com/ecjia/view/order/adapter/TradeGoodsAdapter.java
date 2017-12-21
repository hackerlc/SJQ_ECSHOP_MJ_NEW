package com.ecjia.view.order.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.view.adapter.ECJiaBaseAdapter;
import com.ecjia.entity.responsemodel.ORDER_GOODS_LIST;
import com.ecjia.util.FormatUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/20.
 */
public class TradeGoodsAdapter extends ECJiaBaseAdapter<ORDER_GOODS_LIST> {

    public TradeGoodsAdapter(Context context, ArrayList<ORDER_GOODS_LIST> list) {
        super(context, list);

    }

    @Override
    protected ECJiaCellHolder createCellHolder(View cellView) {
        return null;
    }

    @Override
    protected View bindData(int position, View cellView, ViewGroup parent, ECJiaCellHolder h) {
        return null;
    }

    @Override
    public View createCellView() {
        return null;
    }

    @Override
    public View getView(final int position, View cellView, ViewGroup parent) {

        ViewHolder holder = null;

        if (cellView == null) {
            holder = new ViewHolder();
            cellView = LayoutInflater.from(mContext).inflate(R.layout.trade_goods_item, null);
            holder.ivTradeGoods = (ImageView) cellView.findViewById(R.id.iv_trade_goods);
            holder.tvTradeGoodsNum = (TextView) cellView.findViewById(R.id.tv_trade_goods_num);
            holder.firstSide = cellView.findViewById(R.id.first_side);
            holder.lastSide = cellView.findViewById(R.id.last_side);
            holder.flTradeGoodsNum = (FrameLayout) cellView.findViewById(R.id.fl_trade_goods_num);
            holder.tradeGoodsItem = (FrameLayout) cellView.findViewById(R.id.trade_goods_item);
            cellView.setTag(holder);
        } else {
            holder = (ViewHolder) cellView.getTag();
        }
        final ORDER_GOODS_LIST simplegoods = dataList.get(position);

        if (position == 0) {
            holder.firstSide.setVisibility(View.VISIBLE);
        } else {
            holder.firstSide.setVisibility(View.GONE);
        }

        if (position == dataList.size() - 1) {
            holder.lastSide.setVisibility(View.VISIBLE);
        } else {
            holder.lastSide.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(simplegoods.getGoods_id())) {
            holder.ivTradeGoods.setVisibility(View.INVISIBLE);
        } else {
            holder.ivTradeGoods.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(simplegoods.getImg().getThumb(), holder.ivTradeGoods);
            if (FormatUtil.formatStrToInt(simplegoods.getGoods_number()) > 1) {
                holder.flTradeGoodsNum.setVisibility(View.VISIBLE);
                String num = simplegoods.getGoods_number();
                if (FormatUtil.formatStrToInt(num) > 99) {
                    num = "99+";
                }
                holder.tvTradeGoodsNum.setText(num);
            } else {
                holder.flTradeGoodsNum.setVisibility(View.GONE);
            }
        }


        return cellView;
    }


    /**
     * 单击事件监听器
     */
    private onAdpItemClickListener mListener = null;

    public void setOnAdpItemClickListener(onAdpItemClickListener listener) {
        mListener = listener;
    }

    public interface onAdpItemClickListener {
        void onAdpItemClick(View v, int position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    public class ViewHolder {
        ImageView ivTradeGoods;
        TextView tvTradeGoodsNum;
        View firstSide;
        View lastSide;
        FrameLayout flTradeGoodsNum;
        FrameLayout tradeGoodsItem;

    }
}
