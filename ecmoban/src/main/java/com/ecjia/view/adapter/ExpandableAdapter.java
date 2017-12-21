package com.ecjia.view.adapter;

import java.util.ArrayList;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.CATEGORY;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableAdapter extends BaseExpandableListAdapter {
	private Context context;
	private ArrayList<CATEGORY> parentlist;

	public ExpandableAdapter(Context c, ArrayList<CATEGORY> Parentlist) {
		this.context = c;
		this.parentlist = Parentlist;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return parentlist.get(groupPosition).getChildren().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildHolder holder = null;
		if (convertView == null) {
			holder = new ChildHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.search_child_item, null);
			holder.childName = (TextView) convertView
					.findViewById(R.id.child_name);
			convertView.setTag(holder);
			holder.buttomline=convertView.findViewById(R.id.child_buttomline);
		}
		else {
			holder = (ChildHolder) convertView.getTag();
		}
		holder.childName.setText(parentlist.get(groupPosition).getChildren()
				.get(childPosition).getName());
		if(isLastChild){
			holder.buttomline.setVisibility(View.VISIBLE);
		}else{
			holder.buttomline.setVisibility(View.GONE);
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return parentlist.get(groupPosition).getChildren().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return parentlist.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return parentlist.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupHolder gholder = null;
		if (convertView == null) {
			gholder = new GroupHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.search_parent_item, null);
			gholder.title = (TextView) convertView
					.findViewById(R.id.search_parent_name);
			gholder.childrenNames = (TextView) convertView
					.findViewById(R.id.search_children_names);
			gholder.imageView = (ImageView) convertView
					.findViewById(R.id.search_parent_rightarrow);
			convertView.setTag(gholder);
		} else {
			gholder = (GroupHolder) convertView.getTag();
		}
		gholder.title.setText(parentlist.get(groupPosition).getName());
		String names = "";
		for (int i = 0; i < parentlist.get(groupPosition).getChildren().size(); i++) {
			if (i == parentlist.get(groupPosition).getChildren().size() - 1) {
				names += parentlist.get(groupPosition).getChildren().get(i).getName();
			} else {
				names += (parentlist.get(groupPosition).getChildren().get(i).getName() + "，");
			}
		}
		gholder.childrenNames.setText(names);
        if (isExpanded)// ture is Expanded or false is not isExpanded
            if (parentlist.get(groupPosition).getChildren().size() > 0) {
                gholder.imageView.setImageResource(R.drawable.search_hidden);
            } else {
                gholder.imageView.setImageResource(R.drawable.header_right_arrow);
            }
        else {
            if (parentlist.get(groupPosition).getChildren().size() > 0) {
                gholder.imageView.setImageResource(R.drawable.search_showchild);
            } else {
                gholder.imageView.setImageResource(R.drawable.header_right_arrow);
            }
        }
        return convertView;
    }

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}


	
	
	class GroupHolder {
		TextView title;
		ImageView imageView;
		TextView childrenNames;
	}

	class ChildHolder {
		TextView childName;
		View buttomline;
	}

}
