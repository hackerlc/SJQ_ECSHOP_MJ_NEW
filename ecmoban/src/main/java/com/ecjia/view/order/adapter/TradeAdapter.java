package com.ecjia.view.order.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.HorizontalListView;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.activity.goodsorder.OrderdetailActivity;
import com.ecjia.consts.OrderType;
import com.ecjia.entity.responsemodel.GOODORDER;
import com.ecjia.util.LG;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


public class TradeAdapter extends BaseAdapter {

    private Context context;
    public ArrayList<GOODORDER> list;
    public String flag;
    private LayoutInflater inflater;
    private Resources res;
    private TradeGoodsAdapter tradeGoodsAdapter;

    public TradeAdapter(Context context, ArrayList<GOODORDER> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        res = context.getResources();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GOODORDER getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final GOODORDER goodorder = list.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_trade, null);
            holder = new ViewHolder();
            holder.tvTradeOrderSn = (TextView) convertView.findViewById(R.id.tv_trade_ordersn);
            holder.tvTradeType = (TextView) convertView.findViewById(R.id.tv_trade_type);
            holder.ivSingleTradeGoods = (ImageView) convertView.findViewById(R.id.iv_single_trade_goods);
            holder.tvSingleTradeGoods = (TextView) convertView.findViewById(R.id.tv_single_trade_goods);
            holder.llSingleTradeGoods = (LinearLayout) convertView.findViewById(R.id.ll_single_trade_goods);
            holder.llMultipleTradeGoods = (LinearLayout) convertView.findViewById(R.id.ll_multiple_trade_goods);
            holder.rlvTradeGoods = (HorizontalListView) convertView.findViewById(R.id.rlv_trade_goods);
            holder.tvTradeGoodsNum = (TextView) convertView.findViewById(R.id.tv_trade_goods_num);
            holder.tvTradeCost = (TextView) convertView.findViewById(R.id.tv_trade_cost);
            holder.tvTradeReceive = (TextView) convertView.findViewById(R.id.tv_trade_receive);
            holder.tvTradeComment = (TextView) convertView.findViewById(R.id.tv_trade_comment);
            holder.tvTradeAction = (TextView) convertView.findViewById(R.id.tv_trade_action);
            holder.llTradeItem = (LinearLayout) convertView.findViewById(R.id.ll_trade_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTradeOrderSn.setText(goodorder.getOrder_sn());
        holder.tvTradeCost.setText(goodorder.getFormated_total_fee());
        holder.tvTradeType.setText(goodorder.getLabel_order_status());
        holder.tvTradeGoodsNum.setText(goodorder.getGoods_number() + "");

        //最后面的去支付(确认收货，再次购买)
        if (goodorder.getOrder_status_code().equals(OrderType.AWAIT_PAY)) {
            holder.tvTradeAction.setText(R.string.order_paynow);
            holder.tvTradeAction.setVisibility(View.VISIBLE);
        } else if (goodorder.getOrder_status_code().equals(OrderType.AWAIT_SHIP)) {
            holder.tvTradeAction.setText("");
            holder.tvTradeAction.setVisibility(View.GONE);
        } else if (goodorder.getOrder_status_code().equals(OrderType.SHIPPED)) {
            holder.tvTradeAction.setText(R.string.tradeitem_receive);
            holder.tvTradeAction.setVisibility(View.VISIBLE);
        } else {
            holder.tvTradeAction.setText(R.string.order_buy_again);
            holder.tvTradeAction.setVisibility(View.VISIBLE);
        }

        //中间的空白（去评价，查看评价）
        if (goodorder.getOrder_status_code().equals(OrderType.FINISHED)) {
            holder.tvTradeComment.setVisibility(View.VISIBLE);
            int is_commentedNum = 0;
            for (int i = 0; i < goodorder.getGoods_list().size(); i++) {
                if (goodorder.getGoods_list().get(i).getIs_commented() == 1) {
                    is_commentedNum += 1;
                }
            }
            if (is_commentedNum < goodorder.getGoods_list().size()) {//是否评价
                holder.tvTradeComment.setText(R.string.order_to_comment);
                holder.tvTradeComment.setVisibility(View.VISIBLE);
                list.get(position).setToComment(true);
            } else {
                holder.tvTradeComment.setVisibility(View.GONE);
                holder.tvTradeComment.setText(R.string.order_lookfor_commment);
                list.get(position).setToComment(false);
            }
        } else {
            holder.tvTradeComment.setVisibility(View.GONE);
        }
        //第一个取消（提醒发货，查看物流）
        if (goodorder.getOrder_status_code().equals(OrderType.AWAIT_PAY)) {
            holder.tvTradeReceive.setVisibility(View.VISIBLE);
            holder.tvTradeReceive.setText(R.string.balance_cancel);
        } else if (goodorder.getOrder_status_code().equals(OrderType.AWAIT_SHIP)) {
            holder.tvTradeReceive.setVisibility(View.VISIBLE);
            holder.tvTradeReceive.setText(R.string.order_remind_ship);
        } else if (goodorder.getOrder_status_code().equals(OrderType.SHIPPED)) {
            holder.tvTradeReceive.setVisibility(View.VISIBLE);
            holder.tvTradeReceive.setText(R.string.check_shipinfo);
        } else if (goodorder.getOrder_status_code().equals(OrderType.FINISHED)) {
            holder.tvTradeReceive.setVisibility(View.INVISIBLE);
            holder.tvTradeReceive.setText(R.string.check_shipinfo);
        } else {
            holder.tvTradeReceive.setVisibility(View.INVISIBLE);
        }

        if (goodorder.getGoods_list().size() == 1) {
            holder.llSingleTradeGoods.setVisibility(View.VISIBLE);
            holder.llMultipleTradeGoods.setVisibility(View.GONE);
            holder.tvSingleTradeGoods.setText(goodorder.getGoods_list().get(0).getName());
            ImageLoader.getInstance().displayImage(goodorder.getGoods_list().get(0).getImg().getThumb(), holder.ivSingleTradeGoods);
        } else if (goodorder.getGoods_list().size() > 1) {
            holder.llSingleTradeGoods.setVisibility(View.GONE);
            holder.llMultipleTradeGoods.setVisibility(View.VISIBLE);

            tradeGoodsAdapter = new TradeGoodsAdapter(context, goodorder.getGoods_list());
            holder.rlvTradeGoods.setAdapter(tradeGoodsAdapter);
            holder.rlvTradeGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                    startDetail(position);
                }
            });
        } else {
            holder.llSingleTradeGoods.setVisibility(View.GONE);
            holder.llMultipleTradeGoods.setVisibility(View.GONE);
        }

        holder.llMultipleTradeGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetail(position);
            }
        });

        holder.llSingleTradeGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetail(position);
            }
        });


        holder.tvTradeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onAdpItemClick(v, position);
                }
            }
        });

        holder.tvTradeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onAdpItemClick(v, position);
                }
            }
        });
        holder.tvTradeReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onAdpItemClick(v, position);
                }
            }
        });

        return convertView;
    }

    private void startDetail(final int position) {
        setFlag(list.get(position).getOrder_status_code());
        Intent intent = new Intent(context, OrderdetailActivity.class);
        intent.putExtra(IntentKeywords.ORDER_ID, list.get(position).getOrder_id());
        intent.putExtra(IntentKeywords.PAY_CODE, list.get(position).getOrder_info().getPay_code());
        intent.putExtra("formated_total_fee", list.get(position).getFormated_total_fee());
        intent.putExtra(IntentKeywords.ORDER_TYPE, flag);
        LG.i("===flag===" + flag);
        LG.i("===position===" + position);
        ((Activity) context).startActivityForResult(intent, 1);
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

    private void setFlag(String order_status_code) {
        flag = order_status_code;
    }


    static class ViewHolder {
        TextView tvTradeOrderSn;
        TextView tvTradeType;
        ImageView ivSingleTradeGoods;
        TextView tvSingleTradeGoods;
        LinearLayout llSingleTradeGoods;
        LinearLayout llMultipleTradeGoods;
        HorizontalListView rlvTradeGoods;
        TextView tvTradeGoodsNum;
        TextView tvTradeCost;
        TextView tvTradeReceive;
        TextView tvTradeComment;
        TextView tvTradeAction;
        LinearLayout llTradeItem;
    }
}
