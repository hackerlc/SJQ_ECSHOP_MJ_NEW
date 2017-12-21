package com.ecjia.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/7/22.
 */
public class PopwinParentAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<HashMap<String, Object>> itemList;
    private int slected;
    private int selectedbg=Color.parseColor("#dddddd");
    public PopwinParentAdapter(Context context,
                             ArrayList<HashMap<String, Object>> item,int selecteditem) {
        this.context = context;
        this.itemList = item;
        this.slected=selecteditem;
    }

    public void setSelectedItem(int selectedItem){
        slected=selectedItem;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.shoplist_popuplistitem, null);
            holder.item = (LinearLayout) convertView.findViewById(R.id.item_list_distance);
            holder.content = (TextView) convertView.findViewById(R.id.content_text);
            holder.item_top_line = convertView.findViewById(R.id.item_top_line);
            holder.is_selected = (ImageView) convertView.findViewById(R.id.item_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(slected==position){
            holder.item.setBackgroundColor(selectedbg);
        }else{
            holder.item.setBackgroundColor(Color.WHITE);
        }

        if(itemList.size()==1){
            holder.item_top_line.setVisibility(View.VISIBLE);
        }else{
            if(position==0) {
                holder.item_top_line.setVisibility(View.VISIBLE);
            }else{
                holder.item_top_line.setVisibility(View.GONE);
            }
        }

        holder.content.setText(itemList.get(position).get("name").toString());
        return convertView;
    }
    class ViewHolder {
        TextView content;
        ImageView is_selected;
        View item_top_line;
        LinearLayout item;
    }

}
