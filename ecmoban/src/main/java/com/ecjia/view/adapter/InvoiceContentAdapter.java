package com.ecjia.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.entity.responsemodel.INV_CONTENT_LIST;
import com.ecmoban.android.sijiqing.R;

import java.util.List;

public class InvoiceContentAdapter extends BaseAdapter {

	private Context context;
    private List<INV_CONTENT_LIST> contentList;
    private LayoutInflater inflater;
	public int flag = -1;
    public String content;

	public InvoiceContentAdapter(Context context, List<INV_CONTENT_LIST> contentList, String content) {
		this.context = context;
        this.content = content;
        this.contentList = contentList;
		inflater = LayoutInflater.from(context);
		this.flag = -1;
	}



	@Override
	public int getCount() {
		return contentList.size();
	}

	@Override
	public Object getItem(int position) {
		return contentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.invoice_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.invoice_item_text);
			holder.select = (ImageView) convertView.findViewById(R.id.invoice_item_select);
			holder.top=convertView.findViewById(R.id.invoice_top);
			holder.buttom=convertView.findViewById(R.id.invoice_buttom);
			holder.middletop=convertView.findViewById(R.id.invoice_middle_top);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (contentList.size() == 1) {//当只有一项时
			holder.top.setVisibility(View.VISIBLE);
			holder.buttom.setVisibility(View.VISIBLE);
		} else {//多项时
			if (position == 0) {//多项时的第一项
				holder.top.setVisibility(View.VISIBLE);
				holder.buttom.setVisibility(View.GONE);
			}else if(position==contentList.size()-1){//多项时的最后一项
				holder.buttom.setVisibility(View.VISIBLE);
				holder.middletop.setVisibility(View.VISIBLE);
			}else {//中间项
				holder.middletop.setVisibility(View.VISIBLE);
			}
		}

		holder.name.setText(contentList.get(position).getValue());

		if(flag != -1) {
			if(flag == position) {
				holder.select.setBackgroundResource(R.drawable.payment_selected);
			} else {
				holder.select.setBackgroundResource(R.drawable.payment_unselected);
			}
		} else {
			if(!TextUtils.isEmpty(content)) {
				if(contentList.get(position).getValue().equals(content)) {
					holder.select.setBackgroundResource(R.drawable.payment_selected);
				} else {
					holder.select.setBackgroundResource(R.drawable.payment_unselected);
				}
				
			}else {
                holder.select.setBackgroundResource(R.drawable.payment_unselected);
            }
		}
		
		return convertView;
	}
	
	class ViewHolder {
		private TextView name;
		private ImageView select;
		private View top;
		private View buttom;
		private View middletop;
	}
}
