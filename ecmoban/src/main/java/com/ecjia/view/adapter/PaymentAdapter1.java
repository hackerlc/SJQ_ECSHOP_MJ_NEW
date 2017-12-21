package com.ecjia.view.adapter;

import java.util.List;

import com.ecjia.base.ECJiaApplication;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.AppConst;
import com.ecjia.entity.responsemodel.PAYMENT;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PaymentAdapter1 extends BaseAdapter {

    private Context context;
    private List<PAYMENT> list;
    private LayoutInflater inflater;
    public static Boolean isFirst;
    public Handler handler;

    private ECJiaApplication mApp;

    public PaymentAdapter1(Context context, List<PAYMENT> list) {
        this.context = context;
        mApp = (ECJiaApplication) this.context.getApplicationContext();
        this.list = list;
        inflater = LayoutInflater.from(context);
        if (mApp.onlinepaymentlist.size() == 0) {
            isFirst = true;
        } else {
            isFirst = false;
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.payment_item, null);
            holder.name = (TextView) convertView
                    .findViewById(R.id.payment_item_name);
            holder.paymentitem = (LinearLayout) convertView
                    .findViewById(R.id.payment_item_layout);
            holder.paymentImage = (ImageView) convertView
                    .findViewById(R.id.payment_status);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.paymentitem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (position == 0) {
                    isFirst = true;
                } else {
                    isFirst = false;
                }
                PaymentAdapter.isFirst = false;
                if (AppConst.paymentlist.size() == 1) {
                    AppConst.paymentlist.get(position).put("select", "true");
                    Message msg = new Message();
                    msg.what = 1;
                    msg.arg1 = position;
                    handler.handleMessage(msg);
                } else {
                    AppConst.paymentlist.get(position + mApp.onlinepaymentlist.size()).put("select",
                            "true");
                    for (int i = 0; i < AppConst.paymentlist.size(); i++) {
                        if (i != position + mApp.onlinepaymentlist.size()) {
                            AppConst.paymentlist.get(i).put("select",
                                    "false");
                        }
                    }
                    Message msg = new Message();
                    msg.what = 1;
                    msg.arg1 = position;
                    handler.handleMessage(msg);

                }
            }
        });
        holder.name.setText(list.get(position).getPay_name());
        if ("true".equals(AppConst.paymentlist.get(position + mApp.onlinepaymentlist.size()).get("select"))) {
            holder.paymentImage
                    .setBackgroundResource(R.drawable.payment_selected);
        } else {
            if (position == 0 && isFirst) {
                holder.paymentImage
                        .setBackgroundResource(R.drawable.payment_selected);
            } else {
                holder.paymentImage
                        .setBackgroundResource(R.drawable.payment_unselected);
            }
        }
        return convertView;

    }

    class ViewHolder {
        private TextView name;
        private LinearLayout paymentitem;
        private ImageView paymentImage;
    }

}
