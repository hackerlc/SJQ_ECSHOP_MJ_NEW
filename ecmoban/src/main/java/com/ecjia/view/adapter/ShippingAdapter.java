package com.ecjia.view.adapter;


import java.util.List;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.AppConst;
import com.ecjia.util.LG;
import com.ecjia.entity.responsemodel.SHIPPING;

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

public class ShippingAdapter extends BaseAdapter {

	private Context context;
	private List<SHIPPING> list;
	private LayoutInflater inflater;
	public Handler handler;
    public static int positionIndex=0;

	public ShippingAdapter(Context context, List<SHIPPING> list) {
		this.context = context;
		this.list = list;
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
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.payment_item, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.payment_item_name);
			holder.fee = (TextView) convertView
					.findViewById(R.id.payment_item_fee);
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
				if (AppConst.shippinglist.size() == 1) {
                    AppConst.shippinglist.get(position).put("select", "true");
					Message msg = new Message();
					msg.what = 1;
					msg.arg1 = position;
					handler.handleMessage(msg);
				} else {
					if ("false".equals(AppConst.shippinglist.get(position)
							.get("select"))) {
                        AppConst.shippinglist.get(position).put("select",
								"true");
						for (int i = 0; i < AppConst.shippinglist.size(); i++) {
							if (i != position) {
                                AppConst.shippinglist.get(i).put("select",
										"false");
							}
						}
						Message msg = new Message();
						msg.what = 1;
						msg.arg1 = position;
						handler.handleMessage(msg);
                        LG.i("======多项跳转======");
					}
                     positionIndex=position;
				}
			}
		});
		holder.name.setText(list.get(position).getShipping_name());
		holder.fee.setText(list.get(position).getFormat_shipping_fee());
		if ("true".equals(AppConst.shippinglist.get(position).get("select"))) {
			holder.paymentImage
					.setBackgroundResource(R.drawable.payment_selected);
		} else {
			holder.paymentImage
					.setBackgroundResource(R.drawable.payment_unselected);
		}
		return convertView;
	}

	class ViewHolder {
		private TextView name;
		private TextView fee;
		private LinearLayout paymentitem;
		private ImageView paymentImage;
	}

}
