package com.ecjia.view.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.consts.AppConst;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.entity.intentmodel.OrderGoodInrtent;
import com.ecjia.util.FormatUtil;
import com.ecjia.view.activity.goodsorder.OrderdetailActivity;
import com.ecjia.consts.OrderType;
import com.ecjia.entity.responsemodel.GOODORDER;
import com.ecjia.entity.responsemodel.ORDER_GOODS_LIST;
import com.ecjia.util.ImageLoaderForLV;
import com.ecmoban.android.sijiqing.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class  TradeAdapter extends BaseAdapter {

    private Context context;
    public List<GOODORDER> list;
    public String flag;
    private LayoutInflater inflater;
    private Resources resource;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    SimpleDateFormat format;

    public TradeAdapter(Context context, List<GOODORDER> list, String flag) {
        this.context = context;
        this.list = list;
        this.flag = flag;
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

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.trade_item, null);
            holder.trade_press = (LinearLayout) convertView.findViewById(R.id.trade_item_press);

            holder.sno = (TextView) convertView.findViewById(R.id.trade_item_sno);

            holder.body = (LinearLayout) convertView.findViewById(R.id.trade_item_body);
            holder.fee = (TextView) convertView.findViewById(R.id.trade_item_fee);
            holder.red_paper = (TextView) convertView.findViewById(R.id.trade_item_redPaper);
            holder.integral = (TextView) convertView.findViewById(R.id.trade_item_integral);
            holder.formateddiscount = (TextView) convertView.findViewById(R.id.trade_item_formated_discount);

            holder.vitem = convertView.findViewById(R.id.trade_first_item);

            holder.moreinfo = (LinearLayout) convertView.findViewById(R.id.trade_more_info);
            holder.haveshipfee = (LinearLayout) convertView.findViewById(R.id.trade_ship_fee);
            holder.haveredpager = (LinearLayout) convertView.findViewById(R.id.trade_redpaper);
            holder.havejifen = (LinearLayout) convertView.findViewById(R.id.trade_jifen);
            holder.havezhekou = (LinearLayout) convertView.findViewById(R.id.trade_zhekou);
            holder.enditem = convertView.findViewById(R.id.trade_end_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.vitem.setVisibility(View.VISIBLE);
        } else {
            holder.vitem.setVisibility(View.GONE);
        }
        if (position == list.size() - 1) {
            holder.enditem.setVisibility(View.GONE);
        } else {
            holder.enditem.setVisibility(View.VISIBLE);
        }
        final GOODORDER order = list.get(position);
        Date currentTime = null;
        try {
            currentTime = format.parse(list.get(position).getOrder_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final ArrayList<ORDER_GOODS_LIST> goods_list = list.get(position).getGoods_list();
        int size = goods_list.size() > 3 ? 3 : goods_list.size();// 最多显示三个商品
        for (int i = 0; i < size; i++) {
            holder.body.removeAllViews();
            View view = LayoutInflater.from(context).inflate(R.layout.trade_body, null);
            View top = view.findViewById(R.id.trade_item_top);
            View buttom = view.findViewById(R.id.trade_item_buttom);
            ImageView image = (ImageView) view.findViewById(R.id.trade_body_image);
            TextView text = (TextView) view.findViewById(R.id.trade_body_text);
            TextView total = (TextView) view.findViewById(R.id.trade_body_total);
            TextView num = (TextView) view.findViewById(R.id.trade_body_num);
            TextView ordertype = (TextView) view.findViewById(R.id.order_type);
            TextView ordertime = (TextView) view.findViewById(R.id.order_time);
            TextView tv_tuanpi_dinjin = (TextView) view.findViewById(R.id.tv_tuanpi_dinjin);
            buttom.setVisibility(View.GONE);

            holder.body.addView(view);

            ImageLoaderForLV.getInstance(context).setImageRes(image, goods_list.get(i).getImg().getThumb());

            text.setText(order.getGoods_number() + "");
            resource = AppConst.getResources(context);
            String order_time = resource.getString(R.string.trade_order_time);
            ordertime.setText(order_time + format.format(currentTime));

            total.setText(resource.getString(R.string.yuan_unit) + (Float.valueOf(order.getTotal_fee()) + resource.getString(R.string.yuan)));
            num.setText("X " + goods_list.get(i).getGoods_number());

            if (OrderType.AWAIT_PAY.equals(list.get(position).getOrder_status_code())) {// 判断进入那个界面
                ordertype.setText(resource.getString(R.string.order_await_pay));
            } else if (OrderType.AWAIT_SHIP.equals(list.get(position).getOrder_status_code())) {
                ordertype.setText(resource.getString(R.string.order_await_ship));
            } else if (OrderType.SHIPPED.equals(list.get(position).getOrder_status_code())) {
                ordertype.setText(resource.getString(R.string.order_shipped));
            } else if (OrderType.ALLOW_COMMENT.equals(list.get(position).getOrder_status_code())) {
                ordertype.setText(resource.getString(R.string.order_await_comment));
            } else if (OrderType.FINISHED.equals(list.get(position).getOrder_status_code())) {
                ordertype.setText(resource.getString(R.string.order_history));
            } else if (OrderType.AWAIT_TUANPI.equals(list.get(position).getOrder_status_code())) {
                ordertype.setText("待成团");
                tv_tuanpi_dinjin.setVisibility(View.VISIBLE);
            } else if (OrderType.RETURN_GOOD.equals(list.get(position).getOrder_status_code())) {
                ordertype.setText("退换货");
            } else {
                ordertype.setText("");
            }
        }

        holder.trade_press.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderdetailActivity.class);
                intent.putExtra(IntentKeywords.ORDER_ID, list.get(position).getOrder_id());
                intent.putExtra(IntentKeywords.ORDER_TYPE, list.get(position).getOrder_status_code());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        holder.sno.setText(order.getOrder_sn());
        resource = AppConst.getResources(context);
        String yuan = resource.getString(R.string.yuan);
        String yuan_unit = resource.getString(R.string.yuan_unit);
        if (order.getFormated_shipping_fee() == null
                || order.getFormated_shipping_fee().equals(yuan_unit + "0.00" + yuan)) {
            holder.haveshipfee.setVisibility(View.GONE);
        } else {
            holder.fee.setText(order.getFormated_shipping_fee());
        }

        if (holder.haveshipfee.getVisibility() == View.GONE
                && holder.haveredpager.getVisibility() == View.GONE
                && holder.havejifen.getVisibility() == View.GONE
                && holder.havezhekou.getVisibility() == View.GONE) {
            holder.moreinfo.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        private TextView sno;
        private LinearLayout body;
        private TextView fee;
        private TextView red_paper;
        private TextView integral;
        private TextView formateddiscount;

        private View vitem, enditem;

        private LinearLayout moreinfo;
        private LinearLayout haveshipfee;
        private LinearLayout haveredpager;
        private LinearLayout havejifen;
        private LinearLayout havezhekou;
        private LinearLayout trade_press;
    }
}
