package com.ecjia.view.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.consts.OrderType;
import com.ecjia.entity.intentmodel.OrderGoodInrtent;
import com.ecjia.util.common.JsonUtil;
import com.ecjia.view.activity.goodsorder.ApplyBaseCustomerServiceActivity;
import com.ecjia.view.activity.goodsorder.ApplyOderReturnGoodsDetailActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.entity.responsemodel.ORDER_GOODS_LIST;
import com.ecjia.util.FormatUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class OrderdetailActivityAdapter extends BaseAdapter {
    private Context context;
    public ArrayList<ORDER_GOODS_LIST> list;
    public String flag;
    public String shipping_fee;
    private LayoutInflater inflater;

    public Handler parentHandler;

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    public OrderdetailActivityAdapter(Context context, ArrayList<ORDER_GOODS_LIST> list, String flag, String shipping_fee) {
        this.list = list;
        this.context = context;
        this.flag = flag;
        this.shipping_fee = shipping_fee;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.orderdetail_item, null);
            holder = new ViewHolder();
            holder.item = (LinearLayout) convertView.findViewById(R.id.order_item);
            holder.top = convertView.findViewById(R.id.trade_item_top);
            holder.top.setVisibility(View.GONE);
            holder.buttom = convertView.findViewById(R.id.trade_item_buttom);
            holder.image = (ImageView) convertView.findViewById(R.id.trade_body_image);
            holder.text = (TextView) convertView.findViewById(R.id.trade_body_text);
            holder.total = (TextView) convertView.findViewById(R.id.trade_body_total);
            holder.num = (TextView) convertView.findViewById(R.id.trade_body_num);
            holder.tv_apply_service = (TextView) convertView.findViewById(R.id.tv_apply_service);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ORDER_GOODS_LIST order_good = list.get(position);
        imageLoader.displayImage(order_good.getImg().getThumb(), holder.image);
        holder.text.setText(order_good.getName());
        holder.total.setText(order_good.getFormated_shop_price());
        if (0 == FormatUtil.formatStrToFloat(order_good.getFormated_shop_price())) {
            holder.total.setText("免费");
        } else {
            holder.total.setText(order_good.getFormated_shop_price());
        }
        holder.num.setText("X " + order_good.getGoods_number());
        if (position == list.size() - 1) {
            holder.buttom.setVisibility(View.GONE);// 隐藏底部横线
        }
        holder.item.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra(IntentKeywords.GOODS_ID, order_good.getGoods_id());
                intent.putExtra(IntentKeywords.REC_TYPE, order_good.getActivity_type());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(
                        R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        holder.tv_apply_service.setVisibility(View.VISIBLE);
        if (flag.equals(OrderType.AWAIT_SHIP)) {//代发货
            if (!TextUtils.isEmpty(order_good.getRet_id()) && !"0".equals(order_good.getRet_id())) {
                holder.tv_apply_service.setText("查看售后");
            } else {
                holder.tv_apply_service.setText("退款");
            }
        } else if (flag.equals(OrderType.SHIPPED) || flag.equals(OrderType.FINISHED) || flag.equals(OrderType.ALLOW_COMMENT)) {//待收货  //已完成  //待评价
            if (!TextUtils.isEmpty(order_good.getRet_id()) && !"0".equals(order_good.getRet_id())) {
                holder.tv_apply_service.setText("查看售后");
            } else {
                holder.tv_apply_service.setText("申请售后");
            }
        } else if (flag.equals(OrderType.RETURN_GOOD)) {//退换货
            holder.tv_apply_service.setText("查看售后");
        } else {
            holder.tv_apply_service.setVisibility(View.GONE);
        }
        OrderGoodInrtent orderGoodInrtent = new OrderGoodInrtent();
        orderGoodInrtent.setRec_id(order_good.getRec_id());//id
        orderGoodInrtent.setReturn_number(order_good.getGoods_number());//数量
//        orderGoodInrtent.setShould_return(FormatUtil.formatToDigetPrice(order_good.getFormated_shop_price()));//价格
        orderGoodInrtent.setShould_return(FormatUtil.formatToDigetPrice(order_good.getCeiling()));//价格
//        orderGoodInrtent.setReturn_shipping_fee(FormatUtil.formatToDigetPrice(shipping_fee));//运费
        orderGoodInrtent.setReturn_shipping_fee(FormatUtil.formatToDigetPrice(order_good.getShipping_fee()));//运费
        holder.tv_apply_service.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(order_good.getRet_id()) && !"0".equals(order_good.getRet_id())) {
                    Intent intent = new Intent(context, ApplyOderReturnGoodsDetailActivity.class);
                    intent.putExtra(IntentKeywords.RET_ID, order_good.getRet_id());
                    intent.putExtra(IntentKeywords.REC_ID, order_good.getRec_id());//订单retid
                    intent.putExtra(IntentKeywords.ORDER_TYPE, flag);//订单当前的状态OrderGoodInrtent
                    intent.putExtra(IntentKeywords.ORDER_GOODINRTENT, JsonUtil.toString(orderGoodInrtent));
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, ApplyBaseCustomerServiceActivity.class);
                    intent.putExtra(IntentKeywords.REC_ID, order_good.getRec_id());//订单retid
                    intent.putExtra(IntentKeywords.ORDER_TYPE, flag);//订单当前的状态OrderGoodInrtent
                    intent.putExtra(IntentKeywords.ORDER_GOODINRTENT, JsonUtil.toString(orderGoodInrtent));
                    context.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        private View top;
        private View buttom;
        private ImageView image;
        private TextView text;
        private TextView total;
        private TextView num;
        private LinearLayout item;
        private TextView tv_apply_service;
    }
}
