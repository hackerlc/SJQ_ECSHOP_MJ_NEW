package com.ecjia.view.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.clipviewpager.RecyclingPagerAdapter;
import com.ecjia.entity.responsemodel.INVITE_REWARD;

import java.util.ArrayList;

public class MyInvitationAdapter extends RecyclingPagerAdapter {

	public ArrayList<INVITE_REWARD> list;
	private Context c;

	public MyInvitationAdapter(Context c, ArrayList<INVITE_REWARD> list) {
		this.list = list;
		this.c = c;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(c).inflate(
					R.layout.my_invitation_item, null);
			holder.tv_reward_time = (TextView) convertView
					.findViewById(R.id.tv_reward_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String date=list.get(position).getLabel_invite_data();

		if(date.contains("年")){
			date=date.replace("年", "年\n");
		}

		holder.tv_reward_time.setText(date);

		return convertView;
	}


	class ViewHolder {
		TextView tv_reward_time;
	}

}
