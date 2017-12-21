package com.ecjia.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.CATEGORY;

import java.util.ArrayList;

public class SearchLeftAdapter extends BaseAdapter {
	public ArrayList<CATEGORY> list;
	private Context context;
	private LayoutInflater inflater;

	public SearchLeftAdapter(ArrayList<CATEGORY> list, Context context){
		this.list=list;
		this.context=context;
		this.inflater= LayoutInflater.from(context);

	}
	public void setListData(ArrayList<CATEGORY> list){
		this.list=list;
	}

	@Override
	public int getCount() {
		if(list==null){
			return 0;
		}
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
		final CATEGORY category=list.get(position);
		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.search_left_item, null);
			holder.ll_item=(LinearLayout) convertView.findViewById(R.id.ll_item);
			holder.tv_choose=(TextView) convertView.findViewById(R.id.tv_name);
			holder.left_line=(View) convertView.findViewById(R.id.left_line);
			convertView.setTag(holder);

		}else{
			holder=(ViewHolder) convertView.getTag();
		}

		holder.tv_choose.setText(category.getName());

		if(category.isChoose()){
			holder.ll_item.setBackgroundColor(context.getResources().getColor(R.color.white));
			holder.tv_choose.setTextColor(context.getResources().getColor(R.color.public_theme_color_normal));
			holder.left_line.setVisibility(View.VISIBLE);
		}else{
			holder.ll_item.setBackgroundColor(context.getResources().getColor(R.color.color_gray));
			holder.tv_choose.setTextColor(context.getResources().getColor(R.color.filter_text_color));
			holder.left_line.setVisibility(View.INVISIBLE);
		}

		holder.ll_item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mListener != null) {
					mListener.onMyItemClick(view, position);
				}
			}
		});

		return convertView;
	}

	class ViewHolder{
		LinearLayout ll_item;
		TextView tv_choose;
		View left_line;
	}

	/**
	 * 单击事件监听器
	 */
	private onMyItemClickListener mListener = null;

	public void setOnMyItemClickListener(onMyItemClickListener listener) {
		mListener = listener;
	}

	public interface onMyItemClickListener {
		void onMyItemClick(View v, int position);
	}

}
