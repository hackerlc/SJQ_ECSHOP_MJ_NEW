package com.ecjia.view.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.INVITE_RECORD;

import java.util.ArrayList;

public class MyRecordAdapter extends BaseAdapter {

	public ArrayList<INVITE_RECORD> list;
	private Context c;

	public MyRecordAdapter(Context c, ArrayList<INVITE_RECORD> list) {
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
					R.layout.my_record_item, null);

			holder.tv_my_reward = (TextView) convertView
					.findViewById(R.id.tv_my_reward);
			holder.tv_my_reward_time = (TextView) convertView
					.findViewById(R.id.tv_my_reward_time);
			holder.tv_my_reward_points = (TextView) convertView
					.findViewById(R.id.tv_my_reward_points);
			holder.endline = convertView.findViewById(R.id.end_line);
			holder.middleline = convertView.findViewById(R.id.middle_line);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}


		if (position == 0) {// 多项时的第一项
			holder.middleline.setVisibility(View.GONE);
		} else {// 中间项
			holder.middleline.setVisibility(View.VISIBLE);
		}

		if (position==list.size()-1){
			holder.endline.setVisibility(View.VISIBLE);
		}else{
			holder.endline.setVisibility(View.GONE);
		}

		holder.tv_my_reward.setText(list.get(position).getLabel_award_type());
		holder.tv_my_reward_time.setText(list.get(position).getAward_time());
		holder.tv_my_reward_points.setText(list.get(position).getGive_award());

		return convertView;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	class ViewHolder {
		TextView tv_my_reward,tv_my_reward_time,tv_my_reward_points;
		View middleline,endline;
	}

}
