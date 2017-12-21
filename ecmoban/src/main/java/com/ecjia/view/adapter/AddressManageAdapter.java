package com.ecjia.view.adapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.AutoReturnView;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.activity.AddressManage2Activity;
import com.ecjia.entity.responsemodel.ADDRESS;

public class AddressManageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ADDRESS> list;
    private LayoutInflater inflater;
    public int a = 0;
    public Handler parentHandler;
    private int mRightWidth = 0;
    public static Map<Integer, Boolean> isSelected;
    private int flag;
    private StringBuffer sb;

    public AddressManageAdapter(Context context, ArrayList<ADDRESS> list, int flag, int RightViewWidth) {
        this.context = context;
        this.list = list;
        this.flag = flag;
        mRightWidth = RightViewWidth;
        inflater = LayoutInflater.from(context);
        sb = new StringBuffer();
        init(a);
    }

    private void init(int position) {
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < list.size(); i++) {
            if (i == position) {
                isSelected.put(i, true);
            } else {
                isSelected.put(i, false);
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
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        int count = list != null ? list.size() : 0;
        return count;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (sb.length() > 0) {
            sb.delete(0, sb.length());
        }
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.address_manage_item, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.address_manage_item_name);

            holder.detail = (AutoReturnView) convertView.findViewById(R.id.address_manage_item_detail);
            holder.line = convertView.findViewById(R.id.address_manage_lastline);
            holder.defaultview = convertView.findViewById(R.id.address_isdefault);
            holder.item_left = (LinearLayout) convertView.findViewById(R.id.item_left);
            holder.item_right = (LinearLayout) convertView.findViewById(R.id.item_right);
            holder.address_edit = (LinearLayout) convertView.findViewById(R.id.address_edit);
            holder.molile = (TextView) convertView.findViewById(R.id.address_mobile);
            holder.item_delete = (LinearLayout) convertView.findViewById(R.id.address_delete);
            holder.item_setdefault = (LinearLayout) convertView.findViewById(R.id.address_setdefault);
            holder.tv_isdefault = (TextView) convertView.findViewById(R.id.andress_isdefault);
            holder.address_view = convertView.findViewById(R.id.address_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LinearLayout.LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        holder.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);
        if (position == list.size() - 1) {
            holder.line.setVisibility(View.VISIBLE);
        }
        final ADDRESS address = list.get(position);

        if (address.getDefault_address() == 1) {
            init(position);
        }
        holder.name.setText(address.getConsignee());
        holder.molile.setText(address.getMobile());

        sb.append(address.getProvince_name());
        if (!address.getCity_name().equals("null")) {
            sb.append(address.getCity_name());
        }
        if (!address.getDistrict_name().equals("null")) {
            sb.append(address.getDistrict_name());
        }
        sb.append(address.getAddress());
        holder.detail.setContent(sb.toString());

        if (isSelected.get(position)) {
            holder.defaultview.setVisibility(View.VISIBLE);
            holder.tv_isdefault.setVisibility(View.VISIBLE);
            holder.address_view.setVisibility(View.GONE);
        } else {
            holder.defaultview.setVisibility(View.GONE);
            holder.tv_isdefault.setVisibility(View.GONE);
            holder.address_view.setVisibility(View.VISIBLE);
        }

        holder.item_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightItemClick(v, position);
                }
            }
        });
        holder.item_setdefault.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightItemClick(v, position);
                }
            }
        });


        holder.address_edit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddressManage2Activity.class);
                intent.putExtra(IntentKeywords.ADDRESS_ID, list.get(position).getId() + "");
                context.startActivity(intent);
            }
        });

        if (flag == 1) {
            holder.item_left.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.arg1 = list.get(position).getId();
                    parentHandler.sendMessage(msg);
                }
            });
        }

        return convertView;
    }

    class ViewHolder {
        private TextView name;
        private AutoReturnView detail;

        private View line, address_view;
        public View defaultview;
        LinearLayout item_left;
        LinearLayout item_right;
        LinearLayout item_delete, item_setdefault;
        private LinearLayout address_edit;
        private TextView tv_isdefault;
        private TextView molile;
    }

    /**
     * 单击事件监听器
     */
    private onRightItemClickListener mListener = null;

    public void setOnRightItemClickListener(onRightItemClickListener listener) {
        mListener = listener;
    }

    public interface onRightItemClickListener {
        void onRightItemClick(View v, int position);
    }

}
