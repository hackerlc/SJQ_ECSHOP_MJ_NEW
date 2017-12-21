package com.ecjia.view.adapter;

import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.view.activity.GoodsListActivity;

import org.json.JSONException;

public class SearchHistoryAdapter extends BaseAdapter{
	public  List<String> list;
	private Context context;
	private LayoutInflater inflater;

	public SearchHistoryAdapter(List<String> list,Context context){
		this.list=list;
		this.context=context;
		this.inflater=LayoutInflater.from(context);
		
	}
	public void setListData(List<String> list){
		this.list=list;
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
		String search_list=list.get(position);
		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.search_history_item, null);
			holder.history_name=(TextView) convertView.findViewById(R.id.history_name);
			holder.search_item=(LinearLayout) convertView.findViewById(R.id.search_item);
			convertView.setTag(holder);
			
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.history_name.setText(search_list);
		holder.search_item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context, GoodsListActivity.class);
                try {
                    intent.putExtra("filter", new FILTER().toJson().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtra("keyword", list.get(position).toString());
				context.startActivity(intent);



			}
		});
		return convertView;
	}
	
	class ViewHolder{
		TextView history_name;
		LinearLayout search_item;
	}

}
