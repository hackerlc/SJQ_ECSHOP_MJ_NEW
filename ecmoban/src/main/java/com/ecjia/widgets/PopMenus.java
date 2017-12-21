package com.ecjia.widgets;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.SUB_BUTTON;
import com.ecjia.util.LG;
import com.ecjia.util.ecjiaopentype.ECJiaOpenType;

import java.util.ArrayList;

@SuppressLint({ "ResourceAsColor", "ShowToast" })
public class PopMenus {
	private ArrayList<SUB_BUTTON> sub_buttons=new ArrayList<>();
	private Context context;
	private PopupWindow popupWindow;
	private LinearLayout listView;
	private int width, height;
	private View containerView;

	public PopMenus(Context context, ArrayList<SUB_BUTTON> data, int _width, int _height) {
		this.context = context;
		this.sub_buttons = data;
		this.width = _width;
		this.height = _height;
		containerView = LayoutInflater.from(context).inflate(R.layout.popmenus, null);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
		containerView.setLayoutParams(lp);
		listView = (LinearLayout) containerView.findViewById(R.id.layout_subcustommenu);
		try {
			setSubMenu();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listView.setBackgroundColor(context.getResources().getColor(R.color.white));
		listView.setFocusableInTouchMode(true);
		listView.setFocusable(true);

		popupWindow = new PopupWindow(containerView, width == 0 ? LayoutParams.WRAP_CONTENT : width, height == 0 ? LayoutParams.WRAP_CONTENT : height);
	}

	public void showAsDropDown(View parent) {
		popupWindow.setBackgroundDrawable(new ColorDrawable());
		popupWindow.showAsDropDown(parent);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.update();

		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
			}
		});
	}

	public void showAtLocation(View parent) {
		popupWindow.setBackgroundDrawable(new ColorDrawable());
		containerView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		int[] location = new int[2];
		parent.getLocationOnScreen(location);
		int x = location[0] + 10;
		int y = parent.getHeight() + (parent.getHeight() / 8);
		// Utils.toast(context, y +""); //location[1] - popupHeight -
		// parent.getHeight()
		popupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.BOTTOM, x, y);

		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.update();

		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
			}
		});
	}

	public void dismiss() {
		popupWindow.dismiss();
	}

	void setSubMenu() throws JSONException {
		listView.removeAllViews();
		int size=sub_buttons.size();
		size=size>5?5:size;
		for (int i = 0; i < size; i++) {
			final SUB_BUTTON ob = sub_buttons.get(i);
			LinearLayout layoutItem = (LinearLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.pomenu_menuitem, null);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
			containerView.setLayoutParams(lp);
			layoutItem.setFocusable(true);
			TextView tv_funbtntitle = (TextView) layoutItem.findViewById(R.id.pop_item_textView);
			View pop_item_line = layoutItem.findViewById(R.id.pop_item_line);
			if ((i + 1) == sub_buttons.size()) {
				pop_item_line.setVisibility(View.GONE);
			}
			tv_funbtntitle.setText(ob.getName());
			layoutItem.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ECJiaOpenType.getDefault().startAction(context,ob.getUrl());
					LG.e("===SUB_BUTTON==="+ob.getUrl());
				}
			});
			listView.addView(layoutItem);
		}
		listView.setVisibility(View.VISIBLE);
	}

}
