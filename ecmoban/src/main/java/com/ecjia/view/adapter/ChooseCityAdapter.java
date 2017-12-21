package com.ecjia.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.CITY;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/7/10.
 */
public class ChooseCityAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<CITY> list;
    final int VIEW_TYPE = 3;
    public String[] sections;//
    public HashMap<String, Integer> alphaIndexer;

    public ChooseCityAdapter(Context context, List<CITY> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        alphaIndexer = new HashMap<String, Integer>();
        sections = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            String currentStr = getAlpha(list.get(i).getPinyin());

            String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1)
                    .getPinyin()) : " ";
            if (!previewStr.equals(currentStr)) {
                String name = getAlpha(list.get(i).getPinyin());
                alphaIndexer.put(name, i);
                sections[i] = name;
            }
        }
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
    public int getViewTypeCount() {
        return VIEW_TYPE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.choosecity_list_item, null);
            holder = new ViewHolder();
            holder.alpha = (TextView) convertView
                    .findViewById(R.id.alpha);
            holder.name = (TextView) convertView
                    .findViewById(R.id.name);
            holder.top=convertView.findViewById(R.id.cityitem_top);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position).getName());
        String currentStr = getAlpha(list.get(position).getPinyin());
        String previewStr = (position - 1) >= 0 ? getAlpha(list
                .get(position - 1).getPinyin()) : " ";
        if (!previewStr.equals(currentStr)) {
            holder.alpha.setVisibility(View.VISIBLE);
            holder.alpha.setText(currentStr);
            holder.top.setVisibility(View.VISIBLE);
        } else {
            holder.alpha.setVisibility(View.GONE);
            holder.top.setVisibility(View.GONE);

        }

        return convertView;
    }

    private class ViewHolder {
        TextView alpha;
        TextView name;
        View top;
    }


    private String getAlpha(String str) {

        if (str.equals("-")) {
            return "&";
        }
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        char c = str.trim().substring(0, 1).charAt(0);
        // 匹配字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else {
            return "#";
        }
    }
}
