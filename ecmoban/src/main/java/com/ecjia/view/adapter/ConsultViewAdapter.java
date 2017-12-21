package com.ecjia.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.ECJiaApplication;
import com.ecjia.entity.responsemodel.CONSULTION;
import com.ecmoban.android.sijiqing.R;

import java.util.ArrayList;

public class ConsultViewAdapter extends BaseAdapter {

    public Bitmap profilePhoto;

    public static interface ConsultViewType {
        int FROM_BUSINESS = 0;
        int FROM_CUSTOM = 1;
    }

    private ArrayList<CONSULTION> consultionList;

    private Context ctx;
    private LayoutInflater mInflater;
    private ECJiaApplication mApp;

    public ConsultViewAdapter(Context context, ArrayList<CONSULTION> consultionList, Bitmap profilePhoto) {
        ctx = context;
        mApp = (ECJiaApplication) context.getApplicationContext();
        this.profilePhoto = profilePhoto;
        this.consultionList = consultionList;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return consultionList.size();
    }

    public Object getItem(int position) {
        return consultionList.get(consultionList.size() - 1 - position);
    }

    public long getItemId(int position) {
        return position;
    }


    public int getItemViewType(int position) {
        CONSULTION consultion = consultionList.get(consultionList.size() - 1 - position);
        if (1 == Integer.valueOf(consultion.getIs_myself())) {
            return ConsultViewType.FROM_CUSTOM;
        } else {
            return ConsultViewType.FROM_BUSINESS;
        }
    }

    public int getViewTypeCount() {
        return 2;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        if (consultionList.size() == 0) {
            return null;
        }
        CONSULTION consultion = consultionList.get(consultionList.size() - 1 - position);

        int isComMsg = Integer.valueOf(consultion.getIs_myself());
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.consult_item, null);
            viewHolder.layout_business = (LinearLayout) convertView.findViewById(R.id.consult_item_business);
            viewHolder.layout_custom = (LinearLayout) convertView.findViewById(R.id.consult_item_custom);
            viewHolder.tvContent_custom = (TextView) convertView.findViewById(R.id.tv_chatcontent_custom);
            viewHolder.iv_userhead_custom = (ImageView) convertView.findViewById(R.id.iv_userhead_custom);
            viewHolder.tvContent_business = (TextView) convertView.findViewById(R.id.tv_chatcontent_business);
            viewHolder.iv_userhead_business = (ImageView) convertView.findViewById(R.id.iv_userhead_business);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (isComMsg == 1) {
            viewHolder.layout_custom.setVisibility(View.VISIBLE);
            viewHolder.layout_business.setVisibility(View.GONE);
            viewHolder.tvContent_custom.setText(consultion.getContent());
            setUserHead(viewHolder.iv_userhead_custom, isComMsg);
        } else {
            viewHolder.layout_custom.setVisibility(View.GONE);
            viewHolder.layout_business.setVisibility(View.VISIBLE);
            viewHolder.tvContent_business.setText(consultion.getContent());
            setUserHead(viewHolder.iv_userhead_business, isComMsg);
        }
        return convertView;

    }

    static class ViewHolder {
        public TextView tvContent_business, tvContent_custom;
        public LinearLayout layout_business, layout_custom;
        public ImageView iv_userhead_business, iv_userhead_custom;
    }

    public void setUserHead(ImageView imageView, int type) {
        if (type == 1) {
            if (mApp.getUser() != null) {
                if (mApp.getUser().getAvatar_img() != null && profilePhoto != null) {
                    imageView.setImageBitmap(profilePhoto);
                } else {
                    imageView.setImageResource(R.drawable.profile_no_avarta_icon_light);
                }
            } else {
                imageView.setImageResource(R.drawable.profile_no_avarta_icon);
            }
        }
        if (type == 0) {
            imageView.setImageResource(R.drawable.ecmoban_logo);
        }
    }

}
