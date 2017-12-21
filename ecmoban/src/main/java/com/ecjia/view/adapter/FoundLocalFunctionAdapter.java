package com.ecjia.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.ECJiaApplication;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.ClassName;
import com.ecjia.view.activity.user.LoginActivity;
import com.ecjia.entity.responsemodel.FUNCTION;

import java.util.ArrayList;

/**
 * Created by Adam on 2016-03-14.
 */
public class FoundLocalFunctionAdapter extends ECJiaBaseAdapter<FUNCTION> {

    Activity mContext;
    AbsListView.LayoutParams params;
    int width;

    public FoundLocalFunctionAdapter(Activity context, ArrayList<FUNCTION> quickArrayList) {
        super(context, quickArrayList);
        this.mContext = context;
        width = mContext.getWindowManager().getDefaultDisplay().getWidth();
        params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.MATCH_PARENT);
        params.height = width / 3;
    }

    @Override
    protected ECJiaCellHolder createCellHolder(View cellView) {
        return null;
    }

    @Override
    protected View bindData(int position, View cellView, ViewGroup parent, ECJiaCellHolder h) {
        return null;
    }

    @Override
    public View createCellView() {
        return null;
    }

    @Override
    public int getCount() {

        int count = 0;
        if (dataList.size() > 0 && dataList.size() % 3 != 0) {
            count = ((dataList.size() / 3) + 1) * 3;
        } else {
            count = dataList.size();
        }

        return count;
    }

    @Override
    public FUNCTION getItem(int position) {

        if (position >= dataList.size()) {
            return dataList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (holder == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_foundadapter_item,
                    null);
            holder.item = convertView.findViewById(R.id.found_item);
            holder.image = (ImageView) convertView.findViewById(R.id.found_item_image);
            holder.name = (TextView) convertView.findViewById(R.id.found_item_name);
            holder.add = (ImageView) convertView.findViewById(R.id.found_item_add);
            holder.rightLine = convertView.findViewById(R.id.found_item_rightline);
            convertView.setLayoutParams(params);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (position >= dataList.size()) {

        } else {
            if (position != dataList.size()) {
                holder.add.setVisibility(View.GONE);
                holder.name.setVisibility(View.VISIBLE);
                holder.image.setVisibility(View.VISIBLE);
                holder.image.setImageResource(dataList.get(position).getIcon_colors());
                holder.name.setText(dataList.get(position).getName());
                holder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (dataList.get(position).isNeedLogin() && (((ECJiaApplication)
                                mContext.getApplication())
                                .getUser() == null || (TextUtils.isEmpty(((ECJiaApplication)
                                mContext.getApplication())
                                .getUser().getId())))) {
                            mContext.startActivityForResult(new Intent(mContext, LoginActivity.class),
                                    dataList.get(position).getLoginRequestcode());
                            mContext.overridePendingTransition(R.anim.push_buttom_in, R.anim
                                    .push_buttom_out);
                        } else {
                            try {
                                Intent intent = new Intent();
                                intent.setClass(mContext, Class.forName(dataList.get
                                        (position).getAction()
                                        .getActivityName()));
                                if (dataList.get(position).getAction().equals
                                        (ClassName.ActivityName.QRSHARE)) {
                                    intent.putExtra("startType", 1);
                                }
                                if (dataList.get(position).getCode().equals("promotion")) {
                                    intent.putExtra("type", "promotion");
                                }
                                mContext.startActivity(intent);
                                mContext.overridePendingTransition(R.anim.push_right_in,
                                        R.anim.push_right_out);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                if (position % 3 == 2) {
                    holder.rightLine.setVisibility(View.GONE);
                }
            } else {
                holder.add.setVisibility(View.VISIBLE);
                holder.name.setVisibility(View.GONE);
                holder.image.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    class ViewHolder {
        public View item;
        public TextView name;
        public ImageView image;
        public ImageView add;
        public View rightLine;
    }

}