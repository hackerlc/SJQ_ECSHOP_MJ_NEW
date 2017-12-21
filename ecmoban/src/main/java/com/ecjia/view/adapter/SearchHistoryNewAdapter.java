package com.ecjia.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;

import java.util.List;

public class SearchHistoryNewAdapter extends BaseAdapter{
	public  List<String> list;
	private Context context;
	private LayoutInflater inflater;

	public SearchHistoryNewAdapter(List<String> list, Context context){
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
			convertView=inflater.inflate(R.layout.search_history_new_item, null);
			holder.history_name=(TextView) convertView.findViewById(R.id.history_name);
			holder.longline=(View) convertView.findViewById(R.id.longline);
			holder.shortline=(View) convertView.findViewById(R.id.shortline);
			convertView.setTag(holder);
			
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

        final int pos=list.size()-1;

        if(position==pos){
            holder.longline.setVisibility(View.VISIBLE);
            holder.shortline.setVisibility(View.GONE);
        }else{
            holder.longline.setVisibility(View.GONE);
            holder.shortline.setVisibility(View.VISIBLE);
        }

		holder.history_name.setText(search_list);
		return convertView;
	}
	
	class ViewHolder{
		TextView history_name;
        View longline,shortline;
	}

}
