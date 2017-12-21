package com.ecjia.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.consts.IntentKeywords;
import com.ecjia.entity.intentmodel.OrderGoodInrtent;
import com.ecjia.entity.responsemodel.GOODRETURNORDER;
import com.ecjia.util.FormatUtil;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.util.common.JsonUtil;
import com.ecjia.view.activity.goodsorder.ApplyOderReturnGoodsDetailActivity;
import com.ecmoban.android.sijiqing.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-09.
 */

public class TradeReturnAdapter extends BaseAdapter {

    private Context context;
    public List<GOODRETURNORDER> list;
    private LayoutInflater inflater;
    private Resources resource;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    SimpleDateFormat format;

    public TradeReturnAdapter(Context context, List<GOODRETURNORDER> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        resource = (Resources) context.getResources();
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

        TradeReturnAdapter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new TradeReturnAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_trade_return, null);
            holder.trade_press = (LinearLayout) convertView.findViewById(R.id.trade_item_press);
            holder.sno = (TextView) convertView.findViewById(R.id.trade_item_sno);
            holder.trade_body_image = (ImageView) convertView.findViewById(R.id.trade_body_image);
            holder.order_type = (TextView) convertView.findViewById(R.id.order_type);
            holder.trade_body_text = (TextView) convertView.findViewById(R.id.trade_body_text);
            holder.trade_body_total = (TextView) convertView.findViewById(R.id.trade_body_total);
            holder.order_time = (TextView) convertView.findViewById(R.id.order_time);
            holder.tv_apply_service = (TextView) convertView.findViewById(R.id.tv_apply_service);

            convertView.setTag(holder);
        } else {
            holder = (TradeReturnAdapter.ViewHolder) convertView.getTag();
        }
        GOODRETURNORDER order = list.get(position);
        holder.sno.setText(order.getReturn_sn()+"");
        ImageLoaderForLV.getInstance(context).setImageRes(holder.trade_body_image, order.getGoods_thumb());
        holder.order_type.setText(order.getGoods_name() + "");
        holder.trade_body_text.setText(order.getReturn_number() + "");
        holder.trade_body_total.setText(resource.getString(R.string.yuan_unit) + (Float.valueOf(order.getShould_return()) + resource.getString(R.string.yuan)));
        holder.order_time.setText(order.getApply_time() + "");

        OrderGoodInrtent orderGoodInrtent=new OrderGoodInrtent();
        orderGoodInrtent.setRec_id(order.getRec_id());//id
        orderGoodInrtent.setReturn_number(order.getGoods_number());//数量
        orderGoodInrtent.setShould_return(FormatUtil.formatToDigetPrice(order.getCeiling()));//价格
        orderGoodInrtent.setReturn_shipping_fee(FormatUtil.formatToDigetPrice(order.getShipping_fee()));//运费

        holder.tv_apply_service.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ApplyOderReturnGoodsDetailActivity.class);
                intent.putExtra(IntentKeywords.REC_ID, order.getRec_id());//订单retid
                intent.putExtra(IntentKeywords.ORDER_TYPE, order.getStatus_code());//订单当前的状态OrderGoodInrtent
                intent.putExtra(IntentKeywords.RET_ID,order.getRet_id());
                intent.putExtra(IntentKeywords.ORDER_GOODINRTENT, JsonUtil.toString(orderGoodInrtent));
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        private TextView sno;
        private LinearLayout trade_press;
        private ImageView trade_body_image;
        private TextView order_type;
        private TextView trade_body_text;
        private TextView trade_body_total;
        private TextView order_time;
        private TextView tv_apply_service;

    }
}