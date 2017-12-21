package com.ecjia.view.activity;



import java.util.ArrayList;
import java.util.HashMap;

import android.content.res.Resources;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.AppConst;
import com.ecjia.view.adapter.ShippingAdapter;
import com.ecjia.entity.responsemodel.SHIPPING;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ecjia.widgets.ToastView;
import com.umeng.message.PushAgent;

public class ShippingActivity extends BaseActivity {

	private TextView title;
	private ImageView back;

	private ListView listView;

	private ArrayList<SHIPPING> list = new ArrayList<SHIPPING>();
	private ShippingAdapter shippingAdapter;
	private Handler handler;
	int position = 0;
    private LinearLayout payment_uplineitem;
    private TextView shipTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment);
        PushAgent.getInstance(this).onAppStart();
		Intent intent = getIntent();
		String s = intent.getStringExtra("payment");

		try {
			if (StringUtils.isNotEmpty(s)) {
				JSONObject jo = new JSONObject(s);
				JSONArray paymentArray = jo.optJSONArray("shipping_list");
				if (null != paymentArray && paymentArray.length() > 0) {
					list.clear();
					if (AppConst.iscod) {// 货到付款
						for (int i = 0; i < paymentArray.length(); i++) {
							JSONObject shippingJsonObject = paymentArray
									.getJSONObject(i);
							SHIPPING shipping_Item = SHIPPING
									.fromJson(shippingJsonObject);
							if (shipping_Item.getSupport_cod().equals("1")) {
								list.add(shipping_Item);
							}
						}
					} else {
						for (int i = 0; i < paymentArray.length(); i++) {
							JSONObject shippingJsonObject = paymentArray
									.getJSONObject(i);
							SHIPPING shipping_Item = SHIPPING
									.fromJson(shippingJsonObject);
							list.add(shipping_Item);
						}
					}
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		Resources resource = (Resources) getBaseContext().getResources();
		String way = resource.getString(R.string.balance_shipping);
		title = (TextView) findViewById(R.id.top_view_text);
		title.setText(way);

        shipTextView= (TextView) findViewById(R.id.shipping_text);
        shipTextView.setVisibility(View.GONE);
        payment_uplineitem= (LinearLayout) findViewById(R.id.payment_uplineitem);
        payment_uplineitem.setVisibility(View.GONE);
		if (AppConst.shippinglist == null) {
            AppConst.shippinglist = new ArrayList<HashMap<String, String>>();
			for (int i = 0; i < list.size(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("select", "false");
                AppConst.shippinglist.add(map);
			}
		}


		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					position = msg.arg1;
					Intent intent = new Intent();
					SHIPPING shipping = list.get(position);
					try {
						intent.putExtra("shipping", shipping.toJson()
								.toString());
					} catch (JSONException e) {

					}

					setResult(Activity.RESULT_OK, intent);
					finish();
				}
				shippingAdapter.notifyDataSetChanged();
			}
		};

		back = (ImageView) findViewById(R.id.top_view_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		listView = (ListView) findViewById(R.id.payment_list);

		if (list.size() > 0) {
			listView.setVisibility(View.VISIBLE);
			shippingAdapter = new ShippingAdapter(this, list);
			listView.setAdapter(shippingAdapter);
			shippingAdapter.handler = this.handler;
		} else {
			listView.setVisibility(View.GONE);
			String noway = resource.getString(R.string.no_mode_of_distribution);
			ToastView toast = new ToastView(this, noway);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}


	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
