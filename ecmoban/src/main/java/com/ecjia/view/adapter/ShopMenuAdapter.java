package com.ecjia.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.ShopDetialModel;
import com.ecjia.consts.AppConst;
import com.ecjia.entity.responsemodel.CATEGORY;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/5/21.
 */
public class ShopMenuAdapter extends BaseExpandableListAdapter {
    private Context c;
    private String[] parentdata;
    private ShopDetialModel model;
    private String predefine_category_id;
    private ArrayList<CATEGORY> catlist;

    public ShopMenuAdapter(Context c, String[] list,
                           String predefine_category_id, ShopDetialModel model,
                           ArrayList<CATEGORY> catlist) {
        this.c = c;
        this.parentdata = list;
        this.model = model;
        this.predefine_category_id = predefine_category_id;
        this.catlist = catlist;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (groupPosition == 0) {
            return model.brandList.get(childPosition);
        } else if (groupPosition == 1) {
            return catlist.get(childPosition);
        } else if (groupPosition == 2) {
            return model.priceRangeArrayList.get(childPosition);
        } else {
            return null;
        }
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(c).inflate(
                    R.layout.goodlist_child_item, null);
            holder.name = (TextView) convertView
                    .findViewById(R.id.goodlist_child_name);
            holder.child_buttomline = convertView
                    .findViewById(R.id.child_buttomline);
            holder.childitem = (LinearLayout) convertView
                    .findViewById(R.id.goodlist_child_item);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }

        if (groupPosition == 0) {
            holder.name.setText(model.brandList.get(childPosition).getBrand_name());
            holder.name.setTextColor(AppConst.brandlist.get(childPosition)
                    .get("flag") ? Color.RED : Color.GRAY);
            if (childPosition == model.brandList.size() - 1) {
                holder.child_buttomline.setVisibility(View.VISIBLE);
            } else {
                holder.child_buttomline.setVisibility(View.GONE);
            }
        } else if (groupPosition == 1) {
            holder.name.setText(catlist.get(childPosition).getName());
            holder.name.setTextColor(AppConst.categorylist.get(childPosition)
                    .get("flag") ? Color.RED : Color.GRAY);
            if (childPosition == catlist.size() - 1) {
                holder.child_buttomline.setVisibility(View.VISIBLE);
            } else {
                holder.child_buttomline.setVisibility(View.GONE);
            }
        } else if (groupPosition == 2) {
            holder.name
                    .setText(model.priceRangeArrayList.get(childPosition).getPrice_min()
                            + "-"
                            + model.priceRangeArrayList.get(childPosition).getPrice_max());
            holder.name.setTextColor(AppConst.pricelist.get(childPosition)
                    .get("flag") ? Color.RED : Color.GRAY);
            if (childPosition == model.priceRangeArrayList.size() - 1) {
                holder.child_buttomline.setVisibility(View.VISIBLE);
            } else {
                holder.child_buttomline.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == 0) {
            return model.brandList.size();
        } else if (groupPosition == 1) {
            return catlist.size();
        } else if (groupPosition == 2) {
            return model.priceRangeArrayList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentdata[groupPosition];
    }

    @Override
    public int getGroupCount() {
        return parentdata.length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ParentViewHolder holder = null;
        if (convertView == null) {
            holder = new ParentViewHolder();
            convertView = LayoutInflater.from(c).inflate(
                    R.layout.goodlist_parent_item, null);
            holder.filtername = (TextView) convertView
                    .findViewById(R.id.goodlist_parent_name);
            holder.showmore = (ImageView) convertView
                    .findViewById(R.id.goodlist_parent_rightarrow);
            holder.parent_item = (LinearLayout) convertView
                    .findViewById(R.id.goodlist_p_item);
            holder.selectname = (TextView) convertView
                    .findViewById(R.id.goodlist_parent_selected_name);
            convertView.setTag(holder);
        } else {
            holder = (ParentViewHolder) convertView.getTag();
        }
        if (groupPosition == 0) {
            if (model.brandList.size() > 0) {
                boolean isselect = false;
                for (int i = 0; i < AppConst.brandlist.size(); i++) {
                    if (AppConst.brandlist.get(i).get("flag")) {
                        holder.selectname
                                .setText(model.brandList.get(i).getBrand_name());
                        isselect = true;
                    }
                }
                if (!isselect) {
                    holder.selectname.setText("");
                }
                holder.parent_item.setVisibility(View.VISIBLE);
            } else {
                holder.parent_item.setVisibility(View.GONE);
            }
        } else if (groupPosition == 1) {
            if (catlist.size() > 0) {
                holder.parent_item.setVisibility(View.VISIBLE);
                boolean isselect = false;
                for (int i = 0; i < AppConst.categorylist.size(); i++) {
                    if (AppConst.categorylist.get(i).get("flag")) {
                        holder.selectname.setText(catlist.get(i).getName());
                        isselect = true;
                    }
                }
                if (!isselect) {
                    holder.selectname.setText("");
                }
            } else {
                holder.parent_item.setVisibility(View.GONE);
            }
        } else if (groupPosition == 2) {
            if (model.priceRangeArrayList.size() > 0) {
                holder.parent_item.setVisibility(View.VISIBLE);
                boolean isselect = false;
                for (int i = 0; i < AppConst.pricelist.size(); i++) {
                    if (AppConst.pricelist.get(i).get("flag")) {
                        holder.selectname.setText(model.priceRangeArrayList
                                .get(i).getPrice_min()
                                + "-"
                                + model.priceRangeArrayList.get(i).getPrice_max());
                        isselect = true;
                    }
                }
                if (!isselect) {
                    holder.selectname.setText("");
                }
            } else {
                holder.parent_item.setVisibility(View.GONE);
            }
        }

        holder.filtername.setText(parentdata[groupPosition]);
        if (isExpanded)// ture is Expanded or false is not isExpanded
            holder.showmore.setImageResource(R.drawable.search_hidden);
        else
            holder.showmore.setImageResource(R.drawable.search_showchild);
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

    class ParentViewHolder {
        TextView filtername;
        ImageView showmore;
        LinearLayout parent_item;
        TextView selectname;
    }

    class ChildViewHolder {
        TextView name;
        View child_buttomline;
        LinearLayout childitem;
    }
}
